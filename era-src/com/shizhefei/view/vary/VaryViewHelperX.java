package com.shizhefei.view.vary;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class VaryViewHelperX implements IVaryViewHelper {
  private IVaryViewHelper helper;
  
  private View view;
  
  public VaryViewHelperX(View paramView) {
    this.view = paramView;
    ViewGroup viewGroup = (ViewGroup)paramView.getParent();
    ViewGroup.LayoutParams layoutParams2 = paramView.getLayoutParams();
    FrameLayout frameLayout = new FrameLayout(paramView.getContext());
    viewGroup.removeView(paramView);
    viewGroup.addView((View)frameLayout, layoutParams2);
    ViewGroup.LayoutParams layoutParams1 = new ViewGroup.LayoutParams(-1, -1);
    View view = new View(paramView.getContext());
    frameLayout.addView(paramView, layoutParams1);
    frameLayout.addView(view, layoutParams1);
    this.helper = new VaryViewHelper(view);
  }
  
  public Context getContext() {
    return this.helper.getContext();
  }
  
  public View getCurrentLayout() {
    return this.helper.getCurrentLayout();
  }
  
  public View getView() {
    return this.view;
  }
  
  public View inflate(int paramInt) {
    return this.helper.inflate(paramInt);
  }
  
  public void restoreView() {
    this.helper.restoreView();
  }
  
  public void showLayout(int paramInt) {
    showLayout(inflate(paramInt));
  }
  
  public void showLayout(View paramView) {
    this.helper.showLayout(paramView);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/view/vary/VaryViewHelperX.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */