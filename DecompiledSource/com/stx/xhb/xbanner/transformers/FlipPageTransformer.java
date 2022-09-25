package com.stx.xhb.xbanner.transformers;

import android.view.View;

public class FlipPageTransformer extends BasePageTransformer {
  private static final float ROTATION = 180.0F;
  
  public void handleInvisiblePage(View paramView, float paramFloat) {}
  
  public void handleLeftPage(View paramView, float paramFloat) {
    paramView.setTranslationX(-paramView.getWidth() * paramFloat);
    paramView.setRotationY(180.0F * paramFloat);
    if (paramFloat > -0.5D) {
      paramView.setVisibility(0);
    } else {
      paramView.setVisibility(4);
    } 
  }
  
  public void handleRightPage(View paramView, float paramFloat) {
    paramView.setTranslationX(-paramView.getWidth() * paramFloat);
    paramView.setRotationY(180.0F * paramFloat);
    if (paramFloat < 0.5D) {
      paramView.setVisibility(0);
    } else {
      paramView.setVisibility(4);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/stx/xhb/xbanner/transformers/FlipPageTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */