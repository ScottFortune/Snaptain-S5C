package com.stx.xhb.xbanner.transformers;

import android.view.View;

public class CubePageTransformer extends BasePageTransformer {
  private float mMaxRotation = 90.0F;
  
  public CubePageTransformer() {}
  
  public CubePageTransformer(float paramFloat) {
    setMaxRotation(paramFloat);
  }
  
  public void handleInvisiblePage(View paramView, float paramFloat) {
    paramView.setPivotX(paramView.getMeasuredWidth());
    paramView.setPivotY(paramView.getMeasuredHeight() * 0.5F);
    paramView.setRotationY(0.0F);
  }
  
  public void handleLeftPage(View paramView, float paramFloat) {
    paramView.setPivotX(paramView.getMeasuredWidth());
    paramView.setPivotY(paramView.getMeasuredHeight() * 0.5F);
    paramView.setRotationY(this.mMaxRotation * paramFloat);
  }
  
  public void handleRightPage(View paramView, float paramFloat) {
    paramView.setPivotX(0.0F);
    paramView.setPivotY(paramView.getMeasuredHeight() * 0.5F);
    paramView.setRotationY(this.mMaxRotation * paramFloat);
  }
  
  public void setMaxRotation(float paramFloat) {
    if (paramFloat >= 0.0F && paramFloat <= 90.0F)
      this.mMaxRotation = paramFloat; 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/stx/xhb/xbanner/transformers/CubePageTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */