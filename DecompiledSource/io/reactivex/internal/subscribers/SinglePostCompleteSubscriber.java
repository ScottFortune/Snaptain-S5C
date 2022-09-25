package io.reactivex.internal.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public abstract class SinglePostCompleteSubscriber<T, R> extends AtomicLong implements FlowableSubscriber<T>, Subscription {
  static final long COMPLETE_MASK = -9223372036854775808L;
  
  static final long REQUEST_MASK = 9223372036854775807L;
  
  private static final long serialVersionUID = 7917814472626990048L;
  
  protected final Subscriber<? super R> downstream;
  
  protected long produced;
  
  protected Subscription upstream;
  
  protected R value;
  
  public SinglePostCompleteSubscriber(Subscriber<? super R> paramSubscriber) {
    this.downstream = paramSubscriber;
  }
  
  public void cancel() {
    this.upstream.cancel();
  }
  
  protected final void complete(R paramR) {
    long l = this.produced;
    if (l != 0L)
      BackpressureHelper.produced(this, l); 
    while (true) {
      l = get();
      if ((l & Long.MIN_VALUE) != 0L) {
        onDrop(paramR);
        return;
      } 
      if ((l & Long.MAX_VALUE) != 0L) {
        lazySet(-9223372036854775807L);
        this.downstream.onNext(paramR);
        this.downstream.onComplete();
        return;
      } 
      this.value = paramR;
      if (compareAndSet(0L, Long.MIN_VALUE))
        return; 
      this.value = null;
    } 
  }
  
  protected void onDrop(R paramR) {}
  
  public void onSubscribe(Subscription paramSubscription) {
    if (SubscriptionHelper.validate(this.upstream, paramSubscription)) {
      this.upstream = paramSubscription;
      this.downstream.onSubscribe(this);
    } 
  }
  
  public final void request(long paramLong) {
    if (SubscriptionHelper.validate(paramLong))
      while (true) {
        long l = get();
        if ((l & Long.MIN_VALUE) != 0L) {
          if (compareAndSet(Long.MIN_VALUE, -9223372036854775807L)) {
            this.downstream.onNext(this.value);
            this.downstream.onComplete();
          } 
          break;
        } 
        if (compareAndSet(l, BackpressureHelper.addCap(l, paramLong))) {
          this.upstream.request(paramLong);
          break;
        } 
      }  
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/subscribers/SinglePostCompleteSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */