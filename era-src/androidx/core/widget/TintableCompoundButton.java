package androidx.core.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;

public interface TintableCompoundButton {
  ColorStateList getSupportButtonTintList();
  
  PorterDuff.Mode getSupportButtonTintMode();
  
  void setSupportButtonTintList(ColorStateList paramColorStateList);
  
  void setSupportButtonTintMode(PorterDuff.Mode paramMode);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/core/widget/TintableCompoundButton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */