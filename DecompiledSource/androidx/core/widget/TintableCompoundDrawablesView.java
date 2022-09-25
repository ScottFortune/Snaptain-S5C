package androidx.core.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;

public interface TintableCompoundDrawablesView {
  ColorStateList getSupportCompoundDrawablesTintList();
  
  PorterDuff.Mode getSupportCompoundDrawablesTintMode();
  
  void setSupportCompoundDrawablesTintList(ColorStateList paramColorStateList);
  
  void setSupportCompoundDrawablesTintMode(PorterDuff.Mode paramMode);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/core/widget/TintableCompoundDrawablesView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */