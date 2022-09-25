package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;

public final class CompletablePeek extends Completable {
  final Action onAfterTerminate;
  
  final Action onComplete;
  
  final Action onDispose;
  
  final Consumer<? super Throwable> onError;
  
  final Consumer<? super Disposable> onSubscribe;
  
  final Action onTerminate;
  
  final CompletableSource source;
  
  public CompletablePeek(CompletableSource paramCompletableSource, Consumer<? super Disposable> paramConsumer, Consumer<? super Throwable> paramConsumer1, Action paramAction1, Action paramAction2, Action paramAction3, Action paramAction4) {
    this.source = paramCompletableSource;
    this.onSubscribe = paramConsumer;
    this.onError = paramConsumer1;
    this.onComplete = paramAction1;
    this.onTerminate = paramAction2;
    this.onAfterTerminate = paramAction3;
    this.onDispose = paramAction4;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    this.source.subscribe(new CompletableObserverImplementation(paramCompletableObserver));
  }
  
  final class CompletableObserverImplementation implements CompletableObserver, Disposable {
    final CompletableObserver downstream;
    
    Disposable upstream;
    
    CompletableObserverImplementation(CompletableObserver param1CompletableObserver) {
      this.downstream = param1CompletableObserver;
    }
    
    public void dispose() {
      try {
        CompletablePeek.this.onDispose.run();
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
      } 
    }
    
    void doAfter() {
      try {
        CompletablePeek.this.onAfterTerminate.run();
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
      } 
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      if (this.upstream == DisposableHelper.DISPOSED)
        return; 
      try {
        CompletablePeek.this.onComplete.run();
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        this.downstream.onError(exception);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      CompositeException compositeException;
      if (this.upstream == DisposableHelper.DISPOSED) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      try {
        CompletablePeek.this.onError.accept(param1Throwable);
        CompletablePeek.this.onTerminate.run();
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      try {
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        param1Disposable.dispose();
        this.upstream = (Disposable)DisposableHelper.DISPOSED;
        EmptyDisposable.error(exception, this.downstream);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletablePeek.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */