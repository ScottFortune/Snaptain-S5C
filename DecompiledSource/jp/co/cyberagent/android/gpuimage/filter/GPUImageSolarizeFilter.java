package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageSolarizeFilter extends GPUImageFilter {
  public static final String SOLATIZE_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\nuniform highp float threshold;\n\nconst highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\n\nvoid main()\n{\n    highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n    highp float luminance = dot(textureColor.rgb, W);\n    highp float thresholdResult = step(luminance, threshold);\n    highp vec3 finalColor = abs(thresholdResult - textureColor.rgb);\n    \n    gl_FragColor = vec4(finalColor, textureColor.w);\n}";
  
  private float threshold;
  
  private int uniformThresholdLocation;
  
  public GPUImageSolarizeFilter() {
    this(0.5F);
  }
  
  public GPUImageSolarizeFilter(float paramFloat) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\nuniform highp float threshold;\n\nconst highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\n\nvoid main()\n{\n    highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n    highp float luminance = dot(textureColor.rgb, W);\n    highp float thresholdResult = step(luminance, threshold);\n    highp vec3 finalColor = abs(thresholdResult - textureColor.rgb);\n    \n    gl_FragColor = vec4(finalColor, textureColor.w);\n}");
    this.threshold = paramFloat;
  }
  
  public void onInit() {
    super.onInit();
    this.uniformThresholdLocation = GLES20.glGetUniformLocation(getProgram(), "threshold");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setThreshold(this.threshold);
  }
  
  public void setThreshold(float paramFloat) {
    this.threshold = paramFloat;
    setFloat(this.uniformThresholdLocation, paramFloat);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageSolarizeFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */