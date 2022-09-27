package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.BasicQueueSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableRange extends Flowable<Integer> {
  final int end;
  
  final int start;
  
  public FlowableRange(int paramInt1, int paramInt2) {
    this.start = paramInt1;
    this.end = paramInt1 + paramInt2;
  }
  
  public void subscribeActual(Subscriber<? super Integer> paramSubscriber) {
    if (paramSubscriber instanceof ConditionalSubscriber) {
      paramSubscriber.onSubscribe((Subscription)new RangeConditionalSubscription((ConditionalSubscriber<? super Integer>)paramSubscriber, this.start, this.end));
    } else {
      paramSubscriber.onSubscribe((Subscription)new RangeSubscription(paramSubscriber, this.start, this.end));
    } 
  }
  
  static abstract class BaseRangeSubscription extends BasicQueueSubscription<Integer> {
    private static final long serialVersionUID = -2252972430506210021L;
    
    volatile boolean cancelled;
    
    final int end;
    
    int index;
    
    BaseRangeSubscription(int param1Int1, int param1Int2) {
      this.index = param1Int1;
      this.end = param1Int2;
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
    
    public final Integer poll() {
      int i = this.index;
      if (i == this.end)
        return null; 
      this.index = i + 1;
      return Integer.valueOf(i);
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
    
    final ConditionalSubscriber<? super Integer> downstream;
    
    RangeConditionalSubscription(ConditionalSubscriber<? super Integer> param1ConditionalSubscriber, int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
      this.downstream = param1ConditionalSubscriber;
    }
    
    void fastPath() {
      int i = this.end;
      ConditionalSubscriber<? super Integer> conditionalSubscriber = this.downstream;
      for (int j = this.index; j != i; j++) {
        if (this.cancelled)
          return; 
        conditionalSubscriber.tryOnNext(Integer.valueOf(j));
      } 
      if (this.cancelled)
        return; 
      conditionalSubscriber.onComplete();
    }
    
    void slowPath(long param1Long) {
      long l;
      int i = this.end;
      int j = this.index;
      ConditionalSubscriber<? super Integer> conditionalSubscriber = this.downstream;
      do {
        l = 0L;
        while (true) {
          if (l != param1Long && j != i) {
            if (this.cancelled)
              return; 
            long l2 = l;
            if (conditionalSubscriber.tryOnNext(Integer.valueOf(j)))
              l2 = l + 1L; 
            j++;
            l = l2;
            continue;
          } 
          if (j == i) {
            if (!this.cancelled)
              conditionalSubscriber.onComplete(); 
            return;
          } 
          long l1 = get();
          param1Long = l1;
          if (l == l1) {
            this.index = j;
            l = addAndGet(-l);
            param1Long = l;
            break;
          } 
        } 
      } while (l != 0L);
    }
  }
  
  static final class RangeSubscription extends BaseRangeSubscription {
    private static final long serialVersionUID = 2587302975077663557L;
    
    final Subscriber<? super Integer> downstream;
    
    RangeSubscription(Subscriber<? super Integer> param1Subscriber, int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
      this.downstream = param1Subscriber;
    }
    
    void fastPath() {
      int i = this.end;
      Subscriber<? super Integer> subscriber = this.downstream;
      for (int j = this.index; j != i; j++) {
        if (this.cancelled)
          return; 
        subscriber.onNext(Integer.valueOf(j));
      } 
      if (this.cancelled)
        return; 
      subscriber.onComplete();
    }
    
    void slowPath(long param1Long) {
      long l;
      int i = this.end;
      int j = this.index;
      Subscriber<? super Integer> subscriber = this.downstream;
      do {
        l = 0L;
        while (true) {
          if (l != param1Long && j != i) {
            if (this.cancelled)
              return; 
            subscriber.onNext(Integer.valueOf(j));
            l++;
            j++;
            continue;
          } 
          if (j == i) {
            if (!this.cancelled)
              subscriber.onComplete(); 
            return;
          } 
          long l1 = get();
          param1Long = l1;
          if (l == l1) {
            this.index = j;
            l = addAndGet(-l);
            param1Long = l;
            break;
          } 
        } 
      } while (l != 0L);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableRange.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */