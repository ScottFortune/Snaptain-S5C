package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.fuseable.QueueDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableScalarXMap {
  private ObservableScalarXMap() {
    throw new IllegalStateException("No instances!");
  }
  
  public static <T, U> Observable<U> scalarXMap(T paramT, Function<? super T, ? extends ObservableSource<? extends U>> paramFunction) {
    return RxJavaPlugins.onAssembly(new ScalarXMapObservable<T, U>(paramT, paramFunction));
  }
  
  public static <T, R> boolean tryScalarXMapSubscribe(ObservableSource<T> paramObservableSource, Observer<? super R> paramObserver, Function<? super T, ? extends ObservableSource<? extends R>> paramFunction) {
    if (paramObservableSource instanceof java.util.concurrent.Callable)
      try {
      
      } finally {
        paramObservableSource = null;
        Exceptions.throwIfFatal((Throwable)paramObservableSource);
        EmptyDisposable.error((Throwable)paramObservableSource, paramObserver);
      }  
    return false;
  }
  
  public static final class ScalarDisposable<T> extends AtomicInteger implements QueueDisposable<T>, Runnable {
    static final int FUSED = 1;
    
    static final int ON_COMPLETE = 3;
    
    static final int ON_NEXT = 2;
    
    static final int START = 0;
    
    private static final long serialVersionUID = 3880992722410194083L;
    
    final Observer<? super T> observer;
    
    final T value;
    
    public ScalarDisposable(Observer<? super T> param1Observer, T param1T) {
      this.observer = param1Observer;
      this.value = param1T;
    }
    
    public void clear() {
      lazySet(3);
    }
    
    public void dispose() {
      set(3);
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (get() == 3) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean isEmpty() {
      int i = get();
      boolean bool = true;
      if (i == 1)
        bool = false; 
      return bool;
    }
    
    public boolean offer(T param1T) {
      throw new UnsupportedOperationException("Should not be called!");
    }
    
    public boolean offer(T param1T1, T param1T2) {
      throw new UnsupportedOperationException("Should not be called!");
    }
    
    public T poll() throws Exception {
      if (get() == 1) {
        lazySet(3);
        return this.value;
      } 
      return null;
    }
    
    public int requestFusion(int param1Int) {
      if ((param1Int & 0x1) != 0) {
        lazySet(1);
        return 1;
      } 
      return 0;
    }
    
    public void run() {
      if (get() == 0 && compareAndSet(0, 2)) {
        this.observer.onNext(this.value);
        if (get() == 2) {
          lazySet(3);
          this.observer.onComplete();
        } 
      } 
    }
  }
  
  static final class ScalarXMapObservable<T, R> extends Observable<R> {
    final Function<? super T, ? extends ObservableSource<? extends R>> mapper;
    
    final T value;
    
    ScalarXMapObservable(T param1T, Function<? super T, ? extends ObservableSource<? extends R>> param1Function) {
      this.value = param1T;
      this.mapper = param1Function;
    }
    
    public void subscribeActual(Observer<? super R> param1Observer) {
      try {
        return;
      } finally {
        Exception exception = null;
        EmptyDisposable.error(exception, param1Observer);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableScalarXMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */