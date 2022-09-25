package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableScanSeed<T, R> extends AbstractFlowableWithUpstream<T, R> {
  final BiFunction<R, ? super T, R> accumulator;
  
  final Callable<R> seedSupplier;
  
  public FlowableScanSeed(Flowable<T> paramFlowable, Callable<R> paramCallable, BiFunction<R, ? super T, R> paramBiFunction) {
    super(paramFlowable);
    this.accumulator = paramBiFunction;
    this.seedSupplier = paramCallable;
  }
  
  protected void subscribeActual(Subscriber<? super R> paramSubscriber) {
    try {
      return;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptySubscription.error(exception, paramSubscriber);
    } 
  }
  
  static final class ScanSeedSubscriber<T, R> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = -1776795561228106469L;
    
    final BiFunction<R, ? super T, R> accumulator;
    
    volatile boolean cancelled;
    
    int consumed;
    
    volatile boolean done;
    
    final Subscriber<? super R> downstream;
    
    Throwable error;
    
    final int limit;
    
    final int prefetch;
    
    final SimplePlainQueue<R> queue;
    
    final AtomicLong requested;
    
    Subscription upstream;
    
    R value;
    
    ScanSeedSubscriber(Subscriber<? super R> param1Subscriber, BiFunction<R, ? super T, R> param1BiFunction, R param1R, int param1Int) {
      this.downstream = param1Subscriber;
      this.accumulator = param1BiFunction;
      this.value = param1R;
      this.prefetch = param1Int;
      this.limit = param1Int - (param1Int >> 2);
      this.queue = (SimplePlainQueue<R>)new SpscArrayQueue(param1Int);
      this.queue.offer(param1R);
      this.requested = new AtomicLong();
    }
    
    public void cancel() {
      this.cancelled = true;
      this.upstream.cancel();
      if (getAndIncrement() == 0)
        this.queue.clear(); 
    }
    
    void drain() {
      int m;
      if (getAndIncrement() != 0)
        return; 
      Subscriber<? super R> subscriber = this.downstream;
      SimplePlainQueue<R> simplePlainQueue = this.queue;
      int i = this.limit;
      int j = this.consumed;
      int k = 1;
      do {
        long l1 = this.requested.get();
        long l2 = 0L;
        while (l2 != l1) {
          if (this.cancelled) {
            simplePlainQueue.clear();
            return;
          } 
          boolean bool = this.done;
          if (bool) {
            Throwable throwable = this.error;
            if (throwable != null) {
              simplePlainQueue.clear();
              subscriber.onError(throwable);
              return;
            } 
          } 
          Object object = simplePlainQueue.poll();
          if (object == null) {
            n = 1;
          } else {
            n = 0;
          } 
          if (bool && n) {
            subscriber.onComplete();
            return;
          } 
          if (n)
            break; 
          subscriber.onNext(object);
          long l = l2 + 1L;
          int n = j + 1;
          j = n;
          l2 = l;
          if (n == i) {
            this.upstream.request(i);
            j = 0;
            l2 = l;
          } 
        } 
        if (l2 == l1 && this.done) {
          Throwable throwable = this.error;
          if (throwable != null) {
            simplePlainQueue.clear();
            subscriber.onError(throwable);
            return;
          } 
          if (simplePlainQueue.isEmpty()) {
            subscriber.onComplete();
            return;
          } 
        } 
        if (l2 != 0L)
          BackpressureHelper.produced(this.requested, l2); 
        this.consumed = j;
        m = addAndGet(-k);
        k = m;
      } while (m != 0);
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.error = param1Throwable;
      this.done = true;
      drain();
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      R r = this.value;
      try {
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        this.upstream.cancel();
        onError((Throwable)param1T);
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        param1Subscription.request((this.prefetch - 1));
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableScanSeed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */