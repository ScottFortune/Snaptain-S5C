package io.reactivex.internal.operators.single;

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

public final class SingleFlatMapMaybe<T, R> extends Maybe<R> {
  final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
  
  final SingleSource<? extends T> source;
  
  public SingleFlatMapMaybe(SingleSource<? extends T> paramSingleSource, Function<? super T, ? extends MaybeSource<? extends R>> paramFunction) {
    this.mapper = paramFunction;
    this.source = paramSingleSource;
  }
  
  protected void subscribeActual(MaybeObserver<? super R> paramMaybeObserver) {
    this.source.subscribe(new FlatMapSingleObserver<T, R>(paramMaybeObserver, this.mapper));
  }
  
  static final class FlatMapMaybeObserver<R> implements MaybeObserver<R> {
    final MaybeObserver<? super R> downstream;
    
    final AtomicReference<Disposable> parent;
    
    FlatMapMaybeObserver(AtomicReference<Disposable> param1AtomicReference, MaybeObserver<? super R> param1MaybeObserver) {
      this.parent = param1AtomicReference;
      this.downstream = param1MaybeObserver;
    }
    
    public void onComplete() {
      this.downstream.onComplete();
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
  
  static final class FlatMapSingleObserver<T, R> extends AtomicReference<Disposable> implements SingleObserver<T>, Disposable {
    private static final long serialVersionUID = -5843758257109742742L;
    
    final MaybeObserver<? super R> downstream;
    
    final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
    
    FlatMapSingleObserver(MaybeObserver<? super R> param1MaybeObserver, Function<? super T, ? extends MaybeSource<? extends R>> param1Function) {
      this.downstream = param1MaybeObserver;
      this.mapper = param1Function;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
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
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleFlatMapMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */