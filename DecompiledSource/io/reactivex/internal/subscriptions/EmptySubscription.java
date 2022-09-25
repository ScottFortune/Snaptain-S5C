package io.reactivex.internal.subscriptions;

import io.reactivex.internal.fuseable.QueueSubscription;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public enum EmptySubscription implements QueueSubscription<Object> {
  INSTANCE;
  
  static {
    $VALUES = new EmptySubscription[] { INSTANCE };
  }
  
  public static void complete(Subscriber<?> paramSubscriber) {
    paramSubscriber.onSubscribe((Subscription)INSTANCE);
    paramSubscriber.onComplete();
  }
  
  public static void error(Throwable paramThrowable, Subscriber<?> paramSubscriber) {
    paramSubscriber.onSubscribe((Subscription)INSTANCE);
    paramSubscriber.onError(paramThrowable);
  }
  
  public void cancel() {}
  
  public void clear() {}
  
  public boolean isEmpty() {
    return true;
  }
  
  public boolean offer(Object paramObject) {
    throw new UnsupportedOperationException("Should not be called!");
  }
  
  public boolean offer(Object paramObject1, Object paramObject2) {
    throw new UnsupportedOperationException("Should not be called!");
  }
  
  public Object poll() {
    return null;
  }
  
  public void request(long paramLong) {
    SubscriptionHelper.validate(paramLong);
  }
  
  public int requestFusion(int paramInt) {
    return paramInt & 0x2;
  }
  
  public String toString() {
    return "EmptySubscription";
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/subscriptions/EmptySubscription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */