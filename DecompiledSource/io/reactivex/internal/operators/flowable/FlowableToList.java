package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.Collection;
import java.util.concurrent.Callable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableToList<T, U extends Collection<? super T>> extends AbstractFlowableWithUpstream<T, U> {
  final Callable<U> collectionSupplier;
  
  public FlowableToList(Flowable<T> paramFlowable, Callable<U> paramCallable) {
    super(paramFlowable);
    this.collectionSupplier = paramCallable;
  }
  
  protected void subscribeActual(Subscriber<? super U> paramSubscriber) {
    try {
      return;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptySubscription.error(exception, paramSubscriber);
    } 
  }
  
  static final class ToListSubscriber<T, U extends Collection<? super T>> extends DeferredScalarSubscription<U> implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = -8134157938864266736L;
    
    Subscription upstream;
    
    ToListSubscriber(Subscriber<? super U> param1Subscriber, U param1U) {
      super(param1Subscriber);
      this.value = param1U;
    }
    
    public void cancel() {
      super.cancel();
      this.upstream.cancel();
    }
    
    public void onComplete() {
      complete(this.value);
    }
    
    public void onError(Throwable param1Throwable) {
      this.value = null;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      Collection<T> collection = (Collection)this.value;
      if (collection != null)
        collection.add(param1T); 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        param1Subscription.request(Long.MAX_VALUE);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableToList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */