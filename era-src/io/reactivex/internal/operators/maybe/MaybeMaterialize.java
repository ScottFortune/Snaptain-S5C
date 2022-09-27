package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Notification;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.internal.operators.mixed.MaterializeSingleObserver;

public final class MaybeMaterialize<T> extends Single<Notification<T>> {
  final Maybe<T> source;
  
  public MaybeMaterialize(Maybe<T> paramMaybe) {
    this.source = paramMaybe;
  }
  
  protected void subscribeActual(SingleObserver<? super Notification<T>> paramSingleObserver) {
    this.source.subscribe((MaybeObserver)new MaterializeSingleObserver(paramSingleObserver));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeMaterialize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */