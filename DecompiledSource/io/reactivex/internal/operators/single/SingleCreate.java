package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import io.reactivex.internal.disposables.CancellableDisposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleCreate<T> extends Single<T> {
  final SingleOnSubscribe<T> source;
  
  public SingleCreate(SingleOnSubscribe<T> paramSingleOnSubscribe) {
    this.source = paramSingleOnSubscribe;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    Emitter<T> emitter = new Emitter<T>(paramSingleObserver);
    paramSingleObserver.onSubscribe(emitter);
  }
  
  static final class Emitter<T> extends AtomicReference<Disposable> implements SingleEmitter<T>, Disposable {
    private static final long serialVersionUID = -2467358622224974244L;
    
    final SingleObserver<? super T> downstream;
    
    Emitter(SingleObserver<? super T> param1SingleObserver) {
      this.downstream = param1SingleObserver;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onError(Throwable param1Throwable) {
      if (!tryOnError(param1Throwable))
        RxJavaPlugins.onError(param1Throwable); 
    }
    
    public void onSuccess(T param1T) {
      if (get() != DisposableHelper.DISPOSED) {
        Disposable disposable = getAndSet((Disposable)DisposableHelper.DISPOSED);
        if (disposable != DisposableHelper.DISPOSED)
          if (param1T == null) {
            try {
              SingleObserver<? super T> singleObserver = this.downstream;
              NullPointerException nullPointerException = new NullPointerException();
              this("onSuccess called with null. Null values are generally not allowed in 2.x operators and sources.");
              singleObserver.onError(nullPointerException);
              if (disposable != null)
                disposable.dispose(); 
            } finally {
              if (disposable != null)
                disposable.dispose(); 
            } 
          } else {
            this.downstream.onSuccess(param1T);
            if (disposable != null)
              disposable.dispose(); 
          }  
      } 
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleCreate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */