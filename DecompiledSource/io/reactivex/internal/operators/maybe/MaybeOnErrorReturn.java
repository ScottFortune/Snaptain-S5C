package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;

public final class MaybeOnErrorReturn<T> extends AbstractMaybeWithUpstream<T, T> {
  final Function<? super Throwable, ? extends T> valueSupplier;
  
  public MaybeOnErrorReturn(MaybeSource<T> paramMaybeSource, Function<? super Throwable, ? extends T> paramFunction) {
    super(paramMaybeSource);
    this.valueSupplier = paramFunction;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    this.source.subscribe(new OnErrorReturnMaybeObserver<T>(paramMaybeObserver, this.valueSupplier));
  }
  
  static final class OnErrorReturnMaybeObserver<T> implements MaybeObserver<T>, Disposable {
    final MaybeObserver<? super T> downstream;
    
    Disposable upstream;
    
    final Function<? super Throwable, ? extends T> valueSupplier;
    
    OnErrorReturnMaybeObserver(MaybeObserver<? super T> param1MaybeObserver, Function<? super Throwable, ? extends T> param1Function) {
      this.downstream = param1MaybeObserver;
      this.valueSupplier = param1Function;
    }
    
    public void dispose() {
      this.upstream.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      try {
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        this.downstream.onError((Throwable)new CompositeException(new Throwable[] { param1Throwable, exception }));
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void onSuccess(T param1T) {
      this.downstream.onSuccess(param1T);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeOnErrorReturn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */