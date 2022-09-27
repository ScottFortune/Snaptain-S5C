package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageContrastFilter extends GPUImageFilter {
  public static final String CONTRAST_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n \n uniform sampler2D inputImageTexture;\n uniform lowp float contrast;\n \n void main()\n {\n     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     \n     gl_FragColor = vec4(((textureColor.rgb - vec3(0.5)) * contrast + vec3(0.5)), textureColor.w);\n }";
  
  private float contrast;
  
  private int contrastLocation;
  
  public GPUImageContrastFilter() {
    this(1.2F);
  }
  
  public GPUImageContrastFilter(float paramFloat) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "varying highp vec2 textureCoordinate;\n \n uniform sampler2D inputImageTexture;\n uniform lowp float contrast;\n \n void main()\n {\n     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     \n     gl_FragColor = vec4(((textureColor.rgb - vec3(0.5)) * contrast + vec3(0.5)), textureColor.w);\n }");
    this.contrast = paramFloat;
  }
  
  public void onInit() {
    super.onInit();
    this.contrastLocation = GLES20.glGetUniformLocation(getProgram(), "contrast");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setContrast(this.contrast);
  }
  
  public void setContrast(float paramFloat) {
    this.contrast = paramFloat;
    setFloat(this.contrastLocation, this.contrast);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageContrastFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */