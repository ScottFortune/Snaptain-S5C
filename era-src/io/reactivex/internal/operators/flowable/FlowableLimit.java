package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableLimit<T> extends AbstractFlowableWithUpstream<T, T> {
  final long n;
  
  public FlowableLimit(Flowable<T> paramFlowable, long paramLong) {
    super(paramFlowable);
    this.n = paramLong;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new LimitSubscriber<T>(paramSubscriber, this.n));
  }
  
  static final class LimitSubscriber<T> extends AtomicLong implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = 2288246011222124525L;
    
    final Subscriber<? super T> downstream;
    
    long remaining;
    
    Subscription upstream;
    
    LimitSubscriber(Subscriber<? super T> param1Subscriber, long param1Long) {
      this.downstream = param1Subscriber;
      this.remaining = param1Long;
      lazySet(param1Long);
    }
    
    public void cancel() {
      this.upstream.cancel();
    }
    
    public void onComplete() {
      if (this.remaining > 0L) {
        this.remaining = 0L;
        this.downstream.onComplete();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.remaining > 0L) {
        this.remaining = 0L;
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      long l = this.remaining;
      if (l > 0L) {
        this.remaining = --l;
        this.downstream.onNext(param1T);
        if (l == 0L) {
          this.upstream.cancel();
          this.downstream.onComplete();
        } 
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription))
        if (this.remaining == 0L) {
          param1Subscription.cancel();
          EmptySubscription.complete(this.downstream);
        } else {
          this.upstream = param1Subscription;
          this.downstream.onSubscribe(this);
        }  
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long))
        while (true) {
          long l2;
          long l1 = get();
          if (l1 == 0L)
            break; 
          if (l1 <= param1Long) {
            l2 = l1;
          } else {
            l2 = param1Long;
          } 
          if (compareAndSet(l1, l1 - l2)) {
            this.upstream.request(l2);
            break;
          } 
        }  
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableLimit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */