package com.bumptech.glide.load.data;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;

public interface DataFetcher<T> {
  void cancel();
  
  void cleanup();
  
  Class<T> getDataClass();
  
  DataSource getDataSource();
  
  void loadData(Priority paramPriority, DataCallback<? super T> paramDataCallback);
  
  public static interface DataCallback<T> {
    void onDataReady(T param1T);
    
    void onLoadFailed(Exception param1Exception);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/data/DataFetcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */