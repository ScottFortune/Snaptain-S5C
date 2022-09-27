package com.bumptech.glide.load.engine;

import android.os.Process;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.Executors;
import com.bumptech.glide.util.Preconditions;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

final class ActiveResources {
  final Map<Key, ResourceWeakReference> activeEngineResources = new HashMap<Key, ResourceWeakReference>();
  
  private volatile DequeuedResourceCallback cb;
  
  private final boolean isActiveResourceRetentionAllowed;
  
  private volatile boolean isShutdown;
  
  private EngineResource.ResourceListener listener;
  
  private final Executor monitorClearedResourcesExecutor;
  
  private final ReferenceQueue<EngineResource<?>> resourceReferenceQueue = new ReferenceQueue<EngineResource<?>>();
  
  ActiveResources(boolean paramBoolean) {
    this(paramBoolean, Executors.newSingleThreadExecutor(new ThreadFactory() {
            public Thread newThread(final Runnable r) {
              return new Thread(new Runnable() {
                    public void run() {
                      Process.setThreadPriority(10);
                      r.run();
                    }
                  },  "glide-active-resources");
            }
          }));
  }
  
  ActiveResources(boolean paramBoolean, Executor paramExecutor) {
    this.isActiveResourceRetentionAllowed = paramBoolean;
    this.monitorClearedResourcesExecutor = paramExecutor;
    paramExecutor.execute(new Runnable() {
          public void run() {
            ActiveResources.this.cleanReferenceQueue();
          }
        });
  }
  
  void activate(Key paramKey, EngineResource<?> paramEngineResource) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new com/bumptech/glide/load/engine/ActiveResources$ResourceWeakReference
    //   5: astore_3
    //   6: aload_3
    //   7: aload_1
    //   8: aload_2
    //   9: aload_0
    //   10: getfield resourceReferenceQueue : Ljava/lang/ref/ReferenceQueue;
    //   13: aload_0
    //   14: getfield isActiveResourceRetentionAllowed : Z
    //   17: invokespecial <init> : (Lcom/bumptech/glide/load/Key;Lcom/bumptech/glide/load/engine/EngineResource;Ljava/lang/ref/ReferenceQueue;Z)V
    //   20: aload_0
    //   21: getfield activeEngineResources : Ljava/util/Map;
    //   24: aload_1
    //   25: aload_3
    //   26: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   31: checkcast com/bumptech/glide/load/engine/ActiveResources$ResourceWeakReference
    //   34: astore_1
    //   35: aload_1
    //   36: ifnull -> 43
    //   39: aload_1
    //   40: invokevirtual reset : ()V
    //   43: aload_0
    //   44: monitorexit
    //   45: return
    //   46: astore_1
    //   47: aload_0
    //   48: monitorexit
    //   49: aload_1
    //   50: athrow
    // Exception table:
    //   from	to	target	type
    //   2	35	46	finally
    //   39	43	46	finally
  }
  
  void cleanReferenceQueue() {
    while (!this.isShutdown) {
      try {
        cleanupActiveReference((ResourceWeakReference)this.resourceReferenceQueue.remove());
        DequeuedResourceCallback dequeuedResourceCallback = this.cb;
        if (dequeuedResourceCallback != null)
          dequeuedResourceCallback.onResourceDequeued(); 
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
      } 
    } 
  }
  
  void cleanupActiveReference(ResourceWeakReference paramResourceWeakReference) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield activeEngineResources : Ljava/util/Map;
    //   6: aload_1
    //   7: getfield key : Lcom/bumptech/glide/load/Key;
    //   10: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   15: pop
    //   16: aload_1
    //   17: getfield isCacheable : Z
    //   20: ifeq -> 72
    //   23: aload_1
    //   24: getfield resource : Lcom/bumptech/glide/load/engine/Resource;
    //   27: ifnonnull -> 33
    //   30: goto -> 72
    //   33: aload_0
    //   34: monitorexit
    //   35: new com/bumptech/glide/load/engine/EngineResource
    //   38: dup
    //   39: aload_1
    //   40: getfield resource : Lcom/bumptech/glide/load/engine/Resource;
    //   43: iconst_1
    //   44: iconst_0
    //   45: aload_1
    //   46: getfield key : Lcom/bumptech/glide/load/Key;
    //   49: aload_0
    //   50: getfield listener : Lcom/bumptech/glide/load/engine/EngineResource$ResourceListener;
    //   53: invokespecial <init> : (Lcom/bumptech/glide/load/engine/Resource;ZZLcom/bumptech/glide/load/Key;Lcom/bumptech/glide/load/engine/EngineResource$ResourceListener;)V
    //   56: astore_2
    //   57: aload_0
    //   58: getfield listener : Lcom/bumptech/glide/load/engine/EngineResource$ResourceListener;
    //   61: aload_1
    //   62: getfield key : Lcom/bumptech/glide/load/Key;
    //   65: aload_2
    //   66: invokeinterface onResourceReleased : (Lcom/bumptech/glide/load/Key;Lcom/bumptech/glide/load/engine/EngineResource;)V
    //   71: return
    //   72: aload_0
    //   73: monitorexit
    //   74: return
    //   75: astore_1
    //   76: aload_0
    //   77: monitorexit
    //   78: aload_1
    //   79: athrow
    // Exception table:
    //   from	to	target	type
    //   2	30	75	finally
    //   33	35	75	finally
    //   72	74	75	finally
    //   76	78	75	finally
  }
  
  void deactivate(Key paramKey) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield activeEngineResources : Ljava/util/Map;
    //   6: aload_1
    //   7: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   12: checkcast com/bumptech/glide/load/engine/ActiveResources$ResourceWeakReference
    //   15: astore_1
    //   16: aload_1
    //   17: ifnull -> 24
    //   20: aload_1
    //   21: invokevirtual reset : ()V
    //   24: aload_0
    //   25: monitorexit
    //   26: return
    //   27: astore_1
    //   28: aload_0
    //   29: monitorexit
    //   30: aload_1
    //   31: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	27	finally
    //   20	24	27	finally
  }
  
  EngineResource<?> get(Key paramKey) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield activeEngineResources : Ljava/util/Map;
    //   6: aload_1
    //   7: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   12: checkcast com/bumptech/glide/load/engine/ActiveResources$ResourceWeakReference
    //   15: astore_2
    //   16: aload_2
    //   17: ifnonnull -> 24
    //   20: aload_0
    //   21: monitorexit
    //   22: aconst_null
    //   23: areturn
    //   24: aload_2
    //   25: invokevirtual get : ()Ljava/lang/Object;
    //   28: checkcast com/bumptech/glide/load/engine/EngineResource
    //   31: astore_1
    //   32: aload_1
    //   33: ifnonnull -> 41
    //   36: aload_0
    //   37: aload_2
    //   38: invokevirtual cleanupActiveReference : (Lcom/bumptech/glide/load/engine/ActiveResources$ResourceWeakReference;)V
    //   41: aload_0
    //   42: monitorexit
    //   43: aload_1
    //   44: areturn
    //   45: astore_1
    //   46: aload_0
    //   47: monitorexit
    //   48: aload_1
    //   49: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	45	finally
    //   24	32	45	finally
    //   36	41	45	finally
  }
  
  void setDequeuedResourceCallback(DequeuedResourceCallback paramDequeuedResourceCallback) {
    this.cb = paramDequeuedResourceCallback;
  }
  
  void setListener(EngineResource.ResourceListener paramResourceListener) {
    // Byte code:
    //   0: aload_1
    //   1: monitorenter
    //   2: aload_0
    //   3: monitorenter
    //   4: aload_0
    //   5: aload_1
    //   6: putfield listener : Lcom/bumptech/glide/load/engine/EngineResource$ResourceListener;
    //   9: aload_0
    //   10: monitorexit
    //   11: aload_1
    //   12: monitorexit
    //   13: return
    //   14: astore_2
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_2
    //   18: athrow
    //   19: astore_2
    //   20: aload_1
    //   21: monitorexit
    //   22: aload_2
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	4	19	finally
    //   4	11	14	finally
    //   11	13	19	finally
    //   15	17	14	finally
    //   17	19	19	finally
    //   20	22	19	finally
  }
  
  void shutdown() {
    this.isShutdown = true;
    Executor executor = this.monitorClearedResourcesExecutor;
    if (executor instanceof ExecutorService)
      Executors.shutdownAndAwaitTermination((ExecutorService)executor); 
  }
  
  static interface DequeuedResourceCallback {
    void onResourceDequeued();
  }
  
  static final class ResourceWeakReference extends WeakReference<EngineResource<?>> {
    final boolean isCacheable;
    
    final Key key;
    
    Resource<?> resource;
    
    ResourceWeakReference(Key param1Key, EngineResource<?> param1EngineResource, ReferenceQueue<? super EngineResource<?>> param1ReferenceQueue, boolean param1Boolean) {
      super(param1EngineResource, param1ReferenceQueue);
      this.key = (Key)Preconditions.checkNotNull(param1Key);
      if (param1EngineResource.isMemoryCacheable() && param1Boolean) {
        Resource resource = (Resource)Preconditions.checkNotNull(param1EngineResource.getResource());
      } else {
        param1Key = null;
      } 
      this.resource = (Resource<?>)param1Key;
      this.isCacheable = param1EngineResource.isMemoryCacheable();
    }
    
    void reset() {
      this.resource = null;
      clear();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/ActiveResources.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */