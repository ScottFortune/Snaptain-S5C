package jp.co.cyberagent.android.gpuimage.filter;

public class GPUImageSoftLightBlendFilter extends GPUImageTwoInputFilter {
  public static final String SOFT_LIGHT_BLEND_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2;\n\n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2;\n \n void main()\n {\n     mediump vec4 base = texture2D(inputImageTexture, textureCoordinate);\n     mediump vec4 overlay = texture2D(inputImageTexture2, textureCoordinate2);\n     \n     gl_FragColor = base * (overlay.a * (base / base.a) + (2.0 * overlay * (1.0 - (base / base.a)))) + overlay * (1.0 - base.a) + base * (1.0 - overlay.a);\n }";
  
  public GPUImageSoftLightBlendFilter() {
    super("varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2;\n\n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2;\n \n void main()\n {\n     mediump vec4 base = texture2D(inputImageTexture, textureCoordinate);\n     mediump vec4 overlay = texture2D(inputImageTexture2, textureCoordinate2);\n     \n     gl_FragColor = base * (overlay.a * (base / base.a) + (2.0 * overlay * (1.0 - (base / base.a)))) + overlay * (1.0 - base.a) + base * (1.0 - overlay.a);\n }");
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageSoftLightBlendFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */