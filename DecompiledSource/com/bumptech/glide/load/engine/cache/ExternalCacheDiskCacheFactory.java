package com.bumptech.glide.load.engine.cache;

import android.content.Context;
import java.io.File;

@Deprecated
public final class ExternalCacheDiskCacheFactory extends DiskLruCacheFactory {
  public ExternalCacheDiskCacheFactory(Context paramContext) {
    this(paramContext, "image_manager_disk_cache", 262144000);
  }
  
  public ExternalCacheDiskCacheFactory(Context paramContext, int paramInt) {
    this(paramContext, "image_manager_disk_cache", paramInt);
  }
  
  public ExternalCacheDiskCacheFactory(Context paramContext, String paramString, int paramInt) {
    super(new DiskLruCacheFactory.CacheDirectoryGetter(paramContext, paramString) {
          public File getCacheDirectory() {
            File file = context.getExternalCacheDir();
            if (file == null)
              return null; 
            String str = diskCacheName;
            return (str != null) ? new File(file, str) : file;
          }
        }paramInt);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/cache/ExternalCacheDiskCacheFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */