package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableDelay<T> extends AbstractFlowableWithUpstream<T, T> {
  final long delay;
  
  final boolean delayError;
  
  final Scheduler scheduler;
  
  final TimeUnit unit;
  
  public FlowableDelay(Flowable<T> paramFlowable, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, boolean paramBoolean) {
    super(paramFlowable);
    this.delay = paramLong;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
    this.delayError = paramBoolean;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    SerializedSubscriber serializedSubscriber;
    if (!this.delayError)
      serializedSubscriber = new SerializedSubscriber(paramSubscriber); 
    Scheduler.Worker worker = this.scheduler.createWorker();
    this.source.subscribe(new DelaySubscriber((Subscriber<?>)serializedSubscriber, this.delay, this.unit, worker, this.delayError));
  }
  
  static final class DelaySubscriber<T> implements FlowableSubscriber<T>, Subscription {
    final long delay;
    
    final boolean delayError;
    
    final Subscriber<? super T> downstream;
    
    final TimeUnit unit;
    
    Subscription upstream;
    
    final Scheduler.Worker w;
    
    DelaySubscriber(Subscriber<? super T> param1Subscriber, long param1Long, TimeUnit param1TimeUnit, Scheduler.Worker param1Worker, boolean param1Boolean) {
      this.downstream = param1Subscriber;
      this.delay = param1Long;
      this.unit = param1TimeUnit;
      this.w = param1Worker;
      this.delayError = param1Boolean;
    }
    
    public void cancel() {
      this.upstream.cancel();
      this.w.dispose();
    }
    
    public void onComplete() {
      this.w.schedule(new OnComplete(), this.delay, this.unit);
    }
    
    public void onError(Throwable param1Throwable) {
      long l;
      Scheduler.Worker worker = this.w;
      OnError onError = new OnError(param1Throwable);
      if (this.delayError) {
        l = this.delay;
      } else {
        l = 0L;
      } 
      worker.schedule(onError, l, this.unit);
    }
    
    public void onNext(T param1T) {
      this.w.schedule(new OnNext(param1T), this.delay, this.unit);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void request(long param1Long) {
      this.upstream.request(param1Long);
    }
    
    final class OnComplete implements Runnable {
      public void run() {
        try {
          FlowableDelay.DelaySubscriber.this.downstream.onComplete();
          return;
        } finally {
          FlowableDelay.DelaySubscriber.this.w.dispose();
        } 
      }
    }
    
    final class OnError implements Runnable {
      private final Throwable t;
      
      OnError(Throwable param2Throwable) {
        this.t = param2Throwable;
      }
      
      public void run() {
        try {
          FlowableDelay.DelaySubscriber.this.downstream.onError(this.t);
          return;
        } finally {
          FlowableDelay.DelaySubscriber.this.w.dispose();
        } 
      }
    }
    
    final class OnNext implements Runnable {
      private final T t;
      
      OnNext(T param2T) {
        this.t = param2T;
      }
      
      public void run() {
        FlowableDelay.DelaySubscriber.this.downstream.onNext(this.t);
      }
    }
  }
  
  final class OnComplete implements Runnable {
    public void run() {
      try {
        this.this$0.downstream.onComplete();
        return;
      } finally {
        this.this$0.w.dispose();
      } 
    }
  }
  
  final class OnError implements Runnable {
    private final Throwable t;
    
    OnError(Throwable param1Throwable) {
      this.t = param1Throwable;
    }
    
    public void run() {
      try {
        this.this$0.downstream.onError(this.t);
        return;
      } finally {
        this.this$0.w.dispose();
      } 
    }
  }
  
  final class OnNext implements Runnable {
    private final T t;
    
    OnNext(T param1T) {
      this.t = param1T;
    }
    
    public void run() {
      this.this$0.downstream.onNext(this.t);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableDelay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */