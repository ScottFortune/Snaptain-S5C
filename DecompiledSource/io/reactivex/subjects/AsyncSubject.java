package io.reactivex.subjects;

import io.reactivex.Observer;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.observers.DeferredScalarDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public final class AsyncSubject<T> extends Subject<T> {
  static final AsyncDisposable[] EMPTY = new AsyncDisposable[0];
  
  static final AsyncDisposable[] TERMINATED = new AsyncDisposable[0];
  
  Throwable error;
  
  final AtomicReference<AsyncDisposable<T>[]> subscribers = new AtomicReference(EMPTY);
  
  T value;
  
  @CheckReturnValue
  public static <T> AsyncSubject<T> create() {
    return new AsyncSubject<T>();
  }
  
  boolean add(AsyncDisposable<T> paramAsyncDisposable) {
    while (true) {
      AsyncDisposable[] arrayOfAsyncDisposable1 = (AsyncDisposable[])this.subscribers.get();
      if (arrayOfAsyncDisposable1 == TERMINATED)
        return false; 
      int i = arrayOfAsyncDisposable1.length;
      AsyncDisposable[] arrayOfAsyncDisposable2 = new AsyncDisposable[i + 1];
      System.arraycopy(arrayOfAsyncDisposable1, 0, arrayOfAsyncDisposable2, 0, i);
      arrayOfAsyncDisposable2[i] = paramAsyncDisposable;
      if (this.subscribers.compareAndSet(arrayOfAsyncDisposable1, arrayOfAsyncDisposable2))
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
  
  public boolean hasObservers() {
    boolean bool;
    if (((AsyncDisposable[])this.subscribers.get()).length != 0) {
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
    AsyncDisposable[] arrayOfAsyncDisposable1 = (AsyncDisposable[])this.subscribers.get();
    AsyncDisposable[] arrayOfAsyncDisposable2 = TERMINATED;
    if (arrayOfAsyncDisposable1 == arrayOfAsyncDisposable2)
      return; 
    T t = this.value;
    arrayOfAsyncDisposable2 = (AsyncDisposable[])this.subscribers.getAndSet(arrayOfAsyncDisposable2);
    int i = 0;
    int j = 0;
    if (t == null) {
      i = arrayOfAsyncDisposable2.length;
      while (j < i) {
        arrayOfAsyncDisposable2[j].onComplete();
        j++;
      } 
    } else {
      int k = arrayOfAsyncDisposable2.length;
      for (j = i; j < k; j++)
        arrayOfAsyncDisposable2[j].complete(t); 
    } 
  }
  
  public void onError(Throwable paramThrowable) {
    ObjectHelper.requireNonNull(paramThrowable, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
    AsyncDisposable[] arrayOfAsyncDisposable1 = (AsyncDisposable[])this.subscribers.get();
    AsyncDisposable[] arrayOfAsyncDisposable2 = TERMINATED;
    if (arrayOfAsyncDisposable1 == arrayOfAsyncDisposable2) {
      RxJavaPlugins.onError(paramThrowable);
      return;
    } 
    this.value = null;
    this.error = paramThrowable;
    arrayOfAsyncDisposable1 = (AsyncDisposable[])this.subscribers.getAndSet(arrayOfAsyncDisposable2);
    int i = arrayOfAsyncDisposable1.length;
    for (byte b = 0; b < i; b++)
      arrayOfAsyncDisposable1[b].onError(paramThrowable); 
  }
  
  public void onNext(T paramT) {
    ObjectHelper.requireNonNull(paramT, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
    if (this.subscribers.get() == TERMINATED)
      return; 
    this.value = paramT;
  }
  
  public void onSubscribe(Disposable paramDisposable) {
    if (this.subscribers.get() == TERMINATED)
      paramDisposable.dispose(); 
  }
  
  void remove(AsyncDisposable<T> paramAsyncDisposable) {
    AsyncDisposable[] arrayOfAsyncDisposable1;
    AsyncDisposable[] arrayOfAsyncDisposable2;
    do {
      byte b2;
      arrayOfAsyncDisposable1 = (AsyncDisposable[])this.subscribers.get();
      int i = arrayOfAsyncDisposable1.length;
      if (i == 0)
        return; 
      byte b1 = -1;
      byte b = 0;
      while (true) {
        b2 = b1;
        if (b < i) {
          if (arrayOfAsyncDisposable1[b] == paramAsyncDisposable) {
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
        arrayOfAsyncDisposable2 = EMPTY;
      } else {
        arrayOfAsyncDisposable2 = new AsyncDisposable[i - 1];
        System.arraycopy(arrayOfAsyncDisposable1, 0, arrayOfAsyncDisposable2, 0, b2);
        System.arraycopy(arrayOfAsyncDisposable1, b2 + 1, arrayOfAsyncDisposable2, b2, i - b2 - 1);
      } 
    } while (!this.subscribers.compareAndSet(arrayOfAsyncDisposable1, arrayOfAsyncDisposable2));
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    AsyncDisposable<T> asyncDisposable = new AsyncDisposable<T>(paramObserver, this);
    paramObserver.onSubscribe((Disposable)asyncDisposable);
    if (add(asyncDisposable)) {
      if (asyncDisposable.isDisposed())
        remove(asyncDisposable); 
    } else {
      Throwable throwable = this.error;
      if (throwable != null) {
        paramObserver.onError(throwable);
      } else {
        T t = this.value;
        if (t != null) {
          asyncDisposable.complete(t);
        } else {
          asyncDisposable.onComplete();
        } 
      } 
    } 
  }
  
  static final class AsyncDisposable<T> extends DeferredScalarDisposable<T> {
    private static final long serialVersionUID = 5629876084736248016L;
    
    final AsyncSubject<T> parent;
    
    AsyncDisposable(Observer<? super T> param1Observer, AsyncSubject<T> param1AsyncSubject) {
      super(param1Observer);
      this.parent = param1AsyncSubject;
    }
    
    public void dispose() {
      if (tryDispose())
        this.parent.remove(this); 
    }
    
    void onComplete() {
      if (!isDisposed())
        this.downstream.onComplete(); 
    }
    
    void onError(Throwable param1Throwable) {
      if (isDisposed()) {
        RxJavaPlugins.onError(param1Throwable);
      } else {
        this.downstream.onError(param1Throwable);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/subjects/AsyncSubject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */