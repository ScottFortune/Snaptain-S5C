package io.reactivex.internal.operators.mixed;

import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.Notification;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

public final class MaterializeSingleObserver<T> implements SingleObserver<T>, MaybeObserver<T>, CompletableObserver, Disposable {
  final SingleObserver<? super Notification<T>> downstream;
  
  Disposable upstream;
  
  public MaterializeSingleObserver(SingleObserver<? super Notification<T>> paramSingleObserver) {
    this.downstream = paramSingleObserver;
  }
  
  public void dispose() {
    this.upstream.dispose();
  }
  
  public boolean isDisposed() {
    return this.upstream.isDisposed();
  }
  
  public void onComplete() {
    this.downstream.onSuccess(Notification.createOnComplete());
  }
  
  public void onError(Throwable paramThrowable) {
    this.downstream.onSuccess(Notification.createOnError(paramThrowable));
  }
  
  public void onSubscribe(Disposable paramDisposable) {
    if (DisposableHelper.validate(this.upstream, paramDisposable)) {
      this.upstream = paramDisposable;
      this.downstream.onSubscribe(this);
    } 
  }
  
  public void onSuccess(T paramT) {
    this.downstream.onSuccess(Notification.createOnNext(paramT));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/mixed/MaterializeSingleObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */