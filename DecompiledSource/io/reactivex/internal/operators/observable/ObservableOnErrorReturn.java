package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;

public final class ObservableOnErrorReturn<T> extends AbstractObservableWithUpstream<T, T> {
  final Function<? super Throwable, ? extends T> valueSupplier;
  
  public ObservableOnErrorReturn(ObservableSource<T> paramObservableSource, Function<? super Throwable, ? extends T> paramFunction) {
    super(paramObservableSource);
    this.valueSupplier = paramFunction;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new OnErrorReturnObserver<T>(paramObserver, this.valueSupplier));
  }
  
  static final class OnErrorReturnObserver<T> implements Observer<T>, Disposable {
    final Observer<? super T> downstream;
    
    Disposable upstream;
    
    final Function<? super Throwable, ? extends T> valueSupplier;
    
    OnErrorReturnObserver(Observer<? super T> param1Observer, Function<? super Throwable, ? extends T> param1Function) {
      this.downstream = param1Observer;
      this.valueSupplier = param1Function;
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
      try {
        Object object = this.valueSupplier.apply(param1Throwable);
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        this.downstream.onError((Throwable)new CompositeException(new Throwable[] { param1Throwable, exception }));
      } 
    }
    
    public void onNext(T param1T) {
      this.downstream.onNext(param1T);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableOnErrorReturn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */