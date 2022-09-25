package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageHalftoneFilter extends GPUImageFilter {
  public static final String HALFTONE_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\nuniform sampler2D inputImageTexture;\nuniform highp float fractionalWidthOfPixel;\nuniform highp float aspectRatio;\nconst highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\nvoid main()\n{\n  highp vec2 sampleDivisor = vec2(fractionalWidthOfPixel, fractionalWidthOfPixel / aspectRatio);\n  highp vec2 samplePos = textureCoordinate - mod(textureCoordinate, sampleDivisor) + 0.5 * sampleDivisor;\n  highp vec2 textureCoordinateToUse = vec2(textureCoordinate.x, (textureCoordinate.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\n  highp vec2 adjustedSamplePos = vec2(samplePos.x, (samplePos.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\n  highp float distanceFromSamplePoint = distance(adjustedSamplePos, textureCoordinateToUse);\n  lowp vec3 sampledColor = texture2D(inputImageTexture, samplePos).rgb;\n  highp float dotScaling = 1.0 - dot(sampledColor, W);\n  lowp float checkForPresenceWithinDot = 1.0 - step(distanceFromSamplePoint, (fractionalWidthOfPixel * 0.5) * dotScaling);\n  gl_FragColor = vec4(vec3(checkForPresenceWithinDot), 1.0);\n}";
  
  private float aspectRatio;
  
  private int aspectRatioLocation;
  
  private float fractionalWidthOfAPixel;
  
  private int fractionalWidthOfPixelLocation;
  
  public GPUImageHalftoneFilter() {
    this(0.01F);
  }
  
  public GPUImageHalftoneFilter(float paramFloat) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "varying highp vec2 textureCoordinate;\nuniform sampler2D inputImageTexture;\nuniform highp float fractionalWidthOfPixel;\nuniform highp float aspectRatio;\nconst highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\nvoid main()\n{\n  highp vec2 sampleDivisor = vec2(fractionalWidthOfPixel, fractionalWidthOfPixel / aspectRatio);\n  highp vec2 samplePos = textureCoordinate - mod(textureCoordinate, sampleDivisor) + 0.5 * sampleDivisor;\n  highp vec2 textureCoordinateToUse = vec2(textureCoordinate.x, (textureCoordinate.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\n  highp vec2 adjustedSamplePos = vec2(samplePos.x, (samplePos.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\n  highp float distanceFromSamplePoint = distance(adjustedSamplePos, textureCoordinateToUse);\n  lowp vec3 sampledColor = texture2D(inputImageTexture, samplePos).rgb;\n  highp float dotScaling = 1.0 - dot(sampledColor, W);\n  lowp float checkForPresenceWithinDot = 1.0 - step(distanceFromSamplePoint, (fractionalWidthOfPixel * 0.5) * dotScaling);\n  gl_FragColor = vec4(vec3(checkForPresenceWithinDot), 1.0);\n}");
    this.fractionalWidthOfAPixel = paramFloat;
  }
  
  public void onInit() {
    super.onInit();
    this.fractionalWidthOfPixelLocation = GLES20.glGetUniformLocation(getProgram(), "fractionalWidthOfPixel");
    this.aspectRatioLocation = GLES20.glGetUniformLocation(getProgram(), "aspectRatio");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setFractionalWidthOfAPixel(this.fractionalWidthOfAPixel);
    setAspectRatio(this.aspectRatio);
  }
  
  public void onOutputSizeChanged(int paramInt1, int paramInt2) {
    super.onOutputSizeChanged(paramInt1, paramInt2);
    setAspectRatio(paramInt2 / paramInt1);
  }
  
  public void setAspectRatio(float paramFloat) {
    this.aspectRatio = paramFloat;
    setFloat(this.aspectRatioLocation, this.aspectRatio);
  }
  
  public void setFractionalWidthOfAPixel(float paramFloat) {
    this.fractionalWidthOfAPixel = paramFloat;
    setFloat(this.fractionalWidthOfPixelLocation, this.fractionalWidthOfAPixel);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageHalftoneFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */