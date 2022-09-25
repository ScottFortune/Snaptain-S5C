package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableThrottleLatest<T> extends AbstractFlowableWithUpstream<T, T> {
  final boolean emitLast;
  
  final Scheduler scheduler;
  
  final long timeout;
  
  final TimeUnit unit;
  
  public FlowableThrottleLatest(Flowable<T> paramFlowable, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, boolean paramBoolean) {
    super(paramFlowable);
    this.timeout = paramLong;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
    this.emitLast = paramBoolean;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new ThrottleLatestSubscriber<T>(paramSubscriber, this.timeout, this.unit, this.scheduler.createWorker(), this.emitLast));
  }
  
  static final class ThrottleLatestSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T>, Subscription, Runnable {
    private static final long serialVersionUID = -8296689127439125014L;
    
    volatile boolean cancelled;
    
    volatile boolean done;
    
    final Subscriber<? super T> downstream;
    
    final boolean emitLast;
    
    long emitted;
    
    Throwable error;
    
    final AtomicReference<T> latest;
    
    final AtomicLong requested;
    
    final long timeout;
    
    volatile boolean timerFired;
    
    boolean timerRunning;
    
    final TimeUnit unit;
    
    Subscription upstream;
    
    final Scheduler.Worker worker;
    
    ThrottleLatestSubscriber(Subscriber<? super T> param1Subscriber, long param1Long, TimeUnit param1TimeUnit, Scheduler.Worker param1Worker, boolean param1Boolean) {
      this.downstream = param1Subscriber;
      this.timeout = param1Long;
      this.unit = param1TimeUnit;
      this.worker = param1Worker;
      this.emitLast = param1Boolean;
      this.latest = new AtomicReference<T>();
      this.requested = new AtomicLong();
    }
    
    public void cancel() {
      this.cancelled = true;
      this.upstream.cancel();
      this.worker.dispose();
      if (getAndIncrement() == 0)
        this.latest.lazySet(null); 
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      AtomicReference<T> atomicReference = this.latest;
      AtomicLong atomicLong = this.requested;
      Subscriber<? super T> subscriber = this.downstream;
      int i = 1;
      while (true) {
        if (this.cancelled) {
          atomicReference.lazySet(null);
          return;
        } 
        boolean bool = this.done;
        if (bool && this.error != null) {
          atomicReference.lazySet(null);
          subscriber.onError(this.error);
          this.worker.dispose();
          return;
        } 
        if (atomicReference.get() == null) {
          j = 1;
        } else {
          j = 0;
        } 
        if (bool) {
          if (!j && this.emitLast) {
            atomicReference = (AtomicReference<T>)atomicReference.getAndSet(null);
            long l = this.emitted;
            if (l != atomicLong.get()) {
              this.emitted = l + 1L;
              subscriber.onNext(atomicReference);
              subscriber.onComplete();
            } else {
              subscriber.onError((Throwable)new MissingBackpressureException("Could not emit final value due to lack of requests"));
            } 
          } else {
            atomicReference.lazySet(null);
            subscriber.onComplete();
          } 
          this.worker.dispose();
          return;
        } 
        if (j) {
          if (this.timerFired) {
            this.timerRunning = false;
            this.timerFired = false;
          } 
        } else if (!this.timerRunning || this.timerFired) {
          T t = atomicReference.getAndSet(null);
          long l = this.emitted;
          if (l != atomicLong.get()) {
            subscriber.onNext(t);
            this.emitted = l + 1L;
            this.timerFired = false;
            this.timerRunning = true;
            this.worker.schedule(this, this.timeout, this.unit);
            continue;
          } 
          this.upstream.cancel();
          subscriber.onError((Throwable)new MissingBackpressureException("Could not emit value due to lack of requests"));
          this.worker.dispose();
          return;
        } 
        int j = addAndGet(-i);
        i = j;
        if (j == 0)
          return; 
      } 
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
      this.latest.set(param1T);
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
      if (SubscriptionHelper.validate(param1Long))
        BackpressureHelper.add(this.requested, param1Long); 
    }
    
    public void run() {
      this.timerFired = true;
      drain();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableThrottleLatest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */