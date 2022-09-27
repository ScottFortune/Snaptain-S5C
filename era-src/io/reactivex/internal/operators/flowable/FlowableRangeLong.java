package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.BasicQueueSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableRangeLong extends Flowable<Long> {
  final long end;
  
  final long start;
  
  public FlowableRangeLong(long paramLong1, long paramLong2) {
    this.start = paramLong1;
    this.end = paramLong1 + paramLong2;
  }
  
  public void subscribeActual(Subscriber<? super Long> paramSubscriber) {
    if (paramSubscriber instanceof ConditionalSubscriber) {
      paramSubscriber.onSubscribe((Subscription)new RangeConditionalSubscription((ConditionalSubscriber<? super Long>)paramSubscriber, this.start, this.end));
    } else {
      paramSubscriber.onSubscribe((Subscription)new RangeSubscription(paramSubscriber, this.start, this.end));
    } 
  }
  
  static abstract class BaseRangeSubscription extends BasicQueueSubscription<Long> {
    private static final long serialVersionUID = -2252972430506210021L;
    
    volatile boolean cancelled;
    
    final long end;
    
    long index;
    
    BaseRangeSubscription(long param1Long1, long param1Long2) {
      this.index = param1Long1;
      this.end = param1Long2;
    }
    
    public final void cancel() {
      this.cancelled = true;
    }
    
    public final void clear() {
      this.index = this.end;
    }
    
    abstract void fastPath();
    
    public final boolean isEmpty() {
      boolean bool;
      if (this.index == this.end) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public final Long poll() {
      long l = this.index;
      if (l == this.end)
        return null; 
      this.index = 1L + l;
      return Long.valueOf(l);
    }
    
    public final void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long) && BackpressureHelper.add((AtomicLong)this, param1Long) == 0L)
        if (param1Long == Long.MAX_VALUE) {
          fastPath();
        } else {
          slowPath(param1Long);
        }  
    }
    
    public final int requestFusion(int param1Int) {
      return param1Int & 0x1;
    }
    
    abstract void slowPath(long param1Long);
  }
  
  static final class RangeConditionalSubscription extends BaseRangeSubscription {
    private static final long serialVersionUID = 2587302975077663557L;
    
    final ConditionalSubscriber<? super Long> downstream;
    
    RangeConditionalSubscription(ConditionalSubscriber<? super Long> param1ConditionalSubscriber, long param1Long1, long param1Long2) {
      super(param1Long1, param1Long2);
      this.downstream = param1ConditionalSubscriber;
    }
    
    void fastPath() {
      long l1 = this.end;
      ConditionalSubscriber<? super Long> conditionalSubscriber = this.downstream;
      long l2;
      for (l2 = this.index; l2 != l1; l2++) {
        if (this.cancelled)
          return; 
        conditionalSubscriber.tryOnNext(Long.valueOf(l2));
      } 
      if (this.cancelled)
        return; 
      conditionalSubscriber.onComplete();
    }
    
    void slowPath(long param1Long) {
      long l3;
      long l1 = this.end;
      long l2 = this.index;
      ConditionalSubscriber<? super Long> conditionalSubscriber = this.downstream;
      do {
        l3 = 0L;
        while (true) {
          if (l3 != param1Long && l2 != l1) {
            if (this.cancelled)
              return; 
            long l4 = l3;
            if (conditionalSubscriber.tryOnNext(Long.valueOf(l2)))
              l4 = l3 + 1L; 
            l2++;
            l3 = l4;
            continue;
          } 
          if (l2 == l1) {
            if (!this.cancelled)
              conditionalSubscriber.onComplete(); 
            return;
          } 
          long l = get();
          param1Long = l;
          if (l3 == l) {
            this.index = l2;
            l3 = addAndGet(-l3);
            param1Long = l3;
            break;
          } 
        } 
      } while (l3 != 0L);
    }
  }
  
  static final class RangeSubscription extends BaseRangeSubscription {
    private static final long serialVersionUID = 2587302975077663557L;
    
    final Subscriber<? super Long> downstream;
    
    RangeSubscription(Subscriber<? super Long> param1Subscriber, long param1Long1, long param1Long2) {
      super(param1Long1, param1Long2);
      this.downstream = param1Subscriber;
    }
    
    void fastPath() {
      long l1 = this.end;
      Subscriber<? super Long> subscriber = this.downstream;
      long l2;
      for (l2 = this.index; l2 != l1; l2++) {
        if (this.cancelled)
          return; 
        subscriber.onNext(Long.valueOf(l2));
      } 
      if (this.cancelled)
        return; 
      subscriber.onComplete();
    }
    
    void slowPath(long param1Long) {
      long l3;
      long l1 = this.end;
      long l2 = this.index;
      Subscriber<? super Long> subscriber = this.downstream;
      do {
        l3 = 0L;
        while (true) {
          if (l3 != param1Long && l2 != l1) {
            if (this.cancelled)
              return; 
            subscriber.onNext(Long.valueOf(l2));
            l3++;
            l2++;
            continue;
          } 
          if (l2 == l1) {
            if (!this.cancelled)
              subscriber.onComplete(); 
            return;
          } 
          long l = get();
          param1Long = l;
          if (l3 == l) {
            this.index = l2;
            l3 = addAndGet(-l3);
            param1Long = l3;
            break;
          } 
        } 
      } while (l3 != 0L);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableRangeLong.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */