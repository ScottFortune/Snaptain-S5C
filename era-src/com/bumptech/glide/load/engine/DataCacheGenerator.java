package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import java.io.File;
import java.util.List;

class DataCacheGenerator implements DataFetcherGenerator, DataFetcher.DataCallback<Object> {
  private File cacheFile;
  
  private final List<Key> cacheKeys;
  
  private final DataFetcherGenerator.FetcherReadyCallback cb;
  
  private final DecodeHelper<?> helper;
  
  private volatile ModelLoader.LoadData<?> loadData;
  
  private int modelLoaderIndex;
  
  private List<ModelLoader<File, ?>> modelLoaders;
  
  private int sourceIdIndex = -1;
  
  private Key sourceKey;
  
  DataCacheGenerator(DecodeHelper<?> paramDecodeHelper, DataFetcherGenerator.FetcherReadyCallback paramFetcherReadyCallback) {
    this(paramDecodeHelper.getCacheKeys(), paramDecodeHelper, paramFetcherReadyCallback);
  }
  
  DataCacheGenerator(List<Key> paramList, DecodeHelper<?> paramDecodeHelper, DataFetcherGenerator.FetcherReadyCallback paramFetcherReadyCallback) {
    this.cacheKeys = paramList;
    this.helper = paramDecodeHelper;
    this.cb = paramFetcherReadyCallback;
  }
  
  private boolean hasNextModelLoader() {
    boolean bool;
    if (this.modelLoaderIndex < this.modelLoaders.size()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void cancel() {
    ModelLoader.LoadData<?> loadData = this.loadData;
    if (loadData != null)
      loadData.fetcher.cancel(); 
  }
  
  public void onDataReady(Object paramObject) {
    this.cb.onDataFetcherReady(this.sourceKey, paramObject, this.loadData.fetcher, DataSource.DATA_DISK_CACHE, this.sourceKey);
  }
  
  public void onLoadFailed(Exception paramException) {
    this.cb.onDataFetcherFailed(this.sourceKey, paramException, this.loadData.fetcher, DataSource.DATA_DISK_CACHE);
  }
  
  public boolean startNext() {
    while (true) {
      List<ModelLoader<File, ?>> list = this.modelLoaders;
      boolean bool = false;
      if (list == null || !hasNextModelLoader()) {
        this.sourceIdIndex++;
        if (this.sourceIdIndex >= this.cacheKeys.size())
          return false; 
        Key key = this.cacheKeys.get(this.sourceIdIndex);
        DataCacheKey dataCacheKey = new DataCacheKey(key, this.helper.getSignature());
        this.cacheFile = this.helper.getDiskCache().get(dataCacheKey);
        File file = this.cacheFile;
        if (file != null) {
          this.sourceKey = key;
          this.modelLoaders = this.helper.getModelLoaders(file);
          this.modelLoaderIndex = 0;
        } 
        continue;
      } 
      this.loadData = null;
      while (!bool && hasNextModelLoader()) {
        list = this.modelLoaders;
        int i = this.modelLoaderIndex;
        this.modelLoaderIndex = i + 1;
        this.loadData = ((ModelLoader)list.get(i)).buildLoadData(this.cacheFile, this.helper.getWidth(), this.helper.getHeight(), this.helper.getOptions());
        if (this.loadData != null && this.helper.hasLoadPath(this.loadData.fetcher.getDataClass())) {
          this.loadData.fetcher.loadData(this.helper.getPriority(), this);
          bool = true;
        } 
      } 
      return bool;
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/DataCacheGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */