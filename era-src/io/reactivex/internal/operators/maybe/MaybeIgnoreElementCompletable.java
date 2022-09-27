package io.reactivex.internal.operators.maybe;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.FuseToMaybe;
import io.reactivex.plugins.RxJavaPlugins;

public final class MaybeIgnoreElementCompletable<T> extends Completable implements FuseToMaybe<T> {
  final MaybeSource<T> source;
  
  public MaybeIgnoreElementCompletable(MaybeSource<T> paramMaybeSource) {
    this.source = paramMaybeSource;
  }
  
  public Maybe<T> fuseToMaybe() {
    return RxJavaPlugins.onAssembly(new MaybeIgnoreElement<T>(this.source));
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    this.source.subscribe(new IgnoreMaybeObserver(paramCompletableObserver));
  }
  
  static final class IgnoreMaybeObserver<T> implements MaybeObserver<T>, Disposable {
    final CompletableObserver downstream;
    
    Disposable upstream;
    
    IgnoreMaybeObserver(CompletableObserver param1CompletableObserver) {
      this.downstream = param1CompletableObserver;
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
      this.downstream.onComplete();
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
      this.downstream.onComplete();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeIgnoreElementCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */