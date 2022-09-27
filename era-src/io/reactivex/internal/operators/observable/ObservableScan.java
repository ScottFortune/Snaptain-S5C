package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableScan<T> extends AbstractObservableWithUpstream<T, T> {
  final BiFunction<T, T, T> accumulator;
  
  public ObservableScan(ObservableSource<T> paramObservableSource, BiFunction<T, T, T> paramBiFunction) {
    super(paramObservableSource);
    this.accumulator = paramBiFunction;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new ScanObserver<T>(paramObserver, this.accumulator));
  }
  
  static final class ScanObserver<T> implements Observer<T>, Disposable {
    final BiFunction<T, T, T> accumulator;
    
    boolean done;
    
    final Observer<? super T> downstream;
    
    Disposable upstream;
    
    T value;
    
    ScanObserver(Observer<? super T> param1Observer, BiFunction<T, T, T> param1BiFunction) {
      this.downstream = param1Observer;
      this.accumulator = param1BiFunction;
    }
    
    public void dispose() {
      this.upstream.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      Observer<? super T> observer = this.downstream;
      T t = this.value;
      if (t == null) {
        this.value = param1T;
        observer.onNext(param1T);
      } else {
        try {
          return;
        } finally {
          param1T = null;
          Exceptions.throwIfFatal((Throwable)param1T);
          this.upstream.dispose();
          onError((Throwable)param1T);
        } 
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableScan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */