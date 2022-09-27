package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.observers.SerializedObserver;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableSampleTimed<T> extends AbstractObservableWithUpstream<T, T> {
  final boolean emitLast;
  
  final long period;
  
  final Scheduler scheduler;
  
  final TimeUnit unit;
  
  public ObservableSampleTimed(ObservableSource<T> paramObservableSource, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, boolean paramBoolean) {
    super(paramObservableSource);
    this.period = paramLong;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
    this.emitLast = paramBoolean;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    SerializedObserver serializedObserver = new SerializedObserver(paramObserver);
    if (this.emitLast) {
      this.source.subscribe(new SampleTimedEmitLast((Observer<?>)serializedObserver, this.period, this.unit, this.scheduler));
    } else {
      this.source.subscribe(new SampleTimedNoLast((Observer<?>)serializedObserver, this.period, this.unit, this.scheduler));
    } 
  }
  
  static final class SampleTimedEmitLast<T> extends SampleTimedObserver<T> {
    private static final long serialVersionUID = -7139995637533111443L;
    
    final AtomicInteger wip = new AtomicInteger(1);
    
    SampleTimedEmitLast(Observer<? super T> param1Observer, long param1Long, TimeUnit param1TimeUnit, Scheduler param1Scheduler) {
      super(param1Observer, param1Long, param1TimeUnit, param1Scheduler);
    }
    
    void complete() {
      emit();
      if (this.wip.decrementAndGet() == 0)
        this.downstream.onComplete(); 
    }
    
    public void run() {
      if (this.wip.incrementAndGet() == 2) {
        emit();
        if (this.wip.decrementAndGet() == 0)
          this.downstream.onComplete(); 
      } 
    }
  }
  
  static final class SampleTimedNoLast<T> extends SampleTimedObserver<T> {
    private static final long serialVersionUID = -7139995637533111443L;
    
    SampleTimedNoLast(Observer<? super T> param1Observer, long param1Long, TimeUnit param1TimeUnit, Scheduler param1Scheduler) {
      super(param1Observer, param1Long, param1TimeUnit, param1Scheduler);
    }
    
    void complete() {
      this.downstream.onComplete();
    }
    
    public void run() {
      emit();
    }
  }
  
  static abstract class SampleTimedObserver<T> extends AtomicReference<T> implements Observer<T>, Disposable, Runnable {
    private static final long serialVersionUID = -3517602651313910099L;
    
    final Observer<? super T> downstream;
    
    final long period;
    
    final Scheduler scheduler;
    
    final AtomicReference<Disposable> timer = new AtomicReference<Disposable>();
    
    final TimeUnit unit;
    
    Disposable upstream;
    
    SampleTimedObserver(Observer<? super T> param1Observer, long param1Long, TimeUnit param1TimeUnit, Scheduler param1Scheduler) {
      this.downstream = param1Observer;
      this.period = param1Long;
      this.unit = param1TimeUnit;
      this.scheduler = param1Scheduler;
    }
    
    void cancelTimer() {
      DisposableHelper.dispose(this.timer);
    }
    
    abstract void complete();
    
    public void dispose() {
      cancelTimer();
      this.upstream.dispose();
    }
    
    void emit() {
      T t = getAndSet(null);
      if (t != null)
        this.downstream.onNext(t); 
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      cancelTimer();
      complete();
    }
    
    public void onError(Throwable param1Throwable) {
      cancelTimer();
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      lazySet(param1T);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
        Scheduler scheduler = this.scheduler;
        long l = this.period;
        Disposable disposable = scheduler.schedulePeriodicallyDirect(this, l, l, this.unit);
        DisposableHelper.replace(this.timer, disposable);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableSampleTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */