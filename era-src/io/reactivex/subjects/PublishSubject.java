package io.reactivex.subjects;

import io.reactivex.Observer;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class PublishSubject<T> extends Subject<T> {
  static final PublishDisposable[] EMPTY;
  
  static final PublishDisposable[] TERMINATED = new PublishDisposable[0];
  
  Throwable error;
  
  final AtomicReference<PublishDisposable<T>[]> subscribers = new AtomicReference(EMPTY);
  
  static {
    EMPTY = new PublishDisposable[0];
  }
  
  @CheckReturnValue
  public static <T> PublishSubject<T> create() {
    return new PublishSubject<T>();
  }
  
  boolean add(PublishDisposable<T> paramPublishDisposable) {
    while (true) {
      PublishDisposable[] arrayOfPublishDisposable1 = (PublishDisposable[])this.subscribers.get();
      if (arrayOfPublishDisposable1 == TERMINATED)
        return false; 
      int i = arrayOfPublishDisposable1.length;
      PublishDisposable[] arrayOfPublishDisposable2 = new PublishDisposable[i + 1];
      System.arraycopy(arrayOfPublishDisposable1, 0, arrayOfPublishDisposable2, 0, i);
      arrayOfPublishDisposable2[i] = paramPublishDisposable;
      if (this.subscribers.compareAndSet(arrayOfPublishDisposable1, arrayOfPublishDisposable2))
        return true; 
    } 
  }
  
  public Throwable getThrowable() {
    return (this.subscribers.get() == TERMINATED) ? this.error : null;
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
    if (((PublishDisposable[])this.subscribers.get()).length != 0) {
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
  
  public void onComplete() {
    PublishDisposable[] arrayOfPublishDisposable1 = (PublishDisposable[])this.subscribers.get();
    PublishDisposable[] arrayOfPublishDisposable2 = TERMINATED;
    if (arrayOfPublishDisposable1 == arrayOfPublishDisposable2)
      return; 
    arrayOfPublishDisposable2 = (PublishDisposable[])this.subscribers.getAndSet(arrayOfPublishDisposable2);
    int i = arrayOfPublishDisposable2.length;
    for (byte b = 0; b < i; b++)
      arrayOfPublishDisposable2[b].onComplete(); 
  }
  
  public void onError(Throwable paramThrowable) {
    ObjectHelper.requireNonNull(paramThrowable, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
    PublishDisposable[] arrayOfPublishDisposable1 = (PublishDisposable[])this.subscribers.get();
    PublishDisposable[] arrayOfPublishDisposable2 = TERMINATED;
    if (arrayOfPublishDisposable1 == arrayOfPublishDisposable2) {
      RxJavaPlugins.onError(paramThrowable);
      return;
    } 
    this.error = paramThrowable;
    arrayOfPublishDisposable1 = (PublishDisposable[])this.subscribers.getAndSet(arrayOfPublishDisposable2);
    int i = arrayOfPublishDisposable1.length;
    for (byte b = 0; b < i; b++)
      arrayOfPublishDisposable1[b].onError(paramThrowable); 
  }
  
  public void onNext(T paramT) {
    ObjectHelper.requireNonNull(paramT, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
    PublishDisposable[] arrayOfPublishDisposable = (PublishDisposable[])this.subscribers.get();
    int i = arrayOfPublishDisposable.length;
    for (byte b = 0; b < i; b++)
      arrayOfPublishDisposable[b].onNext(paramT); 
  }
  
  public void onSubscribe(Disposable paramDisposable) {
    if (this.subscribers.get() == TERMINATED)
      paramDisposable.dispose(); 
  }
  
  void remove(PublishDisposable<T> paramPublishDisposable) {
    PublishDisposable[] arrayOfPublishDisposable1;
    PublishDisposable[] arrayOfPublishDisposable2;
    do {
      byte b2;
      arrayOfPublishDisposable1 = (PublishDisposable[])this.subscribers.get();
      if (arrayOfPublishDisposable1 == TERMINATED || arrayOfPublishDisposable1 == EMPTY)
        break; 
      int i = arrayOfPublishDisposable1.length;
      byte b1 = -1;
      byte b = 0;
      while (true) {
        b2 = b1;
        if (b < i) {
          if (arrayOfPublishDisposable1[b] == paramPublishDisposable) {
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
        arrayOfPublishDisposable2 = EMPTY;
      } else {
        arrayOfPublishDisposable2 = new PublishDisposable[i - 1];
        System.arraycopy(arrayOfPublishDisposable1, 0, arrayOfPublishDisposable2, 0, b2);
        System.arraycopy(arrayOfPublishDisposable1, b2 + 1, arrayOfPublishDisposable2, b2, i - b2 - 1);
      } 
    } while (!this.subscribers.compareAndSet(arrayOfPublishDisposable1, arrayOfPublishDisposable2));
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    PublishDisposable<T> publishDisposable = new PublishDisposable<T>(paramObserver, this);
    paramObserver.onSubscribe(publishDisposable);
    if (add(publishDisposable)) {
      if (publishDisposable.isDisposed())
        remove(publishDisposable); 
    } else {
      Throwable throwable = this.error;
      if (throwable != null) {
        paramObserver.onError(throwable);
      } else {
        paramObserver.onComplete();
      } 
    } 
  }
  
  static final class PublishDisposable<T> extends AtomicBoolean implements Disposable {
    private static final long serialVersionUID = 3562861878281475070L;
    
    final Observer<? super T> downstream;
    
    final PublishSubject<T> parent;
    
    PublishDisposable(Observer<? super T> param1Observer, PublishSubject<T> param1PublishSubject) {
      this.downstream = param1Observer;
      this.parent = param1PublishSubject;
    }
    
    public void dispose() {
      if (compareAndSet(false, true))
        this.parent.remove(this); 
    }
    
    public boolean isDisposed() {
      return get();
    }
    
    public void onComplete() {
      if (!get())
        this.downstream.onComplete(); 
    }
    
    public void onError(Throwable param1Throwable) {
      if (get()) {
        RxJavaPlugins.onError(param1Throwable);
      } else {
        this.downstream.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      if (!get())
        this.downstream.onNext(param1T); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/subjects/PublishSubject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */