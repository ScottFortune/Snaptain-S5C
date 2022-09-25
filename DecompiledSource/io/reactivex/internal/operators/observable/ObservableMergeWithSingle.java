package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableMergeWithSingle<T> extends AbstractObservableWithUpstream<T, T> {
  final SingleSource<? extends T> other;
  
  public ObservableMergeWithSingle(Observable<T> paramObservable, SingleSource<? extends T> paramSingleSource) {
    super((ObservableSource<T>)paramObservable);
    this.other = paramSingleSource;
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    MergeWithObserver<T> mergeWithObserver = new MergeWithObserver<T>(paramObserver);
    paramObserver.onSubscribe(mergeWithObserver);
    this.source.subscribe(mergeWithObserver);
    this.other.subscribe(mergeWithObserver.otherObserver);
  }
  
  static final class MergeWithObserver<T> extends AtomicInteger implements Observer<T>, Disposable {
    static final int OTHER_STATE_CONSUMED_OR_EMPTY = 2;
    
    static final int OTHER_STATE_HAS_VALUE = 1;
    
    private static final long serialVersionUID = -4592979584110982903L;
    
    volatile boolean disposed;
    
    final Observer<? super T> downstream;
    
    final AtomicThrowable error;
    
    final AtomicReference<Disposable> mainDisposable;
    
    volatile boolean mainDone;
    
    final OtherObserver<T> otherObserver;
    
    volatile int otherState;
    
    volatile SimplePlainQueue<T> queue;
    
    T singleItem;
    
    MergeWithObserver(Observer<? super T> param1Observer) {
      this.downstream = param1Observer;
      this.mainDisposable = new AtomicReference<Disposable>();
      this.otherObserver = new OtherObserver<T>(this);
      this.error = new AtomicThrowable();
    }
    
    public void dispose() {
      this.disposed = true;
      DisposableHelper.dispose(this.mainDisposable);
      DisposableHelper.dispose(this.otherObserver);
      if (getAndIncrement() == 0) {
        this.queue = null;
        this.singleItem = null;
      } 
    }
    
    void drain() {
      if (getAndIncrement() == 0)
        drainLoop(); 
    }
    
    void drainLoop() {
      Observer<? super T> observer = this.downstream;
      int i = 1;
      while (true) {
        if (this.disposed) {
          this.singleItem = null;
          this.queue = null;
          return;
        } 
        if (this.error.get() != null) {
          this.singleItem = null;
          this.queue = null;
          observer.onError(this.error.terminate());
          return;
        } 
        int j = this.otherState;
        int k = j;
        if (j == 1) {
          T t = this.singleItem;
          this.singleItem = null;
          this.otherState = 2;
          observer.onNext(t);
          k = 2;
        } 
        boolean bool = this.mainDone;
        SimplePlainQueue<T> simplePlainQueue = this.queue;
        if (simplePlainQueue != null) {
          Object object = simplePlainQueue.poll();
        } else {
          simplePlainQueue = null;
        } 
        if (simplePlainQueue == null) {
          j = 1;
        } else {
          j = 0;
        } 
        if (bool && j != 0 && k == 2) {
          this.queue = null;
          observer.onComplete();
          return;
        } 
        if (j != 0) {
          k = addAndGet(-i);
          i = k;
          if (k == 0)
            return; 
          continue;
        } 
        observer.onNext(simplePlainQueue);
      } 
    }
    
    SimplePlainQueue<T> getOrCreateQueue() {
      SpscLinkedArrayQueue spscLinkedArrayQueue;
      SimplePlainQueue<T> simplePlainQueue1 = this.queue;
      SimplePlainQueue<T> simplePlainQueue2 = simplePlainQueue1;
      if (simplePlainQueue1 == null) {
        spscLinkedArrayQueue = new SpscLinkedArrayQueue(Observable.bufferSize());
        this.queue = (SimplePlainQueue<T>)spscLinkedArrayQueue;
      } 
      return (SimplePlainQueue<T>)spscLinkedArrayQueue;
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(this.mainDisposable.get());
    }
    
    public void onComplete() {
      this.mainDone = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.error.addThrowable(param1Throwable)) {
        DisposableHelper.dispose(this.otherObserver);
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      if (compareAndSet(0, 1)) {
        this.downstream.onNext(param1T);
        if (decrementAndGet() == 0)
          return; 
      } else {
        getOrCreateQueue().offer(param1T);
        if (getAndIncrement() != 0)
          return; 
      } 
      drainLoop();
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this.mainDisposable, param1Disposable);
    }
    
    void otherError(Throwable param1Throwable) {
      if (this.error.addThrowable(param1Throwable)) {
        DisposableHelper.dispose(this.mainDisposable);
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    void otherSuccess(T param1T) {
      if (compareAndSet(0, 1)) {
        this.downstream.onNext(param1T);
        this.otherState = 2;
      } else {
        this.singleItem = param1T;
        this.otherState = 1;
        if (getAndIncrement() != 0)
          return; 
      } 
      drainLoop();
    }
    
    static final class OtherObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T> {
      private static final long serialVersionUID = -2935427570954647017L;
      
      final ObservableMergeWithSingle.MergeWithObserver<T> parent;
      
      OtherObserver(ObservableMergeWithSingle.MergeWithObserver<T> param2MergeWithObserver) {
        this.parent = param2MergeWithObserver;
      }
      
      public void onError(Throwable param2Throwable) {
        this.parent.otherError(param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.setOnce(this, param2Disposable);
      }
      
      public void onSuccess(T param2T) {
        this.parent.otherSuccess(param2T);
      }
    }
  }
  
  static final class OtherObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T> {
    private static final long serialVersionUID = -2935427570954647017L;
    
    final ObservableMergeWithSingle.MergeWithObserver<T> parent;
    
    OtherObserver(ObservableMergeWithSingle.MergeWithObserver<T> param1MergeWithObserver) {
      this.parent = param1MergeWithObserver;
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.otherError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      this.parent.otherSuccess(param1T);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableMergeWithSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */