package io.reactivex.internal.subscriptions;

import io.reactivex.internal.fuseable.QueueSubscription;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Subscriber;

public final class ScalarSubscription<T> extends AtomicInteger implements QueueSubscription<T> {
  static final int CANCELLED = 2;
  
  static final int NO_REQUEST = 0;
  
  static final int REQUESTED = 1;
  
  private static final long serialVersionUID = -3830916580126663321L;
  
  final Subscriber<? super T> subscriber;
  
  final T value;
  
  public ScalarSubscription(Subscriber<? super T> paramSubscriber, T paramT) {
    this.subscriber = paramSubscriber;
    this.value = paramT;
  }
  
  public void cancel() {
    lazySet(2);
  }
  
  public void clear() {
    lazySet(1);
  }
  
  public boolean isCancelled() {
    boolean bool;
    if (get() == 2) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isEmpty() {
    boolean bool;
    if (get() != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean offer(T paramT) {
    throw new UnsupportedOperationException("Should not be called!");
  }
  
  public boolean offer(T paramT1, T paramT2) {
    throw new UnsupportedOperationException("Should not be called!");
  }
  
  public T poll() {
    if (get() == 0) {
      lazySet(1);
      return this.value;
    } 
    return null;
  }
  
  public void request(long paramLong) {
    if (!SubscriptionHelper.validate(paramLong))
      return; 
    if (compareAndSet(0, 1)) {
      Subscriber<? super T> subscriber = this.subscriber;
      subscriber.onNext(this.value);
      if (get() != 2)
        subscriber.onComplete(); 
    } 
  }
  
  public int requestFusion(int paramInt) {
    return paramInt & 0x1;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/subscriptions/ScalarSubscription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */