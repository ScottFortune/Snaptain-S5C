package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageColorMatrixFilter extends GPUImageFilter {
  public static final String COLOR_MATRIX_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nuniform lowp mat4 colorMatrix;\nuniform lowp float intensity;\n\nvoid main()\n{\n    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n    lowp vec4 outputColor = textureColor * colorMatrix;\n    \n    gl_FragColor = (intensity * outputColor) + ((1.0 - intensity) * textureColor);\n}";
  
  private float[] colorMatrix;
  
  private int colorMatrixLocation;
  
  private float intensity;
  
  private int intensityLocation;
  
  public GPUImageColorMatrixFilter() {
    this(1.0F, new float[] { 
          1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 
          1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F });
  }
  
  public GPUImageColorMatrixFilter(float paramFloat, float[] paramArrayOffloat) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nuniform lowp mat4 colorMatrix;\nuniform lowp float intensity;\n\nvoid main()\n{\n    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n    lowp vec4 outputColor = textureColor * colorMatrix;\n    \n    gl_FragColor = (intensity * outputColor) + ((1.0 - intensity) * textureColor);\n}");
    this.intensity = paramFloat;
    this.colorMatrix = paramArrayOffloat;
  }
  
  public void onInit() {
    super.onInit();
    this.colorMatrixLocation = GLES20.glGetUniformLocation(getProgram(), "colorMatrix");
    this.intensityLocation = GLES20.glGetUniformLocation(getProgram(), "intensity");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setIntensity(this.intensity);
    setColorMatrix(this.colorMatrix);
  }
  
  public void setColorMatrix(float[] paramArrayOffloat) {
    this.colorMatrix = paramArrayOffloat;
    setUniformMatrix4f(this.colorMatrixLocation, paramArrayOffloat);
  }
  
  public void setIntensity(float paramFloat) {
    this.intensity = paramFloat;
    setFloat(this.intensityLocation, paramFloat);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageColorMatrixFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */