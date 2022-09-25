package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;
import android.opengl.Matrix;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GPUImageTransformFilter extends GPUImageFilter {
  public static final String TRANSFORM_VERTEX_SHADER = "attribute vec4 position;\n attribute vec4 inputTextureCoordinate;\n \n uniform mat4 transformMatrix;\n uniform mat4 orthographicMatrix;\n \n varying vec2 textureCoordinate;\n \n void main()\n {\n     gl_Position = transformMatrix * vec4(position.xyz, 1.0) * orthographicMatrix;\n     textureCoordinate = inputTextureCoordinate.xy;\n }";
  
  private boolean anchorTopLeft;
  
  private boolean ignoreAspectRatio;
  
  private float[] orthographicMatrix = new float[16];
  
  private int orthographicMatrixUniform;
  
  private float[] transform3D;
  
  private int transformMatrixUniform;
  
  public GPUImageTransformFilter() {
    super("attribute vec4 position;\n attribute vec4 inputTextureCoordinate;\n \n uniform mat4 transformMatrix;\n uniform mat4 orthographicMatrix;\n \n varying vec2 textureCoordinate;\n \n void main()\n {\n     gl_Position = transformMatrix * vec4(position.xyz, 1.0) * orthographicMatrix;\n     textureCoordinate = inputTextureCoordinate.xy;\n }", "varying highp vec2 textureCoordinate;\n \nuniform sampler2D inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}");
    Matrix.orthoM(this.orthographicMatrix, 0, -1.0F, 1.0F, -1.0F, 1.0F, -1.0F, 1.0F);
    this.transform3D = new float[16];
    Matrix.setIdentityM(this.transform3D, 0);
  }
  
  public boolean anchorTopLeft() {
    return this.anchorTopLeft;
  }
  
  public float[] getTransform3D() {
    return this.transform3D;
  }
  
  public boolean ignoreAspectRatio() {
    return this.ignoreAspectRatio;
  }
  
  public void onDraw(int paramInt, FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2) {
    FloatBuffer floatBuffer = paramFloatBuffer1;
    if (!this.ignoreAspectRatio) {
      float[] arrayOfFloat = new float[8];
      paramFloatBuffer1.position(0);
      paramFloatBuffer1.get(arrayOfFloat);
      float f = getOutputHeight() / getOutputWidth();
      arrayOfFloat[1] = arrayOfFloat[1] * f;
      arrayOfFloat[3] = arrayOfFloat[3] * f;
      arrayOfFloat[5] = arrayOfFloat[5] * f;
      arrayOfFloat[7] = arrayOfFloat[7] * f;
      floatBuffer = ByteBuffer.allocateDirect(arrayOfFloat.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
      floatBuffer.put(arrayOfFloat).position(0);
    } 
    super.onDraw(paramInt, floatBuffer, paramFloatBuffer2);
  }
  
  public void onInit() {
    super.onInit();
    this.transformMatrixUniform = GLES20.glGetUniformLocation(getProgram(), "transformMatrix");
    this.orthographicMatrixUniform = GLES20.glGetUniformLocation(getProgram(), "orthographicMatrix");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setUniformMatrix4f(this.transformMatrixUniform, this.transform3D);
    setUniformMatrix4f(this.orthographicMatrixUniform, this.orthographicMatrix);
  }
  
  public void onOutputSizeChanged(int paramInt1, int paramInt2) {
    super.onOutputSizeChanged(paramInt1, paramInt2);
    if (!this.ignoreAspectRatio) {
      float[] arrayOfFloat = this.orthographicMatrix;
      float f1 = paramInt2;
      float f2 = paramInt1;
      Matrix.orthoM(arrayOfFloat, 0, -1.0F, 1.0F, -1.0F * f1 / f2, f1 * 1.0F / f2, -1.0F, 1.0F);
      setUniformMatrix4f(this.orthographicMatrixUniform, this.orthographicMatrix);
    } 
  }
  
  public void setAnchorTopLeft(boolean paramBoolean) {
    this.anchorTopLeft = paramBoolean;
    setIgnoreAspectRatio(this.ignoreAspectRatio);
  }
  
  public void setIgnoreAspectRatio(boolean paramBoolean) {
    this.ignoreAspectRatio = paramBoolean;
    if (paramBoolean) {
      Matrix.orthoM(this.orthographicMatrix, 0, -1.0F, 1.0F, -1.0F, 1.0F, -1.0F, 1.0F);
      setUniformMatrix4f(this.orthographicMatrixUniform, this.orthographicMatrix);
    } else {
      onOutputSizeChanged(getOutputWidth(), getOutputHeight());
    } 
  }
  
  public void setTransform3D(float[] paramArrayOffloat) {
    this.transform3D = paramArrayOffloat;
    setUniformMatrix4f(this.transformMatrixUniform, paramArrayOffloat);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageTransformFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */