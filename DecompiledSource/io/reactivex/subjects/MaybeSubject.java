package io.reactivex.subjects;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeSubject<T> extends Maybe<T> implements MaybeObserver<T> {
  static final MaybeDisposable[] EMPTY = new MaybeDisposable[0];
  
  static final MaybeDisposable[] TERMINATED = new MaybeDisposable[0];
  
  Throwable error;
  
  final AtomicReference<MaybeDisposable<T>[]> observers = new AtomicReference(EMPTY);
  
  final AtomicBoolean once = new AtomicBoolean();
  
  T value;
  
  @CheckReturnValue
  public static <T> MaybeSubject<T> create() {
    return new MaybeSubject<T>();
  }
  
  boolean add(MaybeDisposable<T> paramMaybeDisposable) {
    while (true) {
      MaybeDisposable[] arrayOfMaybeDisposable1 = (MaybeDisposable[])this.observers.get();
      if (arrayOfMaybeDisposable1 == TERMINATED)
        return false; 
      int i = arrayOfMaybeDisposable1.length;
      MaybeDisposable[] arrayOfMaybeDisposable2 = new MaybeDisposable[i + 1];
      System.arraycopy(arrayOfMaybeDisposable1, 0, arrayOfMaybeDisposable2, 0, i);
      arrayOfMaybeDisposable2[i] = paramMaybeDisposable;
      if (this.observers.compareAndSet(arrayOfMaybeDisposable1, arrayOfMaybeDisposable2))
        return true; 
    } 
  }
  
  public Throwable getThrowable() {
    return (this.observers.get() == TERMINATED) ? this.error : null;
  }
  
  public T getValue() {
    return (this.observers.get() == TERMINATED) ? this.value : null;
  }
  
  public boolean hasComplete() {
    boolean bool;
    if (this.observers.get() == TERMINATED && this.value == null && this.error == null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean hasObservers() {
    boolean bool;
    if (((MaybeDisposable[])this.observers.get()).length != 0) {
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
    return ((MaybeDisposable[])this.observers.get()).length;
  }
  
  public void onComplete() {
    AtomicBoolean atomicBoolean = this.once;
    byte b = 0;
    if (atomicBoolean.compareAndSet(false, true)) {
      MaybeDisposable[] arrayOfMaybeDisposable = (MaybeDisposable[])this.observers.getAndSet(TERMINATED);
      int i = arrayOfMaybeDisposable.length;
      while (b < i) {
        (arrayOfMaybeDisposable[b]).downstream.onComplete();
        b++;
      } 
    } 
  }
  
  public void onError(Throwable paramThrowable) {
    ObjectHelper.requireNonNull(paramThrowable, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
    AtomicBoolean atomicBoolean = this.once;
    byte b = 0;
    if (atomicBoolean.compareAndSet(false, true)) {
      this.error = paramThrowable;
      MaybeDisposable[] arrayOfMaybeDisposable = (MaybeDisposable[])this.observers.getAndSet(TERMINATED);
      int i = arrayOfMaybeDisposable.length;
      while (b < i) {
        (arrayOfMaybeDisposable[b]).downstream.onError(paramThrowable);
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
      MaybeDisposable[] arrayOfMaybeDisposable = (MaybeDisposable[])this.observers.getAndSet(TERMINATED);
      int i = arrayOfMaybeDisposable.length;
      while (b < i) {
        (arrayOfMaybeDisposable[b]).downstream.onSuccess(paramT);
        b++;
      } 
    } 
  }
  
  void remove(MaybeDisposable<T> paramMaybeDisposable) {
    MaybeDisposable[] arrayOfMaybeDisposable1;
    MaybeDisposable[] arrayOfMaybeDisposable2;
    do {
      byte b2;
      arrayOfMaybeDisposable1 = (MaybeDisposable[])this.observers.get();
      int i = arrayOfMaybeDisposable1.length;
      if (i == 0)
        return; 
      byte b1 = -1;
      byte b = 0;
      while (true) {
        b2 = b1;
        if (b < i) {
          if (arrayOfMaybeDisposable1[b] == paramMaybeDisposable) {
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
        arrayOfMaybeDisposable2 = EMPTY;
      } else {
        arrayOfMaybeDisposable2 = new MaybeDisposable[i - 1];
        System.arraycopy(arrayOfMaybeDisposable1, 0, arrayOfMaybeDisposable2, 0, b2);
        System.arraycopy(arrayOfMaybeDisposable1, b2 + 1, arrayOfMaybeDisposable2, b2, i - b2 - 1);
      } 
    } while (!this.observers.compareAndSet(arrayOfMaybeDisposable1, arrayOfMaybeDisposable2));
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    MaybeDisposable<T> maybeDisposable = new MaybeDisposable<T>(paramMaybeObserver, this);
    paramMaybeObserver.onSubscribe(maybeDisposable);
    if (add(maybeDisposable)) {
      if (maybeDisposable.isDisposed())
        remove(maybeDisposable); 
    } else {
      Throwable throwable = this.error;
      if (throwable != null) {
        paramMaybeObserver.onError(throwable);
      } else {
        T t = this.value;
        if (t == null) {
          paramMaybeObserver.onComplete();
        } else {
          paramMaybeObserver.onSuccess(t);
        } 
      } 
    } 
  }
  
  static final class MaybeDisposable<T> extends AtomicReference<MaybeSubject<T>> implements Disposable {
    private static final long serialVersionUID = -7650903191002190468L;
    
    final MaybeObserver<? super T> downstream;
    
    MaybeDisposable(MaybeObserver<? super T> param1MaybeObserver, MaybeSubject<T> param1MaybeSubject) {
      this.downstream = param1MaybeObserver;
      lazySet(param1MaybeSubject);
    }
    
    public void dispose() {
      MaybeSubject<T> maybeSubject = getAndSet(null);
      if (maybeSubject != null)
        maybeSubject.remove(this); 
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/subjects/MaybeSubject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */