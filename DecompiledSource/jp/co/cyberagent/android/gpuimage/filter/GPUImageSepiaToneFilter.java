package jp.co.cyberagent.android.gpuimage.filter;

public class GPUImageSepiaToneFilter extends GPUImageColorMatrixFilter {
  public GPUImageSepiaToneFilter() {
    this(1.0F);
  }
  
  public GPUImageSepiaToneFilter(float paramFloat) {
    super(paramFloat, new float[] { 
          0.3588F, 0.7044F, 0.1368F, 0.0F, 0.299F, 0.587F, 0.114F, 0.0F, 0.2392F, 0.4696F, 
          0.0912F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F });
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageSepiaToneFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */