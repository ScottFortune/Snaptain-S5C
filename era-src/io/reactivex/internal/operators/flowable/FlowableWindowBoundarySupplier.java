package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.processors.UnicastProcessor;
import io.reactivex.subscribers.DisposableSubscriber;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableWindowBoundarySupplier<T, B> extends AbstractFlowableWithUpstream<T, Flowable<T>> {
  final int capacityHint;
  
  final Callable<? extends Publisher<B>> other;
  
  public FlowableWindowBoundarySupplier(Flowable<T> paramFlowable, Callable<? extends Publisher<B>> paramCallable, int paramInt) {
    super(paramFlowable);
    this.other = paramCallable;
    this.capacityHint = paramInt;
  }
  
  protected void subscribeActual(Subscriber<? super Flowable<T>> paramSubscriber) {
    WindowBoundaryMainSubscriber<T, B> windowBoundaryMainSubscriber = new WindowBoundaryMainSubscriber<T, B>(paramSubscriber, this.capacityHint, this.other);
    this.source.subscribe(windowBoundaryMainSubscriber);
  }
  
  static final class WindowBoundaryInnerSubscriber<T, B> extends DisposableSubscriber<B> {
    boolean done;
    
    final FlowableWindowBoundarySupplier.WindowBoundaryMainSubscriber<T, B> parent;
    
    WindowBoundaryInnerSubscriber(FlowableWindowBoundarySupplier.WindowBoundaryMainSubscriber<T, B> param1WindowBoundaryMainSubscriber) {
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
      this.done = true;
      dispose();
      this.parent.innerNext(this);
    }
  }
  
  static final class WindowBoundaryMainSubscriber<T, B> extends AtomicInteger implements FlowableSubscriber<T>, Subscription, Runnable {
    static final FlowableWindowBoundarySupplier.WindowBoundaryInnerSubscriber<Object, Object> BOUNDARY_DISPOSED = new FlowableWindowBoundarySupplier.WindowBoundaryInnerSubscriber<Object, Object>(null);
    
    static final Object NEXT_WINDOW = new Object();
    
    private static final long serialVersionUID = 2233020065421370272L;
    
    final AtomicReference<FlowableWindowBoundarySupplier.WindowBoundaryInnerSubscriber<T, B>> boundarySubscriber;
    
    final int capacityHint;
    
    volatile boolean done;
    
    final Subscriber<? super Flowable<T>> downstream;
    
    long emitted;
    
    final AtomicThrowable errors;
    
    final Callable<? extends Publisher<B>> other;
    
    final MpscLinkedQueue<Object> queue;
    
    final AtomicLong requested;
    
    final AtomicBoolean stopWindows;
    
    Subscription upstream;
    
    UnicastProcessor<T> window;
    
    final AtomicInteger windows;
    
    WindowBoundaryMainSubscriber(Subscriber<? super Flowable<T>> param1Subscriber, int param1Int, Callable<? extends Publisher<B>> param1Callable) {
      this.downstream = param1Subscriber;
      this.capacityHint = param1Int;
      this.boundarySubscriber = new AtomicReference<FlowableWindowBoundarySupplier.WindowBoundaryInnerSubscriber<T, B>>();
      this.windows = new AtomicInteger(1);
      this.queue = new MpscLinkedQueue();
      this.errors = new AtomicThrowable();
      this.stopWindows = new AtomicBoolean();
      this.other = param1Callable;
      this.requested = new AtomicLong();
    }
    
    public void cancel() {
      if (this.stopWindows.compareAndSet(false, true)) {
        disposeBoundary();
        if (this.windows.decrementAndGet() == 0)
          this.upstream.cancel(); 
      } 
    }
    
    void disposeBoundary() {
      Disposable disposable = (Disposable)this.boundarySubscriber.getAndSet(BOUNDARY_DISPOSED);
      if (disposable != null && disposable != BOUNDARY_DISPOSED)
        disposable.dispose(); 
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
        Object<Object, Object> object = (Object<Object, Object>)throwable.poll();
        if (object == null) {
          j = 1;
        } else {
          j = 0;
        } 
        if (bool && j) {
          throwable = atomicThrowable.terminate();
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
          if (l != this.requested.get()) {
            UnicastProcessor<T> unicastProcessor1 = UnicastProcessor.create(this.capacityHint, this);
            this.window = unicastProcessor1;
            this.windows.getAndIncrement();
            try {
            
            } finally {
              unicastProcessor = null;
              Exceptions.throwIfFatal((Throwable)unicastProcessor);
              atomicThrowable.addThrowable((Throwable)unicastProcessor);
            } 
            continue;
          } 
          this.upstream.cancel();
          disposeBoundary();
          atomicThrowable.addThrowable((Throwable)new MissingBackpressureException("Could not deliver a window due to lack of requests"));
          this.done = true;
        } 
      } 
    }
    
    void innerComplete() {
      this.upstream.cancel();
      this.done = true;
      drain();
    }
    
    void innerError(Throwable param1Throwable) {
      this.upstream.cancel();
      if (this.errors.addThrowable(param1Throwable)) {
        this.done = true;
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    void innerNext(FlowableWindowBoundarySupplier.WindowBoundaryInnerSubscriber<T, B> param1WindowBoundaryInnerSubscriber) {
      this.boundarySubscriber.compareAndSet(param1WindowBoundaryInnerSubscriber, null);
      this.queue.offer(NEXT_WINDOW);
      drain();
    }
    
    public void onComplete() {
      disposeBoundary();
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      disposeBoundary();
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
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        this.queue.offer(NEXT_WINDOW);
        drain();
        param1Subscription.request(Long.MAX_VALUE);
      } 
    }
    
    public void request(long param1Long) {
      BackpressureHelper.add(this.requested, param1Long);
    }
    
    public void run() {
      if (this.windows.decrementAndGet() == 0)
        this.upstream.cancel(); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableWindowBoundarySupplier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */