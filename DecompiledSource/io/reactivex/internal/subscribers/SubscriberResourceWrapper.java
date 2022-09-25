package io.reactivex.internal.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class SubscriberResourceWrapper<T> extends AtomicReference<Disposable> implements FlowableSubscriber<T>, Disposable, Subscription {
  private static final long serialVersionUID = -8612022020200669122L;
  
  final Subscriber<? super T> downstream;
  
  final AtomicReference<Subscription> upstream = new AtomicReference<Subscription>();
  
  public SubscriberResourceWrapper(Subscriber<? super T> paramSubscriber) {
    this.downstream = paramSubscriber;
  }
  
  public void cancel() {
    dispose();
  }
  
  public void dispose() {
    SubscriptionHelper.cancel(this.upstream);
    DisposableHelper.dispose(this);
  }
  
  public boolean isDisposed() {
    boolean bool;
    if (this.upstream.get() == SubscriptionHelper.CANCELLED) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void onComplete() {
    DisposableHelper.dispose(this);
    this.downstream.onComplete();
  }
  
  public void onError(Throwable paramThrowable) {
    DisposableHelper.dispose(this);
    this.downstream.onError(paramThrowable);
  }
  
  public void onNext(T paramT) {
    this.downstream.onNext(paramT);
  }
  
  public void onSubscribe(Subscription paramSubscription) {
    if (SubscriptionHelper.setOnce(this.upstream, paramSubscription))
      this.downstream.onSubscribe(this); 
  }
  
  public void request(long paramLong) {
    if (SubscriptionHelper.validate(paramLong))
      ((Subscription)this.upstream.get()).request(paramLong); 
  }
  
  public void setResource(Disposable paramDisposable) {
    DisposableHelper.set(this, paramDisposable);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/subscribers/SubscriberResourceWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */