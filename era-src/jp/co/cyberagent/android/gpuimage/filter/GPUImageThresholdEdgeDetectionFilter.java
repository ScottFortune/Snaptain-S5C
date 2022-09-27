package jp.co.cyberagent.android.gpuimage.filter;

public class GPUImageThresholdEdgeDetectionFilter extends GPUImageFilterGroup {
  public GPUImageThresholdEdgeDetectionFilter() {
    addFilter(new GPUImageGrayscaleFilter());
    addFilter(new GPUImageSobelThresholdFilter());
  }
  
  public void setLineSize(float paramFloat) {
    ((GPUImage3x3TextureSamplingFilter)getFilters().get(1)).setLineSize(paramFloat);
  }
  
  public void setThreshold(float paramFloat) {
    ((GPUImageSobelThresholdFilter)getFilters().get(1)).setThreshold(paramFloat);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageThresholdEdgeDetectionFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */