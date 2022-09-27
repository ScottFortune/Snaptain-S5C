package com.stx.xhb.xbanner.transformers;

import android.view.View;

public class RotatePageTransformer extends BasePageTransformer {
  private float mMaxRotation = 15.0F;
  
  public RotatePageTransformer() {}
  
  public RotatePageTransformer(float paramFloat) {
    setMaxRotation(paramFloat);
  }
  
  public void handleInvisiblePage(View paramView, float paramFloat) {
    paramView.setPivotX(paramView.getMeasuredWidth() * 0.5F);
    paramView.setPivotY(paramView.getMeasuredHeight());
    paramView.setRotation(0.0F);
  }
  
  public void handleLeftPage(View paramView, float paramFloat) {
    float f = this.mMaxRotation;
    paramView.setPivotX(paramView.getMeasuredWidth() * 0.5F);
    paramView.setPivotY(paramView.getMeasuredHeight());
    paramView.setRotation(f * paramFloat);
  }
  
  public void handleRightPage(View paramView, float paramFloat) {
    handleLeftPage(paramView, paramFloat);
  }
  
  public void setMaxRotation(float paramFloat) {
    if (paramFloat >= 0.0F && paramFloat <= 40.0F)
      this.mMaxRotation = paramFloat; 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/stx/xhb/xbanner/transformers/RotatePageTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */