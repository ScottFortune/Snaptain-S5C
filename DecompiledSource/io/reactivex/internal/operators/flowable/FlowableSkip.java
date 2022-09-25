package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableSkip<T> extends AbstractFlowableWithUpstream<T, T> {
  final long n;
  
  public FlowableSkip(Flowable<T> paramFlowable, long paramLong) {
    super(paramFlowable);
    this.n = paramLong;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new SkipSubscriber<T>(paramSubscriber, this.n));
  }
  
  static final class SkipSubscriber<T> implements FlowableSubscriber<T>, Subscription {
    final Subscriber<? super T> downstream;
    
    long remaining;
    
    Subscription upstream;
    
    SkipSubscriber(Subscriber<? super T> param1Subscriber, long param1Long) {
      this.downstream = param1Subscriber;
      this.remaining = param1Long;
    }
    
    public void cancel() {
      this.upstream.cancel();
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      long l = this.remaining;
      if (l != 0L) {
        this.remaining = l - 1L;
      } else {
        this.downstream.onNext(param1T);
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        long l = this.remaining;
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        param1Subscription.request(l);
      } 
    }
    
    public void request(long param1Long) {
      this.upstream.request(param1Long);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableSkip.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */