package io.reactivex.observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.EndConsumerHelper;

public abstract class DefaultObserver<T> implements Observer<T> {
  private Disposable upstream;
  
  protected final void cancel() {
    Disposable disposable = this.upstream;
    this.upstream = (Disposable)DisposableHelper.DISPOSED;
    disposable.dispose();
  }
  
  protected void onStart() {}
  
  public final void onSubscribe(Disposable paramDisposable) {
    if (EndConsumerHelper.validate(this.upstream, paramDisposable, getClass())) {
      this.upstream = paramDisposable;
      onStart();
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/observers/DefaultObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */