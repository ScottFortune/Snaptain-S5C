package com.stx.xhb.xbanner.transformers;

import android.view.View;
import androidx.core.view.ViewCompat;

public class DepthPageTransformer extends BasePageTransformer {
  private float mMinScale = 0.8F;
  
  public DepthPageTransformer() {}
  
  public DepthPageTransformer(float paramFloat) {
    setMinScale(paramFloat);
  }
  
  public void handleInvisiblePage(View paramView, float paramFloat) {
    ViewCompat.setAlpha(paramView, 0.0F);
  }
  
  public void handleLeftPage(View paramView, float paramFloat) {
    paramView.setAlpha(1.0F);
    paramView.setTranslationX(0.0F);
    paramView.setScaleX(1.0F);
    paramView.setScaleY(1.0F);
  }
  
  public void handleRightPage(View paramView, float paramFloat) {
    float f = 1.0F - paramFloat;
    paramView.setAlpha(f);
    paramView.setTranslationX(-paramView.getWidth() * paramFloat);
    paramFloat = this.mMinScale;
    paramFloat += (1.0F - paramFloat) * f;
    paramView.setScaleX(paramFloat);
    paramView.setScaleY(paramFloat);
  }
  
  public void setMinScale(float paramFloat) {
    if (paramFloat >= 0.6F && paramFloat <= 1.0F)
      this.mMinScale = paramFloat; 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/stx/xhb/xbanner/transformers/DepthPageTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */