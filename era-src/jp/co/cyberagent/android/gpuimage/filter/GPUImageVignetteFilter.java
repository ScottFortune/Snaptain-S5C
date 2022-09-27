package jp.co.cyberagent.android.gpuimage.filter;

import android.graphics.PointF;
import android.opengl.GLES20;

public class GPUImageVignetteFilter extends GPUImageFilter {
  public static final String VIGNETTING_FRAGMENT_SHADER = " uniform sampler2D inputImageTexture;\n varying highp vec2 textureCoordinate;\n \n uniform lowp vec2 vignetteCenter;\n uniform lowp vec3 vignetteColor;\n uniform highp float vignetteStart;\n uniform highp float vignetteEnd;\n \n void main()\n {\n     /*\n     lowp vec3 rgb = texture2D(inputImageTexture, textureCoordinate).rgb;\n     lowp float d = distance(textureCoordinate, vec2(0.5,0.5));\n     rgb *= (1.0 - smoothstep(vignetteStart, vignetteEnd, d));\n     gl_FragColor = vec4(vec3(rgb),1.0);\n      */\n     \n     lowp vec3 rgb = texture2D(inputImageTexture, textureCoordinate).rgb;\n     lowp float d = distance(textureCoordinate, vec2(vignetteCenter.x, vignetteCenter.y));\n     lowp float percent = smoothstep(vignetteStart, vignetteEnd, d);\n     gl_FragColor = vec4(mix(rgb.x, vignetteColor.x, percent), mix(rgb.y, vignetteColor.y, percent), mix(rgb.z, vignetteColor.z, percent), 1.0);\n }";
  
  private PointF vignetteCenter;
  
  private int vignetteCenterLocation;
  
  private float[] vignetteColor;
  
  private int vignetteColorLocation;
  
  private float vignetteEnd;
  
  private int vignetteEndLocation;
  
  private float vignetteStart;
  
  private int vignetteStartLocation;
  
  public GPUImageVignetteFilter() {
    this(new PointF(), new float[] { 0.0F, 0.0F, 0.0F }, 0.3F, 0.75F);
  }
  
  public GPUImageVignetteFilter(PointF paramPointF, float[] paramArrayOffloat, float paramFloat1, float paramFloat2) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", " uniform sampler2D inputImageTexture;\n varying highp vec2 textureCoordinate;\n \n uniform lowp vec2 vignetteCenter;\n uniform lowp vec3 vignetteColor;\n uniform highp float vignetteStart;\n uniform highp float vignetteEnd;\n \n void main()\n {\n     /*\n     lowp vec3 rgb = texture2D(inputImageTexture, textureCoordinate).rgb;\n     lowp float d = distance(textureCoordinate, vec2(0.5,0.5));\n     rgb *= (1.0 - smoothstep(vignetteStart, vignetteEnd, d));\n     gl_FragColor = vec4(vec3(rgb),1.0);\n      */\n     \n     lowp vec3 rgb = texture2D(inputImageTexture, textureCoordinate).rgb;\n     lowp float d = distance(textureCoordinate, vec2(vignetteCenter.x, vignetteCenter.y));\n     lowp float percent = smoothstep(vignetteStart, vignetteEnd, d);\n     gl_FragColor = vec4(mix(rgb.x, vignetteColor.x, percent), mix(rgb.y, vignetteColor.y, percent), mix(rgb.z, vignetteColor.z, percent), 1.0);\n }");
    this.vignetteCenter = paramPointF;
    this.vignetteColor = paramArrayOffloat;
    this.vignetteStart = paramFloat1;
    this.vignetteEnd = paramFloat2;
  }
  
  public void onInit() {
    super.onInit();
    this.vignetteCenterLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteCenter");
    this.vignetteColorLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteColor");
    this.vignetteStartLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteStart");
    this.vignetteEndLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteEnd");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setVignetteCenter(this.vignetteCenter);
    setVignetteColor(this.vignetteColor);
    setVignetteStart(this.vignetteStart);
    setVignetteEnd(this.vignetteEnd);
  }
  
  public void setVignetteCenter(PointF paramPointF) {
    this.vignetteCenter = paramPointF;
    setPoint(this.vignetteCenterLocation, this.vignetteCenter);
  }
  
  public void setVignetteColor(float[] paramArrayOffloat) {
    this.vignetteColor = paramArrayOffloat;
    setFloatVec3(this.vignetteColorLocation, this.vignetteColor);
  }
  
  public void setVignetteEnd(float paramFloat) {
    this.vignetteEnd = paramFloat;
    setFloat(this.vignetteEndLocation, this.vignetteEnd);
  }
  
  public void setVignetteStart(float paramFloat) {
    this.vignetteStart = paramFloat;
    setFloat(this.vignetteStartLocation, this.vignetteStart);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageVignetteFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */