package io.reactivex.internal.subscriptions;

import io.reactivex.exceptions.ProtocolViolationException;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;

public enum SubscriptionHelper implements Subscription {
  CANCELLED;
  
  static {
    $VALUES = new SubscriptionHelper[] { CANCELLED };
  }
  
  public static boolean cancel(AtomicReference<Subscription> paramAtomicReference) {
    Subscription subscription = paramAtomicReference.get();
    SubscriptionHelper subscriptionHelper = CANCELLED;
    if (subscription != subscriptionHelper) {
      Subscription subscription1 = paramAtomicReference.getAndSet(subscriptionHelper);
      if (subscription1 != CANCELLED) {
        if (subscription1 != null)
          subscription1.cancel(); 
        return true;
      } 
    } 
    return false;
  }
  
  public static void deferredRequest(AtomicReference<Subscription> paramAtomicReference, AtomicLong paramAtomicLong, long paramLong) {
    Subscription subscription = paramAtomicReference.get();
    if (subscription != null) {
      subscription.request(paramLong);
    } else if (validate(paramLong)) {
      BackpressureHelper.add(paramAtomicLong, paramLong);
      Subscription subscription1 = paramAtomicReference.get();
      if (subscription1 != null) {
        paramLong = paramAtomicLong.getAndSet(0L);
        if (paramLong != 0L)
          subscription1.request(paramLong); 
      } 
    } 
  }
  
  public static boolean deferredSetOnce(AtomicReference<Subscription> paramAtomicReference, AtomicLong paramAtomicLong, Subscription paramSubscription) {
    if (setOnce(paramAtomicReference, paramSubscription)) {
      long l = paramAtomicLong.getAndSet(0L);
      if (l != 0L)
        paramSubscription.request(l); 
      return true;
    } 
    return false;
  }
  
  public static boolean replace(AtomicReference<Subscription> paramAtomicReference, Subscription paramSubscription) {
    while (true) {
      Subscription subscription = paramAtomicReference.get();
      if (subscription == CANCELLED) {
        if (paramSubscription != null)
          paramSubscription.cancel(); 
        return false;
      } 
      if (paramAtomicReference.compareAndSet(subscription, paramSubscription))
        return true; 
    } 
  }
  
  public static void reportMoreProduced(long paramLong) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("More produced than requested: ");
    stringBuilder.append(paramLong);
    RxJavaPlugins.onError((Throwable)new ProtocolViolationException(stringBuilder.toString()));
  }
  
  public static void reportSubscriptionSet() {
    RxJavaPlugins.onError((Throwable)new ProtocolViolationException("Subscription already set!"));
  }
  
  public static boolean set(AtomicReference<Subscription> paramAtomicReference, Subscription paramSubscription) {
    while (true) {
      Subscription subscription = paramAtomicReference.get();
      if (subscription == CANCELLED) {
        if (paramSubscription != null)
          paramSubscription.cancel(); 
        return false;
      } 
      if (paramAtomicReference.compareAndSet(subscription, paramSubscription)) {
        if (subscription != null)
          subscription.cancel(); 
        return true;
      } 
    } 
  }
  
  public static boolean setOnce(AtomicReference<Subscription> paramAtomicReference, Subscription paramSubscription) {
    ObjectHelper.requireNonNull(paramSubscription, "s is null");
    if (!paramAtomicReference.compareAndSet(null, paramSubscription)) {
      paramSubscription.cancel();
      if (paramAtomicReference.get() != CANCELLED)
        reportSubscriptionSet(); 
      return false;
    } 
    return true;
  }
  
  public static boolean setOnce(AtomicReference<Subscription> paramAtomicReference, Subscription paramSubscription, long paramLong) {
    if (setOnce(paramAtomicReference, paramSubscription)) {
      paramSubscription.request(paramLong);
      return true;
    } 
    return false;
  }
  
  public static boolean validate(long paramLong) {
    if (paramLong <= 0L) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("n > 0 required but it was ");
      stringBuilder.append(paramLong);
      RxJavaPlugins.onError(new IllegalArgumentException(stringBuilder.toString()));
      return false;
    } 
    return true;
  }
  
  public static boolean validate(Subscription paramSubscription1, Subscription paramSubscription2) {
    if (paramSubscription2 == null) {
      RxJavaPlugins.onError(new NullPointerException("next is null"));
      return false;
    } 
    if (paramSubscription1 != null) {
      paramSubscription2.cancel();
      reportSubscriptionSet();
      return false;
    } 
    return true;
  }
  
  public void cancel() {}
  
  public void request(long paramLong) {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/subscriptions/SubscriptionHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */