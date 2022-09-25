package androidx.appcompat.widget;

import android.os.Build;
import android.view.View;

public class TooltipCompat {
  public static void setTooltipText(View paramView, CharSequence paramCharSequence) {
    if (Build.VERSION.SDK_INT >= 26) {
      paramView.setTooltipText(paramCharSequence);
    } else {
      TooltipCompatHandler.setTooltipText(paramView, paramCharSequence);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/appcompat/widget/TooltipCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */