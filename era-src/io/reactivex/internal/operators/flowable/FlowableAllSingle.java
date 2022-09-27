package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.fuseable.FuseToFlowable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscription;

public final class FlowableAllSingle<T> extends Single<Boolean> implements FuseToFlowable<Boolean> {
  final Predicate<? super T> predicate;
  
  final Flowable<T> source;
  
  public FlowableAllSingle(Flowable<T> paramFlowable, Predicate<? super T> paramPredicate) {
    this.source = paramFlowable;
    this.predicate = paramPredicate;
  }
  
  public Flowable<Boolean> fuseToFlowable() {
    return RxJavaPlugins.onAssembly(new FlowableAll<T>(this.source, this.predicate));
  }
  
  protected void subscribeActual(SingleObserver<? super Boolean> paramSingleObserver) {
    this.source.subscribe(new AllSubscriber<T>(paramSingleObserver, this.predicate));
  }
  
  static final class AllSubscriber<T> implements FlowableSubscriber<T>, Disposable {
    boolean done;
    
    final SingleObserver<? super Boolean> downstream;
    
    final Predicate<? super T> predicate;
    
    Subscription upstream;
    
    AllSubscriber(SingleObserver<? super Boolean> param1SingleObserver, Predicate<? super T> param1Predicate) {
      this.downstream = param1SingleObserver;
      this.predicate = param1Predicate;
    }
    
    public void dispose() {
      this.upstream.cancel();
      this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (this.upstream == SubscriptionHelper.CANCELLED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
      this.downstream.onSuccess(Boolean.valueOf(true));
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      try {
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        this.upstream.cancel();
        this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
        onError((Throwable)param1T);
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        param1Subscription.request(Long.MAX_VALUE);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableAllSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */