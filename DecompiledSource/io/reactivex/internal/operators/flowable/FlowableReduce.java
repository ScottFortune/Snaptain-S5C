package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableReduce<T> extends AbstractFlowableWithUpstream<T, T> {
  final BiFunction<T, T, T> reducer;
  
  public FlowableReduce(Flowable<T> paramFlowable, BiFunction<T, T, T> paramBiFunction) {
    super(paramFlowable);
    this.reducer = paramBiFunction;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new ReduceSubscriber<T>(paramSubscriber, this.reducer));
  }
  
  static final class ReduceSubscriber<T> extends DeferredScalarSubscription<T> implements FlowableSubscriber<T> {
    private static final long serialVersionUID = -4663883003264602070L;
    
    final BiFunction<T, T, T> reducer;
    
    Subscription upstream;
    
    ReduceSubscriber(Subscriber<? super T> param1Subscriber, BiFunction<T, T, T> param1BiFunction) {
      super(param1Subscriber);
      this.reducer = param1BiFunction;
    }
    
    public void cancel() {
      super.cancel();
      this.upstream.cancel();
      this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
    }
    
    public void onComplete() {
      if (this.upstream == SubscriptionHelper.CANCELLED)
        return; 
      this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
      Object object = this.value;
      if (object != null) {
        complete(object);
      } else {
        this.downstream.onComplete();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.upstream == SubscriptionHelper.CANCELLED) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.upstream == SubscriptionHelper.CANCELLED)
        return; 
      Object object = this.value;
      if (object == null) {
        this.value = param1T;
      } else {
        try {
          this.value = ObjectHelper.requireNonNull(this.reducer.apply(object, param1T), "The reducer returned a null value");
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
        this.downstream.onSubscribe((Subscription)this);
        param1Subscription.request(Long.MAX_VALUE);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableReduce.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */