package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageLookupFilter extends GPUImageTwoInputFilter {
  public static final String LOOKUP_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2; // TODO: This is not used\n \n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2; // lookup texture\n \n uniform lowp float intensity;\n \n void main()\n {\n     highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     \n     highp float blueColor = textureColor.b * 63.0;\n     \n     highp vec2 quad1;\n     quad1.y = floor(floor(blueColor) / 8.0);\n     quad1.x = floor(blueColor) - (quad1.y * 8.0);\n     \n     highp vec2 quad2;\n     quad2.y = floor(ceil(blueColor) / 8.0);\n     quad2.x = ceil(blueColor) - (quad2.y * 8.0);\n     \n     highp vec2 texPos1;\n     texPos1.x = (quad1.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.r);\n     texPos1.y = (quad1.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.g);\n     \n     highp vec2 texPos2;\n     texPos2.x = (quad2.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.r);\n     texPos2.y = (quad2.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.g);\n     \n     lowp vec4 newColor1 = texture2D(inputImageTexture2, texPos1);\n     lowp vec4 newColor2 = texture2D(inputImageTexture2, texPos2);\n     \n     lowp vec4 newColor = mix(newColor1, newColor2, fract(blueColor));\n     gl_FragColor = mix(textureColor, vec4(newColor.rgb, textureColor.w), intensity);\n }";
  
  private float intensity;
  
  private int intensityLocation;
  
  public GPUImageLookupFilter() {
    this(1.0F);
  }
  
  public GPUImageLookupFilter(float paramFloat) {
    super("varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2; // TODO: This is not used\n \n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2; // lookup texture\n \n uniform lowp float intensity;\n \n void main()\n {\n     highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     \n     highp float blueColor = textureColor.b * 63.0;\n     \n     highp vec2 quad1;\n     quad1.y = floor(floor(blueColor) / 8.0);\n     quad1.x = floor(blueColor) - (quad1.y * 8.0);\n     \n     highp vec2 quad2;\n     quad2.y = floor(ceil(blueColor) / 8.0);\n     quad2.x = ceil(blueColor) - (quad2.y * 8.0);\n     \n     highp vec2 texPos1;\n     texPos1.x = (quad1.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.r);\n     texPos1.y = (quad1.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.g);\n     \n     highp vec2 texPos2;\n     texPos2.x = (quad2.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.r);\n     texPos2.y = (quad2.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.g);\n     \n     lowp vec4 newColor1 = texture2D(inputImageTexture2, texPos1);\n     lowp vec4 newColor2 = texture2D(inputImageTexture2, texPos2);\n     \n     lowp vec4 newColor = mix(newColor1, newColor2, fract(blueColor));\n     gl_FragColor = mix(textureColor, vec4(newColor.rgb, textureColor.w), intensity);\n }");
    this.intensity = paramFloat;
  }
  
  public void onInit() {
    super.onInit();
    this.intensityLocation = GLES20.glGetUniformLocation(getProgram(), "intensity");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setIntensity(this.intensity);
  }
  
  public void setIntensity(float paramFloat) {
    this.intensity = paramFloat;
    setFloat(this.intensityLocation, this.intensity);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageLookupFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */