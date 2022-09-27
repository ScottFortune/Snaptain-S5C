package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableOnErrorNext<T> extends AbstractObservableWithUpstream<T, T> {
  final boolean allowFatal;
  
  final Function<? super Throwable, ? extends ObservableSource<? extends T>> nextSupplier;
  
  public ObservableOnErrorNext(ObservableSource<T> paramObservableSource, Function<? super Throwable, ? extends ObservableSource<? extends T>> paramFunction, boolean paramBoolean) {
    super(paramObservableSource);
    this.nextSupplier = paramFunction;
    this.allowFatal = paramBoolean;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    OnErrorNextObserver<T> onErrorNextObserver = new OnErrorNextObserver<T>(paramObserver, this.nextSupplier, this.allowFatal);
    paramObserver.onSubscribe((Disposable)onErrorNextObserver.arbiter);
    this.source.subscribe(onErrorNextObserver);
  }
  
  static final class OnErrorNextObserver<T> implements Observer<T> {
    final boolean allowFatal;
    
    final SequentialDisposable arbiter;
    
    boolean done;
    
    final Observer<? super T> downstream;
    
    final Function<? super Throwable, ? extends ObservableSource<? extends T>> nextSupplier;
    
    boolean once;
    
    OnErrorNextObserver(Observer<? super T> param1Observer, Function<? super Throwable, ? extends ObservableSource<? extends T>> param1Function, boolean param1Boolean) {
      this.downstream = param1Observer;
      this.nextSupplier = param1Function;
      this.allowFatal = param1Boolean;
      this.arbiter = new SequentialDisposable();
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      this.once = true;
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.once) {
        if (this.done) {
          RxJavaPlugins.onError(param1Throwable);
          return;
        } 
        this.downstream.onError(param1Throwable);
        return;
      } 
      this.once = true;
      if (this.allowFatal && !(param1Throwable instanceof Exception)) {
        this.downstream.onError(param1Throwable);
        return;
      } 
      try {
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        this.downstream.onError((Throwable)new CompositeException(new Throwable[] { param1Throwable, exception }));
      } 
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      this.downstream.onNext(param1T);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.arbiter.replace(param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableOnErrorNext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */