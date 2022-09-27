package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.Functions;
import io.reactivex.observers.LambdaConsumerIntrospection;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeCallbackObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable, LambdaConsumerIntrospection {
  private static final long serialVersionUID = -6076952298809384986L;
  
  final Action onComplete;
  
  final Consumer<? super Throwable> onError;
  
  final Consumer<? super T> onSuccess;
  
  public MaybeCallbackObserver(Consumer<? super T> paramConsumer, Consumer<? super Throwable> paramConsumer1, Action paramAction) {
    this.onSuccess = paramConsumer;
    this.onError = paramConsumer1;
    this.onComplete = paramAction;
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
    return DisposableHelper.isDisposed(get());
  }
  
  public void onComplete() {
    lazySet((Disposable)DisposableHelper.DISPOSED);
  }
  
  public void onError(Throwable paramThrowable) {
    lazySet((Disposable)DisposableHelper.DISPOSED);
  }
  
  public void onSubscribe(Disposable paramDisposable) {
    DisposableHelper.setOnce(this, paramDisposable);
  }
  
  public void onSuccess(T paramT) {
    lazySet((Disposable)DisposableHelper.DISPOSED);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeCallbackObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */