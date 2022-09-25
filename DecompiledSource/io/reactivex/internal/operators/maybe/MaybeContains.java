package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

public final class MaybeContains<T> extends Single<Boolean> implements HasUpstreamMaybeSource<T> {
  final MaybeSource<T> source;
  
  final Object value;
  
  public MaybeContains(MaybeSource<T> paramMaybeSource, Object paramObject) {
    this.source = paramMaybeSource;
    this.value = paramObject;
  }
  
  public MaybeSource<T> source() {
    return this.source;
  }
  
  protected void subscribeActual(SingleObserver<? super Boolean> paramSingleObserver) {
    this.source.subscribe(new ContainsMaybeObserver(paramSingleObserver, this.value));
  }
  
  static final class ContainsMaybeObserver implements MaybeObserver<Object>, Disposable {
    final SingleObserver<? super Boolean> downstream;
    
    Disposable upstream;
    
    final Object value;
    
    ContainsMaybeObserver(SingleObserver<? super Boolean> param1SingleObserver, Object param1Object) {
      this.downstream = param1SingleObserver;
      this.value = param1Object;
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
      this.downstream.onSuccess(Boolean.valueOf(false));
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
      this.downstream.onSuccess(Boolean.valueOf(ObjectHelper.equals(param1Object, this.value)));
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeContains.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */