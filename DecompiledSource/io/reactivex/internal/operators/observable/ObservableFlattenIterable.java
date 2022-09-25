package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableFlattenIterable<T, R> extends AbstractObservableWithUpstream<T, R> {
  final Function<? super T, ? extends Iterable<? extends R>> mapper;
  
  public ObservableFlattenIterable(ObservableSource<T> paramObservableSource, Function<? super T, ? extends Iterable<? extends R>> paramFunction) {
    super(paramObservableSource);
    this.mapper = paramFunction;
  }
  
  protected void subscribeActual(Observer<? super R> paramObserver) {
    this.source.subscribe(new FlattenIterableObserver<T, R>(paramObserver, this.mapper));
  }
  
  static final class FlattenIterableObserver<T, R> implements Observer<T>, Disposable {
    final Observer<? super R> downstream;
    
    final Function<? super T, ? extends Iterable<? extends R>> mapper;
    
    Disposable upstream;
    
    FlattenIterableObserver(Observer<? super R> param1Observer, Function<? super T, ? extends Iterable<? extends R>> param1Function) {
      this.downstream = param1Observer;
      this.mapper = param1Function;
    }
    
    public void dispose() {
      this.upstream.dispose();
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      if (this.upstream == DisposableHelper.DISPOSED)
        return; 
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.upstream == DisposableHelper.DISPOSED) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.upstream == DisposableHelper.DISPOSED)
        return; 
      try {
      
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
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableFlattenIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */