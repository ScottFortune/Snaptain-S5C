package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageBilateralBlurFilter extends GPUImageFilter {
  public static final String BILATERAL_FRAGMENT_SHADER = "uniform sampler2D inputImageTexture;\n const lowp int GAUSSIAN_SAMPLES = 9;\n varying highp vec2 textureCoordinate;\n varying highp vec2 blurCoordinates[GAUSSIAN_SAMPLES];\n uniform mediump float distanceNormalizationFactor;\n void main()\n {\n     lowp vec4 centralColor;\n     lowp float gaussianWeightTotal;\n     lowp vec4 sum;\n     lowp vec4 sampleColor;\n     lowp float distanceFromCentralColor;\n     lowp float gaussianWeight;\n     \n     centralColor = texture2D(inputImageTexture, blurCoordinates[4]);\n     gaussianWeightTotal = 0.18;\n     sum = centralColor * 0.18;\n     \n     sampleColor = texture2D(inputImageTexture, blurCoordinates[0]);\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.05 * (1.0 - distanceFromCentralColor);\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\n     sampleColor = texture2D(inputImageTexture, blurCoordinates[1]);\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\n     sampleColor = texture2D(inputImageTexture, blurCoordinates[2]);\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.12 * (1.0 - distanceFromCentralColor);\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\n     sampleColor = texture2D(inputImageTexture, blurCoordinates[3]);\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.15 * (1.0 - distanceFromCentralColor);\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\n     sampleColor = texture2D(inputImageTexture, blurCoordinates[5]);\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.15 * (1.0 - distanceFromCentralColor);\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\n     sampleColor = texture2D(inputImageTexture, blurCoordinates[6]);\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.12 * (1.0 - distanceFromCentralColor);\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\n     sampleColor = texture2D(inputImageTexture, blurCoordinates[7]);\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\n     sampleColor = texture2D(inputImageTexture, blurCoordinates[8]);\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.05 * (1.0 - distanceFromCentralColor);\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\n     gl_FragColor = sum / gaussianWeightTotal;\n }";
  
  public static final String BILATERAL_VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nconst int GAUSSIAN_SAMPLES = 9;\nuniform vec2 singleStepOffset;\nvarying vec2 textureCoordinate;\nvarying vec2 blurCoordinates[GAUSSIAN_SAMPLES];\nvoid main()\n{\n\tgl_Position = position;\n\ttextureCoordinate = inputTextureCoordinate.xy;\n\tint multiplier = 0;\n\tvec2 blurStep;\n\tfor (int i = 0; i < GAUSSIAN_SAMPLES; i++)\n\t{\n\t\tmultiplier = (i - ((GAUSSIAN_SAMPLES - 1) / 2));\n\t\tblurStep = float(multiplier) * singleStepOffset;\n\t\tblurCoordinates[i] = inputTextureCoordinate.xy + blurStep;\n\t}\n}";
  
  private int disFactorLocation;
  
  private float distanceNormalizationFactor;
  
  private int singleStepOffsetLocation;
  
  public GPUImageBilateralBlurFilter() {
    this(8.0F);
  }
  
  public GPUImageBilateralBlurFilter(float paramFloat) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nconst int GAUSSIAN_SAMPLES = 9;\nuniform vec2 singleStepOffset;\nvarying vec2 textureCoordinate;\nvarying vec2 blurCoordinates[GAUSSIAN_SAMPLES];\nvoid main()\n{\n\tgl_Position = position;\n\ttextureCoordinate = inputTextureCoordinate.xy;\n\tint multiplier = 0;\n\tvec2 blurStep;\n\tfor (int i = 0; i < GAUSSIAN_SAMPLES; i++)\n\t{\n\t\tmultiplier = (i - ((GAUSSIAN_SAMPLES - 1) / 2));\n\t\tblurStep = float(multiplier) * singleStepOffset;\n\t\tblurCoordinates[i] = inputTextureCoordinate.xy + blurStep;\n\t}\n}", "uniform sampler2D inputImageTexture;\n const lowp int GAUSSIAN_SAMPLES = 9;\n varying highp vec2 textureCoordinate;\n varying highp vec2 blurCoordinates[GAUSSIAN_SAMPLES];\n uniform mediump float distanceNormalizationFactor;\n void main()\n {\n     lowp vec4 centralColor;\n     lowp float gaussianWeightTotal;\n     lowp vec4 sum;\n     lowp vec4 sampleColor;\n     lowp float distanceFromCentralColor;\n     lowp float gaussianWeight;\n     \n     centralColor = texture2D(inputImageTexture, blurCoordinates[4]);\n     gaussianWeightTotal = 0.18;\n     sum = centralColor * 0.18;\n     \n     sampleColor = texture2D(inputImageTexture, blurCoordinates[0]);\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.05 * (1.0 - distanceFromCentralColor);\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\n     sampleColor = texture2D(inputImageTexture, blurCoordinates[1]);\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\n     sampleColor = texture2D(inputImageTexture, blurCoordinates[2]);\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.12 * (1.0 - distanceFromCentralColor);\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\n     sampleColor = texture2D(inputImageTexture, blurCoordinates[3]);\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.15 * (1.0 - distanceFromCentralColor);\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\n     sampleColor = texture2D(inputImageTexture, blurCoordinates[5]);\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.15 * (1.0 - distanceFromCentralColor);\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\n     sampleColor = texture2D(inputImageTexture, blurCoordinates[6]);\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.12 * (1.0 - distanceFromCentralColor);\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\n     sampleColor = texture2D(inputImageTexture, blurCoordinates[7]);\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\n     sampleColor = texture2D(inputImageTexture, blurCoordinates[8]);\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.05 * (1.0 - distanceFromCentralColor);\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\n     gl_FragColor = sum / gaussianWeightTotal;\n }");
    this.distanceNormalizationFactor = paramFloat;
  }
  
  private void setTexelSize(float paramFloat1, float paramFloat2) {
    setFloatVec2(this.singleStepOffsetLocation, new float[] { 1.0F / paramFloat1, 1.0F / paramFloat2 });
  }
  
  public void onInit() {
    super.onInit();
    this.disFactorLocation = GLES20.glGetUniformLocation(getProgram(), "distanceNormalizationFactor");
    this.singleStepOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "singleStepOffset");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setDistanceNormalizationFactor(this.distanceNormalizationFactor);
  }
  
  public void onOutputSizeChanged(int paramInt1, int paramInt2) {
    super.onOutputSizeChanged(paramInt1, paramInt2);
    setTexelSize(paramInt1, paramInt2);
  }
  
  public void setDistanceNormalizationFactor(float paramFloat) {
    this.distanceNormalizationFactor = paramFloat;
    setFloat(this.disFactorLocation, paramFloat);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageBilateralBlurFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */