package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import io.reactivex.internal.disposables.CancellableDisposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;

public final class CompletableCreate extends Completable {
  final CompletableOnSubscribe source;
  
  public CompletableCreate(CompletableOnSubscribe paramCompletableOnSubscribe) {
    this.source = paramCompletableOnSubscribe;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    Emitter emitter = new Emitter(paramCompletableObserver);
    paramCompletableObserver.onSubscribe(emitter);
  }
  
  static final class Emitter extends AtomicReference<Disposable> implements CompletableEmitter, Disposable {
    private static final long serialVersionUID = -2467358622224974244L;
    
    final CompletableObserver downstream;
    
    Emitter(CompletableObserver param1CompletableObserver) {
      this.downstream = param1CompletableObserver;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      if (get() != DisposableHelper.DISPOSED) {
        Disposable disposable = getAndSet((Disposable)DisposableHelper.DISPOSED);
        if (disposable != DisposableHelper.DISPOSED)
          try {
            this.downstream.onComplete();
          } finally {
            if (disposable != null)
              disposable.dispose(); 
          }  
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (!tryOnError(param1Throwable))
        RxJavaPlugins.onError(param1Throwable); 
    }
    
    public void setCancellable(Cancellable param1Cancellable) {
      setDisposable((Disposable)new CancellableDisposable(param1Cancellable));
    }
    
    public void setDisposable(Disposable param1Disposable) {
      DisposableHelper.set(this, param1Disposable);
    }
    
    public String toString() {
      return String.format("%s{%s}", new Object[] { getClass().getSimpleName(), super.toString() });
    }
    
    public boolean tryOnError(Throwable param1Throwable) {
      Throwable throwable = param1Throwable;
      if (param1Throwable == null)
        throwable = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources."); 
      if (get() != DisposableHelper.DISPOSED) {
        Disposable disposable = getAndSet((Disposable)DisposableHelper.DISPOSED);
        if (disposable != DisposableHelper.DISPOSED)
          try {
            this.downstream.onError(throwable);
            return true;
          } finally {
            if (disposable != null)
              disposable.dispose(); 
          }  
      } 
      return false;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableCreate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */