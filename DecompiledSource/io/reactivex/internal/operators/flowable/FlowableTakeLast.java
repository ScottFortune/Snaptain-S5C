package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableTakeLast<T> extends AbstractFlowableWithUpstream<T, T> {
  final int count;
  
  public FlowableTakeLast(Flowable<T> paramFlowable, int paramInt) {
    super(paramFlowable);
    this.count = paramInt;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new TakeLastSubscriber<T>(paramSubscriber, this.count));
  }
  
  static final class TakeLastSubscriber<T> extends ArrayDeque<T> implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = 7240042530241604978L;
    
    volatile boolean cancelled;
    
    final int count;
    
    volatile boolean done;
    
    final Subscriber<? super T> downstream;
    
    final AtomicLong requested = new AtomicLong();
    
    Subscription upstream;
    
    final AtomicInteger wip = new AtomicInteger();
    
    TakeLastSubscriber(Subscriber<? super T> param1Subscriber, int param1Int) {
      this.downstream = param1Subscriber;
      this.count = param1Int;
    }
    
    public void cancel() {
      this.cancelled = true;
      this.upstream.cancel();
    }
    
    void drain() {
      if (this.wip.getAndIncrement() == 0) {
        Subscriber<? super T> subscriber = this.downstream;
        long l = this.requested.get();
        do {
          if (this.cancelled)
            return; 
          long l1 = l;
          if (this.done) {
            long l2;
            for (l2 = 0L; l2 != l; l2++) {
              if (this.cancelled)
                return; 
              T t = poll();
              if (t == null) {
                subscriber.onComplete();
                return;
              } 
              subscriber.onNext(t);
            } 
            l1 = l;
            if (l2 != 0L) {
              l1 = l;
              if (l != Long.MAX_VALUE)
                l1 = this.requested.addAndGet(-l2); 
            } 
          } 
          l = l1;
        } while (this.wip.decrementAndGet() != 0);
      } 
    }
    
    public void onComplete() {
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.count == size())
        poll(); 
      offer(param1T);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        param1Subscription.request(Long.MAX_VALUE);
      } 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.add(this.requested, param1Long);
        drain();
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableTakeLast.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */