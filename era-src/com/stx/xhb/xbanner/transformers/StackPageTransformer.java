package com.stx.xhb.xbanner.transformers;

import android.view.View;

public class StackPageTransformer extends BasePageTransformer {
  public void handleInvisiblePage(View paramView, float paramFloat) {}
  
  public void handleLeftPage(View paramView, float paramFloat) {}
  
  public void handleRightPage(View paramView, float paramFloat) {
    paramView.setTranslationX(-paramView.getWidth() * paramFloat);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/stx/xhb/xbanner/transformers/StackPageTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */