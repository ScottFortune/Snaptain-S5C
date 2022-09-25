package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import java.util.concurrent.Callable;

public final class CompletableDefer extends Completable {
  final Callable<? extends CompletableSource> completableSupplier;
  
  public CompletableDefer(Callable<? extends CompletableSource> paramCallable) {
    this.completableSupplier = paramCallable;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    try {
      return;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptyDisposable.error(exception, paramCompletableObserver);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableDefer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */