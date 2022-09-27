package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.internal.subscriptions.SubscriptionArbiter;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableRepeatUntil<T> extends AbstractFlowableWithUpstream<T, T> {
  final BooleanSupplier until;
  
  public FlowableRepeatUntil(Flowable<T> paramFlowable, BooleanSupplier paramBooleanSupplier) {
    super(paramFlowable);
    this.until = paramBooleanSupplier;
  }
  
  public void subscribeActual(Subscriber<? super T> paramSubscriber) {
    SubscriptionArbiter subscriptionArbiter = new SubscriptionArbiter(false);
    paramSubscriber.onSubscribe((Subscription)subscriptionArbiter);
    (new RepeatSubscriber(paramSubscriber, this.until, subscriptionArbiter, (Publisher<?>)this.source)).subscribeNext();
  }
  
  static final class RepeatSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T> {
    private static final long serialVersionUID = -7098360935104053232L;
    
    final Subscriber<? super T> downstream;
    
    long produced;
    
    final SubscriptionArbiter sa;
    
    final Publisher<? extends T> source;
    
    final BooleanSupplier stop;
    
    RepeatSubscriber(Subscriber<? super T> param1Subscriber, BooleanSupplier param1BooleanSupplier, SubscriptionArbiter param1SubscriptionArbiter, Publisher<? extends T> param1Publisher) {
      this.downstream = param1Subscriber;
      this.sa = param1SubscriptionArbiter;
      this.source = param1Publisher;
      this.stop = param1BooleanSupplier;
    }
    
    public void onComplete() {
      try {
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        this.downstream.onError(exception);
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableRepeatUntil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */