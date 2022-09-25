package io.reactivex.internal.schedulers;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableContainer;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleScheduler extends Scheduler {
  private static final String KEY_SINGLE_PRIORITY = "rx2.single-priority";
  
  static final ScheduledExecutorService SHUTDOWN = Executors.newScheduledThreadPool(0);
  
  static final RxThreadFactory SINGLE_THREAD_FACTORY = new RxThreadFactory("RxSingleScheduler", Math.max(1, Math.min(10, Integer.getInteger("rx2.single-priority", 5).intValue())), true);
  
  private static final String THREAD_NAME_PREFIX = "RxSingleScheduler";
  
  final AtomicReference<ScheduledExecutorService> executor = new AtomicReference<ScheduledExecutorService>();
  
  final ThreadFactory threadFactory;
  
  public SingleScheduler() {
    this(SINGLE_THREAD_FACTORY);
  }
  
  public SingleScheduler(ThreadFactory paramThreadFactory) {
    this.threadFactory = paramThreadFactory;
    this.executor.lazySet(createExecutor(paramThreadFactory));
  }
  
  static ScheduledExecutorService createExecutor(ThreadFactory paramThreadFactory) {
    return SchedulerPoolFactory.create(paramThreadFactory);
  }
  
  public Scheduler.Worker createWorker() {
    return new ScheduledWorker(this.executor.get());
  }
  
  public Disposable scheduleDirect(Runnable paramRunnable, long paramLong, TimeUnit paramTimeUnit) {
    ScheduledDirectTask scheduledDirectTask = new ScheduledDirectTask(RxJavaPlugins.onSchedule(paramRunnable));
    if (paramLong <= 0L)
      try {
        Future<?> future = ((ScheduledExecutorService)this.executor.get()).submit(scheduledDirectTask);
        scheduledDirectTask.setFuture(future);
        return scheduledDirectTask;
      } catch (RejectedExecutionException rejectedExecutionException) {
        RxJavaPlugins.onError(rejectedExecutionException);
        return (Disposable)EmptyDisposable.INSTANCE;
      }  
    ScheduledFuture<?> scheduledFuture = ((ScheduledExecutorService)this.executor.get()).schedule(scheduledDirectTask, paramLong, paramTimeUnit);
    scheduledDirectTask.setFuture(scheduledFuture);
    return scheduledDirectTask;
  }
  
  public Disposable schedulePeriodicallyDirect(Runnable paramRunnable, long paramLong1, long paramLong2, TimeUnit paramTimeUnit) {
    InstantPeriodicTask instantPeriodicTask;
    Runnable runnable = RxJavaPlugins.onSchedule(paramRunnable);
    if (paramLong2 <= 0L) {
      ScheduledExecutorService scheduledExecutorService = this.executor.get();
      instantPeriodicTask = new InstantPeriodicTask(runnable, scheduledExecutorService);
      if (paramLong1 <= 0L)
        try {
          Future<?> future = scheduledExecutorService.submit(instantPeriodicTask);
          instantPeriodicTask.setFirst(future);
          return instantPeriodicTask;
        } catch (RejectedExecutionException rejectedExecutionException) {
          RxJavaPlugins.onError(rejectedExecutionException);
          return (Disposable)EmptyDisposable.INSTANCE;
        }  
      ScheduledFuture<?> scheduledFuture = rejectedExecutionException.schedule(instantPeriodicTask, paramLong1, paramTimeUnit);
      instantPeriodicTask.setFirst(scheduledFuture);
      return instantPeriodicTask;
    } 
    paramRunnable = new ScheduledDirectPeriodicTask((Runnable)instantPeriodicTask);
    try {
      paramRunnable.setFuture(((ScheduledExecutorService)this.executor.get()).scheduleAtFixedRate(paramRunnable, paramLong1, paramLong2, paramTimeUnit));
      return (Disposable)paramRunnable;
    } catch (RejectedExecutionException rejectedExecutionException) {
      RxJavaPlugins.onError(rejectedExecutionException);
      return (Disposable)EmptyDisposable.INSTANCE;
    } 
  }
  
  public void shutdown() {
    ScheduledExecutorService scheduledExecutorService1 = this.executor.get();
    ScheduledExecutorService scheduledExecutorService2 = SHUTDOWN;
    if (scheduledExecutorService1 != scheduledExecutorService2) {
      scheduledExecutorService1 = this.executor.getAndSet(scheduledExecutorService2);
      if (scheduledExecutorService1 != SHUTDOWN)
        scheduledExecutorService1.shutdownNow(); 
    } 
  }
  
  public void start() {
    ScheduledExecutorService scheduledExecutorService2;
    ScheduledExecutorService scheduledExecutorService3;
    ScheduledExecutorService scheduledExecutorService1 = null;
    do {
      scheduledExecutorService2 = this.executor.get();
      if (scheduledExecutorService2 != SHUTDOWN) {
        if (scheduledExecutorService1 != null)
          scheduledExecutorService1.shutdown(); 
        return;
      } 
      scheduledExecutorService3 = scheduledExecutorService1;
      if (scheduledExecutorService1 == null)
        scheduledExecutorService3 = createExecutor(this.threadFactory); 
      scheduledExecutorService1 = scheduledExecutorService3;
    } while (!this.executor.compareAndSet(scheduledExecutorService2, scheduledExecutorService3));
  }
  
  static {
    SHUTDOWN.shutdown();
  }
  
  static final class ScheduledWorker extends Scheduler.Worker {
    volatile boolean disposed;
    
    final ScheduledExecutorService executor;
    
    final CompositeDisposable tasks;
    
    ScheduledWorker(ScheduledExecutorService param1ScheduledExecutorService) {
      this.executor = param1ScheduledExecutorService;
      this.tasks = new CompositeDisposable();
    }
    
    public void dispose() {
      if (!this.disposed) {
        this.disposed = true;
        this.tasks.dispose();
      } 
    }
    
    public boolean isDisposed() {
      return this.disposed;
    }
    
    public Disposable schedule(Runnable param1Runnable, long param1Long, TimeUnit param1TimeUnit) {
      if (this.disposed)
        return (Disposable)EmptyDisposable.INSTANCE; 
      ScheduledRunnable scheduledRunnable = new ScheduledRunnable(RxJavaPlugins.onSchedule(param1Runnable), (DisposableContainer)this.tasks);
      this.tasks.add(scheduledRunnable);
      if (param1Long <= 0L)
        try {
          Future<?> future = this.executor.submit(scheduledRunnable);
          scheduledRunnable.setFuture(future);
          return scheduledRunnable;
        } catch (RejectedExecutionException rejectedExecutionException) {
          dispose();
          RxJavaPlugins.onError(rejectedExecutionException);
          return (Disposable)EmptyDisposable.INSTANCE;
        }  
      ScheduledFuture<?> scheduledFuture = this.executor.schedule(scheduledRunnable, param1Long, param1TimeUnit);
      scheduledRunnable.setFuture(scheduledFuture);
      return scheduledRunnable;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/schedulers/SingleScheduler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */