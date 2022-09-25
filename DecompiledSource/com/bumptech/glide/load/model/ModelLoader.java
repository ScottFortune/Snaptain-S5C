package com.bumptech.glide.load.model;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.util.Preconditions;
import java.util.Collections;
import java.util.List;

public interface ModelLoader<Model, Data> {
  LoadData<Data> buildLoadData(Model paramModel, int paramInt1, int paramInt2, Options paramOptions);
  
  boolean handles(Model paramModel);
  
  public static class LoadData<Data> {
    public final List<Key> alternateKeys;
    
    public final DataFetcher<Data> fetcher;
    
    public final Key sourceKey;
    
    public LoadData(Key param1Key, DataFetcher<Data> param1DataFetcher) {
      this(param1Key, Collections.emptyList(), param1DataFetcher);
    }
    
    public LoadData(Key param1Key, List<Key> param1List, DataFetcher<Data> param1DataFetcher) {
      this.sourceKey = (Key)Preconditions.checkNotNull(param1Key);
      this.alternateKeys = (List<Key>)Preconditions.checkNotNull(param1List);
      this.fetcher = (DataFetcher<Data>)Preconditions.checkNotNull(param1DataFetcher);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/model/ModelLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */