package io.reactivex.internal.observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;

public final class DisposableLambdaObserver<T> implements Observer<T>, Disposable {
  final Observer<? super T> downstream;
  
  final Action onDispose;
  
  final Consumer<? super Disposable> onSubscribe;
  
  Disposable upstream;
  
  public DisposableLambdaObserver(Observer<? super T> paramObserver, Consumer<? super Disposable> paramConsumer, Action paramAction) {
    this.downstream = paramObserver;
    this.onSubscribe = paramConsumer;
    this.onDispose = paramAction;
  }
  
  public void dispose() {
    Disposable disposable = this.upstream;
    if (disposable != DisposableHelper.DISPOSED)
      this.upstream = (Disposable)DisposableHelper.DISPOSED; 
  }
  
  public boolean isDisposed() {
    return this.upstream.isDisposed();
  }
  
  public void onComplete() {
    if (this.upstream != DisposableHelper.DISPOSED) {
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      this.downstream.onComplete();
    } 
  }
  
  public void onError(Throwable paramThrowable) {
    if (this.upstream != DisposableHelper.DISPOSED) {
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      this.downstream.onError(paramThrowable);
    } else {
      RxJavaPlugins.onError(paramThrowable);
    } 
  }
  
  public void onNext(T paramT) {
    this.downstream.onNext(paramT);
  }
  
  public void onSubscribe(Disposable paramDisposable) {
    try {
      return;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      paramDisposable.dispose();
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      EmptyDisposable.error(exception, this.downstream);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/observers/DisposableLambdaObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */