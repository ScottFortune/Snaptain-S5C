package com.bumptech.glide.load.engine;

import android.util.Log;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.util.LogTime;

class SourceGenerator implements DataFetcherGenerator, DataFetcher.DataCallback<Object>, DataFetcherGenerator.FetcherReadyCallback {
  private static final String TAG = "SourceGenerator";
  
  private final DataFetcherGenerator.FetcherReadyCallback cb;
  
  private Object dataToCache;
  
  private final DecodeHelper<?> helper;
  
  private volatile ModelLoader.LoadData<?> loadData;
  
  private int loadDataListIndex;
  
  private DataCacheKey originalKey;
  
  private DataCacheGenerator sourceCacheGenerator;
  
  SourceGenerator(DecodeHelper<?> paramDecodeHelper, DataFetcherGenerator.FetcherReadyCallback paramFetcherReadyCallback) {
    this.helper = paramDecodeHelper;
    this.cb = paramFetcherReadyCallback;
  }
  
  private void cacheData(Object paramObject) {
    long l = LogTime.getLogTime();
    try {
      Encoder<DataType> encoder = this.helper.getSourceEncoder(paramObject);
      DataCacheWriter dataCacheWriter = new DataCacheWriter();
      this(encoder, (DataType)paramObject, this.helper.getOptions());
      DataCacheKey dataCacheKey = new DataCacheKey();
      this(this.loadData.sourceKey, this.helper.getSignature());
      this.originalKey = dataCacheKey;
      this.helper.getDiskCache().put(this.originalKey, dataCacheWriter);
      if (Log.isLoggable("SourceGenerator", 2)) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append("Finished encoding source to cache, key: ");
        stringBuilder.append(this.originalKey);
        stringBuilder.append(", data: ");
        stringBuilder.append(paramObject);
        stringBuilder.append(", encoder: ");
        stringBuilder.append(encoder);
        stringBuilder.append(", duration: ");
        stringBuilder.append(LogTime.getElapsedMillis(l));
        Log.v("SourceGenerator", stringBuilder.toString());
      } 
      this.loadData.fetcher.cleanup();
      return;
    } finally {
      this.loadData.fetcher.cleanup();
    } 
  }
  
  private boolean hasNextModelLoader() {
    boolean bool;
    if (this.loadDataListIndex < this.helper.getLoadData().size()) {
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
  
  public void onDataFetcherFailed(Key paramKey, Exception paramException, DataFetcher<?> paramDataFetcher, DataSource paramDataSource) {
    this.cb.onDataFetcherFailed(paramKey, paramException, paramDataFetcher, this.loadData.fetcher.getDataSource());
  }
  
  public void onDataFetcherReady(Key paramKey1, Object paramObject, DataFetcher<?> paramDataFetcher, DataSource paramDataSource, Key paramKey2) {
    this.cb.onDataFetcherReady(paramKey1, paramObject, paramDataFetcher, this.loadData.fetcher.getDataSource(), paramKey1);
  }
  
  public void onDataReady(Object paramObject) {
    DiskCacheStrategy diskCacheStrategy = this.helper.getDiskCacheStrategy();
    if (paramObject != null && diskCacheStrategy.isDataCacheable(this.loadData.fetcher.getDataSource())) {
      this.dataToCache = paramObject;
      this.cb.reschedule();
    } else {
      this.cb.onDataFetcherReady(this.loadData.sourceKey, paramObject, this.loadData.fetcher, this.loadData.fetcher.getDataSource(), this.originalKey);
    } 
  }
  
  public void onLoadFailed(Exception paramException) {
    this.cb.onDataFetcherFailed(this.originalKey, paramException, this.loadData.fetcher, this.loadData.fetcher.getDataSource());
  }
  
  public void reschedule() {
    throw new UnsupportedOperationException();
  }
  
  public boolean startNext() {
    Object<ModelLoader.LoadData<?>> object = (Object<ModelLoader.LoadData<?>>)this.dataToCache;
    if (object != null) {
      this.dataToCache = null;
      cacheData(object);
    } 
    object = (Object<ModelLoader.LoadData<?>>)this.sourceCacheGenerator;
    if (object != null && object.startNext())
      return true; 
    this.sourceCacheGenerator = null;
    this.loadData = null;
    boolean bool = false;
    while (!bool && hasNextModelLoader()) {
      object = (Object<ModelLoader.LoadData<?>>)this.helper.getLoadData();
      int i = this.loadDataListIndex;
      this.loadDataListIndex = i + 1;
      this.loadData = object.get(i);
      if (this.loadData != null && (this.helper.getDiskCacheStrategy().isDataCacheable(this.loadData.fetcher.getDataSource()) || this.helper.hasLoadPath(this.loadData.fetcher.getDataClass()))) {
        this.loadData.fetcher.loadData(this.helper.getPriority(), this);
        bool = true;
      } 
    } 
    return bool;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/SourceGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */