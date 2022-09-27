package io.reactivex.internal.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BlockingHelper;
import io.reactivex.internal.util.ExceptionHelper;
import java.util.concurrent.CountDownLatch;
import org.reactivestreams.Subscription;

public abstract class BlockingBaseSubscriber<T> extends CountDownLatch implements FlowableSubscriber<T> {
  volatile boolean cancelled;
  
  Throwable error;
  
  Subscription upstream;
  
  T value;
  
  public BlockingBaseSubscriber() {
    super(1);
  }
  
  public final T blockingGet() {
    if (getCount() != 0L)
      try {
        BlockingHelper.verifyNonBlocking();
        await();
      } catch (InterruptedException interruptedException) {
        Subscription subscription = this.upstream;
        this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
        if (subscription != null)
          subscription.cancel(); 
        throw ExceptionHelper.wrapOrThrow(interruptedException);
      }  
    Throwable throwable = this.error;
    if (throwable == null)
      return this.value; 
    throw ExceptionHelper.wrapOrThrow(throwable);
  }
  
  public final void onComplete() {
    countDown();
  }
  
  public final void onSubscribe(Subscription paramSubscription) {
    if (SubscriptionHelper.validate(this.upstream, paramSubscription)) {
      this.upstream = paramSubscription;
      if (!this.cancelled) {
        paramSubscription.request(Long.MAX_VALUE);
        if (this.cancelled) {
          this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
          paramSubscription.cancel();
        } 
      } 
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/subscribers/BlockingBaseSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */