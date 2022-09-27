package com.bumptech.glide.load.engine.bitmap_recycle;

import com.bumptech.glide.util.Util;
import java.util.Queue;

abstract class BaseKeyPool<T extends Poolable> {
  private static final int MAX_SIZE = 20;
  
  private final Queue<T> keyPool = Util.createQueue(20);
  
  abstract T create();
  
  T get() {
    Poolable poolable1 = (Poolable)this.keyPool.poll();
    Poolable poolable2 = poolable1;
    if (poolable1 == null)
      poolable2 = (Poolable)create(); 
    return (T)poolable2;
  }
  
  public void offer(T paramT) {
    if (this.keyPool.size() < 20)
      this.keyPool.offer(paramT); 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/bitmap_recycle/BaseKeyPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */