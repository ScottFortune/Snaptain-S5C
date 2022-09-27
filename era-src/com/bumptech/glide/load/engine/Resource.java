package com.bumptech.glide.load.engine;

public interface Resource<Z> {
  Z get();
  
  Class<Z> getResourceClass();
  
  int getSize();
  
  void recycle();
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/Resource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */