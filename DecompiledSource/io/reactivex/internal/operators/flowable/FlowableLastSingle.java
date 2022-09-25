package io.reactivex.internal.operators.flowable;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.NoSuchElementException;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableLastSingle<T> extends Single<T> {
  final T defaultItem;
  
  final Publisher<T> source;
  
  public FlowableLastSingle(Publisher<T> paramPublisher, T paramT) {
    this.source = paramPublisher;
    this.defaultItem = paramT;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    this.source.subscribe((Subscriber)new LastSubscriber<T>(paramSingleObserver, this.defaultItem));
  }
  
  static final class LastSubscriber<T> implements FlowableSubscriber<T>, Disposable {
    final T defaultItem;
    
    final SingleObserver<? super T> downstream;
    
    T item;
    
    Subscription upstream;
    
    LastSubscriber(SingleObserver<? super T> param1SingleObserver, T param1T) {
      this.downstream = param1SingleObserver;
      this.defaultItem = param1T;
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
        t = this.defaultItem;
        if (t != null) {
          this.downstream.onSuccess(t);
        } else {
          this.downstream.onError(new NoSuchElementException());
        } 
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableLastSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */