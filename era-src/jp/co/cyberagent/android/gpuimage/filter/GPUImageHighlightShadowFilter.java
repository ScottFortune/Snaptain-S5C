package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageHighlightShadowFilter extends GPUImageFilter {
  public static final String HIGHLIGHT_SHADOW_FRAGMENT_SHADER = " uniform sampler2D inputImageTexture;\n varying highp vec2 textureCoordinate;\n  \n uniform lowp float shadows;\n uniform lowp float highlights;\n \n const mediump vec3 luminanceWeighting = vec3(0.3, 0.3, 0.3);\n \n void main()\n {\n \tlowp vec4 source = texture2D(inputImageTexture, textureCoordinate);\n \tmediump float luminance = dot(source.rgb, luminanceWeighting);\n \n \tmediump float shadow = clamp((pow(luminance, 1.0/(shadows+1.0)) + (-0.76)*pow(luminance, 2.0/(shadows+1.0))) - luminance, 0.0, 1.0);\n \tmediump float highlight = clamp((1.0 - (pow(1.0-luminance, 1.0/(2.0-highlights)) + (-0.8)*pow(1.0-luminance, 2.0/(2.0-highlights)))) - luminance, -1.0, 0.0);\n \tlowp vec3 result = vec3(0.0, 0.0, 0.0) + ((luminance + shadow + highlight) - 0.0) * ((source.rgb - vec3(0.0, 0.0, 0.0))/(luminance - 0.0));\n \n \tgl_FragColor = vec4(result.rgb, source.a);\n }";
  
  private float highlights;
  
  private int highlightsLocation;
  
  private float shadows;
  
  private int shadowsLocation;
  
  public GPUImageHighlightShadowFilter() {
    this(0.0F, 1.0F);
  }
  
  public GPUImageHighlightShadowFilter(float paramFloat1, float paramFloat2) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", " uniform sampler2D inputImageTexture;\n varying highp vec2 textureCoordinate;\n  \n uniform lowp float shadows;\n uniform lowp float highlights;\n \n const mediump vec3 luminanceWeighting = vec3(0.3, 0.3, 0.3);\n \n void main()\n {\n \tlowp vec4 source = texture2D(inputImageTexture, textureCoordinate);\n \tmediump float luminance = dot(source.rgb, luminanceWeighting);\n \n \tmediump float shadow = clamp((pow(luminance, 1.0/(shadows+1.0)) + (-0.76)*pow(luminance, 2.0/(shadows+1.0))) - luminance, 0.0, 1.0);\n \tmediump float highlight = clamp((1.0 - (pow(1.0-luminance, 1.0/(2.0-highlights)) + (-0.8)*pow(1.0-luminance, 2.0/(2.0-highlights)))) - luminance, -1.0, 0.0);\n \tlowp vec3 result = vec3(0.0, 0.0, 0.0) + ((luminance + shadow + highlight) - 0.0) * ((source.rgb - vec3(0.0, 0.0, 0.0))/(luminance - 0.0));\n \n \tgl_FragColor = vec4(result.rgb, source.a);\n }");
    this.highlights = paramFloat2;
    this.shadows = paramFloat1;
  }
  
  public void onInit() {
    super.onInit();
    this.highlightsLocation = GLES20.glGetUniformLocation(getProgram(), "highlights");
    this.shadowsLocation = GLES20.glGetUniformLocation(getProgram(), "shadows");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setHighlights(this.highlights);
    setShadows(this.shadows);
  }
  
  public void setHighlights(float paramFloat) {
    this.highlights = paramFloat;
    setFloat(this.highlightsLocation, this.highlights);
  }
  
  public void setShadows(float paramFloat) {
    this.shadows = paramFloat;
    setFloat(this.shadowsLocation, this.shadows);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageHighlightShadowFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */