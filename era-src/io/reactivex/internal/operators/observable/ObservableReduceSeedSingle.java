package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableReduceSeedSingle<T, R> extends Single<R> {
  final BiFunction<R, ? super T, R> reducer;
  
  final R seed;
  
  final ObservableSource<T> source;
  
  public ObservableReduceSeedSingle(ObservableSource<T> paramObservableSource, R paramR, BiFunction<R, ? super T, R> paramBiFunction) {
    this.source = paramObservableSource;
    this.seed = paramR;
    this.reducer = paramBiFunction;
  }
  
  protected void subscribeActual(SingleObserver<? super R> paramSingleObserver) {
    this.source.subscribe(new ReduceSeedObserver<T, R>(paramSingleObserver, this.reducer, this.seed));
  }
  
  static final class ReduceSeedObserver<T, R> implements Observer<T>, Disposable {
    final SingleObserver<? super R> downstream;
    
    final BiFunction<R, ? super T, R> reducer;
    
    Disposable upstream;
    
    R value;
    
    ReduceSeedObserver(SingleObserver<? super R> param1SingleObserver, BiFunction<R, ? super T, R> param1BiFunction, R param1R) {
      this.downstream = param1SingleObserver;
      this.value = param1R;
      this.reducer = param1BiFunction;
    }
    
    public void dispose() {
      this.upstream.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      R r = this.value;
      if (r != null) {
        this.value = null;
        this.downstream.onSuccess(r);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.value != null) {
        this.value = null;
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      R r = this.value;
      if (r != null)
        try {
          this.value = (R)ObjectHelper.requireNonNull(this.reducer.apply(r, param1T), "The reducer returned a null value");
        } finally {
          param1T = null;
          Exceptions.throwIfFatal((Throwable)param1T);
          this.upstream.dispose();
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableReduceSeedSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */