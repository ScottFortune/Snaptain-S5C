package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;

public final class CompletableOnErrorComplete extends Completable {
  final Predicate<? super Throwable> predicate;
  
  final CompletableSource source;
  
  public CompletableOnErrorComplete(CompletableSource paramCompletableSource, Predicate<? super Throwable> paramPredicate) {
    this.source = paramCompletableSource;
    this.predicate = paramPredicate;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    this.source.subscribe(new OnError(paramCompletableObserver));
  }
  
  final class OnError implements CompletableObserver {
    private final CompletableObserver downstream;
    
    OnError(CompletableObserver param1CompletableObserver) {
      this.downstream = param1CompletableObserver;
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      try {
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        this.downstream.onError((Throwable)new CompositeException(new Throwable[] { param1Throwable, exception }));
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.downstream.onSubscribe(param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableOnErrorComplete.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */