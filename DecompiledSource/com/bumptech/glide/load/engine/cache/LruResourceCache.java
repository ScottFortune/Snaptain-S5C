package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.LruCache;

public class LruResourceCache extends LruCache<Key, Resource<?>> implements MemoryCache {
  private MemoryCache.ResourceRemovedListener listener;
  
  public LruResourceCache(long paramLong) {
    super(paramLong);
  }
  
  protected int getSize(Resource<?> paramResource) {
    return (paramResource == null) ? super.getSize(null) : paramResource.getSize();
  }
  
  protected void onItemEvicted(Key paramKey, Resource<?> paramResource) {
    MemoryCache.ResourceRemovedListener resourceRemovedListener = this.listener;
    if (resourceRemovedListener != null && paramResource != null)
      resourceRemovedListener.onResourceRemoved(paramResource); 
  }
  
  public void setResourceRemovedListener(MemoryCache.ResourceRemovedListener paramResourceRemovedListener) {
    this.listener = paramResourceRemovedListener;
  }
  
  public void trimMemory(int paramInt) {
    if (paramInt >= 40) {
      clearMemory();
    } else if (paramInt >= 20 || paramInt == 15) {
      trimToSize(getMaxSize() / 2L);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/cache/LruResourceCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */