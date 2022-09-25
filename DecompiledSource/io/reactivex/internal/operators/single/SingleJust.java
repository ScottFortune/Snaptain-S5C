package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposables;

public final class SingleJust<T> extends Single<T> {
  final T value;
  
  public SingleJust(T paramT) {
    this.value = paramT;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    paramSingleObserver.onSubscribe(Disposables.disposed());
    paramSingleObserver.onSuccess(this.value);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleJust.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */