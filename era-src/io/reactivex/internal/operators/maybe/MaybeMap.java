package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;

public final class MaybeMap<T, R> extends AbstractMaybeWithUpstream<T, R> {
  final Function<? super T, ? extends R> mapper;
  
  public MaybeMap(MaybeSource<T> paramMaybeSource, Function<? super T, ? extends R> paramFunction) {
    super(paramMaybeSource);
    this.mapper = paramFunction;
  }
  
  protected void subscribeActual(MaybeObserver<? super R> paramMaybeObserver) {
    this.source.subscribe(new MapMaybeObserver<T, R>(paramMaybeObserver, this.mapper));
  }
  
  static final class MapMaybeObserver<T, R> implements MaybeObserver<T>, Disposable {
    final MaybeObserver<? super R> downstream;
    
    final Function<? super T, ? extends R> mapper;
    
    Disposable upstream;
    
    MapMaybeObserver(MaybeObserver<? super R> param1MaybeObserver, Function<? super T, ? extends R> param1Function) {
      this.downstream = param1MaybeObserver;
      this.mapper = param1Function;
    }
    
    public void dispose() {
      Disposable disposable = this.upstream;
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      disposable.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void onSuccess(T param1T) {
      try {
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        this.downstream.onError((Throwable)param1T);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */