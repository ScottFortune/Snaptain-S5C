package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.EmptyComponent;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableDetach<T> extends AbstractFlowableWithUpstream<T, T> {
  public FlowableDetach(Flowable<T> paramFlowable) {
    super(paramFlowable);
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new DetachSubscriber<T>(paramSubscriber));
  }
  
  static final class DetachSubscriber<T> implements FlowableSubscriber<T>, Subscription {
    Subscriber<? super T> downstream;
    
    Subscription upstream;
    
    DetachSubscriber(Subscriber<? super T> param1Subscriber) {
      this.downstream = param1Subscriber;
    }
    
    public void cancel() {
      Subscription subscription = this.upstream;
      this.upstream = (Subscription)EmptyComponent.INSTANCE;
      this.downstream = EmptyComponent.asSubscriber();
      subscription.cancel();
    }
    
    public void onComplete() {
      Subscriber<? super T> subscriber = this.downstream;
      this.upstream = (Subscription)EmptyComponent.INSTANCE;
      this.downstream = EmptyComponent.asSubscriber();
      subscriber.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      Subscriber<? super T> subscriber = this.downstream;
      this.upstream = (Subscription)EmptyComponent.INSTANCE;
      this.downstream = EmptyComponent.asSubscriber();
      subscriber.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.downstream.onNext(param1T);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void request(long param1Long) {
      this.upstream.request(param1Long);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableDetach.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */