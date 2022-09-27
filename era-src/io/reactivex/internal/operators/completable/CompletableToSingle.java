package io.reactivex.internal.operators.completable;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import java.util.concurrent.Callable;

public final class CompletableToSingle<T> extends Single<T> {
  final T completionValue;
  
  final Callable<? extends T> completionValueSupplier;
  
  final CompletableSource source;
  
  public CompletableToSingle(CompletableSource paramCompletableSource, Callable<? extends T> paramCallable, T paramT) {
    this.source = paramCompletableSource;
    this.completionValue = paramT;
    this.completionValueSupplier = paramCallable;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    this.source.subscribe(new ToSingle(paramSingleObserver));
  }
  
  final class ToSingle implements CompletableObserver {
    private final SingleObserver<? super T> observer;
    
    ToSingle(SingleObserver<? super T> param1SingleObserver) {
      this.observer = param1SingleObserver;
    }
    
    public void onComplete() {
      T t;
      if (CompletableToSingle.this.completionValueSupplier != null) {
        try {
          Object object = CompletableToSingle.this.completionValueSupplier.call();
        } finally {
          Exception exception = null;
          Exceptions.throwIfFatal(exception);
          this.observer.onError(exception);
        } 
      } else {
        t = CompletableToSingle.this.completionValue;
      } 
      if (t == null) {
        this.observer.onError(new NullPointerException("The value supplied is null"));
      } else {
        this.observer.onSuccess(t);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      this.observer.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.observer.onSubscribe(param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableToSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */