package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImagePixelationFilter extends GPUImageFilter {
  public static final String PIXELATION_FRAGMENT_SHADER = "precision highp float;\nvarying vec2 textureCoordinate;\nuniform float imageWidthFactor;\nuniform float imageHeightFactor;\nuniform sampler2D inputImageTexture;\nuniform float pixel;\nvoid main()\n{\n  vec2 uv  = textureCoordinate.xy;\n  float dx = pixel * imageWidthFactor;\n  float dy = pixel * imageHeightFactor;\n  vec2 coord = vec2(dx * floor(uv.x / dx), dy * floor(uv.y / dy));\n  vec3 tc = texture2D(inputImageTexture, coord).xyz;\n  gl_FragColor = vec4(tc, 1.0);\n}";
  
  private int imageHeightFactorLocation;
  
  private int imageWidthFactorLocation;
  
  private float pixel = 1.0F;
  
  private int pixelLocation;
  
  public GPUImagePixelationFilter() {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "precision highp float;\nvarying vec2 textureCoordinate;\nuniform float imageWidthFactor;\nuniform float imageHeightFactor;\nuniform sampler2D inputImageTexture;\nuniform float pixel;\nvoid main()\n{\n  vec2 uv  = textureCoordinate.xy;\n  float dx = pixel * imageWidthFactor;\n  float dy = pixel * imageHeightFactor;\n  vec2 coord = vec2(dx * floor(uv.x / dx), dy * floor(uv.y / dy));\n  vec3 tc = texture2D(inputImageTexture, coord).xyz;\n  gl_FragColor = vec4(tc, 1.0);\n}");
  }
  
  public void onInit() {
    super.onInit();
    this.imageWidthFactorLocation = GLES20.glGetUniformLocation(getProgram(), "imageWidthFactor");
    this.imageHeightFactorLocation = GLES20.glGetUniformLocation(getProgram(), "imageHeightFactor");
    this.pixelLocation = GLES20.glGetUniformLocation(getProgram(), "pixel");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setPixel(this.pixel);
  }
  
  public void onOutputSizeChanged(int paramInt1, int paramInt2) {
    super.onOutputSizeChanged(paramInt1, paramInt2);
    setFloat(this.imageWidthFactorLocation, 1.0F / paramInt1);
    setFloat(this.imageHeightFactorLocation, 1.0F / paramInt2);
  }
  
  public void setPixel(float paramFloat) {
    this.pixel = paramFloat;
    setFloat(this.pixelLocation, this.pixel);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImagePixelationFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */