package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableTimer extends Flowable<Long> {
  final long delay;
  
  final Scheduler scheduler;
  
  final TimeUnit unit;
  
  public FlowableTimer(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    this.delay = paramLong;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
  }
  
  public void subscribeActual(Subscriber<? super Long> paramSubscriber) {
    TimerSubscriber timerSubscriber = new TimerSubscriber(paramSubscriber);
    paramSubscriber.onSubscribe(timerSubscriber);
    timerSubscriber.setResource(this.scheduler.scheduleDirect(timerSubscriber, this.delay, this.unit));
  }
  
  static final class TimerSubscriber extends AtomicReference<Disposable> implements Subscription, Runnable {
    private static final long serialVersionUID = -2809475196591179431L;
    
    final Subscriber<? super Long> downstream;
    
    volatile boolean requested;
    
    TimerSubscriber(Subscriber<? super Long> param1Subscriber) {
      this.downstream = param1Subscriber;
    }
    
    public void cancel() {
      DisposableHelper.dispose(this);
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long))
        this.requested = true; 
    }
    
    public void run() {
      if (get() != DisposableHelper.DISPOSED)
        if (this.requested) {
          this.downstream.onNext(Long.valueOf(0L));
          lazySet((Disposable)EmptyDisposable.INSTANCE);
          this.downstream.onComplete();
        } else {
          lazySet((Disposable)EmptyDisposable.INSTANCE);
          this.downstream.onError((Throwable)new MissingBackpressureException("Can't deliver value due to lack of requests"));
        }  
    }
    
    public void setResource(Disposable param1Disposable) {
      DisposableHelper.trySet(this, param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableTimer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */