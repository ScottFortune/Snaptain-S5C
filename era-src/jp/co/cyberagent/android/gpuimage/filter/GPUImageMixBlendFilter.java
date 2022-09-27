package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageMixBlendFilter extends GPUImageTwoInputFilter {
  private float mix;
  
  private int mixLocation;
  
  public GPUImageMixBlendFilter(String paramString) {
    this(paramString, 0.5F);
  }
  
  public GPUImageMixBlendFilter(String paramString, float paramFloat) {
    super(paramString);
    this.mix = paramFloat;
  }
  
  public void onInit() {
    super.onInit();
    this.mixLocation = GLES20.glGetUniformLocation(getProgram(), "mixturePercent");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setMix(this.mix);
  }
  
  public void setMix(float paramFloat) {
    this.mix = paramFloat;
    setFloat(this.mixLocation, this.mix);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageMixBlendFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */