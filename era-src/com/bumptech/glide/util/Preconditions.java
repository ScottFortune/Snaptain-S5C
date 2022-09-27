package com.bumptech.glide.util;

import android.text.TextUtils;

public final class Preconditions {
  public static void checkArgument(boolean paramBoolean, String paramString) {
    if (paramBoolean)
      return; 
    throw new IllegalArgumentException(paramString);
  }
  
  public static String checkNotEmpty(String paramString) {
    if (!TextUtils.isEmpty(paramString))
      return paramString; 
    throw new IllegalArgumentException("Must not be null or empty");
  }
  
  public static <T extends java.util.Collection<Y>, Y> T checkNotEmpty(T paramT) {
    if (!paramT.isEmpty())
      return paramT; 
    throw new IllegalArgumentException("Must not be empty.");
  }
  
  public static <T> T checkNotNull(T paramT) {
    return checkNotNull(paramT, "Argument must not be null");
  }
  
  public static <T> T checkNotNull(T paramT, String paramString) {
    if (paramT != null)
      return paramT; 
    throw new NullPointerException(paramString);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/util/Preconditions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */