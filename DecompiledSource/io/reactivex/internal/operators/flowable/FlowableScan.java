package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableScan<T> extends AbstractFlowableWithUpstream<T, T> {
  final BiFunction<T, T, T> accumulator;
  
  public FlowableScan(Flowable<T> paramFlowable, BiFunction<T, T, T> paramBiFunction) {
    super(paramFlowable);
    this.accumulator = paramBiFunction;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new ScanSubscriber<T>(paramSubscriber, this.accumulator));
  }
  
  static final class ScanSubscriber<T> implements FlowableSubscriber<T>, Subscription {
    final BiFunction<T, T, T> accumulator;
    
    boolean done;
    
    final Subscriber<? super T> downstream;
    
    Subscription upstream;
    
    T value;
    
    ScanSubscriber(Subscriber<? super T> param1Subscriber, BiFunction<T, T, T> param1BiFunction) {
      this.downstream = param1Subscriber;
      this.accumulator = param1BiFunction;
    }
    
    public void cancel() {
      this.upstream.cancel();
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      Subscriber<? super T> subscriber = this.downstream;
      T t = this.value;
      if (t == null) {
        this.value = param1T;
        subscriber.onNext(param1T);
      } else {
        try {
          return;
        } finally {
          param1T = null;
          Exceptions.throwIfFatal((Throwable)param1T);
          this.upstream.cancel();
          onError((Throwable)param1T);
        } 
      } 
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
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableScan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */