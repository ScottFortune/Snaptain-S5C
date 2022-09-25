package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableCache<T> extends AbstractObservableWithUpstream<T, T> implements Observer<T> {
  static final CacheDisposable[] EMPTY = new CacheDisposable[0];
  
  static final CacheDisposable[] TERMINATED = new CacheDisposable[0];
  
  final int capacityHint;
  
  volatile boolean done;
  
  Throwable error;
  
  final Node<T> head;
  
  final AtomicReference<CacheDisposable<T>[]> observers;
  
  final AtomicBoolean once;
  
  volatile long size;
  
  Node<T> tail;
  
  int tailOffset;
  
  public ObservableCache(Observable<T> paramObservable, int paramInt) {
    super((ObservableSource<T>)paramObservable);
    this.capacityHint = paramInt;
    this.once = new AtomicBoolean();
    Node<T> node = new Node(paramInt);
    this.head = node;
    this.tail = node;
    this.observers = new AtomicReference(EMPTY);
  }
  
  void add(CacheDisposable<T> paramCacheDisposable) {
    CacheDisposable[] arrayOfCacheDisposable1;
    CacheDisposable[] arrayOfCacheDisposable2;
    do {
      arrayOfCacheDisposable1 = (CacheDisposable[])this.observers.get();
      if (arrayOfCacheDisposable1 == TERMINATED)
        return; 
      int i = arrayOfCacheDisposable1.length;
      arrayOfCacheDisposable2 = new CacheDisposable[i + 1];
      System.arraycopy(arrayOfCacheDisposable1, 0, arrayOfCacheDisposable2, 0, i);
      arrayOfCacheDisposable2[i] = paramCacheDisposable;
    } while (!this.observers.compareAndSet(arrayOfCacheDisposable1, arrayOfCacheDisposable2));
  }
  
  long cachedEventCount() {
    return this.size;
  }
  
  boolean hasObservers() {
    boolean bool;
    if (((CacheDisposable[])this.observers.get()).length != 0) {
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
    CacheDisposable[] arrayOfCacheDisposable = (CacheDisposable[])this.observers.getAndSet(TERMINATED);
    int i = arrayOfCacheDisposable.length;
    for (byte b = 0; b < i; b++)
      replay(arrayOfCacheDisposable[b]); 
  }
  
  public void onError(Throwable paramThrowable) {
    this.error = paramThrowable;
    this.done = true;
    CacheDisposable[] arrayOfCacheDisposable = (CacheDisposable[])this.observers.getAndSet(TERMINATED);
    int i = arrayOfCacheDisposable.length;
    for (byte b = 0; b < i; b++)
      replay(arrayOfCacheDisposable[b]); 
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
    CacheDisposable[] arrayOfCacheDisposable = (CacheDisposable[])this.observers.get();
    i = arrayOfCacheDisposable.length;
    while (b < i) {
      replay(arrayOfCacheDisposable[b]);
      b++;
    } 
  }
  
  public void onSubscribe(Disposable paramDisposable) {}
  
  void remove(CacheDisposable<T> paramCacheDisposable) {
    CacheDisposable[] arrayOfCacheDisposable1;
    CacheDisposable[] arrayOfCacheDisposable2;
    do {
      byte b2;
      arrayOfCacheDisposable1 = (CacheDisposable[])this.observers.get();
      int i = arrayOfCacheDisposable1.length;
      if (i == 0)
        return; 
      byte b1 = -1;
      byte b = 0;
      while (true) {
        b2 = b1;
        if (b < i) {
          if (arrayOfCacheDisposable1[b] == paramCacheDisposable) {
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
        arrayOfCacheDisposable2 = EMPTY;
      } else {
        arrayOfCacheDisposable2 = new CacheDisposable[i - 1];
        System.arraycopy(arrayOfCacheDisposable1, 0, arrayOfCacheDisposable2, 0, b2);
        System.arraycopy(arrayOfCacheDisposable1, b2 + 1, arrayOfCacheDisposable2, b2, i - b2 - 1);
      } 
    } while (!this.observers.compareAndSet(arrayOfCacheDisposable1, arrayOfCacheDisposable2));
  }
  
  void replay(CacheDisposable<T> paramCacheDisposable) {
    if (paramCacheDisposable.getAndIncrement() != 0)
      return; 
    long l = paramCacheDisposable.index;
    int i = paramCacheDisposable.offset;
    Node<T> node = paramCacheDisposable.node;
    Observer<? super T> observer = paramCacheDisposable.downstream;
    int j = this.capacityHint;
    int k = 1;
    while (true) {
      Throwable throwable;
      if (paramCacheDisposable.disposed) {
        paramCacheDisposable.node = null;
        return;
      } 
      boolean bool = this.done;
      if (this.size == l) {
        m = 1;
      } else {
        m = 0;
      } 
      if (bool && m) {
        paramCacheDisposable.node = null;
        throwable = this.error;
        if (throwable != null) {
          observer.onError(throwable);
        } else {
          observer.onComplete();
        } 
        return;
      } 
      if (!m) {
        m = i;
        Node<T> node1 = node;
        if (i == j) {
          node1 = node.next;
          m = 0;
        } 
        observer.onNext(node1.values[m]);
        i = m + 1;
        l++;
        node = node1;
        continue;
      } 
      ((CacheDisposable)throwable).index = l;
      ((CacheDisposable)throwable).offset = i;
      ((CacheDisposable)throwable).node = node;
      int m = throwable.addAndGet(-k);
      k = m;
      if (m == 0)
        break; 
    } 
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    CacheDisposable<T> cacheDisposable = new CacheDisposable<T>(paramObserver, this);
    paramObserver.onSubscribe(cacheDisposable);
    add(cacheDisposable);
    if (!this.once.get() && this.once.compareAndSet(false, true)) {
      this.source.subscribe(this);
    } else {
      replay(cacheDisposable);
    } 
  }
  
  static final class CacheDisposable<T> extends AtomicInteger implements Disposable {
    private static final long serialVersionUID = 6770240836423125754L;
    
    volatile boolean disposed;
    
    final Observer<? super T> downstream;
    
    long index;
    
    ObservableCache.Node<T> node;
    
    int offset;
    
    final ObservableCache<T> parent;
    
    CacheDisposable(Observer<? super T> param1Observer, ObservableCache<T> param1ObservableCache) {
      this.downstream = param1Observer;
      this.parent = param1ObservableCache;
      this.node = param1ObservableCache.head;
    }
    
    public void dispose() {
      if (!this.disposed) {
        this.disposed = true;
        this.parent.remove(this);
      } 
    }
    
    public boolean isDisposed() {
      return this.disposed;
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */