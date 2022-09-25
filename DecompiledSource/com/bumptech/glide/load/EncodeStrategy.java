package com.bumptech.glide.load;

public enum EncodeStrategy {
  NONE, SOURCE, TRANSFORMED;
  
  static {
    NONE = new EncodeStrategy("NONE", 2);
    $VALUES = new EncodeStrategy[] { SOURCE, TRANSFORMED, NONE };
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/EncodeStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */