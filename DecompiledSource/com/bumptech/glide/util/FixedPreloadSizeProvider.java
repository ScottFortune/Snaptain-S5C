package com.bumptech.glide.util;

import com.bumptech.glide.ListPreloader;

public class FixedPreloadSizeProvider<T> implements ListPreloader.PreloadSizeProvider<T> {
  private final int[] size;
  
  public FixedPreloadSizeProvider(int paramInt1, int paramInt2) {
    this.size = new int[] { paramInt1, paramInt2 };
  }
  
  public int[] getPreloadSize(T paramT, int paramInt1, int paramInt2) {
    return this.size;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/util/FixedPreloadSizeProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */