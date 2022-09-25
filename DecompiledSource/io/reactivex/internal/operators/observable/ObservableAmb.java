package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableAmb<T> extends Observable<T> {
  final ObservableSource<? extends T>[] sources;
  
  final Iterable<? extends ObservableSource<? extends T>> sourcesIterable;
  
  public ObservableAmb(ObservableSource<? extends T>[] paramArrayOfObservableSource, Iterable<? extends ObservableSource<? extends T>> paramIterable) {
    this.sources = paramArrayOfObservableSource;
    this.sourcesIterable = paramIterable;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    ObservableSource[] arrayOfObservableSource;
    int i;
    ObservableSource<? extends T>[] arrayOfObservableSource1 = this.sources;
    if (arrayOfObservableSource1 == null) {
      Observable[] arrayOfObservable = new Observable[8];
      try {
        Iterator<? extends ObservableSource<? extends T>> iterator = this.sourcesIterable.iterator();
        byte b = 0;
        while (true) {
          Observable[] arrayOfObservable1 = arrayOfObservable;
          i = b;
          if (iterator.hasNext()) {
            NullPointerException nullPointerException2;
            ObservableSource observableSource = iterator.next();
            if (observableSource == null)
              return; 
            NullPointerException nullPointerException1 = nullPointerException2;
            if (b == nullPointerException2.length) {
              arrayOfObservableSource = new ObservableSource[(b >> 2) + b];
              System.arraycopy(nullPointerException2, 0, arrayOfObservableSource, 0, b);
            } 
            arrayOfObservableSource[b] = observableSource;
            b++;
            ObservableSource[] arrayOfObservableSource2 = arrayOfObservableSource;
            continue;
          } 
          break;
        } 
      } finally {
        arrayOfObservable = null;
        Exceptions.throwIfFatal((Throwable)arrayOfObservable);
        EmptyDisposable.error((Throwable)arrayOfObservable, paramObserver);
      } 
    } else {
      i = arrayOfObservableSource.length;
    } 
    if (i == 0) {
      EmptyDisposable.complete(paramObserver);
      return;
    } 
    if (i == 1) {
      arrayOfObservableSource[0].subscribe(paramObserver);
      return;
    } 
    (new AmbCoordinator(paramObserver, i)).subscribe((ObservableSource<?>[])arrayOfObservableSource);
  }
  
  static final class AmbCoordinator<T> implements Disposable {
    final Observer<? super T> downstream;
    
    final ObservableAmb.AmbInnerObserver<T>[] observers;
    
    final AtomicInteger winner = new AtomicInteger();
    
    AmbCoordinator(Observer<? super T> param1Observer, int param1Int) {
      this.downstream = param1Observer;
      this.observers = (ObservableAmb.AmbInnerObserver<T>[])new ObservableAmb.AmbInnerObserver[param1Int];
    }
    
    public void dispose() {
      if (this.winner.get() != -1) {
        this.winner.lazySet(-1);
        ObservableAmb.AmbInnerObserver<T>[] arrayOfAmbInnerObserver = this.observers;
        int i = arrayOfAmbInnerObserver.length;
        for (byte b = 0; b < i; b++)
          arrayOfAmbInnerObserver[b].dispose(); 
      } 
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (this.winner.get() == -1) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void subscribe(ObservableSource<? extends T>[] param1ArrayOfObservableSource) {
      ObservableAmb.AmbInnerObserver<T>[] arrayOfAmbInnerObserver = this.observers;
      int i = arrayOfAmbInnerObserver.length;
      boolean bool = false;
      int j;
      for (j = 0; j < i; j = k) {
        int k = j + 1;
        arrayOfAmbInnerObserver[j] = new ObservableAmb.AmbInnerObserver<T>(this, k, this.downstream);
      } 
      this.winner.lazySet(0);
      this.downstream.onSubscribe(this);
      for (j = bool; j < i; j++) {
        if (this.winner.get() != 0)
          return; 
        param1ArrayOfObservableSource[j].subscribe(arrayOfAmbInnerObserver[j]);
      } 
    }
    
    public boolean win(int param1Int) {
      int i = this.winner.get();
      boolean bool = true;
      int j = 0;
      if (i == 0) {
        if (this.winner.compareAndSet(0, param1Int)) {
          ObservableAmb.AmbInnerObserver<T>[] arrayOfAmbInnerObserver = this.observers;
          int k = arrayOfAmbInnerObserver.length;
          while (j < k) {
            i = j + 1;
            if (i != param1Int)
              arrayOfAmbInnerObserver[j].dispose(); 
            j = i;
          } 
          return true;
        } 
        return false;
      } 
      if (i != param1Int)
        bool = false; 
      return bool;
    }
  }
  
  static final class AmbInnerObserver<T> extends AtomicReference<Disposable> implements Observer<T> {
    private static final long serialVersionUID = -1185974347409665484L;
    
    final Observer<? super T> downstream;
    
    final int index;
    
    final ObservableAmb.AmbCoordinator<T> parent;
    
    boolean won;
    
    AmbInnerObserver(ObservableAmb.AmbCoordinator<T> param1AmbCoordinator, int param1Int, Observer<? super T> param1Observer) {
      this.parent = param1AmbCoordinator;
      this.index = param1Int;
      this.downstream = param1Observer;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public void onComplete() {
      if (this.won) {
        this.downstream.onComplete();
      } else if (this.parent.win(this.index)) {
        this.won = true;
        this.downstream.onComplete();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.won) {
        this.downstream.onError(param1Throwable);
      } else if (this.parent.win(this.index)) {
        this.won = true;
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      if (this.won) {
        this.downstream.onNext(param1T);
      } else if (this.parent.win(this.index)) {
        this.won = true;
        this.downstream.onNext(param1T);
      } else {
        get().dispose();
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableAmb.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */