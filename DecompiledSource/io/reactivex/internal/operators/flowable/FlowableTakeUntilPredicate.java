package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableTakeUntilPredicate<T> extends AbstractFlowableWithUpstream<T, T> {
  final Predicate<? super T> predicate;
  
  public FlowableTakeUntilPredicate(Flowable<T> paramFlowable, Predicate<? super T> paramPredicate) {
    super(paramFlowable);
    this.predicate = paramPredicate;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new InnerSubscriber<T>(paramSubscriber, this.predicate));
  }
  
  static final class InnerSubscriber<T> implements FlowableSubscriber<T>, Subscription {
    boolean done;
    
    final Subscriber<? super T> downstream;
    
    final Predicate<? super T> predicate;
    
    Subscription upstream;
    
    InnerSubscriber(Subscriber<? super T> param1Subscriber, Predicate<? super T> param1Predicate) {
      this.downstream = param1Subscriber;
      this.predicate = param1Predicate;
    }
    
    public void cancel() {
      this.upstream.cancel();
    }
    
    public void onComplete() {
      if (!this.done) {
        this.done = true;
        this.downstream.onComplete();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (!this.done) {
        this.done = true;
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      if (!this.done) {
        this.downstream.onNext(param1T);
        try {
        
        } finally {
          param1T = null;
          Exceptions.throwIfFatal((Throwable)param1T);
          this.upstream.cancel();
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableTakeUntilPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */