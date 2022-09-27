package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.exceptions.Exceptions;
import java.util.concurrent.Callable;

public final class MaybeFromRunnable<T> extends Maybe<T> implements Callable<T> {
  final Runnable runnable;
  
  public MaybeFromRunnable(Runnable paramRunnable) {
    this.runnable = paramRunnable;
  }
  
  public T call() throws Exception {
    this.runnable.run();
    return null;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    Disposable disposable = Disposables.empty();
    paramMaybeObserver.onSubscribe(disposable);
    if (!disposable.isDisposed())
      try {
      
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
      }  
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeFromRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */