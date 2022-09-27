package io.reactivex.internal.subscriptions;

import io.reactivex.disposables.Disposable;
import java.util.concurrent.atomic.AtomicReferenceArray;
import org.reactivestreams.Subscription;

public final class ArrayCompositeSubscription extends AtomicReferenceArray<Subscription> implements Disposable {
  private static final long serialVersionUID = 2746389416410565408L;
  
  public ArrayCompositeSubscription(int paramInt) {
    super(paramInt);
  }
  
  public void dispose() {
    byte b = 0;
    if (get(0) != SubscriptionHelper.CANCELLED) {
      int i = length();
      while (b < i) {
        if (get(b) != SubscriptionHelper.CANCELLED) {
          Subscription subscription = getAndSet(b, SubscriptionHelper.CANCELLED);
          if (subscription != SubscriptionHelper.CANCELLED && subscription != null)
            subscription.cancel(); 
        } 
        b++;
      } 
    } 
  }
  
  public boolean isDisposed() {
    boolean bool = false;
    if (get(0) == SubscriptionHelper.CANCELLED)
      bool = true; 
    return bool;
  }
  
  public Subscription replaceResource(int paramInt, Subscription paramSubscription) {
    while (true) {
      Subscription subscription = get(paramInt);
      if (subscription == SubscriptionHelper.CANCELLED) {
        if (paramSubscription != null)
          paramSubscription.cancel(); 
        return null;
      } 
      if (compareAndSet(paramInt, subscription, paramSubscription))
        return subscription; 
    } 
  }
  
  public boolean setResource(int paramInt, Subscription paramSubscription) {
    while (true) {
      Subscription subscription = get(paramInt);
      if (subscription == SubscriptionHelper.CANCELLED) {
        if (paramSubscription != null)
          paramSubscription.cancel(); 
        return false;
      } 
      if (compareAndSet(paramInt, subscription, paramSubscription)) {
        if (subscription != null)
          subscription.cancel(); 
        return true;
      } 
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/subscriptions/ArrayCompositeSubscription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */