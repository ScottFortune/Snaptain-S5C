package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeCache<T> extends Maybe<T> implements MaybeObserver<T> {
  static final CacheDisposable[] EMPTY = new CacheDisposable[0];
  
  static final CacheDisposable[] TERMINATED = new CacheDisposable[0];
  
  Throwable error;
  
  final AtomicReference<CacheDisposable<T>[]> observers;
  
  final AtomicReference<MaybeSource<T>> source;
  
  T value;
  
  public MaybeCache(MaybeSource<T> paramMaybeSource) {
    this.source = new AtomicReference<MaybeSource<T>>(paramMaybeSource);
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
  
  public void onComplete() {
    for (CacheDisposable cacheDisposable : (CacheDisposable[])this.observers.getAndSet(TERMINATED)) {
      if (!cacheDisposable.isDisposed())
        cacheDisposable.downstream.onComplete(); 
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
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    MaybeSource maybeSource;
    CacheDisposable<T> cacheDisposable = new CacheDisposable<T>(paramMaybeObserver, this);
    paramMaybeObserver.onSubscribe(cacheDisposable);
    if (add(cacheDisposable)) {
      if (cacheDisposable.isDisposed()) {
        remove(cacheDisposable);
        return;
      } 
      maybeSource = this.source.getAndSet(null);
      if (maybeSource != null)
        maybeSource.subscribe(this); 
      return;
    } 
    if (!cacheDisposable.isDisposed()) {
      Throwable throwable = this.error;
      if (throwable != null) {
        maybeSource.onError(throwable);
      } else {
        T t = this.value;
        if (t != null) {
          maybeSource.onSuccess(t);
        } else {
          maybeSource.onComplete();
        } 
      } 
    } 
  }
  
  static final class CacheDisposable<T> extends AtomicReference<MaybeCache<T>> implements Disposable {
    private static final long serialVersionUID = -5791853038359966195L;
    
    final MaybeObserver<? super T> downstream;
    
    CacheDisposable(MaybeObserver<? super T> param1MaybeObserver, MaybeCache<T> param1MaybeCache) {
      super(param1MaybeCache);
      this.downstream = param1MaybeObserver;
    }
    
    public void dispose() {
      MaybeCache<T> maybeCache = getAndSet(null);
      if (maybeCache != null)
        maybeCache.remove(this); 
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (get() == null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */