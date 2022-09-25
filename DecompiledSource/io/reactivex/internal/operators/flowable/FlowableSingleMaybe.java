package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.fuseable.FuseToFlowable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscription;

public final class FlowableSingleMaybe<T> extends Maybe<T> implements FuseToFlowable<T> {
  final Flowable<T> source;
  
  public FlowableSingleMaybe(Flowable<T> paramFlowable) {
    this.source = paramFlowable;
  }
  
  public Flowable<T> fuseToFlowable() {
    return RxJavaPlugins.onAssembly(new FlowableSingle<T>(this.source, null, false));
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    this.source.subscribe(new SingleElementSubscriber<T>(paramMaybeObserver));
  }
  
  static final class SingleElementSubscriber<T> implements FlowableSubscriber<T>, Disposable {
    boolean done;
    
    final MaybeObserver<? super T> downstream;
    
    Subscription upstream;
    
    T value;
    
    SingleElementSubscriber(MaybeObserver<? super T> param1MaybeObserver) {
      this.downstream = param1MaybeObserver;
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
      T t = this.value;
      this.value = null;
      if (t == null) {
        this.downstream.onComplete();
      } else {
        this.downstream.onSuccess(t);
      } 
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
      if (this.value != null) {
        this.done = true;
        this.upstream.cancel();
        this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
        this.downstream.onError(new IllegalArgumentException("Sequence contains more than one element!"));
        return;
      } 
      this.value = param1T;
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableSingleMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */