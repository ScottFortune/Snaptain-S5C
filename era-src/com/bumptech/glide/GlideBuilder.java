package com.bumptech.glide;

import android.content.Context;
import androidx.collection.ArrayMap;
import androidx.core.os.BuildCompat;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPoolAdapter;
import com.bumptech.glide.load.engine.bitmap_recycle.LruArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.DefaultConnectivityMonitorFactory;
import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class GlideBuilder {
  private GlideExecutor animationExecutor;
  
  private ArrayPool arrayPool;
  
  private BitmapPool bitmapPool;
  
  private ConnectivityMonitorFactory connectivityMonitorFactory;
  
  private List<RequestListener<Object>> defaultRequestListeners;
  
  private Glide.RequestOptionsFactory defaultRequestOptionsFactory = new Glide.RequestOptionsFactory() {
      public RequestOptions build() {
        return new RequestOptions();
      }
    };
  
  private final Map<Class<?>, TransitionOptions<?, ?>> defaultTransitionOptions = (Map<Class<?>, TransitionOptions<?, ?>>)new ArrayMap();
  
  private GlideExecutor diskCacheExecutor;
  
  private DiskCache.Factory diskCacheFactory;
  
  private Engine engine;
  
  private int hardwareBitmapFdLimit = 700;
  
  private boolean isActiveResourceRetentionAllowed;
  
  private boolean isImageDecoderEnabledForBitmaps;
  
  private boolean isLoggingRequestOriginsEnabled;
  
  private int logLevel = 4;
  
  private MemoryCache memoryCache;
  
  private MemorySizeCalculator memorySizeCalculator;
  
  private int minHardwareDimension = 128;
  
  private RequestManagerRetriever.RequestManagerFactory requestManagerFactory;
  
  private GlideExecutor sourceExecutor;
  
  public GlideBuilder addGlobalRequestListener(RequestListener<Object> paramRequestListener) {
    if (this.defaultRequestListeners == null)
      this.defaultRequestListeners = new ArrayList<RequestListener<Object>>(); 
    this.defaultRequestListeners.add(paramRequestListener);
    return this;
  }
  
  Glide build(Context paramContext) {
    if (this.sourceExecutor == null)
      this.sourceExecutor = GlideExecutor.newSourceExecutor(); 
    if (this.diskCacheExecutor == null)
      this.diskCacheExecutor = GlideExecutor.newDiskCacheExecutor(); 
    if (this.animationExecutor == null)
      this.animationExecutor = GlideExecutor.newAnimationExecutor(); 
    if (this.memorySizeCalculator == null)
      this.memorySizeCalculator = (new MemorySizeCalculator.Builder(paramContext)).build(); 
    if (this.connectivityMonitorFactory == null)
      this.connectivityMonitorFactory = (ConnectivityMonitorFactory)new DefaultConnectivityMonitorFactory(); 
    if (this.bitmapPool == null) {
      int i = this.memorySizeCalculator.getBitmapPoolSize();
      if (i > 0) {
        this.bitmapPool = (BitmapPool)new LruBitmapPool(i);
      } else {
        this.bitmapPool = (BitmapPool)new BitmapPoolAdapter();
      } 
    } 
    if (this.arrayPool == null)
      this.arrayPool = (ArrayPool)new LruArrayPool(this.memorySizeCalculator.getArrayPoolSizeInBytes()); 
    if (this.memoryCache == null)
      this.memoryCache = (MemoryCache)new LruResourceCache(this.memorySizeCalculator.getMemoryCacheSize()); 
    if (this.diskCacheFactory == null)
      this.diskCacheFactory = (DiskCache.Factory)new InternalCacheDiskCacheFactory(paramContext); 
    if (this.engine == null)
      this.engine = new Engine(this.memoryCache, this.diskCacheFactory, this.diskCacheExecutor, this.sourceExecutor, GlideExecutor.newUnlimitedSourceExecutor(), this.animationExecutor, this.isActiveResourceRetentionAllowed); 
    List<RequestListener<Object>> list = this.defaultRequestListeners;
    if (list == null) {
      this.defaultRequestListeners = Collections.emptyList();
    } else {
      this.defaultRequestListeners = Collections.unmodifiableList(list);
    } 
    RequestManagerRetriever requestManagerRetriever = new RequestManagerRetriever(this.requestManagerFactory);
    return new Glide(paramContext, this.engine, this.memoryCache, this.bitmapPool, this.arrayPool, requestManagerRetriever, this.connectivityMonitorFactory, this.logLevel, this.defaultRequestOptionsFactory, this.defaultTransitionOptions, this.defaultRequestListeners, this.isLoggingRequestOriginsEnabled, this.isImageDecoderEnabledForBitmaps, this.hardwareBitmapFdLimit, this.minHardwareDimension);
  }
  
  public GlideBuilder setAnimationExecutor(GlideExecutor paramGlideExecutor) {
    this.animationExecutor = paramGlideExecutor;
    return this;
  }
  
  public GlideBuilder setArrayPool(ArrayPool paramArrayPool) {
    this.arrayPool = paramArrayPool;
    return this;
  }
  
  public GlideBuilder setBitmapPool(BitmapPool paramBitmapPool) {
    this.bitmapPool = paramBitmapPool;
    return this;
  }
  
  public GlideBuilder setConnectivityMonitorFactory(ConnectivityMonitorFactory paramConnectivityMonitorFactory) {
    this.connectivityMonitorFactory = paramConnectivityMonitorFactory;
    return this;
  }
  
  public GlideBuilder setDefaultRequestOptions(Glide.RequestOptionsFactory paramRequestOptionsFactory) {
    this.defaultRequestOptionsFactory = (Glide.RequestOptionsFactory)Preconditions.checkNotNull(paramRequestOptionsFactory);
    return this;
  }
  
  public GlideBuilder setDefaultRequestOptions(final RequestOptions requestOptions) {
    return setDefaultRequestOptions(new Glide.RequestOptionsFactory() {
          public RequestOptions build() {
            RequestOptions requestOptions = requestOptions;
            if (requestOptions == null)
              requestOptions = new RequestOptions(); 
            return requestOptions;
          }
        });
  }
  
  public <T> GlideBuilder setDefaultTransitionOptions(Class<T> paramClass, TransitionOptions<?, T> paramTransitionOptions) {
    this.defaultTransitionOptions.put(paramClass, paramTransitionOptions);
    return this;
  }
  
  public GlideBuilder setDiskCache(DiskCache.Factory paramFactory) {
    this.diskCacheFactory = paramFactory;
    return this;
  }
  
  public GlideBuilder setDiskCacheExecutor(GlideExecutor paramGlideExecutor) {
    this.diskCacheExecutor = paramGlideExecutor;
    return this;
  }
  
  GlideBuilder setEngine(Engine paramEngine) {
    this.engine = paramEngine;
    return this;
  }
  
  public GlideBuilder setImageDecoderEnabledForBitmaps(boolean paramBoolean) {
    if (!BuildCompat.isAtLeastQ())
      return this; 
    this.isImageDecoderEnabledForBitmaps = paramBoolean;
    return this;
  }
  
  public GlideBuilder setIsActiveResourceRetentionAllowed(boolean paramBoolean) {
    this.isActiveResourceRetentionAllowed = paramBoolean;
    return this;
  }
  
  public GlideBuilder setLogLevel(int paramInt) {
    if (paramInt >= 2 && paramInt <= 6) {
      this.logLevel = paramInt;
      return this;
    } 
    throw new IllegalArgumentException("Log level must be one of Log.VERBOSE, Log.DEBUG, Log.INFO, Log.WARN, or Log.ERROR");
  }
  
  public GlideBuilder setLogRequestOrigins(boolean paramBoolean) {
    this.isLoggingRequestOriginsEnabled = paramBoolean;
    return this;
  }
  
  public GlideBuilder setMemoryCache(MemoryCache paramMemoryCache) {
    this.memoryCache = paramMemoryCache;
    return this;
  }
  
  public GlideBuilder setMemorySizeCalculator(MemorySizeCalculator.Builder paramBuilder) {
    return setMemorySizeCalculator(paramBuilder.build());
  }
  
  public GlideBuilder setMemorySizeCalculator(MemorySizeCalculator paramMemorySizeCalculator) {
    this.memorySizeCalculator = paramMemorySizeCalculator;
    return this;
  }
  
  void setRequestManagerFactory(RequestManagerRetriever.RequestManagerFactory paramRequestManagerFactory) {
    this.requestManagerFactory = paramRequestManagerFactory;
  }
  
  @Deprecated
  public GlideBuilder setResizeExecutor(GlideExecutor paramGlideExecutor) {
    return setSourceExecutor(paramGlideExecutor);
  }
  
  public GlideBuilder setSourceExecutor(GlideExecutor paramGlideExecutor) {
    this.sourceExecutor = paramGlideExecutor;
    return this;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/GlideBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */