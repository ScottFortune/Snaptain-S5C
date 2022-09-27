package com.bumptech.glide.load.engine.bitmap_recycle;

interface ArrayAdapterInterface<T> {
  int getArrayLength(T paramT);
  
  int getElementSizeInBytes();
  
  String getTag();
  
  T newArray(int paramInt);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/bitmap_recycle/ArrayAdapterInterface.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */