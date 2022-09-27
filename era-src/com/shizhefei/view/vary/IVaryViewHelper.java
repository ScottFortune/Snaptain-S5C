package com.shizhefei.view.vary;

import android.content.Context;
import android.view.View;

public interface IVaryViewHelper {
  Context getContext();
  
  View getCurrentLayout();
  
  View getView();
  
  View inflate(int paramInt);
  
  void restoreView();
  
  void showLayout(int paramInt);
  
  void showLayout(View paramView);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/view/vary/IVaryViewHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */