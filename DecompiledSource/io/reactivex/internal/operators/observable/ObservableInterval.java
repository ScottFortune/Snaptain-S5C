package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableInterval extends Observable<Long> {
  final long initialDelay;
  
  final long period;
  
  final Scheduler scheduler;
  
  final TimeUnit unit;
  
  public ObservableInterval(long paramLong1, long paramLong2, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    this.initialDelay = paramLong1;
    this.period = paramLong2;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
  }
  
  public void subscribeActual(Observer<? super Long> paramObserver) {
    Scheduler.Worker worker;
    IntervalObserver intervalObserver = new IntervalObserver(paramObserver);
    paramObserver.onSubscribe(intervalObserver);
    Scheduler scheduler = this.scheduler;
    if (scheduler instanceof io.reactivex.internal.schedulers.TrampolineScheduler) {
      worker = scheduler.createWorker();
      intervalObserver.setResource((Disposable)worker);
      worker.schedulePeriodically(intervalObserver, this.initialDelay, this.period, this.unit);
    } else {
      intervalObserver.setResource(worker.schedulePeriodicallyDirect(intervalObserver, this.initialDelay, this.period, this.unit));
    } 
  }
  
  static final class IntervalObserver extends AtomicReference<Disposable> implements Disposable, Runnable {
    private static final long serialVersionUID = 346773832286157679L;
    
    long count;
    
    final Observer<? super Long> downstream;
    
    IntervalObserver(Observer<? super Long> param1Observer) {
      this.downstream = param1Observer;
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
      if (get() != DisposableHelper.DISPOSED) {
        Observer<? super Long> observer = this.downstream;
        long l = this.count;
        this.count = 1L + l;
        observer.onNext(Long.valueOf(l));
      } 
    }
    
    public void setResource(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableInterval.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */