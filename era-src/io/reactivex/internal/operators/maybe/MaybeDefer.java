package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import java.util.concurrent.Callable;

public final class MaybeDefer<T> extends Maybe<T> {
  final Callable<? extends MaybeSource<? extends T>> maybeSupplier;
  
  public MaybeDefer(Callable<? extends MaybeSource<? extends T>> paramCallable) {
    this.maybeSupplier = paramCallable;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    try {
      return;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptyDisposable.error(exception, paramMaybeObserver);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeDefer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */