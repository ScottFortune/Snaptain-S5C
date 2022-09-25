package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageBrightnessFilter extends GPUImageFilter {
  public static final String BRIGHTNESS_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n \n uniform sampler2D inputImageTexture;\n uniform lowp float brightness;\n \n void main()\n {\n     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     \n     gl_FragColor = vec4((textureColor.rgb + vec3(brightness)), textureColor.w);\n }";
  
  private float brightness;
  
  private int brightnessLocation;
  
  public GPUImageBrightnessFilter() {
    this(0.0F);
  }
  
  public GPUImageBrightnessFilter(float paramFloat) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "varying highp vec2 textureCoordinate;\n \n uniform sampler2D inputImageTexture;\n uniform lowp float brightness;\n \n void main()\n {\n     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     \n     gl_FragColor = vec4((textureColor.rgb + vec3(brightness)), textureColor.w);\n }");
    this.brightness = paramFloat;
  }
  
  public void onInit() {
    super.onInit();
    this.brightnessLocation = GLES20.glGetUniformLocation(getProgram(), "brightness");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setBrightness(this.brightness);
  }
  
  public void setBrightness(float paramFloat) {
    this.brightness = paramFloat;
    setFloat(this.brightnessLocation, this.brightness);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageBrightnessFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */