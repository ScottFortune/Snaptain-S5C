package com.bumptech.glide.load.engine.bitmap_recycle;

public interface ArrayPool {
  public static final int STANDARD_BUFFER_SIZE_BYTES = 65536;
  
  void clearMemory();
  
  <T> T get(int paramInt, Class<T> paramClass);
  
  <T> T getExact(int paramInt, Class<T> paramClass);
  
  <T> void put(T paramT);
  
  @Deprecated
  <T> void put(T paramT, Class<T> paramClass);
  
  void trimMemory(int paramInt);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/bitmap_recycle/ArrayPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */