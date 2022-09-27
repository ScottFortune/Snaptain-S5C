package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.internal.subscriptions.SubscriptionArbiter;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableTimeout<T, U, V> extends AbstractFlowableWithUpstream<T, T> {
  final Publisher<U> firstTimeoutIndicator;
  
  final Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator;
  
  final Publisher<? extends T> other;
  
  public FlowableTimeout(Flowable<T> paramFlowable, Publisher<U> paramPublisher, Function<? super T, ? extends Publisher<V>> paramFunction, Publisher<? extends T> paramPublisher1) {
    super(paramFlowable);
    this.firstTimeoutIndicator = paramPublisher;
    this.itemTimeoutIndicator = paramFunction;
    this.other = paramPublisher1;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    TimeoutSubscriber<T> timeoutSubscriber;
    Publisher<? extends T> publisher = this.other;
    if (publisher == null) {
      timeoutSubscriber = new TimeoutSubscriber<T>(paramSubscriber, this.itemTimeoutIndicator);
      paramSubscriber.onSubscribe(timeoutSubscriber);
      timeoutSubscriber.startFirstTimeout(this.firstTimeoutIndicator);
      this.source.subscribe(timeoutSubscriber);
    } else {
      TimeoutFallbackSubscriber<T> timeoutFallbackSubscriber = new TimeoutFallbackSubscriber<T>(paramSubscriber, this.itemTimeoutIndicator, (Publisher<? extends T>)timeoutSubscriber);
      paramSubscriber.onSubscribe((Subscription)timeoutFallbackSubscriber);
      timeoutFallbackSubscriber.startFirstTimeout(this.firstTimeoutIndicator);
      this.source.subscribe(timeoutFallbackSubscriber);
    } 
  }
  
  static final class TimeoutConsumer extends AtomicReference<Subscription> implements FlowableSubscriber<Object>, Disposable {
    private static final long serialVersionUID = 8708641127342403073L;
    
    final long idx;
    
    final FlowableTimeout.TimeoutSelectorSupport parent;
    
    TimeoutConsumer(long param1Long, FlowableTimeout.TimeoutSelectorSupport param1TimeoutSelectorSupport) {
      this.idx = param1Long;
      this.parent = param1TimeoutSelectorSupport;
    }
    
    public void dispose() {
      SubscriptionHelper.cancel(this);
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (get() == SubscriptionHelper.CANCELLED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onComplete() {
      if (get() != SubscriptionHelper.CANCELLED) {
        lazySet((Subscription)SubscriptionHelper.CANCELLED);
        this.parent.onTimeout(this.idx);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (get() != SubscriptionHelper.CANCELLED) {
        lazySet((Subscription)SubscriptionHelper.CANCELLED);
        this.parent.onTimeoutError(this.idx, param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(Object param1Object) {
      param1Object = get();
      if (param1Object != SubscriptionHelper.CANCELLED) {
        param1Object.cancel();
        lazySet((Subscription)SubscriptionHelper.CANCELLED);
        this.parent.onTimeout(this.idx);
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.setOnce(this, param1Subscription, Long.MAX_VALUE);
    }
  }
  
  static final class TimeoutFallbackSubscriber<T> extends SubscriptionArbiter implements FlowableSubscriber<T>, TimeoutSelectorSupport {
    private static final long serialVersionUID = 3764492702657003550L;
    
    long consumed;
    
    final Subscriber<? super T> downstream;
    
    Publisher<? extends T> fallback;
    
    final AtomicLong index;
    
    final Function<? super T, ? extends Publisher<?>> itemTimeoutIndicator;
    
    final SequentialDisposable task;
    
    final AtomicReference<Subscription> upstream;
    
    TimeoutFallbackSubscriber(Subscriber<? super T> param1Subscriber, Function<? super T, ? extends Publisher<?>> param1Function, Publisher<? extends T> param1Publisher) {
      super(true);
      this.downstream = param1Subscriber;
      this.itemTimeoutIndicator = param1Function;
      this.task = new SequentialDisposable();
      this.upstream = new AtomicReference<Subscription>();
      this.fallback = param1Publisher;
      this.index = new AtomicLong();
    }
    
    public void cancel() {
      super.cancel();
      this.task.dispose();
    }
    
    public void onComplete() {
      if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
        this.task.dispose();
        this.downstream.onComplete();
        this.task.dispose();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
        this.task.dispose();
        this.downstream.onError(param1Throwable);
        this.task.dispose();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      long l = this.index.get();
      if (l != Long.MAX_VALUE) {
        AtomicLong atomicLong = this.index;
        long l1 = l + 1L;
        if (atomicLong.compareAndSet(l, l1)) {
          Disposable disposable = (Disposable)this.task.get();
          if (disposable != null)
            disposable.dispose(); 
          this.consumed++;
          this.downstream.onNext(param1T);
          try {
            return;
          } finally {
            param1T = null;
            Exceptions.throwIfFatal((Throwable)param1T);
            ((Subscription)this.upstream.get()).cancel();
            this.index.getAndSet(Long.MAX_VALUE);
          } 
        } 
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.setOnce(this.upstream, param1Subscription))
        setSubscription(param1Subscription); 
    }
    
    public void onTimeout(long param1Long) {
      if (this.index.compareAndSet(param1Long, Long.MAX_VALUE)) {
        SubscriptionHelper.cancel(this.upstream);
        Publisher<? extends T> publisher = this.fallback;
        this.fallback = null;
        param1Long = this.consumed;
        if (param1Long != 0L)
          produced(param1Long); 
        publisher.subscribe((Subscriber)new FlowableTimeoutTimed.FallbackSubscriber<T>(this.downstream, this));
      } 
    }
    
    public void onTimeoutError(long param1Long, Throwable param1Throwable) {
      if (this.index.compareAndSet(param1Long, Long.MAX_VALUE)) {
        SubscriptionHelper.cancel(this.upstream);
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    void startFirstTimeout(Publisher<?> param1Publisher) {
      if (param1Publisher != null) {
        FlowableTimeout.TimeoutConsumer timeoutConsumer = new FlowableTimeout.TimeoutConsumer(0L, this);
        if (this.task.replace(timeoutConsumer))
          param1Publisher.subscribe((Subscriber)timeoutConsumer); 
      } 
    }
  }
  
  static interface TimeoutSelectorSupport extends FlowableTimeoutTimed.TimeoutSupport {
    void onTimeoutError(long param1Long, Throwable param1Throwable);
  }
  
  static final class TimeoutSubscriber<T> extends AtomicLong implements FlowableSubscriber<T>, Subscription, TimeoutSelectorSupport {
    private static final long serialVersionUID = 3764492702657003550L;
    
    final Subscriber<? super T> downstream;
    
    final Function<? super T, ? extends Publisher<?>> itemTimeoutIndicator;
    
    final AtomicLong requested;
    
    final SequentialDisposable task;
    
    final AtomicReference<Subscription> upstream;
    
    TimeoutSubscriber(Subscriber<? super T> param1Subscriber, Function<? super T, ? extends Publisher<?>> param1Function) {
      this.downstream = param1Subscriber;
      this.itemTimeoutIndicator = param1Function;
      this.task = new SequentialDisposable();
      this.upstream = new AtomicReference<Subscription>();
      this.requested = new AtomicLong();
    }
    
    public void cancel() {
      SubscriptionHelper.cancel(this.upstream);
      this.task.dispose();
    }
    
    public void onComplete() {
      if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
        this.task.dispose();
        this.downstream.onComplete();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
        this.task.dispose();
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      long l = get();
      if (l != Long.MAX_VALUE) {
        long l1 = 1L + l;
        if (compareAndSet(l, l1)) {
          Disposable disposable = (Disposable)this.task.get();
          if (disposable != null)
            disposable.dispose(); 
          this.downstream.onNext(param1T);
          try {
            return;
          } finally {
            param1T = null;
            Exceptions.throwIfFatal((Throwable)param1T);
            ((Subscription)this.upstream.get()).cancel();
            getAndSet(Long.MAX_VALUE);
          } 
        } 
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.deferredSetOnce(this.upstream, this.requested, param1Subscription);
    }
    
    public void onTimeout(long param1Long) {
      if (compareAndSet(param1Long, Long.MAX_VALUE)) {
        SubscriptionHelper.cancel(this.upstream);
        this.downstream.onError(new TimeoutException());
      } 
    }
    
    public void onTimeoutError(long param1Long, Throwable param1Throwable) {
      if (compareAndSet(param1Long, Long.MAX_VALUE)) {
        SubscriptionHelper.cancel(this.upstream);
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void request(long param1Long) {
      SubscriptionHelper.deferredRequest(this.upstream, this.requested, param1Long);
    }
    
    void startFirstTimeout(Publisher<?> param1Publisher) {
      if (param1Publisher != null) {
        FlowableTimeout.TimeoutConsumer timeoutConsumer = new FlowableTimeout.TimeoutConsumer(0L, this);
        if (this.task.replace(timeoutConsumer))
          param1Publisher.subscribe((Subscriber)timeoutConsumer); 
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableTimeout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */