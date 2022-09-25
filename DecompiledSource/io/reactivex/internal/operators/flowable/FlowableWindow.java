package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.processors.UnicastProcessor;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Processor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableWindow<T> extends AbstractFlowableWithUpstream<T, Flowable<T>> {
  final int bufferSize;
  
  final long size;
  
  final long skip;
  
  public FlowableWindow(Flowable<T> paramFlowable, long paramLong1, long paramLong2, int paramInt) {
    super(paramFlowable);
    this.size = paramLong1;
    this.skip = paramLong2;
    this.bufferSize = paramInt;
  }
  
  public void subscribeActual(Subscriber<? super Flowable<T>> paramSubscriber) {
    long l1 = this.skip;
    long l2 = this.size;
    if (l1 == l2) {
      this.source.subscribe(new WindowExactSubscriber<T>(paramSubscriber, this.size, this.bufferSize));
    } else if (l1 > l2) {
      this.source.subscribe(new WindowSkipSubscriber<T>(paramSubscriber, this.size, this.skip, this.bufferSize));
    } else {
      this.source.subscribe(new WindowOverlapSubscriber<T>(paramSubscriber, this.size, this.skip, this.bufferSize));
    } 
  }
  
  static final class WindowExactSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T>, Subscription, Runnable {
    private static final long serialVersionUID = -2365647875069161133L;
    
    final int bufferSize;
    
    final Subscriber<? super Flowable<T>> downstream;
    
    long index;
    
    final AtomicBoolean once;
    
    final long size;
    
    Subscription upstream;
    
    UnicastProcessor<T> window;
    
    WindowExactSubscriber(Subscriber<? super Flowable<T>> param1Subscriber, long param1Long, int param1Int) {
      super(1);
      this.downstream = param1Subscriber;
      this.size = param1Long;
      this.once = new AtomicBoolean();
      this.bufferSize = param1Int;
    }
    
    public void cancel() {
      if (this.once.compareAndSet(false, true))
        run(); 
    }
    
    public void onComplete() {
      UnicastProcessor<T> unicastProcessor = this.window;
      if (unicastProcessor != null) {
        this.window = null;
        unicastProcessor.onComplete();
      } 
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      UnicastProcessor<T> unicastProcessor = this.window;
      if (unicastProcessor != null) {
        this.window = null;
        unicastProcessor.onError(param1Throwable);
      } 
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      long l = this.index;
      UnicastProcessor<T> unicastProcessor = this.window;
      if (l == 0L) {
        getAndIncrement();
        unicastProcessor = UnicastProcessor.create(this.bufferSize, this);
        this.window = unicastProcessor;
        this.downstream.onNext(unicastProcessor);
      } 
      l++;
      unicastProcessor.onNext(param1T);
      if (l == this.size) {
        this.index = 0L;
        this.window = null;
        unicastProcessor.onComplete();
      } else {
        this.index = l;
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        param1Long = BackpressureHelper.multiplyCap(this.size, param1Long);
        this.upstream.request(param1Long);
      } 
    }
    
    public void run() {
      if (decrementAndGet() == 0)
        this.upstream.cancel(); 
    }
  }
  
  static final class WindowOverlapSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T>, Subscription, Runnable {
    private static final long serialVersionUID = 2428527070996323976L;
    
    final int bufferSize;
    
    volatile boolean cancelled;
    
    volatile boolean done;
    
    final Subscriber<? super Flowable<T>> downstream;
    
    Throwable error;
    
    final AtomicBoolean firstRequest;
    
    long index;
    
    final AtomicBoolean once;
    
    long produced;
    
    final SpscLinkedArrayQueue<UnicastProcessor<T>> queue;
    
    final AtomicLong requested;
    
    final long size;
    
    final long skip;
    
    Subscription upstream;
    
    final ArrayDeque<UnicastProcessor<T>> windows;
    
    final AtomicInteger wip;
    
    WindowOverlapSubscriber(Subscriber<? super Flowable<T>> param1Subscriber, long param1Long1, long param1Long2, int param1Int) {
      super(1);
      this.downstream = param1Subscriber;
      this.size = param1Long1;
      this.skip = param1Long2;
      this.queue = new SpscLinkedArrayQueue(param1Int);
      this.windows = new ArrayDeque<UnicastProcessor<T>>();
      this.once = new AtomicBoolean();
      this.firstRequest = new AtomicBoolean();
      this.requested = new AtomicLong();
      this.wip = new AtomicInteger();
      this.bufferSize = param1Int;
    }
    
    public void cancel() {
      this.cancelled = true;
      if (this.once.compareAndSet(false, true))
        run(); 
    }
    
    boolean checkTerminated(boolean param1Boolean1, boolean param1Boolean2, Subscriber<?> param1Subscriber, SpscLinkedArrayQueue<?> param1SpscLinkedArrayQueue) {
      if (this.cancelled) {
        param1SpscLinkedArrayQueue.clear();
        return true;
      } 
      if (param1Boolean1) {
        Throwable throwable = this.error;
        if (throwable != null) {
          param1SpscLinkedArrayQueue.clear();
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
      if (this.wip.getAndIncrement() != 0)
        return; 
      Subscriber<? super Flowable<T>> subscriber = this.downstream;
      SpscLinkedArrayQueue<UnicastProcessor<T>> spscLinkedArrayQueue = this.queue;
      int i = 1;
      do {
        long l1 = this.requested.get();
        long l2;
        for (l2 = 0L; l2 != l1; l2++) {
          boolean bool1;
          boolean bool = this.done;
          UnicastProcessor unicastProcessor = (UnicastProcessor)spscLinkedArrayQueue.poll();
          if (unicastProcessor == null) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          if (checkTerminated(bool, bool1, subscriber, spscLinkedArrayQueue))
            return; 
          if (bool1)
            break; 
          subscriber.onNext(unicastProcessor);
        } 
        if (l2 == l1 && checkTerminated(this.done, spscLinkedArrayQueue.isEmpty(), subscriber, spscLinkedArrayQueue))
          return; 
        if (l2 != 0L && l1 != Long.MAX_VALUE)
          this.requested.addAndGet(-l2); 
        j = this.wip.addAndGet(-i);
        i = j;
      } while (j != 0);
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      Iterator<UnicastProcessor<T>> iterator = this.windows.iterator();
      while (iterator.hasNext())
        ((Processor)iterator.next()).onComplete(); 
      this.windows.clear();
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      Iterator<UnicastProcessor<T>> iterator = this.windows.iterator();
      while (iterator.hasNext())
        ((Processor)iterator.next()).onError(param1Throwable); 
      this.windows.clear();
      this.error = param1Throwable;
      this.done = true;
      drain();
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      long l1 = this.index;
      if (l1 == 0L && !this.cancelled) {
        getAndIncrement();
        UnicastProcessor<T> unicastProcessor = UnicastProcessor.create(this.bufferSize, this);
        this.windows.offer(unicastProcessor);
        this.queue.offer(unicastProcessor);
        drain();
      } 
      long l2 = l1 + 1L;
      Iterator<UnicastProcessor<T>> iterator = this.windows.iterator();
      while (iterator.hasNext())
        ((Processor)iterator.next()).onNext(param1T); 
      l1 = this.produced + 1L;
      if (l1 == this.size) {
        this.produced = l1 - this.skip;
        Processor processor = (Processor)this.windows.poll();
        if (processor != null)
          processor.onComplete(); 
      } else {
        this.produced = l1;
      } 
      if (l2 == this.skip) {
        this.index = 0L;
      } else {
        this.index = l2;
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.add(this.requested, param1Long);
        if (!this.firstRequest.get() && this.firstRequest.compareAndSet(false, true)) {
          param1Long = BackpressureHelper.multiplyCap(this.skip, param1Long - 1L);
          param1Long = BackpressureHelper.addCap(this.size, param1Long);
          this.upstream.request(param1Long);
        } else {
          param1Long = BackpressureHelper.multiplyCap(this.skip, param1Long);
          this.upstream.request(param1Long);
        } 
        drain();
      } 
    }
    
    public void run() {
      if (decrementAndGet() == 0)
        this.upstream.cancel(); 
    }
  }
  
  static final class WindowSkipSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T>, Subscription, Runnable {
    private static final long serialVersionUID = -8792836352386833856L;
    
    final int bufferSize;
    
    final Subscriber<? super Flowable<T>> downstream;
    
    final AtomicBoolean firstRequest;
    
    long index;
    
    final AtomicBoolean once;
    
    final long size;
    
    final long skip;
    
    Subscription upstream;
    
    UnicastProcessor<T> window;
    
    WindowSkipSubscriber(Subscriber<? super Flowable<T>> param1Subscriber, long param1Long1, long param1Long2, int param1Int) {
      super(1);
      this.downstream = param1Subscriber;
      this.size = param1Long1;
      this.skip = param1Long2;
      this.once = new AtomicBoolean();
      this.firstRequest = new AtomicBoolean();
      this.bufferSize = param1Int;
    }
    
    public void cancel() {
      if (this.once.compareAndSet(false, true))
        run(); 
    }
    
    public void onComplete() {
      UnicastProcessor<T> unicastProcessor = this.window;
      if (unicastProcessor != null) {
        this.window = null;
        unicastProcessor.onComplete();
      } 
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      UnicastProcessor<T> unicastProcessor = this.window;
      if (unicastProcessor != null) {
        this.window = null;
        unicastProcessor.onError(param1Throwable);
      } 
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      long l = this.index;
      UnicastProcessor<T> unicastProcessor = this.window;
      if (l == 0L) {
        getAndIncrement();
        unicastProcessor = UnicastProcessor.create(this.bufferSize, this);
        this.window = unicastProcessor;
        this.downstream.onNext(unicastProcessor);
      } 
      l++;
      if (unicastProcessor != null)
        unicastProcessor.onNext(param1T); 
      if (l == this.size) {
        this.window = null;
        unicastProcessor.onComplete();
      } 
      if (l == this.skip) {
        this.index = 0L;
      } else {
        this.index = l;
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long))
        if (!this.firstRequest.get() && this.firstRequest.compareAndSet(false, true)) {
          param1Long = BackpressureHelper.addCap(BackpressureHelper.multiplyCap(this.size, param1Long), BackpressureHelper.multiplyCap(this.skip - this.size, param1Long - 1L));
          this.upstream.request(param1Long);
        } else {
          param1Long = BackpressureHelper.multiplyCap(this.skip, param1Long);
          this.upstream.request(param1Long);
        }  
    }
    
    public void run() {
      if (decrementAndGet() == 0)
        this.upstream.cancel(); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableWindow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */