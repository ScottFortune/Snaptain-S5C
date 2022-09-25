package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableSkipWhile<T> extends AbstractFlowableWithUpstream<T, T> {
  final Predicate<? super T> predicate;
  
  public FlowableSkipWhile(Flowable<T> paramFlowable, Predicate<? super T> paramPredicate) {
    super(paramFlowable);
    this.predicate = paramPredicate;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new SkipWhileSubscriber<T>(paramSubscriber, this.predicate));
  }
  
  static final class SkipWhileSubscriber<T> implements FlowableSubscriber<T>, Subscription {
    final Subscriber<? super T> downstream;
    
    boolean notSkipping;
    
    final Predicate<? super T> predicate;
    
    Subscription upstream;
    
    SkipWhileSubscriber(Subscriber<? super T> param1Subscriber, Predicate<? super T> param1Predicate) {
      this.downstream = param1Subscriber;
      this.predicate = param1Predicate;
    }
    
    public void cancel() {
      this.upstream.cancel();
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.notSkipping) {
        this.downstream.onNext(param1T);
      } else {
        try {
          return;
        } finally {
          param1T = null;
          Exceptions.throwIfFatal((Throwable)param1T);
          this.upstream.cancel();
          this.downstream.onError((Throwable)param1T);
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableSkipWhile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */