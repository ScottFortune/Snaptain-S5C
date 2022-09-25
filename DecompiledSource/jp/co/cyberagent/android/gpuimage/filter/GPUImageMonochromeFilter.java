package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageMonochromeFilter extends GPUImageFilter {
  public static final String MONOCHROME_FRAGMENT_SHADER = " precision lowp float;\n  \n  varying highp vec2 textureCoordinate;\n  \n  uniform sampler2D inputImageTexture;\n  uniform float intensity;\n  uniform vec3 filterColor;\n  \n  const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);\n  \n  void main()\n  {\n \t//desat, then apply overlay blend\n \tlowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n \tfloat luminance = dot(textureColor.rgb, luminanceWeighting);\n \t\n \tlowp vec4 desat = vec4(vec3(luminance), 1.0);\n \t\n \t//overlay\n \tlowp vec4 outputColor = vec4(\n                                  (desat.r < 0.5 ? (2.0 * desat.r * filterColor.r) : (1.0 - 2.0 * (1.0 - desat.r) * (1.0 - filterColor.r))),\n                                  (desat.g < 0.5 ? (2.0 * desat.g * filterColor.g) : (1.0 - 2.0 * (1.0 - desat.g) * (1.0 - filterColor.g))),\n                                  (desat.b < 0.5 ? (2.0 * desat.b * filterColor.b) : (1.0 - 2.0 * (1.0 - desat.b) * (1.0 - filterColor.b))),\n                                  1.0\n                                  );\n \t\n \t//which is better, or are they equal?\n \tgl_FragColor = vec4( mix(textureColor.rgb, outputColor.rgb, intensity), textureColor.a);\n  }";
  
  private float[] color;
  
  private int filterColorLocation;
  
  private float intensity;
  
  private int intensityLocation;
  
  public GPUImageMonochromeFilter() {
    this(1.0F, new float[] { 0.6F, 0.45F, 0.3F, 1.0F });
  }
  
  public GPUImageMonochromeFilter(float paramFloat, float[] paramArrayOffloat) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", " precision lowp float;\n  \n  varying highp vec2 textureCoordinate;\n  \n  uniform sampler2D inputImageTexture;\n  uniform float intensity;\n  uniform vec3 filterColor;\n  \n  const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);\n  \n  void main()\n  {\n \t//desat, then apply overlay blend\n \tlowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n \tfloat luminance = dot(textureColor.rgb, luminanceWeighting);\n \t\n \tlowp vec4 desat = vec4(vec3(luminance), 1.0);\n \t\n \t//overlay\n \tlowp vec4 outputColor = vec4(\n                                  (desat.r < 0.5 ? (2.0 * desat.r * filterColor.r) : (1.0 - 2.0 * (1.0 - desat.r) * (1.0 - filterColor.r))),\n                                  (desat.g < 0.5 ? (2.0 * desat.g * filterColor.g) : (1.0 - 2.0 * (1.0 - desat.g) * (1.0 - filterColor.g))),\n                                  (desat.b < 0.5 ? (2.0 * desat.b * filterColor.b) : (1.0 - 2.0 * (1.0 - desat.b) * (1.0 - filterColor.b))),\n                                  1.0\n                                  );\n \t\n \t//which is better, or are they equal?\n \tgl_FragColor = vec4( mix(textureColor.rgb, outputColor.rgb, intensity), textureColor.a);\n  }");
    this.intensity = paramFloat;
    this.color = paramArrayOffloat;
  }
  
  public void onInit() {
    super.onInit();
    this.intensityLocation = GLES20.glGetUniformLocation(getProgram(), "intensity");
    this.filterColorLocation = GLES20.glGetUniformLocation(getProgram(), "filterColor");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setIntensity(1.0F);
    setColor(new float[] { 0.6F, 0.45F, 0.3F, 1.0F });
  }
  
  public void setColor(float paramFloat1, float paramFloat2, float paramFloat3) {
    setFloatVec3(this.filterColorLocation, new float[] { paramFloat1, paramFloat2, paramFloat3 });
  }
  
  public void setColor(float[] paramArrayOffloat) {
    this.color = paramArrayOffloat;
    paramArrayOffloat = this.color;
    setColor(paramArrayOffloat[0], paramArrayOffloat[1], paramArrayOffloat[2]);
  }
  
  public void setIntensity(float paramFloat) {
    this.intensity = paramFloat;
    setFloat(this.intensityLocation, this.intensity);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageMonochromeFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */