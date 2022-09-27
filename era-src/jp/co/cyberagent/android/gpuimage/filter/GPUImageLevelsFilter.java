package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageLevelsFilter extends GPUImageFilter {
  public static final String LEVELS_FRAGMET_SHADER = " varying highp vec2 textureCoordinate;\n \n uniform sampler2D inputImageTexture;\n uniform mediump vec3 levelMinimum;\n uniform mediump vec3 levelMiddle;\n uniform mediump vec3 levelMaximum;\n uniform mediump vec3 minOutput;\n uniform mediump vec3 maxOutput;\n \n void main()\n {\n     mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     \n     gl_FragColor = vec4( mix(minOutput, maxOutput, pow(min(max(textureColor.rgb -levelMinimum, vec3(0.0)) / (levelMaximum - levelMinimum  ), vec3(1.0)), 1.0 /levelMiddle)) , textureColor.a);\n }\n";
  
  private static final String LOGTAG = GPUImageLevelsFilter.class.getSimpleName();
  
  private float[] max;
  
  private int maxLocation;
  
  private float[] maxOutput;
  
  private int maxOutputLocation;
  
  private float[] mid;
  
  private int midLocation;
  
  private float[] min;
  
  private int minLocation;
  
  private float[] minOutput;
  
  private int minOutputLocation;
  
  public GPUImageLevelsFilter() {
    this(new float[] { 0.0F, 0.0F, 0.0F }, new float[] { 1.0F, 1.0F, 1.0F }, new float[] { 1.0F, 1.0F, 1.0F }, new float[] { 0.0F, 0.0F, 0.0F }, new float[] { 1.0F, 1.0F, 1.0F });
  }
  
  private GPUImageLevelsFilter(float[] paramArrayOffloat1, float[] paramArrayOffloat2, float[] paramArrayOffloat3, float[] paramArrayOffloat4, float[] paramArrayOffloat5) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", " varying highp vec2 textureCoordinate;\n \n uniform sampler2D inputImageTexture;\n uniform mediump vec3 levelMinimum;\n uniform mediump vec3 levelMiddle;\n uniform mediump vec3 levelMaximum;\n uniform mediump vec3 minOutput;\n uniform mediump vec3 maxOutput;\n \n void main()\n {\n     mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     \n     gl_FragColor = vec4( mix(minOutput, maxOutput, pow(min(max(textureColor.rgb -levelMinimum, vec3(0.0)) / (levelMaximum - levelMinimum  ), vec3(1.0)), 1.0 /levelMiddle)) , textureColor.a);\n }\n");
    this.min = paramArrayOffloat1;
    this.mid = paramArrayOffloat2;
    this.max = paramArrayOffloat3;
    this.minOutput = paramArrayOffloat4;
    this.maxOutput = paramArrayOffloat5;
  }
  
  public void onInit() {
    super.onInit();
    this.minLocation = GLES20.glGetUniformLocation(getProgram(), "levelMinimum");
    this.midLocation = GLES20.glGetUniformLocation(getProgram(), "levelMiddle");
    this.maxLocation = GLES20.glGetUniformLocation(getProgram(), "levelMaximum");
    this.minOutputLocation = GLES20.glGetUniformLocation(getProgram(), "minOutput");
    this.maxOutputLocation = GLES20.glGetUniformLocation(getProgram(), "maxOutput");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setMin(0.0F, 1.0F, 1.0F, 0.0F, 1.0F);
    updateUniforms();
  }
  
  public void setBlueMin(float paramFloat1, float paramFloat2, float paramFloat3) {
    setBlueMin(paramFloat1, paramFloat2, paramFloat3, 0.0F, 1.0F);
  }
  
  public void setBlueMin(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {
    this.min[2] = paramFloat1;
    this.mid[2] = paramFloat2;
    this.max[2] = paramFloat3;
    this.minOutput[2] = paramFloat4;
    this.maxOutput[2] = paramFloat5;
    updateUniforms();
  }
  
  public void setGreenMin(float paramFloat1, float paramFloat2, float paramFloat3) {
    setGreenMin(paramFloat1, paramFloat2, paramFloat3, 0.0F, 1.0F);
  }
  
  public void setGreenMin(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {
    this.min[1] = paramFloat1;
    this.mid[1] = paramFloat2;
    this.max[1] = paramFloat3;
    this.minOutput[1] = paramFloat4;
    this.maxOutput[1] = paramFloat5;
    updateUniforms();
  }
  
  public void setMin(float paramFloat1, float paramFloat2, float paramFloat3) {
    setMin(paramFloat1, paramFloat2, paramFloat3, 0.0F, 1.0F);
  }
  
  public void setMin(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {
    setRedMin(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5);
    setGreenMin(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5);
    setBlueMin(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5);
  }
  
  public void setRedMin(float paramFloat1, float paramFloat2, float paramFloat3) {
    setRedMin(paramFloat1, paramFloat2, paramFloat3, 0.0F, 1.0F);
  }
  
  public void setRedMin(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {
    this.min[0] = paramFloat1;
    this.mid[0] = paramFloat2;
    this.max[0] = paramFloat3;
    this.minOutput[0] = paramFloat4;
    this.maxOutput[0] = paramFloat5;
    updateUniforms();
  }
  
  public void updateUniforms() {
    setFloatVec3(this.minLocation, this.min);
    setFloatVec3(this.midLocation, this.mid);
    setFloatVec3(this.maxLocation, this.max);
    setFloatVec3(this.minOutputLocation, this.minOutput);
    setFloatVec3(this.maxOutputLocation, this.maxOutput);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageLevelsFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */