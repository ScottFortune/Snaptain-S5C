package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.LongConsumer;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableDoOnLifecycle<T> extends AbstractFlowableWithUpstream<T, T> {
  private final Action onCancel;
  
  private final LongConsumer onRequest;
  
  private final Consumer<? super Subscription> onSubscribe;
  
  public FlowableDoOnLifecycle(Flowable<T> paramFlowable, Consumer<? super Subscription> paramConsumer, LongConsumer paramLongConsumer, Action paramAction) {
    super(paramFlowable);
    this.onSubscribe = paramConsumer;
    this.onRequest = paramLongConsumer;
    this.onCancel = paramAction;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new SubscriptionLambdaSubscriber<T>(paramSubscriber, this.onSubscribe, this.onRequest, this.onCancel));
  }
  
  static final class SubscriptionLambdaSubscriber<T> implements FlowableSubscriber<T>, Subscription {
    final Subscriber<? super T> downstream;
    
    final Action onCancel;
    
    final LongConsumer onRequest;
    
    final Consumer<? super Subscription> onSubscribe;
    
    Subscription upstream;
    
    SubscriptionLambdaSubscriber(Subscriber<? super T> param1Subscriber, Consumer<? super Subscription> param1Consumer, LongConsumer param1LongConsumer, Action param1Action) {
      this.downstream = param1Subscriber;
      this.onSubscribe = param1Consumer;
      this.onCancel = param1Action;
      this.onRequest = param1LongConsumer;
    }
    
    public void cancel() {
      Subscription subscription = this.upstream;
      if (subscription != SubscriptionHelper.CANCELLED)
        this.upstream = (Subscription)SubscriptionHelper.CANCELLED; 
    }
    
    public void onComplete() {
      if (this.upstream != SubscriptionHelper.CANCELLED)
        this.downstream.onComplete(); 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.upstream != SubscriptionHelper.CANCELLED) {
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      this.downstream.onNext(param1T);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      try {
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        param1Subscription.cancel();
        this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
        EmptySubscription.error(exception, this.downstream);
      } 
    }
    
    public void request(long param1Long) {
      try {
        this.onRequest.accept(param1Long);
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableDoOnLifecycle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */