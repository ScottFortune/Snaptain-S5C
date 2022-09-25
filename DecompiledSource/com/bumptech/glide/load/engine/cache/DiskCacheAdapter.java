package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import java.io.File;

public class DiskCacheAdapter implements DiskCache {
  public void clear() {}
  
  public void delete(Key paramKey) {}
  
  public File get(Key paramKey) {
    return null;
  }
  
  public void put(Key paramKey, DiskCache.Writer paramWriter) {}
  
  public static final class Factory implements DiskCache.Factory {
    public DiskCache build() {
      return new DiskCacheAdapter();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/cache/DiskCacheAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */