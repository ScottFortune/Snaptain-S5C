package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;

public final class SingleDoOnTerminate<T> extends Single<T> {
  final Action onTerminate;
  
  final SingleSource<T> source;
  
  public SingleDoOnTerminate(SingleSource<T> paramSingleSource, Action paramAction) {
    this.source = paramSingleSource;
    this.onTerminate = paramAction;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    this.source.subscribe(new DoOnTerminate(paramSingleObserver));
  }
  
  final class DoOnTerminate implements SingleObserver<T> {
    final SingleObserver<? super T> downstream;
    
    DoOnTerminate(SingleObserver<? super T> param1SingleObserver) {
      this.downstream = param1SingleObserver;
    }
    
    public void onError(Throwable param1Throwable) {
      CompositeException compositeException;
      try {
        SingleDoOnTerminate.this.onTerminate.run();
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleDoOnTerminate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */