package io.reactivex.subjects;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class CompletableSubject extends Completable implements CompletableObserver {
  static final CompletableDisposable[] EMPTY = new CompletableDisposable[0];
  
  static final CompletableDisposable[] TERMINATED = new CompletableDisposable[0];
  
  Throwable error;
  
  final AtomicReference<CompletableDisposable[]> observers = (AtomicReference)new AtomicReference<CompletableDisposable>(EMPTY);
  
  final AtomicBoolean once = new AtomicBoolean();
  
  @CheckReturnValue
  public static CompletableSubject create() {
    return new CompletableSubject();
  }
  
  boolean add(CompletableDisposable paramCompletableDisposable) {
    while (true) {
      CompletableDisposable[] arrayOfCompletableDisposable1 = this.observers.get();
      if (arrayOfCompletableDisposable1 == TERMINATED)
        return false; 
      int i = arrayOfCompletableDisposable1.length;
      CompletableDisposable[] arrayOfCompletableDisposable2 = new CompletableDisposable[i + 1];
      System.arraycopy(arrayOfCompletableDisposable1, 0, arrayOfCompletableDisposable2, 0, i);
      arrayOfCompletableDisposable2[i] = paramCompletableDisposable;
      if (this.observers.compareAndSet(arrayOfCompletableDisposable1, arrayOfCompletableDisposable2))
        return true; 
    } 
  }
  
  public Throwable getThrowable() {
    return (this.observers.get() == TERMINATED) ? this.error : null;
  }
  
  public boolean hasComplete() {
    boolean bool;
    if (this.observers.get() == TERMINATED && this.error == null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean hasObservers() {
    boolean bool;
    if (((CompletableDisposable[])this.observers.get()).length != 0) {
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
  
  int observerCount() {
    return ((CompletableDisposable[])this.observers.get()).length;
  }
  
  public void onComplete() {
    AtomicBoolean atomicBoolean = this.once;
    byte b = 0;
    if (atomicBoolean.compareAndSet(false, true)) {
      CompletableDisposable[] arrayOfCompletableDisposable = this.observers.getAndSet(TERMINATED);
      int i = arrayOfCompletableDisposable.length;
      while (b < i) {
        (arrayOfCompletableDisposable[b]).downstream.onComplete();
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
      CompletableDisposable[] arrayOfCompletableDisposable = this.observers.getAndSet(TERMINATED);
      int i = arrayOfCompletableDisposable.length;
      while (b < i) {
        (arrayOfCompletableDisposable[b]).downstream.onError(paramThrowable);
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
  
  void remove(CompletableDisposable paramCompletableDisposable) {
    CompletableDisposable[] arrayOfCompletableDisposable1;
    CompletableDisposable[] arrayOfCompletableDisposable2;
    do {
      byte b2;
      arrayOfCompletableDisposable1 = this.observers.get();
      int i = arrayOfCompletableDisposable1.length;
      if (i == 0)
        return; 
      byte b1 = -1;
      byte b = 0;
      while (true) {
        b2 = b1;
        if (b < i) {
          if (arrayOfCompletableDisposable1[b] == paramCompletableDisposable) {
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
        arrayOfCompletableDisposable2 = EMPTY;
      } else {
        arrayOfCompletableDisposable2 = new CompletableDisposable[i - 1];
        System.arraycopy(arrayOfCompletableDisposable1, 0, arrayOfCompletableDisposable2, 0, b2);
        System.arraycopy(arrayOfCompletableDisposable1, b2 + 1, arrayOfCompletableDisposable2, b2, i - b2 - 1);
      } 
    } while (!this.observers.compareAndSet(arrayOfCompletableDisposable1, arrayOfCompletableDisposable2));
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    CompletableDisposable completableDisposable = new CompletableDisposable(paramCompletableObserver, this);
    paramCompletableObserver.onSubscribe(completableDisposable);
    if (add(completableDisposable)) {
      if (completableDisposable.isDisposed())
        remove(completableDisposable); 
    } else {
      Throwable throwable = this.error;
      if (throwable != null) {
        paramCompletableObserver.onError(throwable);
      } else {
        paramCompletableObserver.onComplete();
      } 
    } 
  }
  
  static final class CompletableDisposable extends AtomicReference<CompletableSubject> implements Disposable {
    private static final long serialVersionUID = -7650903191002190468L;
    
    final CompletableObserver downstream;
    
    CompletableDisposable(CompletableObserver param1CompletableObserver, CompletableSubject param1CompletableSubject) {
      this.downstream = param1CompletableObserver;
      lazySet(param1CompletableSubject);
    }
    
    public void dispose() {
      CompletableSubject completableSubject = getAndSet(null);
      if (completableSubject != null)
        completableSubject.remove(this); 
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/subjects/CompletableSubject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */