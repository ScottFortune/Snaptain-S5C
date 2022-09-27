package io.reactivex.processors;

import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class AsyncProcessor<T> extends FlowableProcessor<T> {
  static final AsyncSubscription[] EMPTY = new AsyncSubscription[0];
  
  static final AsyncSubscription[] TERMINATED = new AsyncSubscription[0];
  
  Throwable error;
  
  final AtomicReference<AsyncSubscription<T>[]> subscribers = new AtomicReference(EMPTY);
  
  T value;
  
  @CheckReturnValue
  public static <T> AsyncProcessor<T> create() {
    return new AsyncProcessor<T>();
  }
  
  boolean add(AsyncSubscription<T> paramAsyncSubscription) {
    while (true) {
      AsyncSubscription[] arrayOfAsyncSubscription1 = (AsyncSubscription[])this.subscribers.get();
      if (arrayOfAsyncSubscription1 == TERMINATED)
        return false; 
      int i = arrayOfAsyncSubscription1.length;
      AsyncSubscription[] arrayOfAsyncSubscription2 = new AsyncSubscription[i + 1];
      System.arraycopy(arrayOfAsyncSubscription1, 0, arrayOfAsyncSubscription2, 0, i);
      arrayOfAsyncSubscription2[i] = paramAsyncSubscription;
      if (this.subscribers.compareAndSet(arrayOfAsyncSubscription1, arrayOfAsyncSubscription2))
        return true; 
    } 
  }
  
  public Throwable getThrowable() {
    Throwable throwable;
    if (this.subscribers.get() == TERMINATED) {
      throwable = this.error;
    } else {
      throwable = null;
    } 
    return throwable;
  }
  
  public T getValue() {
    T t;
    if (this.subscribers.get() == TERMINATED) {
      T t1 = this.value;
    } else {
      t = null;
    } 
    return t;
  }
  
  @Deprecated
  public Object[] getValues() {
    Object[] arrayOfObject;
    T t = getValue();
    if (t != null) {
      arrayOfObject = new Object[1];
      arrayOfObject[0] = t;
    } else {
      arrayOfObject = new Object[0];
    } 
    return arrayOfObject;
  }
  
  @Deprecated
  public T[] getValues(T[] paramArrayOfT) {
    T t = getValue();
    if (t == null) {
      if (paramArrayOfT.length != 0)
        paramArrayOfT[0] = null; 
      return paramArrayOfT;
    } 
    T[] arrayOfT = paramArrayOfT;
    if (paramArrayOfT.length == 0)
      arrayOfT = Arrays.copyOf(paramArrayOfT, 1); 
    arrayOfT[0] = t;
    if (arrayOfT.length != 1)
      arrayOfT[1] = null; 
    return arrayOfT;
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
    if (((AsyncSubscription[])this.subscribers.get()).length != 0) {
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
  
  public boolean hasValue() {
    boolean bool;
    if (this.subscribers.get() == TERMINATED && this.value != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void onComplete() {
    AsyncSubscription[] arrayOfAsyncSubscription1 = (AsyncSubscription[])this.subscribers.get();
    AsyncSubscription[] arrayOfAsyncSubscription2 = TERMINATED;
    if (arrayOfAsyncSubscription1 == arrayOfAsyncSubscription2)
      return; 
    T t = this.value;
    arrayOfAsyncSubscription2 = (AsyncSubscription[])this.subscribers.getAndSet(arrayOfAsyncSubscription2);
    int i = 0;
    int j = 0;
    if (t == null) {
      i = arrayOfAsyncSubscription2.length;
      while (j < i) {
        arrayOfAsyncSubscription2[j].onComplete();
        j++;
      } 
    } else {
      int k = arrayOfAsyncSubscription2.length;
      for (j = i; j < k; j++)
        arrayOfAsyncSubscription2[j].complete(t); 
    } 
  }
  
  public void onError(Throwable paramThrowable) {
    ObjectHelper.requireNonNull(paramThrowable, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
    AsyncSubscription[] arrayOfAsyncSubscription1 = (AsyncSubscription[])this.subscribers.get();
    AsyncSubscription[] arrayOfAsyncSubscription2 = TERMINATED;
    if (arrayOfAsyncSubscription1 == arrayOfAsyncSubscription2) {
      RxJavaPlugins.onError(paramThrowable);
      return;
    } 
    this.value = null;
    this.error = paramThrowable;
    arrayOfAsyncSubscription1 = (AsyncSubscription[])this.subscribers.getAndSet(arrayOfAsyncSubscription2);
    int i = arrayOfAsyncSubscription1.length;
    for (byte b = 0; b < i; b++)
      arrayOfAsyncSubscription1[b].onError(paramThrowable); 
  }
  
  public void onNext(T paramT) {
    ObjectHelper.requireNonNull(paramT, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
    if (this.subscribers.get() == TERMINATED)
      return; 
    this.value = paramT;
  }
  
  public void onSubscribe(Subscription paramSubscription) {
    if (this.subscribers.get() == TERMINATED) {
      paramSubscription.cancel();
      return;
    } 
    paramSubscription.request(Long.MAX_VALUE);
  }
  
  void remove(AsyncSubscription<T> paramAsyncSubscription) {
    AsyncSubscription[] arrayOfAsyncSubscription1;
    AsyncSubscription[] arrayOfAsyncSubscription2;
    do {
      byte b2;
      arrayOfAsyncSubscription1 = (AsyncSubscription[])this.subscribers.get();
      int i = arrayOfAsyncSubscription1.length;
      if (i == 0)
        return; 
      byte b1 = -1;
      byte b = 0;
      while (true) {
        b2 = b1;
        if (b < i) {
          if (arrayOfAsyncSubscription1[b] == paramAsyncSubscription) {
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
        arrayOfAsyncSubscription2 = EMPTY;
      } else {
        arrayOfAsyncSubscription2 = new AsyncSubscription[i - 1];
        System.arraycopy(arrayOfAsyncSubscription1, 0, arrayOfAsyncSubscription2, 0, b2);
        System.arraycopy(arrayOfAsyncSubscription1, b2 + 1, arrayOfAsyncSubscription2, b2, i - b2 - 1);
      } 
    } while (!this.subscribers.compareAndSet(arrayOfAsyncSubscription1, arrayOfAsyncSubscription2));
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    AsyncSubscription<T> asyncSubscription = new AsyncSubscription<T>(paramSubscriber, this);
    paramSubscriber.onSubscribe((Subscription)asyncSubscription);
    if (add(asyncSubscription)) {
      if (asyncSubscription.isCancelled())
        remove(asyncSubscription); 
    } else {
      Throwable throwable = this.error;
      if (throwable != null) {
        paramSubscriber.onError(throwable);
      } else {
        T t = this.value;
        if (t != null) {
          asyncSubscription.complete(t);
        } else {
          asyncSubscription.onComplete();
        } 
      } 
    } 
  }
  
  static final class AsyncSubscription<T> extends DeferredScalarSubscription<T> {
    private static final long serialVersionUID = 5629876084736248016L;
    
    final AsyncProcessor<T> parent;
    
    AsyncSubscription(Subscriber<? super T> param1Subscriber, AsyncProcessor<T> param1AsyncProcessor) {
      super(param1Subscriber);
      this.parent = param1AsyncProcessor;
    }
    
    public void cancel() {
      if (tryCancel())
        this.parent.remove(this); 
    }
    
    void onComplete() {
      if (!isCancelled())
        this.downstream.onComplete(); 
    }
    
    void onError(Throwable param1Throwable) {
      if (isCancelled()) {
        RxJavaPlugins.onError(param1Throwable);
      } else {
        this.downstream.onError(param1Throwable);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/processors/AsyncProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */