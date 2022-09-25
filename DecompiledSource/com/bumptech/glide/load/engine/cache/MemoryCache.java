package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.Resource;

public interface MemoryCache {
  void clearMemory();
  
  long getCurrentSize();
  
  long getMaxSize();
  
  Resource<?> put(Key paramKey, Resource<?> paramResource);
  
  Resource<?> remove(Key paramKey);
  
  void setResourceRemovedListener(ResourceRemovedListener paramResourceRemovedListener);
  
  void setSizeMultiplier(float paramFloat);
  
  void trimMemory(int paramInt);
  
  public static interface ResourceRemovedListener {
    void onResourceRemoved(Resource<?> param1Resource);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/cache/MemoryCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */