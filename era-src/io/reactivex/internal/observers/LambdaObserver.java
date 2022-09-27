package io.reactivex.internal.observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.Functions;
import io.reactivex.observers.LambdaConsumerIntrospection;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;

public final class LambdaObserver<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable, LambdaConsumerIntrospection {
  private static final long serialVersionUID = -7251123623727029452L;
  
  final Action onComplete;
  
  final Consumer<? super Throwable> onError;
  
  final Consumer<? super T> onNext;
  
  final Consumer<? super Disposable> onSubscribe;
  
  public LambdaObserver(Consumer<? super T> paramConsumer, Consumer<? super Throwable> paramConsumer1, Action paramAction, Consumer<? super Disposable> paramConsumer2) {
    this.onNext = paramConsumer;
    this.onError = paramConsumer1;
    this.onComplete = paramAction;
    this.onSubscribe = paramConsumer2;
  }
  
  public void dispose() {
    DisposableHelper.dispose(this);
  }
  
  public boolean hasCustomOnError() {
    boolean bool;
    if (this.onError != Functions.ON_ERROR_MISSING) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isDisposed() {
    boolean bool;
    if (get() == DisposableHelper.DISPOSED) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void onComplete() {
    if (!isDisposed()) {
      lazySet((Disposable)DisposableHelper.DISPOSED);
      try {
        this.onComplete.run();
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
      } 
    } 
  }
  
  public void onError(Throwable paramThrowable) {
    if (!isDisposed()) {
      lazySet((Disposable)DisposableHelper.DISPOSED);
      try {
        this.onError.accept(paramThrowable);
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
      } 
    } else {
      RxJavaPlugins.onError(paramThrowable);
    } 
  }
  
  public void onNext(T paramT) {
    if (!isDisposed())
      try {
        this.onNext.accept(paramT);
      } finally {
        paramT = null;
        Exceptions.throwIfFatal((Throwable)paramT);
        get().dispose();
      }  
  }
  
  public void onSubscribe(Disposable paramDisposable) {
    if (DisposableHelper.setOnce(this, paramDisposable))
      try {
        this.onSubscribe.accept(this);
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        paramDisposable.dispose();
      }  
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/observers/LambdaObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */