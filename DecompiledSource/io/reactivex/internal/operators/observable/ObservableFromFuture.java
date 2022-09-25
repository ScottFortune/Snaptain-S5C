package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.observers.DeferredScalarDisposable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public final class ObservableFromFuture<T> extends Observable<T> {
  final Future<? extends T> future;
  
  final long timeout;
  
  final TimeUnit unit;
  
  public ObservableFromFuture(Future<? extends T> paramFuture, long paramLong, TimeUnit paramTimeUnit) {
    this.future = paramFuture;
    this.timeout = paramLong;
    this.unit = paramTimeUnit;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    DeferredScalarDisposable deferredScalarDisposable = new DeferredScalarDisposable(paramObserver);
    paramObserver.onSubscribe((Disposable)deferredScalarDisposable);
    if (!deferredScalarDisposable.isDisposed())
      try {
        T t;
        if (this.unit != null) {
          T t1 = this.future.get(this.timeout, this.unit);
        } else {
          t = this.future.get();
        } 
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
      }  
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableFromFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */