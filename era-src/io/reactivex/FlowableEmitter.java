package io.reactivex;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;

public interface FlowableEmitter<T> extends Emitter<T> {
  boolean isCancelled();
  
  long requested();
  
  FlowableEmitter<T> serialize();
  
  void setCancellable(Cancellable paramCancellable);
  
  void setDisposable(Disposable paramDisposable);
  
  boolean tryOnError(Throwable paramThrowable);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/FlowableEmitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */