package io.reactivex.internal.observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;

public final class ForEachWhileObserver<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable {
  private static final long serialVersionUID = -4403180040475402120L;
  
  boolean done;
  
  final Action onComplete;
  
  final Consumer<? super Throwable> onError;
  
  final Predicate<? super T> onNext;
  
  public ForEachWhileObserver(Predicate<? super T> paramPredicate, Consumer<? super Throwable> paramConsumer, Action paramAction) {
    this.onNext = paramPredicate;
    this.onError = paramConsumer;
    this.onComplete = paramAction;
  }
  
  public void dispose() {
    DisposableHelper.dispose(this);
  }
  
  public boolean isDisposed() {
    return DisposableHelper.isDisposed(get());
  }
  
  public void onComplete() {
    if (this.done)
      return; 
    this.done = true;
  }
  
  public void onError(Throwable paramThrowable) {
    if (this.done) {
      RxJavaPlugins.onError(paramThrowable);
      return;
    } 
    this.done = true;
  }
  
  public void onNext(T paramT) {
    if (this.done)
      return; 
    try {
      return;
    } finally {
      paramT = null;
      Exceptions.throwIfFatal((Throwable)paramT);
      dispose();
      onError((Throwable)paramT);
    } 
  }
  
  public void onSubscribe(Disposable paramDisposable) {
    DisposableHelper.setOnce(this, paramDisposable);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/observers/ForEachWhileObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */