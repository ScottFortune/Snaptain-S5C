package com.stx.xhb.xbanner;

import android.view.View;

public abstract class OnDoubleClickListener implements View.OnClickListener {
  private long mLastClickTime = 0L;
  
  private int mThrottleFirstTime = 1000;
  
  public OnDoubleClickListener() {}
  
  public OnDoubleClickListener(int paramInt) {
    this.mThrottleFirstTime = paramInt;
  }
  
  public void onClick(View paramView) {
    long l = System.currentTimeMillis();
    if (l - this.mLastClickTime > this.mThrottleFirstTime) {
      this.mLastClickTime = l;
      onNoDoubleClick(paramView);
    } 
  }
  
  public abstract void onNoDoubleClick(View paramView);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/stx/xhb/xbanner/OnDoubleClickListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */