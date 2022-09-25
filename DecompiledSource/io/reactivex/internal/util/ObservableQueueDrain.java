package io.reactivex.internal.util;

import io.reactivex.Observer;

public interface ObservableQueueDrain<T, U> {
  void accept(Observer<? super U> paramObserver, T paramT);
  
  boolean cancelled();
  
  boolean done();
  
  boolean enter();
  
  Throwable error();
  
  int leave(int paramInt);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/util/ObservableQueueDrain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */