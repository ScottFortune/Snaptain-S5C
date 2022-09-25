package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeTakeUntilMaybe<T, U> extends AbstractMaybeWithUpstream<T, T> {
  final MaybeSource<U> other;
  
  public MaybeTakeUntilMaybe(MaybeSource<T> paramMaybeSource, MaybeSource<U> paramMaybeSource1) {
    super(paramMaybeSource);
    this.other = paramMaybeSource1;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    TakeUntilMainMaybeObserver<T, Object> takeUntilMainMaybeObserver = new TakeUntilMainMaybeObserver<T, Object>(paramMaybeObserver);
    paramMaybeObserver.onSubscribe(takeUntilMainMaybeObserver);
    this.other.subscribe(takeUntilMainMaybeObserver.other);
    this.source.subscribe(takeUntilMainMaybeObserver);
  }
  
  static final class TakeUntilMainMaybeObserver<T, U> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {
    private static final long serialVersionUID = -2187421758664251153L;
    
    final MaybeObserver<? super T> downstream;
    
    final TakeUntilOtherMaybeObserver<U> other;
    
    TakeUntilMainMaybeObserver(MaybeObserver<? super T> param1MaybeObserver) {
      this.downstream = param1MaybeObserver;
      this.other = new TakeUntilOtherMaybeObserver<U>(this);
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
      DisposableHelper.dispose(this.other);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      DisposableHelper.dispose(this.other);
      if (getAndSet((Disposable)DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED)
        this.downstream.onComplete(); 
    }
    
    public void onError(Throwable param1Throwable) {
      DisposableHelper.dispose(this.other);
      if (getAndSet((Disposable)DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      DisposableHelper.dispose(this.other);
      if (getAndSet((Disposable)DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED)
        this.downstream.onSuccess(param1T); 
    }
    
    void otherComplete() {
      if (DisposableHelper.dispose(this))
        this.downstream.onComplete(); 
    }
    
    void otherError(Throwable param1Throwable) {
      if (DisposableHelper.dispose(this)) {
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    static final class TakeUntilOtherMaybeObserver<U> extends AtomicReference<Disposable> implements MaybeObserver<U> {
      private static final long serialVersionUID = -1266041316834525931L;
      
      final MaybeTakeUntilMaybe.TakeUntilMainMaybeObserver<?, U> parent;
      
      TakeUntilOtherMaybeObserver(MaybeTakeUntilMaybe.TakeUntilMainMaybeObserver<?, U> param2TakeUntilMainMaybeObserver) {
        this.parent = param2TakeUntilMainMaybeObserver;
      }
      
      public void onComplete() {
        this.parent.otherComplete();
      }
      
      public void onError(Throwable param2Throwable) {
        this.parent.otherError(param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.setOnce(this, param2Disposable);
      }
      
      public void onSuccess(Object param2Object) {
        this.parent.otherComplete();
      }
    }
  }
  
  static final class TakeUntilOtherMaybeObserver<U> extends AtomicReference<Disposable> implements MaybeObserver<U> {
    private static final long serialVersionUID = -1266041316834525931L;
    
    final MaybeTakeUntilMaybe.TakeUntilMainMaybeObserver<?, U> parent;
    
    TakeUntilOtherMaybeObserver(MaybeTakeUntilMaybe.TakeUntilMainMaybeObserver<?, U> param1TakeUntilMainMaybeObserver) {
      this.parent = param1TakeUntilMainMaybeObserver;
    }
    
    public void onComplete() {
      this.parent.otherComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.otherError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
    
    public void onSuccess(Object param1Object) {
      this.parent.otherComplete();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeTakeUntilMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */