package androidx.appcompat.widget;

import android.graphics.Rect;

public interface FitWindowsViewGroup {
  void setOnFitSystemWindowsListener(OnFitSystemWindowsListener paramOnFitSystemWindowsListener);
  
  public static interface OnFitSystemWindowsListener {
    void onFitSystemWindows(Rect param1Rect);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/appcompat/widget/FitWindowsViewGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */