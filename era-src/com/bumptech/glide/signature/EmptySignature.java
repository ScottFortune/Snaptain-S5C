package com.bumptech.glide.signature;

import com.bumptech.glide.load.Key;
import java.security.MessageDigest;

public final class EmptySignature implements Key {
  private static final EmptySignature EMPTY_KEY = new EmptySignature();
  
  public static EmptySignature obtain() {
    return EMPTY_KEY;
  }
  
  public String toString() {
    return "EmptySignature";
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/signature/EmptySignature.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */