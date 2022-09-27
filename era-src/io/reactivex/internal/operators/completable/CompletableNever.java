package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.EmptyDisposable;

public final class CompletableNever extends Completable {
  public static final Completable INSTANCE = new CompletableNever();
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    paramCompletableObserver.onSubscribe((Disposable)EmptyDisposable.NEVER);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableNever.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */