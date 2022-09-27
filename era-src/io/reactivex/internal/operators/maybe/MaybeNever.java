package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.EmptyDisposable;

public final class MaybeNever extends Maybe<Object> {
  public static final MaybeNever INSTANCE = new MaybeNever();
  
  protected void subscribeActual(MaybeObserver<? super Object> paramMaybeObserver) {
    paramMaybeObserver.onSubscribe((Disposable)EmptyDisposable.NEVER);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeNever.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */