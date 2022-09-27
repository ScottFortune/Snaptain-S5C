package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageVibranceFilter extends GPUImageFilter {
  public static final String VIBRANCE_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\nuniform lowp float vibrance;\n\nvoid main() {\n    lowp vec4 color = texture2D(inputImageTexture, textureCoordinate);\n    lowp float average = (color.r + color.g + color.b) / 3.0;\n    lowp float mx = max(color.r, max(color.g, color.b));\n    lowp float amt = (mx - average) * (-vibrance * 3.0);\n    color.rgb = mix(color.rgb, vec3(mx), amt);\n    gl_FragColor = color;\n}";
  
  private float vibrance;
  
  private int vibranceLocation;
  
  public GPUImageVibranceFilter() {
    this(0.0F);
  }
  
  public GPUImageVibranceFilter(float paramFloat) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\nuniform lowp float vibrance;\n\nvoid main() {\n    lowp vec4 color = texture2D(inputImageTexture, textureCoordinate);\n    lowp float average = (color.r + color.g + color.b) / 3.0;\n    lowp float mx = max(color.r, max(color.g, color.b));\n    lowp float amt = (mx - average) * (-vibrance * 3.0);\n    color.rgb = mix(color.rgb, vec3(mx), amt);\n    gl_FragColor = color;\n}");
    this.vibrance = paramFloat;
  }
  
  public void onInit() {
    super.onInit();
    this.vibranceLocation = GLES20.glGetUniformLocation(getProgram(), "vibrance");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setVibrance(this.vibrance);
  }
  
  public void setVibrance(float paramFloat) {
    this.vibrance = paramFloat;
    if (isInitialized())
      setFloat(this.vibranceLocation, paramFloat); 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageVibranceFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */