package com.bumptech.glide.load.engine;

import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.resource.UnitTransformation;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

final class DecodeHelper<Transcode> {
  private final List<Key> cacheKeys = new ArrayList<Key>();
  
  private DecodeJob.DiskCacheProvider diskCacheProvider;
  
  private DiskCacheStrategy diskCacheStrategy;
  
  private GlideContext glideContext;
  
  private int height;
  
  private boolean isCacheKeysSet;
  
  private boolean isLoadDataSet;
  
  private boolean isScaleOnlyOrNoTransform;
  
  private boolean isTransformationRequired;
  
  private final List<ModelLoader.LoadData<?>> loadData = new ArrayList<ModelLoader.LoadData<?>>();
  
  private Object model;
  
  private Options options;
  
  private Priority priority;
  
  private Class<?> resourceClass;
  
  private Key signature;
  
  private Class<Transcode> transcodeClass;
  
  private Map<Class<?>, Transformation<?>> transformations;
  
  private int width;
  
  void clear() {
    this.glideContext = null;
    this.model = null;
    this.signature = null;
    this.resourceClass = null;
    this.transcodeClass = null;
    this.options = null;
    this.priority = null;
    this.transformations = null;
    this.diskCacheStrategy = null;
    this.loadData.clear();
    this.isLoadDataSet = false;
    this.cacheKeys.clear();
    this.isCacheKeysSet = false;
  }
  
  ArrayPool getArrayPool() {
    return this.glideContext.getArrayPool();
  }
  
  List<Key> getCacheKeys() {
    if (!this.isCacheKeysSet) {
      this.isCacheKeysSet = true;
      this.cacheKeys.clear();
      List<ModelLoader.LoadData<?>> list = getLoadData();
      int i = list.size();
      for (byte b = 0; b < i; b++) {
        ModelLoader.LoadData loadData = list.get(b);
        if (!this.cacheKeys.contains(loadData.sourceKey))
          this.cacheKeys.add(loadData.sourceKey); 
        for (byte b1 = 0; b1 < loadData.alternateKeys.size(); b1++) {
          if (!this.cacheKeys.contains(loadData.alternateKeys.get(b1)))
            this.cacheKeys.add(loadData.alternateKeys.get(b1)); 
        } 
      } 
    } 
    return this.cacheKeys;
  }
  
  DiskCache getDiskCache() {
    return this.diskCacheProvider.getDiskCache();
  }
  
  DiskCacheStrategy getDiskCacheStrategy() {
    return this.diskCacheStrategy;
  }
  
  int getHeight() {
    return this.height;
  }
  
  List<ModelLoader.LoadData<?>> getLoadData() {
    if (!this.isLoadDataSet) {
      this.isLoadDataSet = true;
      this.loadData.clear();
      List<ModelLoader> list = this.glideContext.getRegistry().getModelLoaders(this.model);
      byte b = 0;
      int i = list.size();
      while (b < i) {
        ModelLoader.LoadData<?> loadData = ((ModelLoader)list.get(b)).buildLoadData(this.model, this.width, this.height, this.options);
        if (loadData != null)
          this.loadData.add(loadData); 
        b++;
      } 
    } 
    return this.loadData;
  }
  
  <Data> LoadPath<Data, ?, Transcode> getLoadPath(Class<Data> paramClass) {
    return this.glideContext.getRegistry().getLoadPath(paramClass, this.resourceClass, this.transcodeClass);
  }
  
  Class<?> getModelClass() {
    return this.model.getClass();
  }
  
  List<ModelLoader<File, ?>> getModelLoaders(File paramFile) throws Registry.NoModelLoaderAvailableException {
    return this.glideContext.getRegistry().getModelLoaders(paramFile);
  }
  
  Options getOptions() {
    return this.options;
  }
  
  Priority getPriority() {
    return this.priority;
  }
  
  List<Class<?>> getRegisteredResourceClasses() {
    return this.glideContext.getRegistry().getRegisteredResourceClasses(this.model.getClass(), this.resourceClass, this.transcodeClass);
  }
  
  <Z> ResourceEncoder<Z> getResultEncoder(Resource<Z> paramResource) {
    return this.glideContext.getRegistry().getResultEncoder(paramResource);
  }
  
  Key getSignature() {
    return this.signature;
  }
  
  <X> Encoder<X> getSourceEncoder(X paramX) throws Registry.NoSourceEncoderAvailableException {
    return this.glideContext.getRegistry().getSourceEncoder(paramX);
  }
  
  Class<?> getTranscodeClass() {
    return this.transcodeClass;
  }
  
  <Z> Transformation<Z> getTransformation(Class<Z> paramClass) {
    StringBuilder stringBuilder;
    Transformation transformation1 = this.transformations.get(paramClass);
    Transformation transformation2 = transformation1;
    if (transformation1 == null) {
      Iterator<Map.Entry> iterator = this.transformations.entrySet().iterator();
      while (true) {
        transformation2 = transformation1;
        if (iterator.hasNext()) {
          Map.Entry entry = iterator.next();
          if (((Class)entry.getKey()).isAssignableFrom(paramClass)) {
            transformation2 = (Transformation)entry.getValue();
            break;
          } 
          continue;
        } 
        break;
      } 
    } 
    if (transformation2 == null) {
      if (!this.transformations.isEmpty() || !this.isTransformationRequired)
        return (Transformation<Z>)UnitTransformation.get(); 
      stringBuilder = new StringBuilder();
      stringBuilder.append("Missing transformation for ");
      stringBuilder.append(paramClass);
      stringBuilder.append(". If you wish to ignore unknown resource types, use the optional transformation methods.");
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    return (Transformation<Z>)stringBuilder;
  }
  
  int getWidth() {
    return this.width;
  }
  
  boolean hasLoadPath(Class<?> paramClass) {
    boolean bool;
    if (getLoadPath(paramClass) != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  <R> void init(GlideContext paramGlideContext, Object paramObject, Key paramKey, int paramInt1, int paramInt2, DiskCacheStrategy paramDiskCacheStrategy, Class<?> paramClass, Class<R> paramClass1, Priority paramPriority, Options paramOptions, Map<Class<?>, Transformation<?>> paramMap, boolean paramBoolean1, boolean paramBoolean2, DecodeJob.DiskCacheProvider paramDiskCacheProvider) {
    this.glideContext = paramGlideContext;
    this.model = paramObject;
    this.signature = paramKey;
    this.width = paramInt1;
    this.height = paramInt2;
    this.diskCacheStrategy = paramDiskCacheStrategy;
    this.resourceClass = paramClass;
    this.diskCacheProvider = paramDiskCacheProvider;
    this.transcodeClass = paramClass1;
    this.priority = paramPriority;
    this.options = paramOptions;
    this.transformations = paramMap;
    this.isTransformationRequired = paramBoolean1;
    this.isScaleOnlyOrNoTransform = paramBoolean2;
  }
  
  boolean isResourceEncoderAvailable(Resource<?> paramResource) {
    return this.glideContext.getRegistry().isResourceEncoderAvailable(paramResource);
  }
  
  boolean isScaleOnlyOrNoTransform() {
    return this.isScaleOnlyOrNoTransform;
  }
  
  boolean isSourceKey(Key paramKey) {
    List<ModelLoader.LoadData<?>> list = getLoadData();
    int i = list.size();
    for (byte b = 0; b < i; b++) {
      if (((ModelLoader.LoadData)list.get(b)).sourceKey.equals(paramKey))
        return true; 
    } 
    return false;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/DecodeHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */