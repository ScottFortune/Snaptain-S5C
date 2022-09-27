package com.shizhefei.task.imp;

import androidx.collection.LruCache;
import com.shizhefei.task.ICacheStore;

public class MemoryCacheStore implements ICacheStore {
  private LruCache<String, ICacheStore.Cache> lruCache;
  
  public MemoryCacheStore(int paramInt) {
    this.lruCache = new LruCache<String, ICacheStore.Cache>(paramInt) {
        protected int sizeOf(String param1String, ICacheStore.Cache param1Cache) {
          return 1;
        }
      };
  }
  
  public ICacheStore.Cache getCache(String paramString) {
    return (ICacheStore.Cache)this.lruCache.get(paramString);
  }
  
  public void saveCache(String paramString, long paramLong1, long paramLong2, Object paramObject) {
    this.lruCache.put(paramString, new ICacheStore.Cache(paramLong1, paramLong2, paramObject));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/task/imp/MemoryCacheStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */