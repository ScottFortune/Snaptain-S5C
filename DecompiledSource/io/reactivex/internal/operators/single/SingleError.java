package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.Callable;

public final class SingleError<T> extends Single<T> {
  final Callable<? extends Throwable> errorSupplier;
  
  public SingleError(Callable<? extends Throwable> paramCallable) {
    this.errorSupplier = paramCallable;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    Throwable throwable;
    try {
      throwable = (Throwable)ObjectHelper.requireNonNull(this.errorSupplier.call(), "Callable returned null throwable. Null values are generally not allowed in 2.x operators and sources.");
    } finally {
      throwable = null;
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */