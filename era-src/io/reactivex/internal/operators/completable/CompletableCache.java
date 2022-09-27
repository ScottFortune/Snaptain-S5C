package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class CompletableCache extends Completable implements CompletableObserver {
  static final InnerCompletableCache[] EMPTY = new InnerCompletableCache[0];
  
  static final InnerCompletableCache[] TERMINATED = new InnerCompletableCache[0];
  
  Throwable error;
  
  final AtomicReference<InnerCompletableCache[]> observers;
  
  final AtomicBoolean once;
  
  final CompletableSource source;
  
  public CompletableCache(CompletableSource paramCompletableSource) {
    this.source = paramCompletableSource;
    this.observers = (AtomicReference)new AtomicReference<InnerCompletableCache>(EMPTY);
    this.once = new AtomicBoolean();
  }
  
  boolean add(InnerCompletableCache paramInnerCompletableCache) {
    while (true) {
      InnerCompletableCache[] arrayOfInnerCompletableCache1 = this.observers.get();
      if (arrayOfInnerCompletableCache1 == TERMINATED)
        return false; 
      int i = arrayOfInnerCompletableCache1.length;
      InnerCompletableCache[] arrayOfInnerCompletableCache2 = new InnerCompletableCache[i + 1];
      System.arraycopy(arrayOfInnerCompletableCache1, 0, arrayOfInnerCompletableCache2, 0, i);
      arrayOfInnerCompletableCache2[i] = paramInnerCompletableCache;
      if (this.observers.compareAndSet(arrayOfInnerCompletableCache1, arrayOfInnerCompletableCache2))
        return true; 
    } 
  }
  
  public void onComplete() {
    for (InnerCompletableCache innerCompletableCache : (InnerCompletableCache[])this.observers.getAndSet(TERMINATED)) {
      if (!innerCompletableCache.get())
        innerCompletableCache.downstream.onComplete(); 
    } 
  }
  
  public void onError(Throwable paramThrowable) {
    this.error = paramThrowable;
    for (InnerCompletableCache innerCompletableCache : (InnerCompletableCache[])this.observers.getAndSet(TERMINATED)) {
      if (!innerCompletableCache.get())
        innerCompletableCache.downstream.onError(paramThrowable); 
    } 
  }
  
  public void onSubscribe(Disposable paramDisposable) {}
  
  void remove(InnerCompletableCache paramInnerCompletableCache) {
    InnerCompletableCache[] arrayOfInnerCompletableCache1;
    InnerCompletableCache[] arrayOfInnerCompletableCache2;
    do {
      byte b2;
      arrayOfInnerCompletableCache1 = this.observers.get();
      int i = arrayOfInnerCompletableCache1.length;
      if (i == 0)
        return; 
      byte b1 = -1;
      byte b = 0;
      while (true) {
        b2 = b1;
        if (b < i) {
          if (arrayOfInnerCompletableCache1[b] == paramInnerCompletableCache) {
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
        arrayOfInnerCompletableCache2 = EMPTY;
      } else {
        arrayOfInnerCompletableCache2 = new InnerCompletableCache[i - 1];
        System.arraycopy(arrayOfInnerCompletableCache1, 0, arrayOfInnerCompletableCache2, 0, b2);
        System.arraycopy(arrayOfInnerCompletableCache1, b2 + 1, arrayOfInnerCompletableCache2, b2, i - b2 - 1);
      } 
    } while (!this.observers.compareAndSet(arrayOfInnerCompletableCache1, arrayOfInnerCompletableCache2));
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    InnerCompletableCache innerCompletableCache = new InnerCompletableCache(paramCompletableObserver);
    paramCompletableObserver.onSubscribe(innerCompletableCache);
    if (add(innerCompletableCache)) {
      if (innerCompletableCache.isDisposed())
        remove(innerCompletableCache); 
      if (this.once.compareAndSet(false, true))
        this.source.subscribe(this); 
    } else {
      Throwable throwable = this.error;
      if (throwable != null) {
        paramCompletableObserver.onError(throwable);
      } else {
        paramCompletableObserver.onComplete();
      } 
    } 
  }
  
  final class InnerCompletableCache extends AtomicBoolean implements Disposable {
    private static final long serialVersionUID = 8943152917179642732L;
    
    final CompletableObserver downstream;
    
    InnerCompletableCache(CompletableObserver param1CompletableObserver) {
      this.downstream = param1CompletableObserver;
    }
    
    public void dispose() {
      if (compareAndSet(false, true))
        CompletableCache.this.remove(this); 
    }
    
    public boolean isDisposed() {
      return get();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */