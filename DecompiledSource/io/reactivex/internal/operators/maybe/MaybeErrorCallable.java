package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposables;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.Callable;

public final class MaybeErrorCallable<T> extends Maybe<T> {
  final Callable<? extends Throwable> errorSupplier;
  
  public MaybeErrorCallable(Callable<? extends Throwable> paramCallable) {
    this.errorSupplier = paramCallable;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    Throwable throwable;
    paramMaybeObserver.onSubscribe(Disposables.disposed());
    try {
      throwable = (Throwable)ObjectHelper.requireNonNull(this.errorSupplier.call(), "Callable returned null throwable. Null values are generally not allowed in 2.x operators and sources.");
    } finally {
      throwable = null;
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeErrorCallable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */