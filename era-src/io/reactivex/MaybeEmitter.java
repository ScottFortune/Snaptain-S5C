package io.reactivex;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;

public interface MaybeEmitter<T> {
  boolean isDisposed();
  
  void onComplete();
  
  void onError(Throwable paramThrowable);
  
  void onSuccess(T paramT);
  
  void setCancellable(Cancellable paramCancellable);
  
  void setDisposable(Disposable paramDisposable);
  
  boolean tryOnError(Throwable paramThrowable);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/MaybeEmitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */