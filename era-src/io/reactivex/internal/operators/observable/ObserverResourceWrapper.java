package io.reactivex.internal.operators.observable;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class ObserverResourceWrapper<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable {
  private static final long serialVersionUID = -8612022020200669122L;
  
  final Observer<? super T> downstream;
  
  final AtomicReference<Disposable> upstream = new AtomicReference<Disposable>();
  
  public ObserverResourceWrapper(Observer<? super T> paramObserver) {
    this.downstream = paramObserver;
  }
  
  public void dispose() {
    DisposableHelper.dispose(this.upstream);
    DisposableHelper.dispose(this);
  }
  
  public boolean isDisposed() {
    boolean bool;
    if (this.upstream.get() == DisposableHelper.DISPOSED) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void onComplete() {
    dispose();
    this.downstream.onComplete();
  }
  
  public void onError(Throwable paramThrowable) {
    dispose();
    this.downstream.onError(paramThrowable);
  }
  
  public void onNext(T paramT) {
    this.downstream.onNext(paramT);
  }
  
  public void onSubscribe(Disposable paramDisposable) {
    if (DisposableHelper.setOnce(this.upstream, paramDisposable))
      this.downstream.onSubscribe(this); 
  }
  
  public void setResource(Disposable paramDisposable) {
    DisposableHelper.set(this, paramDisposable);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObserverResourceWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */