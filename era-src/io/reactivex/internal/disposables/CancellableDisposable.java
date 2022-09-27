package io.reactivex.internal.disposables;

import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Cancellable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;

public final class CancellableDisposable extends AtomicReference<Cancellable> implements Disposable {
  private static final long serialVersionUID = 5718521705281392066L;
  
  public CancellableDisposable(Cancellable paramCancellable) {
    super(paramCancellable);
  }
  
  public void dispose() {
    if (get() != null) {
      Cancellable cancellable = getAndSet(null);
      if (cancellable != null)
        try {
          cancellable.cancel();
        } catch (Exception exception) {
          Exceptions.throwIfFatal(exception);
          RxJavaPlugins.onError(exception);
        }  
    } 
  }
  
  public boolean isDisposed() {
    boolean bool;
    if (get() == null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/disposables/CancellableDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */