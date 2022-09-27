package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageExposureFilter extends GPUImageFilter {
  public static final String EXPOSURE_FRAGMENT_SHADER = " varying highp vec2 textureCoordinate;\n \n uniform sampler2D inputImageTexture;\n uniform highp float exposure;\n \n void main()\n {\n     highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     \n     gl_FragColor = vec4(textureColor.rgb * pow(2.0, exposure), textureColor.w);\n } ";
  
  private float exposure;
  
  private int exposureLocation;
  
  public GPUImageExposureFilter() {
    this(1.0F);
  }
  
  public GPUImageExposureFilter(float paramFloat) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", " varying highp vec2 textureCoordinate;\n \n uniform sampler2D inputImageTexture;\n uniform highp float exposure;\n \n void main()\n {\n     highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     \n     gl_FragColor = vec4(textureColor.rgb * pow(2.0, exposure), textureColor.w);\n } ");
    this.exposure = paramFloat;
  }
  
  public void onInit() {
    super.onInit();
    this.exposureLocation = GLES20.glGetUniformLocation(getProgram(), "exposure");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setExposure(this.exposure);
  }
  
  public void setExposure(float paramFloat) {
    this.exposure = paramFloat;
    setFloat(this.exposureLocation, this.exposure);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageExposureFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */