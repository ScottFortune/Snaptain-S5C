package io.reactivex.observers;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.EndConsumerHelper;
import java.util.concurrent.atomic.AtomicReference;

public abstract class DisposableMaybeObserver<T> implements MaybeObserver<T>, Disposable {
  final AtomicReference<Disposable> upstream = new AtomicReference<Disposable>();
  
  public final void dispose() {
    DisposableHelper.dispose(this.upstream);
  }
  
  public final boolean isDisposed() {
    boolean bool;
    if (this.upstream.get() == DisposableHelper.DISPOSED) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected void onStart() {}
  
  public final void onSubscribe(Disposable paramDisposable) {
    if (EndConsumerHelper.setOnce(this.upstream, paramDisposable, getClass()))
      onStart(); 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/observers/DisposableMaybeObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */