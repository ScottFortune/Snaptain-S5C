package io.reactivex.disposables;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

final class FutureDisposable extends AtomicReference<Future<?>> implements Disposable {
  private static final long serialVersionUID = 6545242830671168775L;
  
  private final boolean allowInterrupt;
  
  FutureDisposable(Future<?> paramFuture, boolean paramBoolean) {
    super(paramFuture);
    this.allowInterrupt = paramBoolean;
  }
  
  public void dispose() {
    Future<?> future = getAndSet(null);
    if (future != null)
      future.cancel(this.allowInterrupt); 
  }
  
  public boolean isDisposed() {
    Future<?> future = get();
    return (future == null || future.isDone());
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/disposables/FutureDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */