package com.shizhefei.view.vary;

import android.view.View;

public class VaryViewHelperXV extends VaryViewHelperX {
  private View view;
  
  public VaryViewHelperXV(View paramView) {
    super(paramView);
    this.view = paramView;
  }
  
  public void restoreView() {
    super.restoreView();
    this.view.setVisibility(0);
  }
  
  public void showLayout(int paramInt) {
    super.showLayout(paramInt);
    this.view.setVisibility(8);
  }
  
  public void showLayout(View paramView) {
    super.showLayout(paramView);
    this.view.setVisibility(8);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/view/vary/VaryViewHelperXV.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */