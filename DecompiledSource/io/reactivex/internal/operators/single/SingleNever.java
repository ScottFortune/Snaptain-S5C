package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.EmptyDisposable;

public final class SingleNever extends Single<Object> {
  public static final Single<Object> INSTANCE = new SingleNever();
  
  protected void subscribeActual(SingleObserver<? super Object> paramSingleObserver) {
    paramSingleObserver.onSubscribe((Disposable)EmptyDisposable.NEVER);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleNever.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */