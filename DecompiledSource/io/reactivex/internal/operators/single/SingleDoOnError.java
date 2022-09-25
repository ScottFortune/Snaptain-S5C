package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;

public final class SingleDoOnError<T> extends Single<T> {
  final Consumer<? super Throwable> onError;
  
  final SingleSource<T> source;
  
  public SingleDoOnError(SingleSource<T> paramSingleSource, Consumer<? super Throwable> paramConsumer) {
    this.source = paramSingleSource;
    this.onError = paramConsumer;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    this.source.subscribe(new DoOnError(paramSingleObserver));
  }
  
  final class DoOnError implements SingleObserver<T> {
    private final SingleObserver<? super T> downstream;
    
    DoOnError(SingleObserver<? super T> param1SingleObserver) {
      this.downstream = param1SingleObserver;
    }
    
    public void onError(Throwable param1Throwable) {
      CompositeException compositeException;
      try {
        SingleDoOnError.this.onError.accept(param1Throwable);
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.downstream.onSubscribe(param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      this.downstream.onSuccess(param1T);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleDoOnError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */