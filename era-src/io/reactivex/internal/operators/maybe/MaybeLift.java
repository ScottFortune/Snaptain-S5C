package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeOperator;
import io.reactivex.MaybeSource;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;

public final class MaybeLift<T, R> extends AbstractMaybeWithUpstream<T, R> {
  final MaybeOperator<? extends R, ? super T> operator;
  
  public MaybeLift(MaybeSource<T> paramMaybeSource, MaybeOperator<? extends R, ? super T> paramMaybeOperator) {
    super(paramMaybeSource);
    this.operator = paramMaybeOperator;
  }
  
  protected void subscribeActual(MaybeObserver<? super R> paramMaybeObserver) {
    try {
      return;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptyDisposable.error(exception, paramMaybeObserver);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeLift.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */