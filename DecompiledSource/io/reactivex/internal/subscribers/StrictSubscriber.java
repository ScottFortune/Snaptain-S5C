package io.reactivex.internal.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.HalfSerializer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class StrictSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
  private static final long serialVersionUID = -4945028590049415624L;
  
  volatile boolean done;
  
  final Subscriber<? super T> downstream;
  
  final AtomicThrowable error;
  
  final AtomicBoolean once;
  
  final AtomicLong requested;
  
  final AtomicReference<Subscription> upstream;
  
  public StrictSubscriber(Subscriber<? super T> paramSubscriber) {
    this.downstream = paramSubscriber;
    this.error = new AtomicThrowable();
    this.requested = new AtomicLong();
    this.upstream = new AtomicReference<Subscription>();
    this.once = new AtomicBoolean();
  }
  
  public void cancel() {
    if (!this.done)
      SubscriptionHelper.cancel(this.upstream); 
  }
  
  public void onComplete() {
    this.done = true;
    HalfSerializer.onComplete(this.downstream, this, this.error);
  }
  
  public void onError(Throwable paramThrowable) {
    this.done = true;
    HalfSerializer.onError(this.downstream, paramThrowable, this, this.error);
  }
  
  public void onNext(T paramT) {
    HalfSerializer.onNext(this.downstream, paramT, this, this.error);
  }
  
  public void onSubscribe(Subscription paramSubscription) {
    if (this.once.compareAndSet(false, true)) {
      this.downstream.onSubscribe(this);
      SubscriptionHelper.deferredSetOnce(this.upstream, this.requested, paramSubscription);
    } else {
      paramSubscription.cancel();
      cancel();
      onError(new IllegalStateException("§2.12 violated: onSubscribe must be called at most once"));
    } 
  }
  
  public void request(long paramLong) {
    if (paramLong <= 0L) {
      cancel();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("§3.9 violated: positive request amount required but it was ");
      stringBuilder.append(paramLong);
      onError(new IllegalArgumentException(stringBuilder.toString()));
    } else {
      SubscriptionHelper.deferredRequest(this.upstream, this.requested, paramLong);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/subscribers/StrictSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */