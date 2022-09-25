package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionArbiter;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.ErrorMode;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableConcatMap<T, R> extends AbstractFlowableWithUpstream<T, R> {
  final ErrorMode errorMode;
  
  final Function<? super T, ? extends Publisher<? extends R>> mapper;
  
  final int prefetch;
  
  public FlowableConcatMap(Flowable<T> paramFlowable, Function<? super T, ? extends Publisher<? extends R>> paramFunction, int paramInt, ErrorMode paramErrorMode) {
    super(paramFlowable);
    this.mapper = paramFunction;
    this.prefetch = paramInt;
    this.errorMode = paramErrorMode;
  }
  
  public static <T, R> Subscriber<T> subscribe(Subscriber<? super R> paramSubscriber, Function<? super T, ? extends Publisher<? extends R>> paramFunction, int paramInt, ErrorMode paramErrorMode) {
    int i = null.$SwitchMap$io$reactivex$internal$util$ErrorMode[paramErrorMode.ordinal()];
    return (Subscriber<T>)((i != 1) ? ((i != 2) ? new ConcatMapImmediate<T, R>(paramSubscriber, paramFunction, paramInt) : new ConcatMapDelayed<T, R>(paramSubscriber, paramFunction, paramInt, true)) : new ConcatMapDelayed<T, R>(paramSubscriber, paramFunction, paramInt, false));
  }
  
  protected void subscribeActual(Subscriber<? super R> paramSubscriber) {
    if (FlowableScalarXMap.tryScalarXMapSubscribe((Publisher<T>)this.source, paramSubscriber, this.mapper))
      return; 
    this.source.subscribe(subscribe(paramSubscriber, this.mapper, this.prefetch, this.errorMode));
  }
  
  static abstract class BaseConcatMapSubscriber<T, R> extends AtomicInteger implements FlowableSubscriber<T>, ConcatMapSupport<R>, Subscription {
    private static final long serialVersionUID = -3511336836796789179L;
    
    volatile boolean active;
    
    volatile boolean cancelled;
    
    int consumed;
    
    volatile boolean done;
    
    final AtomicThrowable errors;
    
    final FlowableConcatMap.ConcatMapInner<R> inner;
    
    final int limit;
    
    final Function<? super T, ? extends Publisher<? extends R>> mapper;
    
    final int prefetch;
    
    SimpleQueue<T> queue;
    
    int sourceMode;
    
    Subscription upstream;
    
    BaseConcatMapSubscriber(Function<? super T, ? extends Publisher<? extends R>> param1Function, int param1Int) {
      this.mapper = param1Function;
      this.prefetch = param1Int;
      this.limit = param1Int - (param1Int >> 2);
      this.inner = new FlowableConcatMap.ConcatMapInner<R>(this);
      this.errors = new AtomicThrowable();
    }
    
    abstract void drain();
    
    public final void innerComplete() {
      this.active = false;
      drain();
    }
    
    public final void onComplete() {
      this.done = true;
      drain();
    }
    
    public final void onNext(T param1T) {
      if (this.sourceMode != 2 && !this.queue.offer(param1T)) {
        this.upstream.cancel();
        onError(new IllegalStateException("Queue full?!"));
        return;
      } 
      drain();
    }
    
    public final void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        if (param1Subscription instanceof QueueSubscription) {
          QueueSubscription queueSubscription = (QueueSubscription)param1Subscription;
          int i = queueSubscription.requestFusion(7);
          if (i == 1) {
            this.sourceMode = i;
            this.queue = (SimpleQueue<T>)queueSubscription;
            this.done = true;
            subscribeActual();
            drain();
            return;
          } 
          if (i == 2) {
            this.sourceMode = i;
            this.queue = (SimpleQueue<T>)queueSubscription;
            subscribeActual();
            param1Subscription.request(this.prefetch);
            return;
          } 
        } 
        this.queue = (SimpleQueue<T>)new SpscArrayQueue(this.prefetch);
        subscribeActual();
        param1Subscription.request(this.prefetch);
      } 
    }
    
    abstract void subscribeActual();
  }
  
  static final class ConcatMapDelayed<T, R> extends BaseConcatMapSubscriber<T, R> {
    private static final long serialVersionUID = -2945777694260521066L;
    
    final Subscriber<? super R> downstream;
    
    final boolean veryEnd;
    
    ConcatMapDelayed(Subscriber<? super R> param1Subscriber, Function<? super T, ? extends Publisher<? extends R>> param1Function, int param1Int, boolean param1Boolean) {
      super(param1Function, param1Int);
      this.downstream = param1Subscriber;
      this.veryEnd = param1Boolean;
    }
    
    public void cancel() {
      if (!this.cancelled) {
        this.cancelled = true;
        this.inner.cancel();
        this.upstream.cancel();
      } 
    }
    
    void drain() {
      if (getAndIncrement() == 0)
        while (true) {
          if (this.cancelled)
            return; 
          if (!this.active) {
            boolean bool = this.done;
            if (bool && !this.veryEnd && (Throwable)this.errors.get() != null) {
              this.downstream.onError(this.errors.terminate());
              return;
            } 
            try {
              Object object;
            } finally {
              Exception exception = null;
              Exceptions.throwIfFatal(exception);
              this.upstream.cancel();
              this.errors.addThrowable(exception);
              this.downstream.onError(this.errors.terminate());
            } 
          } 
          if (decrementAndGet() == 0)
            break; 
        }  
    }
    
    public void innerError(Throwable param1Throwable) {
      if (this.errors.addThrowable(param1Throwable)) {
        if (!this.veryEnd) {
          this.upstream.cancel();
          this.done = true;
        } 
        this.active = false;
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void innerNext(R param1R) {
      this.downstream.onNext(param1R);
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.errors.addThrowable(param1Throwable)) {
        this.done = true;
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void request(long param1Long) {
      this.inner.request(param1Long);
    }
    
    void subscribeActual() {
      this.downstream.onSubscribe(this);
    }
  }
  
  static final class ConcatMapImmediate<T, R> extends BaseConcatMapSubscriber<T, R> {
    private static final long serialVersionUID = 7898995095634264146L;
    
    final Subscriber<? super R> downstream;
    
    final AtomicInteger wip;
    
    ConcatMapImmediate(Subscriber<? super R> param1Subscriber, Function<? super T, ? extends Publisher<? extends R>> param1Function, int param1Int) {
      super(param1Function, param1Int);
      this.downstream = param1Subscriber;
      this.wip = new AtomicInteger();
    }
    
    public void cancel() {
      if (!this.cancelled) {
        this.cancelled = true;
        this.inner.cancel();
        this.upstream.cancel();
      } 
    }
    
    void drain() {
      if (this.wip.getAndIncrement() == 0)
        while (true) {
          if (this.cancelled)
            return; 
          if (!this.active) {
            boolean bool = this.done;
            try {
            
            } finally {
              Exception exception = null;
              Exceptions.throwIfFatal(exception);
              this.upstream.cancel();
              this.errors.addThrowable(exception);
              this.downstream.onError(this.errors.terminate());
            } 
          } 
          if (this.wip.decrementAndGet() == 0)
            break; 
        }  
    }
    
    public void innerError(Throwable param1Throwable) {
      if (this.errors.addThrowable(param1Throwable)) {
        this.upstream.cancel();
        if (getAndIncrement() == 0)
          this.downstream.onError(this.errors.terminate()); 
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void innerNext(R param1R) {
      if (get() == 0 && compareAndSet(0, 1)) {
        this.downstream.onNext(param1R);
        if (compareAndSet(1, 0))
          return; 
        this.downstream.onError(this.errors.terminate());
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.errors.addThrowable(param1Throwable)) {
        this.inner.cancel();
        if (getAndIncrement() == 0)
          this.downstream.onError(this.errors.terminate()); 
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void request(long param1Long) {
      this.inner.request(param1Long);
    }
    
    void subscribeActual() {
      this.downstream.onSubscribe(this);
    }
  }
  
  static final class ConcatMapInner<R> extends SubscriptionArbiter implements FlowableSubscriber<R> {
    private static final long serialVersionUID = 897683679971470653L;
    
    final FlowableConcatMap.ConcatMapSupport<R> parent;
    
    long produced;
    
    ConcatMapInner(FlowableConcatMap.ConcatMapSupport<R> param1ConcatMapSupport) {
      super(false);
      this.parent = param1ConcatMapSupport;
    }
    
    public void onComplete() {
      long l = this.produced;
      if (l != 0L) {
        this.produced = 0L;
        produced(l);
      } 
      this.parent.innerComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      long l = this.produced;
      if (l != 0L) {
        this.produced = 0L;
        produced(l);
      } 
      this.parent.innerError(param1Throwable);
    }
    
    public void onNext(R param1R) {
      this.produced++;
      this.parent.innerNext(param1R);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      setSubscription(param1Subscription);
    }
  }
  
  static interface ConcatMapSupport<T> {
    void innerComplete();
    
    void innerError(Throwable param1Throwable);
    
    void innerNext(T param1T);
  }
  
  static final class WeakScalarSubscription<T> implements Subscription {
    final Subscriber<? super T> downstream;
    
    boolean once;
    
    final T value;
    
    WeakScalarSubscription(T param1T, Subscriber<? super T> param1Subscriber) {
      this.value = param1T;
      this.downstream = param1Subscriber;
    }
    
    public void cancel() {}
    
    public void request(long param1Long) {
      if (param1Long > 0L && !this.once) {
        this.once = true;
        Subscriber<? super T> subscriber = this.downstream;
        subscriber.onNext(this.value);
        subscriber.onComplete();
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableConcatMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */