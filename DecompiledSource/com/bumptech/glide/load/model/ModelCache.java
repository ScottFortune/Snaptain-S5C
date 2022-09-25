package com.bumptech.glide.load.model;

import com.bumptech.glide.util.LruCache;
import com.bumptech.glide.util.Util;
import java.util.Queue;

public class ModelCache<A, B> {
  private static final int DEFAULT_SIZE = 250;
  
  private final LruCache<ModelKey<A>, B> cache;
  
  public ModelCache() {
    this(250L);
  }
  
  public ModelCache(long paramLong) {
    this.cache = new LruCache<ModelKey<A>, B>(paramLong) {
        protected void onItemEvicted(ModelCache.ModelKey<A> param1ModelKey, B param1B) {
          param1ModelKey.release();
        }
      };
  }
  
  public void clear() {
    this.cache.clearMemory();
  }
  
  public B get(A paramA, int paramInt1, int paramInt2) {
    ModelKey<A> modelKey = ModelKey.get(paramA, paramInt1, paramInt2);
    Object object = this.cache.get(modelKey);
    modelKey.release();
    return (B)object;
  }
  
  public void put(A paramA, int paramInt1, int paramInt2, B paramB) {
    ModelKey<A> modelKey = ModelKey.get(paramA, paramInt1, paramInt2);
    this.cache.put(modelKey, paramB);
  }
  
  static final class ModelKey<A> {
    private static final Queue<ModelKey<?>> KEY_QUEUE = Util.createQueue(0);
    
    private int height;
    
    private A model;
    
    private int width;
    
    static <A> ModelKey<A> get(A param1A, int param1Int1, int param1Int2) {
      Queue<ModelKey<?>> queue;
      ModelKey<A> modelKey;
      synchronized (KEY_QUEUE) {
        ModelKey<A> modelKey1 = (ModelKey)KEY_QUEUE.poll();
        modelKey = modelKey1;
        if (modelKey1 == null)
          modelKey = new ModelKey(); 
        modelKey.init(param1A, param1Int1, param1Int2);
        return modelKey;
      } 
    }
    
    private void init(A param1A, int param1Int1, int param1Int2) {
      this.model = param1A;
      this.width = param1Int1;
      this.height = param1Int2;
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = param1Object instanceof ModelKey;
      boolean bool1 = false;
      boolean bool2 = bool1;
      if (bool) {
        param1Object = param1Object;
        bool2 = bool1;
        if (this.width == ((ModelKey)param1Object).width) {
          bool2 = bool1;
          if (this.height == ((ModelKey)param1Object).height) {
            bool2 = bool1;
            if (this.model.equals(((ModelKey)param1Object).model))
              bool2 = true; 
          } 
        } 
      } 
      return bool2;
    }
    
    public int hashCode() {
      return (this.height * 31 + this.width) * 31 + this.model.hashCode();
    }
    
    public void release() {
      synchronized (KEY_QUEUE) {
        KEY_QUEUE.offer(this);
        return;
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/model/ModelCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */