package io.reactivex.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.ListCompositeDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.EndConsumerHelper;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;

public abstract class ResourceSubscriber<T> implements FlowableSubscriber<T>, Disposable {
  private final AtomicLong missedRequested = new AtomicLong();
  
  private final ListCompositeDisposable resources = new ListCompositeDisposable();
  
  private final AtomicReference<Subscription> upstream = new AtomicReference<Subscription>();
  
  public final void add(Disposable paramDisposable) {
    ObjectHelper.requireNonNull(paramDisposable, "resource is null");
    this.resources.add(paramDisposable);
  }
  
  public final void dispose() {
    if (SubscriptionHelper.cancel(this.upstream))
      this.resources.dispose(); 
  }
  
  public final boolean isDisposed() {
    boolean bool;
    if (this.upstream.get() == SubscriptionHelper.CANCELLED) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected void onStart() {
    request(Long.MAX_VALUE);
  }
  
  public final void onSubscribe(Subscription paramSubscription) {
    if (EndConsumerHelper.setOnce(this.upstream, paramSubscription, getClass())) {
      long l = this.missedRequested.getAndSet(0L);
      if (l != 0L)
        paramSubscription.request(l); 
      onStart();
    } 
  }
  
  protected final void request(long paramLong) {
    SubscriptionHelper.deferredRequest(this.upstream, this.missedRequested, paramLong);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/subscribers/ResourceSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */