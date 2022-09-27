package com.netopsun.drone;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class AnimationLinearLayout extends LinearLayout {
  private boolean isLayout;
  
  public AnimationLinearLayout(Context paramContext) {
    super(paramContext);
  }
  
  public AnimationLinearLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  public AnimationLinearLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public AnimationLinearLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    this.isLayout = true;
  }
  
  public void setVisibility(int paramInt) {
    Animation animation;
    super.setVisibility(paramInt);
    if (!this.isLayout)
      return; 
    if (paramInt == 0) {
      animation = AnimationUtils.loadAnimation(getContext(), 2130771993);
    } else {
      animation = AnimationUtils.loadAnimation(getContext(), 2130771992);
    } 
    startAnimation(animation);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/AnimationLinearLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */