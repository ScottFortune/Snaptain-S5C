package com.bumptech.glide.load;

import java.nio.charset.Charset;
import java.security.MessageDigest;

public interface Key {
  public static final Charset CHARSET = Charset.forName("UTF-8");
  
  public static final String STRING_CHARSET_NAME = "UTF-8";
  
  boolean equals(Object paramObject);
  
  int hashCode();
  
  void updateDiskCacheKey(MessageDigest paramMessageDigest);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/Key.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */