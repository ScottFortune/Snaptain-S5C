package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subjects.UnicastSubject;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableWindowBoundarySupplier<T, B> extends AbstractObservableWithUpstream<T, Observable<T>> {
  final int capacityHint;
  
  final Callable<? extends ObservableSource<B>> other;
  
  public ObservableWindowBoundarySupplier(ObservableSource<T> paramObservableSource, Callable<? extends ObservableSource<B>> paramCallable, int paramInt) {
    super(paramObservableSource);
    this.other = paramCallable;
    this.capacityHint = paramInt;
  }
  
  public void subscribeActual(Observer<? super Observable<T>> paramObserver) {
    paramObserver = new WindowBoundaryMainObserver<Observable<T>, B>((Observer)paramObserver, this.capacityHint, this.other);
    this.source.subscribe(paramObserver);
  }
  
  static final class WindowBoundaryInnerObserver<T, B> extends DisposableObserver<B> {
    boolean done;
    
    final ObservableWindowBoundarySupplier.WindowBoundaryMainObserver<T, B> parent;
    
    WindowBoundaryInnerObserver(ObservableWindowBoundarySupplier.WindowBoundaryMainObserver<T, B> param1WindowBoundaryMainObserver) {
      this.parent = param1WindowBoundaryMainObserver;
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      this.parent.innerComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.parent.innerError(param1Throwable);
    }
    
    public void onNext(B param1B) {
      if (this.done)
        return; 
      this.done = true;
      dispose();
      this.parent.innerNext(this);
    }
  }
  
  static final class WindowBoundaryMainObserver<T, B> extends AtomicInteger implements Observer<T>, Disposable, Runnable {
    static final ObservableWindowBoundarySupplier.WindowBoundaryInnerObserver<Object, Object> BOUNDARY_DISPOSED = new ObservableWindowBoundarySupplier.WindowBoundaryInnerObserver<Object, Object>(null);
    
    static final Object NEXT_WINDOW = new Object();
    
    private static final long serialVersionUID = 2233020065421370272L;
    
    final AtomicReference<ObservableWindowBoundarySupplier.WindowBoundaryInnerObserver<T, B>> boundaryObserver;
    
    final int capacityHint;
    
    volatile boolean done;
    
    final Observer<? super Observable<T>> downstream;
    
    final AtomicThrowable errors;
    
    final Callable<? extends ObservableSource<B>> other;
    
    final MpscLinkedQueue<Object> queue;
    
    final AtomicBoolean stopWindows;
    
    Disposable upstream;
    
    UnicastSubject<T> window;
    
    final AtomicInteger windows;
    
    WindowBoundaryMainObserver(Observer<? super Observable<T>> param1Observer, int param1Int, Callable<? extends ObservableSource<B>> param1Callable) {
      this.downstream = param1Observer;
      this.capacityHint = param1Int;
      this.boundaryObserver = new AtomicReference<ObservableWindowBoundarySupplier.WindowBoundaryInnerObserver<T, B>>();
      this.windows = new AtomicInteger(1);
      this.queue = new MpscLinkedQueue();
      this.errors = new AtomicThrowable();
      this.stopWindows = new AtomicBoolean();
      this.other = param1Callable;
    }
    
    public void dispose() {
      if (this.stopWindows.compareAndSet(false, true)) {
        disposeBoundary();
        if (this.windows.decrementAndGet() == 0)
          this.upstream.dispose(); 
      } 
    }
    
    void disposeBoundary() {
      Disposable disposable = (Disposable)this.boundaryObserver.getAndSet(BOUNDARY_DISPOSED);
      if (disposable != null && disposable != BOUNDARY_DISPOSED)
        disposable.dispose(); 
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      Observer<? super Observable<T>> observer = this.downstream;
      MpscLinkedQueue<Object> mpscLinkedQueue = this.queue;
      AtomicThrowable atomicThrowable = this.errors;
      int i = 1;
      while (true) {
        Throwable throwable;
        int j;
        if (this.windows.get() == 0) {
          mpscLinkedQueue.clear();
          this.window = null;
          return;
        } 
        UnicastSubject<T> unicastSubject = this.window;
        boolean bool = this.done;
        if (bool && atomicThrowable.get() != null) {
          mpscLinkedQueue.clear();
          throwable = atomicThrowable.terminate();
          if (unicastSubject != null) {
            this.window = null;
            unicastSubject.onError(throwable);
          } 
          observer.onError(throwable);
          return;
        } 
        Object<Object, Object> object = (Object<Object, Object>)throwable.poll();
        if (object == null) {
          j = 1;
        } else {
          j = 0;
        } 
        if (bool && j) {
          throwable = atomicThrowable.terminate();
          if (throwable == null) {
            if (unicastSubject != null) {
              this.window = null;
              unicastSubject.onComplete();
            } 
            observer.onComplete();
          } else {
            if (unicastSubject != null) {
              this.window = null;
              unicastSubject.onError(throwable);
            } 
            observer.onError(throwable);
          } 
          return;
        } 
        if (j) {
          j = addAndGet(-i);
          i = j;
          if (j == 0)
            return; 
          continue;
        } 
        if (object != NEXT_WINDOW) {
          unicastSubject.onNext(object);
          continue;
        } 
        if (unicastSubject != null) {
          this.window = null;
          unicastSubject.onComplete();
        } 
        if (!this.stopWindows.get()) {
          UnicastSubject<T> unicastSubject1 = UnicastSubject.create(this.capacityHint, this);
          this.window = unicastSubject1;
          this.windows.getAndIncrement();
          try {
          
          } finally {
            unicastSubject = null;
            Exceptions.throwIfFatal((Throwable)unicastSubject);
            atomicThrowable.addThrowable((Throwable)unicastSubject);
          } 
        } 
      } 
    }
    
    void innerComplete() {
      this.upstream.dispose();
      this.done = true;
      drain();
    }
    
    void innerError(Throwable param1Throwable) {
      this.upstream.dispose();
      if (this.errors.addThrowable(param1Throwable)) {
        this.done = true;
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    void innerNext(ObservableWindowBoundarySupplier.WindowBoundaryInnerObserver<T, B> param1WindowBoundaryInnerObserver) {
      this.boundaryObserver.compareAndSet(param1WindowBoundaryInnerObserver, null);
      this.queue.offer(NEXT_WINDOW);
      drain();
    }
    
    public boolean isDisposed() {
      return this.stopWindows.get();
    }
    
    public void onComplete() {
      disposeBoundary();
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      disposeBoundary();
      if (this.errors.addThrowable(param1Throwable)) {
        this.done = true;
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      this.queue.offer(param1T);
      drain();
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
        this.queue.offer(NEXT_WINDOW);
        drain();
      } 
    }
    
    public void run() {
      if (this.windows.decrementAndGet() == 0)
        this.upstream.dispose(); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableWindowBoundarySupplier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */