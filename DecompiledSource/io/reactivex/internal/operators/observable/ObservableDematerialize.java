package io.reactivex.internal.operators.observable;

import io.reactivex.Notification;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableDematerialize<T, R> extends AbstractObservableWithUpstream<T, R> {
  final Function<? super T, ? extends Notification<R>> selector;
  
  public ObservableDematerialize(ObservableSource<T> paramObservableSource, Function<? super T, ? extends Notification<R>> paramFunction) {
    super(paramObservableSource);
    this.selector = paramFunction;
  }
  
  public void subscribeActual(Observer<? super R> paramObserver) {
    this.source.subscribe(new DematerializeObserver<T, R>(paramObserver, this.selector));
  }
  
  static final class DematerializeObserver<T, R> implements Observer<T>, Disposable {
    boolean done;
    
    final Observer<? super R> downstream;
    
    final Function<? super T, ? extends Notification<R>> selector;
    
    Disposable upstream;
    
    DematerializeObserver(Observer<? super R> param1Observer, Function<? super T, ? extends Notification<R>> param1Function) {
      this.downstream = param1Observer;
      this.selector = param1Function;
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
      Notification notification;
      if (this.done) {
        if (param1T instanceof Notification) {
          notification = (Notification)param1T;
          if (notification.isOnError())
            RxJavaPlugins.onError(notification.getError()); 
        } 
        return;
      } 
      try {
        return;
      } finally {
        notification = null;
        Exceptions.throwIfFatal((Throwable)notification);
        this.upstream.dispose();
        onError((Throwable)notification);
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableDematerialize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */