package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.observers.SerializedObserver;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableThrottleFirstTimed<T> extends AbstractObservableWithUpstream<T, T> {
  final Scheduler scheduler;
  
  final long timeout;
  
  final TimeUnit unit;
  
  public ObservableThrottleFirstTimed(ObservableSource<T> paramObservableSource, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    super(paramObservableSource);
    this.timeout = paramLong;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new DebounceTimedObserver((Observer<?>)new SerializedObserver(paramObserver), this.timeout, this.unit, this.scheduler.createWorker()));
  }
  
  static final class DebounceTimedObserver<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable, Runnable {
    private static final long serialVersionUID = 786994795061867455L;
    
    boolean done;
    
    final Observer<? super T> downstream;
    
    volatile boolean gate;
    
    final long timeout;
    
    final TimeUnit unit;
    
    Disposable upstream;
    
    final Scheduler.Worker worker;
    
    DebounceTimedObserver(Observer<? super T> param1Observer, long param1Long, TimeUnit param1TimeUnit, Scheduler.Worker param1Worker) {
      this.downstream = param1Observer;
      this.timeout = param1Long;
      this.unit = param1TimeUnit;
      this.worker = param1Worker;
    }
    
    public void dispose() {
      this.upstream.dispose();
      this.worker.dispose();
    }
    
    public boolean isDisposed() {
      return this.worker.isDisposed();
    }
    
    public void onComplete() {
      if (!this.done) {
        this.done = true;
        this.downstream.onComplete();
        this.worker.dispose();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
      } else {
        this.done = true;
        this.downstream.onError(param1Throwable);
        this.worker.dispose();
      } 
    }
    
    public void onNext(T param1T) {
      if (!this.gate && !this.done) {
        this.gate = true;
        this.downstream.onNext(param1T);
        Disposable disposable = get();
        if (disposable != null)
          disposable.dispose(); 
        DisposableHelper.replace(this, this.worker.schedule(this, this.timeout, this.unit));
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void run() {
      this.gate = false;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableThrottleFirstTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */