package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOperator;
import io.reactivex.CompletableSource;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;

public final class CompletableLift extends Completable {
  final CompletableOperator onLift;
  
  final CompletableSource source;
  
  public CompletableLift(CompletableSource paramCompletableSource, CompletableOperator paramCompletableOperator) {
    this.source = paramCompletableSource;
    this.onLift = paramCompletableOperator;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    try {
      return;
    } catch (NullPointerException nullPointerException) {
      throw nullPointerException;
    } finally {
      paramCompletableObserver = null;
      Exceptions.throwIfFatal((Throwable)paramCompletableObserver);
      RxJavaPlugins.onError((Throwable)paramCompletableObserver);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableLift.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */