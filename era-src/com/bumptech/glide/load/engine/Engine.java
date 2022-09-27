package com.bumptech.glide.load.engine;

import android.util.Log;
import androidx.core.util.Pools;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.util.Executors;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.pool.FactoryPools;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class Engine implements EngineJobListener, MemoryCache.ResourceRemovedListener, EngineResource.ResourceListener {
  private static final int JOB_POOL_SIZE = 150;
  
  private static final String TAG = "Engine";
  
  private static final boolean VERBOSE_IS_LOGGABLE = Log.isLoggable("Engine", 2);
  
  private final ActiveResources activeResources;
  
  private final MemoryCache cache;
  
  private final DecodeJobFactory decodeJobFactory;
  
  private final LazyDiskCacheProvider diskCacheProvider;
  
  private final EngineJobFactory engineJobFactory;
  
  private final Jobs jobs;
  
  private final EngineKeyFactory keyFactory;
  
  private final ResourceRecycler resourceRecycler;
  
  Engine(MemoryCache paramMemoryCache, DiskCache.Factory paramFactory, GlideExecutor paramGlideExecutor1, GlideExecutor paramGlideExecutor2, GlideExecutor paramGlideExecutor3, GlideExecutor paramGlideExecutor4, Jobs paramJobs, EngineKeyFactory paramEngineKeyFactory, ActiveResources paramActiveResources, EngineJobFactory paramEngineJobFactory, DecodeJobFactory paramDecodeJobFactory, ResourceRecycler paramResourceRecycler, boolean paramBoolean) {
    Jobs jobs;
    EngineJobFactory engineJobFactory;
    DecodeJobFactory decodeJobFactory;
    this.cache = paramMemoryCache;
    this.diskCacheProvider = new LazyDiskCacheProvider(paramFactory);
    if (paramActiveResources == null)
      paramActiveResources = new ActiveResources(paramBoolean); 
    this.activeResources = paramActiveResources;
    paramActiveResources.setListener(this);
    if (paramEngineKeyFactory == null)
      paramEngineKeyFactory = new EngineKeyFactory(); 
    this.keyFactory = paramEngineKeyFactory;
    if (paramJobs == null) {
      jobs = new Jobs();
    } else {
      jobs = paramJobs;
    } 
    this.jobs = jobs;
    if (paramEngineJobFactory == null) {
      engineJobFactory = new EngineJobFactory(paramGlideExecutor1, paramGlideExecutor2, paramGlideExecutor3, paramGlideExecutor4, this, this);
    } else {
      engineJobFactory = paramEngineJobFactory;
    } 
    this.engineJobFactory = engineJobFactory;
    if (paramDecodeJobFactory == null) {
      decodeJobFactory = new DecodeJobFactory(this.diskCacheProvider);
    } else {
      decodeJobFactory = paramDecodeJobFactory;
    } 
    this.decodeJobFactory = decodeJobFactory;
    if (paramResourceRecycler == null)
      paramResourceRecycler = new ResourceRecycler(); 
    this.resourceRecycler = paramResourceRecycler;
    paramMemoryCache.setResourceRemovedListener(this);
  }
  
  public Engine(MemoryCache paramMemoryCache, DiskCache.Factory paramFactory, GlideExecutor paramGlideExecutor1, GlideExecutor paramGlideExecutor2, GlideExecutor paramGlideExecutor3, GlideExecutor paramGlideExecutor4, boolean paramBoolean) {
    this(paramMemoryCache, paramFactory, paramGlideExecutor1, paramGlideExecutor2, paramGlideExecutor3, paramGlideExecutor4, null, null, null, null, null, null, paramBoolean);
  }
  
  private EngineResource<?> getEngineResourceFromCache(Key paramKey) {
    EngineResource<?> engineResource;
    Resource<?> resource = this.cache.remove(paramKey);
    if (resource == null) {
      paramKey = null;
    } else if (resource instanceof EngineResource) {
      engineResource = (EngineResource)resource;
    } else {
      engineResource = new EngineResource(resource, true, true, (Key)engineResource, this);
    } 
    return engineResource;
  }
  
  private EngineResource<?> loadFromActiveResources(Key paramKey) {
    EngineResource<?> engineResource = this.activeResources.get(paramKey);
    if (engineResource != null)
      engineResource.acquire(); 
    return engineResource;
  }
  
  private EngineResource<?> loadFromCache(Key paramKey) {
    EngineResource<?> engineResource = getEngineResourceFromCache(paramKey);
    if (engineResource != null) {
      engineResource.acquire();
      this.activeResources.activate(paramKey, engineResource);
    } 
    return engineResource;
  }
  
  private EngineResource<?> loadFromMemory(EngineKey paramEngineKey, boolean paramBoolean, long paramLong) {
    if (!paramBoolean)
      return null; 
    EngineResource<?> engineResource = loadFromActiveResources(paramEngineKey);
    if (engineResource != null) {
      if (VERBOSE_IS_LOGGABLE)
        logWithTimeAndKey("Loaded resource from active resources", paramLong, paramEngineKey); 
      return engineResource;
    } 
    engineResource = loadFromCache(paramEngineKey);
    if (engineResource != null) {
      if (VERBOSE_IS_LOGGABLE)
        logWithTimeAndKey("Loaded resource from cache", paramLong, paramEngineKey); 
      return engineResource;
    } 
    return null;
  }
  
  private static void logWithTimeAndKey(String paramString, long paramLong, Key paramKey) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append(" in ");
    stringBuilder.append(LogTime.getElapsedMillis(paramLong));
    stringBuilder.append("ms, key: ");
    stringBuilder.append(paramKey);
    Log.v("Engine", stringBuilder.toString());
  }
  
  private <R> LoadStatus waitForExistingOrStartNewJob(GlideContext paramGlideContext, Object paramObject, Key paramKey, int paramInt1, int paramInt2, Class<?> paramClass, Class<R> paramClass1, Priority paramPriority, DiskCacheStrategy paramDiskCacheStrategy, Map<Class<?>, Transformation<?>> paramMap, boolean paramBoolean1, boolean paramBoolean2, Options paramOptions, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, boolean paramBoolean6, ResourceCallback paramResourceCallback, Executor paramExecutor, EngineKey paramEngineKey, long paramLong) {
    EngineJob<?> engineJob = this.jobs.get(paramEngineKey, paramBoolean6);
    if (engineJob != null) {
      engineJob.addCallback(paramResourceCallback, paramExecutor);
      if (VERBOSE_IS_LOGGABLE)
        logWithTimeAndKey("Added to existing load", paramLong, paramEngineKey); 
      return new LoadStatus(paramResourceCallback, engineJob);
    } 
    engineJob = this.engineJobFactory.build(paramEngineKey, paramBoolean3, paramBoolean4, paramBoolean5, paramBoolean6);
    DecodeJob<R> decodeJob = this.decodeJobFactory.build(paramGlideContext, paramObject, paramEngineKey, paramKey, paramInt1, paramInt2, paramClass, paramClass1, paramPriority, paramDiskCacheStrategy, paramMap, paramBoolean1, paramBoolean2, paramBoolean6, paramOptions, (DecodeJob.Callback)engineJob);
    this.jobs.put(paramEngineKey, engineJob);
    engineJob.addCallback(paramResourceCallback, paramExecutor);
    engineJob.start(decodeJob);
    if (VERBOSE_IS_LOGGABLE)
      logWithTimeAndKey("Started new load", paramLong, paramEngineKey); 
    return new LoadStatus(paramResourceCallback, engineJob);
  }
  
  public void clearDiskCache() {
    this.diskCacheProvider.getDiskCache().clear();
  }
  
  public <R> LoadStatus load(GlideContext paramGlideContext, Object paramObject, Key paramKey, int paramInt1, int paramInt2, Class<?> paramClass, Class<R> paramClass1, Priority paramPriority, DiskCacheStrategy paramDiskCacheStrategy, Map<Class<?>, Transformation<?>> paramMap, boolean paramBoolean1, boolean paramBoolean2, Options paramOptions, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, boolean paramBoolean6, ResourceCallback paramResourceCallback, Executor paramExecutor) {
    // Byte code:
    //   0: getstatic com/bumptech/glide/load/engine/Engine.VERBOSE_IS_LOGGABLE : Z
    //   3: ifeq -> 14
    //   6: invokestatic getLogTime : ()J
    //   9: lstore #20
    //   11: goto -> 17
    //   14: lconst_0
    //   15: lstore #20
    //   17: aload_0
    //   18: getfield keyFactory : Lcom/bumptech/glide/load/engine/EngineKeyFactory;
    //   21: aload_2
    //   22: aload_3
    //   23: iload #4
    //   25: iload #5
    //   27: aload #10
    //   29: aload #6
    //   31: aload #7
    //   33: aload #13
    //   35: invokevirtual buildKey : (Ljava/lang/Object;Lcom/bumptech/glide/load/Key;IILjava/util/Map;Ljava/lang/Class;Ljava/lang/Class;Lcom/bumptech/glide/load/Options;)Lcom/bumptech/glide/load/engine/EngineKey;
    //   38: astore #22
    //   40: aload_0
    //   41: monitorenter
    //   42: aload_0
    //   43: aload #22
    //   45: iload #14
    //   47: lload #20
    //   49: invokespecial loadFromMemory : (Lcom/bumptech/glide/load/engine/EngineKey;ZJ)Lcom/bumptech/glide/load/engine/EngineResource;
    //   52: astore #23
    //   54: aload #23
    //   56: ifnonnull -> 107
    //   59: aload_0
    //   60: aload_1
    //   61: aload_2
    //   62: aload_3
    //   63: iload #4
    //   65: iload #5
    //   67: aload #6
    //   69: aload #7
    //   71: aload #8
    //   73: aload #9
    //   75: aload #10
    //   77: iload #11
    //   79: iload #12
    //   81: aload #13
    //   83: iload #14
    //   85: iload #15
    //   87: iload #16
    //   89: iload #17
    //   91: aload #18
    //   93: aload #19
    //   95: aload #22
    //   97: lload #20
    //   99: invokespecial waitForExistingOrStartNewJob : (Lcom/bumptech/glide/GlideContext;Ljava/lang/Object;Lcom/bumptech/glide/load/Key;IILjava/lang/Class;Ljava/lang/Class;Lcom/bumptech/glide/Priority;Lcom/bumptech/glide/load/engine/DiskCacheStrategy;Ljava/util/Map;ZZLcom/bumptech/glide/load/Options;ZZZZLcom/bumptech/glide/request/ResourceCallback;Ljava/util/concurrent/Executor;Lcom/bumptech/glide/load/engine/EngineKey;J)Lcom/bumptech/glide/load/engine/Engine$LoadStatus;
    //   102: astore_1
    //   103: aload_0
    //   104: monitorexit
    //   105: aload_1
    //   106: areturn
    //   107: aload_0
    //   108: monitorexit
    //   109: aload #18
    //   111: aload #23
    //   113: getstatic com/bumptech/glide/load/DataSource.MEMORY_CACHE : Lcom/bumptech/glide/load/DataSource;
    //   116: invokeinterface onResourceReady : (Lcom/bumptech/glide/load/engine/Resource;Lcom/bumptech/glide/load/DataSource;)V
    //   121: aconst_null
    //   122: areturn
    //   123: astore_1
    //   124: aload_0
    //   125: monitorexit
    //   126: aload_1
    //   127: athrow
    // Exception table:
    //   from	to	target	type
    //   42	54	123	finally
    //   59	105	123	finally
    //   107	109	123	finally
    //   124	126	123	finally
  }
  
  public void onEngineJobCancelled(EngineJob<?> paramEngineJob, Key paramKey) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield jobs : Lcom/bumptech/glide/load/engine/Jobs;
    //   6: aload_2
    //   7: aload_1
    //   8: invokevirtual removeIfCurrent : (Lcom/bumptech/glide/load/Key;Lcom/bumptech/glide/load/engine/EngineJob;)V
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	14	finally
  }
  
  public void onEngineJobComplete(EngineJob<?> paramEngineJob, Key paramKey, EngineResource<?> paramEngineResource) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_3
    //   3: ifnull -> 22
    //   6: aload_3
    //   7: invokevirtual isMemoryCacheable : ()Z
    //   10: ifeq -> 22
    //   13: aload_0
    //   14: getfield activeResources : Lcom/bumptech/glide/load/engine/ActiveResources;
    //   17: aload_2
    //   18: aload_3
    //   19: invokevirtual activate : (Lcom/bumptech/glide/load/Key;Lcom/bumptech/glide/load/engine/EngineResource;)V
    //   22: aload_0
    //   23: getfield jobs : Lcom/bumptech/glide/load/engine/Jobs;
    //   26: aload_2
    //   27: aload_1
    //   28: invokevirtual removeIfCurrent : (Lcom/bumptech/glide/load/Key;Lcom/bumptech/glide/load/engine/EngineJob;)V
    //   31: aload_0
    //   32: monitorexit
    //   33: return
    //   34: astore_1
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_1
    //   38: athrow
    // Exception table:
    //   from	to	target	type
    //   6	22	34	finally
    //   22	31	34	finally
  }
  
  public void onResourceReleased(Key paramKey, EngineResource<?> paramEngineResource) {
    this.activeResources.deactivate(paramKey);
    if (paramEngineResource.isMemoryCacheable()) {
      this.cache.put(paramKey, paramEngineResource);
    } else {
      this.resourceRecycler.recycle(paramEngineResource);
    } 
  }
  
  public void onResourceRemoved(Resource<?> paramResource) {
    this.resourceRecycler.recycle(paramResource);
  }
  
  public void release(Resource<?> paramResource) {
    if (paramResource instanceof EngineResource) {
      ((EngineResource)paramResource).release();
      return;
    } 
    throw new IllegalArgumentException("Cannot release anything but an EngineResource");
  }
  
  public void shutdown() {
    this.engineJobFactory.shutdown();
    this.diskCacheProvider.clearDiskCacheIfCreated();
    this.activeResources.shutdown();
  }
  
  static class DecodeJobFactory {
    private int creationOrder;
    
    final DecodeJob.DiskCacheProvider diskCacheProvider;
    
    final Pools.Pool<DecodeJob<?>> pool = FactoryPools.threadSafe(150, new FactoryPools.Factory<DecodeJob<?>>() {
          public DecodeJob<?> create() {
            return new DecodeJob(Engine.DecodeJobFactory.this.diskCacheProvider, Engine.DecodeJobFactory.this.pool);
          }
        });
    
    DecodeJobFactory(DecodeJob.DiskCacheProvider param1DiskCacheProvider) {
      this.diskCacheProvider = param1DiskCacheProvider;
    }
    
    <R> DecodeJob<R> build(GlideContext param1GlideContext, Object param1Object, EngineKey param1EngineKey, Key param1Key, int param1Int1, int param1Int2, Class<?> param1Class, Class<R> param1Class1, Priority param1Priority, DiskCacheStrategy param1DiskCacheStrategy, Map<Class<?>, Transformation<?>> param1Map, boolean param1Boolean1, boolean param1Boolean2, boolean param1Boolean3, Options param1Options, DecodeJob.Callback<R> param1Callback) {
      DecodeJob<R> decodeJob = (DecodeJob)Preconditions.checkNotNull(this.pool.acquire());
      int i = this.creationOrder;
      this.creationOrder = i + 1;
      return decodeJob.init(param1GlideContext, param1Object, param1EngineKey, param1Key, param1Int1, param1Int2, param1Class, param1Class1, param1Priority, param1DiskCacheStrategy, param1Map, param1Boolean1, param1Boolean2, param1Boolean3, param1Options, param1Callback, i);
    }
  }
  
  class null implements FactoryPools.Factory<DecodeJob<?>> {
    public DecodeJob<?> create() {
      return new DecodeJob(this.this$0.diskCacheProvider, this.this$0.pool);
    }
  }
  
  static class EngineJobFactory {
    final GlideExecutor animationExecutor;
    
    final GlideExecutor diskCacheExecutor;
    
    final EngineJobListener engineJobListener;
    
    final Pools.Pool<EngineJob<?>> pool = FactoryPools.threadSafe(150, new FactoryPools.Factory<EngineJob<?>>() {
          public EngineJob<?> create() {
            return new EngineJob(Engine.EngineJobFactory.this.diskCacheExecutor, Engine.EngineJobFactory.this.sourceExecutor, Engine.EngineJobFactory.this.sourceUnlimitedExecutor, Engine.EngineJobFactory.this.animationExecutor, Engine.EngineJobFactory.this.engineJobListener, Engine.EngineJobFactory.this.resourceListener, Engine.EngineJobFactory.this.pool);
          }
        });
    
    final EngineResource.ResourceListener resourceListener;
    
    final GlideExecutor sourceExecutor;
    
    final GlideExecutor sourceUnlimitedExecutor;
    
    EngineJobFactory(GlideExecutor param1GlideExecutor1, GlideExecutor param1GlideExecutor2, GlideExecutor param1GlideExecutor3, GlideExecutor param1GlideExecutor4, EngineJobListener param1EngineJobListener, EngineResource.ResourceListener param1ResourceListener) {
      this.diskCacheExecutor = param1GlideExecutor1;
      this.sourceExecutor = param1GlideExecutor2;
      this.sourceUnlimitedExecutor = param1GlideExecutor3;
      this.animationExecutor = param1GlideExecutor4;
      this.engineJobListener = param1EngineJobListener;
      this.resourceListener = param1ResourceListener;
    }
    
    <R> EngineJob<R> build(Key param1Key, boolean param1Boolean1, boolean param1Boolean2, boolean param1Boolean3, boolean param1Boolean4) {
      return ((EngineJob<R>)Preconditions.checkNotNull(this.pool.acquire())).init(param1Key, param1Boolean1, param1Boolean2, param1Boolean3, param1Boolean4);
    }
    
    void shutdown() {
      Executors.shutdownAndAwaitTermination((ExecutorService)this.diskCacheExecutor);
      Executors.shutdownAndAwaitTermination((ExecutorService)this.sourceExecutor);
      Executors.shutdownAndAwaitTermination((ExecutorService)this.sourceUnlimitedExecutor);
      Executors.shutdownAndAwaitTermination((ExecutorService)this.animationExecutor);
    }
  }
  
  class null implements FactoryPools.Factory<EngineJob<?>> {
    public EngineJob<?> create() {
      return new EngineJob(this.this$0.diskCacheExecutor, this.this$0.sourceExecutor, this.this$0.sourceUnlimitedExecutor, this.this$0.animationExecutor, this.this$0.engineJobListener, this.this$0.resourceListener, this.this$0.pool);
    }
  }
  
  private static class LazyDiskCacheProvider implements DecodeJob.DiskCacheProvider {
    private volatile DiskCache diskCache;
    
    private final DiskCache.Factory factory;
    
    LazyDiskCacheProvider(DiskCache.Factory param1Factory) {
      this.factory = param1Factory;
    }
    
    void clearDiskCacheIfCreated() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield diskCache : Lcom/bumptech/glide/load/engine/cache/DiskCache;
      //   6: astore_1
      //   7: aload_1
      //   8: ifnonnull -> 14
      //   11: aload_0
      //   12: monitorexit
      //   13: return
      //   14: aload_0
      //   15: getfield diskCache : Lcom/bumptech/glide/load/engine/cache/DiskCache;
      //   18: invokeinterface clear : ()V
      //   23: aload_0
      //   24: monitorexit
      //   25: return
      //   26: astore_1
      //   27: aload_0
      //   28: monitorexit
      //   29: aload_1
      //   30: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	26	finally
      //   14	23	26	finally
    }
    
    public DiskCache getDiskCache() {
      // Byte code:
      //   0: aload_0
      //   1: getfield diskCache : Lcom/bumptech/glide/load/engine/cache/DiskCache;
      //   4: ifnonnull -> 59
      //   7: aload_0
      //   8: monitorenter
      //   9: aload_0
      //   10: getfield diskCache : Lcom/bumptech/glide/load/engine/cache/DiskCache;
      //   13: ifnonnull -> 29
      //   16: aload_0
      //   17: aload_0
      //   18: getfield factory : Lcom/bumptech/glide/load/engine/cache/DiskCache$Factory;
      //   21: invokeinterface build : ()Lcom/bumptech/glide/load/engine/cache/DiskCache;
      //   26: putfield diskCache : Lcom/bumptech/glide/load/engine/cache/DiskCache;
      //   29: aload_0
      //   30: getfield diskCache : Lcom/bumptech/glide/load/engine/cache/DiskCache;
      //   33: ifnonnull -> 49
      //   36: new com/bumptech/glide/load/engine/cache/DiskCacheAdapter
      //   39: astore_1
      //   40: aload_1
      //   41: invokespecial <init> : ()V
      //   44: aload_0
      //   45: aload_1
      //   46: putfield diskCache : Lcom/bumptech/glide/load/engine/cache/DiskCache;
      //   49: aload_0
      //   50: monitorexit
      //   51: goto -> 59
      //   54: astore_1
      //   55: aload_0
      //   56: monitorexit
      //   57: aload_1
      //   58: athrow
      //   59: aload_0
      //   60: getfield diskCache : Lcom/bumptech/glide/load/engine/cache/DiskCache;
      //   63: areturn
      // Exception table:
      //   from	to	target	type
      //   9	29	54	finally
      //   29	49	54	finally
      //   49	51	54	finally
      //   55	57	54	finally
    }
  }
  
  public class LoadStatus {
    private final ResourceCallback cb;
    
    private final EngineJob<?> engineJob;
    
    LoadStatus(ResourceCallback param1ResourceCallback, EngineJob<?> param1EngineJob) {
      this.cb = param1ResourceCallback;
      this.engineJob = param1EngineJob;
    }
    
    public void cancel() {
      synchronized (Engine.this) {
        this.engineJob.removeCallback(this.cb);
        return;
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/Engine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */