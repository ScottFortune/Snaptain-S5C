package jp.co.cyberagent.android.gpuimage.filter;

public class GPUImageTwoPassFilter extends GPUImageFilterGroup {
  public GPUImageTwoPassFilter(String paramString1, String paramString2, String paramString3, String paramString4) {
    super(null);
    addFilter(new GPUImageFilter(paramString1, paramString2));
    addFilter(new GPUImageFilter(paramString3, paramString4));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageTwoPassFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */