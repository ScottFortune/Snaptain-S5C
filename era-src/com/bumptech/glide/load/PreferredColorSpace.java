package com.bumptech.glide.load;

public enum PreferredColorSpace {
  DISPLAY_P3, SRGB;
  
  static {
    DISPLAY_P3 = new PreferredColorSpace("DISPLAY_P3", 1);
    $VALUES = new PreferredColorSpace[] { SRGB, DISPLAY_P3 };
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/PreferredColorSpace.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */