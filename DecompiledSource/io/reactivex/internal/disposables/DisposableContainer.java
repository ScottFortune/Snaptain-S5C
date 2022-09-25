package io.reactivex.internal.disposables;

import io.reactivex.disposables.Disposable;

public interface DisposableContainer {
  boolean add(Disposable paramDisposable);
  
  boolean delete(Disposable paramDisposable);
  
  boolean remove(Disposable paramDisposable);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/disposables/DisposableContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */