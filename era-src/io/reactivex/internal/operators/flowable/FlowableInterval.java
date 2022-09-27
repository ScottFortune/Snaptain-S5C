package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableInterval extends Flowable<Long> {
  final long initialDelay;
  
  final long period;
  
  final Scheduler scheduler;
  
  final TimeUnit unit;
  
  public FlowableInterval(long paramLong1, long paramLong2, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    this.initialDelay = paramLong1;
    this.period = paramLong2;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
  }
  
  public void subscribeActual(Subscriber<? super Long> paramSubscriber) {
    Scheduler.Worker worker;
    IntervalSubscriber intervalSubscriber = new IntervalSubscriber(paramSubscriber);
    paramSubscriber.onSubscribe(intervalSubscriber);
    Scheduler scheduler = this.scheduler;
    if (scheduler instanceof io.reactivex.internal.schedulers.TrampolineScheduler) {
      worker = scheduler.createWorker();
      intervalSubscriber.setResource((Disposable)worker);
      worker.schedulePeriodically(intervalSubscriber, this.initialDelay, this.period, this.unit);
    } else {
      intervalSubscriber.setResource(worker.schedulePeriodicallyDirect(intervalSubscriber, this.initialDelay, this.period, this.unit));
    } 
  }
  
  static final class IntervalSubscriber extends AtomicLong implements Subscription, Runnable {
    private static final long serialVersionUID = -2809475196591179431L;
    
    long count;
    
    final Subscriber<? super Long> downstream;
    
    final AtomicReference<Disposable> resource = new AtomicReference<Disposable>();
    
    IntervalSubscriber(Subscriber<? super Long> param1Subscriber) {
      this.downstream = param1Subscriber;
    }
    
    public void cancel() {
      DisposableHelper.dispose(this.resource);
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long))
        BackpressureHelper.add(this, param1Long); 
    }
    
    public void run() {
      if (this.resource.get() != DisposableHelper.DISPOSED)
        if (get() != 0L) {
          Subscriber<? super Long> subscriber = this.downstream;
          long l = this.count;
          this.count = l + 1L;
          subscriber.onNext(Long.valueOf(l));
          BackpressureHelper.produced(this, 1L);
        } else {
          Subscriber<? super Long> subscriber = this.downstream;
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Can't deliver value ");
          stringBuilder.append(this.count);
          stringBuilder.append(" due to lack of requests");
          subscriber.onError((Throwable)new MissingBackpressureException(stringBuilder.toString()));
          DisposableHelper.dispose(this.resource);
        }  
    }
    
    public void setResource(Disposable param1Disposable) {
      DisposableHelper.setOnce(this.resource, param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableInterval.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */