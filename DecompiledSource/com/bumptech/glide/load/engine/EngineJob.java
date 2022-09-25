package com.bumptech.glide.load.engine;

import androidx.core.util.Pools;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.util.Executors;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

class EngineJob<R> implements DecodeJob.Callback<R>, FactoryPools.Poolable {
  private static final EngineResourceFactory DEFAULT_FACTORY = new EngineResourceFactory();
  
  private final GlideExecutor animationExecutor;
  
  final ResourceCallbacksAndExecutors cbs = new ResourceCallbacksAndExecutors();
  
  DataSource dataSource;
  
  private DecodeJob<R> decodeJob;
  
  private final GlideExecutor diskCacheExecutor;
  
  private final EngineJobListener engineJobListener;
  
  EngineResource<?> engineResource;
  
  private final EngineResourceFactory engineResourceFactory;
  
  GlideException exception;
  
  private boolean hasLoadFailed;
  
  private boolean hasResource;
  
  private boolean isCacheable;
  
  private volatile boolean isCancelled;
  
  private Key key;
  
  private boolean onlyRetrieveFromCache;
  
  private final AtomicInteger pendingCallbacks = new AtomicInteger();
  
  private final Pools.Pool<EngineJob<?>> pool;
  
  private Resource<?> resource;
  
  private final EngineResource.ResourceListener resourceListener;
  
  private final GlideExecutor sourceExecutor;
  
  private final GlideExecutor sourceUnlimitedExecutor;
  
  private final StateVerifier stateVerifier = StateVerifier.newInstance();
  
  private boolean useAnimationPool;
  
  private boolean useUnlimitedSourceGeneratorPool;
  
  EngineJob(GlideExecutor paramGlideExecutor1, GlideExecutor paramGlideExecutor2, GlideExecutor paramGlideExecutor3, GlideExecutor paramGlideExecutor4, EngineJobListener paramEngineJobListener, EngineResource.ResourceListener paramResourceListener, Pools.Pool<EngineJob<?>> paramPool) {
    this(paramGlideExecutor1, paramGlideExecutor2, paramGlideExecutor3, paramGlideExecutor4, paramEngineJobListener, paramResourceListener, paramPool, DEFAULT_FACTORY);
  }
  
  EngineJob(GlideExecutor paramGlideExecutor1, GlideExecutor paramGlideExecutor2, GlideExecutor paramGlideExecutor3, GlideExecutor paramGlideExecutor4, EngineJobListener paramEngineJobListener, EngineResource.ResourceListener paramResourceListener, Pools.Pool<EngineJob<?>> paramPool, EngineResourceFactory paramEngineResourceFactory) {
    this.diskCacheExecutor = paramGlideExecutor1;
    this.sourceExecutor = paramGlideExecutor2;
    this.sourceUnlimitedExecutor = paramGlideExecutor3;
    this.animationExecutor = paramGlideExecutor4;
    this.engineJobListener = paramEngineJobListener;
    this.resourceListener = paramResourceListener;
    this.pool = paramPool;
    this.engineResourceFactory = paramEngineResourceFactory;
  }
  
  private GlideExecutor getActiveSourceExecutor() {
    GlideExecutor glideExecutor;
    if (this.useUnlimitedSourceGeneratorPool) {
      glideExecutor = this.sourceUnlimitedExecutor;
    } else if (this.useAnimationPool) {
      glideExecutor = this.animationExecutor;
    } else {
      glideExecutor = this.sourceExecutor;
    } 
    return glideExecutor;
  }
  
  private boolean isDone() {
    return (this.hasLoadFailed || this.hasResource || this.isCancelled);
  }
  
  private void release() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield key : Lcom/bumptech/glide/load/Key;
    //   6: ifnull -> 83
    //   9: aload_0
    //   10: getfield cbs : Lcom/bumptech/glide/load/engine/EngineJob$ResourceCallbacksAndExecutors;
    //   13: invokevirtual clear : ()V
    //   16: aload_0
    //   17: aconst_null
    //   18: putfield key : Lcom/bumptech/glide/load/Key;
    //   21: aload_0
    //   22: aconst_null
    //   23: putfield engineResource : Lcom/bumptech/glide/load/engine/EngineResource;
    //   26: aload_0
    //   27: aconst_null
    //   28: putfield resource : Lcom/bumptech/glide/load/engine/Resource;
    //   31: aload_0
    //   32: iconst_0
    //   33: putfield hasLoadFailed : Z
    //   36: aload_0
    //   37: iconst_0
    //   38: putfield isCancelled : Z
    //   41: aload_0
    //   42: iconst_0
    //   43: putfield hasResource : Z
    //   46: aload_0
    //   47: getfield decodeJob : Lcom/bumptech/glide/load/engine/DecodeJob;
    //   50: iconst_0
    //   51: invokevirtual release : (Z)V
    //   54: aload_0
    //   55: aconst_null
    //   56: putfield decodeJob : Lcom/bumptech/glide/load/engine/DecodeJob;
    //   59: aload_0
    //   60: aconst_null
    //   61: putfield exception : Lcom/bumptech/glide/load/engine/GlideException;
    //   64: aload_0
    //   65: aconst_null
    //   66: putfield dataSource : Lcom/bumptech/glide/load/DataSource;
    //   69: aload_0
    //   70: getfield pool : Landroidx/core/util/Pools$Pool;
    //   73: aload_0
    //   74: invokeinterface release : (Ljava/lang/Object;)Z
    //   79: pop
    //   80: aload_0
    //   81: monitorexit
    //   82: return
    //   83: new java/lang/IllegalArgumentException
    //   86: astore_1
    //   87: aload_1
    //   88: invokespecial <init> : ()V
    //   91: aload_1
    //   92: athrow
    //   93: astore_1
    //   94: aload_0
    //   95: monitorexit
    //   96: aload_1
    //   97: athrow
    // Exception table:
    //   from	to	target	type
    //   2	80	93	finally
    //   83	93	93	finally
  }
  
  void addCallback(ResourceCallback paramResourceCallback, Executor paramExecutor) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield stateVerifier : Lcom/bumptech/glide/util/pool/StateVerifier;
    //   6: invokevirtual throwIfRecycled : ()V
    //   9: aload_0
    //   10: getfield cbs : Lcom/bumptech/glide/load/engine/EngineJob$ResourceCallbacksAndExecutors;
    //   13: aload_1
    //   14: aload_2
    //   15: invokevirtual add : (Lcom/bumptech/glide/request/ResourceCallback;Ljava/util/concurrent/Executor;)V
    //   18: aload_0
    //   19: getfield hasResource : Z
    //   22: istore_3
    //   23: iconst_1
    //   24: istore #4
    //   26: iload_3
    //   27: ifeq -> 58
    //   30: aload_0
    //   31: iconst_1
    //   32: invokevirtual incrementPendingCallbacks : (I)V
    //   35: new com/bumptech/glide/load/engine/EngineJob$CallResourceReady
    //   38: astore #5
    //   40: aload #5
    //   42: aload_0
    //   43: aload_1
    //   44: invokespecial <init> : (Lcom/bumptech/glide/load/engine/EngineJob;Lcom/bumptech/glide/request/ResourceCallback;)V
    //   47: aload_2
    //   48: aload #5
    //   50: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   55: goto -> 113
    //   58: aload_0
    //   59: getfield hasLoadFailed : Z
    //   62: ifeq -> 93
    //   65: aload_0
    //   66: iconst_1
    //   67: invokevirtual incrementPendingCallbacks : (I)V
    //   70: new com/bumptech/glide/load/engine/EngineJob$CallLoadFailed
    //   73: astore #5
    //   75: aload #5
    //   77: aload_0
    //   78: aload_1
    //   79: invokespecial <init> : (Lcom/bumptech/glide/load/engine/EngineJob;Lcom/bumptech/glide/request/ResourceCallback;)V
    //   82: aload_2
    //   83: aload #5
    //   85: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   90: goto -> 113
    //   93: aload_0
    //   94: getfield isCancelled : Z
    //   97: ifne -> 103
    //   100: goto -> 106
    //   103: iconst_0
    //   104: istore #4
    //   106: iload #4
    //   108: ldc 'Cannot add callbacks to a cancelled EngineJob'
    //   110: invokestatic checkArgument : (ZLjava/lang/String;)V
    //   113: aload_0
    //   114: monitorexit
    //   115: return
    //   116: astore_1
    //   117: aload_0
    //   118: monitorexit
    //   119: aload_1
    //   120: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	116	finally
    //   30	55	116	finally
    //   58	90	116	finally
    //   93	100	116	finally
    //   106	113	116	finally
  }
  
  void callCallbackOnLoadFailed(ResourceCallback paramResourceCallback) {
    try {
      return;
    } finally {
      paramResourceCallback = null;
    } 
  }
  
  void callCallbackOnResourceReady(ResourceCallback paramResourceCallback) {
    try {
      return;
    } finally {
      paramResourceCallback = null;
    } 
  }
  
  void cancel() {
    if (isDone())
      return; 
    this.isCancelled = true;
    this.decodeJob.cancel();
    this.engineJobListener.onEngineJobCancelled(this, this.key);
  }
  
  void decrementPendingCallbacks() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield stateVerifier : Lcom/bumptech/glide/util/pool/StateVerifier;
    //   6: invokevirtual throwIfRecycled : ()V
    //   9: aload_0
    //   10: invokespecial isDone : ()Z
    //   13: ldc 'Not yet complete!'
    //   15: invokestatic checkArgument : (ZLjava/lang/String;)V
    //   18: aload_0
    //   19: getfield pendingCallbacks : Ljava/util/concurrent/atomic/AtomicInteger;
    //   22: invokevirtual decrementAndGet : ()I
    //   25: istore_1
    //   26: iload_1
    //   27: iflt -> 35
    //   30: iconst_1
    //   31: istore_2
    //   32: goto -> 37
    //   35: iconst_0
    //   36: istore_2
    //   37: iload_2
    //   38: ldc 'Can't decrement below 0'
    //   40: invokestatic checkArgument : (ZLjava/lang/String;)V
    //   43: iload_1
    //   44: ifne -> 59
    //   47: aload_0
    //   48: getfield engineResource : Lcom/bumptech/glide/load/engine/EngineResource;
    //   51: astore_3
    //   52: aload_0
    //   53: invokespecial release : ()V
    //   56: goto -> 61
    //   59: aconst_null
    //   60: astore_3
    //   61: aload_0
    //   62: monitorexit
    //   63: aload_3
    //   64: ifnull -> 71
    //   67: aload_3
    //   68: invokevirtual release : ()V
    //   71: return
    //   72: astore_3
    //   73: aload_0
    //   74: monitorexit
    //   75: aload_3
    //   76: athrow
    // Exception table:
    //   from	to	target	type
    //   2	26	72	finally
    //   37	43	72	finally
    //   47	56	72	finally
    //   61	63	72	finally
    //   73	75	72	finally
  }
  
  public StateVerifier getVerifier() {
    return this.stateVerifier;
  }
  
  void incrementPendingCallbacks(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial isDone : ()Z
    //   6: ldc 'Not yet complete!'
    //   8: invokestatic checkArgument : (ZLjava/lang/String;)V
    //   11: aload_0
    //   12: getfield pendingCallbacks : Ljava/util/concurrent/atomic/AtomicInteger;
    //   15: iload_1
    //   16: invokevirtual getAndAdd : (I)I
    //   19: ifne -> 36
    //   22: aload_0
    //   23: getfield engineResource : Lcom/bumptech/glide/load/engine/EngineResource;
    //   26: ifnull -> 36
    //   29: aload_0
    //   30: getfield engineResource : Lcom/bumptech/glide/load/engine/EngineResource;
    //   33: invokevirtual acquire : ()V
    //   36: aload_0
    //   37: monitorexit
    //   38: return
    //   39: astore_2
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_2
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	36	39	finally
  }
  
  EngineJob<R> init(Key paramKey, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield key : Lcom/bumptech/glide/load/Key;
    //   7: aload_0
    //   8: iload_2
    //   9: putfield isCacheable : Z
    //   12: aload_0
    //   13: iload_3
    //   14: putfield useUnlimitedSourceGeneratorPool : Z
    //   17: aload_0
    //   18: iload #4
    //   20: putfield useAnimationPool : Z
    //   23: aload_0
    //   24: iload #5
    //   26: putfield onlyRetrieveFromCache : Z
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_0
    //   32: areturn
    //   33: astore_1
    //   34: aload_0
    //   35: monitorexit
    //   36: aload_1
    //   37: athrow
    // Exception table:
    //   from	to	target	type
    //   2	29	33	finally
  }
  
  boolean isCancelled() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isCancelled : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  void notifyCallbacksOfException() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield stateVerifier : Lcom/bumptech/glide/util/pool/StateVerifier;
    //   6: invokevirtual throwIfRecycled : ()V
    //   9: aload_0
    //   10: getfield isCancelled : Z
    //   13: ifeq -> 23
    //   16: aload_0
    //   17: invokespecial release : ()V
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: aload_0
    //   24: getfield cbs : Lcom/bumptech/glide/load/engine/EngineJob$ResourceCallbacksAndExecutors;
    //   27: invokevirtual isEmpty : ()Z
    //   30: ifne -> 148
    //   33: aload_0
    //   34: getfield hasLoadFailed : Z
    //   37: ifne -> 135
    //   40: aload_0
    //   41: iconst_1
    //   42: putfield hasLoadFailed : Z
    //   45: aload_0
    //   46: getfield key : Lcom/bumptech/glide/load/Key;
    //   49: astore_1
    //   50: aload_0
    //   51: getfield cbs : Lcom/bumptech/glide/load/engine/EngineJob$ResourceCallbacksAndExecutors;
    //   54: invokevirtual copy : ()Lcom/bumptech/glide/load/engine/EngineJob$ResourceCallbacksAndExecutors;
    //   57: astore_2
    //   58: aload_0
    //   59: aload_2
    //   60: invokevirtual size : ()I
    //   63: iconst_1
    //   64: iadd
    //   65: invokevirtual incrementPendingCallbacks : (I)V
    //   68: aload_0
    //   69: monitorexit
    //   70: aload_0
    //   71: getfield engineJobListener : Lcom/bumptech/glide/load/engine/EngineJobListener;
    //   74: aload_0
    //   75: aload_1
    //   76: aconst_null
    //   77: invokeinterface onEngineJobComplete : (Lcom/bumptech/glide/load/engine/EngineJob;Lcom/bumptech/glide/load/Key;Lcom/bumptech/glide/load/engine/EngineResource;)V
    //   82: aload_2
    //   83: invokevirtual iterator : ()Ljava/util/Iterator;
    //   86: astore_1
    //   87: aload_1
    //   88: invokeinterface hasNext : ()Z
    //   93: ifeq -> 130
    //   96: aload_1
    //   97: invokeinterface next : ()Ljava/lang/Object;
    //   102: checkcast com/bumptech/glide/load/engine/EngineJob$ResourceCallbackAndExecutor
    //   105: astore_2
    //   106: aload_2
    //   107: getfield executor : Ljava/util/concurrent/Executor;
    //   110: new com/bumptech/glide/load/engine/EngineJob$CallLoadFailed
    //   113: dup
    //   114: aload_0
    //   115: aload_2
    //   116: getfield cb : Lcom/bumptech/glide/request/ResourceCallback;
    //   119: invokespecial <init> : (Lcom/bumptech/glide/load/engine/EngineJob;Lcom/bumptech/glide/request/ResourceCallback;)V
    //   122: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   127: goto -> 87
    //   130: aload_0
    //   131: invokevirtual decrementPendingCallbacks : ()V
    //   134: return
    //   135: new java/lang/IllegalStateException
    //   138: astore_1
    //   139: aload_1
    //   140: ldc_w 'Already failed once'
    //   143: invokespecial <init> : (Ljava/lang/String;)V
    //   146: aload_1
    //   147: athrow
    //   148: new java/lang/IllegalStateException
    //   151: astore_1
    //   152: aload_1
    //   153: ldc_w 'Received an exception without any callbacks to notify'
    //   156: invokespecial <init> : (Ljava/lang/String;)V
    //   159: aload_1
    //   160: athrow
    //   161: astore_1
    //   162: aload_0
    //   163: monitorexit
    //   164: goto -> 169
    //   167: aload_1
    //   168: athrow
    //   169: goto -> 167
    // Exception table:
    //   from	to	target	type
    //   2	22	161	finally
    //   23	70	161	finally
    //   135	148	161	finally
    //   148	161	161	finally
    //   162	164	161	finally
  }
  
  void notifyCallbacksOfResult() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield stateVerifier : Lcom/bumptech/glide/util/pool/StateVerifier;
    //   6: invokevirtual throwIfRecycled : ()V
    //   9: aload_0
    //   10: getfield isCancelled : Z
    //   13: ifeq -> 32
    //   16: aload_0
    //   17: getfield resource : Lcom/bumptech/glide/load/engine/Resource;
    //   20: invokeinterface recycle : ()V
    //   25: aload_0
    //   26: invokespecial release : ()V
    //   29: aload_0
    //   30: monitorexit
    //   31: return
    //   32: aload_0
    //   33: getfield cbs : Lcom/bumptech/glide/load/engine/EngineJob$ResourceCallbacksAndExecutors;
    //   36: invokevirtual isEmpty : ()Z
    //   39: ifne -> 189
    //   42: aload_0
    //   43: getfield hasResource : Z
    //   46: ifne -> 176
    //   49: aload_0
    //   50: aload_0
    //   51: getfield engineResourceFactory : Lcom/bumptech/glide/load/engine/EngineJob$EngineResourceFactory;
    //   54: aload_0
    //   55: getfield resource : Lcom/bumptech/glide/load/engine/Resource;
    //   58: aload_0
    //   59: getfield isCacheable : Z
    //   62: aload_0
    //   63: getfield key : Lcom/bumptech/glide/load/Key;
    //   66: aload_0
    //   67: getfield resourceListener : Lcom/bumptech/glide/load/engine/EngineResource$ResourceListener;
    //   70: invokevirtual build : (Lcom/bumptech/glide/load/engine/Resource;ZLcom/bumptech/glide/load/Key;Lcom/bumptech/glide/load/engine/EngineResource$ResourceListener;)Lcom/bumptech/glide/load/engine/EngineResource;
    //   73: putfield engineResource : Lcom/bumptech/glide/load/engine/EngineResource;
    //   76: aload_0
    //   77: iconst_1
    //   78: putfield hasResource : Z
    //   81: aload_0
    //   82: getfield cbs : Lcom/bumptech/glide/load/engine/EngineJob$ResourceCallbacksAndExecutors;
    //   85: invokevirtual copy : ()Lcom/bumptech/glide/load/engine/EngineJob$ResourceCallbacksAndExecutors;
    //   88: astore_1
    //   89: aload_0
    //   90: aload_1
    //   91: invokevirtual size : ()I
    //   94: iconst_1
    //   95: iadd
    //   96: invokevirtual incrementPendingCallbacks : (I)V
    //   99: aload_0
    //   100: getfield key : Lcom/bumptech/glide/load/Key;
    //   103: astore_2
    //   104: aload_0
    //   105: getfield engineResource : Lcom/bumptech/glide/load/engine/EngineResource;
    //   108: astore_3
    //   109: aload_0
    //   110: monitorexit
    //   111: aload_0
    //   112: getfield engineJobListener : Lcom/bumptech/glide/load/engine/EngineJobListener;
    //   115: aload_0
    //   116: aload_2
    //   117: aload_3
    //   118: invokeinterface onEngineJobComplete : (Lcom/bumptech/glide/load/engine/EngineJob;Lcom/bumptech/glide/load/Key;Lcom/bumptech/glide/load/engine/EngineResource;)V
    //   123: aload_1
    //   124: invokevirtual iterator : ()Ljava/util/Iterator;
    //   127: astore_2
    //   128: aload_2
    //   129: invokeinterface hasNext : ()Z
    //   134: ifeq -> 171
    //   137: aload_2
    //   138: invokeinterface next : ()Ljava/lang/Object;
    //   143: checkcast com/bumptech/glide/load/engine/EngineJob$ResourceCallbackAndExecutor
    //   146: astore_1
    //   147: aload_1
    //   148: getfield executor : Ljava/util/concurrent/Executor;
    //   151: new com/bumptech/glide/load/engine/EngineJob$CallResourceReady
    //   154: dup
    //   155: aload_0
    //   156: aload_1
    //   157: getfield cb : Lcom/bumptech/glide/request/ResourceCallback;
    //   160: invokespecial <init> : (Lcom/bumptech/glide/load/engine/EngineJob;Lcom/bumptech/glide/request/ResourceCallback;)V
    //   163: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   168: goto -> 128
    //   171: aload_0
    //   172: invokevirtual decrementPendingCallbacks : ()V
    //   175: return
    //   176: new java/lang/IllegalStateException
    //   179: astore_1
    //   180: aload_1
    //   181: ldc_w 'Already have resource'
    //   184: invokespecial <init> : (Ljava/lang/String;)V
    //   187: aload_1
    //   188: athrow
    //   189: new java/lang/IllegalStateException
    //   192: astore_1
    //   193: aload_1
    //   194: ldc_w 'Received a resource without any callbacks to notify'
    //   197: invokespecial <init> : (Ljava/lang/String;)V
    //   200: aload_1
    //   201: athrow
    //   202: astore_1
    //   203: aload_0
    //   204: monitorexit
    //   205: goto -> 210
    //   208: aload_1
    //   209: athrow
    //   210: goto -> 208
    // Exception table:
    //   from	to	target	type
    //   2	31	202	finally
    //   32	111	202	finally
    //   176	189	202	finally
    //   189	202	202	finally
    //   203	205	202	finally
  }
  
  public void onLoadFailed(GlideException paramGlideException) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield exception : Lcom/bumptech/glide/load/engine/GlideException;
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_0
    //   10: invokevirtual notifyCallbacksOfException : ()V
    //   13: return
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	14	finally
    //   15	17	14	finally
  }
  
  public void onResourceReady(Resource<R> paramResource, DataSource paramDataSource) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield resource : Lcom/bumptech/glide/load/engine/Resource;
    //   7: aload_0
    //   8: aload_2
    //   9: putfield dataSource : Lcom/bumptech/glide/load/DataSource;
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_0
    //   15: invokevirtual notifyCallbacksOfResult : ()V
    //   18: return
    //   19: astore_1
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_1
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	19	finally
    //   20	22	19	finally
  }
  
  boolean onlyRetrieveFromCache() {
    return this.onlyRetrieveFromCache;
  }
  
  void removeCallback(ResourceCallback paramResourceCallback) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield stateVerifier : Lcom/bumptech/glide/util/pool/StateVerifier;
    //   6: invokevirtual throwIfRecycled : ()V
    //   9: aload_0
    //   10: getfield cbs : Lcom/bumptech/glide/load/engine/EngineJob$ResourceCallbacksAndExecutors;
    //   13: aload_1
    //   14: invokevirtual remove : (Lcom/bumptech/glide/request/ResourceCallback;)V
    //   17: aload_0
    //   18: getfield cbs : Lcom/bumptech/glide/load/engine/EngineJob$ResourceCallbacksAndExecutors;
    //   21: invokevirtual isEmpty : ()Z
    //   24: ifeq -> 73
    //   27: aload_0
    //   28: invokevirtual cancel : ()V
    //   31: aload_0
    //   32: getfield hasResource : Z
    //   35: ifne -> 53
    //   38: aload_0
    //   39: getfield hasLoadFailed : Z
    //   42: ifeq -> 48
    //   45: goto -> 53
    //   48: iconst_0
    //   49: istore_2
    //   50: goto -> 55
    //   53: iconst_1
    //   54: istore_2
    //   55: iload_2
    //   56: ifeq -> 73
    //   59: aload_0
    //   60: getfield pendingCallbacks : Ljava/util/concurrent/atomic/AtomicInteger;
    //   63: invokevirtual get : ()I
    //   66: ifne -> 73
    //   69: aload_0
    //   70: invokespecial release : ()V
    //   73: aload_0
    //   74: monitorexit
    //   75: return
    //   76: astore_1
    //   77: aload_0
    //   78: monitorexit
    //   79: aload_1
    //   80: athrow
    // Exception table:
    //   from	to	target	type
    //   2	45	76	finally
    //   59	73	76	finally
  }
  
  public void reschedule(DecodeJob<?> paramDecodeJob) {
    getActiveSourceExecutor().execute(paramDecodeJob);
  }
  
  public void start(DecodeJob<R> paramDecodeJob) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield decodeJob : Lcom/bumptech/glide/load/engine/DecodeJob;
    //   7: aload_1
    //   8: invokevirtual willDecodeFromCache : ()Z
    //   11: ifeq -> 22
    //   14: aload_0
    //   15: getfield diskCacheExecutor : Lcom/bumptech/glide/load/engine/executor/GlideExecutor;
    //   18: astore_2
    //   19: goto -> 27
    //   22: aload_0
    //   23: invokespecial getActiveSourceExecutor : ()Lcom/bumptech/glide/load/engine/executor/GlideExecutor;
    //   26: astore_2
    //   27: aload_2
    //   28: aload_1
    //   29: invokevirtual execute : (Ljava/lang/Runnable;)V
    //   32: aload_0
    //   33: monitorexit
    //   34: return
    //   35: astore_1
    //   36: aload_0
    //   37: monitorexit
    //   38: aload_1
    //   39: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	35	finally
    //   22	27	35	finally
    //   27	32	35	finally
  }
  
  private class CallLoadFailed implements Runnable {
    private final ResourceCallback cb;
    
    CallLoadFailed(ResourceCallback param1ResourceCallback) {
      this.cb = param1ResourceCallback;
    }
    
    public void run() {
      synchronized (this.cb.getLock()) {
        synchronized (EngineJob.this) {
          if (EngineJob.this.cbs.contains(this.cb))
            EngineJob.this.callCallbackOnLoadFailed(this.cb); 
          EngineJob.this.decrementPendingCallbacks();
          return;
        } 
      } 
    }
  }
  
  private class CallResourceReady implements Runnable {
    private final ResourceCallback cb;
    
    CallResourceReady(ResourceCallback param1ResourceCallback) {
      this.cb = param1ResourceCallback;
    }
    
    public void run() {
      synchronized (this.cb.getLock()) {
        synchronized (EngineJob.this) {
          if (EngineJob.this.cbs.contains(this.cb)) {
            EngineJob.this.engineResource.acquire();
            EngineJob.this.callCallbackOnResourceReady(this.cb);
            EngineJob.this.removeCallback(this.cb);
          } 
          EngineJob.this.decrementPendingCallbacks();
          return;
        } 
      } 
    }
  }
  
  static class EngineResourceFactory {
    public <R> EngineResource<R> build(Resource<R> param1Resource, boolean param1Boolean, Key param1Key, EngineResource.ResourceListener param1ResourceListener) {
      return new EngineResource<R>(param1Resource, param1Boolean, true, param1Key, param1ResourceListener);
    }
  }
  
  static final class ResourceCallbackAndExecutor {
    final ResourceCallback cb;
    
    final Executor executor;
    
    ResourceCallbackAndExecutor(ResourceCallback param1ResourceCallback, Executor param1Executor) {
      this.cb = param1ResourceCallback;
      this.executor = param1Executor;
    }
    
    public boolean equals(Object param1Object) {
      if (param1Object instanceof ResourceCallbackAndExecutor) {
        param1Object = param1Object;
        return this.cb.equals(((ResourceCallbackAndExecutor)param1Object).cb);
      } 
      return false;
    }
    
    public int hashCode() {
      return this.cb.hashCode();
    }
  }
  
  static final class ResourceCallbacksAndExecutors implements Iterable<ResourceCallbackAndExecutor> {
    private final List<EngineJob.ResourceCallbackAndExecutor> callbacksAndExecutors;
    
    ResourceCallbacksAndExecutors() {
      this(new ArrayList<EngineJob.ResourceCallbackAndExecutor>(2));
    }
    
    ResourceCallbacksAndExecutors(List<EngineJob.ResourceCallbackAndExecutor> param1List) {
      this.callbacksAndExecutors = param1List;
    }
    
    private static EngineJob.ResourceCallbackAndExecutor defaultCallbackAndExecutor(ResourceCallback param1ResourceCallback) {
      return new EngineJob.ResourceCallbackAndExecutor(param1ResourceCallback, Executors.directExecutor());
    }
    
    void add(ResourceCallback param1ResourceCallback, Executor param1Executor) {
      this.callbacksAndExecutors.add(new EngineJob.ResourceCallbackAndExecutor(param1ResourceCallback, param1Executor));
    }
    
    void clear() {
      this.callbacksAndExecutors.clear();
    }
    
    boolean contains(ResourceCallback param1ResourceCallback) {
      return this.callbacksAndExecutors.contains(defaultCallbackAndExecutor(param1ResourceCallback));
    }
    
    ResourceCallbacksAndExecutors copy() {
      return new ResourceCallbacksAndExecutors(new ArrayList<EngineJob.ResourceCallbackAndExecutor>(this.callbacksAndExecutors));
    }
    
    boolean isEmpty() {
      return this.callbacksAndExecutors.isEmpty();
    }
    
    public Iterator<EngineJob.ResourceCallbackAndExecutor> iterator() {
      return this.callbacksAndExecutors.iterator();
    }
    
    void remove(ResourceCallback param1ResourceCallback) {
      this.callbacksAndExecutors.remove(defaultCallbackAndExecutor(param1ResourceCallback));
    }
    
    int size() {
      return this.callbacksAndExecutors.size();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/EngineJob.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */