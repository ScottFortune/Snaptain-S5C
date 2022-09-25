package io.reactivex.disposables;

import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class SerialDisposable implements Disposable {
  final AtomicReference<Disposable> resource = new AtomicReference<Disposable>();
  
  public SerialDisposable() {}
  
  public SerialDisposable(Disposable paramDisposable) {}
  
  public void dispose() {
    DisposableHelper.dispose(this.resource);
  }
  
  public Disposable get() {
    Disposable disposable1 = this.resource.get();
    Disposable disposable2 = disposable1;
    if (disposable1 == DisposableHelper.DISPOSED)
      disposable2 = Disposables.disposed(); 
    return disposable2;
  }
  
  public boolean isDisposed() {
    return DisposableHelper.isDisposed(this.resource.get());
  }
  
  public boolean replace(Disposable paramDisposable) {
    return DisposableHelper.replace(this.resource, paramDisposable);
  }
  
  public boolean set(Disposable paramDisposable) {
    return DisposableHelper.set(this.resource, paramDisposable);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/disposables/SerialDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */