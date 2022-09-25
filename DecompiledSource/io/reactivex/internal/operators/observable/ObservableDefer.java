package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import java.util.concurrent.Callable;

public final class ObservableDefer<T> extends Observable<T> {
  final Callable<? extends ObservableSource<? extends T>> supplier;
  
  public ObservableDefer(Callable<? extends ObservableSource<? extends T>> paramCallable) {
    this.supplier = paramCallable;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    try {
      return;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptyDisposable.error(exception, paramObserver);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableDefer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */