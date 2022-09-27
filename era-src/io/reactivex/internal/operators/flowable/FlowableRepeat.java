package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionArbiter;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableRepeat<T> extends AbstractFlowableWithUpstream<T, T> {
  final long count;
  
  public FlowableRepeat(Flowable<T> paramFlowable, long paramLong) {
    super(paramFlowable);
    this.count = paramLong;
  }
  
  public void subscribeActual(Subscriber<? super T> paramSubscriber) {
    SubscriptionArbiter subscriptionArbiter = new SubscriptionArbiter(false);
    paramSubscriber.onSubscribe((Subscription)subscriptionArbiter);
    long l1 = this.count;
    long l2 = Long.MAX_VALUE;
    if (l1 != Long.MAX_VALUE)
      l2 = l1 - 1L; 
    (new RepeatSubscriber(paramSubscriber, l2, subscriptionArbiter, (Publisher<?>)this.source)).subscribeNext();
  }
  
  static final class RepeatSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T> {
    private static final long serialVersionUID = -7098360935104053232L;
    
    final Subscriber<? super T> downstream;
    
    long produced;
    
    long remaining;
    
    final SubscriptionArbiter sa;
    
    final Publisher<? extends T> source;
    
    RepeatSubscriber(Subscriber<? super T> param1Subscriber, long param1Long, SubscriptionArbiter param1SubscriptionArbiter, Publisher<? extends T> param1Publisher) {
      this.downstream = param1Subscriber;
      this.sa = param1SubscriptionArbiter;
      this.source = param1Publisher;
      this.remaining = param1Long;
    }
    
    public void onComplete() {
      long l = this.remaining;
      if (l != Long.MAX_VALUE)
        this.remaining = l - 1L; 
      if (l != 0L) {
        subscribeNext();
      } else {
        this.downstream.onComplete();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.produced++;
      this.downstream.onNext(param1T);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      this.sa.setSubscription(param1Subscription);
    }
    
    void subscribeNext() {
      if (getAndIncrement() == 0) {
        int j;
        int i = 1;
        do {
          if (this.sa.isCancelled())
            return; 
          long l = this.produced;
          if (l != 0L) {
            this.produced = 0L;
            this.sa.produced(l);
          } 
          this.source.subscribe((Subscriber)this);
          j = addAndGet(-i);
          i = j;
        } while (j != 0);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableRepeat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */