package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Notification;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.internal.operators.mixed.MaterializeSingleObserver;

public final class CompletableMaterialize<T> extends Single<Notification<T>> {
  final Completable source;
  
  public CompletableMaterialize(Completable paramCompletable) {
    this.source = paramCompletable;
  }
  
  protected void subscribeActual(SingleObserver<? super Notification<T>> paramSingleObserver) {
    this.source.subscribe((CompletableObserver)new MaterializeSingleObserver(paramSingleObserver));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableMaterialize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */