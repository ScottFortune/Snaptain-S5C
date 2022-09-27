package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableIntervalRange extends Observable<Long> {
  final long end;
  
  final long initialDelay;
  
  final long period;
  
  final Scheduler scheduler;
  
  final long start;
  
  final TimeUnit unit;
  
  public ObservableIntervalRange(long paramLong1, long paramLong2, long paramLong3, long paramLong4, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    this.initialDelay = paramLong3;
    this.period = paramLong4;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
    this.start = paramLong1;
    this.end = paramLong2;
  }
  
  public void subscribeActual(Observer<? super Long> paramObserver) {
    Scheduler.Worker worker;
    IntervalRangeObserver intervalRangeObserver = new IntervalRangeObserver(paramObserver, this.start, this.end);
    paramObserver.onSubscribe(intervalRangeObserver);
    Scheduler scheduler = this.scheduler;
    if (scheduler instanceof io.reactivex.internal.schedulers.TrampolineScheduler) {
      worker = scheduler.createWorker();
      intervalRangeObserver.setResource((Disposable)worker);
      worker.schedulePeriodically(intervalRangeObserver, this.initialDelay, this.period, this.unit);
    } else {
      intervalRangeObserver.setResource(worker.schedulePeriodicallyDirect(intervalRangeObserver, this.initialDelay, this.period, this.unit));
    } 
  }
  
  static final class IntervalRangeObserver extends AtomicReference<Disposable> implements Disposable, Runnable {
    private static final long serialVersionUID = 1891866368734007884L;
    
    long count;
    
    final Observer<? super Long> downstream;
    
    final long end;
    
    IntervalRangeObserver(Observer<? super Long> param1Observer, long param1Long1, long param1Long2) {
      this.downstream = param1Observer;
      this.count = param1Long1;
      this.end = param1Long2;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (get() == DisposableHelper.DISPOSED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void run() {
      if (!isDisposed()) {
        long l = this.count;
        this.downstream.onNext(Long.valueOf(l));
        if (l == this.end) {
          DisposableHelper.dispose(this);
          this.downstream.onComplete();
          return;
        } 
        this.count = l + 1L;
      } 
    }
    
    public void setResource(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableIntervalRange.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */