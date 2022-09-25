package io.reactivex.internal.operators.flowable;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableLastMaybe<T> extends Maybe<T> {
  final Publisher<T> source;
  
  public FlowableLastMaybe(Publisher<T> paramPublisher) {
    this.source = paramPublisher;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    this.source.subscribe((Subscriber)new LastSubscriber<T>(paramMaybeObserver));
  }
  
  static final class LastSubscriber<T> implements FlowableSubscriber<T>, Disposable {
    final MaybeObserver<? super T> downstream;
    
    T item;
    
    Subscription upstream;
    
    LastSubscriber(MaybeObserver<? super T> param1MaybeObserver) {
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
      this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
      T t = this.item;
      if (t != null) {
        this.item = null;
        this.downstream.onSuccess(t);
      } else {
        this.downstream.onComplete();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
      this.item = null;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.item = param1T;
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableLastMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */