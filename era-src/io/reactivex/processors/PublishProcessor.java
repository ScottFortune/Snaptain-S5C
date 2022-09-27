package io.reactivex.processors;

import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class PublishProcessor<T> extends FlowableProcessor<T> {
  static final PublishSubscription[] EMPTY;
  
  static final PublishSubscription[] TERMINATED = new PublishSubscription[0];
  
  Throwable error;
  
  final AtomicReference<PublishSubscription<T>[]> subscribers = new AtomicReference(EMPTY);
  
  static {
    EMPTY = new PublishSubscription[0];
  }
  
  @CheckReturnValue
  public static <T> PublishProcessor<T> create() {
    return new PublishProcessor<T>();
  }
  
  boolean add(PublishSubscription<T> paramPublishSubscription) {
    while (true) {
      PublishSubscription[] arrayOfPublishSubscription1 = (PublishSubscription[])this.subscribers.get();
      if (arrayOfPublishSubscription1 == TERMINATED)
        return false; 
      int i = arrayOfPublishSubscription1.length;
      PublishSubscription[] arrayOfPublishSubscription2 = new PublishSubscription[i + 1];
      System.arraycopy(arrayOfPublishSubscription1, 0, arrayOfPublishSubscription2, 0, i);
      arrayOfPublishSubscription2[i] = paramPublishSubscription;
      if (this.subscribers.compareAndSet(arrayOfPublishSubscription1, arrayOfPublishSubscription2))
        return true; 
    } 
  }
  
  public Throwable getThrowable() {
    return (this.subscribers.get() == TERMINATED) ? this.error : null;
  }
  
  public boolean hasComplete() {
    boolean bool;
    if (this.subscribers.get() == TERMINATED && this.error == null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean hasSubscribers() {
    boolean bool;
    if (((PublishSubscription[])this.subscribers.get()).length != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean hasThrowable() {
    boolean bool;
    if (this.subscribers.get() == TERMINATED && this.error != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean offer(T paramT) {
    if (paramT == null) {
      onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
      return true;
    } 
    PublishSubscription[] arrayOfPublishSubscription = (PublishSubscription[])this.subscribers.get();
    int i = arrayOfPublishSubscription.length;
    boolean bool = false;
    byte b;
    for (b = 0; b < i; b++) {
      if (arrayOfPublishSubscription[b].isFull())
        return false; 
    } 
    i = arrayOfPublishSubscription.length;
    for (b = bool; b < i; b++)
      arrayOfPublishSubscription[b].onNext(paramT); 
    return true;
  }
  
  public void onComplete() {
    PublishSubscription[] arrayOfPublishSubscription1 = (PublishSubscription[])this.subscribers.get();
    PublishSubscription[] arrayOfPublishSubscription2 = TERMINATED;
    if (arrayOfPublishSubscription1 == arrayOfPublishSubscription2)
      return; 
    arrayOfPublishSubscription1 = (PublishSubscription[])this.subscribers.getAndSet(arrayOfPublishSubscription2);
    int i = arrayOfPublishSubscription1.length;
    for (byte b = 0; b < i; b++)
      arrayOfPublishSubscription1[b].onComplete(); 
  }
  
  public void onError(Throwable paramThrowable) {
    ObjectHelper.requireNonNull(paramThrowable, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
    PublishSubscription[] arrayOfPublishSubscription1 = (PublishSubscription[])this.subscribers.get();
    PublishSubscription[] arrayOfPublishSubscription2 = TERMINATED;
    if (arrayOfPublishSubscription1 == arrayOfPublishSubscription2) {
      RxJavaPlugins.onError(paramThrowable);
      return;
    } 
    this.error = paramThrowable;
    arrayOfPublishSubscription1 = (PublishSubscription[])this.subscribers.getAndSet(arrayOfPublishSubscription2);
    int i = arrayOfPublishSubscription1.length;
    for (byte b = 0; b < i; b++)
      arrayOfPublishSubscription1[b].onError(paramThrowable); 
  }
  
  public void onNext(T paramT) {
    ObjectHelper.requireNonNull(paramT, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
    PublishSubscription[] arrayOfPublishSubscription = (PublishSubscription[])this.subscribers.get();
    int i = arrayOfPublishSubscription.length;
    for (byte b = 0; b < i; b++)
      arrayOfPublishSubscription[b].onNext(paramT); 
  }
  
  public void onSubscribe(Subscription paramSubscription) {
    if (this.subscribers.get() == TERMINATED) {
      paramSubscription.cancel();
      return;
    } 
    paramSubscription.request(Long.MAX_VALUE);
  }
  
  void remove(PublishSubscription<T> paramPublishSubscription) {
    PublishSubscription[] arrayOfPublishSubscription1;
    PublishSubscription[] arrayOfPublishSubscription2;
    do {
      byte b2;
      arrayOfPublishSubscription1 = (PublishSubscription[])this.subscribers.get();
      if (arrayOfPublishSubscription1 == TERMINATED || arrayOfPublishSubscription1 == EMPTY)
        break; 
      int i = arrayOfPublishSubscription1.length;
      byte b1 = -1;
      byte b = 0;
      while (true) {
        b2 = b1;
        if (b < i) {
          if (arrayOfPublishSubscription1[b] == paramPublishSubscription) {
            b2 = b;
            break;
          } 
          b++;
          continue;
        } 
        break;
      } 
      if (b2 < 0)
        return; 
      if (i == 1) {
        arrayOfPublishSubscription2 = EMPTY;
      } else {
        arrayOfPublishSubscription2 = new PublishSubscription[i - 1];
        System.arraycopy(arrayOfPublishSubscription1, 0, arrayOfPublishSubscription2, 0, b2);
        System.arraycopy(arrayOfPublishSubscription1, b2 + 1, arrayOfPublishSubscription2, b2, i - b2 - 1);
      } 
    } while (!this.subscribers.compareAndSet(arrayOfPublishSubscription1, arrayOfPublishSubscription2));
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    PublishSubscription<T> publishSubscription = new PublishSubscription<T>(paramSubscriber, this);
    paramSubscriber.onSubscribe(publishSubscription);
    if (add(publishSubscription)) {
      if (publishSubscription.isCancelled())
        remove(publishSubscription); 
    } else {
      Throwable throwable = this.error;
      if (throwable != null) {
        paramSubscriber.onError(throwable);
      } else {
        paramSubscriber.onComplete();
      } 
    } 
  }
  
  static final class PublishSubscription<T> extends AtomicLong implements Subscription {
    private static final long serialVersionUID = 3562861878281475070L;
    
    final Subscriber<? super T> downstream;
    
    final PublishProcessor<T> parent;
    
    PublishSubscription(Subscriber<? super T> param1Subscriber, PublishProcessor<T> param1PublishProcessor) {
      this.downstream = param1Subscriber;
      this.parent = param1PublishProcessor;
    }
    
    public void cancel() {
      if (getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE)
        this.parent.remove(this); 
    }
    
    public boolean isCancelled() {
      boolean bool;
      if (get() == Long.MIN_VALUE) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean isFull() {
      boolean bool;
      if (get() == 0L) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onComplete() {
      if (get() != Long.MIN_VALUE)
        this.downstream.onComplete(); 
    }
    
    public void onError(Throwable param1Throwable) {
      if (get() != Long.MIN_VALUE) {
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      long l = get();
      if (l == Long.MIN_VALUE)
        return; 
      if (l != 0L) {
        this.downstream.onNext(param1T);
        BackpressureHelper.producedCancel(this, 1L);
      } else {
        cancel();
        this.downstream.onError((Throwable)new MissingBackpressureException("Could not emit value due to lack of requests"));
      } 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long))
        BackpressureHelper.addCancel(this, param1Long); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/processors/PublishProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */