package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;

public final class SingleFromCallable<T> extends Single<T> {
  final Callable<? extends T> callable;
  
  public SingleFromCallable(Callable<? extends T> paramCallable) {
    this.callable = paramCallable;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    Disposable disposable = Disposables.empty();
    paramSingleObserver.onSubscribe(disposable);
    if (disposable.isDisposed())
      return; 
    try {
      return;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      if (!disposable.isDisposed()) {
        paramSingleObserver.onError(exception);
      } else {
        RxJavaPlugins.onError(exception);
      } 
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleFromCallable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */