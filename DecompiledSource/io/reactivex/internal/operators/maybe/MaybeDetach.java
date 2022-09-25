package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

public final class MaybeDetach<T> extends AbstractMaybeWithUpstream<T, T> {
  public MaybeDetach(MaybeSource<T> paramMaybeSource) {
    super(paramMaybeSource);
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    this.source.subscribe(new DetachMaybeObserver<T>(paramMaybeObserver));
  }
  
  static final class DetachMaybeObserver<T> implements MaybeObserver<T>, Disposable {
    MaybeObserver<? super T> downstream;
    
    Disposable upstream;
    
    DetachMaybeObserver(MaybeObserver<? super T> param1MaybeObserver) {
      this.downstream = param1MaybeObserver;
    }
    
    public void dispose() {
      this.downstream = null;
      this.upstream.dispose();
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      MaybeObserver<? super T> maybeObserver = this.downstream;
      if (maybeObserver != null) {
        this.downstream = null;
        maybeObserver.onComplete();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      MaybeObserver<? super T> maybeObserver = this.downstream;
      if (maybeObserver != null) {
        this.downstream = null;
        maybeObserver.onError(param1Throwable);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void onSuccess(T param1T) {
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      MaybeObserver<? super T> maybeObserver = this.downstream;
      if (maybeObserver != null) {
        this.downstream = null;
        maybeObserver.onSuccess(param1T);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeDetach.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */