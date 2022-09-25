package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class FitWindowsFrameLayout extends FrameLayout implements FitWindowsViewGroup {
  private FitWindowsViewGroup.OnFitSystemWindowsListener mListener;
  
  public FitWindowsFrameLayout(Context paramContext) {
    super(paramContext);
  }
  
  public FitWindowsFrameLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  protected boolean fitSystemWindows(Rect paramRect) {
    FitWindowsViewGroup.OnFitSystemWindowsListener onFitSystemWindowsListener = this.mListener;
    if (onFitSystemWindowsListener != null)
      onFitSystemWindowsListener.onFitSystemWindows(paramRect); 
    return super.fitSystemWindows(paramRect);
  }
  
  public void setOnFitSystemWindowsListener(FitWindowsViewGroup.OnFitSystemWindowsListener paramOnFitSystemWindowsListener) {
    this.mListener = paramOnFitSystemWindowsListener;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/appcompat/widget/FitWindowsFrameLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */