package org.reactivestreams;

public interface Subscription {
  void cancel();
  
  void request(long paramLong);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/org/reactivestreams/Subscription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */