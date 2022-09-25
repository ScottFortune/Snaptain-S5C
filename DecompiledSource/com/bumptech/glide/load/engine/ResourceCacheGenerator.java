package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import java.io.File;
import java.util.List;

class ResourceCacheGenerator implements DataFetcherGenerator, DataFetcher.DataCallback<Object> {
  private File cacheFile;
  
  private final DataFetcherGenerator.FetcherReadyCallback cb;
  
  private ResourceCacheKey currentKey;
  
  private final DecodeHelper<?> helper;
  
  private volatile ModelLoader.LoadData<?> loadData;
  
  private int modelLoaderIndex;
  
  private List<ModelLoader<File, ?>> modelLoaders;
  
  private int resourceClassIndex = -1;
  
  private int sourceIdIndex;
  
  private Key sourceKey;
  
  ResourceCacheGenerator(DecodeHelper<?> paramDecodeHelper, DataFetcherGenerator.FetcherReadyCallback paramFetcherReadyCallback) {
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
    this.cb.onDataFetcherReady(this.sourceKey, paramObject, this.loadData.fetcher, DataSource.RESOURCE_DISK_CACHE, this.currentKey);
  }
  
  public void onLoadFailed(Exception paramException) {
    this.cb.onDataFetcherFailed(this.currentKey, paramException, this.loadData.fetcher, DataSource.RESOURCE_DISK_CACHE);
  }
  
  public boolean startNext() {
    StringBuilder stringBuilder;
    List<Key> list = this.helper.getCacheKeys();
    boolean bool = list.isEmpty();
    boolean bool1 = false;
    if (bool)
      return false; 
    List<Class<?>> list1 = this.helper.getRegisteredResourceClasses();
    if (list1.isEmpty()) {
      if (File.class.equals(this.helper.getTranscodeClass()))
        return false; 
      stringBuilder = new StringBuilder();
      stringBuilder.append("Failed to find any load path from ");
      stringBuilder.append(this.helper.getModelClass());
      stringBuilder.append(" to ");
      stringBuilder.append(this.helper.getTranscodeClass());
      throw new IllegalStateException(stringBuilder.toString());
    } 
    while (true) {
      if (this.modelLoaders == null || !hasNextModelLoader()) {
        this.resourceClassIndex++;
        if (this.resourceClassIndex >= stringBuilder.size()) {
          this.sourceIdIndex++;
          if (this.sourceIdIndex >= list.size())
            return false; 
          this.resourceClassIndex = 0;
        } 
        Key key = list.get(this.sourceIdIndex);
        Class<?> clazz = stringBuilder.get(this.resourceClassIndex);
        Transformation<?> transformation = this.helper.getTransformation(clazz);
        this.currentKey = new ResourceCacheKey(this.helper.getArrayPool(), key, this.helper.getSignature(), this.helper.getWidth(), this.helper.getHeight(), transformation, clazz, this.helper.getOptions());
        this.cacheFile = this.helper.getDiskCache().get(this.currentKey);
        File file = this.cacheFile;
        if (file != null) {
          this.sourceKey = key;
          this.modelLoaders = this.helper.getModelLoaders(file);
          this.modelLoaderIndex = 0;
        } 
        continue;
      } 
      this.loadData = null;
      while (!bool1 && hasNextModelLoader()) {
        List<ModelLoader<File, ?>> list2 = this.modelLoaders;
        int i = this.modelLoaderIndex;
        this.modelLoaderIndex = i + 1;
        this.loadData = ((ModelLoader)list2.get(i)).buildLoadData(this.cacheFile, this.helper.getWidth(), this.helper.getHeight(), this.helper.getOptions());
        if (this.loadData != null && this.helper.hasLoadPath(this.loadData.fetcher.getDataClass())) {
          this.loadData.fetcher.loadData(this.helper.getPriority(), this);
          bool1 = true;
        } 
      } 
      return bool1;
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/ResourceCacheGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */