package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeFlatMapBiSelector<T, U, R> extends AbstractMaybeWithUpstream<T, R> {
  final Function<? super T, ? extends MaybeSource<? extends U>> mapper;
  
  final BiFunction<? super T, ? super U, ? extends R> resultSelector;
  
  public MaybeFlatMapBiSelector(MaybeSource<T> paramMaybeSource, Function<? super T, ? extends MaybeSource<? extends U>> paramFunction, BiFunction<? super T, ? super U, ? extends R> paramBiFunction) {
    super(paramMaybeSource);
    this.mapper = paramFunction;
    this.resultSelector = paramBiFunction;
  }
  
  protected void subscribeActual(MaybeObserver<? super R> paramMaybeObserver) {
    this.source.subscribe(new FlatMapBiMainObserver<T, U, R>(paramMaybeObserver, this.mapper, this.resultSelector));
  }
  
  static final class FlatMapBiMainObserver<T, U, R> implements MaybeObserver<T>, Disposable {
    final InnerObserver<T, U, R> inner;
    
    final Function<? super T, ? extends MaybeSource<? extends U>> mapper;
    
    FlatMapBiMainObserver(MaybeObserver<? super R> param1MaybeObserver, Function<? super T, ? extends MaybeSource<? extends U>> param1Function, BiFunction<? super T, ? super U, ? extends R> param1BiFunction) {
      this.inner = new InnerObserver<T, U, R>(param1MaybeObserver, param1BiFunction);
      this.mapper = param1Function;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this.inner);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(this.inner.get());
    }
    
    public void onComplete() {
      this.inner.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.inner.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.setOnce(this.inner, param1Disposable))
        this.inner.downstream.onSubscribe(this); 
    }
    
    public void onSuccess(T param1T) {
      try {
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        this.inner.downstream.onError((Throwable)param1T);
      } 
    }
    
    static final class InnerObserver<T, U, R> extends AtomicReference<Disposable> implements MaybeObserver<U> {
      private static final long serialVersionUID = -2897979525538174559L;
      
      final MaybeObserver<? super R> downstream;
      
      final BiFunction<? super T, ? super U, ? extends R> resultSelector;
      
      T value;
      
      InnerObserver(MaybeObserver<? super R> param2MaybeObserver, BiFunction<? super T, ? super U, ? extends R> param2BiFunction) {
        this.downstream = param2MaybeObserver;
        this.resultSelector = param2BiFunction;
      }
      
      public void onComplete() {
        this.downstream.onComplete();
      }
      
      public void onError(Throwable param2Throwable) {
        this.downstream.onError(param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.setOnce(this, param2Disposable);
      }
      
      public void onSuccess(U param2U) {
        T t = this.value;
        this.value = null;
        try {
          return;
        } finally {
          param2U = null;
          Exceptions.throwIfFatal((Throwable)param2U);
          this.downstream.onError((Throwable)param2U);
        } 
      }
    }
  }
  
  static final class InnerObserver<T, U, R> extends AtomicReference<Disposable> implements MaybeObserver<U> {
    private static final long serialVersionUID = -2897979525538174559L;
    
    final MaybeObserver<? super R> downstream;
    
    final BiFunction<? super T, ? super U, ? extends R> resultSelector;
    
    T value;
    
    InnerObserver(MaybeObserver<? super R> param1MaybeObserver, BiFunction<? super T, ? super U, ? extends R> param1BiFunction) {
      this.downstream = param1MaybeObserver;
      this.resultSelector = param1BiFunction;
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
    
    public void onSuccess(U param1U) {
      T t = this.value;
      this.value = null;
      try {
        return;
      } finally {
        param1U = null;
        Exceptions.throwIfFatal((Throwable)param1U);
        this.downstream.onError((Throwable)param1U);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeFlatMapBiSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */