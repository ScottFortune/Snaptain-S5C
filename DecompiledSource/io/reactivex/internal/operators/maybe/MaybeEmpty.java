package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.fuseable.ScalarCallable;

public final class MaybeEmpty extends Maybe<Object> implements ScalarCallable<Object> {
  public static final MaybeEmpty INSTANCE = new MaybeEmpty();
  
  public Object call() {
    return null;
  }
  
  protected void subscribeActual(MaybeObserver<? super Object> paramMaybeObserver) {
    EmptyDisposable.complete(paramMaybeObserver);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeEmpty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */