package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.Callable;

public final class CompletableErrorSupplier extends Completable {
  final Callable<? extends Throwable> errorSupplier;
  
  public CompletableErrorSupplier(Callable<? extends Throwable> paramCallable) {
    this.errorSupplier = paramCallable;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    Throwable throwable;
    try {
      throwable = (Throwable)ObjectHelper.requireNonNull(this.errorSupplier.call(), "The error returned is null");
    } finally {
      throwable = null;
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableErrorSupplier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */