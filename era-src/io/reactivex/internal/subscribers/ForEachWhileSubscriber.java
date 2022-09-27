package io.reactivex.internal.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;

public final class ForEachWhileSubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<T>, Disposable {
  private static final long serialVersionUID = -4403180040475402120L;
  
  boolean done;
  
  final Action onComplete;
  
  final Consumer<? super Throwable> onError;
  
  final Predicate<? super T> onNext;
  
  public ForEachWhileSubscriber(Predicate<? super T> paramPredicate, Consumer<? super Throwable> paramConsumer, Action paramAction) {
    this.onNext = paramPredicate;
    this.onError = paramConsumer;
    this.onComplete = paramAction;
  }
  
  public void dispose() {
    SubscriptionHelper.cancel(this);
  }
  
  public boolean isDisposed() {
    boolean bool;
    if (get() == SubscriptionHelper.CANCELLED) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
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
  
  public void onSubscribe(Subscription paramSubscription) {
    SubscriptionHelper.setOnce(this, paramSubscription, Long.MAX_VALUE);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/subscribers/ForEachWhileSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */