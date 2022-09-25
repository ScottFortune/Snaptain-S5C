package io.reactivex.internal.observers;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.Functions;
import io.reactivex.observers.LambdaConsumerIntrospection;
import java.util.concurrent.atomic.AtomicReference;

public final class ConsumerSingleObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T>, Disposable, LambdaConsumerIntrospection {
  private static final long serialVersionUID = -7012088219455310787L;
  
  final Consumer<? super Throwable> onError;
  
  final Consumer<? super T> onSuccess;
  
  public ConsumerSingleObserver(Consumer<? super T> paramConsumer, Consumer<? super Throwable> paramConsumer1) {
    this.onSuccess = paramConsumer;
    this.onError = paramConsumer1;
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/observers/ConsumerSingleObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */