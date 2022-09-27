package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.internal.subscriptions.SubscriptionArbiter;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableTimeoutTimed<T> extends AbstractFlowableWithUpstream<T, T> {
  final Publisher<? extends T> other;
  
  final Scheduler scheduler;
  
  final long timeout;
  
  final TimeUnit unit;
  
  public FlowableTimeoutTimed(Flowable<T> paramFlowable, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, Publisher<? extends T> paramPublisher) {
    super(paramFlowable);
    this.timeout = paramLong;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
    this.other = paramPublisher;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    if (this.other == null) {
      TimeoutSubscriber<T> timeoutSubscriber = new TimeoutSubscriber<T>(paramSubscriber, this.timeout, this.unit, this.scheduler.createWorker());
      paramSubscriber.onSubscribe(timeoutSubscriber);
      timeoutSubscriber.startTimeout(0L);
      this.source.subscribe(timeoutSubscriber);
    } else {
      TimeoutFallbackSubscriber<T> timeoutFallbackSubscriber = new TimeoutFallbackSubscriber<T>(paramSubscriber, this.timeout, this.unit, this.scheduler.createWorker(), this.other);
      paramSubscriber.onSubscribe((Subscription)timeoutFallbackSubscriber);
      timeoutFallbackSubscriber.startTimeout(0L);
      this.source.subscribe(timeoutFallbackSubscriber);
    } 
  }
  
  static final class FallbackSubscriber<T> implements FlowableSubscriber<T> {
    final SubscriptionArbiter arbiter;
    
    final Subscriber<? super T> downstream;
    
    FallbackSubscriber(Subscriber<? super T> param1Subscriber, SubscriptionArbiter param1SubscriptionArbiter) {
      this.downstream = param1Subscriber;
      this.arbiter = param1SubscriptionArbiter;
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.downstream.onNext(param1T);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      this.arbiter.setSubscription(param1Subscription);
    }
  }
  
  static final class TimeoutFallbackSubscriber<T> extends SubscriptionArbiter implements FlowableSubscriber<T>, TimeoutSupport {
    private static final long serialVersionUID = 3764492702657003550L;
    
    long consumed;
    
    final Subscriber<? super T> downstream;
    
    Publisher<? extends T> fallback;
    
    final AtomicLong index;
    
    final SequentialDisposable task;
    
    final long timeout;
    
    final TimeUnit unit;
    
    final AtomicReference<Subscription> upstream;
    
    final Scheduler.Worker worker;
    
    TimeoutFallbackSubscriber(Subscriber<? super T> param1Subscriber, long param1Long, TimeUnit param1TimeUnit, Scheduler.Worker param1Worker, Publisher<? extends T> param1Publisher) {
      super(true);
      this.downstream = param1Subscriber;
      this.timeout = param1Long;
      this.unit = param1TimeUnit;
      this.worker = param1Worker;
      this.fallback = param1Publisher;
      this.task = new SequentialDisposable();
      this.upstream = new AtomicReference<Subscription>();
      this.index = new AtomicLong();
    }
    
    public void cancel() {
      super.cancel();
      this.worker.dispose();
    }
    
    public void onComplete() {
      if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
        this.task.dispose();
        this.downstream.onComplete();
        this.worker.dispose();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
        this.task.dispose();
        this.downstream.onError(param1Throwable);
        this.worker.dispose();
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
          ((Disposable)this.task.get()).dispose();
          this.consumed++;
          this.downstream.onNext(param1T);
          startTimeout(l1);
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
        param1Long = this.consumed;
        if (param1Long != 0L)
          produced(param1Long); 
        Publisher<? extends T> publisher = this.fallback;
        this.fallback = null;
        publisher.subscribe((Subscriber)new FlowableTimeoutTimed.FallbackSubscriber<T>(this.downstream, this));
        this.worker.dispose();
      } 
    }
    
    void startTimeout(long param1Long) {
      this.task.replace(this.worker.schedule(new FlowableTimeoutTimed.TimeoutTask(param1Long, this), this.timeout, this.unit));
    }
  }
  
  static final class TimeoutSubscriber<T> extends AtomicLong implements FlowableSubscriber<T>, Subscription, TimeoutSupport {
    private static final long serialVersionUID = 3764492702657003550L;
    
    final Subscriber<? super T> downstream;
    
    final AtomicLong requested;
    
    final SequentialDisposable task;
    
    final long timeout;
    
    final TimeUnit unit;
    
    final AtomicReference<Subscription> upstream;
    
    final Scheduler.Worker worker;
    
    TimeoutSubscriber(Subscriber<? super T> param1Subscriber, long param1Long, TimeUnit param1TimeUnit, Scheduler.Worker param1Worker) {
      this.downstream = param1Subscriber;
      this.timeout = param1Long;
      this.unit = param1TimeUnit;
      this.worker = param1Worker;
      this.task = new SequentialDisposable();
      this.upstream = new AtomicReference<Subscription>();
      this.requested = new AtomicLong();
    }
    
    public void cancel() {
      SubscriptionHelper.cancel(this.upstream);
      this.worker.dispose();
    }
    
    public void onComplete() {
      if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
        this.task.dispose();
        this.downstream.onComplete();
        this.worker.dispose();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
        this.task.dispose();
        this.downstream.onError(param1Throwable);
        this.worker.dispose();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      long l = get();
      if (l != Long.MAX_VALUE) {
        long l1 = 1L + l;
        if (compareAndSet(l, l1)) {
          ((Disposable)this.task.get()).dispose();
          this.downstream.onNext(param1T);
          startTimeout(l1);
        } 
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.deferredSetOnce(this.upstream, this.requested, param1Subscription);
    }
    
    public void onTimeout(long param1Long) {
      if (compareAndSet(param1Long, Long.MAX_VALUE)) {
        SubscriptionHelper.cancel(this.upstream);
        this.downstream.onError(new TimeoutException(ExceptionHelper.timeoutMessage(this.timeout, this.unit)));
        this.worker.dispose();
      } 
    }
    
    public void request(long param1Long) {
      SubscriptionHelper.deferredRequest(this.upstream, this.requested, param1Long);
    }
    
    void startTimeout(long param1Long) {
      this.task.replace(this.worker.schedule(new FlowableTimeoutTimed.TimeoutTask(param1Long, this), this.timeout, this.unit));
    }
  }
  
  static interface TimeoutSupport {
    void onTimeout(long param1Long);
  }
  
  static final class TimeoutTask implements Runnable {
    final long idx;
    
    final FlowableTimeoutTimed.TimeoutSupport parent;
    
    TimeoutTask(long param1Long, FlowableTimeoutTimed.TimeoutSupport param1TimeoutSupport) {
      this.idx = param1Long;
      this.parent = param1TimeoutSupport;
    }
    
    public void run() {
      this.parent.onTimeout(this.idx);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableTimeoutTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */