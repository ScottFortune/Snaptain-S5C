package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableOnBackpressureError<T> extends AbstractFlowableWithUpstream<T, T> {
  public FlowableOnBackpressureError(Flowable<T> paramFlowable) {
    super(paramFlowable);
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new BackpressureErrorSubscriber<T>(paramSubscriber));
  }
  
  static final class BackpressureErrorSubscriber<T> extends AtomicLong implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = -3176480756392482682L;
    
    boolean done;
    
    final Subscriber<? super T> downstream;
    
    Subscription upstream;
    
    BackpressureErrorSubscriber(Subscriber<? super T> param1Subscriber) {
      this.downstream = param1Subscriber;
    }
    
    public void cancel() {
      this.upstream.cancel();
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      if (get() != 0L) {
        this.downstream.onNext(param1T);
        BackpressureHelper.produced(this, 1L);
      } else {
        onError((Throwable)new MissingBackpressureException("could not emit value due to lack of requests"));
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        param1Subscription.request(Long.MAX_VALUE);
      } 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long))
        BackpressureHelper.add(this, param1Long); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableOnBackpressureError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */