package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import io.reactivex.internal.disposables.CancellableDisposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableCreate<T> extends Observable<T> {
  final ObservableOnSubscribe<T> source;
  
  public ObservableCreate(ObservableOnSubscribe<T> paramObservableOnSubscribe) {
    this.source = paramObservableOnSubscribe;
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    CreateEmitter<T> createEmitter = new CreateEmitter<T>(paramObserver);
    paramObserver.onSubscribe(createEmitter);
  }
  
  static final class CreateEmitter<T> extends AtomicReference<Disposable> implements ObservableEmitter<T>, Disposable {
    private static final long serialVersionUID = -3434801548987643227L;
    
    final Observer<? super T> observer;
    
    CreateEmitter(Observer<? super T> param1Observer) {
      this.observer = param1Observer;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      if (!isDisposed())
        try {
          this.observer.onComplete();
        } finally {
          dispose();
        }  
    }
    
    public void onError(Throwable param1Throwable) {
      if (!tryOnError(param1Throwable))
        RxJavaPlugins.onError(param1Throwable); 
    }
    
    public void onNext(T param1T) {
      if (param1T == null) {
        onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
        return;
      } 
      if (!isDisposed())
        this.observer.onNext(param1T); 
    }
    
    public ObservableEmitter<T> serialize() {
      return new ObservableCreate.SerializedEmitter<T>(this);
    }
    
    public void setCancellable(Cancellable param1Cancellable) {
      setDisposable((Disposable)new CancellableDisposable(param1Cancellable));
    }
    
    public void setDisposable(Disposable param1Disposable) {
      DisposableHelper.set(this, param1Disposable);
    }
    
    public String toString() {
      return String.format("%s{%s}", new Object[] { getClass().getSimpleName(), super.toString() });
    }
    
    public boolean tryOnError(Throwable param1Throwable) {
      Throwable throwable = param1Throwable;
      if (param1Throwable == null)
        throwable = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources."); 
      if (!isDisposed())
        try {
          this.observer.onError(throwable);
          return true;
        } finally {
          dispose();
        }  
      return false;
    }
  }
  
  static final class SerializedEmitter<T> extends AtomicInteger implements ObservableEmitter<T> {
    private static final long serialVersionUID = 4883307006032401862L;
    
    volatile boolean done;
    
    final ObservableEmitter<T> emitter;
    
    final AtomicThrowable error;
    
    final SpscLinkedArrayQueue<T> queue;
    
    SerializedEmitter(ObservableEmitter<T> param1ObservableEmitter) {
      this.emitter = param1ObservableEmitter;
      this.error = new AtomicThrowable();
      this.queue = new SpscLinkedArrayQueue(16);
    }
    
    void drain() {
      if (getAndIncrement() == 0)
        drainLoop(); 
    }
    
    void drainLoop() {
      ObservableEmitter<T> observableEmitter = this.emitter;
      SpscLinkedArrayQueue<T> spscLinkedArrayQueue = this.queue;
      AtomicThrowable atomicThrowable = this.error;
      int i = 1;
      while (true) {
        int j;
        if (observableEmitter.isDisposed()) {
          spscLinkedArrayQueue.clear();
          return;
        } 
        if (atomicThrowable.get() != null) {
          spscLinkedArrayQueue.clear();
          observableEmitter.onError(atomicThrowable.terminate());
          return;
        } 
        boolean bool = this.done;
        Object object = spscLinkedArrayQueue.poll();
        if (object == null) {
          j = 1;
        } else {
          j = 0;
        } 
        if (bool && j) {
          observableEmitter.onComplete();
          return;
        } 
        if (j) {
          j = addAndGet(-i);
          i = j;
          if (j == 0)
            return; 
          continue;
        } 
        observableEmitter.onNext(object);
      } 
    }
    
    public boolean isDisposed() {
      return this.emitter.isDisposed();
    }
    
    public void onComplete() {
      if (!this.emitter.isDisposed() && !this.done) {
        this.done = true;
        drain();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (!tryOnError(param1Throwable))
        RxJavaPlugins.onError(param1Throwable); 
    }
    
    public void onNext(T param1T) {
      if (this.emitter.isDisposed() || this.done)
        return; 
      if (param1T == null) {
        onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
        return;
      } 
      if (get() == 0 && compareAndSet(0, 1)) {
        this.emitter.onNext(param1T);
        if (decrementAndGet() == 0)
          return; 
      } else {
        synchronized (this.queue) {
          null.offer(param1T);
          if (getAndIncrement() != 0)
            return; 
          drainLoop();
          return;
        } 
      } 
      drainLoop();
    }
    
    public ObservableEmitter<T> serialize() {
      return this;
    }
    
    public void setCancellable(Cancellable param1Cancellable) {
      this.emitter.setCancellable(param1Cancellable);
    }
    
    public void setDisposable(Disposable param1Disposable) {
      this.emitter.setDisposable(param1Disposable);
    }
    
    public String toString() {
      return this.emitter.toString();
    }
    
    public boolean tryOnError(Throwable param1Throwable) {
      if (!this.emitter.isDisposed() && !this.done) {
        Throwable throwable = param1Throwable;
        if (param1Throwable == null)
          throwable = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources."); 
        if (this.error.addThrowable(throwable)) {
          this.done = true;
          drain();
          return true;
        } 
      } 
      return false;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableCreate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */