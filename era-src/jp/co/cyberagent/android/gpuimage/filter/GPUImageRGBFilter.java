package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageRGBFilter extends GPUImageFilter {
  public static final String RGB_FRAGMENT_SHADER = "  varying highp vec2 textureCoordinate;\n  \n  uniform sampler2D inputImageTexture;\n  uniform highp float red;\n  uniform highp float green;\n  uniform highp float blue;\n  \n  void main()\n  {\n      highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n      \n      gl_FragColor = vec4(textureColor.r * red, textureColor.g * green, textureColor.b * blue, 1.0);\n  }\n";
  
  private float blue;
  
  private int blueLocation;
  
  private float green;
  
  private int greenLocation;
  
  private float red;
  
  private int redLocation;
  
  public GPUImageRGBFilter() {
    this(1.0F, 1.0F, 1.0F);
  }
  
  public GPUImageRGBFilter(float paramFloat1, float paramFloat2, float paramFloat3) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "  varying highp vec2 textureCoordinate;\n  \n  uniform sampler2D inputImageTexture;\n  uniform highp float red;\n  uniform highp float green;\n  uniform highp float blue;\n  \n  void main()\n  {\n      highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n      \n      gl_FragColor = vec4(textureColor.r * red, textureColor.g * green, textureColor.b * blue, 1.0);\n  }\n");
    this.red = paramFloat1;
    this.green = paramFloat2;
    this.blue = paramFloat3;
  }
  
  public void onInit() {
    super.onInit();
    this.redLocation = GLES20.glGetUniformLocation(getProgram(), "red");
    this.greenLocation = GLES20.glGetUniformLocation(getProgram(), "green");
    this.blueLocation = GLES20.glGetUniformLocation(getProgram(), "blue");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setRed(this.red);
    setGreen(this.green);
    setBlue(this.blue);
  }
  
  public void setBlue(float paramFloat) {
    this.blue = paramFloat;
    setFloat(this.blueLocation, this.blue);
  }
  
  public void setGreen(float paramFloat) {
    this.green = paramFloat;
    setFloat(this.greenLocation, this.green);
  }
  
  public void setRed(float paramFloat) {
    this.red = paramFloat;
    setFloat(this.redLocation, this.red);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageRGBFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */