package com.bumptech.glide.load.engine.cache;

import android.content.Context;
import java.io.File;

public final class ExternalPreferredCacheDiskCacheFactory extends DiskLruCacheFactory {
  public ExternalPreferredCacheDiskCacheFactory(Context paramContext) {
    this(paramContext, "image_manager_disk_cache", 262144000L);
  }
  
  public ExternalPreferredCacheDiskCacheFactory(Context paramContext, long paramLong) {
    this(paramContext, "image_manager_disk_cache", paramLong);
  }
  
  public ExternalPreferredCacheDiskCacheFactory(Context paramContext, String paramString, long paramLong) {
    super(new DiskLruCacheFactory.CacheDirectoryGetter(paramContext, paramString) {
          private File getInternalCacheDirectory() {
            File file = context.getCacheDir();
            if (file == null)
              return null; 
            String str = diskCacheName;
            return (str != null) ? new File(file, str) : file;
          }
          
          public File getCacheDirectory() {
            File file1 = getInternalCacheDirectory();
            if (file1 != null && file1.exists())
              return file1; 
            File file2 = context.getExternalCacheDir();
            if (file2 == null || !file2.canWrite())
              return file1; 
            String str = diskCacheName;
            return (str != null) ? new File(file2, str) : file2;
          }
        }paramLong);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/cache/ExternalPreferredCacheDiskCacheFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */