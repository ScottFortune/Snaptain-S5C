package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageFalseColorFilter extends GPUImageFilter {
  public static final String FALSECOLOR_FRAGMENT_SHADER = "precision lowp float;\n\nvarying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\nuniform float intensity;\nuniform vec3 firstColor;\nuniform vec3 secondColor;\n\nconst mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);\n\nvoid main()\n{\nlowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\nfloat luminance = dot(textureColor.rgb, luminanceWeighting);\n\ngl_FragColor = vec4( mix(firstColor.rgb, secondColor.rgb, luminance), textureColor.a);\n}\n";
  
  private float[] firstColor;
  
  private int firstColorLocation;
  
  private float[] secondColor;
  
  private int secondColorLocation;
  
  public GPUImageFalseColorFilter() {
    this(0.0F, 0.0F, 0.5F, 1.0F, 0.0F, 0.0F);
  }
  
  public GPUImageFalseColorFilter(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6) {
    this(new float[] { paramFloat1, paramFloat2, paramFloat3 }, new float[] { paramFloat4, paramFloat5, paramFloat6 });
  }
  
  public GPUImageFalseColorFilter(float[] paramArrayOffloat1, float[] paramArrayOffloat2) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "precision lowp float;\n\nvarying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\nuniform float intensity;\nuniform vec3 firstColor;\nuniform vec3 secondColor;\n\nconst mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);\n\nvoid main()\n{\nlowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\nfloat luminance = dot(textureColor.rgb, luminanceWeighting);\n\ngl_FragColor = vec4( mix(firstColor.rgb, secondColor.rgb, luminance), textureColor.a);\n}\n");
    this.firstColor = paramArrayOffloat1;
    this.secondColor = paramArrayOffloat2;
  }
  
  public void onInit() {
    super.onInit();
    this.firstColorLocation = GLES20.glGetUniformLocation(getProgram(), "firstColor");
    this.secondColorLocation = GLES20.glGetUniformLocation(getProgram(), "secondColor");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setFirstColor(this.firstColor);
    setSecondColor(this.secondColor);
  }
  
  public void setFirstColor(float[] paramArrayOffloat) {
    this.firstColor = paramArrayOffloat;
    setFloatVec3(this.firstColorLocation, paramArrayOffloat);
  }
  
  public void setSecondColor(float[] paramArrayOffloat) {
    this.secondColor = paramArrayOffloat;
    setFloatVec3(this.secondColorLocation, paramArrayOffloat);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageFalseColorFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */