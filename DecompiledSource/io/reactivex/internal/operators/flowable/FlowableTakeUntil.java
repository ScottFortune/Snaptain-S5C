package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.HalfSerializer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableTakeUntil<T, U> extends AbstractFlowableWithUpstream<T, T> {
  final Publisher<? extends U> other;
  
  public FlowableTakeUntil(Flowable<T> paramFlowable, Publisher<? extends U> paramPublisher) {
    super(paramFlowable);
    this.other = paramPublisher;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    TakeUntilMainSubscriber<T> takeUntilMainSubscriber = new TakeUntilMainSubscriber<T>(paramSubscriber);
    paramSubscriber.onSubscribe(takeUntilMainSubscriber);
    this.other.subscribe((Subscriber)takeUntilMainSubscriber.other);
    this.source.subscribe(takeUntilMainSubscriber);
  }
  
  static final class TakeUntilMainSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = -4945480365982832967L;
    
    final Subscriber<? super T> downstream;
    
    final AtomicThrowable error;
    
    final OtherSubscriber other;
    
    final AtomicLong requested;
    
    final AtomicReference<Subscription> upstream;
    
    TakeUntilMainSubscriber(Subscriber<? super T> param1Subscriber) {
      this.downstream = param1Subscriber;
      this.requested = new AtomicLong();
      this.upstream = new AtomicReference<Subscription>();
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
      HalfSerializer.onNext(this.downstream, param1T, this, this.error);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.deferredSetOnce(this.upstream, this.requested, param1Subscription);
    }
    
    public void request(long param1Long) {
      SubscriptionHelper.deferredRequest(this.upstream, this.requested, param1Long);
    }
    
    final class OtherSubscriber extends AtomicReference<Subscription> implements FlowableSubscriber<Object> {
      private static final long serialVersionUID = -3592821756711087922L;
      
      public void onComplete() {
        SubscriptionHelper.cancel(FlowableTakeUntil.TakeUntilMainSubscriber.this.upstream);
        Subscriber subscriber = FlowableTakeUntil.TakeUntilMainSubscriber.this.downstream;
        FlowableTakeUntil.TakeUntilMainSubscriber takeUntilMainSubscriber = FlowableTakeUntil.TakeUntilMainSubscriber.this;
        HalfSerializer.onComplete(subscriber, takeUntilMainSubscriber, takeUntilMainSubscriber.error);
      }
      
      public void onError(Throwable param2Throwable) {
        SubscriptionHelper.cancel(FlowableTakeUntil.TakeUntilMainSubscriber.this.upstream);
        Subscriber subscriber = FlowableTakeUntil.TakeUntilMainSubscriber.this.downstream;
        FlowableTakeUntil.TakeUntilMainSubscriber takeUntilMainSubscriber = FlowableTakeUntil.TakeUntilMainSubscriber.this;
        HalfSerializer.onError(subscriber, param2Throwable, takeUntilMainSubscriber, takeUntilMainSubscriber.error);
      }
      
      public void onNext(Object param2Object) {
        SubscriptionHelper.cancel(this);
        onComplete();
      }
      
      public void onSubscribe(Subscription param2Subscription) {
        SubscriptionHelper.setOnce(this, param2Subscription, Long.MAX_VALUE);
      }
    }
  }
  
  final class OtherSubscriber extends AtomicReference<Subscription> implements FlowableSubscriber<Object> {
    private static final long serialVersionUID = -3592821756711087922L;
    
    public void onComplete() {
      SubscriptionHelper.cancel(this.this$0.upstream);
      Subscriber subscriber = this.this$0.downstream;
      FlowableTakeUntil.TakeUntilMainSubscriber takeUntilMainSubscriber = this.this$0;
      HalfSerializer.onComplete(subscriber, takeUntilMainSubscriber, takeUntilMainSubscriber.error);
    }
    
    public void onError(Throwable param1Throwable) {
      SubscriptionHelper.cancel(this.this$0.upstream);
      Subscriber subscriber = this.this$0.downstream;
      FlowableTakeUntil.TakeUntilMainSubscriber takeUntilMainSubscriber = this.this$0;
      HalfSerializer.onError(subscriber, param1Throwable, takeUntilMainSubscriber, takeUntilMainSubscriber.error);
    }
    
    public void onNext(Object param1Object) {
      SubscriptionHelper.cancel(this);
      onComplete();
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.setOnce(this, param1Subscription, Long.MAX_VALUE);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableTakeUntil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */