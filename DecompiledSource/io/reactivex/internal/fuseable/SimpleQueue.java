package io.reactivex.internal.fuseable;

public interface SimpleQueue<T> {
  void clear();
  
  boolean isEmpty();
  
  boolean offer(T paramT);
  
  boolean offer(T paramT1, T paramT2);
  
  T poll() throws Exception;
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/fuseable/SimpleQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */