package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeTimeoutMaybe<T, U> extends AbstractMaybeWithUpstream<T, T> {
  final MaybeSource<? extends T> fallback;
  
  final MaybeSource<U> other;
  
  public MaybeTimeoutMaybe(MaybeSource<T> paramMaybeSource, MaybeSource<U> paramMaybeSource1, MaybeSource<? extends T> paramMaybeSource2) {
    super(paramMaybeSource);
    this.other = paramMaybeSource1;
    this.fallback = paramMaybeSource2;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    TimeoutMainMaybeObserver<T, Object> timeoutMainMaybeObserver = new TimeoutMainMaybeObserver<T, Object>(paramMaybeObserver, this.fallback);
    paramMaybeObserver.onSubscribe(timeoutMainMaybeObserver);
    this.other.subscribe(timeoutMainMaybeObserver.other);
    this.source.subscribe(timeoutMainMaybeObserver);
  }
  
  static final class TimeoutFallbackMaybeObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T> {
    private static final long serialVersionUID = 8663801314800248617L;
    
    final MaybeObserver<? super T> downstream;
    
    TimeoutFallbackMaybeObserver(MaybeObserver<? super T> param1MaybeObserver) {
      this.downstream = param1MaybeObserver;
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      this.downstream.onSuccess(param1T);
    }
  }
  
  static final class TimeoutMainMaybeObserver<T, U> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {
    private static final long serialVersionUID = -5955289211445418871L;
    
    final MaybeObserver<? super T> downstream;
    
    final MaybeSource<? extends T> fallback;
    
    final MaybeTimeoutMaybe.TimeoutOtherMaybeObserver<T, U> other;
    
    final MaybeTimeoutMaybe.TimeoutFallbackMaybeObserver<T> otherObserver;
    
    TimeoutMainMaybeObserver(MaybeObserver<? super T> param1MaybeObserver, MaybeSource<? extends T> param1MaybeSource) {
      this.downstream = param1MaybeObserver;
      this.other = new MaybeTimeoutMaybe.TimeoutOtherMaybeObserver<T, U>(this);
      this.fallback = param1MaybeSource;
      if (param1MaybeSource != null) {
        param1MaybeObserver = new MaybeTimeoutMaybe.TimeoutFallbackMaybeObserver<T>(param1MaybeObserver);
      } else {
        param1MaybeObserver = null;
      } 
      this.otherObserver = (MaybeTimeoutMaybe.TimeoutFallbackMaybeObserver)param1MaybeObserver;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
      DisposableHelper.dispose(this.other);
      MaybeTimeoutMaybe.TimeoutFallbackMaybeObserver<T> timeoutFallbackMaybeObserver = this.otherObserver;
      if (timeoutFallbackMaybeObserver != null)
        DisposableHelper.dispose(timeoutFallbackMaybeObserver); 
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
    
    public void otherComplete() {
      if (DisposableHelper.dispose(this)) {
        MaybeSource<? extends T> maybeSource = this.fallback;
        if (maybeSource == null) {
          this.downstream.onError(new TimeoutException());
        } else {
          maybeSource.subscribe(this.otherObserver);
        } 
      } 
    }
    
    public void otherError(Throwable param1Throwable) {
      if (DisposableHelper.dispose(this)) {
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
  }
  
  static final class TimeoutOtherMaybeObserver<T, U> extends AtomicReference<Disposable> implements MaybeObserver<Object> {
    private static final long serialVersionUID = 8663801314800248617L;
    
    final MaybeTimeoutMaybe.TimeoutMainMaybeObserver<T, U> parent;
    
    TimeoutOtherMaybeObserver(MaybeTimeoutMaybe.TimeoutMainMaybeObserver<T, U> param1TimeoutMainMaybeObserver) {
      this.parent = param1TimeoutMainMaybeObserver;
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeTimeoutMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */