package io.reactivex.internal.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.observers.LambdaConsumerIntrospection;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;

public final class BoundedSubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<T>, Subscription, Disposable, LambdaConsumerIntrospection {
  private static final long serialVersionUID = -7251123623727029452L;
  
  final int bufferSize;
  
  int consumed;
  
  final int limit;
  
  final Action onComplete;
  
  final Consumer<? super Throwable> onError;
  
  final Consumer<? super T> onNext;
  
  final Consumer<? super Subscription> onSubscribe;
  
  public BoundedSubscriber(Consumer<? super T> paramConsumer, Consumer<? super Throwable> paramConsumer1, Action paramAction, Consumer<? super Subscription> paramConsumer2, int paramInt) {
    this.onNext = paramConsumer;
    this.onError = paramConsumer1;
    this.onComplete = paramAction;
    this.onSubscribe = paramConsumer2;
    this.bufferSize = paramInt;
    this.limit = paramInt - (paramInt >> 2);
  }
  
  public void cancel() {
    SubscriptionHelper.cancel(this);
  }
  
  public void dispose() {
    cancel();
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
    if (get() == SubscriptionHelper.CANCELLED) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void onComplete() {
    if (get() != SubscriptionHelper.CANCELLED) {
      lazySet((Subscription)SubscriptionHelper.CANCELLED);
      try {
        this.onComplete.run();
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
      } 
    } 
  }
  
  public void onError(Throwable paramThrowable) {
    if (get() != SubscriptionHelper.CANCELLED) {
      lazySet((Subscription)SubscriptionHelper.CANCELLED);
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
      
      } finally {
        paramT = null;
        Exceptions.throwIfFatal((Throwable)paramT);
        get().cancel();
      }  
  }
  
  public void onSubscribe(Subscription paramSubscription) {
    if (SubscriptionHelper.setOnce(this, paramSubscription))
      try {
        this.onSubscribe.accept(this);
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        paramSubscription.cancel();
      }  
  }
  
  public void request(long paramLong) {
    get().request(paramLong);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/subscribers/BoundedSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */