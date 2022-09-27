package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiConsumer;

public final class SingleDoOnEvent<T> extends Single<T> {
  final BiConsumer<? super T, ? super Throwable> onEvent;
  
  final SingleSource<T> source;
  
  public SingleDoOnEvent(SingleSource<T> paramSingleSource, BiConsumer<? super T, ? super Throwable> paramBiConsumer) {
    this.source = paramSingleSource;
    this.onEvent = paramBiConsumer;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    this.source.subscribe(new DoOnEvent(paramSingleObserver));
  }
  
  final class DoOnEvent implements SingleObserver<T> {
    private final SingleObserver<? super T> downstream;
    
    DoOnEvent(SingleObserver<? super T> param1SingleObserver) {
      this.downstream = param1SingleObserver;
    }
    
    public void onError(Throwable param1Throwable) {
      CompositeException compositeException;
      try {
        SingleDoOnEvent.this.onEvent.accept(null, param1Throwable);
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.downstream.onSubscribe(param1Disposable);
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleDoOnEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */