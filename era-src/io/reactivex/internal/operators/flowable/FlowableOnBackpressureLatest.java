package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableOnBackpressureLatest<T> extends AbstractFlowableWithUpstream<T, T> {
  public FlowableOnBackpressureLatest(Flowable<T> paramFlowable) {
    super(paramFlowable);
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new BackpressureLatestSubscriber<T>(paramSubscriber));
  }
  
  static final class BackpressureLatestSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = 163080509307634843L;
    
    volatile boolean cancelled;
    
    final AtomicReference<T> current = new AtomicReference<T>();
    
    volatile boolean done;
    
    final Subscriber<? super T> downstream;
    
    Throwable error;
    
    final AtomicLong requested = new AtomicLong();
    
    Subscription upstream;
    
    BackpressureLatestSubscriber(Subscriber<? super T> param1Subscriber) {
      this.downstream = param1Subscriber;
    }
    
    public void cancel() {
      if (!this.cancelled) {
        this.cancelled = true;
        this.upstream.cancel();
        if (getAndIncrement() == 0)
          this.current.lazySet(null); 
      } 
    }
    
    boolean checkTerminated(boolean param1Boolean1, boolean param1Boolean2, Subscriber<?> param1Subscriber, AtomicReference<T> param1AtomicReference) {
      if (this.cancelled) {
        param1AtomicReference.lazySet(null);
        return true;
      } 
      if (param1Boolean1) {
        Throwable throwable = this.error;
        if (throwable != null) {
          param1AtomicReference.lazySet(null);
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
      AtomicLong atomicLong = this.requested;
      AtomicReference<T> atomicReference = this.current;
      int i = 1;
      do {
        boolean bool;
        long l = 0L;
        while (true) {
          long l1 = atomicLong.get();
          bool = false;
          if (l != l1) {
            boolean bool2;
            boolean bool1 = this.done;
            T t = atomicReference.getAndSet(null);
            if (t == null) {
              bool2 = true;
            } else {
              bool2 = false;
            } 
            if (checkTerminated(bool1, bool2, subscriber, atomicReference))
              return; 
            if (bool2)
              break; 
            subscriber.onNext(t);
            l++;
            continue;
          } 
          break;
        } 
        if (l == atomicLong.get()) {
          boolean bool1 = this.done;
          boolean bool2 = bool;
          if (atomicReference.get() == null)
            bool2 = true; 
          if (checkTerminated(bool1, bool2, subscriber, atomicReference))
            return; 
        } 
        if (l != 0L)
          BackpressureHelper.produced(atomicLong, l); 
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
      this.current.lazySet(param1T);
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableOnBackpressureLatest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */