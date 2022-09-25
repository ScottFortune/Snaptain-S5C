package jp.co.cyberagent.android.gpuimage.filter;

public class GPUImageSmoothToonFilter extends GPUImageFilterGroup {
  private GPUImageGaussianBlurFilter blurFilter = new GPUImageGaussianBlurFilter();
  
  private GPUImageToonFilter toonFilter;
  
  public GPUImageSmoothToonFilter() {
    addFilter(this.blurFilter);
    this.toonFilter = new GPUImageToonFilter();
    addFilter(this.toonFilter);
    getFilters().add(this.blurFilter);
  }
  
  public void onInitialized() {
    super.onInitialized();
    setBlurSize(0.5F);
    setThreshold(0.2F);
    setQuantizationLevels(10.0F);
  }
  
  public void setBlurSize(float paramFloat) {
    this.blurFilter.setBlurSize(paramFloat);
  }
  
  public void setQuantizationLevels(float paramFloat) {
    this.toonFilter.setQuantizationLevels(paramFloat);
  }
  
  public void setTexelHeight(float paramFloat) {
    this.toonFilter.setTexelHeight(paramFloat);
  }
  
  public void setTexelWidth(float paramFloat) {
    this.toonFilter.setTexelWidth(paramFloat);
  }
  
  public void setThreshold(float paramFloat) {
    this.toonFilter.setThreshold(paramFloat);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageSmoothToonFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */