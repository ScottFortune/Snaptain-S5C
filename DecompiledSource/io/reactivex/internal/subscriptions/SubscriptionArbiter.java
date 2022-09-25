package io.reactivex.internal.subscriptions;

import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;

public class SubscriptionArbiter extends AtomicInteger implements Subscription {
  private static final long serialVersionUID = -2189523197179400958L;
  
  Subscription actual;
  
  final boolean cancelOnReplace;
  
  volatile boolean cancelled;
  
  final AtomicLong missedProduced;
  
  final AtomicLong missedRequested;
  
  final AtomicReference<Subscription> missedSubscription;
  
  long requested;
  
  protected boolean unbounded;
  
  public SubscriptionArbiter(boolean paramBoolean) {
    this.cancelOnReplace = paramBoolean;
    this.missedSubscription = new AtomicReference<Subscription>();
    this.missedRequested = new AtomicLong();
    this.missedProduced = new AtomicLong();
  }
  
  public void cancel() {
    if (!this.cancelled) {
      this.cancelled = true;
      drain();
    } 
  }
  
  final void drain() {
    if (getAndIncrement() != 0)
      return; 
    drainLoop();
  }
  
  final void drainLoop() {
    Subscription subscription = null;
    long l = 0L;
    int i = 1;
    while (true) {
      Subscription subscription1 = this.missedSubscription.get();
      Subscription subscription2 = subscription1;
      if (subscription1 != null)
        subscription2 = this.missedSubscription.getAndSet(null); 
      long l1 = this.missedRequested.get();
      long l2 = l1;
      if (l1 != 0L)
        l2 = this.missedRequested.getAndSet(0L); 
      l1 = this.missedProduced.get();
      long l3 = l1;
      if (l1 != 0L)
        l3 = this.missedProduced.getAndSet(0L); 
      Subscription subscription3 = this.actual;
      if (this.cancelled) {
        if (subscription3 != null) {
          subscription3.cancel();
          this.actual = null;
        } 
        l1 = l;
        subscription1 = subscription;
        if (subscription2 != null) {
          subscription2.cancel();
          l1 = l;
          subscription1 = subscription;
        } 
      } else {
        l1 = this.requested;
        long l4 = l1;
        if (l1 != Long.MAX_VALUE) {
          l4 = BackpressureHelper.addCap(l1, l2);
          l1 = l4;
          if (l4 != Long.MAX_VALUE) {
            l3 = l4 - l3;
            l1 = l3;
            if (l3 < 0L) {
              SubscriptionHelper.reportMoreProduced(l3);
              l1 = 0L;
            } 
          } 
          this.requested = l1;
          l4 = l1;
        } 
        if (subscription2 != null) {
          if (subscription3 != null && this.cancelOnReplace)
            subscription3.cancel(); 
          this.actual = subscription2;
          l1 = l;
          subscription1 = subscription;
          if (l4 != 0L) {
            l1 = BackpressureHelper.addCap(l, l4);
            subscription1 = subscription2;
          } 
        } else {
          l1 = l;
          subscription1 = subscription;
          if (subscription3 != null) {
            l1 = l;
            subscription1 = subscription;
            if (l2 != 0L) {
              l1 = BackpressureHelper.addCap(l, l2);
              subscription1 = subscription3;
            } 
          } 
        } 
      } 
      int j = addAndGet(-i);
      l = l1;
      i = j;
      subscription = subscription1;
      if (j == 0) {
        if (l1 != 0L)
          subscription1.request(l1); 
        return;
      } 
    } 
  }
  
  public final boolean isCancelled() {
    return this.cancelled;
  }
  
  public final boolean isUnbounded() {
    return this.unbounded;
  }
  
  public final void produced(long paramLong) {
    if (this.unbounded)
      return; 
    if (get() == 0 && compareAndSet(0, 1)) {
      long l = this.requested;
      if (l != Long.MAX_VALUE) {
        paramLong = l - paramLong;
        l = 0L;
        if (paramLong < 0L) {
          SubscriptionHelper.reportMoreProduced(paramLong);
          paramLong = l;
        } 
        this.requested = paramLong;
      } 
      if (decrementAndGet() == 0)
        return; 
      drainLoop();
      return;
    } 
    BackpressureHelper.add(this.missedProduced, paramLong);
    drain();
  }
  
  public final void request(long paramLong) {
    if (SubscriptionHelper.validate(paramLong)) {
      if (this.unbounded)
        return; 
      if (get() == 0 && compareAndSet(0, 1)) {
        long l = this.requested;
        if (l != Long.MAX_VALUE) {
          l = BackpressureHelper.addCap(l, paramLong);
          this.requested = l;
          if (l == Long.MAX_VALUE)
            this.unbounded = true; 
        } 
        Subscription subscription = this.actual;
        if (decrementAndGet() != 0)
          drainLoop(); 
        if (subscription != null)
          subscription.request(paramLong); 
        return;
      } 
      BackpressureHelper.add(this.missedRequested, paramLong);
      drain();
    } 
  }
  
  public final void setSubscription(Subscription paramSubscription) {
    if (this.cancelled) {
      paramSubscription.cancel();
      return;
    } 
    ObjectHelper.requireNonNull(paramSubscription, "s is null");
    if (get() == 0 && compareAndSet(0, 1)) {
      Subscription subscription = this.actual;
      if (subscription != null && this.cancelOnReplace)
        subscription.cancel(); 
      this.actual = paramSubscription;
      long l = this.requested;
      if (decrementAndGet() != 0)
        drainLoop(); 
      if (l != 0L)
        paramSubscription.request(l); 
      return;
    } 
    paramSubscription = this.missedSubscription.getAndSet(paramSubscription);
    if (paramSubscription != null && this.cancelOnReplace)
      paramSubscription.cancel(); 
    drain();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/subscriptions/SubscriptionArbiter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */