package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.Callable;

public final class ObservableMapNotification<T, R> extends AbstractObservableWithUpstream<T, ObservableSource<? extends R>> {
  final Callable<? extends ObservableSource<? extends R>> onCompleteSupplier;
  
  final Function<? super Throwable, ? extends ObservableSource<? extends R>> onErrorMapper;
  
  final Function<? super T, ? extends ObservableSource<? extends R>> onNextMapper;
  
  public ObservableMapNotification(ObservableSource<T> paramObservableSource, Function<? super T, ? extends ObservableSource<? extends R>> paramFunction, Function<? super Throwable, ? extends ObservableSource<? extends R>> paramFunction1, Callable<? extends ObservableSource<? extends R>> paramCallable) {
    super(paramObservableSource);
    this.onNextMapper = paramFunction;
    this.onErrorMapper = paramFunction1;
    this.onCompleteSupplier = paramCallable;
  }
  
  public void subscribeActual(Observer<? super ObservableSource<? extends R>> paramObserver) {
    this.source.subscribe(new MapNotificationObserver<T, R>(paramObserver, this.onNextMapper, this.onErrorMapper, this.onCompleteSupplier));
  }
  
  static final class MapNotificationObserver<T, R> implements Observer<T>, Disposable {
    final Observer<? super ObservableSource<? extends R>> downstream;
    
    final Callable<? extends ObservableSource<? extends R>> onCompleteSupplier;
    
    final Function<? super Throwable, ? extends ObservableSource<? extends R>> onErrorMapper;
    
    final Function<? super T, ? extends ObservableSource<? extends R>> onNextMapper;
    
    Disposable upstream;
    
    MapNotificationObserver(Observer<? super ObservableSource<? extends R>> param1Observer, Function<? super T, ? extends ObservableSource<? extends R>> param1Function, Function<? super Throwable, ? extends ObservableSource<? extends R>> param1Function1, Callable<? extends ObservableSource<? extends R>> param1Callable) {
      this.downstream = param1Observer;
      this.onNextMapper = param1Function;
      this.onErrorMapper = param1Function1;
      this.onCompleteSupplier = param1Callable;
    }
    
    public void dispose() {
      this.upstream.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      try {
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        this.downstream.onError(exception);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      try {
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        this.downstream.onError((Throwable)new CompositeException(new Throwable[] { param1Throwable, exception }));
      } 
    }
    
    public void onNext(T param1T) {
      try {
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        this.downstream.onError((Throwable)param1T);
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableMapNotification.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */