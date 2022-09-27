package io.reactivex.internal.observers;

import io.reactivex.internal.fuseable.QueueDisposable;

public abstract class BasicQueueDisposable<T> implements QueueDisposable<T> {
  public final boolean offer(T paramT) {
    throw new UnsupportedOperationException("Should not be called");
  }
  
  public final boolean offer(T paramT1, T paramT2) {
    throw new UnsupportedOperationException("Should not be called");
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/observers/BasicQueueDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */