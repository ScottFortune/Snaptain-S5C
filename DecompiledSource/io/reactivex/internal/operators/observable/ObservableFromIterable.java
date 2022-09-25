package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.observers.BasicQueueDisposable;
import java.util.Iterator;

public final class ObservableFromIterable<T> extends Observable<T> {
  final Iterable<? extends T> source;
  
  public ObservableFromIterable(Iterable<? extends T> paramIterable) {
    this.source = paramIterable;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    try {
    
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptyDisposable.error(exception, paramObserver);
    } 
  }
  
  static final class FromIterableDisposable<T> extends BasicQueueDisposable<T> {
    boolean checkNext;
    
    volatile boolean disposed;
    
    boolean done;
    
    final Observer<? super T> downstream;
    
    boolean fusionMode;
    
    final Iterator<? extends T> it;
    
    FromIterableDisposable(Observer<? super T> param1Observer, Iterator<? extends T> param1Iterator) {
      this.downstream = param1Observer;
      this.it = param1Iterator;
    }
    
    public void clear() {
      this.done = true;
    }
    
    public void dispose() {
      this.disposed = true;
    }
    
    public boolean isDisposed() {
      return this.disposed;
    }
    
    public boolean isEmpty() {
      return this.done;
    }
    
    public T poll() {
      if (this.done)
        return null; 
      if (this.checkNext) {
        if (!this.it.hasNext()) {
          this.done = true;
          return null;
        } 
      } else {
        this.checkNext = true;
      } 
      return (T)ObjectHelper.requireNonNull(this.it.next(), "The iterator returned a null value");
    }
    
    public int requestFusion(int param1Int) {
      if ((param1Int & 0x1) != 0) {
        this.fusionMode = true;
        return 1;
      } 
      return 0;
    }
    
    void run() {
      while (true) {
        if (isDisposed())
          return; 
        try {
          Object object = ObjectHelper.requireNonNull(this.it.next(), "The iterator returned a null value");
        } finally {
          Exception exception = null;
          Exceptions.throwIfFatal(exception);
          this.downstream.onError(exception);
        } 
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableFromIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */