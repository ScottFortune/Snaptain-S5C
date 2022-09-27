package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.schedulers.Timed;
import java.util.concurrent.TimeUnit;

public final class ObservableTimeInterval<T> extends AbstractObservableWithUpstream<T, Timed<T>> {
  final Scheduler scheduler;
  
  final TimeUnit unit;
  
  public ObservableTimeInterval(ObservableSource<T> paramObservableSource, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    super(paramObservableSource);
    this.scheduler = paramScheduler;
    this.unit = paramTimeUnit;
  }
  
  public void subscribeActual(Observer<? super Timed<T>> paramObserver) {
    this.source.subscribe(new TimeIntervalObserver<T>(paramObserver, this.unit, this.scheduler));
  }
  
  static final class TimeIntervalObserver<T> implements Observer<T>, Disposable {
    final Observer<? super Timed<T>> downstream;
    
    long lastTime;
    
    final Scheduler scheduler;
    
    final TimeUnit unit;
    
    Disposable upstream;
    
    TimeIntervalObserver(Observer<? super Timed<T>> param1Observer, TimeUnit param1TimeUnit, Scheduler param1Scheduler) {
      this.downstream = param1Observer;
      this.scheduler = param1Scheduler;
      this.unit = param1TimeUnit;
    }
    
    public void dispose() {
      this.upstream.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      long l1 = this.scheduler.now(this.unit);
      long l2 = this.lastTime;
      this.lastTime = l1;
      this.downstream.onNext(new Timed(param1T, l1 - l2, this.unit));
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.lastTime = this.scheduler.now(this.unit);
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableTimeInterval.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */