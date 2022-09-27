package com.stx.xhb.xbanner.entity;

public class LocalImageInfo extends SimpleBannerInfo {
  private int bannerRes;
  
  public LocalImageInfo(int paramInt) {
    this.bannerRes = paramInt;
  }
  
  public Integer getXBannerUrl() {
    return Integer.valueOf(this.bannerRes);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/stx/xhb/xbanner/entity/LocalImageInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */