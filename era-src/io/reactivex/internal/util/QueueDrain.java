package io.reactivex.internal.util;

import org.reactivestreams.Subscriber;

public interface QueueDrain<T, U> {
  boolean accept(Subscriber<? super U> paramSubscriber, T paramT);
  
  boolean cancelled();
  
  boolean done();
  
  boolean enter();
  
  Throwable error();
  
  int leave(int paramInt);
  
  long produced(long paramLong);
  
  long requested();
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/util/QueueDrain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */