package com.bumptech.glide.load.model;

import androidx.core.util.Pools;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class MultiModelLoader<Model, Data> implements ModelLoader<Model, Data> {
  private final Pools.Pool<List<Throwable>> exceptionListPool;
  
  private final List<ModelLoader<Model, Data>> modelLoaders;
  
  MultiModelLoader(List<ModelLoader<Model, Data>> paramList, Pools.Pool<List<Throwable>> paramPool) {
    this.modelLoaders = paramList;
    this.exceptionListPool = paramPool;
  }
  
  public ModelLoader.LoadData<Data> buildLoadData(Model paramModel, int paramInt1, int paramInt2, Options paramOptions) {
    ModelLoader.LoadData<Data> loadData;
    int i = this.modelLoaders.size();
    ArrayList<DataFetcher> arrayList = new ArrayList(i);
    Model model = null;
    byte b = 0;
    Key key;
    for (key = null; b < i; key = key1) {
      ModelLoader modelLoader = this.modelLoaders.get(b);
      Key key1 = key;
      if (modelLoader.handles(paramModel)) {
        ModelLoader.LoadData loadData1 = modelLoader.buildLoadData(paramModel, paramInt1, paramInt2, paramOptions);
        key1 = key;
        if (loadData1 != null) {
          key1 = loadData1.sourceKey;
          arrayList.add(loadData1.fetcher);
        } 
      } 
      b++;
    } 
    paramModel = model;
    if (!arrayList.isEmpty()) {
      paramModel = model;
      if (key != null)
        loadData = new ModelLoader.LoadData(key, new MultiFetcher((List)arrayList, this.exceptionListPool)); 
    } 
    return loadData;
  }
  
  public boolean handles(Model paramModel) {
    Iterator<ModelLoader<Model, Data>> iterator = this.modelLoaders.iterator();
    while (iterator.hasNext()) {
      if (((ModelLoader)iterator.next()).handles(paramModel))
        return true; 
    } 
    return false;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("MultiModelLoader{modelLoaders=");
    stringBuilder.append(Arrays.toString(this.modelLoaders.toArray()));
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  static class MultiFetcher<Data> implements DataFetcher<Data>, DataFetcher.DataCallback<Data> {
    private DataFetcher.DataCallback<? super Data> callback;
    
    private int currentIndex;
    
    private List<Throwable> exceptions;
    
    private final List<DataFetcher<Data>> fetchers;
    
    private boolean isCancelled;
    
    private Priority priority;
    
    private final Pools.Pool<List<Throwable>> throwableListPool;
    
    MultiFetcher(List<DataFetcher<Data>> param1List, Pools.Pool<List<Throwable>> param1Pool) {
      this.throwableListPool = param1Pool;
      Preconditions.checkNotEmpty(param1List);
      this.fetchers = param1List;
      this.currentIndex = 0;
    }
    
    private void startNextOrFail() {
      if (this.isCancelled)
        return; 
      if (this.currentIndex < this.fetchers.size() - 1) {
        this.currentIndex++;
        loadData(this.priority, this.callback);
      } else {
        Preconditions.checkNotNull(this.exceptions);
        this.callback.onLoadFailed((Exception)new GlideException("Fetch failed", new ArrayList<Throwable>(this.exceptions)));
      } 
    }
    
    public void cancel() {
      this.isCancelled = true;
      Iterator<DataFetcher<Data>> iterator = this.fetchers.iterator();
      while (iterator.hasNext())
        ((DataFetcher)iterator.next()).cancel(); 
    }
    
    public void cleanup() {
      List<Throwable> list = this.exceptions;
      if (list != null)
        this.throwableListPool.release(list); 
      this.exceptions = null;
      Iterator<DataFetcher<Data>> iterator = this.fetchers.iterator();
      while (iterator.hasNext())
        ((DataFetcher)iterator.next()).cleanup(); 
    }
    
    public Class<Data> getDataClass() {
      return ((DataFetcher)this.fetchers.get(0)).getDataClass();
    }
    
    public DataSource getDataSource() {
      return ((DataFetcher)this.fetchers.get(0)).getDataSource();
    }
    
    public void loadData(Priority param1Priority, DataFetcher.DataCallback<? super Data> param1DataCallback) {
      this.priority = param1Priority;
      this.callback = param1DataCallback;
      this.exceptions = (List<Throwable>)this.throwableListPool.acquire();
      ((DataFetcher)this.fetchers.get(this.currentIndex)).loadData(param1Priority, this);
      if (this.isCancelled)
        cancel(); 
    }
    
    public void onDataReady(Data param1Data) {
      if (param1Data != null) {
        this.callback.onDataReady(param1Data);
      } else {
        startNextOrFail();
      } 
    }
    
    public void onLoadFailed(Exception param1Exception) {
      ((List<Exception>)Preconditions.checkNotNull(this.exceptions)).add(param1Exception);
      startNextOrFail();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/model/MultiModelLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */