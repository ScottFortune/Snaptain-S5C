package com.stx.xhb.xbanner;

import android.content.Context;
import android.widget.Scroller;

public class XBannerScroller extends Scroller {
  private int mDuration = 800;
  
  XBannerScroller(Context paramContext, int paramInt) {
    super(paramContext);
    this.mDuration = paramInt;
  }
  
  public void startScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.startScroll(paramInt1, paramInt2, paramInt3, paramInt4, this.mDuration);
  }
  
  public void startScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    super.startScroll(paramInt1, paramInt2, paramInt3, paramInt4, this.mDuration);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/stx/xhb/xbanner/XBannerScroller.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */