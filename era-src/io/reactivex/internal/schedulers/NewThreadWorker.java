package io.reactivex.internal.schedulers;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableContainer;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class NewThreadWorker extends Scheduler.Worker implements Disposable {
  volatile boolean disposed;
  
  private final ScheduledExecutorService executor;
  
  public NewThreadWorker(ThreadFactory paramThreadFactory) {
    this.executor = SchedulerPoolFactory.create(paramThreadFactory);
  }
  
  public void dispose() {
    if (!this.disposed) {
      this.disposed = true;
      this.executor.shutdownNow();
    } 
  }
  
  public boolean isDisposed() {
    return this.disposed;
  }
  
  public Disposable schedule(Runnable paramRunnable) {
    return schedule(paramRunnable, 0L, null);
  }
  
  public Disposable schedule(Runnable paramRunnable, long paramLong, TimeUnit paramTimeUnit) {
    return (Disposable)(this.disposed ? EmptyDisposable.INSTANCE : scheduleActual(paramRunnable, paramLong, paramTimeUnit, null));
  }
  
  public ScheduledRunnable scheduleActual(Runnable paramRunnable, long paramLong, TimeUnit paramTimeUnit, DisposableContainer paramDisposableContainer) {
    ScheduledRunnable scheduledRunnable = new ScheduledRunnable(RxJavaPlugins.onSchedule(paramRunnable), paramDisposableContainer);
    if (paramDisposableContainer != null && !paramDisposableContainer.add(scheduledRunnable))
      return scheduledRunnable; 
    if (paramLong <= 0L) {
      try {
        Future<?> future = this.executor.submit(scheduledRunnable);
        scheduledRunnable.setFuture(future);
      } catch (RejectedExecutionException rejectedExecutionException) {
        if (paramDisposableContainer != null)
          paramDisposableContainer.remove(scheduledRunnable); 
        RxJavaPlugins.onError(rejectedExecutionException);
      } 
    } else {
      ScheduledFuture<?> scheduledFuture = this.executor.schedule(scheduledRunnable, paramLong, paramTimeUnit);
      scheduledRunnable.setFuture(scheduledFuture);
    } 
    return scheduledRunnable;
  }
  
  public Disposable scheduleDirect(Runnable paramRunnable, long paramLong, TimeUnit paramTimeUnit) {
    ScheduledDirectTask scheduledDirectTask = new ScheduledDirectTask(RxJavaPlugins.onSchedule(paramRunnable));
    if (paramLong <= 0L)
      try {
        Future<?> future = this.executor.submit(scheduledDirectTask);
        scheduledDirectTask.setFuture(future);
        return scheduledDirectTask;
      } catch (RejectedExecutionException rejectedExecutionException) {
        RxJavaPlugins.onError(rejectedExecutionException);
        return (Disposable)EmptyDisposable.INSTANCE;
      }  
    ScheduledFuture<?> scheduledFuture = this.executor.schedule(scheduledDirectTask, paramLong, paramTimeUnit);
    scheduledDirectTask.setFuture(scheduledFuture);
    return scheduledDirectTask;
  }
  
  public Disposable schedulePeriodicallyDirect(Runnable paramRunnable, long paramLong1, long paramLong2, TimeUnit paramTimeUnit) {
    ScheduledFuture<?> scheduledFuture;
    paramRunnable = RxJavaPlugins.onSchedule(paramRunnable);
    if (paramLong2 <= 0L) {
      InstantPeriodicTask instantPeriodicTask = new InstantPeriodicTask(paramRunnable, this.executor);
      if (paramLong1 <= 0L)
        try {
          Future<?> future = this.executor.submit(instantPeriodicTask);
          instantPeriodicTask.setFirst(future);
          return instantPeriodicTask;
        } catch (RejectedExecutionException rejectedExecutionException) {
          RxJavaPlugins.onError(rejectedExecutionException);
          return (Disposable)EmptyDisposable.INSTANCE;
        }  
      scheduledFuture = this.executor.schedule(instantPeriodicTask, paramLong1, paramTimeUnit);
      instantPeriodicTask.setFirst(scheduledFuture);
      return instantPeriodicTask;
    } 
    ScheduledDirectPeriodicTask scheduledDirectPeriodicTask = new ScheduledDirectPeriodicTask((Runnable)scheduledFuture);
    try {
      scheduledDirectPeriodicTask.setFuture(this.executor.scheduleAtFixedRate(scheduledDirectPeriodicTask, paramLong1, paramLong2, paramTimeUnit));
      return scheduledDirectPeriodicTask;
    } catch (RejectedExecutionException rejectedExecutionException) {
      RxJavaPlugins.onError(rejectedExecutionException);
      return (Disposable)EmptyDisposable.INSTANCE;
    } 
  }
  
  public void shutdown() {
    if (!this.disposed) {
      this.disposed = true;
      this.executor.shutdown();
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/schedulers/NewThreadWorker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */