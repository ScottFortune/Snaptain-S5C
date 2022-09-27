package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImagePosterizeFilter extends GPUImageFilter {
  public static final String POSTERIZE_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\nuniform highp float colorLevels;\n\nvoid main()\n{\n   highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n   \n   gl_FragColor = floor((textureColor * colorLevels) + vec4(0.5)) / colorLevels;\n}";
  
  private int colorLevels;
  
  private int glUniformColorLevels;
  
  public GPUImagePosterizeFilter() {
    this(10);
  }
  
  public GPUImagePosterizeFilter(int paramInt) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\nuniform highp float colorLevels;\n\nvoid main()\n{\n   highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n   \n   gl_FragColor = floor((textureColor * colorLevels) + vec4(0.5)) / colorLevels;\n}");
    this.colorLevels = paramInt;
  }
  
  public void onInit() {
    super.onInit();
    this.glUniformColorLevels = GLES20.glGetUniformLocation(getProgram(), "colorLevels");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setColorLevels(this.colorLevels);
  }
  
  public void setColorLevels(int paramInt) {
    this.colorLevels = paramInt;
    setFloat(this.glUniformColorLevels, paramInt);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImagePosterizeFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */