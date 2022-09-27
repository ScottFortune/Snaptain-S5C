package com.bumptech.glide.load.data.mediastore;

import android.net.Uri;

public final class MediaStoreUtil {
  private static final int MINI_THUMB_HEIGHT = 384;
  
  private static final int MINI_THUMB_WIDTH = 512;
  
  public static boolean isMediaStoreImageUri(Uri paramUri) {
    boolean bool;
    if (isMediaStoreUri(paramUri) && !isVideoUri(paramUri)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean isMediaStoreUri(Uri paramUri) {
    boolean bool;
    if (paramUri != null && "content".equals(paramUri.getScheme()) && "media".equals(paramUri.getAuthority())) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean isMediaStoreVideoUri(Uri paramUri) {
    boolean bool;
    if (isMediaStoreUri(paramUri) && isVideoUri(paramUri)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean isThumbnailSize(int paramInt1, int paramInt2) {
    boolean bool;
    if (paramInt1 != Integer.MIN_VALUE && paramInt2 != Integer.MIN_VALUE && paramInt1 <= 512 && paramInt2 <= 384) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static boolean isVideoUri(Uri paramUri) {
    return paramUri.getPathSegments().contains("video");
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/data/mediastore/MediaStoreUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */