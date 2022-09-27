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

public final class FlowableTakeLastTimed<T> extends AbstractFlowableWithUpstream<T, T> {
  final int bufferSize;
  
  final long count;
  
  final boolean delayError;
  
  final Scheduler scheduler;
  
  final long time;
  
  final TimeUnit unit;
  
  public FlowableTakeLastTimed(Flowable<T> paramFlowable, long paramLong1, long paramLong2, TimeUnit paramTimeUnit, Scheduler paramScheduler, int paramInt, boolean paramBoolean) {
    super(paramFlowable);
    this.count = paramLong1;
    this.time = paramLong2;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
    this.bufferSize = paramInt;
    this.delayError = paramBoolean;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new TakeLastTimedSubscriber<T>(paramSubscriber, this.count, this.time, this.unit, this.scheduler, this.bufferSize, this.delayError));
  }
  
  static final class TakeLastTimedSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = -5677354903406201275L;
    
    volatile boolean cancelled;
    
    final long count;
    
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
    
    TakeLastTimedSubscriber(Subscriber<? super T> param1Subscriber, long param1Long1, long param1Long2, TimeUnit param1TimeUnit, Scheduler param1Scheduler, int param1Int, boolean param1Boolean) {
      this.downstream = param1Subscriber;
      this.count = param1Long1;
      this.time = param1Long2;
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
    
    boolean checkTerminated(boolean param1Boolean1, Subscriber<? super T> param1Subscriber, boolean param1Boolean2) {
      if (this.cancelled) {
        this.queue.clear();
        return true;
      } 
      if (param1Boolean2) {
        if (param1Boolean1) {
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
        if (param1Boolean1) {
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
      int i = 1;
      do {
        if (this.done) {
          if (checkTerminated(spscLinkedArrayQueue.isEmpty(), subscriber, bool))
            return; 
          long l1 = this.requested.get();
          long l2;
          for (l2 = 0L;; l2++) {
            boolean bool1;
            if (spscLinkedArrayQueue.peek() == null) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            if (checkTerminated(bool1, subscriber, bool))
              return; 
            if (l1 == l2) {
              if (l2 != 0L)
                BackpressureHelper.produced(this.requested, l2); 
              break;
            } 
            spscLinkedArrayQueue.poll();
            subscriber.onNext(spscLinkedArrayQueue.poll());
          } 
        } 
        j = addAndGet(-i);
        i = j;
      } while (j != 0);
    }
    
    public void onComplete() {
      trim(this.scheduler.now(this.unit), this.queue);
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.delayError)
        trim(this.scheduler.now(this.unit), this.queue); 
      this.error = param1Throwable;
      this.done = true;
      drain();
    }
    
    public void onNext(T param1T) {
      SpscLinkedArrayQueue<Object> spscLinkedArrayQueue = this.queue;
      long l = this.scheduler.now(this.unit);
      spscLinkedArrayQueue.offer(Long.valueOf(l), param1T);
      trim(l, spscLinkedArrayQueue);
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
    
    void trim(long param1Long, SpscLinkedArrayQueue<Object> param1SpscLinkedArrayQueue) {
      boolean bool;
      long l1 = this.time;
      long l2 = this.count;
      if (l2 == Long.MAX_VALUE) {
        bool = true;
      } else {
        bool = false;
      } 
      while (!param1SpscLinkedArrayQueue.isEmpty() && (((Long)param1SpscLinkedArrayQueue.peek()).longValue() < param1Long - l1 || (!bool && (param1SpscLinkedArrayQueue.size() >> 1) > l2))) {
        param1SpscLinkedArrayQueue.poll();
        param1SpscLinkedArrayQueue.poll();
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableTakeLastTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */