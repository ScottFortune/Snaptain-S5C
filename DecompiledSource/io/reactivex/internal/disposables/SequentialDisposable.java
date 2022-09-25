package io.reactivex.internal.disposables;

import io.reactivex.disposables.Disposable;
import java.util.concurrent.atomic.AtomicReference;

public final class SequentialDisposable extends AtomicReference<Disposable> implements Disposable {
  private static final long serialVersionUID = -754898800686245608L;
  
  public SequentialDisposable() {}
  
  public SequentialDisposable(Disposable paramDisposable) {
    lazySet(paramDisposable);
  }
  
  public void dispose() {
    DisposableHelper.dispose(this);
  }
  
  public boolean isDisposed() {
    return DisposableHelper.isDisposed(get());
  }
  
  public boolean replace(Disposable paramDisposable) {
    return DisposableHelper.replace(this, paramDisposable);
  }
  
  public boolean update(Disposable paramDisposable) {
    return DisposableHelper.set(this, paramDisposable);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/disposables/SequentialDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */