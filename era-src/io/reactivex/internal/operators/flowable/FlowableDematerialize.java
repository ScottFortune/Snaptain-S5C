package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Notification;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableDematerialize<T, R> extends AbstractFlowableWithUpstream<T, R> {
  final Function<? super T, ? extends Notification<R>> selector;
  
  public FlowableDematerialize(Flowable<T> paramFlowable, Function<? super T, ? extends Notification<R>> paramFunction) {
    super(paramFlowable);
    this.selector = paramFunction;
  }
  
  protected void subscribeActual(Subscriber<? super R> paramSubscriber) {
    this.source.subscribe(new DematerializeSubscriber<T, R>(paramSubscriber, this.selector));
  }
  
  static final class DematerializeSubscriber<T, R> implements FlowableSubscriber<T>, Subscription {
    boolean done;
    
    final Subscriber<? super R> downstream;
    
    final Function<? super T, ? extends Notification<R>> selector;
    
    Subscription upstream;
    
    DematerializeSubscriber(Subscriber<? super R> param1Subscriber, Function<? super T, ? extends Notification<R>> param1Function) {
      this.downstream = param1Subscriber;
      this.selector = param1Function;
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
      Notification notification;
      if (this.done) {
        if (param1T instanceof Notification) {
          notification = (Notification)param1T;
          if (notification.isOnError())
            RxJavaPlugins.onError(notification.getError()); 
        } 
        return;
      } 
      try {
        return;
      } finally {
        notification = null;
        Exceptions.throwIfFatal((Throwable)notification);
        this.upstream.cancel();
        onError((Throwable)notification);
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableDematerialize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */