package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleFlatMap<T, R> extends Single<R> {
  final Function<? super T, ? extends SingleSource<? extends R>> mapper;
  
  final SingleSource<? extends T> source;
  
  public SingleFlatMap(SingleSource<? extends T> paramSingleSource, Function<? super T, ? extends SingleSource<? extends R>> paramFunction) {
    this.mapper = paramFunction;
    this.source = paramSingleSource;
  }
  
  protected void subscribeActual(SingleObserver<? super R> paramSingleObserver) {
    this.source.subscribe(new SingleFlatMapCallback<T, R>(paramSingleObserver, this.mapper));
  }
  
  static final class SingleFlatMapCallback<T, R> extends AtomicReference<Disposable> implements SingleObserver<T>, Disposable {
    private static final long serialVersionUID = 3258103020495908596L;
    
    final SingleObserver<? super R> downstream;
    
    final Function<? super T, ? extends SingleSource<? extends R>> mapper;
    
    SingleFlatMapCallback(SingleObserver<? super R> param1SingleObserver, Function<? super T, ? extends SingleSource<? extends R>> param1Function) {
      this.downstream = param1SingleObserver;
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
        this.downstream.onError((Throwable)param1T);
      } 
    }
    
    static final class FlatMapSingleObserver<R> implements SingleObserver<R> {
      final SingleObserver<? super R> downstream;
      
      final AtomicReference<Disposable> parent;
      
      FlatMapSingleObserver(AtomicReference<Disposable> param2AtomicReference, SingleObserver<? super R> param2SingleObserver) {
        this.parent = param2AtomicReference;
        this.downstream = param2SingleObserver;
      }
      
      public void onError(Throwable param2Throwable) {
        this.downstream.onError(param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.replace(this.parent, param2Disposable);
      }
      
      public void onSuccess(R param2R) {
        this.downstream.onSuccess(param2R);
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleFlatMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */