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

public final class FlowableElementAtMaybe<T> extends Maybe<T> implements FuseToFlowable<T> {
  final long index;
  
  final Flowable<T> source;
  
  public FlowableElementAtMaybe(Flowable<T> paramFlowable, long paramLong) {
    this.source = paramFlowable;
    this.index = paramLong;
  }
  
  public Flowable<T> fuseToFlowable() {
    return RxJavaPlugins.onAssembly(new FlowableElementAt<T>(this.source, this.index, null, false));
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    this.source.subscribe(new ElementAtSubscriber<T>(paramMaybeObserver, this.index));
  }
  
  static final class ElementAtSubscriber<T> implements FlowableSubscriber<T>, Disposable {
    long count;
    
    boolean done;
    
    final MaybeObserver<? super T> downstream;
    
    final long index;
    
    Subscription upstream;
    
    ElementAtSubscriber(MaybeObserver<? super T> param1MaybeObserver, long param1Long) {
      this.downstream = param1MaybeObserver;
      this.index = param1Long;
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
      this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
      if (!this.done) {
        this.done = true;
        this.downstream.onComplete();
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
      long l = this.count;
      if (l == this.index) {
        this.done = true;
        this.upstream.cancel();
        this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
        this.downstream.onSuccess(param1T);
        return;
      } 
      this.count = l + 1L;
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableElementAtMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */