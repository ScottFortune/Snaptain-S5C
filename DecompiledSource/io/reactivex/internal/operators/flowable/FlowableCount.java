package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableCount<T> extends AbstractFlowableWithUpstream<T, Long> {
  public FlowableCount(Flowable<T> paramFlowable) {
    super(paramFlowable);
  }
  
  protected void subscribeActual(Subscriber<? super Long> paramSubscriber) {
    this.source.subscribe(new CountSubscriber(paramSubscriber));
  }
  
  static final class CountSubscriber extends DeferredScalarSubscription<Long> implements FlowableSubscriber<Object> {
    private static final long serialVersionUID = 4973004223787171406L;
    
    long count;
    
    Subscription upstream;
    
    CountSubscriber(Subscriber<? super Long> param1Subscriber) {
      super(param1Subscriber);
    }
    
    public void cancel() {
      super.cancel();
      this.upstream.cancel();
    }
    
    public void onComplete() {
      complete(Long.valueOf(this.count));
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(Object param1Object) {
      this.count++;
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe((Subscription)this);
        param1Subscription.request(Long.MAX_VALUE);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableCount.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */