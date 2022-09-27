package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeFlatMapSingle<T, R> extends Single<R> {
  final Function<? super T, ? extends SingleSource<? extends R>> mapper;
  
  final MaybeSource<T> source;
  
  public MaybeFlatMapSingle(MaybeSource<T> paramMaybeSource, Function<? super T, ? extends SingleSource<? extends R>> paramFunction) {
    this.source = paramMaybeSource;
    this.mapper = paramFunction;
  }
  
  protected void subscribeActual(SingleObserver<? super R> paramSingleObserver) {
    this.source.subscribe(new FlatMapMaybeObserver<T, R>(paramSingleObserver, this.mapper));
  }
  
  static final class FlatMapMaybeObserver<T, R> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {
    private static final long serialVersionUID = 4827726964688405508L;
    
    final SingleObserver<? super R> downstream;
    
    final Function<? super T, ? extends SingleSource<? extends R>> mapper;
    
    FlatMapMaybeObserver(SingleObserver<? super R> param1SingleObserver, Function<? super T, ? extends SingleSource<? extends R>> param1Function) {
      this.downstream = param1SingleObserver;
      this.mapper = param1Function;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      this.downstream.onError(new NoSuchElementException());
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
    final SingleObserver<? super R> downstream;
    
    final AtomicReference<Disposable> parent;
    
    FlatMapSingleObserver(AtomicReference<Disposable> param1AtomicReference, SingleObserver<? super R> param1SingleObserver) {
      this.parent = param1AtomicReference;
      this.downstream = param1SingleObserver;
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeFlatMapSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */