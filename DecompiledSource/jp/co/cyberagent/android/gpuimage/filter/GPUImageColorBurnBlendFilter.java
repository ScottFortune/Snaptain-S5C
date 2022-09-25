package jp.co.cyberagent.android.gpuimage.filter;

public class GPUImageColorBurnBlendFilter extends GPUImageTwoInputFilter {
  public static final String COLOR_BURN_BLEND_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2;\n\n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2;\n \n void main()\n {\n    mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n    mediump vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n    mediump vec4 whiteColor = vec4(1.0);\n    gl_FragColor = whiteColor - (whiteColor - textureColor) / textureColor2;\n }";
  
  public GPUImageColorBurnBlendFilter() {
    super("varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2;\n\n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2;\n \n void main()\n {\n    mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n    mediump vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n    mediump vec4 whiteColor = vec4(1.0);\n    gl_FragColor = whiteColor - (whiteColor - textureColor) / textureColor2;\n }");
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageColorBurnBlendFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */