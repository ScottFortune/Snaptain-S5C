package io.reactivex.observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.ListCompositeDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.util.EndConsumerHelper;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ResourceObserver<T> implements Observer<T>, Disposable {
  private final ListCompositeDisposable resources = new ListCompositeDisposable();
  
  private final AtomicReference<Disposable> upstream = new AtomicReference<Disposable>();
  
  public final void add(Disposable paramDisposable) {
    ObjectHelper.requireNonNull(paramDisposable, "resource is null");
    this.resources.add(paramDisposable);
  }
  
  public final void dispose() {
    if (DisposableHelper.dispose(this.upstream))
      this.resources.dispose(); 
  }
  
  public final boolean isDisposed() {
    return DisposableHelper.isDisposed(this.upstream.get());
  }
  
  protected void onStart() {}
  
  public final void onSubscribe(Disposable paramDisposable) {
    if (EndConsumerHelper.setOnce(this.upstream, paramDisposable, getClass()))
      onStart(); 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/observers/ResourceObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */