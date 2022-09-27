package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
import java.util.NoSuchElementException;

public final class MaybeToSingle<T> extends Single<T> implements HasUpstreamMaybeSource<T> {
  final T defaultValue;
  
  final MaybeSource<T> source;
  
  public MaybeToSingle(MaybeSource<T> paramMaybeSource, T paramT) {
    this.source = paramMaybeSource;
    this.defaultValue = paramT;
  }
  
  public MaybeSource<T> source() {
    return this.source;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    this.source.subscribe(new ToSingleMaybeSubscriber<T>(paramSingleObserver, this.defaultValue));
  }
  
  static final class ToSingleMaybeSubscriber<T> implements MaybeObserver<T>, Disposable {
    final T defaultValue;
    
    final SingleObserver<? super T> downstream;
    
    Disposable upstream;
    
    ToSingleMaybeSubscriber(SingleObserver<? super T> param1SingleObserver, T param1T) {
      this.downstream = param1SingleObserver;
      this.defaultValue = param1T;
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
      T t = this.defaultValue;
      if (t != null) {
        this.downstream.onSuccess(t);
      } else {
        this.downstream.onError(new NoSuchElementException("The MaybeSource is empty"));
      } 
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
    
    public void onSuccess(T param1T) {
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      this.downstream.onSuccess(param1T);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeToSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */