package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiPredicate;
import io.reactivex.internal.disposables.ArrayCompositeDisposable;
import io.reactivex.internal.fuseable.FuseToObservable;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableSequenceEqualSingle<T> extends Single<Boolean> implements FuseToObservable<Boolean> {
  final int bufferSize;
  
  final BiPredicate<? super T, ? super T> comparer;
  
  final ObservableSource<? extends T> first;
  
  final ObservableSource<? extends T> second;
  
  public ObservableSequenceEqualSingle(ObservableSource<? extends T> paramObservableSource1, ObservableSource<? extends T> paramObservableSource2, BiPredicate<? super T, ? super T> paramBiPredicate, int paramInt) {
    this.first = paramObservableSource1;
    this.second = paramObservableSource2;
    this.comparer = paramBiPredicate;
    this.bufferSize = paramInt;
  }
  
  public Observable<Boolean> fuseToObservable() {
    return RxJavaPlugins.onAssembly(new ObservableSequenceEqual<T>(this.first, this.second, this.comparer, this.bufferSize));
  }
  
  public void subscribeActual(SingleObserver<? super Boolean> paramSingleObserver) {
    EqualCoordinator<T> equalCoordinator = new EqualCoordinator<T>(paramSingleObserver, this.bufferSize, this.first, this.second, this.comparer);
    paramSingleObserver.onSubscribe(equalCoordinator);
    equalCoordinator.subscribe();
  }
  
  static final class EqualCoordinator<T> extends AtomicInteger implements Disposable {
    private static final long serialVersionUID = -6178010334400373240L;
    
    volatile boolean cancelled;
    
    final BiPredicate<? super T, ? super T> comparer;
    
    final SingleObserver<? super Boolean> downstream;
    
    final ObservableSource<? extends T> first;
    
    final ObservableSequenceEqualSingle.EqualObserver<T>[] observers;
    
    final ArrayCompositeDisposable resources;
    
    final ObservableSource<? extends T> second;
    
    T v1;
    
    T v2;
    
    EqualCoordinator(SingleObserver<? super Boolean> param1SingleObserver, int param1Int, ObservableSource<? extends T> param1ObservableSource1, ObservableSource<? extends T> param1ObservableSource2, BiPredicate<? super T, ? super T> param1BiPredicate) {
      this.downstream = param1SingleObserver;
      this.first = param1ObservableSource1;
      this.second = param1ObservableSource2;
      this.comparer = param1BiPredicate;
      ObservableSequenceEqualSingle.EqualObserver[] arrayOfEqualObserver = new ObservableSequenceEqualSingle.EqualObserver[2];
      this.observers = (ObservableSequenceEqualSingle.EqualObserver<T>[])arrayOfEqualObserver;
      arrayOfEqualObserver[0] = new ObservableSequenceEqualSingle.EqualObserver(this, 0, param1Int);
      arrayOfEqualObserver[1] = new ObservableSequenceEqualSingle.EqualObserver(this, 1, param1Int);
      this.resources = new ArrayCompositeDisposable(2);
    }
    
    void cancel(SpscLinkedArrayQueue<T> param1SpscLinkedArrayQueue1, SpscLinkedArrayQueue<T> param1SpscLinkedArrayQueue2) {
      this.cancelled = true;
      param1SpscLinkedArrayQueue1.clear();
      param1SpscLinkedArrayQueue2.clear();
    }
    
    public void dispose() {
      if (!this.cancelled) {
        this.cancelled = true;
        this.resources.dispose();
        if (getAndIncrement() == 0) {
          ObservableSequenceEqualSingle.EqualObserver<T>[] arrayOfEqualObserver = this.observers;
          (arrayOfEqualObserver[0]).queue.clear();
          (arrayOfEqualObserver[1]).queue.clear();
        } 
      } 
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      ObservableSequenceEqualSingle.EqualObserver<T>[] arrayOfEqualObserver = this.observers;
      ObservableSequenceEqualSingle.EqualObserver<T> equalObserver1 = arrayOfEqualObserver[0];
      SpscLinkedArrayQueue<T> spscLinkedArrayQueue2 = equalObserver1.queue;
      ObservableSequenceEqualSingle.EqualObserver<T> equalObserver2 = arrayOfEqualObserver[1];
      SpscLinkedArrayQueue<T> spscLinkedArrayQueue1 = equalObserver2.queue;
      int i = 1;
      while (true) {
        int j;
        byte b;
        if (this.cancelled) {
          spscLinkedArrayQueue2.clear();
          spscLinkedArrayQueue1.clear();
          return;
        } 
        boolean bool1 = equalObserver1.done;
        if (bool1) {
          Throwable throwable = equalObserver1.error;
          if (throwable != null) {
            cancel(spscLinkedArrayQueue2, spscLinkedArrayQueue1);
            this.downstream.onError(throwable);
            return;
          } 
        } 
        boolean bool2 = equalObserver2.done;
        if (bool2) {
          Throwable throwable = equalObserver2.error;
          if (throwable != null) {
            cancel(spscLinkedArrayQueue2, spscLinkedArrayQueue1);
            this.downstream.onError(throwable);
            return;
          } 
        } 
        if (this.v1 == null)
          this.v1 = (T)spscLinkedArrayQueue2.poll(); 
        if (this.v1 == null) {
          j = 1;
        } else {
          j = 0;
        } 
        if (this.v2 == null)
          this.v2 = (T)spscLinkedArrayQueue1.poll(); 
        if (this.v2 == null) {
          b = 1;
        } else {
          b = 0;
        } 
        if (bool1 && bool2 && j && b) {
          this.downstream.onSuccess(Boolean.valueOf(true));
          return;
        } 
        if (bool1 && bool2 && j != b) {
          cancel(spscLinkedArrayQueue2, spscLinkedArrayQueue1);
          this.downstream.onSuccess(Boolean.valueOf(false));
          return;
        } 
        if (!j && !b)
          try {
          
          } finally {
            equalObserver1 = null;
            Exceptions.throwIfFatal((Throwable)equalObserver1);
            cancel(spscLinkedArrayQueue2, spscLinkedArrayQueue1);
            this.downstream.onError((Throwable)equalObserver1);
          }  
        if (j || b) {
          j = addAndGet(-i);
          i = j;
          if (j == 0)
            break; 
        } 
      } 
    }
    
    public boolean isDisposed() {
      return this.cancelled;
    }
    
    boolean setDisposable(Disposable param1Disposable, int param1Int) {
      return this.resources.setResource(param1Int, param1Disposable);
    }
    
    void subscribe() {
      ObservableSequenceEqualSingle.EqualObserver<T>[] arrayOfEqualObserver = this.observers;
      this.first.subscribe(arrayOfEqualObserver[0]);
      this.second.subscribe(arrayOfEqualObserver[1]);
    }
  }
  
  static final class EqualObserver<T> implements Observer<T> {
    volatile boolean done;
    
    Throwable error;
    
    final int index;
    
    final ObservableSequenceEqualSingle.EqualCoordinator<T> parent;
    
    final SpscLinkedArrayQueue<T> queue;
    
    EqualObserver(ObservableSequenceEqualSingle.EqualCoordinator<T> param1EqualCoordinator, int param1Int1, int param1Int2) {
      this.parent = param1EqualCoordinator;
      this.index = param1Int1;
      this.queue = new SpscLinkedArrayQueue(param1Int2);
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
      this.parent.setDisposable(param1Disposable, this.index);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableSequenceEqualSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */