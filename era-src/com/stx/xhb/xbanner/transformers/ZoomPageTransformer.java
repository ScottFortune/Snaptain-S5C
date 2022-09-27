package com.stx.xhb.xbanner.transformers;

import android.view.View;
import androidx.core.view.ViewCompat;

public class ZoomPageTransformer extends BasePageTransformer {
  private float mMinAlpha = 0.65F;
  
  private float mMinScale = 0.85F;
  
  public ZoomPageTransformer() {}
  
  public ZoomPageTransformer(float paramFloat1, float paramFloat2) {
    setMinAlpha(paramFloat1);
    setMinScale(paramFloat2);
  }
  
  public void handleInvisiblePage(View paramView, float paramFloat) {
    ViewCompat.setAlpha(paramView, 0.0F);
  }
  
  public void handleLeftPage(View paramView, float paramFloat) {
    paramFloat = Math.max(this.mMinScale, paramFloat + 1.0F);
    float f1 = paramView.getHeight();
    float f2 = 1.0F - paramFloat;
    f1 = f1 * f2 / 2.0F;
    paramView.setTranslationX(paramView.getWidth() * f2 / 2.0F - f1 / 2.0F);
    paramView.setScaleX(paramFloat);
    paramView.setScaleY(paramFloat);
    f2 = this.mMinAlpha;
    f1 = this.mMinScale;
    paramView.setAlpha(f2 + (paramFloat - f1) / (1.0F - f1) * (1.0F - f2));
  }
  
  public void handleRightPage(View paramView, float paramFloat) {
    paramFloat = Math.max(this.mMinScale, 1.0F - paramFloat);
    float f1 = paramView.getHeight();
    float f2 = 1.0F - paramFloat;
    f1 = f1 * f2 / 2.0F;
    paramView.setTranslationX(-(paramView.getWidth() * f2 / 2.0F) + f1 / 2.0F);
    paramView.setScaleX(paramFloat);
    paramView.setScaleY(paramFloat);
    f1 = this.mMinAlpha;
    f2 = this.mMinScale;
    paramView.setAlpha(f1 + (paramFloat - f2) / (1.0F - f2) * (1.0F - f1));
  }
  
  public void setMinAlpha(float paramFloat) {
    if (paramFloat >= 0.6F && paramFloat <= 1.0F)
      this.mMinAlpha = paramFloat; 
  }
  
  public void setMinScale(float paramFloat) {
    if (paramFloat >= 0.6F && paramFloat <= 1.0F)
      this.mMinScale = paramFloat; 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/stx/xhb/xbanner/transformers/ZoomPageTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */