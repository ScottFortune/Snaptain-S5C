package org.reactivestreams;

public interface Subscriber<T> {
  void onComplete();
  
  void onError(Throwable paramThrowable);
  
  void onNext(T paramT);
  
  void onSubscribe(Subscription paramSubscription);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/org/reactivestreams/Subscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */