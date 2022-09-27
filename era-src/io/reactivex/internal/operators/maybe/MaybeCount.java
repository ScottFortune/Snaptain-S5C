package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

public final class MaybeCount<T> extends Single<Long> implements HasUpstreamMaybeSource<T> {
  final MaybeSource<T> source;
  
  public MaybeCount(MaybeSource<T> paramMaybeSource) {
    this.source = paramMaybeSource;
  }
  
  public MaybeSource<T> source() {
    return this.source;
  }
  
  protected void subscribeActual(SingleObserver<? super Long> paramSingleObserver) {
    this.source.subscribe(new CountMaybeObserver(paramSingleObserver));
  }
  
  static final class CountMaybeObserver implements MaybeObserver<Object>, Disposable {
    final SingleObserver<? super Long> downstream;
    
    Disposable upstream;
    
    CountMaybeObserver(SingleObserver<? super Long> param1SingleObserver) {
      this.downstream = param1SingleObserver;
    }
    
    public void dispose() {
      this.upstream.dispose();
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      this.downstream.onSuccess(Long.valueOf(0L));
    }
    
    public void onError(Throwable param1Throwable) {
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      this.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void onSuccess(Object param1Object) {
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      this.downstream.onSuccess(Long.valueOf(1L));
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeCount.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */