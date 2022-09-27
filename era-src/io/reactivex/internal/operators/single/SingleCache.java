package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleCache<T> extends Single<T> implements SingleObserver<T> {
  static final CacheDisposable[] EMPTY = new CacheDisposable[0];
  
  static final CacheDisposable[] TERMINATED = new CacheDisposable[0];
  
  Throwable error;
  
  final AtomicReference<CacheDisposable<T>[]> observers;
  
  final SingleSource<? extends T> source;
  
  T value;
  
  final AtomicInteger wip;
  
  public SingleCache(SingleSource<? extends T> paramSingleSource) {
    this.source = paramSingleSource;
    this.wip = new AtomicInteger();
    this.observers = new AtomicReference(EMPTY);
  }
  
  boolean add(CacheDisposable<T> paramCacheDisposable) {
    while (true) {
      CacheDisposable[] arrayOfCacheDisposable1 = (CacheDisposable[])this.observers.get();
      if (arrayOfCacheDisposable1 == TERMINATED)
        return false; 
      int i = arrayOfCacheDisposable1.length;
      CacheDisposable[] arrayOfCacheDisposable2 = new CacheDisposable[i + 1];
      System.arraycopy(arrayOfCacheDisposable1, 0, arrayOfCacheDisposable2, 0, i);
      arrayOfCacheDisposable2[i] = paramCacheDisposable;
      if (this.observers.compareAndSet(arrayOfCacheDisposable1, arrayOfCacheDisposable2))
        return true; 
    } 
  }
  
  public void onError(Throwable paramThrowable) {
    this.error = paramThrowable;
    for (CacheDisposable cacheDisposable : (CacheDisposable[])this.observers.getAndSet(TERMINATED)) {
      if (!cacheDisposable.isDisposed())
        cacheDisposable.downstream.onError(paramThrowable); 
    } 
  }
  
  public void onSubscribe(Disposable paramDisposable) {}
  
  public void onSuccess(T paramT) {
    this.value = paramT;
    for (CacheDisposable cacheDisposable : (CacheDisposable[])this.observers.getAndSet(TERMINATED)) {
      if (!cacheDisposable.isDisposed())
        cacheDisposable.downstream.onSuccess(paramT); 
    } 
  }
  
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
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    CacheDisposable<T> cacheDisposable = new CacheDisposable<T>(paramSingleObserver, this);
    paramSingleObserver.onSubscribe(cacheDisposable);
    if (add(cacheDisposable)) {
      if (cacheDisposable.isDisposed())
        remove(cacheDisposable); 
      if (this.wip.getAndIncrement() == 0)
        this.source.subscribe(this); 
      return;
    } 
    Throwable throwable = this.error;
    if (throwable != null) {
      paramSingleObserver.onError(throwable);
    } else {
      paramSingleObserver.onSuccess(this.value);
    } 
  }
  
  static final class CacheDisposable<T> extends AtomicBoolean implements Disposable {
    private static final long serialVersionUID = 7514387411091976596L;
    
    final SingleObserver<? super T> downstream;
    
    final SingleCache<T> parent;
    
    CacheDisposable(SingleObserver<? super T> param1SingleObserver, SingleCache<T> param1SingleCache) {
      this.downstream = param1SingleObserver;
      this.parent = param1SingleCache;
    }
    
    public void dispose() {
      if (compareAndSet(false, true))
        this.parent.remove(this); 
    }
    
    public boolean isDisposed() {
      return get();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */