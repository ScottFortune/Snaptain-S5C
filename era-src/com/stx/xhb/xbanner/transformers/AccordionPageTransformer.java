package com.stx.xhb.xbanner.transformers;

import android.view.View;

public class AccordionPageTransformer extends BasePageTransformer {
  public void handleInvisiblePage(View paramView, float paramFloat) {}
  
  public void handleLeftPage(View paramView, float paramFloat) {
    paramView.setPivotX(paramView.getWidth());
    paramView.setScaleX(paramFloat + 1.0F);
  }
  
  public void handleRightPage(View paramView, float paramFloat) {
    paramView.setPivotX(0.0F);
    paramView.setScaleX(1.0F - paramFloat);
    paramView.setAlpha(1.0F);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/stx/xhb/xbanner/transformers/AccordionPageTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */