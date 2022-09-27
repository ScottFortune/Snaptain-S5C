package io.reactivex.internal.operators.observable;

import io.reactivex.Notification;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

public final class ObservableMaterialize<T> extends AbstractObservableWithUpstream<T, Notification<T>> {
  public ObservableMaterialize(ObservableSource<T> paramObservableSource) {
    super(paramObservableSource);
  }
  
  public void subscribeActual(Observer<? super Notification<T>> paramObserver) {
    this.source.subscribe(new MaterializeObserver<T>(paramObserver));
  }
  
  static final class MaterializeObserver<T> implements Observer<T>, Disposable {
    final Observer<? super Notification<T>> downstream;
    
    Disposable upstream;
    
    MaterializeObserver(Observer<? super Notification<T>> param1Observer) {
      this.downstream = param1Observer;
    }
    
    public void dispose() {
      this.upstream.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      Notification notification = Notification.createOnComplete();
      this.downstream.onNext(notification);
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      Notification notification = Notification.createOnError(param1Throwable);
      this.downstream.onNext(notification);
      this.downstream.onComplete();
    }
    
    public void onNext(T param1T) {
      this.downstream.onNext(Notification.createOnNext(param1T));
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableMaterialize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */