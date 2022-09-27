package com.bumptech.glide.load.data;

import java.io.IOException;

public interface DataRewinder<T> {
  void cleanup();
  
  T rewindAndGet() throws IOException;
  
  public static interface Factory<T> {
    DataRewinder<T> build(T param1T);
    
    Class<T> getDataClass();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/data/DataRewinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */