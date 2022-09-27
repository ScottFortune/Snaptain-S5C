package com.bumptech.glide.load.engine.cache;

import android.content.Context;
import java.io.File;

public final class InternalCacheDiskCacheFactory extends DiskLruCacheFactory {
  public InternalCacheDiskCacheFactory(Context paramContext) {
    this(paramContext, "image_manager_disk_cache", 262144000L);
  }
  
  public InternalCacheDiskCacheFactory(Context paramContext, long paramLong) {
    this(paramContext, "image_manager_disk_cache", paramLong);
  }
  
  public InternalCacheDiskCacheFactory(Context paramContext, String paramString, long paramLong) {
    super(new DiskLruCacheFactory.CacheDirectoryGetter(paramContext, paramString) {
          public File getCacheDirectory() {
            File file = context.getCacheDir();
            if (file == null)
              return null; 
            String str = diskCacheName;
            return (str != null) ? new File(file, str) : file;
          }
        }paramLong);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/cache/InternalCacheDiskCacheFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */