package com.bumptech.glide.load.engine.cache;

import java.io.File;

public class DiskLruCacheFactory implements DiskCache.Factory {
  private final CacheDirectoryGetter cacheDirectoryGetter;
  
  private final long diskCacheSize;
  
  public DiskLruCacheFactory(CacheDirectoryGetter paramCacheDirectoryGetter, long paramLong) {
    this.diskCacheSize = paramLong;
    this.cacheDirectoryGetter = paramCacheDirectoryGetter;
  }
  
  public DiskLruCacheFactory(String paramString, long paramLong) {
    this(new CacheDirectoryGetter(paramString) {
          public File getCacheDirectory() {
            return new File(diskCacheFolder);
          }
        },  paramLong);
  }
  
  public DiskLruCacheFactory(String paramString1, String paramString2, long paramLong) {
    this(new CacheDirectoryGetter(paramString1, paramString2) {
          public File getCacheDirectory() {
            return new File(diskCacheFolder, diskCacheName);
          }
        }paramLong);
  }
  
  public DiskCache build() {
    File file = this.cacheDirectoryGetter.getCacheDirectory();
    return (file == null) ? null : ((!file.mkdirs() && (!file.exists() || !file.isDirectory())) ? null : DiskLruCacheWrapper.create(file, this.diskCacheSize));
  }
  
  public static interface CacheDirectoryGetter {
    File getCacheDirectory();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/cache/DiskLruCacheFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */