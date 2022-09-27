package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.HalfSerializer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableSkipUntil<T, U> extends AbstractFlowableWithUpstream<T, T> {
  final Publisher<U> other;
  
  public FlowableSkipUntil(Flowable<T> paramFlowable, Publisher<U> paramPublisher) {
    super(paramFlowable);
    this.other = paramPublisher;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    SkipUntilMainSubscriber<T> skipUntilMainSubscriber = new SkipUntilMainSubscriber<T>(paramSubscriber);
    paramSubscriber.onSubscribe(skipUntilMainSubscriber);
    this.other.subscribe((Subscriber)skipUntilMainSubscriber.other);
    this.source.subscribe((FlowableSubscriber)skipUntilMainSubscriber);
  }
  
  static final class SkipUntilMainSubscriber<T> extends AtomicInteger implements ConditionalSubscriber<T>, Subscription {
    private static final long serialVersionUID = -6270983465606289181L;
    
    final Subscriber<? super T> downstream;
    
    final AtomicThrowable error;
    
    volatile boolean gate;
    
    final OtherSubscriber other;
    
    final AtomicLong requested;
    
    final AtomicReference<Subscription> upstream;
    
    SkipUntilMainSubscriber(Subscriber<? super T> param1Subscriber) {
      this.downstream = param1Subscriber;
      this.upstream = new AtomicReference<Subscription>();
      this.requested = new AtomicLong();
      this.other = new OtherSubscriber();
      this.error = new AtomicThrowable();
    }
    
    public void cancel() {
      SubscriptionHelper.cancel(this.upstream);
      SubscriptionHelper.cancel(this.other);
    }
    
    public void onComplete() {
      SubscriptionHelper.cancel(this.other);
      HalfSerializer.onComplete(this.downstream, this, this.error);
    }
    
    public void onError(Throwable param1Throwable) {
      SubscriptionHelper.cancel(this.other);
      HalfSerializer.onError(this.downstream, param1Throwable, this, this.error);
    }
    
    public void onNext(T param1T) {
      if (!tryOnNext(param1T))
        ((Subscription)this.upstream.get()).request(1L); 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.deferredSetOnce(this.upstream, this.requested, param1Subscription);
    }
    
    public void request(long param1Long) {
      SubscriptionHelper.deferredRequest(this.upstream, this.requested, param1Long);
    }
    
    public boolean tryOnNext(T param1T) {
      if (this.gate) {
        HalfSerializer.onNext(this.downstream, param1T, this, this.error);
        return true;
      } 
      return false;
    }
    
    final class OtherSubscriber extends AtomicReference<Subscription> implements FlowableSubscriber<Object> {
      private static final long serialVersionUID = -5592042965931999169L;
      
      public void onComplete() {
        FlowableSkipUntil.SkipUntilMainSubscriber.this.gate = true;
      }
      
      public void onError(Throwable param2Throwable) {
        SubscriptionHelper.cancel(FlowableSkipUntil.SkipUntilMainSubscriber.this.upstream);
        Subscriber subscriber = FlowableSkipUntil.SkipUntilMainSubscriber.this.downstream;
        FlowableSkipUntil.SkipUntilMainSubscriber skipUntilMainSubscriber = FlowableSkipUntil.SkipUntilMainSubscriber.this;
        HalfSerializer.onError(subscriber, param2Throwable, skipUntilMainSubscriber, skipUntilMainSubscriber.error);
      }
      
      public void onNext(Object param2Object) {
        FlowableSkipUntil.SkipUntilMainSubscriber.this.gate = true;
        get().cancel();
      }
      
      public void onSubscribe(Subscription param2Subscription) {
        SubscriptionHelper.setOnce(this, param2Subscription, Long.MAX_VALUE);
      }
    }
  }
  
  final class OtherSubscriber extends AtomicReference<Subscription> implements FlowableSubscriber<Object> {
    private static final long serialVersionUID = -5592042965931999169L;
    
    public void onComplete() {
      this.this$0.gate = true;
    }
    
    public void onError(Throwable param1Throwable) {
      SubscriptionHelper.cancel(this.this$0.upstream);
      Subscriber subscriber = this.this$0.downstream;
      FlowableSkipUntil.SkipUntilMainSubscriber skipUntilMainSubscriber = this.this$0;
      HalfSerializer.onError(subscriber, param1Throwable, skipUntilMainSubscriber, skipUntilMainSubscriber.error);
    }
    
    public void onNext(Object param1Object) {
      this.this$0.gate = true;
      get().cancel();
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.setOnce(this, param1Subscription, Long.MAX_VALUE);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableSkipUntil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */