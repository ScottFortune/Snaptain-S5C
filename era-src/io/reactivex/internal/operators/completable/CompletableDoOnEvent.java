package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;

public final class CompletableDoOnEvent extends Completable {
  final Consumer<? super Throwable> onEvent;
  
  final CompletableSource source;
  
  public CompletableDoOnEvent(CompletableSource paramCompletableSource, Consumer<? super Throwable> paramConsumer) {
    this.source = paramCompletableSource;
    this.onEvent = paramConsumer;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    this.source.subscribe(new DoOnEvent(paramCompletableObserver));
  }
  
  final class DoOnEvent implements CompletableObserver {
    private final CompletableObserver observer;
    
    DoOnEvent(CompletableObserver param1CompletableObserver) {
      this.observer = param1CompletableObserver;
    }
    
    public void onComplete() {
      try {
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        this.observer.onError(exception);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      CompositeException compositeException;
      try {
        CompletableDoOnEvent.this.onEvent.accept(param1Throwable);
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.observer.onSubscribe(param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableDoOnEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */