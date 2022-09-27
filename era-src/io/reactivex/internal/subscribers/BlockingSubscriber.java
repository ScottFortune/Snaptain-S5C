package io.reactivex.internal.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.NotificationLite;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;

public final class BlockingSubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<T>, Subscription {
  public static final Object TERMINATED = new Object();
  
  private static final long serialVersionUID = -4875965440900746268L;
  
  final Queue<Object> queue;
  
  public BlockingSubscriber(Queue<Object> paramQueue) {
    this.queue = paramQueue;
  }
  
  public void cancel() {
    if (SubscriptionHelper.cancel(this))
      this.queue.offer(TERMINATED); 
  }
  
  public boolean isCancelled() {
    boolean bool;
    if (get() == SubscriptionHelper.CANCELLED) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void onComplete() {
    this.queue.offer(NotificationLite.complete());
  }
  
  public void onError(Throwable paramThrowable) {
    this.queue.offer(NotificationLite.error(paramThrowable));
  }
  
  public void onNext(T paramT) {
    this.queue.offer(NotificationLite.next(paramT));
  }
  
  public void onSubscribe(Subscription paramSubscription) {
    if (SubscriptionHelper.setOnce(this, paramSubscription))
      this.queue.offer(NotificationLite.subscription(this)); 
  }
  
  public void request(long paramLong) {
    get().request(paramLong);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/subscribers/BlockingSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */