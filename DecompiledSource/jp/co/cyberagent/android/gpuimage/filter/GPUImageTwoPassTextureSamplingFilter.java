package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageTwoPassTextureSamplingFilter extends GPUImageTwoPassFilter {
  public GPUImageTwoPassTextureSamplingFilter(String paramString1, String paramString2, String paramString3, String paramString4) {
    super(paramString1, paramString2, paramString3, paramString4);
  }
  
  public float getHorizontalTexelOffsetRatio() {
    return 1.0F;
  }
  
  public float getVerticalTexelOffsetRatio() {
    return 1.0F;
  }
  
  protected void initTexelOffsets() {
    float f = getHorizontalTexelOffsetRatio();
    GPUImageFilter gPUImageFilter = getFilters().get(0);
    int i = GLES20.glGetUniformLocation(gPUImageFilter.getProgram(), "texelWidthOffset");
    int j = GLES20.glGetUniformLocation(gPUImageFilter.getProgram(), "texelHeightOffset");
    gPUImageFilter.setFloat(i, f / getOutputWidth());
    gPUImageFilter.setFloat(j, 0.0F);
    f = getVerticalTexelOffsetRatio();
    gPUImageFilter = getFilters().get(1);
    i = GLES20.glGetUniformLocation(gPUImageFilter.getProgram(), "texelWidthOffset");
    j = GLES20.glGetUniformLocation(gPUImageFilter.getProgram(), "texelHeightOffset");
    gPUImageFilter.setFloat(i, 0.0F);
    gPUImageFilter.setFloat(j, f / getOutputHeight());
  }
  
  public void onInit() {
    super.onInit();
    initTexelOffsets();
  }
  
  public void onOutputSizeChanged(int paramInt1, int paramInt2) {
    super.onOutputSizeChanged(paramInt1, paramInt2);
    initTexelOffsets();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageTwoPassTextureSamplingFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */