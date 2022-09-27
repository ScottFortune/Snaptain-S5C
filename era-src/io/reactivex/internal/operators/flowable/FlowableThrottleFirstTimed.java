package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableThrottleFirstTimed<T> extends AbstractFlowableWithUpstream<T, T> {
  final Scheduler scheduler;
  
  final long timeout;
  
  final TimeUnit unit;
  
  public FlowableThrottleFirstTimed(Flowable<T> paramFlowable, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    super(paramFlowable);
    this.timeout = paramLong;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new DebounceTimedSubscriber((Subscriber<?>)new SerializedSubscriber(paramSubscriber), this.timeout, this.unit, this.scheduler.createWorker()));
  }
  
  static final class DebounceTimedSubscriber<T> extends AtomicLong implements FlowableSubscriber<T>, Subscription, Runnable {
    private static final long serialVersionUID = -9102637559663639004L;
    
    boolean done;
    
    final Subscriber<? super T> downstream;
    
    volatile boolean gate;
    
    final long timeout;
    
    final SequentialDisposable timer = new SequentialDisposable();
    
    final TimeUnit unit;
    
    Subscription upstream;
    
    final Scheduler.Worker worker;
    
    DebounceTimedSubscriber(Subscriber<? super T> param1Subscriber, long param1Long, TimeUnit param1TimeUnit, Scheduler.Worker param1Worker) {
      this.downstream = param1Subscriber;
      this.timeout = param1Long;
      this.unit = param1TimeUnit;
      this.worker = param1Worker;
    }
    
    public void cancel() {
      this.upstream.cancel();
      this.worker.dispose();
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      this.downstream.onComplete();
      this.worker.dispose();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.downstream.onError(param1Throwable);
      this.worker.dispose();
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      if (!this.gate) {
        this.gate = true;
        if (get() != 0L) {
          this.downstream.onNext(param1T);
          BackpressureHelper.produced(this, 1L);
          Disposable disposable = (Disposable)this.timer.get();
          if (disposable != null)
            disposable.dispose(); 
          this.timer.replace(this.worker.schedule(this, this.timeout, this.unit));
        } else {
          this.done = true;
          cancel();
          this.downstream.onError((Throwable)new MissingBackpressureException("Could not deliver value due to lack of requests"));
        } 
      } 
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
        BackpressureHelper.add(this, param1Long); 
    }
    
    public void run() {
      this.gate = false;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableThrottleFirstTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */