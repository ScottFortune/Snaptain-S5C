package androidx.core.widget;

import android.os.Build;

public interface AutoSizeableTextView {
  public static final boolean PLATFORM_SUPPORTS_AUTOSIZE;
  
  static {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 27) {
      bool = true;
    } else {
      bool = false;
    } 
    PLATFORM_SUPPORTS_AUTOSIZE = bool;
  }
  
  int getAutoSizeMaxTextSize();
  
  int getAutoSizeMinTextSize();
  
  int getAutoSizeStepGranularity();
  
  int[] getAutoSizeTextAvailableSizes();
  
  int getAutoSizeTextType();
  
  void setAutoSizeTextTypeUniformWithConfiguration(int paramInt1, int paramInt2, int paramInt3, int paramInt4) throws IllegalArgumentException;
  
  void setAutoSizeTextTypeUniformWithPresetSizes(int[] paramArrayOfint, int paramInt) throws IllegalArgumentException;
  
  void setAutoSizeTextTypeWithDefaults(int paramInt);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/core/widget/AutoSizeableTextView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */