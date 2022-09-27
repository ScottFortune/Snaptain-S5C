package io.reactivex.internal.disposables;

import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.ProtocolViolationException;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;

public enum DisposableHelper implements Disposable {
  DISPOSED;
  
  static {
    $VALUES = new DisposableHelper[] { DISPOSED };
  }
  
  public static boolean dispose(AtomicReference<Disposable> paramAtomicReference) {
    Disposable disposable = paramAtomicReference.get();
    DisposableHelper disposableHelper = DISPOSED;
    if (disposable != disposableHelper) {
      Disposable disposable1 = paramAtomicReference.getAndSet(disposableHelper);
      if (disposable1 != disposableHelper) {
        if (disposable1 != null)
          disposable1.dispose(); 
        return true;
      } 
    } 
    return false;
  }
  
  public static boolean isDisposed(Disposable paramDisposable) {
    boolean bool;
    if (paramDisposable == DISPOSED) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean replace(AtomicReference<Disposable> paramAtomicReference, Disposable paramDisposable) {
    while (true) {
      Disposable disposable = paramAtomicReference.get();
      if (disposable == DISPOSED) {
        if (paramDisposable != null)
          paramDisposable.dispose(); 
        return false;
      } 
      if (paramAtomicReference.compareAndSet(disposable, paramDisposable))
        return true; 
    } 
  }
  
  public static void reportDisposableSet() {
    RxJavaPlugins.onError((Throwable)new ProtocolViolationException("Disposable already set!"));
  }
  
  public static boolean set(AtomicReference<Disposable> paramAtomicReference, Disposable paramDisposable) {
    while (true) {
      Disposable disposable = paramAtomicReference.get();
      if (disposable == DISPOSED) {
        if (paramDisposable != null)
          paramDisposable.dispose(); 
        return false;
      } 
      if (paramAtomicReference.compareAndSet(disposable, paramDisposable)) {
        if (disposable != null)
          disposable.dispose(); 
        return true;
      } 
    } 
  }
  
  public static boolean setOnce(AtomicReference<Disposable> paramAtomicReference, Disposable paramDisposable) {
    ObjectHelper.requireNonNull(paramDisposable, "d is null");
    if (!paramAtomicReference.compareAndSet(null, paramDisposable)) {
      paramDisposable.dispose();
      if (paramAtomicReference.get() != DISPOSED)
        reportDisposableSet(); 
      return false;
    } 
    return true;
  }
  
  public static boolean trySet(AtomicReference<Disposable> paramAtomicReference, Disposable paramDisposable) {
    if (!paramAtomicReference.compareAndSet(null, paramDisposable)) {
      if (paramAtomicReference.get() == DISPOSED)
        paramDisposable.dispose(); 
      return false;
    } 
    return true;
  }
  
  public static boolean validate(Disposable paramDisposable1, Disposable paramDisposable2) {
    if (paramDisposable2 == null) {
      RxJavaPlugins.onError(new NullPointerException("next is null"));
      return false;
    } 
    if (paramDisposable1 != null) {
      paramDisposable2.dispose();
      reportDisposableSet();
      return false;
    } 
    return true;
  }
  
  public void dispose() {}
  
  public boolean isDisposed() {
    return true;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/disposables/DisposableHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */