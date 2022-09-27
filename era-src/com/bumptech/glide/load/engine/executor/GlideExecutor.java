package com.bumptech.glide.load.engine.executor;

import android.os.Process;
import android.os.StrictMode;
import android.util.Log;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class GlideExecutor implements ExecutorService {
  private static final String ANIMATION_EXECUTOR_NAME = "animation";
  
  private static final String DEFAULT_DISK_CACHE_EXECUTOR_NAME = "disk-cache";
  
  private static final int DEFAULT_DISK_CACHE_EXECUTOR_THREADS = 1;
  
  private static final String DEFAULT_SOURCE_EXECUTOR_NAME = "source";
  
  private static final long KEEP_ALIVE_TIME_MS = TimeUnit.SECONDS.toMillis(10L);
  
  private static final int MAXIMUM_AUTOMATIC_THREAD_COUNT = 4;
  
  private static final String SOURCE_UNLIMITED_EXECUTOR_NAME = "source-unlimited";
  
  private static final String TAG = "GlideExecutor";
  
  private static volatile int bestThreadCount;
  
  private final ExecutorService delegate;
  
  GlideExecutor(ExecutorService paramExecutorService) {
    this.delegate = paramExecutorService;
  }
  
  public static int calculateBestThreadCount() {
    if (bestThreadCount == 0)
      bestThreadCount = Math.min(4, RuntimeCompat.availableProcessors()); 
    return bestThreadCount;
  }
  
  public static GlideExecutor newAnimationExecutor() {
    boolean bool;
    if (calculateBestThreadCount() >= 4) {
      bool = true;
    } else {
      bool = true;
    } 
    return newAnimationExecutor(bool, UncaughtThrowableStrategy.DEFAULT);
  }
  
  public static GlideExecutor newAnimationExecutor(int paramInt, UncaughtThrowableStrategy paramUncaughtThrowableStrategy) {
    return new GlideExecutor(new ThreadPoolExecutor(paramInt, paramInt, 0L, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<Runnable>(), new DefaultThreadFactory("animation", paramUncaughtThrowableStrategy, true)));
  }
  
  public static GlideExecutor newDiskCacheExecutor() {
    return newDiskCacheExecutor(1, "disk-cache", UncaughtThrowableStrategy.DEFAULT);
  }
  
  public static GlideExecutor newDiskCacheExecutor(int paramInt, String paramString, UncaughtThrowableStrategy paramUncaughtThrowableStrategy) {
    return new GlideExecutor(new ThreadPoolExecutor(paramInt, paramInt, 0L, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<Runnable>(), new DefaultThreadFactory(paramString, paramUncaughtThrowableStrategy, true)));
  }
  
  public static GlideExecutor newDiskCacheExecutor(UncaughtThrowableStrategy paramUncaughtThrowableStrategy) {
    return newDiskCacheExecutor(1, "disk-cache", paramUncaughtThrowableStrategy);
  }
  
  public static GlideExecutor newSourceExecutor() {
    return newSourceExecutor(calculateBestThreadCount(), "source", UncaughtThrowableStrategy.DEFAULT);
  }
  
  public static GlideExecutor newSourceExecutor(int paramInt, String paramString, UncaughtThrowableStrategy paramUncaughtThrowableStrategy) {
    return new GlideExecutor(new ThreadPoolExecutor(paramInt, paramInt, 0L, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<Runnable>(), new DefaultThreadFactory(paramString, paramUncaughtThrowableStrategy, false)));
  }
  
  public static GlideExecutor newSourceExecutor(UncaughtThrowableStrategy paramUncaughtThrowableStrategy) {
    return newSourceExecutor(calculateBestThreadCount(), "source", paramUncaughtThrowableStrategy);
  }
  
  public static GlideExecutor newUnlimitedSourceExecutor() {
    return new GlideExecutor(new ThreadPoolExecutor(0, 2147483647, KEEP_ALIVE_TIME_MS, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(), new DefaultThreadFactory("source-unlimited", UncaughtThrowableStrategy.DEFAULT, false)));
  }
  
  public boolean awaitTermination(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException {
    return this.delegate.awaitTermination(paramLong, paramTimeUnit);
  }
  
  public void execute(Runnable paramRunnable) {
    this.delegate.execute(paramRunnable);
  }
  
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> paramCollection) throws InterruptedException {
    return this.delegate.invokeAll(paramCollection);
  }
  
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> paramCollection, long paramLong, TimeUnit paramTimeUnit) throws InterruptedException {
    return this.delegate.invokeAll(paramCollection, paramLong, paramTimeUnit);
  }
  
  public <T> T invokeAny(Collection<? extends Callable<T>> paramCollection) throws InterruptedException, ExecutionException {
    return this.delegate.invokeAny(paramCollection);
  }
  
  public <T> T invokeAny(Collection<? extends Callable<T>> paramCollection, long paramLong, TimeUnit paramTimeUnit) throws InterruptedException, ExecutionException, TimeoutException {
    return this.delegate.invokeAny(paramCollection, paramLong, paramTimeUnit);
  }
  
  public boolean isShutdown() {
    return this.delegate.isShutdown();
  }
  
  public boolean isTerminated() {
    return this.delegate.isTerminated();
  }
  
  public void shutdown() {
    this.delegate.shutdown();
  }
  
  public List<Runnable> shutdownNow() {
    return this.delegate.shutdownNow();
  }
  
  public Future<?> submit(Runnable paramRunnable) {
    return this.delegate.submit(paramRunnable);
  }
  
  public <T> Future<T> submit(Runnable paramRunnable, T paramT) {
    return this.delegate.submit(paramRunnable, paramT);
  }
  
  public <T> Future<T> submit(Callable<T> paramCallable) {
    return this.delegate.submit(paramCallable);
  }
  
  public String toString() {
    return this.delegate.toString();
  }
  
  private static final class DefaultThreadFactory implements ThreadFactory {
    private static final int DEFAULT_PRIORITY = 9;
    
    private final String name;
    
    final boolean preventNetworkOperations;
    
    private int threadNum;
    
    final GlideExecutor.UncaughtThrowableStrategy uncaughtThrowableStrategy;
    
    DefaultThreadFactory(String param1String, GlideExecutor.UncaughtThrowableStrategy param1UncaughtThrowableStrategy, boolean param1Boolean) {
      this.name = param1String;
      this.uncaughtThrowableStrategy = param1UncaughtThrowableStrategy;
      this.preventNetworkOperations = param1Boolean;
    }
    
    public Thread newThread(Runnable param1Runnable) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: new com/bumptech/glide/load/engine/executor/GlideExecutor$DefaultThreadFactory$1
      //   5: astore_2
      //   6: new java/lang/StringBuilder
      //   9: astore_3
      //   10: aload_3
      //   11: invokespecial <init> : ()V
      //   14: aload_3
      //   15: ldc 'glide-'
      //   17: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   20: pop
      //   21: aload_3
      //   22: aload_0
      //   23: getfield name : Ljava/lang/String;
      //   26: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   29: pop
      //   30: aload_3
      //   31: ldc '-thread-'
      //   33: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   36: pop
      //   37: aload_3
      //   38: aload_0
      //   39: getfield threadNum : I
      //   42: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   45: pop
      //   46: aload_2
      //   47: aload_0
      //   48: aload_1
      //   49: aload_3
      //   50: invokevirtual toString : ()Ljava/lang/String;
      //   53: invokespecial <init> : (Lcom/bumptech/glide/load/engine/executor/GlideExecutor$DefaultThreadFactory;Ljava/lang/Runnable;Ljava/lang/String;)V
      //   56: aload_0
      //   57: aload_0
      //   58: getfield threadNum : I
      //   61: iconst_1
      //   62: iadd
      //   63: putfield threadNum : I
      //   66: aload_0
      //   67: monitorexit
      //   68: aload_2
      //   69: areturn
      //   70: astore_1
      //   71: aload_0
      //   72: monitorexit
      //   73: aload_1
      //   74: athrow
      // Exception table:
      //   from	to	target	type
      //   2	66	70	finally
    }
  }
  
  class null extends Thread {
    null(Runnable param1Runnable, String param1String) {
      super(param1Runnable, param1String);
    }
    
    public void run() {
      Process.setThreadPriority(9);
      if (this.this$0.preventNetworkOperations)
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder()).detectNetwork().penaltyDeath().build()); 
      try {
        super.run();
      } finally {
        Exception exception = null;
      } 
    }
  }
  
  public static interface UncaughtThrowableStrategy {
    public static final UncaughtThrowableStrategy DEFAULT = LOG;
    
    public static final UncaughtThrowableStrategy IGNORE = new UncaughtThrowableStrategy() {
        public void handle(Throwable param2Throwable) {}
      };
    
    public static final UncaughtThrowableStrategy LOG = new UncaughtThrowableStrategy() {
        public void handle(Throwable param2Throwable) {
          if (param2Throwable != null && Log.isLoggable("GlideExecutor", 6))
            Log.e("GlideExecutor", "Request threw uncaught throwable", param2Throwable); 
        }
      };
    
    public static final UncaughtThrowableStrategy THROW = new UncaughtThrowableStrategy() {
        public void handle(Throwable param2Throwable) {
          if (param2Throwable == null)
            return; 
          throw new RuntimeException("Request threw uncaught throwable", param2Throwable);
        }
      };
    
    static {
    
    }
    
    void handle(Throwable param1Throwable);
  }
  
  class null implements UncaughtThrowableStrategy {
    public void handle(Throwable param1Throwable) {}
  }
  
  class null implements UncaughtThrowableStrategy {
    public void handle(Throwable param1Throwable) {
      if (param1Throwable != null && Log.isLoggable("GlideExecutor", 6))
        Log.e("GlideExecutor", "Request threw uncaught throwable", param1Throwable); 
    }
  }
  
  class null implements UncaughtThrowableStrategy {
    public void handle(Throwable param1Throwable) {
      if (param1Throwable == null)
        return; 
      throw new RuntimeException("Request threw uncaught throwable", param1Throwable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/executor/GlideExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */