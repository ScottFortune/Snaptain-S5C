package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableZip<T, R> extends Observable<R> {
  final int bufferSize;
  
  final boolean delayError;
  
  final ObservableSource<? extends T>[] sources;
  
  final Iterable<? extends ObservableSource<? extends T>> sourcesIterable;
  
  final Function<? super Object[], ? extends R> zipper;
  
  public ObservableZip(ObservableSource<? extends T>[] paramArrayOfObservableSource, Iterable<? extends ObservableSource<? extends T>> paramIterable, Function<? super Object[], ? extends R> paramFunction, int paramInt, boolean paramBoolean) {
    this.sources = paramArrayOfObservableSource;
    this.sourcesIterable = paramIterable;
    this.zipper = paramFunction;
    this.bufferSize = paramInt;
    this.delayError = paramBoolean;
  }
  
  public void subscribeActual(Observer<? super R> paramObserver) {
    ObservableSource[] arrayOfObservableSource;
    int i;
    ObservableSource<? extends T>[] arrayOfObservableSource1 = this.sources;
    if (arrayOfObservableSource1 == null) {
      Observable[] arrayOfObservable = new Observable[8];
      Iterator<? extends ObservableSource<? extends T>> iterator = this.sourcesIterable.iterator();
      byte b = 0;
      while (true) {
        Observable[] arrayOfObservable1 = arrayOfObservable;
        i = b;
        if (iterator.hasNext()) {
          ObservableSource observableSource = iterator.next();
          arrayOfObservable1 = arrayOfObservable;
          if (b == arrayOfObservable.length) {
            arrayOfObservableSource = new ObservableSource[(b >> 2) + b];
            System.arraycopy(arrayOfObservable, 0, arrayOfObservableSource, 0, b);
          } 
          arrayOfObservableSource[b] = observableSource;
          b++;
          ObservableSource[] arrayOfObservableSource2 = arrayOfObservableSource;
          continue;
        } 
        break;
      } 
    } else {
      i = arrayOfObservableSource.length;
    } 
    if (i == 0) {
      EmptyDisposable.complete(paramObserver);
      return;
    } 
    (new ZipCoordinator<Object, Object>(paramObserver, this.zipper, i, this.delayError)).subscribe((ObservableSource<?>[])arrayOfObservableSource, this.bufferSize);
  }
  
  static final class ZipCoordinator<T, R> extends AtomicInteger implements Disposable {
    private static final long serialVersionUID = 2983708048395377667L;
    
    volatile boolean cancelled;
    
    final boolean delayError;
    
    final Observer<? super R> downstream;
    
    final ObservableZip.ZipObserver<T, R>[] observers;
    
    final T[] row;
    
    final Function<? super Object[], ? extends R> zipper;
    
    ZipCoordinator(Observer<? super R> param1Observer, Function<? super Object[], ? extends R> param1Function, int param1Int, boolean param1Boolean) {
      this.downstream = param1Observer;
      this.zipper = param1Function;
      this.observers = (ObservableZip.ZipObserver<T, R>[])new ObservableZip.ZipObserver[param1Int];
      this.row = (T[])new Object[param1Int];
      this.delayError = param1Boolean;
    }
    
    void cancel() {
      clear();
      cancelSources();
    }
    
    void cancelSources() {
      ObservableZip.ZipObserver<T, R>[] arrayOfZipObserver = this.observers;
      int i = arrayOfZipObserver.length;
      for (byte b = 0; b < i; b++)
        arrayOfZipObserver[b].dispose(); 
    }
    
    boolean checkTerminated(boolean param1Boolean1, boolean param1Boolean2, Observer<? super R> param1Observer, boolean param1Boolean3, ObservableZip.ZipObserver<?, ?> param1ZipObserver) {
      if (this.cancelled) {
        cancel();
        return true;
      } 
      if (param1Boolean1) {
        Throwable throwable;
        if (param1Boolean3) {
          if (param1Boolean2) {
            throwable = param1ZipObserver.error;
            this.cancelled = true;
            cancel();
            if (throwable != null) {
              param1Observer.onError(throwable);
            } else {
              param1Observer.onComplete();
            } 
            return true;
          } 
        } else {
          throwable = ((ObservableZip.ZipObserver)throwable).error;
          if (throwable != null) {
            this.cancelled = true;
            cancel();
            param1Observer.onError(throwable);
            return true;
          } 
          if (param1Boolean2) {
            this.cancelled = true;
            cancel();
            param1Observer.onComplete();
            return true;
          } 
        } 
      } 
      return false;
    }
    
    void clear() {
      ObservableZip.ZipObserver<T, R>[] arrayOfZipObserver = this.observers;
      int i = arrayOfZipObserver.length;
      for (byte b = 0; b < i; b++)
        (arrayOfZipObserver[b]).queue.clear(); 
    }
    
    public void dispose() {
      if (!this.cancelled) {
        this.cancelled = true;
        cancelSources();
        if (getAndIncrement() == 0)
          clear(); 
      } 
    }
    
    public void drain() {
      if (getAndIncrement() != 0)
        return; 
      ObservableZip.ZipObserver<T, R>[] arrayOfZipObserver = this.observers;
      Observer<? super R> observer = this.downstream;
      T[] arrayOfT = this.row;
      boolean bool = this.delayError;
      int i = 1;
      while (true) {
        int j = arrayOfZipObserver.length;
        byte b1 = 0;
        byte b2 = 0;
        int k = 0;
        while (b1 < j) {
          byte b;
          ObservableZip.ZipObserver<T, R> zipObserver = arrayOfZipObserver[b1];
          if (arrayOfT[k] == null) {
            boolean bool2;
            boolean bool1 = zipObserver.done;
            Object object = zipObserver.queue.poll();
            if (object == null) {
              bool2 = true;
            } else {
              bool2 = false;
            } 
            if (checkTerminated(bool1, bool2, observer, bool, zipObserver))
              return; 
            if (!bool2) {
              arrayOfT[k] = (T)object;
              b = b2;
            } else {
              b = b2 + 1;
            } 
          } else {
            b = b2;
            if (zipObserver.done) {
              b = b2;
              if (!bool) {
                Throwable throwable = zipObserver.error;
                b = b2;
                if (throwable != null) {
                  this.cancelled = true;
                  cancel();
                  observer.onError(throwable);
                  return;
                } 
              } 
            } 
          } 
          k++;
          b1++;
          b2 = b;
        } 
        if (b2 != 0) {
          k = addAndGet(-i);
          i = k;
          if (k == 0)
            return; 
          continue;
        } 
        try {
        
        } finally {
          arrayOfT = null;
          Exceptions.throwIfFatal((Throwable)arrayOfT);
          cancel();
          observer.onError((Throwable)arrayOfT);
        } 
      } 
    }
    
    public boolean isDisposed() {
      return this.cancelled;
    }
    
    public void subscribe(ObservableSource<? extends T>[] param1ArrayOfObservableSource, int param1Int) {
      ObservableZip.ZipObserver<T, R>[] arrayOfZipObserver = this.observers;
      int i = arrayOfZipObserver.length;
      boolean bool = false;
      for (byte b = 0; b < i; b++)
        arrayOfZipObserver[b] = new ObservableZip.ZipObserver<T, R>(this, param1Int); 
      lazySet(0);
      this.downstream.onSubscribe(this);
      for (param1Int = bool; param1Int < i; param1Int++) {
        if (this.cancelled)
          return; 
        param1ArrayOfObservableSource[param1Int].subscribe(arrayOfZipObserver[param1Int]);
      } 
    }
  }
  
  static final class ZipObserver<T, R> implements Observer<T> {
    volatile boolean done;
    
    Throwable error;
    
    final ObservableZip.ZipCoordinator<T, R> parent;
    
    final SpscLinkedArrayQueue<T> queue;
    
    final AtomicReference<Disposable> upstream = new AtomicReference<Disposable>();
    
    ZipObserver(ObservableZip.ZipCoordinator<T, R> param1ZipCoordinator, int param1Int) {
      this.parent = param1ZipCoordinator;
      this.queue = new SpscLinkedArrayQueue(param1Int);
    }
    
    public void dispose() {
      DisposableHelper.dispose(this.upstream);
    }
    
    public void onComplete() {
      this.done = true;
      this.parent.drain();
    }
    
    public void onError(Throwable param1Throwable) {
      this.error = param1Throwable;
      this.done = true;
      this.parent.drain();
    }
    
    public void onNext(T param1T) {
      this.queue.offer(param1T);
      this.parent.drain();
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this.upstream, param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableZip.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */