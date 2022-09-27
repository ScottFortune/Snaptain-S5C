package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableSkipLastTimed<T> extends AbstractFlowableWithUpstream<T, T> {
  final int bufferSize;
  
  final boolean delayError;
  
  final Scheduler scheduler;
  
  final long time;
  
  final TimeUnit unit;
  
  public FlowableSkipLastTimed(Flowable<T> paramFlowable, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, int paramInt, boolean paramBoolean) {
    super(paramFlowable);
    this.time = paramLong;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
    this.bufferSize = paramInt;
    this.delayError = paramBoolean;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new SkipLastTimedSubscriber<T>(paramSubscriber, this.time, this.unit, this.scheduler, this.bufferSize, this.delayError));
  }
  
  static final class SkipLastTimedSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = -5677354903406201275L;
    
    volatile boolean cancelled;
    
    final boolean delayError;
    
    volatile boolean done;
    
    final Subscriber<? super T> downstream;
    
    Throwable error;
    
    final SpscLinkedArrayQueue<Object> queue;
    
    final AtomicLong requested = new AtomicLong();
    
    final Scheduler scheduler;
    
    final long time;
    
    final TimeUnit unit;
    
    Subscription upstream;
    
    SkipLastTimedSubscriber(Subscriber<? super T> param1Subscriber, long param1Long, TimeUnit param1TimeUnit, Scheduler param1Scheduler, int param1Int, boolean param1Boolean) {
      this.downstream = param1Subscriber;
      this.time = param1Long;
      this.unit = param1TimeUnit;
      this.scheduler = param1Scheduler;
      this.queue = new SpscLinkedArrayQueue(param1Int);
      this.delayError = param1Boolean;
    }
    
    public void cancel() {
      if (!this.cancelled) {
        this.cancelled = true;
        this.upstream.cancel();
        if (getAndIncrement() == 0)
          this.queue.clear(); 
      } 
    }
    
    boolean checkTerminated(boolean param1Boolean1, boolean param1Boolean2, Subscriber<? super T> param1Subscriber, boolean param1Boolean3) {
      if (this.cancelled) {
        this.queue.clear();
        return true;
      } 
      if (param1Boolean1)
        if (param1Boolean3) {
          if (param1Boolean2) {
            Throwable throwable = this.error;
            if (throwable != null) {
              param1Subscriber.onError(throwable);
            } else {
              param1Subscriber.onComplete();
            } 
            return true;
          } 
        } else {
          Throwable throwable = this.error;
          if (throwable != null) {
            this.queue.clear();
            param1Subscriber.onError(throwable);
            return true;
          } 
          if (param1Boolean2) {
            param1Subscriber.onComplete();
            return true;
          } 
        }  
      return false;
    }
    
    void drain() {
      int j;
      if (getAndIncrement() != 0)
        return; 
      Subscriber<? super T> subscriber = this.downstream;
      SpscLinkedArrayQueue<Object> spscLinkedArrayQueue = this.queue;
      boolean bool = this.delayError;
      TimeUnit timeUnit = this.unit;
      Scheduler scheduler = this.scheduler;
      long l = this.time;
      int i = 1;
      do {
        long l1 = this.requested.get();
        long l2;
        for (l2 = 0L; l2 != l1; l2++) {
          boolean bool2;
          boolean bool1 = this.done;
          Long long_ = (Long)spscLinkedArrayQueue.peek();
          if (long_ == null) {
            bool2 = true;
          } else {
            bool2 = false;
          } 
          long l3 = scheduler.now(timeUnit);
          if (!bool2 && long_.longValue() > l3 - l)
            bool2 = true; 
          if (checkTerminated(bool1, bool2, subscriber, bool))
            return; 
          if (bool2)
            break; 
          spscLinkedArrayQueue.poll();
          subscriber.onNext(spscLinkedArrayQueue.poll());
        } 
        if (l2 != 0L)
          BackpressureHelper.produced(this.requested, l2); 
        j = addAndGet(-i);
        i = j;
      } while (j != 0);
    }
    
    public void onComplete() {
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      this.error = param1Throwable;
      this.done = true;
      drain();
    }
    
    public void onNext(T param1T) {
      long l = this.scheduler.now(this.unit);
      this.queue.offer(Long.valueOf(l), param1T);
      drain();
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableSkipLastTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */