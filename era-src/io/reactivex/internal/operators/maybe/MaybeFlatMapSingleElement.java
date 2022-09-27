package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeFlatMapSingleElement<T, R> extends Maybe<R> {
  final Function<? super T, ? extends SingleSource<? extends R>> mapper;
  
  final MaybeSource<T> source;
  
  public MaybeFlatMapSingleElement(MaybeSource<T> paramMaybeSource, Function<? super T, ? extends SingleSource<? extends R>> paramFunction) {
    this.source = paramMaybeSource;
    this.mapper = paramFunction;
  }
  
  protected void subscribeActual(MaybeObserver<? super R> paramMaybeObserver) {
    this.source.subscribe(new FlatMapMaybeObserver<T, R>(paramMaybeObserver, this.mapper));
  }
  
  static final class FlatMapMaybeObserver<T, R> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {
    private static final long serialVersionUID = 4827726964688405508L;
    
    final MaybeObserver<? super R> downstream;
    
    final Function<? super T, ? extends SingleSource<? extends R>> mapper;
    
    FlatMapMaybeObserver(MaybeObserver<? super R> param1MaybeObserver, Function<? super T, ? extends SingleSource<? extends R>> param1Function) {
      this.downstream = param1MaybeObserver;
      this.mapper = param1Function;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.setOnce(this, param1Disposable))
        this.downstream.onSubscribe(this); 
    }
    
    public void onSuccess(T param1T) {
      try {
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        onError((Throwable)param1T);
      } 
    }
  }
  
  static final class FlatMapSingleObserver<R> implements SingleObserver<R> {
    final MaybeObserver<? super R> downstream;
    
    final AtomicReference<Disposable> parent;
    
    FlatMapSingleObserver(AtomicReference<Disposable> param1AtomicReference, MaybeObserver<? super R> param1MaybeObserver) {
      this.parent = param1AtomicReference;
      this.downstream = param1MaybeObserver;
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.replace(this.parent, param1Disposable);
    }
    
    public void onSuccess(R param1R) {
      this.downstream.onSuccess(param1R);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeFlatMapSingleElement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */