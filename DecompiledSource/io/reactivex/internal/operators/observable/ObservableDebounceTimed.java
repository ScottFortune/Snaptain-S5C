package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.observers.SerializedObserver;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableDebounceTimed<T> extends AbstractObservableWithUpstream<T, T> {
  final Scheduler scheduler;
  
  final long timeout;
  
  final TimeUnit unit;
  
  public ObservableDebounceTimed(ObservableSource<T> paramObservableSource, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    super(paramObservableSource);
    this.timeout = paramLong;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new DebounceTimedObserver((Observer<?>)new SerializedObserver(paramObserver), this.timeout, this.unit, this.scheduler.createWorker()));
  }
  
  static final class DebounceEmitter<T> extends AtomicReference<Disposable> implements Runnable, Disposable {
    private static final long serialVersionUID = 6812032969491025141L;
    
    final long idx;
    
    final AtomicBoolean once = new AtomicBoolean();
    
    final ObservableDebounceTimed.DebounceTimedObserver<T> parent;
    
    final T value;
    
    DebounceEmitter(T param1T, long param1Long, ObservableDebounceTimed.DebounceTimedObserver<T> param1DebounceTimedObserver) {
      this.value = param1T;
      this.idx = param1Long;
      this.parent = param1DebounceTimedObserver;
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
      if (this.once.compareAndSet(false, true))
        this.parent.emit(this.idx, this.value, this); 
    }
    
    public void setResource(Disposable param1Disposable) {
      DisposableHelper.replace(this, param1Disposable);
    }
  }
  
  static final class DebounceTimedObserver<T> implements Observer<T>, Disposable {
    boolean done;
    
    final Observer<? super T> downstream;
    
    volatile long index;
    
    final long timeout;
    
    Disposable timer;
    
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
    
    void emit(long param1Long, T param1T, ObservableDebounceTimed.DebounceEmitter<T> param1DebounceEmitter) {
      if (param1Long == this.index) {
        this.downstream.onNext(param1T);
        param1DebounceEmitter.dispose();
      } 
    }
    
    public boolean isDisposed() {
      return this.worker.isDisposed();
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      Disposable disposable = this.timer;
      if (disposable != null)
        disposable.dispose(); 
      disposable = disposable;
      if (disposable != null)
        disposable.run(); 
      this.downstream.onComplete();
      this.worker.dispose();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      Disposable disposable = this.timer;
      if (disposable != null)
        disposable.dispose(); 
      this.done = true;
      this.downstream.onError(param1Throwable);
      this.worker.dispose();
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      long l = this.index + 1L;
      this.index = l;
      Disposable disposable = this.timer;
      if (disposable != null)
        disposable.dispose(); 
      ObservableDebounceTimed.DebounceEmitter<T> debounceEmitter = new ObservableDebounceTimed.DebounceEmitter<T>(param1T, l, this);
      this.timer = debounceEmitter;
      debounceEmitter.setResource(this.worker.schedule(debounceEmitter, this.timeout, this.unit));
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableDebounceTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */