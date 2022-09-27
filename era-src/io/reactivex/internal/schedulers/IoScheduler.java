package io.reactivex.internal.schedulers;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableContainer;
import io.reactivex.internal.disposables.EmptyDisposable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class IoScheduler extends Scheduler {
  static final RxThreadFactory EVICTOR_THREAD_FACTORY;
  
  private static final String EVICTOR_THREAD_NAME_PREFIX = "RxCachedWorkerPoolEvictor";
  
  private static final long KEEP_ALIVE_TIME;
  
  public static final long KEEP_ALIVE_TIME_DEFAULT = 60L;
  
  private static final TimeUnit KEEP_ALIVE_UNIT = TimeUnit.SECONDS;
  
  private static final String KEY_IO_PRIORITY = "rx2.io-priority";
  
  private static final String KEY_KEEP_ALIVE_TIME = "rx2.io-keep-alive-time";
  
  static final CachedWorkerPool NONE;
  
  static final ThreadWorker SHUTDOWN_THREAD_WORKER;
  
  static final RxThreadFactory WORKER_THREAD_FACTORY;
  
  private static final String WORKER_THREAD_NAME_PREFIX = "RxCachedThreadScheduler";
  
  final AtomicReference<CachedWorkerPool> pool;
  
  final ThreadFactory threadFactory;
  
  static {
    KEEP_ALIVE_TIME = Long.getLong("rx2.io-keep-alive-time", 60L).longValue();
    SHUTDOWN_THREAD_WORKER = new ThreadWorker(new RxThreadFactory("RxCachedThreadSchedulerShutdown"));
    SHUTDOWN_THREAD_WORKER.dispose();
    int i = Math.max(1, Math.min(10, Integer.getInteger("rx2.io-priority", 5).intValue()));
    WORKER_THREAD_FACTORY = new RxThreadFactory("RxCachedThreadScheduler", i);
    EVICTOR_THREAD_FACTORY = new RxThreadFactory("RxCachedWorkerPoolEvictor", i);
    NONE = new CachedWorkerPool(0L, null, WORKER_THREAD_FACTORY);
    NONE.shutdown();
  }
  
  public IoScheduler() {
    this(WORKER_THREAD_FACTORY);
  }
  
  public IoScheduler(ThreadFactory paramThreadFactory) {
    this.threadFactory = paramThreadFactory;
    this.pool = new AtomicReference<CachedWorkerPool>(NONE);
    start();
  }
  
  public Scheduler.Worker createWorker() {
    return new EventLoopWorker(this.pool.get());
  }
  
  public void shutdown() {
    while (true) {
      CachedWorkerPool cachedWorkerPool1 = this.pool.get();
      CachedWorkerPool cachedWorkerPool2 = NONE;
      if (cachedWorkerPool1 == cachedWorkerPool2)
        return; 
      if (this.pool.compareAndSet(cachedWorkerPool1, cachedWorkerPool2)) {
        cachedWorkerPool1.shutdown();
        return;
      } 
    } 
  }
  
  public int size() {
    return ((CachedWorkerPool)this.pool.get()).allWorkers.size();
  }
  
  public void start() {
    CachedWorkerPool cachedWorkerPool = new CachedWorkerPool(KEEP_ALIVE_TIME, KEEP_ALIVE_UNIT, this.threadFactory);
    if (!this.pool.compareAndSet(NONE, cachedWorkerPool))
      cachedWorkerPool.shutdown(); 
  }
  
  static final class CachedWorkerPool implements Runnable {
    final CompositeDisposable allWorkers;
    
    private final ScheduledExecutorService evictorService;
    
    private final Future<?> evictorTask;
    
    private final ConcurrentLinkedQueue<IoScheduler.ThreadWorker> expiringWorkerQueue;
    
    private final long keepAliveTime;
    
    private final ThreadFactory threadFactory;
    
    CachedWorkerPool(long param1Long, TimeUnit param1TimeUnit, ThreadFactory param1ThreadFactory) {
      if (param1TimeUnit != null) {
        param1Long = param1TimeUnit.toNanos(param1Long);
      } else {
        param1Long = 0L;
      } 
      this.keepAliveTime = param1Long;
      this.expiringWorkerQueue = new ConcurrentLinkedQueue<IoScheduler.ThreadWorker>();
      this.allWorkers = new CompositeDisposable();
      this.threadFactory = param1ThreadFactory;
      TimeUnit timeUnit = null;
      if (param1TimeUnit != null) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1, IoScheduler.EVICTOR_THREAD_FACTORY);
        param1Long = this.keepAliveTime;
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(this, param1Long, param1Long, TimeUnit.NANOSECONDS);
      } else {
        param1ThreadFactory = null;
        param1TimeUnit = timeUnit;
      } 
      this.evictorService = (ScheduledExecutorService)param1TimeUnit;
      this.evictorTask = (Future<?>)param1ThreadFactory;
    }
    
    void evictExpiredWorkers() {
      if (!this.expiringWorkerQueue.isEmpty()) {
        long l = now();
        for (IoScheduler.ThreadWorker threadWorker : this.expiringWorkerQueue) {
          if (threadWorker.getExpirationTime() <= l)
            if (this.expiringWorkerQueue.remove(threadWorker))
              this.allWorkers.remove(threadWorker);  
        } 
      } 
    }
    
    IoScheduler.ThreadWorker get() {
      if (this.allWorkers.isDisposed())
        return IoScheduler.SHUTDOWN_THREAD_WORKER; 
      while (!this.expiringWorkerQueue.isEmpty()) {
        IoScheduler.ThreadWorker threadWorker1 = this.expiringWorkerQueue.poll();
        if (threadWorker1 != null)
          return threadWorker1; 
      } 
      IoScheduler.ThreadWorker threadWorker = new IoScheduler.ThreadWorker(this.threadFactory);
      this.allWorkers.add(threadWorker);
      return threadWorker;
    }
    
    long now() {
      return System.nanoTime();
    }
    
    void release(IoScheduler.ThreadWorker param1ThreadWorker) {
      param1ThreadWorker.setExpirationTime(now() + this.keepAliveTime);
      this.expiringWorkerQueue.offer(param1ThreadWorker);
    }
    
    public void run() {
      evictExpiredWorkers();
    }
    
    void shutdown() {
      this.allWorkers.dispose();
      Future<?> future = this.evictorTask;
      if (future != null)
        future.cancel(true); 
      ScheduledExecutorService scheduledExecutorService = this.evictorService;
      if (scheduledExecutorService != null)
        scheduledExecutorService.shutdownNow(); 
    }
  }
  
  static final class EventLoopWorker extends Scheduler.Worker {
    final AtomicBoolean once = new AtomicBoolean();
    
    private final IoScheduler.CachedWorkerPool pool;
    
    private final CompositeDisposable tasks;
    
    private final IoScheduler.ThreadWorker threadWorker;
    
    EventLoopWorker(IoScheduler.CachedWorkerPool param1CachedWorkerPool) {
      this.pool = param1CachedWorkerPool;
      this.tasks = new CompositeDisposable();
      this.threadWorker = param1CachedWorkerPool.get();
    }
    
    public void dispose() {
      if (this.once.compareAndSet(false, true)) {
        this.tasks.dispose();
        this.pool.release(this.threadWorker);
      } 
    }
    
    public boolean isDisposed() {
      return this.once.get();
    }
    
    public Disposable schedule(Runnable param1Runnable, long param1Long, TimeUnit param1TimeUnit) {
      return (Disposable)(this.tasks.isDisposed() ? EmptyDisposable.INSTANCE : this.threadWorker.scheduleActual(param1Runnable, param1Long, param1TimeUnit, (DisposableContainer)this.tasks));
    }
  }
  
  static final class ThreadWorker extends NewThreadWorker {
    private long expirationTime = 0L;
    
    ThreadWorker(ThreadFactory param1ThreadFactory) {
      super(param1ThreadFactory);
    }
    
    public long getExpirationTime() {
      return this.expirationTime;
    }
    
    public void setExpirationTime(long param1Long) {
      this.expirationTime = param1Long;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/schedulers/IoScheduler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */