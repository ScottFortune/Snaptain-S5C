package io.reactivex.subjects;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleSubject<T> extends Single<T> implements SingleObserver<T> {
  static final SingleDisposable[] EMPTY = new SingleDisposable[0];
  
  static final SingleDisposable[] TERMINATED = new SingleDisposable[0];
  
  Throwable error;
  
  final AtomicReference<SingleDisposable<T>[]> observers = new AtomicReference(EMPTY);
  
  final AtomicBoolean once = new AtomicBoolean();
  
  T value;
  
  @CheckReturnValue
  public static <T> SingleSubject<T> create() {
    return new SingleSubject<T>();
  }
  
  boolean add(SingleDisposable<T> paramSingleDisposable) {
    while (true) {
      SingleDisposable[] arrayOfSingleDisposable1 = (SingleDisposable[])this.observers.get();
      if (arrayOfSingleDisposable1 == TERMINATED)
        return false; 
      int i = arrayOfSingleDisposable1.length;
      SingleDisposable[] arrayOfSingleDisposable2 = new SingleDisposable[i + 1];
      System.arraycopy(arrayOfSingleDisposable1, 0, arrayOfSingleDisposable2, 0, i);
      arrayOfSingleDisposable2[i] = paramSingleDisposable;
      if (this.observers.compareAndSet(arrayOfSingleDisposable1, arrayOfSingleDisposable2))
        return true; 
    } 
  }
  
  public Throwable getThrowable() {
    return (this.observers.get() == TERMINATED) ? this.error : null;
  }
  
  public T getValue() {
    return (this.observers.get() == TERMINATED) ? this.value : null;
  }
  
  public boolean hasObservers() {
    boolean bool;
    if (((SingleDisposable[])this.observers.get()).length != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean hasThrowable() {
    boolean bool;
    if (this.observers.get() == TERMINATED && this.error != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean hasValue() {
    boolean bool;
    if (this.observers.get() == TERMINATED && this.value != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  int observerCount() {
    return ((SingleDisposable[])this.observers.get()).length;
  }
  
  public void onError(Throwable paramThrowable) {
    ObjectHelper.requireNonNull(paramThrowable, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
    AtomicBoolean atomicBoolean = this.once;
    byte b = 0;
    if (atomicBoolean.compareAndSet(false, true)) {
      this.error = paramThrowable;
      SingleDisposable[] arrayOfSingleDisposable = (SingleDisposable[])this.observers.getAndSet(TERMINATED);
      int i = arrayOfSingleDisposable.length;
      while (b < i) {
        (arrayOfSingleDisposable[b]).downstream.onError(paramThrowable);
        b++;
      } 
    } else {
      RxJavaPlugins.onError(paramThrowable);
    } 
  }
  
  public void onSubscribe(Disposable paramDisposable) {
    if (this.observers.get() == TERMINATED)
      paramDisposable.dispose(); 
  }
  
  public void onSuccess(T paramT) {
    ObjectHelper.requireNonNull(paramT, "onSuccess called with null. Null values are generally not allowed in 2.x operators and sources.");
    AtomicBoolean atomicBoolean = this.once;
    byte b = 0;
    if (atomicBoolean.compareAndSet(false, true)) {
      this.value = paramT;
      SingleDisposable[] arrayOfSingleDisposable = (SingleDisposable[])this.observers.getAndSet(TERMINATED);
      int i = arrayOfSingleDisposable.length;
      while (b < i) {
        (arrayOfSingleDisposable[b]).downstream.onSuccess(paramT);
        b++;
      } 
    } 
  }
  
  void remove(SingleDisposable<T> paramSingleDisposable) {
    SingleDisposable[] arrayOfSingleDisposable1;
    SingleDisposable[] arrayOfSingleDisposable2;
    do {
      byte b2;
      arrayOfSingleDisposable1 = (SingleDisposable[])this.observers.get();
      int i = arrayOfSingleDisposable1.length;
      if (i == 0)
        return; 
      byte b1 = -1;
      byte b = 0;
      while (true) {
        b2 = b1;
        if (b < i) {
          if (arrayOfSingleDisposable1[b] == paramSingleDisposable) {
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
        arrayOfSingleDisposable2 = EMPTY;
      } else {
        arrayOfSingleDisposable2 = new SingleDisposable[i - 1];
        System.arraycopy(arrayOfSingleDisposable1, 0, arrayOfSingleDisposable2, 0, b2);
        System.arraycopy(arrayOfSingleDisposable1, b2 + 1, arrayOfSingleDisposable2, b2, i - b2 - 1);
      } 
    } while (!this.observers.compareAndSet(arrayOfSingleDisposable1, arrayOfSingleDisposable2));
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    SingleDisposable<T> singleDisposable = new SingleDisposable<T>(paramSingleObserver, this);
    paramSingleObserver.onSubscribe(singleDisposable);
    if (add(singleDisposable)) {
      if (singleDisposable.isDisposed())
        remove(singleDisposable); 
    } else {
      Throwable throwable = this.error;
      if (throwable != null) {
        paramSingleObserver.onError(throwable);
      } else {
        paramSingleObserver.onSuccess(this.value);
      } 
    } 
  }
  
  static final class SingleDisposable<T> extends AtomicReference<SingleSubject<T>> implements Disposable {
    private static final long serialVersionUID = -7650903191002190468L;
    
    final SingleObserver<? super T> downstream;
    
    SingleDisposable(SingleObserver<? super T> param1SingleObserver, SingleSubject<T> param1SingleSubject) {
      this.downstream = param1SingleObserver;
      lazySet(param1SingleSubject);
    }
    
    public void dispose() {
      SingleSubject<T> singleSubject = getAndSet(null);
      if (singleSubject != null)
        singleSubject.remove(this); 
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/subjects/SingleSubject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */