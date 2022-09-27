package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageOpacityFilter extends GPUImageFilter {
  public static final String OPACITY_FRAGMENT_SHADER = "  varying highp vec2 textureCoordinate;\n  \n  uniform sampler2D inputImageTexture;\n  uniform lowp float opacity;\n  \n  void main()\n  {\n      lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n      \n      gl_FragColor = vec4(textureColor.rgb, textureColor.a * opacity);\n  }\n";
  
  private float opacity;
  
  private int opacityLocation;
  
  public GPUImageOpacityFilter() {
    this(1.0F);
  }
  
  public GPUImageOpacityFilter(float paramFloat) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "  varying highp vec2 textureCoordinate;\n  \n  uniform sampler2D inputImageTexture;\n  uniform lowp float opacity;\n  \n  void main()\n  {\n      lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n      \n      gl_FragColor = vec4(textureColor.rgb, textureColor.a * opacity);\n  }\n");
    this.opacity = paramFloat;
  }
  
  public void onInit() {
    super.onInit();
    this.opacityLocation = GLES20.glGetUniformLocation(getProgram(), "opacity");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setOpacity(this.opacity);
  }
  
  public void setOpacity(float paramFloat) {
    this.opacity = paramFloat;
    setFloat(this.opacityLocation, this.opacity);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageOpacityFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */