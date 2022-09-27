package com.stx.xhb.xbanner.transformers;

import android.view.View;

public class ZoomFadePageTransformer extends BasePageTransformer {
  public void handleInvisiblePage(View paramView, float paramFloat) {}
  
  public void handleLeftPage(View paramView, float paramFloat) {
    paramView.setTranslationX(-paramView.getWidth() * paramFloat);
    paramView.setPivotX(paramView.getWidth() * 0.5F);
    paramView.setPivotY(paramView.getHeight() * 0.5F);
    paramFloat++;
    paramView.setScaleX(paramFloat);
    paramView.setScaleY(paramFloat);
    paramView.setAlpha(paramFloat);
  }
  
  public void handleRightPage(View paramView, float paramFloat) {
    paramView.setTranslationX(-paramView.getWidth() * paramFloat);
    paramView.setPivotX(paramView.getWidth() * 0.5F);
    paramView.setPivotY(paramView.getHeight() * 0.5F);
    paramFloat = 1.0F - paramFloat;
    paramView.setScaleX(paramFloat);
    paramView.setScaleY(paramFloat);
    paramView.setAlpha(paramFloat);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/stx/xhb/xbanner/transformers/ZoomFadePageTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */