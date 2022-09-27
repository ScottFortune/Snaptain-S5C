package io.reactivex.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.EndConsumerHelper;
import org.reactivestreams.Subscription;

public abstract class DefaultSubscriber<T> implements FlowableSubscriber<T> {
  Subscription upstream;
  
  protected final void cancel() {
    Subscription subscription = this.upstream;
    this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
    subscription.cancel();
  }
  
  protected void onStart() {
    request(Long.MAX_VALUE);
  }
  
  public final void onSubscribe(Subscription paramSubscription) {
    if (EndConsumerHelper.validate(this.upstream, paramSubscription, getClass())) {
      this.upstream = paramSubscription;
      onStart();
    } 
  }
  
  protected final void request(long paramLong) {
    Subscription subscription = this.upstream;
    if (subscription != null)
      subscription.request(paramLong); 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/subscribers/DefaultSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */