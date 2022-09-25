package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableTimer extends Observable<Long> {
  final long delay;
  
  final Scheduler scheduler;
  
  final TimeUnit unit;
  
  public ObservableTimer(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    this.delay = paramLong;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
  }
  
  public void subscribeActual(Observer<? super Long> paramObserver) {
    TimerObserver timerObserver = new TimerObserver(paramObserver);
    paramObserver.onSubscribe(timerObserver);
    timerObserver.setResource(this.scheduler.scheduleDirect(timerObserver, this.delay, this.unit));
  }
  
  static final class TimerObserver extends AtomicReference<Disposable> implements Disposable, Runnable {
    private static final long serialVersionUID = -2809475196591179431L;
    
    final Observer<? super Long> downstream;
    
    TimerObserver(Observer<? super Long> param1Observer) {
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
      if (!isDisposed()) {
        this.downstream.onNext(Long.valueOf(0L));
        lazySet((Disposable)EmptyDisposable.INSTANCE);
        this.downstream.onComplete();
      } 
    }
    
    public void setResource(Disposable param1Disposable) {
      DisposableHelper.trySet(this, param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableTimer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */