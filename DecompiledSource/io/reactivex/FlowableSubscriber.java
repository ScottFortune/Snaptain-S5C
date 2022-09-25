package io.reactivex;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public interface FlowableSubscriber<T> extends Subscriber<T> {
  void onSubscribe(Subscription paramSubscription);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/FlowableSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */