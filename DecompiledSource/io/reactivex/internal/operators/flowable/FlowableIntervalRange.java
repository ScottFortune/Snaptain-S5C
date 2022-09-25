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

public final class FlowableIntervalRange extends Flowable<Long> {
  final long end;
  
  final long initialDelay;
  
  final long period;
  
  final Scheduler scheduler;
  
  final long start;
  
  final TimeUnit unit;
  
  public FlowableIntervalRange(long paramLong1, long paramLong2, long paramLong3, long paramLong4, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    this.initialDelay = paramLong3;
    this.period = paramLong4;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
    this.start = paramLong1;
    this.end = paramLong2;
  }
  
  public void subscribeActual(Subscriber<? super Long> paramSubscriber) {
    Scheduler.Worker worker;
    IntervalRangeSubscriber intervalRangeSubscriber = new IntervalRangeSubscriber(paramSubscriber, this.start, this.end);
    paramSubscriber.onSubscribe(intervalRangeSubscriber);
    Scheduler scheduler = this.scheduler;
    if (scheduler instanceof io.reactivex.internal.schedulers.TrampolineScheduler) {
      worker = scheduler.createWorker();
      intervalRangeSubscriber.setResource((Disposable)worker);
      worker.schedulePeriodically(intervalRangeSubscriber, this.initialDelay, this.period, this.unit);
    } else {
      intervalRangeSubscriber.setResource(worker.schedulePeriodicallyDirect(intervalRangeSubscriber, this.initialDelay, this.period, this.unit));
    } 
  }
  
  static final class IntervalRangeSubscriber extends AtomicLong implements Subscription, Runnable {
    private static final long serialVersionUID = -2809475196591179431L;
    
    long count;
    
    final Subscriber<? super Long> downstream;
    
    final long end;
    
    final AtomicReference<Disposable> resource = new AtomicReference<Disposable>();
    
    IntervalRangeSubscriber(Subscriber<? super Long> param1Subscriber, long param1Long1, long param1Long2) {
      this.downstream = param1Subscriber;
      this.count = param1Long1;
      this.end = param1Long2;
    }
    
    public void cancel() {
      DisposableHelper.dispose(this.resource);
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long))
        BackpressureHelper.add(this, param1Long); 
    }
    
    public void run() {
      if (this.resource.get() != DisposableHelper.DISPOSED) {
        long l = get();
        if (l != 0L) {
          long l1 = this.count;
          this.downstream.onNext(Long.valueOf(l1));
          if (l1 == this.end) {
            if (this.resource.get() != DisposableHelper.DISPOSED)
              this.downstream.onComplete(); 
            DisposableHelper.dispose(this.resource);
            return;
          } 
          this.count = l1 + 1L;
          if (l != Long.MAX_VALUE)
            decrementAndGet(); 
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
    }
    
    public void setResource(Disposable param1Disposable) {
      DisposableHelper.setOnce(this.resource, param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableIntervalRange.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */