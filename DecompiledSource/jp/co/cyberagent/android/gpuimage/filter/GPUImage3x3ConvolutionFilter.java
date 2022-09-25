package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImage3x3ConvolutionFilter extends GPUImage3x3TextureSamplingFilter {
  public static final String THREE_X_THREE_TEXTURE_SAMPLING_FRAGMENT_SHADER = "precision highp float;\n\nuniform sampler2D inputImageTexture;\n\nuniform mediump mat3 convolutionMatrix;\n\nvarying vec2 textureCoordinate;\nvarying vec2 leftTextureCoordinate;\nvarying vec2 rightTextureCoordinate;\n\nvarying vec2 topTextureCoordinate;\nvarying vec2 topLeftTextureCoordinate;\nvarying vec2 topRightTextureCoordinate;\n\nvarying vec2 bottomTextureCoordinate;\nvarying vec2 bottomLeftTextureCoordinate;\nvarying vec2 bottomRightTextureCoordinate;\n\nvoid main()\n{\n    mediump vec4 bottomColor = texture2D(inputImageTexture, bottomTextureCoordinate);\n    mediump vec4 bottomLeftColor = texture2D(inputImageTexture, bottomLeftTextureCoordinate);\n    mediump vec4 bottomRightColor = texture2D(inputImageTexture, bottomRightTextureCoordinate);\n    mediump vec4 centerColor = texture2D(inputImageTexture, textureCoordinate);\n    mediump vec4 leftColor = texture2D(inputImageTexture, leftTextureCoordinate);\n    mediump vec4 rightColor = texture2D(inputImageTexture, rightTextureCoordinate);\n    mediump vec4 topColor = texture2D(inputImageTexture, topTextureCoordinate);\n    mediump vec4 topRightColor = texture2D(inputImageTexture, topRightTextureCoordinate);\n    mediump vec4 topLeftColor = texture2D(inputImageTexture, topLeftTextureCoordinate);\n\n    mediump vec4 resultColor = topLeftColor * convolutionMatrix[0][0] + topColor * convolutionMatrix[0][1] + topRightColor * convolutionMatrix[0][2];\n    resultColor += leftColor * convolutionMatrix[1][0] + centerColor * convolutionMatrix[1][1] + rightColor * convolutionMatrix[1][2];\n    resultColor += bottomLeftColor * convolutionMatrix[2][0] + bottomColor * convolutionMatrix[2][1] + bottomRightColor * convolutionMatrix[2][2];\n\n    gl_FragColor = resultColor;\n}";
  
  private float[] convolutionKernel;
  
  private int uniformConvolutionMatrix;
  
  public GPUImage3x3ConvolutionFilter() {
    this(new float[] { 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F });
  }
  
  public GPUImage3x3ConvolutionFilter(float[] paramArrayOffloat) {
    super("precision highp float;\n\nuniform sampler2D inputImageTexture;\n\nuniform mediump mat3 convolutionMatrix;\n\nvarying vec2 textureCoordinate;\nvarying vec2 leftTextureCoordinate;\nvarying vec2 rightTextureCoordinate;\n\nvarying vec2 topTextureCoordinate;\nvarying vec2 topLeftTextureCoordinate;\nvarying vec2 topRightTextureCoordinate;\n\nvarying vec2 bottomTextureCoordinate;\nvarying vec2 bottomLeftTextureCoordinate;\nvarying vec2 bottomRightTextureCoordinate;\n\nvoid main()\n{\n    mediump vec4 bottomColor = texture2D(inputImageTexture, bottomTextureCoordinate);\n    mediump vec4 bottomLeftColor = texture2D(inputImageTexture, bottomLeftTextureCoordinate);\n    mediump vec4 bottomRightColor = texture2D(inputImageTexture, bottomRightTextureCoordinate);\n    mediump vec4 centerColor = texture2D(inputImageTexture, textureCoordinate);\n    mediump vec4 leftColor = texture2D(inputImageTexture, leftTextureCoordinate);\n    mediump vec4 rightColor = texture2D(inputImageTexture, rightTextureCoordinate);\n    mediump vec4 topColor = texture2D(inputImageTexture, topTextureCoordinate);\n    mediump vec4 topRightColor = texture2D(inputImageTexture, topRightTextureCoordinate);\n    mediump vec4 topLeftColor = texture2D(inputImageTexture, topLeftTextureCoordinate);\n\n    mediump vec4 resultColor = topLeftColor * convolutionMatrix[0][0] + topColor * convolutionMatrix[0][1] + topRightColor * convolutionMatrix[0][2];\n    resultColor += leftColor * convolutionMatrix[1][0] + centerColor * convolutionMatrix[1][1] + rightColor * convolutionMatrix[1][2];\n    resultColor += bottomLeftColor * convolutionMatrix[2][0] + bottomColor * convolutionMatrix[2][1] + bottomRightColor * convolutionMatrix[2][2];\n\n    gl_FragColor = resultColor;\n}");
    this.convolutionKernel = paramArrayOffloat;
  }
  
  public void onInit() {
    super.onInit();
    this.uniformConvolutionMatrix = GLES20.glGetUniformLocation(getProgram(), "convolutionMatrix");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setConvolutionKernel(this.convolutionKernel);
  }
  
  public void setConvolutionKernel(float[] paramArrayOffloat) {
    this.convolutionKernel = paramArrayOffloat;
    setUniformMatrix3f(this.uniformConvolutionMatrix, this.convolutionKernel);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImage3x3ConvolutionFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */