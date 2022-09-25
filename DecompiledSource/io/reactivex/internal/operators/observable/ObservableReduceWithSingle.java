package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.disposables.EmptyDisposable;
import java.util.concurrent.Callable;

public final class ObservableReduceWithSingle<T, R> extends Single<R> {
  final BiFunction<R, ? super T, R> reducer;
  
  final Callable<R> seedSupplier;
  
  final ObservableSource<T> source;
  
  public ObservableReduceWithSingle(ObservableSource<T> paramObservableSource, Callable<R> paramCallable, BiFunction<R, ? super T, R> paramBiFunction) {
    this.source = paramObservableSource;
    this.seedSupplier = paramCallable;
    this.reducer = paramBiFunction;
  }
  
  protected void subscribeActual(SingleObserver<? super R> paramSingleObserver) {
    try {
      return;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptyDisposable.error(exception, paramSingleObserver);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableReduceWithSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */