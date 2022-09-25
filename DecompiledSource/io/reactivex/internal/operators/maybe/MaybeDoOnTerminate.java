package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;

public final class MaybeDoOnTerminate<T> extends Maybe<T> {
  final Action onTerminate;
  
  final MaybeSource<T> source;
  
  public MaybeDoOnTerminate(MaybeSource<T> paramMaybeSource, Action paramAction) {
    this.source = paramMaybeSource;
    this.onTerminate = paramAction;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    this.source.subscribe(new DoOnTerminate(paramMaybeObserver));
  }
  
  final class DoOnTerminate implements MaybeObserver<T> {
    final MaybeObserver<? super T> downstream;
    
    DoOnTerminate(MaybeObserver<? super T> param1MaybeObserver) {
      this.downstream = param1MaybeObserver;
    }
    
    public void onComplete() {
      try {
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        this.downstream.onError(exception);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      CompositeException compositeException;
      try {
        MaybeDoOnTerminate.this.onTerminate.run();
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeDoOnTerminate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */