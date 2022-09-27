package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.internal.disposables.EmptyDisposable;

public final class CompletableError extends Completable {
  final Throwable error;
  
  public CompletableError(Throwable paramThrowable) {
    this.error = paramThrowable;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    EmptyDisposable.error(this.error, paramCompletableObserver);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */