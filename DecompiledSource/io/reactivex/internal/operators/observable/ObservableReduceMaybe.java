package io.reactivex.internal.operators.observable;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableReduceMaybe<T> extends Maybe<T> {
  final BiFunction<T, T, T> reducer;
  
  final ObservableSource<T> source;
  
  public ObservableReduceMaybe(ObservableSource<T> paramObservableSource, BiFunction<T, T, T> paramBiFunction) {
    this.source = paramObservableSource;
    this.reducer = paramBiFunction;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    this.source.subscribe(new ReduceObserver<T>(paramMaybeObserver, this.reducer));
  }
  
  static final class ReduceObserver<T> implements Observer<T>, Disposable {
    boolean done;
    
    final MaybeObserver<? super T> downstream;
    
    final BiFunction<T, T, T> reducer;
    
    Disposable upstream;
    
    T value;
    
    ReduceObserver(MaybeObserver<? super T> param1MaybeObserver, BiFunction<T, T, T> param1BiFunction) {
      this.downstream = param1MaybeObserver;
      this.reducer = param1BiFunction;
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
      T t = this.value;
      this.value = null;
      if (t != null) {
        this.downstream.onSuccess(t);
      } else {
        this.downstream.onComplete();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.value = null;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (!this.done) {
        T t = this.value;
        if (t == null) {
          this.value = param1T;
        } else {
          try {
            this.value = (T)ObjectHelper.requireNonNull(this.reducer.apply(t, param1T), "The reducer returned a null value");
          } finally {
            param1T = null;
            Exceptions.throwIfFatal((Throwable)param1T);
            this.upstream.dispose();
          } 
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableReduceMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */