package io.reactivex.schedulers;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public final class TestScheduler extends Scheduler {
  long counter;
  
  final Queue<TimedRunnable> queue = new PriorityBlockingQueue<TimedRunnable>(11);
  
  volatile long time;
  
  public TestScheduler() {}
  
  public TestScheduler(long paramLong, TimeUnit paramTimeUnit) {
    this.time = paramTimeUnit.toNanos(paramLong);
  }
  
  private void triggerActions(long paramLong) {
    while (true) {
      long l;
      TimedRunnable timedRunnable = this.queue.peek();
      if (timedRunnable == null || timedRunnable.time > paramLong)
        break; 
      if (timedRunnable.time == 0L) {
        l = this.time;
      } else {
        l = timedRunnable.time;
      } 
      this.time = l;
      this.queue.remove(timedRunnable);
      if (!timedRunnable.scheduler.disposed)
        timedRunnable.run.run(); 
    } 
    this.time = paramLong;
  }
  
  public void advanceTimeBy(long paramLong, TimeUnit paramTimeUnit) {
    advanceTimeTo(this.time + paramTimeUnit.toNanos(paramLong), TimeUnit.NANOSECONDS);
  }
  
  public void advanceTimeTo(long paramLong, TimeUnit paramTimeUnit) {
    triggerActions(paramTimeUnit.toNanos(paramLong));
  }
  
  public Scheduler.Worker createWorker() {
    return new TestWorker();
  }
  
  public long now(TimeUnit paramTimeUnit) {
    return paramTimeUnit.convert(this.time, TimeUnit.NANOSECONDS);
  }
  
  public void triggerActions() {
    triggerActions(this.time);
  }
  
  final class TestWorker extends Scheduler.Worker {
    volatile boolean disposed;
    
    public void dispose() {
      this.disposed = true;
    }
    
    public boolean isDisposed() {
      return this.disposed;
    }
    
    public long now(TimeUnit param1TimeUnit) {
      return TestScheduler.this.now(param1TimeUnit);
    }
    
    public Disposable schedule(Runnable param1Runnable) {
      if (this.disposed)
        return (Disposable)EmptyDisposable.INSTANCE; 
      TestScheduler testScheduler = TestScheduler.this;
      long l = testScheduler.counter;
      testScheduler.counter = 1L + l;
      TestScheduler.TimedRunnable timedRunnable = new TestScheduler.TimedRunnable(this, 0L, param1Runnable, l);
      TestScheduler.this.queue.add(timedRunnable);
      return Disposables.fromRunnable(new QueueRemove(timedRunnable));
    }
    
    public Disposable schedule(Runnable param1Runnable, long param1Long, TimeUnit param1TimeUnit) {
      if (this.disposed)
        return (Disposable)EmptyDisposable.INSTANCE; 
      long l1 = TestScheduler.this.time;
      param1Long = param1TimeUnit.toNanos(param1Long);
      TestScheduler testScheduler = TestScheduler.this;
      long l2 = testScheduler.counter;
      testScheduler.counter = 1L + l2;
      TestScheduler.TimedRunnable timedRunnable = new TestScheduler.TimedRunnable(this, l1 + param1Long, param1Runnable, l2);
      TestScheduler.this.queue.add(timedRunnable);
      return Disposables.fromRunnable(new QueueRemove(timedRunnable));
    }
    
    final class QueueRemove implements Runnable {
      final TestScheduler.TimedRunnable timedAction;
      
      QueueRemove(TestScheduler.TimedRunnable param2TimedRunnable) {
        this.timedAction = param2TimedRunnable;
      }
      
      public void run() {
        TestScheduler.this.queue.remove(this.timedAction);
      }
    }
  }
  
  final class QueueRemove implements Runnable {
    final TestScheduler.TimedRunnable timedAction;
    
    QueueRemove(TestScheduler.TimedRunnable param1TimedRunnable) {
      this.timedAction = param1TimedRunnable;
    }
    
    public void run() {
      TestScheduler.this.queue.remove(this.timedAction);
    }
  }
  
  static final class TimedRunnable implements Comparable<TimedRunnable> {
    final long count;
    
    final Runnable run;
    
    final TestScheduler.TestWorker scheduler;
    
    final long time;
    
    TimedRunnable(TestScheduler.TestWorker param1TestWorker, long param1Long1, Runnable param1Runnable, long param1Long2) {
      this.time = param1Long1;
      this.run = param1Runnable;
      this.scheduler = param1TestWorker;
      this.count = param1Long2;
    }
    
    public int compareTo(TimedRunnable param1TimedRunnable) {
      long l1 = this.time;
      long l2 = param1TimedRunnable.time;
      return (l1 == l2) ? ObjectHelper.compare(this.count, param1TimedRunnable.count) : ObjectHelper.compare(l1, l2);
    }
    
    public String toString() {
      return String.format("TimedRunnable(time = %d, run = %s)", new Object[] { Long.valueOf(this.time), this.run.toString() });
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/schedulers/TestScheduler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */