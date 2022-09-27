package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;

public final class ObservableScanSeed<T, R> extends AbstractObservableWithUpstream<T, R> {
  final BiFunction<R, ? super T, R> accumulator;
  
  final Callable<R> seedSupplier;
  
  public ObservableScanSeed(ObservableSource<T> paramObservableSource, Callable<R> paramCallable, BiFunction<R, ? super T, R> paramBiFunction) {
    super(paramObservableSource);
    this.accumulator = paramBiFunction;
    this.seedSupplier = paramCallable;
  }
  
  public void subscribeActual(Observer<? super R> paramObserver) {
    try {
      return;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptyDisposable.error(exception, paramObserver);
    } 
  }
  
  static final class ScanSeedObserver<T, R> implements Observer<T>, Disposable {
    final BiFunction<R, ? super T, R> accumulator;
    
    boolean done;
    
    final Observer<? super R> downstream;
    
    Disposable upstream;
    
    R value;
    
    ScanSeedObserver(Observer<? super R> param1Observer, BiFunction<R, ? super T, R> param1BiFunction, R param1R) {
      this.downstream = param1Observer;
      this.accumulator = param1BiFunction;
      this.value = param1R;
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
      R r = this.value;
      try {
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        this.upstream.dispose();
        onError((Throwable)param1T);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
        this.downstream.onNext(this.value);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableScanSeed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */