package com.stx.xhb.xbanner.transformers;

import android.view.View;

public class ScalePageTransformer extends BasePageTransformer {
  private static final float MIN_SCALE = 0.9F;
  
  public void handleInvisiblePage(View paramView, float paramFloat) {
    paramView.setScaleY(0.9F);
  }
  
  public void handleLeftPage(View paramView, float paramFloat) {
    paramView.setScaleY(Math.max(0.9F, 1.0F - Math.abs(paramFloat)));
  }
  
  public void handleRightPage(View paramView, float paramFloat) {
    paramView.setScaleY(Math.max(0.9F, 1.0F - Math.abs(paramFloat)));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/stx/xhb/xbanner/transformers/ScalePageTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */