package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableIgnoreElements<T> extends AbstractFlowableWithUpstream<T, T> {
  public FlowableIgnoreElements(Flowable<T> paramFlowable) {
    super(paramFlowable);
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new IgnoreElementsSubscriber<T>(paramSubscriber));
  }
  
  static final class IgnoreElementsSubscriber<T> implements FlowableSubscriber<T>, QueueSubscription<T> {
    final Subscriber<? super T> downstream;
    
    Subscription upstream;
    
    IgnoreElementsSubscriber(Subscriber<? super T> param1Subscriber) {
      this.downstream = param1Subscriber;
    }
    
    public void cancel() {
      this.upstream.cancel();
    }
    
    public void clear() {}
    
    public boolean isEmpty() {
      return true;
    }
    
    public boolean offer(T param1T) {
      throw new UnsupportedOperationException("Should not be called!");
    }
    
    public boolean offer(T param1T1, T param1T2) {
      throw new UnsupportedOperationException("Should not be called!");
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {}
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe((Subscription)this);
        param1Subscription.request(Long.MAX_VALUE);
      } 
    }
    
    public T poll() {
      return null;
    }
    
    public void request(long param1Long) {}
    
    public int requestFusion(int param1Int) {
      return param1Int & 0x2;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableIgnoreElements.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */