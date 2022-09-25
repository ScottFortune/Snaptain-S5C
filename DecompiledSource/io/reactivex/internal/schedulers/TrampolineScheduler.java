package io.reactivex.internal.schedulers;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class TrampolineScheduler extends Scheduler {
  private static final TrampolineScheduler INSTANCE = new TrampolineScheduler();
  
  public static TrampolineScheduler instance() {
    return INSTANCE;
  }
  
  public Scheduler.Worker createWorker() {
    return new TrampolineWorker();
  }
  
  public Disposable scheduleDirect(Runnable paramRunnable) {
    RxJavaPlugins.onSchedule(paramRunnable).run();
    return (Disposable)EmptyDisposable.INSTANCE;
  }
  
  public Disposable scheduleDirect(Runnable paramRunnable, long paramLong, TimeUnit paramTimeUnit) {
    try {
      paramTimeUnit.sleep(paramLong);
      RxJavaPlugins.onSchedule(paramRunnable).run();
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      RxJavaPlugins.onError(interruptedException);
    } 
    return (Disposable)EmptyDisposable.INSTANCE;
  }
  
  static final class SleepingRunnable implements Runnable {
    private final long execTime;
    
    private final Runnable run;
    
    private final TrampolineScheduler.TrampolineWorker worker;
    
    SleepingRunnable(Runnable param1Runnable, TrampolineScheduler.TrampolineWorker param1TrampolineWorker, long param1Long) {
      this.run = param1Runnable;
      this.worker = param1TrampolineWorker;
      this.execTime = param1Long;
    }
    
    public void run() {
      if (!this.worker.disposed) {
        long l1 = this.worker.now(TimeUnit.MILLISECONDS);
        long l2 = this.execTime;
        if (l2 > l1)
          try {
            Thread.sleep(l2 - l1);
          } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            RxJavaPlugins.onError(interruptedException);
            return;
          }  
        if (!this.worker.disposed)
          this.run.run(); 
      } 
    }
  }
  
  static final class TimedRunnable implements Comparable<TimedRunnable> {
    final int count;
    
    volatile boolean disposed;
    
    final long execTime;
    
    final Runnable run;
    
    TimedRunnable(Runnable param1Runnable, Long param1Long, int param1Int) {
      this.run = param1Runnable;
      this.execTime = param1Long.longValue();
      this.count = param1Int;
    }
    
    public int compareTo(TimedRunnable param1TimedRunnable) {
      int i = ObjectHelper.compare(this.execTime, param1TimedRunnable.execTime);
      return (i == 0) ? ObjectHelper.compare(this.count, param1TimedRunnable.count) : i;
    }
  }
  
  static final class TrampolineWorker extends Scheduler.Worker implements Disposable {
    final AtomicInteger counter = new AtomicInteger();
    
    volatile boolean disposed;
    
    final PriorityBlockingQueue<TrampolineScheduler.TimedRunnable> queue = new PriorityBlockingQueue<TrampolineScheduler.TimedRunnable>();
    
    private final AtomicInteger wip = new AtomicInteger();
    
    public void dispose() {
      this.disposed = true;
    }
    
    Disposable enqueue(Runnable param1Runnable, long param1Long) {
      if (this.disposed)
        return (Disposable)EmptyDisposable.INSTANCE; 
      TrampolineScheduler.TimedRunnable timedRunnable = new TrampolineScheduler.TimedRunnable(param1Runnable, Long.valueOf(param1Long), this.counter.incrementAndGet());
      this.queue.add(timedRunnable);
      if (this.wip.getAndIncrement() == 0) {
        int i = 1;
        while (true) {
          if (this.disposed) {
            this.queue.clear();
            return (Disposable)EmptyDisposable.INSTANCE;
          } 
          timedRunnable = this.queue.poll();
          if (timedRunnable == null) {
            int j = this.wip.addAndGet(-i);
            i = j;
            if (j == 0)
              return (Disposable)EmptyDisposable.INSTANCE; 
            continue;
          } 
          if (!timedRunnable.disposed)
            timedRunnable.run.run(); 
        } 
      } 
      return Disposables.fromRunnable(new AppendToQueueTask(timedRunnable));
    }
    
    public boolean isDisposed() {
      return this.disposed;
    }
    
    public Disposable schedule(Runnable param1Runnable) {
      return enqueue(param1Runnable, now(TimeUnit.MILLISECONDS));
    }
    
    public Disposable schedule(Runnable param1Runnable, long param1Long, TimeUnit param1TimeUnit) {
      param1Long = now(TimeUnit.MILLISECONDS) + param1TimeUnit.toMillis(param1Long);
      return enqueue(new TrampolineScheduler.SleepingRunnable(param1Runnable, this, param1Long), param1Long);
    }
    
    final class AppendToQueueTask implements Runnable {
      final TrampolineScheduler.TimedRunnable timedRunnable;
      
      AppendToQueueTask(TrampolineScheduler.TimedRunnable param2TimedRunnable) {
        this.timedRunnable = param2TimedRunnable;
      }
      
      public void run() {
        this.timedRunnable.disposed = true;
        TrampolineScheduler.TrampolineWorker.this.queue.remove(this.timedRunnable);
      }
    }
  }
  
  final class AppendToQueueTask implements Runnable {
    final TrampolineScheduler.TimedRunnable timedRunnable;
    
    AppendToQueueTask(TrampolineScheduler.TimedRunnable param1TimedRunnable) {
      this.timedRunnable = param1TimedRunnable;
    }
    
    public void run() {
      this.timedRunnable.disposed = true;
      this.this$0.queue.remove(this.timedRunnable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/schedulers/TrampolineScheduler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */