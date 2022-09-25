package io.reactivex.internal.subscriptions;

import org.reactivestreams.Subscriber;

public class DeferredScalarSubscription<T> extends BasicIntQueueSubscription<T> {
  static final int CANCELLED = 4;
  
  static final int FUSED_CONSUMED = 32;
  
  static final int FUSED_EMPTY = 8;
  
  static final int FUSED_READY = 16;
  
  static final int HAS_REQUEST_HAS_VALUE = 3;
  
  static final int HAS_REQUEST_NO_VALUE = 2;
  
  static final int NO_REQUEST_HAS_VALUE = 1;
  
  static final int NO_REQUEST_NO_VALUE = 0;
  
  private static final long serialVersionUID = -2151279923272604993L;
  
  protected final Subscriber<? super T> downstream;
  
  protected T value;
  
  public DeferredScalarSubscription(Subscriber<? super T> paramSubscriber) {
    this.downstream = paramSubscriber;
  }
  
  public void cancel() {
    set(4);
    this.value = null;
  }
  
  public final void clear() {
    lazySet(32);
    this.value = null;
  }
  
  public final void complete(T paramT) {
    int i = get();
    while (true) {
      if (i == 8) {
        this.value = paramT;
        lazySet(16);
        Subscriber<? super T> subscriber = this.downstream;
        subscriber.onNext(paramT);
        if (get() != 4)
          subscriber.onComplete(); 
        return;
      } 
      if ((i & 0xFFFFFFFD) != 0)
        return; 
      if (i == 2) {
        lazySet(3);
        Subscriber<? super T> subscriber = this.downstream;
        subscriber.onNext(paramT);
        if (get() != 4)
          subscriber.onComplete(); 
        return;
      } 
      this.value = paramT;
      if (compareAndSet(0, 1))
        return; 
      int j = get();
      i = j;
      if (j == 4) {
        this.value = null;
        return;
      } 
    } 
  }
  
  public final boolean isCancelled() {
    boolean bool;
    if (get() == 4) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public final boolean isEmpty() {
    boolean bool;
    if (get() != 16) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public final T poll() {
    if (get() == 16) {
      lazySet(32);
      T t = this.value;
      this.value = null;
      return t;
    } 
    return null;
  }
  
  public final void request(long paramLong) {
    if (SubscriptionHelper.validate(paramLong))
      do {
        int i = get();
        if ((i & 0xFFFFFFFE) != 0)
          return; 
        if (i == 1) {
          if (compareAndSet(1, 3)) {
            T t = this.value;
            if (t != null) {
              this.value = null;
              Subscriber<? super T> subscriber = this.downstream;
              subscriber.onNext(t);
              if (get() != 4)
                subscriber.onComplete(); 
            } 
          } 
          return;
        } 
      } while (!compareAndSet(0, 2)); 
  }
  
  public final int requestFusion(int paramInt) {
    if ((paramInt & 0x2) != 0) {
      lazySet(8);
      return 2;
    } 
    return 0;
  }
  
  public final boolean tryCancel() {
    boolean bool;
    if (getAndSet(4) != 4) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/subscriptions/DeferredScalarSubscription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */