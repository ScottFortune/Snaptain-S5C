package jp.co.cyberagent.android.gpuimage.filter;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import jp.co.cyberagent.android.gpuimage.util.OpenGlUtils;
import jp.co.cyberagent.android.gpuimage.util.Rotation;
import jp.co.cyberagent.android.gpuimage.util.TextureRotationUtil;

public class GPUImageTwoInputFilter extends GPUImageFilter {
  private static final String VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nattribute vec4 inputTextureCoordinate2;\n \nvarying vec2 textureCoordinate;\nvarying vec2 textureCoordinate2;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n    textureCoordinate2 = inputTextureCoordinate2.xy;\n}";
  
  private Bitmap bitmap;
  
  private int filterInputTextureUniform2;
  
  private int filterSecondTextureCoordinateAttribute;
  
  private int filterSourceTexture2 = -1;
  
  private ByteBuffer texture2CoordinatesBuffer;
  
  public GPUImageTwoInputFilter(String paramString) {
    this("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nattribute vec4 inputTextureCoordinate2;\n \nvarying vec2 textureCoordinate;\nvarying vec2 textureCoordinate2;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n    textureCoordinate2 = inputTextureCoordinate2.xy;\n}", paramString);
  }
  
  public GPUImageTwoInputFilter(String paramString1, String paramString2) {
    super(paramString1, paramString2);
    setRotation(Rotation.NORMAL, false, false);
  }
  
  public Bitmap getBitmap() {
    return this.bitmap;
  }
  
  public void onDestroy() {
    super.onDestroy();
    GLES20.glDeleteTextures(1, new int[] { this.filterSourceTexture2 }, 0);
    this.filterSourceTexture2 = -1;
  }
  
  protected void onDrawArraysPre() {
    GLES20.glEnableVertexAttribArray(this.filterSecondTextureCoordinateAttribute);
    GLES20.glActiveTexture(33987);
    GLES20.glBindTexture(3553, this.filterSourceTexture2);
    GLES20.glUniform1i(this.filterInputTextureUniform2, 3);
    this.texture2CoordinatesBuffer.position(0);
    GLES20.glVertexAttribPointer(this.filterSecondTextureCoordinateAttribute, 2, 5126, false, 0, this.texture2CoordinatesBuffer);
  }
  
  public void onInit() {
    super.onInit();
    this.filterSecondTextureCoordinateAttribute = GLES20.glGetAttribLocation(getProgram(), "inputTextureCoordinate2");
    this.filterInputTextureUniform2 = GLES20.glGetUniformLocation(getProgram(), "inputImageTexture2");
    GLES20.glEnableVertexAttribArray(this.filterSecondTextureCoordinateAttribute);
  }
  
  public void onInitialized() {
    super.onInitialized();
    Bitmap bitmap = this.bitmap;
    if (bitmap != null && !bitmap.isRecycled())
      setBitmap(this.bitmap); 
  }
  
  public void recycleBitmap() {
    Bitmap bitmap = this.bitmap;
    if (bitmap != null && !bitmap.isRecycled()) {
      this.bitmap.recycle();
      this.bitmap = null;
    } 
  }
  
  public void setBitmap(final Bitmap bitmap) {
    if (bitmap != null && bitmap.isRecycled())
      return; 
    this.bitmap = bitmap;
    if (this.bitmap == null)
      return; 
    runOnDraw(new Runnable() {
          public void run() {
            if (GPUImageTwoInputFilter.this.filterSourceTexture2 == -1) {
              Bitmap bitmap = bitmap;
              if (bitmap != null && !bitmap.isRecycled()) {
                GLES20.glActiveTexture(33987);
                GPUImageTwoInputFilter.access$002(GPUImageTwoInputFilter.this, OpenGlUtils.loadTexture(bitmap, -1, false));
              } 
            } 
          }
        });
  }
  
  public void setRotation(Rotation paramRotation, boolean paramBoolean1, boolean paramBoolean2) {
    float[] arrayOfFloat = TextureRotationUtil.getRotation(paramRotation, paramBoolean1, paramBoolean2);
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder());
    FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
    floatBuffer.put(arrayOfFloat);
    floatBuffer.flip();
    this.texture2CoordinatesBuffer = byteBuffer;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageTwoInputFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */