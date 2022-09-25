package io.reactivex.internal.operators.single;

import io.reactivex.Notification;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.internal.operators.mixed.MaterializeSingleObserver;

public final class SingleMaterialize<T> extends Single<Notification<T>> {
  final Single<T> source;
  
  public SingleMaterialize(Single<T> paramSingle) {
    this.source = paramSingle;
  }
  
  protected void subscribeActual(SingleObserver<? super Notification<T>> paramSingleObserver) {
    this.source.subscribe((SingleObserver)new MaterializeSingleObserver(paramSingleObserver));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleMaterialize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */