package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageGammaFilter extends GPUImageFilter {
  public static final String GAMMA_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n \n uniform sampler2D inputImageTexture;\n uniform lowp float gamma;\n \n void main()\n {\n     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     \n     gl_FragColor = vec4(pow(textureColor.rgb, vec3(gamma)), textureColor.w);\n }";
  
  private float gamma;
  
  private int gammaLocation;
  
  public GPUImageGammaFilter() {
    this(1.2F);
  }
  
  public GPUImageGammaFilter(float paramFloat) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "varying highp vec2 textureCoordinate;\n \n uniform sampler2D inputImageTexture;\n uniform lowp float gamma;\n \n void main()\n {\n     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     \n     gl_FragColor = vec4(pow(textureColor.rgb, vec3(gamma)), textureColor.w);\n }");
    this.gamma = paramFloat;
  }
  
  public void onInit() {
    super.onInit();
    this.gammaLocation = GLES20.glGetUniformLocation(getProgram(), "gamma");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setGamma(this.gamma);
  }
  
  public void setGamma(float paramFloat) {
    this.gamma = paramFloat;
    setFloat(this.gammaLocation, this.gamma);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageGammaFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */