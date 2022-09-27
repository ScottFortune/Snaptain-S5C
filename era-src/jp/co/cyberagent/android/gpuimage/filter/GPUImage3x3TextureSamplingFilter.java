package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImage3x3TextureSamplingFilter extends GPUImageFilter {
  public static final String THREE_X_THREE_TEXTURE_SAMPLING_VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n\nuniform highp float texelWidth; \nuniform highp float texelHeight; \n\nvarying vec2 textureCoordinate;\nvarying vec2 leftTextureCoordinate;\nvarying vec2 rightTextureCoordinate;\n\nvarying vec2 topTextureCoordinate;\nvarying vec2 topLeftTextureCoordinate;\nvarying vec2 topRightTextureCoordinate;\n\nvarying vec2 bottomTextureCoordinate;\nvarying vec2 bottomLeftTextureCoordinate;\nvarying vec2 bottomRightTextureCoordinate;\n\nvoid main()\n{\n    gl_Position = position;\n\n    vec2 widthStep = vec2(texelWidth, 0.0);\n    vec2 heightStep = vec2(0.0, texelHeight);\n    vec2 widthHeightStep = vec2(texelWidth, texelHeight);\n    vec2 widthNegativeHeightStep = vec2(texelWidth, -texelHeight);\n\n    textureCoordinate = inputTextureCoordinate.xy;\n    leftTextureCoordinate = inputTextureCoordinate.xy - widthStep;\n    rightTextureCoordinate = inputTextureCoordinate.xy + widthStep;\n\n    topTextureCoordinate = inputTextureCoordinate.xy - heightStep;\n    topLeftTextureCoordinate = inputTextureCoordinate.xy - widthHeightStep;\n    topRightTextureCoordinate = inputTextureCoordinate.xy + widthNegativeHeightStep;\n\n    bottomTextureCoordinate = inputTextureCoordinate.xy + heightStep;\n    bottomLeftTextureCoordinate = inputTextureCoordinate.xy - widthNegativeHeightStep;\n    bottomRightTextureCoordinate = inputTextureCoordinate.xy + widthHeightStep;\n}";
  
  private boolean hasOverriddenImageSizeFactor = false;
  
  private float lineSize = 1.0F;
  
  private float texelHeight;
  
  private float texelWidth;
  
  private int uniformTexelHeightLocation;
  
  private int uniformTexelWidthLocation;
  
  public GPUImage3x3TextureSamplingFilter() {
    this("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}");
  }
  
  public GPUImage3x3TextureSamplingFilter(String paramString) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n\nuniform highp float texelWidth; \nuniform highp float texelHeight; \n\nvarying vec2 textureCoordinate;\nvarying vec2 leftTextureCoordinate;\nvarying vec2 rightTextureCoordinate;\n\nvarying vec2 topTextureCoordinate;\nvarying vec2 topLeftTextureCoordinate;\nvarying vec2 topRightTextureCoordinate;\n\nvarying vec2 bottomTextureCoordinate;\nvarying vec2 bottomLeftTextureCoordinate;\nvarying vec2 bottomRightTextureCoordinate;\n\nvoid main()\n{\n    gl_Position = position;\n\n    vec2 widthStep = vec2(texelWidth, 0.0);\n    vec2 heightStep = vec2(0.0, texelHeight);\n    vec2 widthHeightStep = vec2(texelWidth, texelHeight);\n    vec2 widthNegativeHeightStep = vec2(texelWidth, -texelHeight);\n\n    textureCoordinate = inputTextureCoordinate.xy;\n    leftTextureCoordinate = inputTextureCoordinate.xy - widthStep;\n    rightTextureCoordinate = inputTextureCoordinate.xy + widthStep;\n\n    topTextureCoordinate = inputTextureCoordinate.xy - heightStep;\n    topLeftTextureCoordinate = inputTextureCoordinate.xy - widthHeightStep;\n    topRightTextureCoordinate = inputTextureCoordinate.xy + widthNegativeHeightStep;\n\n    bottomTextureCoordinate = inputTextureCoordinate.xy + heightStep;\n    bottomLeftTextureCoordinate = inputTextureCoordinate.xy - widthNegativeHeightStep;\n    bottomRightTextureCoordinate = inputTextureCoordinate.xy + widthHeightStep;\n}", paramString);
  }
  
  private void updateTexelValues() {
    setFloat(this.uniformTexelWidthLocation, this.texelWidth);
    setFloat(this.uniformTexelHeightLocation, this.texelHeight);
  }
  
  public void onInit() {
    super.onInit();
    this.uniformTexelWidthLocation = GLES20.glGetUniformLocation(getProgram(), "texelWidth");
    this.uniformTexelHeightLocation = GLES20.glGetUniformLocation(getProgram(), "texelHeight");
  }
  
  public void onInitialized() {
    super.onInitialized();
    if (this.texelWidth != 0.0F)
      updateTexelValues(); 
  }
  
  public void onOutputSizeChanged(int paramInt1, int paramInt2) {
    super.onOutputSizeChanged(paramInt1, paramInt2);
    if (!this.hasOverriddenImageSizeFactor)
      setLineSize(this.lineSize); 
  }
  
  public void setLineSize(float paramFloat) {
    this.lineSize = paramFloat;
    this.texelWidth = paramFloat / getOutputWidth();
    this.texelHeight = paramFloat / getOutputHeight();
    updateTexelValues();
  }
  
  public void setTexelHeight(float paramFloat) {
    this.hasOverriddenImageSizeFactor = true;
    this.texelHeight = paramFloat;
    setFloat(this.uniformTexelHeightLocation, paramFloat);
  }
  
  public void setTexelWidth(float paramFloat) {
    this.hasOverriddenImageSizeFactor = true;
    this.texelWidth = paramFloat;
    setFloat(this.uniformTexelWidthLocation, paramFloat);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImage3x3TextureSamplingFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */