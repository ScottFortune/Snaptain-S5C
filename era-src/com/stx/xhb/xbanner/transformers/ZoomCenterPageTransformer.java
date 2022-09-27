package com.stx.xhb.xbanner.transformers;

import android.view.View;
import androidx.core.view.ViewCompat;

public class ZoomCenterPageTransformer extends BasePageTransformer {
  public void handleInvisiblePage(View paramView, float paramFloat) {}
  
  public void handleLeftPage(View paramView, float paramFloat) {
    paramView.setTranslationX(-paramView.getWidth() * paramFloat);
    paramView.setPivotX(paramView.getWidth() * 0.5F);
    paramView.setPivotY(paramView.getHeight() * 0.5F);
    float f = paramFloat + 1.0F;
    paramView.setScaleX(f);
    paramView.setScaleY(f);
    if (paramFloat < -0.95F) {
      paramView.setAlpha(0.0F);
    } else {
      paramView.setAlpha(1.0F);
    } 
  }
  
  public void handleRightPage(View paramView, float paramFloat) {
    paramView.setTranslationX(-paramView.getWidth() * paramFloat);
    paramView.setPivotX(paramView.getWidth() * 0.5F);
    paramView.setPivotY(paramView.getHeight() * 0.5F);
    float f = 1.0F - paramFloat;
    paramView.setScaleX(f);
    paramView.setScaleY(f);
    if (paramFloat > 0.95F) {
      ViewCompat.setAlpha(paramView, 0.0F);
    } else {
      ViewCompat.setAlpha(paramView, 1.0F);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/stx/xhb/xbanner/transformers/ZoomCenterPageTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */