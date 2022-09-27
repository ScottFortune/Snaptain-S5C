package jp.co.cyberagent.android.gpuimage.filter;

public class GPUImageEmbossFilter extends GPUImage3x3ConvolutionFilter {
  private float intensity;
  
  public GPUImageEmbossFilter() {
    this(1.0F);
  }
  
  public GPUImageEmbossFilter(float paramFloat) {
    this.intensity = paramFloat;
  }
  
  public float getIntensity() {
    return this.intensity;
  }
  
  public void onInit() {
    super.onInit();
  }
  
  public void onInitialized() {
    super.onInitialized();
    setIntensity(this.intensity);
  }
  
  public void setIntensity(float paramFloat) {
    this.intensity = paramFloat;
    float f = -paramFloat;
    setConvolutionKernel(new float[] { -2.0F * paramFloat, f, 0.0F, f, 1.0F, paramFloat, 0.0F, paramFloat, paramFloat * 2.0F });
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageEmbossFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */