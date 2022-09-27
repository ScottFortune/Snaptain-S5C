package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableCache<T> extends AbstractFlowableWithUpstream<T, T> implements FlowableSubscriber<T> {
  static final CacheSubscription[] EMPTY = new CacheSubscription[0];
  
  static final CacheSubscription[] TERMINATED = new CacheSubscription[0];
  
  final int capacityHint;
  
  volatile boolean done;
  
  Throwable error;
  
  final Node<T> head;
  
  final AtomicBoolean once;
  
  volatile long size;
  
  final AtomicReference<CacheSubscription<T>[]> subscribers;
  
  Node<T> tail;
  
  int tailOffset;
  
  public FlowableCache(Flowable<T> paramFlowable, int paramInt) {
    super(paramFlowable);
    this.capacityHint = paramInt;
    this.once = new AtomicBoolean();
    Node<T> node = new Node(paramInt);
    this.head = node;
    this.tail = node;
    this.subscribers = new AtomicReference(EMPTY);
  }
  
  void add(CacheSubscription<T> paramCacheSubscription) {
    CacheSubscription[] arrayOfCacheSubscription1;
    CacheSubscription[] arrayOfCacheSubscription2;
    do {
      arrayOfCacheSubscription1 = (CacheSubscription[])this.subscribers.get();
      if (arrayOfCacheSubscription1 == TERMINATED)
        return; 
      int i = arrayOfCacheSubscription1.length;
      arrayOfCacheSubscription2 = new CacheSubscription[i + 1];
      System.arraycopy(arrayOfCacheSubscription1, 0, arrayOfCacheSubscription2, 0, i);
      arrayOfCacheSubscription2[i] = paramCacheSubscription;
    } while (!this.subscribers.compareAndSet(arrayOfCacheSubscription1, arrayOfCacheSubscription2));
  }
  
  long cachedEventCount() {
    return this.size;
  }
  
  boolean hasSubscribers() {
    boolean bool;
    if (((CacheSubscription[])this.subscribers.get()).length != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  boolean isConnected() {
    return this.once.get();
  }
  
  public void onComplete() {
    this.done = true;
    CacheSubscription[] arrayOfCacheSubscription = (CacheSubscription[])this.subscribers.getAndSet(TERMINATED);
    int i = arrayOfCacheSubscription.length;
    for (byte b = 0; b < i; b++)
      replay(arrayOfCacheSubscription[b]); 
  }
  
  public void onError(Throwable paramThrowable) {
    if (this.done) {
      RxJavaPlugins.onError(paramThrowable);
      return;
    } 
    this.error = paramThrowable;
    this.done = true;
    CacheSubscription[] arrayOfCacheSubscription = (CacheSubscription[])this.subscribers.getAndSet(TERMINATED);
    int i = arrayOfCacheSubscription.length;
    for (byte b = 0; b < i; b++)
      replay(arrayOfCacheSubscription[b]); 
  }
  
  public void onNext(T paramT) {
    int i = this.tailOffset;
    int j = this.capacityHint;
    byte b = 0;
    if (i == j) {
      Node<T> node = new Node(i);
      node.values[0] = paramT;
      this.tailOffset = 1;
      this.tail.next = node;
      this.tail = node;
    } else {
      this.tail.values[i] = paramT;
      this.tailOffset = i + 1;
    } 
    this.size++;
    CacheSubscription[] arrayOfCacheSubscription = (CacheSubscription[])this.subscribers.get();
    j = arrayOfCacheSubscription.length;
    while (b < j) {
      replay(arrayOfCacheSubscription[b]);
      b++;
    } 
  }
  
  public void onSubscribe(Subscription paramSubscription) {
    paramSubscription.request(Long.MAX_VALUE);
  }
  
  void remove(CacheSubscription<T> paramCacheSubscription) {
    CacheSubscription[] arrayOfCacheSubscription1;
    CacheSubscription[] arrayOfCacheSubscription2;
    do {
      byte b2;
      arrayOfCacheSubscription1 = (CacheSubscription[])this.subscribers.get();
      int i = arrayOfCacheSubscription1.length;
      if (i == 0)
        return; 
      byte b1 = -1;
      byte b = 0;
      while (true) {
        b2 = b1;
        if (b < i) {
          if (arrayOfCacheSubscription1[b] == paramCacheSubscription) {
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
        arrayOfCacheSubscription2 = EMPTY;
      } else {
        arrayOfCacheSubscription2 = new CacheSubscription[i - 1];
        System.arraycopy(arrayOfCacheSubscription1, 0, arrayOfCacheSubscription2, 0, b2);
        System.arraycopy(arrayOfCacheSubscription1, b2 + 1, arrayOfCacheSubscription2, b2, i - b2 - 1);
      } 
    } while (!this.subscribers.compareAndSet(arrayOfCacheSubscription1, arrayOfCacheSubscription2));
  }
  
  void replay(CacheSubscription<T> paramCacheSubscription) {
    if (paramCacheSubscription.getAndIncrement() != 0)
      return; 
    long l = paramCacheSubscription.index;
    int i = paramCacheSubscription.offset;
    Node<T> node = paramCacheSubscription.node;
    AtomicLong atomicLong = paramCacheSubscription.requested;
    Subscriber<? super T> subscriber = paramCacheSubscription.downstream;
    int j = this.capacityHint;
    int k = 1;
    while (true) {
      Throwable throwable;
      boolean bool = this.done;
      long l1 = this.size;
      boolean bool1 = false;
      if (l1 == l) {
        m = 1;
      } else {
        m = 0;
      } 
      if (bool && m) {
        paramCacheSubscription.node = null;
        throwable = this.error;
        if (throwable != null) {
          subscriber.onError(throwable);
        } else {
          subscriber.onComplete();
        } 
        return;
      } 
      if (!m) {
        l1 = atomicLong.get();
        if (l1 == Long.MIN_VALUE) {
          ((CacheSubscription)throwable).node = null;
          return;
        } 
        if (l1 != l) {
          if (i == j) {
            node = node.next;
            i = bool1;
          } 
          subscriber.onNext(node.values[i]);
          i++;
          l++;
          continue;
        } 
      } 
      ((CacheSubscription)throwable).index = l;
      ((CacheSubscription)throwable).offset = i;
      ((CacheSubscription)throwable).node = node;
      int m = throwable.addAndGet(-k);
      k = m;
      if (m == 0)
        break; 
    } 
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    CacheSubscription<T> cacheSubscription = new CacheSubscription<T>(paramSubscriber, this);
    paramSubscriber.onSubscribe(cacheSubscription);
    add(cacheSubscription);
    if (!this.once.get() && this.once.compareAndSet(false, true)) {
      this.source.subscribe(this);
    } else {
      replay(cacheSubscription);
    } 
  }
  
  static final class CacheSubscription<T> extends AtomicInteger implements Subscription {
    private static final long serialVersionUID = 6770240836423125754L;
    
    final Subscriber<? super T> downstream;
    
    long index;
    
    FlowableCache.Node<T> node;
    
    int offset;
    
    final FlowableCache<T> parent;
    
    final AtomicLong requested;
    
    CacheSubscription(Subscriber<? super T> param1Subscriber, FlowableCache<T> param1FlowableCache) {
      this.downstream = param1Subscriber;
      this.parent = param1FlowableCache;
      this.node = param1FlowableCache.head;
      this.requested = new AtomicLong();
    }
    
    public void cancel() {
      if (this.requested.getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE)
        this.parent.remove(this); 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.addCancel(this.requested, param1Long);
        this.parent.replay(this);
      } 
    }
  }
  
  static final class Node<T> {
    volatile Node<T> next;
    
    final T[] values;
    
    Node(int param1Int) {
      this.values = (T[])new Object[param1Int];
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */