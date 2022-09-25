package io.reactivex.internal.operators.mixed;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.MaybeSource;
import io.reactivex.Observer;
import io.reactivex.SingleSource;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.EmptyDisposable;
import java.util.concurrent.Callable;

final class ScalarXMapZHelper {
  private ScalarXMapZHelper() {
    throw new IllegalStateException("No instances!");
  }
  
  static <T> boolean tryAsCompletable(Object paramObject, Function<? super T, ? extends CompletableSource> paramFunction, CompletableObserver paramCompletableObserver) {
    if (paramObject instanceof Callable) {
      Callable<Callable> callable = (Callable)paramObject;
      paramObject = null;
      try {
        return true;
      } finally {
        paramObject = null;
        Exceptions.throwIfFatal((Throwable)paramObject);
        EmptyDisposable.error((Throwable)paramObject, paramCompletableObserver);
      } 
    } 
    return false;
  }
  
  static <T, R> boolean tryAsMaybe(Object paramObject, Function<? super T, ? extends MaybeSource<? extends R>> paramFunction, Observer<? super R> paramObserver) {
    if (paramObject instanceof Callable) {
      Callable<Callable> callable = (Callable)paramObject;
      paramObject = null;
      try {
        return true;
      } finally {
        paramObject = null;
        Exceptions.throwIfFatal((Throwable)paramObject);
        EmptyDisposable.error((Throwable)paramObject, paramObserver);
      } 
    } 
    return false;
  }
  
  static <T, R> boolean tryAsSingle(Object paramObject, Function<? super T, ? extends SingleSource<? extends R>> paramFunction, Observer<? super R> paramObserver) {
    if (paramObject instanceof Callable) {
      Callable<Callable> callable = (Callable)paramObject;
      paramObject = null;
      try {
        return true;
      } finally {
        paramObject = null;
        Exceptions.throwIfFatal((Throwable)paramObject);
        EmptyDisposable.error((Throwable)paramObject, paramObserver);
      } 
    } 
    return false;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/mixed/ScalarXMapZHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */