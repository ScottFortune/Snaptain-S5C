package io.reactivex.internal.subscriptions;

import java.util.concurrent.atomic.AtomicBoolean;
import org.reactivestreams.Subscription;

public final class BooleanSubscription extends AtomicBoolean implements Subscription {
  private static final long serialVersionUID = -8127758972444290902L;
  
  public void cancel() {
    lazySet(true);
  }
  
  public boolean isCancelled() {
    return get();
  }
  
  public void request(long paramLong) {
    SubscriptionHelper.validate(paramLong);
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("BooleanSubscription(cancelled=");
    stringBuilder.append(get());
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/subscriptions/BooleanSubscription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */