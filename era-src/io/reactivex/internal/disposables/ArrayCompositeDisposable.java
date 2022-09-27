package io.reactivex.internal.disposables;

import io.reactivex.disposables.Disposable;
import java.util.concurrent.atomic.AtomicReferenceArray;

public final class ArrayCompositeDisposable extends AtomicReferenceArray<Disposable> implements Disposable {
  private static final long serialVersionUID = 2746389416410565408L;
  
  public ArrayCompositeDisposable(int paramInt) {
    super(paramInt);
  }
  
  public void dispose() {
    byte b = 0;
    if (get(0) != DisposableHelper.DISPOSED) {
      int i = length();
      while (b < i) {
        if (get(b) != DisposableHelper.DISPOSED) {
          Disposable disposable = getAndSet(b, DisposableHelper.DISPOSED);
          if (disposable != DisposableHelper.DISPOSED && disposable != null)
            disposable.dispose(); 
        } 
        b++;
      } 
    } 
  }
  
  public boolean isDisposed() {
    boolean bool = false;
    if (get(0) == DisposableHelper.DISPOSED)
      bool = true; 
    return bool;
  }
  
  public Disposable replaceResource(int paramInt, Disposable paramDisposable) {
    while (true) {
      Disposable disposable = get(paramInt);
      if (disposable == DisposableHelper.DISPOSED) {
        paramDisposable.dispose();
        return null;
      } 
      if (compareAndSet(paramInt, disposable, paramDisposable))
        return disposable; 
    } 
  }
  
  public boolean setResource(int paramInt, Disposable paramDisposable) {
    while (true) {
      Disposable disposable = get(paramInt);
      if (disposable == DisposableHelper.DISPOSED) {
        paramDisposable.dispose();
        return false;
      } 
      if (compareAndSet(paramInt, disposable, paramDisposable)) {
        if (disposable != null)
          disposable.dispose(); 
        return true;
      } 
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/disposables/ArrayCompositeDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */