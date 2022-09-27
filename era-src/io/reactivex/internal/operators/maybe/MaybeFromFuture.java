package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.exceptions.Exceptions;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public final class MaybeFromFuture<T> extends Maybe<T> {
  final Future<? extends T> future;
  
  final long timeout;
  
  final TimeUnit unit;
  
  public MaybeFromFuture(Future<? extends T> paramFuture, long paramLong, TimeUnit paramTimeUnit) {
    this.future = paramFuture;
    this.timeout = paramLong;
    this.unit = paramTimeUnit;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    Disposable disposable = Disposables.empty();
    paramMaybeObserver.onSubscribe(disposable);
    if (!disposable.isDisposed())
      try {
      
      } finally {
        Exception exception = null;
        Throwable throwable = exception;
        if (exception instanceof java.util.concurrent.ExecutionException)
          throwable = exception.getCause(); 
        Exceptions.throwIfFatal(throwable);
      }  
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeFromFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */