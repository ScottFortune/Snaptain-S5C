package io.reactivex.internal.schedulers;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableContainer;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.disposables.ListCompositeDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class ComputationScheduler extends Scheduler implements SchedulerMultiWorkerSupport {
  private static final String KEY_COMPUTATION_PRIORITY = "rx2.computation-priority";
  
  static final String KEY_MAX_THREADS = "rx2.computation-threads";
  
  static final int MAX_THREADS = cap(Runtime.getRuntime().availableProcessors(), Integer.getInteger("rx2.computation-threads", 0).intValue());
  
  static final FixedSchedulerPool NONE;
  
  static final PoolWorker SHUTDOWN_WORKER = new PoolWorker(new RxThreadFactory("RxComputationShutdown"));
  
  static final RxThreadFactory THREAD_FACTORY = new RxThreadFactory("RxComputationThreadPool", Math.max(1, Math.min(10, Integer.getInteger("rx2.computation-priority", 5).intValue())), true);
  
  private static final String THREAD_NAME_PREFIX = "RxComputationThreadPool";
  
  final AtomicReference<FixedSchedulerPool> pool;
  
  final ThreadFactory threadFactory;
  
  static {
    NONE = new FixedSchedulerPool(0, THREAD_FACTORY);
    NONE.shutdown();
  }
  
  public ComputationScheduler() {
    this(THREAD_FACTORY);
  }
  
  public ComputationScheduler(ThreadFactory paramThreadFactory) {
    this.threadFactory = paramThreadFactory;
    this.pool = new AtomicReference<FixedSchedulerPool>(NONE);
    start();
  }
  
  static int cap(int paramInt1, int paramInt2) {
    int i = paramInt1;
    if (paramInt2 > 0)
      if (paramInt2 > paramInt1) {
        i = paramInt1;
      } else {
        i = paramInt2;
      }  
    return i;
  }
  
  public Scheduler.Worker createWorker() {
    return new EventLoopWorker(((FixedSchedulerPool)this.pool.get()).getEventLoop());
  }
  
  public void createWorkers(int paramInt, SchedulerMultiWorkerSupport.WorkerCallback paramWorkerCallback) {
    ObjectHelper.verifyPositive(paramInt, "number > 0 required");
    ((FixedSchedulerPool)this.pool.get()).createWorkers(paramInt, paramWorkerCallback);
  }
  
  public Disposable scheduleDirect(Runnable paramRunnable, long paramLong, TimeUnit paramTimeUnit) {
    return ((FixedSchedulerPool)this.pool.get()).getEventLoop().scheduleDirect(paramRunnable, paramLong, paramTimeUnit);
  }
  
  public Disposable schedulePeriodicallyDirect(Runnable paramRunnable, long paramLong1, long paramLong2, TimeUnit paramTimeUnit) {
    return ((FixedSchedulerPool)this.pool.get()).getEventLoop().schedulePeriodicallyDirect(paramRunnable, paramLong1, paramLong2, paramTimeUnit);
  }
  
  public void shutdown() {
    while (true) {
      FixedSchedulerPool fixedSchedulerPool1 = this.pool.get();
      FixedSchedulerPool fixedSchedulerPool2 = NONE;
      if (fixedSchedulerPool1 == fixedSchedulerPool2)
        return; 
      if (this.pool.compareAndSet(fixedSchedulerPool1, fixedSchedulerPool2)) {
        fixedSchedulerPool1.shutdown();
        return;
      } 
    } 
  }
  
  public void start() {
    FixedSchedulerPool fixedSchedulerPool = new FixedSchedulerPool(MAX_THREADS, this.threadFactory);
    if (!this.pool.compareAndSet(NONE, fixedSchedulerPool))
      fixedSchedulerPool.shutdown(); 
  }
  
  static {
    SHUTDOWN_WORKER.dispose();
  }
  
  static final class EventLoopWorker extends Scheduler.Worker {
    private final ListCompositeDisposable both;
    
    volatile boolean disposed;
    
    private final ComputationScheduler.PoolWorker poolWorker;
    
    private final ListCompositeDisposable serial;
    
    private final CompositeDisposable timed;
    
    EventLoopWorker(ComputationScheduler.PoolWorker param1PoolWorker) {
      this.poolWorker = param1PoolWorker;
      this.serial = new ListCompositeDisposable();
      this.timed = new CompositeDisposable();
      this.both = new ListCompositeDisposable();
      this.both.add((Disposable)this.serial);
      this.both.add((Disposable)this.timed);
    }
    
    public void dispose() {
      if (!this.disposed) {
        this.disposed = true;
        this.both.dispose();
      } 
    }
    
    public boolean isDisposed() {
      return this.disposed;
    }
    
    public Disposable schedule(Runnable param1Runnable) {
      return (Disposable)(this.disposed ? EmptyDisposable.INSTANCE : this.poolWorker.scheduleActual(param1Runnable, 0L, TimeUnit.MILLISECONDS, (DisposableContainer)this.serial));
    }
    
    public Disposable schedule(Runnable param1Runnable, long param1Long, TimeUnit param1TimeUnit) {
      return (Disposable)(this.disposed ? EmptyDisposable.INSTANCE : this.poolWorker.scheduleActual(param1Runnable, param1Long, param1TimeUnit, (DisposableContainer)this.timed));
    }
  }
  
  static final class FixedSchedulerPool implements SchedulerMultiWorkerSupport {
    final int cores;
    
    final ComputationScheduler.PoolWorker[] eventLoops;
    
    long n;
    
    FixedSchedulerPool(int param1Int, ThreadFactory param1ThreadFactory) {
      this.cores = param1Int;
      this.eventLoops = new ComputationScheduler.PoolWorker[param1Int];
      for (byte b = 0; b < param1Int; b++)
        this.eventLoops[b] = new ComputationScheduler.PoolWorker(param1ThreadFactory); 
    }
    
    public void createWorkers(int param1Int, SchedulerMultiWorkerSupport.WorkerCallback param1WorkerCallback) {
      int i = this.cores;
      int j = 0;
      if (i == 0) {
        while (j < param1Int) {
          param1WorkerCallback.onWorker(j, ComputationScheduler.SHUTDOWN_WORKER);
          j++;
        } 
      } else {
        j = (int)this.n % i;
        for (byte b = 0; b < param1Int; b++) {
          param1WorkerCallback.onWorker(b, new ComputationScheduler.EventLoopWorker(this.eventLoops[j]));
          int k = j + 1;
          j = k;
          if (k == i)
            j = 0; 
        } 
        this.n = j;
      } 
    }
    
    public ComputationScheduler.PoolWorker getEventLoop() {
      int i = this.cores;
      if (i == 0)
        return ComputationScheduler.SHUTDOWN_WORKER; 
      ComputationScheduler.PoolWorker[] arrayOfPoolWorker = this.eventLoops;
      long l = this.n;
      this.n = 1L + l;
      return arrayOfPoolWorker[(int)(l % i)];
    }
    
    public void shutdown() {
      ComputationScheduler.PoolWorker[] arrayOfPoolWorker = this.eventLoops;
      int i = arrayOfPoolWorker.length;
      for (byte b = 0; b < i; b++)
        arrayOfPoolWorker[b].dispose(); 
    }
  }
  
  static final class PoolWorker extends NewThreadWorker {
    PoolWorker(ThreadFactory param1ThreadFactory) {
      super(param1ThreadFactory);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/schedulers/ComputationScheduler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */