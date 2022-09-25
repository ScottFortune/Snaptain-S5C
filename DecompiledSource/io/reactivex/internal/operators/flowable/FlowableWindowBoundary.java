package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.processors.UnicastProcessor;
import io.reactivex.subscribers.DisposableSubscriber;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableWindowBoundary<T, B> extends AbstractFlowableWithUpstream<T, Flowable<T>> {
  final int capacityHint;
  
  final Publisher<B> other;
  
  public FlowableWindowBoundary(Flowable<T> paramFlowable, Publisher<B> paramPublisher, int paramInt) {
    super(paramFlowable);
    this.other = paramPublisher;
    this.capacityHint = paramInt;
  }
  
  protected void subscribeActual(Subscriber<? super Flowable<T>> paramSubscriber) {
    WindowBoundaryMainSubscriber<T, Object> windowBoundaryMainSubscriber = new WindowBoundaryMainSubscriber<T, Object>(paramSubscriber, this.capacityHint);
    paramSubscriber.onSubscribe(windowBoundaryMainSubscriber);
    windowBoundaryMainSubscriber.innerNext();
    this.other.subscribe((Subscriber)windowBoundaryMainSubscriber.boundarySubscriber);
    this.source.subscribe(windowBoundaryMainSubscriber);
  }
  
  static final class WindowBoundaryInnerSubscriber<T, B> extends DisposableSubscriber<B> {
    boolean done;
    
    final FlowableWindowBoundary.WindowBoundaryMainSubscriber<T, B> parent;
    
    WindowBoundaryInnerSubscriber(FlowableWindowBoundary.WindowBoundaryMainSubscriber<T, B> param1WindowBoundaryMainSubscriber) {
      this.parent = param1WindowBoundaryMainSubscriber;
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      this.parent.innerComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.parent.innerError(param1Throwable);
    }
    
    public void onNext(B param1B) {
      if (this.done)
        return; 
      this.parent.innerNext();
    }
  }
  
  static final class WindowBoundaryMainSubscriber<T, B> extends AtomicInteger implements FlowableSubscriber<T>, Subscription, Runnable {
    static final Object NEXT_WINDOW = new Object();
    
    private static final long serialVersionUID = 2233020065421370272L;
    
    final FlowableWindowBoundary.WindowBoundaryInnerSubscriber<T, B> boundarySubscriber;
    
    final int capacityHint;
    
    volatile boolean done;
    
    final Subscriber<? super Flowable<T>> downstream;
    
    long emitted;
    
    final AtomicThrowable errors;
    
    final MpscLinkedQueue<Object> queue;
    
    final AtomicLong requested;
    
    final AtomicBoolean stopWindows;
    
    final AtomicReference<Subscription> upstream;
    
    UnicastProcessor<T> window;
    
    final AtomicInteger windows;
    
    WindowBoundaryMainSubscriber(Subscriber<? super Flowable<T>> param1Subscriber, int param1Int) {
      this.downstream = param1Subscriber;
      this.capacityHint = param1Int;
      this.boundarySubscriber = new FlowableWindowBoundary.WindowBoundaryInnerSubscriber<T, B>(this);
      this.upstream = new AtomicReference<Subscription>();
      this.windows = new AtomicInteger(1);
      this.queue = new MpscLinkedQueue();
      this.errors = new AtomicThrowable();
      this.stopWindows = new AtomicBoolean();
      this.requested = new AtomicLong();
    }
    
    public void cancel() {
      if (this.stopWindows.compareAndSet(false, true)) {
        this.boundarySubscriber.dispose();
        if (this.windows.decrementAndGet() == 0)
          SubscriptionHelper.cancel(this.upstream); 
      } 
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      Subscriber<? super Flowable<T>> subscriber = this.downstream;
      MpscLinkedQueue<Object> mpscLinkedQueue = this.queue;
      AtomicThrowable atomicThrowable = this.errors;
      long l = this.emitted;
      int i = 1;
      while (true) {
        Throwable throwable;
        int j;
        if (this.windows.get() == 0) {
          mpscLinkedQueue.clear();
          this.window = null;
          return;
        } 
        UnicastProcessor<T> unicastProcessor = this.window;
        boolean bool = this.done;
        if (bool && atomicThrowable.get() != null) {
          mpscLinkedQueue.clear();
          throwable = atomicThrowable.terminate();
          if (unicastProcessor != null) {
            this.window = null;
            unicastProcessor.onError(throwable);
          } 
          subscriber.onError(throwable);
          return;
        } 
        Object object = mpscLinkedQueue.poll();
        if (object == null) {
          j = 1;
        } else {
          j = 0;
        } 
        if (bool && j) {
          throwable = throwable.terminate();
          if (throwable == null) {
            if (unicastProcessor != null) {
              this.window = null;
              unicastProcessor.onComplete();
            } 
            subscriber.onComplete();
          } else {
            if (unicastProcessor != null) {
              this.window = null;
              unicastProcessor.onError(throwable);
            } 
            subscriber.onError(throwable);
          } 
          return;
        } 
        if (j) {
          this.emitted = l;
          j = addAndGet(-i);
          i = j;
          if (j == 0)
            return; 
          continue;
        } 
        if (object != NEXT_WINDOW) {
          unicastProcessor.onNext(object);
          continue;
        } 
        if (unicastProcessor != null) {
          this.window = null;
          unicastProcessor.onComplete();
        } 
        if (!this.stopWindows.get()) {
          unicastProcessor = UnicastProcessor.create(this.capacityHint, this);
          this.window = unicastProcessor;
          this.windows.getAndIncrement();
          if (l != this.requested.get()) {
            l++;
            subscriber.onNext(unicastProcessor);
            continue;
          } 
          SubscriptionHelper.cancel(this.upstream);
          this.boundarySubscriber.dispose();
          throwable.addThrowable((Throwable)new MissingBackpressureException("Could not deliver a window due to lack of requests"));
          this.done = true;
        } 
      } 
    }
    
    void innerComplete() {
      SubscriptionHelper.cancel(this.upstream);
      this.done = true;
      drain();
    }
    
    void innerError(Throwable param1Throwable) {
      SubscriptionHelper.cancel(this.upstream);
      if (this.errors.addThrowable(param1Throwable)) {
        this.done = true;
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    void innerNext() {
      this.queue.offer(NEXT_WINDOW);
      drain();
    }
    
    public void onComplete() {
      this.boundarySubscriber.dispose();
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      this.boundarySubscriber.dispose();
      if (this.errors.addThrowable(param1Throwable)) {
        this.done = true;
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      this.queue.offer(param1T);
      drain();
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.setOnce(this.upstream, param1Subscription, Long.MAX_VALUE);
    }
    
    public void request(long param1Long) {
      BackpressureHelper.add(this.requested, param1Long);
    }
    
    public void run() {
      if (this.windows.decrementAndGet() == 0)
        SubscriptionHelper.cancel(this.upstream); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableWindowBoundary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */