package com.shizhefei.view.vary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class VaryViewHelper implements IVaryViewHelper {
  private View currentView;
  
  private ViewGroup.LayoutParams originalParams;
  
  private View originalView;
  
  private ViewGroup parentView;
  
  private ViewGroup.LayoutParams selfParams;
  
  private int viewIndex;
  
  public VaryViewHelper(View paramView) {
    this.originalView = paramView;
  }
  
  public VaryViewHelper(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    this.originalView = paramView;
    this.selfParams = paramLayoutParams;
  }
  
  private void init() {
    this.originalParams = this.originalView.getLayoutParams();
    if (this.originalView.getParent() != null) {
      this.parentView = (ViewGroup)this.originalView.getParent();
    } else {
      this.parentView = (ViewGroup)this.originalView.getRootView().findViewById(16908290);
    } 
    int i = this.parentView.getChildCount();
    for (byte b = 0; b < i; b++) {
      if (this.originalView == this.parentView.getChildAt(b)) {
        this.viewIndex = b;
        break;
      } 
    } 
    this.currentView = this.originalView;
  }
  
  public Context getContext() {
    return this.originalView.getContext();
  }
  
  public View getCurrentLayout() {
    return this.currentView;
  }
  
  public View getView() {
    return this.originalView;
  }
  
  public View inflate(int paramInt) {
    return LayoutInflater.from(this.originalView.getContext()).inflate(paramInt, null);
  }
  
  public void restoreView() {
    showLayout(this.originalView);
  }
  
  public void showLayout(int paramInt) {
    showLayout(inflate(paramInt));
  }
  
  public void showLayout(View paramView) {
    if (this.parentView == null)
      init(); 
    this.currentView = paramView;
    if (this.parentView.getChildAt(this.viewIndex) != paramView) {
      ViewGroup viewGroup = (ViewGroup)paramView.getParent();
      if (viewGroup != null)
        viewGroup.removeView(paramView); 
      this.parentView.removeViewAt(this.viewIndex);
      if (paramView != this.originalView) {
        ViewGroup.LayoutParams layoutParams = this.selfParams;
        if (layoutParams != null) {
          this.parentView.addView(paramView, this.viewIndex, layoutParams);
          return;
        } 
      } 
      this.parentView.addView(paramView, this.viewIndex, this.originalParams);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/view/vary/VaryViewHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */