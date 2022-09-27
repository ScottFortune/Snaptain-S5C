package io.reactivex;

import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.internal.schedulers.NewThreadWorker;
import io.reactivex.internal.schedulers.SchedulerWhen;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.SchedulerRunnableIntrospection;
import java.util.concurrent.TimeUnit;

public abstract class Scheduler {
  static final long CLOCK_DRIFT_TOLERANCE_NANOSECONDS = TimeUnit.MINUTES.toNanos(Long.getLong("rx2.scheduler.drift-tolerance", 15L).longValue());
  
  public static long clockDriftTolerance() {
    return CLOCK_DRIFT_TOLERANCE_NANOSECONDS;
  }
  
  public abstract Worker createWorker();
  
  public long now(TimeUnit paramTimeUnit) {
    return paramTimeUnit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
  }
  
  public Disposable scheduleDirect(Runnable paramRunnable) {
    return scheduleDirect(paramRunnable, 0L, TimeUnit.NANOSECONDS);
  }
  
  public Disposable scheduleDirect(Runnable paramRunnable, long paramLong, TimeUnit paramTimeUnit) {
    Worker worker = createWorker();
    paramRunnable = new DisposeTask(RxJavaPlugins.onSchedule(paramRunnable), worker);
    worker.schedule(paramRunnable, paramLong, paramTimeUnit);
    return (Disposable)paramRunnable;
  }
  
  public Disposable schedulePeriodicallyDirect(Runnable paramRunnable, long paramLong1, long paramLong2, TimeUnit paramTimeUnit) {
    Worker worker = createWorker();
    paramRunnable = new PeriodicDirectTask(RxJavaPlugins.onSchedule(paramRunnable), worker);
    Disposable disposable = worker.schedulePeriodically(paramRunnable, paramLong1, paramLong2, paramTimeUnit);
    return (Disposable)((disposable == EmptyDisposable.INSTANCE) ? disposable : paramRunnable);
  }
  
  public void shutdown() {}
  
  public void start() {}
  
  public <S extends Scheduler & Disposable> S when(Function<Flowable<Flowable<Completable>>, Completable> paramFunction) {
    return (S)new SchedulerWhen(paramFunction, this);
  }
  
  static final class DisposeTask implements Disposable, Runnable, SchedulerRunnableIntrospection {
    final Runnable decoratedRun;
    
    Thread runner;
    
    final Scheduler.Worker w;
    
    DisposeTask(Runnable param1Runnable, Scheduler.Worker param1Worker) {
      this.decoratedRun = param1Runnable;
      this.w = param1Worker;
    }
    
    public void dispose() {
      if (this.runner == Thread.currentThread()) {
        Scheduler.Worker worker = this.w;
        if (worker instanceof NewThreadWorker) {
          ((NewThreadWorker)worker).shutdown();
          return;
        } 
      } 
      this.w.dispose();
    }
    
    public Runnable getWrappedRunnable() {
      return this.decoratedRun;
    }
    
    public boolean isDisposed() {
      return this.w.isDisposed();
    }
    
    public void run() {
      this.runner = Thread.currentThread();
      try {
        this.decoratedRun.run();
        return;
      } finally {
        dispose();
        this.runner = null;
      } 
    }
  }
  
  static final class PeriodicDirectTask implements Disposable, Runnable, SchedulerRunnableIntrospection {
    volatile boolean disposed;
    
    final Runnable run;
    
    final Scheduler.Worker worker;
    
    PeriodicDirectTask(Runnable param1Runnable, Scheduler.Worker param1Worker) {
      this.run = param1Runnable;
      this.worker = param1Worker;
    }
    
    public void dispose() {
      this.disposed = true;
      this.worker.dispose();
    }
    
    public Runnable getWrappedRunnable() {
      return this.run;
    }
    
    public boolean isDisposed() {
      return this.disposed;
    }
    
    public void run() {
      if (!this.disposed)
        try {
          this.run.run();
        } finally {
          Exception exception = null;
          Exceptions.throwIfFatal(exception);
          this.worker.dispose();
        }  
    }
  }
  
  public static abstract class Worker implements Disposable {
    public long now(TimeUnit param1TimeUnit) {
      return param1TimeUnit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }
    
    public Disposable schedule(Runnable param1Runnable) {
      return schedule(param1Runnable, 0L, TimeUnit.NANOSECONDS);
    }
    
    public abstract Disposable schedule(Runnable param1Runnable, long param1Long, TimeUnit param1TimeUnit);
    
    public Disposable schedulePeriodically(Runnable param1Runnable, long param1Long1, long param1Long2, TimeUnit param1TimeUnit) {
      SequentialDisposable sequentialDisposable1 = new SequentialDisposable();
      SequentialDisposable sequentialDisposable2 = new SequentialDisposable((Disposable)sequentialDisposable1);
      param1Runnable = RxJavaPlugins.onSchedule(param1Runnable);
      param1Long2 = param1TimeUnit.toNanos(param1Long2);
      long l = now(TimeUnit.NANOSECONDS);
      Disposable disposable = schedule(new PeriodicTask(l + param1TimeUnit.toNanos(param1Long1), param1Runnable, l, sequentialDisposable2, param1Long2), param1Long1, param1TimeUnit);
      if (disposable == EmptyDisposable.INSTANCE)
        return disposable; 
      sequentialDisposable1.replace(disposable);
      return (Disposable)sequentialDisposable2;
    }
    
    final class PeriodicTask implements Runnable, SchedulerRunnableIntrospection {
      long count;
      
      final Runnable decoratedRun;
      
      long lastNowNanoseconds;
      
      final long periodInNanoseconds;
      
      final SequentialDisposable sd;
      
      long startInNanoseconds;
      
      PeriodicTask(long param2Long1, Runnable param2Runnable, long param2Long2, SequentialDisposable param2SequentialDisposable, long param2Long3) {
        this.decoratedRun = param2Runnable;
        this.sd = param2SequentialDisposable;
        this.periodInNanoseconds = param2Long3;
        this.lastNowNanoseconds = param2Long2;
        this.startInNanoseconds = param2Long1;
      }
      
      public Runnable getWrappedRunnable() {
        return this.decoratedRun;
      }
      
      public void run() {
        this.decoratedRun.run();
        if (!this.sd.isDisposed()) {
          long l1 = Scheduler.Worker.this.now(TimeUnit.NANOSECONDS);
          long l2 = Scheduler.CLOCK_DRIFT_TOLERANCE_NANOSECONDS;
          long l3 = this.lastNowNanoseconds;
          if (l2 + l1 < l3 || l1 >= l3 + this.periodInNanoseconds + Scheduler.CLOCK_DRIFT_TOLERANCE_NANOSECONDS) {
            long l = this.periodInNanoseconds;
            l3 = l1 + l;
            l2 = this.count + 1L;
            this.count = l2;
            this.startInNanoseconds = l3 - l * l2;
          } else {
            l2 = this.startInNanoseconds;
            l3 = this.count + 1L;
            this.count = l3;
            l3 = l2 + l3 * this.periodInNanoseconds;
          } 
          this.lastNowNanoseconds = l1;
          this.sd.replace(Scheduler.Worker.this.schedule(this, l3 - l1, TimeUnit.NANOSECONDS));
        } 
      }
    }
  }
  
  final class PeriodicTask implements Runnable, SchedulerRunnableIntrospection {
    long count;
    
    final Runnable decoratedRun;
    
    long lastNowNanoseconds;
    
    final long periodInNanoseconds;
    
    final SequentialDisposable sd;
    
    long startInNanoseconds;
    
    PeriodicTask(long param1Long1, Runnable param1Runnable, long param1Long2, SequentialDisposable param1SequentialDisposable, long param1Long3) {
      this.decoratedRun = param1Runnable;
      this.sd = param1SequentialDisposable;
      this.periodInNanoseconds = param1Long3;
      this.lastNowNanoseconds = param1Long2;
      this.startInNanoseconds = param1Long1;
    }
    
    public Runnable getWrappedRunnable() {
      return this.decoratedRun;
    }
    
    public void run() {
      this.decoratedRun.run();
      if (!this.sd.isDisposed()) {
        long l1 = this.this$0.now(TimeUnit.NANOSECONDS);
        long l2 = Scheduler.CLOCK_DRIFT_TOLERANCE_NANOSECONDS;
        long l3 = this.lastNowNanoseconds;
        if (l2 + l1 < l3 || l1 >= l3 + this.periodInNanoseconds + Scheduler.CLOCK_DRIFT_TOLERANCE_NANOSECONDS) {
          long l = this.periodInNanoseconds;
          l3 = l1 + l;
          l2 = this.count + 1L;
          this.count = l2;
          this.startInNanoseconds = l3 - l * l2;
        } else {
          l2 = this.startInNanoseconds;
          l3 = this.count + 1L;
          this.count = l3;
          l3 = l2 + l3 * this.periodInNanoseconds;
        } 
        this.lastNowNanoseconds = l1;
        this.sd.replace(this.this$0.schedule(this, l3 - l1, TimeUnit.NANOSECONDS));
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/Scheduler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */