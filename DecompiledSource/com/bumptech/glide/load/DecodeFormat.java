package com.bumptech.glide.load;

public enum DecodeFormat {
  PREFER_ARGB_8888, PREFER_RGB_565;
  
  public static final DecodeFormat DEFAULT;
  
  static {
    DecodeFormat decodeFormat = PREFER_ARGB_8888;
    $VALUES = new DecodeFormat[] { decodeFormat, PREFER_RGB_565 };
    DEFAULT = decodeFormat;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/DecodeFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */