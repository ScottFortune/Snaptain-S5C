package com.stx.xhb.xbanner.transformers;

import android.view.View;
import androidx.core.view.ViewCompat;

public class AlphaPageTransformer extends BasePageTransformer {
  private float mMinScale = 0.4F;
  
  public AlphaPageTransformer() {}
  
  public AlphaPageTransformer(float paramFloat) {
    setMinScale(paramFloat);
  }
  
  public void handleInvisiblePage(View paramView, float paramFloat) {
    ViewCompat.setAlpha(paramView, 0.0F);
  }
  
  public void handleLeftPage(View paramView, float paramFloat) {
    float f = this.mMinScale;
    paramView.setAlpha(f + (1.0F - f) * (paramFloat + 1.0F));
  }
  
  public void handleRightPage(View paramView, float paramFloat) {
    float f = this.mMinScale;
    paramView.setAlpha(f + (1.0F - f) * (1.0F - paramFloat));
  }
  
  public void setMinScale(float paramFloat) {
    if (paramFloat >= 0.0F && paramFloat <= 1.0F)
      this.mMinScale = paramFloat; 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/stx/xhb/xbanner/transformers/AlphaPageTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */