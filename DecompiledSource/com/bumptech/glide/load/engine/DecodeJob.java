package com.bumptech.glide.load.engine;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import androidx.core.util.Pools;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.GlideTrace;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class DecodeJob<R> implements DataFetcherGenerator.FetcherReadyCallback, Runnable, Comparable<DecodeJob<?>>, FactoryPools.Poolable {
  private static final String TAG = "DecodeJob";
  
  private Callback<R> callback;
  
  private Key currentAttemptingKey;
  
  private Object currentData;
  
  private DataSource currentDataSource;
  
  private DataFetcher<?> currentFetcher;
  
  private volatile DataFetcherGenerator currentGenerator;
  
  private Key currentSourceKey;
  
  private Thread currentThread;
  
  private final DecodeHelper<R> decodeHelper = new DecodeHelper<R>();
  
  private final DeferredEncodeManager<?> deferredEncodeManager = new DeferredEncodeManager();
  
  private final DiskCacheProvider diskCacheProvider;
  
  private DiskCacheStrategy diskCacheStrategy;
  
  private GlideContext glideContext;
  
  private int height;
  
  private volatile boolean isCallbackNotified;
  
  private volatile boolean isCancelled;
  
  private EngineKey loadKey;
  
  private Object model;
  
  private boolean onlyRetrieveFromCache;
  
  private Options options;
  
  private int order;
  
  private final Pools.Pool<DecodeJob<?>> pool;
  
  private Priority priority;
  
  private final ReleaseManager releaseManager = new ReleaseManager();
  
  private RunReason runReason;
  
  private Key signature;
  
  private Stage stage;
  
  private long startFetchTime;
  
  private final StateVerifier stateVerifier = StateVerifier.newInstance();
  
  private final List<Throwable> throwables = new ArrayList<Throwable>();
  
  private int width;
  
  DecodeJob(DiskCacheProvider paramDiskCacheProvider, Pools.Pool<DecodeJob<?>> paramPool) {
    this.diskCacheProvider = paramDiskCacheProvider;
    this.pool = paramPool;
  }
  
  private <Data> Resource<R> decodeFromData(DataFetcher<?> paramDataFetcher, Data paramData, DataSource paramDataSource) throws GlideException {
    if (paramData == null) {
      paramDataFetcher.cleanup();
      return null;
    } 
    try {
      long l = LogTime.getLogTime();
      Resource<?> resource = decodeFromFetcher(paramData, paramDataSource);
      if (Log.isLoggable("DecodeJob", 2)) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append("Decoded result ");
        stringBuilder.append(resource);
        logWithTimeAndKey(stringBuilder.toString(), l);
      } 
      return (Resource)resource;
    } finally {
      paramDataFetcher.cleanup();
    } 
  }
  
  private <Data> Resource<R> decodeFromFetcher(Data paramData, DataSource paramDataSource) throws GlideException {
    return runLoadPath(paramData, paramDataSource, this.decodeHelper.getLoadPath((Class)paramData.getClass()));
  }
  
  private void decodeFromRetrievedData() {
    if (Log.isLoggable("DecodeJob", 2)) {
      long l = this.startFetchTime;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("data: ");
      stringBuilder.append(this.currentData);
      stringBuilder.append(", cache key: ");
      stringBuilder.append(this.currentSourceKey);
      stringBuilder.append(", fetcher: ");
      stringBuilder.append(this.currentFetcher);
      logWithTimeAndKey("Retrieved data", l, stringBuilder.toString());
    } 
    Resource<?> resource = null;
    try {
      Resource<?> resource1 = decodeFromData(this.currentFetcher, this.currentData, this.currentDataSource);
      resource = resource1;
    } catch (GlideException glideException) {
      glideException.setLoggingDetails(this.currentAttemptingKey, this.currentDataSource);
      this.throwables.add(glideException);
    } 
    if (resource != null) {
      notifyEncodeAndRelease((Resource)resource, this.currentDataSource);
    } else {
      runGenerators();
    } 
  }
  
  private DataFetcherGenerator getNextGenerator() {
    int i = null.$SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage[this.stage.ordinal()];
    if (i != 1) {
      if (i != 2) {
        if (i != 3) {
          if (i == 4)
            return null; 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Unrecognized stage: ");
          stringBuilder.append(this.stage);
          throw new IllegalStateException(stringBuilder.toString());
        } 
        return new SourceGenerator(this.decodeHelper, this);
      } 
      return new DataCacheGenerator(this.decodeHelper, this);
    } 
    return new ResourceCacheGenerator(this.decodeHelper, this);
  }
  
  private Stage getNextStage(Stage paramStage) {
    int i = null.$SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage[paramStage.ordinal()];
    if (i != 1) {
      if (i != 2) {
        if (i != 3 && i != 4) {
          if (i == 5) {
            if (this.diskCacheStrategy.decodeCachedResource()) {
              paramStage = Stage.RESOURCE_CACHE;
            } else {
              paramStage = getNextStage(Stage.RESOURCE_CACHE);
            } 
            return paramStage;
          } 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Unrecognized stage: ");
          stringBuilder.append(paramStage);
          throw new IllegalArgumentException(stringBuilder.toString());
        } 
        return Stage.FINISHED;
      } 
      if (this.onlyRetrieveFromCache) {
        paramStage = Stage.FINISHED;
      } else {
        paramStage = Stage.SOURCE;
      } 
      return paramStage;
    } 
    if (this.diskCacheStrategy.decodeCachedData()) {
      paramStage = Stage.DATA_CACHE;
    } else {
      paramStage = getNextStage(Stage.DATA_CACHE);
    } 
    return paramStage;
  }
  
  private Options getOptionsWithHardwareConfig(DataSource paramDataSource) {
    boolean bool1;
    Options options2 = this.options;
    if (Build.VERSION.SDK_INT < 26)
      return options2; 
    if (paramDataSource == DataSource.RESOURCE_DISK_CACHE || this.decodeHelper.isScaleOnlyOrNoTransform()) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    Boolean bool = (Boolean)options2.get(Downsampler.ALLOW_HARDWARE_CONFIG);
    if (bool != null && (!bool.booleanValue() || bool1))
      return options2; 
    Options options1 = new Options();
    options1.putAll(this.options);
    options1.set(Downsampler.ALLOW_HARDWARE_CONFIG, Boolean.valueOf(bool1));
    return options1;
  }
  
  private int getPriority() {
    return this.priority.ordinal();
  }
  
  private void logWithTimeAndKey(String paramString, long paramLong) {
    logWithTimeAndKey(paramString, paramLong, null);
  }
  
  private void logWithTimeAndKey(String paramString1, long paramLong, String paramString2) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString1);
    stringBuilder.append(" in ");
    stringBuilder.append(LogTime.getElapsedMillis(paramLong));
    stringBuilder.append(", load key: ");
    stringBuilder.append(this.loadKey);
    if (paramString2 != null) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(", ");
      stringBuilder1.append(paramString2);
      String str = stringBuilder1.toString();
    } else {
      paramString1 = "";
    } 
    stringBuilder.append(paramString1);
    stringBuilder.append(", thread: ");
    stringBuilder.append(Thread.currentThread().getName());
    Log.v("DecodeJob", stringBuilder.toString());
  }
  
  private void notifyComplete(Resource<R> paramResource, DataSource paramDataSource) {
    setNotifiedOrThrow();
    this.callback.onResourceReady(paramResource, paramDataSource);
  }
  
  private void notifyEncodeAndRelease(Resource<R> paramResource, DataSource paramDataSource) {
    if (paramResource instanceof Initializable)
      ((Initializable)paramResource).initialize(); 
    Resource<R> resource1 = null;
    Resource<R> resource2 = paramResource;
    if (this.deferredEncodeManager.hasResourceToEncode()) {
      resource2 = LockedResource.obtain(paramResource);
      resource1 = resource2;
    } 
    notifyComplete(resource2, paramDataSource);
    this.stage = Stage.ENCODE;
    try {
      if (this.deferredEncodeManager.hasResourceToEncode())
        this.deferredEncodeManager.encode(this.diskCacheProvider, this.options); 
      if (resource1 != null)
        resource1.unlock(); 
      return;
    } finally {
      if (resource1 != null)
        resource1.unlock(); 
    } 
  }
  
  private void notifyFailed() {
    setNotifiedOrThrow();
    GlideException glideException = new GlideException("Failed to load resource", new ArrayList<Throwable>(this.throwables));
    this.callback.onLoadFailed(glideException);
    onLoadFailed();
  }
  
  private void onEncodeComplete() {
    if (this.releaseManager.onEncodeComplete())
      releaseInternal(); 
  }
  
  private void onLoadFailed() {
    if (this.releaseManager.onFailed())
      releaseInternal(); 
  }
  
  private void releaseInternal() {
    this.releaseManager.reset();
    this.deferredEncodeManager.clear();
    this.decodeHelper.clear();
    this.isCallbackNotified = false;
    this.glideContext = null;
    this.signature = null;
    this.options = null;
    this.priority = null;
    this.loadKey = null;
    this.callback = null;
    this.stage = null;
    this.currentGenerator = null;
    this.currentThread = null;
    this.currentSourceKey = null;
    this.currentData = null;
    this.currentDataSource = null;
    this.currentFetcher = null;
    this.startFetchTime = 0L;
    this.isCancelled = false;
    this.model = null;
    this.throwables.clear();
    this.pool.release(this);
  }
  
  private void runGenerators() {
    boolean bool2;
    this.currentThread = Thread.currentThread();
    this.startFetchTime = LogTime.getLogTime();
    boolean bool1 = false;
    while (true) {
      bool2 = bool1;
      if (!this.isCancelled) {
        bool2 = bool1;
        if (this.currentGenerator != null) {
          bool1 = this.currentGenerator.startNext();
          bool2 = bool1;
          if (!bool1) {
            this.stage = getNextStage(this.stage);
            this.currentGenerator = getNextGenerator();
            if (this.stage == Stage.SOURCE) {
              reschedule();
              return;
            } 
            continue;
          } 
        } 
      } 
      break;
    } 
    if ((this.stage == Stage.FINISHED || this.isCancelled) && !bool2)
      notifyFailed(); 
  }
  
  private <Data, ResourceType> Resource<R> runLoadPath(Data paramData, DataSource paramDataSource, LoadPath<Data, ResourceType, R> paramLoadPath) throws GlideException {
    Options options = getOptionsWithHardwareConfig(paramDataSource);
    DataRewinder<Data> dataRewinder = this.glideContext.getRegistry().getRewinder(paramData);
    try {
      int i = this.width;
      int j = this.height;
      DecodeCallback<ResourceType> decodeCallback = new DecodeCallback();
      this(this, paramDataSource);
      return paramLoadPath.load(dataRewinder, options, i, j, decodeCallback);
    } finally {
      dataRewinder.cleanup();
    } 
  }
  
  private void runWrapped() {
    int i = null.$SwitchMap$com$bumptech$glide$load$engine$DecodeJob$RunReason[this.runReason.ordinal()];
    if (i != 1) {
      if (i != 2) {
        if (i == 3) {
          decodeFromRetrievedData();
        } else {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Unrecognized run reason: ");
          stringBuilder.append(this.runReason);
          throw new IllegalStateException(stringBuilder.toString());
        } 
      } else {
        runGenerators();
      } 
    } else {
      this.stage = getNextStage(Stage.INITIALIZE);
      this.currentGenerator = getNextGenerator();
      runGenerators();
    } 
  }
  
  private void setNotifiedOrThrow() {
    this.stateVerifier.throwIfRecycled();
    if (this.isCallbackNotified) {
      Throwable throwable;
      if (this.throwables.isEmpty()) {
        throwable = null;
      } else {
        List<Throwable> list = this.throwables;
        throwable = list.get(list.size() - 1);
      } 
      throw new IllegalStateException("Already notified", throwable);
    } 
    this.isCallbackNotified = true;
  }
  
  public void cancel() {
    this.isCancelled = true;
    DataFetcherGenerator dataFetcherGenerator = this.currentGenerator;
    if (dataFetcherGenerator != null)
      dataFetcherGenerator.cancel(); 
  }
  
  public int compareTo(DecodeJob<?> paramDecodeJob) {
    int i = getPriority() - paramDecodeJob.getPriority();
    int j = i;
    if (i == 0)
      j = this.order - paramDecodeJob.order; 
    return j;
  }
  
  public StateVerifier getVerifier() {
    return this.stateVerifier;
  }
  
  DecodeJob<R> init(GlideContext paramGlideContext, Object paramObject, EngineKey paramEngineKey, Key paramKey, int paramInt1, int paramInt2, Class<?> paramClass, Class<R> paramClass1, Priority paramPriority, DiskCacheStrategy paramDiskCacheStrategy, Map<Class<?>, Transformation<?>> paramMap, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, Options paramOptions, Callback<R> paramCallback, int paramInt3) {
    this.decodeHelper.init(paramGlideContext, paramObject, paramKey, paramInt1, paramInt2, paramDiskCacheStrategy, paramClass, paramClass1, paramPriority, paramOptions, paramMap, paramBoolean1, paramBoolean2, this.diskCacheProvider);
    this.glideContext = paramGlideContext;
    this.signature = paramKey;
    this.priority = paramPriority;
    this.loadKey = paramEngineKey;
    this.width = paramInt1;
    this.height = paramInt2;
    this.diskCacheStrategy = paramDiskCacheStrategy;
    this.onlyRetrieveFromCache = paramBoolean3;
    this.options = paramOptions;
    this.callback = paramCallback;
    this.order = paramInt3;
    this.runReason = RunReason.INITIALIZE;
    this.model = paramObject;
    return this;
  }
  
  public void onDataFetcherFailed(Key paramKey, Exception paramException, DataFetcher<?> paramDataFetcher, DataSource paramDataSource) {
    paramDataFetcher.cleanup();
    paramException = new GlideException("Fetching data failed", paramException);
    paramException.setLoggingDetails(paramKey, paramDataSource, paramDataFetcher.getDataClass());
    this.throwables.add(paramException);
    if (Thread.currentThread() != this.currentThread) {
      this.runReason = RunReason.SWITCH_TO_SOURCE_SERVICE;
      this.callback.reschedule(this);
    } else {
      runGenerators();
    } 
  }
  
  public void onDataFetcherReady(Key paramKey1, Object paramObject, DataFetcher<?> paramDataFetcher, DataSource paramDataSource, Key paramKey2) {
    this.currentSourceKey = paramKey1;
    this.currentData = paramObject;
    this.currentFetcher = paramDataFetcher;
    this.currentDataSource = paramDataSource;
    this.currentAttemptingKey = paramKey2;
    if (Thread.currentThread() != this.currentThread) {
      this.runReason = RunReason.DECODE_DATA;
      this.callback.reschedule(this);
    } else {
      GlideTrace.beginSection("DecodeJob.decodeFromRetrievedData");
      try {
        decodeFromRetrievedData();
        return;
      } finally {
        GlideTrace.endSection();
      } 
    } 
  }
  
  <Z> Resource<Z> onResourceDecoded(DataSource paramDataSource, Resource<Z> paramResource) {
    Resource<Z> resource1;
    Transformation transformation;
    EncodeStrategy encodeStrategy;
    Class<?> clazz = paramResource.get().getClass();
    DataSource dataSource = DataSource.RESOURCE_DISK_CACHE;
    Resource<Z> resource2 = null;
    if (paramDataSource != dataSource) {
      transformation = this.decodeHelper.getTransformation(clazz);
      resource1 = transformation.transform((Context)this.glideContext, paramResource, this.width, this.height);
    } else {
      resource1 = paramResource;
      transformation = null;
    } 
    if (!paramResource.equals(resource1))
      paramResource.recycle(); 
    if (this.decodeHelper.isResourceEncoderAvailable(resource1)) {
      ResourceEncoder<Z> resourceEncoder = this.decodeHelper.getResultEncoder(resource1);
      encodeStrategy = resourceEncoder.getEncodeStrategy(this.options);
    } else {
      encodeStrategy = EncodeStrategy.NONE;
      paramResource = resource2;
    } 
    boolean bool = this.decodeHelper.isSourceKey(this.currentSourceKey);
    resource2 = resource1;
    if (this.diskCacheStrategy.isResourceCacheable(bool ^ true, paramDataSource, encodeStrategy))
      if (paramResource != null) {
        DataCacheKey dataCacheKey;
        int i = null.$SwitchMap$com$bumptech$glide$load$EncodeStrategy[encodeStrategy.ordinal()];
        if (i != 1) {
          if (i == 2) {
            ResourceCacheKey resourceCacheKey = new ResourceCacheKey(this.decodeHelper.getArrayPool(), this.currentSourceKey, this.signature, this.width, this.height, transformation, clazz, this.options);
          } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown strategy: ");
            stringBuilder.append(encodeStrategy);
            throw new IllegalArgumentException(stringBuilder.toString());
          } 
        } else {
          dataCacheKey = new DataCacheKey(this.currentSourceKey, this.signature);
        } 
        resource2 = LockedResource.obtain(resource1);
        this.deferredEncodeManager.init(dataCacheKey, (ResourceEncoder<Z>)paramResource, (LockedResource<Z>)resource2);
      } else {
        throw new Registry.NoResultEncoderAvailableException(resource1.get().getClass());
      }  
    return resource2;
  }
  
  void release(boolean paramBoolean) {
    if (this.releaseManager.release(paramBoolean))
      releaseInternal(); 
  }
  
  public void reschedule() {
    this.runReason = RunReason.SWITCH_TO_SOURCE_SERVICE;
    this.callback.reschedule(this);
  }
  
  public void run() {
    Exception exception;
    GlideTrace.beginSectionFormat("DecodeJob#run(model=%s)", this.model);
    DataFetcher<?> dataFetcher = this.currentFetcher;
    try {
      if (this.isCancelled) {
        notifyFailed();
        if (dataFetcher != null)
          dataFetcher.cleanup(); 
        return;
      } 
      runWrapped();
      if (dataFetcher != null)
        dataFetcher.cleanup(); 
      return;
    } catch (CallbackException null) {
      throw exception;
    } finally {
      exception = null;
    } 
    if (dataFetcher != null)
      dataFetcher.cleanup(); 
    GlideTrace.endSection();
    throw exception;
  }
  
  boolean willDecodeFromCache() {
    Stage stage = getNextStage(Stage.INITIALIZE);
    return (stage == Stage.RESOURCE_CACHE || stage == Stage.DATA_CACHE);
  }
  
  static interface Callback<R> {
    void onLoadFailed(GlideException param1GlideException);
    
    void onResourceReady(Resource<R> param1Resource, DataSource param1DataSource);
    
    void reschedule(DecodeJob<?> param1DecodeJob);
  }
  
  private final class DecodeCallback<Z> implements DecodePath.DecodeCallback<Z> {
    private final DataSource dataSource;
    
    DecodeCallback(DataSource param1DataSource) {
      this.dataSource = param1DataSource;
    }
    
    public Resource<Z> onResourceDecoded(Resource<Z> param1Resource) {
      return DecodeJob.this.onResourceDecoded(this.dataSource, param1Resource);
    }
  }
  
  private static class DeferredEncodeManager<Z> {
    private ResourceEncoder<Z> encoder;
    
    private Key key;
    
    private LockedResource<Z> toEncode;
    
    void clear() {
      this.key = null;
      this.encoder = null;
      this.toEncode = null;
    }
    
    void encode(DecodeJob.DiskCacheProvider param1DiskCacheProvider, Options param1Options) {
      GlideTrace.beginSection("DecodeJob.encode");
      try {
        DiskCache diskCache = param1DiskCacheProvider.getDiskCache();
        Key key = this.key;
        DataCacheWriter dataCacheWriter = new DataCacheWriter();
        this((Encoder)this.encoder, this.toEncode, param1Options);
        diskCache.put(key, dataCacheWriter);
        return;
      } finally {
        this.toEncode.unlock();
        GlideTrace.endSection();
      } 
    }
    
    boolean hasResourceToEncode() {
      boolean bool;
      if (this.toEncode != null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    <X> void init(Key param1Key, ResourceEncoder<X> param1ResourceEncoder, LockedResource<X> param1LockedResource) {
      this.key = param1Key;
      this.encoder = param1ResourceEncoder;
      this.toEncode = param1LockedResource;
    }
  }
  
  static interface DiskCacheProvider {
    DiskCache getDiskCache();
  }
  
  private static class ReleaseManager {
    private boolean isEncodeComplete;
    
    private boolean isFailed;
    
    private boolean isReleased;
    
    private boolean isComplete(boolean param1Boolean) {
      if ((this.isFailed || param1Boolean || this.isEncodeComplete) && this.isReleased) {
        param1Boolean = true;
      } else {
        param1Boolean = false;
      } 
      return param1Boolean;
    }
    
    boolean onEncodeComplete() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: iconst_1
      //   4: putfield isEncodeComplete : Z
      //   7: aload_0
      //   8: iconst_0
      //   9: invokespecial isComplete : (Z)Z
      //   12: istore_1
      //   13: aload_0
      //   14: monitorexit
      //   15: iload_1
      //   16: ireturn
      //   17: astore_2
      //   18: aload_0
      //   19: monitorexit
      //   20: aload_2
      //   21: athrow
      // Exception table:
      //   from	to	target	type
      //   2	13	17	finally
    }
    
    boolean onFailed() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: iconst_1
      //   4: putfield isFailed : Z
      //   7: aload_0
      //   8: iconst_0
      //   9: invokespecial isComplete : (Z)Z
      //   12: istore_1
      //   13: aload_0
      //   14: monitorexit
      //   15: iload_1
      //   16: ireturn
      //   17: astore_2
      //   18: aload_0
      //   19: monitorexit
      //   20: aload_2
      //   21: athrow
      // Exception table:
      //   from	to	target	type
      //   2	13	17	finally
    }
    
    boolean release(boolean param1Boolean) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: iconst_1
      //   4: putfield isReleased : Z
      //   7: aload_0
      //   8: iload_1
      //   9: invokespecial isComplete : (Z)Z
      //   12: istore_1
      //   13: aload_0
      //   14: monitorexit
      //   15: iload_1
      //   16: ireturn
      //   17: astore_2
      //   18: aload_0
      //   19: monitorexit
      //   20: aload_2
      //   21: athrow
      // Exception table:
      //   from	to	target	type
      //   2	13	17	finally
    }
    
    void reset() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: iconst_0
      //   4: putfield isEncodeComplete : Z
      //   7: aload_0
      //   8: iconst_0
      //   9: putfield isReleased : Z
      //   12: aload_0
      //   13: iconst_0
      //   14: putfield isFailed : Z
      //   17: aload_0
      //   18: monitorexit
      //   19: return
      //   20: astore_1
      //   21: aload_0
      //   22: monitorexit
      //   23: aload_1
      //   24: athrow
      // Exception table:
      //   from	to	target	type
      //   2	17	20	finally
    }
  }
  
  private enum RunReason {
    DECODE_DATA, INITIALIZE, SWITCH_TO_SOURCE_SERVICE;
    
    static {
      $VALUES = new RunReason[] { INITIALIZE, SWITCH_TO_SOURCE_SERVICE, DECODE_DATA };
    }
  }
  
  private enum Stage {
    INITIALIZE, DATA_CACHE, ENCODE, FINISHED, RESOURCE_CACHE, SOURCE;
    
    static {
      ENCODE = new Stage("ENCODE", 4);
      FINISHED = new Stage("FINISHED", 5);
      $VALUES = new Stage[] { INITIALIZE, RESOURCE_CACHE, DATA_CACHE, SOURCE, ENCODE, FINISHED };
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/DecodeJob.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */