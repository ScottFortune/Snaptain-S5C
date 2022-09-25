package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.internal.fuseable.ScalarCallable;
import io.reactivex.internal.subscriptions.ScalarSubscription;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableJust<T> extends Flowable<T> implements ScalarCallable<T> {
  private final T value;
  
  public FlowableJust(T paramT) {
    this.value = paramT;
  }
  
  public T call() {
    return this.value;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    paramSubscriber.onSubscribe((Subscription)new ScalarSubscription(paramSubscriber, this.value));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableJust.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */