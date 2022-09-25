package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.data.DataFetcher;

interface DataFetcherGenerator {
  void cancel();
  
  boolean startNext();
  
  public static interface FetcherReadyCallback {
    void onDataFetcherFailed(Key param1Key, Exception param1Exception, DataFetcher<?> param1DataFetcher, DataSource param1DataSource);
    
    void onDataFetcherReady(Key param1Key1, Object param1Object, DataFetcher<?> param1DataFetcher, DataSource param1DataSource, Key param1Key2);
    
    void reschedule();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/DataFetcherGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */