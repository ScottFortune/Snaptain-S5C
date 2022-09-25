package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageChromaKeyBlendFilter extends GPUImageTwoInputFilter {
  public static final String CHROMA_KEY_BLEND_FRAGMENT_SHADER = " precision highp float;\n \n varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2;\n\n uniform float thresholdSensitivity;\n uniform float smoothing;\n uniform vec3 colorToReplace;\n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2;\n \n void main()\n {\n     vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n     \n     float maskY = 0.2989 * colorToReplace.r + 0.5866 * colorToReplace.g + 0.1145 * colorToReplace.b;\n     float maskCr = 0.7132 * (colorToReplace.r - maskY);\n     float maskCb = 0.5647 * (colorToReplace.b - maskY);\n     \n     float Y = 0.2989 * textureColor.r + 0.5866 * textureColor.g + 0.1145 * textureColor.b;\n     float Cr = 0.7132 * (textureColor.r - Y);\n     float Cb = 0.5647 * (textureColor.b - Y);\n     \n     float blendValue = 1.0 - smoothstep(thresholdSensitivity, thresholdSensitivity + smoothing, distance(vec2(Cr, Cb), vec2(maskCr, maskCb)));\n     gl_FragColor = mix(textureColor, textureColor2, blendValue);\n }";
  
  private float[] colorToReplace = new float[] { 0.0F, 1.0F, 0.0F };
  
  private int colorToReplaceLocation;
  
  private float smoothing = 0.1F;
  
  private int smoothingLocation;
  
  private float thresholdSensitivity = 0.4F;
  
  private int thresholdSensitivityLocation;
  
  public GPUImageChromaKeyBlendFilter() {
    super(" precision highp float;\n \n varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2;\n\n uniform float thresholdSensitivity;\n uniform float smoothing;\n uniform vec3 colorToReplace;\n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2;\n \n void main()\n {\n     vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n     \n     float maskY = 0.2989 * colorToReplace.r + 0.5866 * colorToReplace.g + 0.1145 * colorToReplace.b;\n     float maskCr = 0.7132 * (colorToReplace.r - maskY);\n     float maskCb = 0.5647 * (colorToReplace.b - maskY);\n     \n     float Y = 0.2989 * textureColor.r + 0.5866 * textureColor.g + 0.1145 * textureColor.b;\n     float Cr = 0.7132 * (textureColor.r - Y);\n     float Cb = 0.5647 * (textureColor.b - Y);\n     \n     float blendValue = 1.0 - smoothstep(thresholdSensitivity, thresholdSensitivity + smoothing, distance(vec2(Cr, Cb), vec2(maskCr, maskCb)));\n     gl_FragColor = mix(textureColor, textureColor2, blendValue);\n }");
  }
  
  public void onInit() {
    super.onInit();
    this.thresholdSensitivityLocation = GLES20.glGetUniformLocation(getProgram(), "thresholdSensitivity");
    this.smoothingLocation = GLES20.glGetUniformLocation(getProgram(), "smoothing");
    this.colorToReplaceLocation = GLES20.glGetUniformLocation(getProgram(), "colorToReplace");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setSmoothing(this.smoothing);
    setThresholdSensitivity(this.thresholdSensitivity);
    float[] arrayOfFloat = this.colorToReplace;
    setColorToReplace(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2]);
  }
  
  public void setColorToReplace(float paramFloat1, float paramFloat2, float paramFloat3) {
    this.colorToReplace = new float[] { paramFloat1, paramFloat2, paramFloat3 };
    setFloatVec3(this.colorToReplaceLocation, this.colorToReplace);
  }
  
  public void setSmoothing(float paramFloat) {
    this.smoothing = paramFloat;
    setFloat(this.smoothingLocation, this.smoothing);
  }
  
  public void setThresholdSensitivity(float paramFloat) {
    this.thresholdSensitivity = paramFloat;
    setFloat(this.thresholdSensitivityLocation, this.thresholdSensitivity);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageChromaKeyBlendFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */