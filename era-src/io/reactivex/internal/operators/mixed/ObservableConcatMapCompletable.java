package io.reactivex.internal.operators.mixed;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.QueueDisposable;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.ErrorMode;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableConcatMapCompletable<T> extends Completable {
  final ErrorMode errorMode;
  
  final Function<? super T, ? extends CompletableSource> mapper;
  
  final int prefetch;
  
  final Observable<T> source;
  
  public ObservableConcatMapCompletable(Observable<T> paramObservable, Function<? super T, ? extends CompletableSource> paramFunction, ErrorMode paramErrorMode, int paramInt) {
    this.source = paramObservable;
    this.mapper = paramFunction;
    this.errorMode = paramErrorMode;
    this.prefetch = paramInt;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    if (!ScalarXMapZHelper.tryAsCompletable(this.source, this.mapper, paramCompletableObserver))
      this.source.subscribe(new ConcatMapCompletableObserver<T>(paramCompletableObserver, this.mapper, this.errorMode, this.prefetch)); 
  }
  
  static final class ConcatMapCompletableObserver<T> extends AtomicInteger implements Observer<T>, Disposable {
    private static final long serialVersionUID = 3610901111000061034L;
    
    volatile boolean active;
    
    volatile boolean disposed;
    
    volatile boolean done;
    
    final CompletableObserver downstream;
    
    final ErrorMode errorMode;
    
    final AtomicThrowable errors;
    
    final ConcatMapInnerObserver inner;
    
    final Function<? super T, ? extends CompletableSource> mapper;
    
    final int prefetch;
    
    SimpleQueue<T> queue;
    
    Disposable upstream;
    
    ConcatMapCompletableObserver(CompletableObserver param1CompletableObserver, Function<? super T, ? extends CompletableSource> param1Function, ErrorMode param1ErrorMode, int param1Int) {
      this.downstream = param1CompletableObserver;
      this.mapper = param1Function;
      this.errorMode = param1ErrorMode;
      this.prefetch = param1Int;
      this.errors = new AtomicThrowable();
      this.inner = new ConcatMapInnerObserver(this);
    }
    
    public void dispose() {
      this.disposed = true;
      this.upstream.dispose();
      this.inner.dispose();
      if (getAndIncrement() == 0)
        this.queue.clear(); 
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      AtomicThrowable atomicThrowable = this.errors;
      ErrorMode errorMode = this.errorMode;
      do {
        if (this.disposed) {
          this.queue.clear();
          return;
        } 
        if (this.active)
          continue; 
        if (errorMode == ErrorMode.BOUNDARY && atomicThrowable.get() != null) {
          this.disposed = true;
          this.queue.clear();
          Throwable throwable = atomicThrowable.terminate();
          this.downstream.onError(throwable);
          return;
        } 
        boolean bool = this.done;
        CompletableSource completableSource = null;
        try {
        
        } finally {
          completableSource = null;
          Exceptions.throwIfFatal((Throwable)completableSource);
          this.disposed = true;
          this.queue.clear();
          this.upstream.dispose();
          atomicThrowable.addThrowable((Throwable)completableSource);
          Throwable throwable = atomicThrowable.terminate();
          this.downstream.onError(throwable);
        } 
      } while (decrementAndGet() != 0);
    }
    
    void innerComplete() {
      this.active = false;
      drain();
    }
    
    void innerError(Throwable param1Throwable) {
      if (this.errors.addThrowable(param1Throwable)) {
        if (this.errorMode == ErrorMode.IMMEDIATE) {
          this.disposed = true;
          this.upstream.dispose();
          param1Throwable = this.errors.terminate();
          if (param1Throwable != ExceptionHelper.TERMINATED)
            this.downstream.onError(param1Throwable); 
          if (getAndIncrement() == 0)
            this.queue.clear(); 
        } else {
          this.active = false;
          drain();
        } 
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public boolean isDisposed() {
      return this.disposed;
    }
    
    public void onComplete() {
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.errors.addThrowable(param1Throwable)) {
        if (this.errorMode == ErrorMode.IMMEDIATE) {
          this.disposed = true;
          this.inner.dispose();
          param1Throwable = this.errors.terminate();
          if (param1Throwable != ExceptionHelper.TERMINATED)
            this.downstream.onError(param1Throwable); 
          if (getAndIncrement() == 0)
            this.queue.clear(); 
        } else {
          this.done = true;
          drain();
        } 
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      if (param1T != null)
        this.queue.offer(param1T); 
      drain();
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        if (param1Disposable instanceof QueueDisposable) {
          QueueDisposable queueDisposable = (QueueDisposable)param1Disposable;
          int i = queueDisposable.requestFusion(3);
          if (i == 1) {
            this.queue = (SimpleQueue<T>)queueDisposable;
            this.done = true;
            this.downstream.onSubscribe(this);
            drain();
            return;
          } 
          if (i == 2) {
            this.queue = (SimpleQueue<T>)queueDisposable;
            this.downstream.onSubscribe(this);
            return;
          } 
        } 
        this.queue = (SimpleQueue<T>)new SpscLinkedArrayQueue(this.prefetch);
        this.downstream.onSubscribe(this);
      } 
    }
    
    static final class ConcatMapInnerObserver extends AtomicReference<Disposable> implements CompletableObserver {
      private static final long serialVersionUID = 5638352172918776687L;
      
      final ObservableConcatMapCompletable.ConcatMapCompletableObserver<?> parent;
      
      ConcatMapInnerObserver(ObservableConcatMapCompletable.ConcatMapCompletableObserver<?> param2ConcatMapCompletableObserver) {
        this.parent = param2ConcatMapCompletableObserver;
      }
      
      void dispose() {
        DisposableHelper.dispose(this);
      }
      
      public void onComplete() {
        this.parent.innerComplete();
      }
      
      public void onError(Throwable param2Throwable) {
        this.parent.innerError(param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.replace(this, param2Disposable);
      }
    }
  }
  
  static final class ConcatMapInnerObserver extends AtomicReference<Disposable> implements CompletableObserver {
    private static final long serialVersionUID = 5638352172918776687L;
    
    final ObservableConcatMapCompletable.ConcatMapCompletableObserver<?> parent;
    
    ConcatMapInnerObserver(ObservableConcatMapCompletable.ConcatMapCompletableObserver<?> param1ConcatMapCompletableObserver) {
      this.parent = param1ConcatMapCompletableObserver;
    }
    
    void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public void onComplete() {
      this.parent.innerComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.innerError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.replace(this, param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/mixed/ObservableConcatMapCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */