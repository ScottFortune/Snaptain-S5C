package io.reactivex.internal.operators.mixed;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.ErrorMode;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableConcatMapSingle<T, R> extends Observable<R> {
  final ErrorMode errorMode;
  
  final Function<? super T, ? extends SingleSource<? extends R>> mapper;
  
  final int prefetch;
  
  final Observable<T> source;
  
  public ObservableConcatMapSingle(Observable<T> paramObservable, Function<? super T, ? extends SingleSource<? extends R>> paramFunction, ErrorMode paramErrorMode, int paramInt) {
    this.source = paramObservable;
    this.mapper = paramFunction;
    this.errorMode = paramErrorMode;
    this.prefetch = paramInt;
  }
  
  protected void subscribeActual(Observer<? super R> paramObserver) {
    if (!ScalarXMapZHelper.tryAsSingle(this.source, this.mapper, paramObserver))
      this.source.subscribe(new ConcatMapSingleMainObserver<T, R>(paramObserver, this.mapper, this.prefetch, this.errorMode)); 
  }
  
  static final class ConcatMapSingleMainObserver<T, R> extends AtomicInteger implements Observer<T>, Disposable {
    static final int STATE_ACTIVE = 1;
    
    static final int STATE_INACTIVE = 0;
    
    static final int STATE_RESULT_VALUE = 2;
    
    private static final long serialVersionUID = -9140123220065488293L;
    
    volatile boolean cancelled;
    
    volatile boolean done;
    
    final Observer<? super R> downstream;
    
    final ErrorMode errorMode;
    
    final AtomicThrowable errors;
    
    final ConcatMapSingleObserver<R> inner;
    
    R item;
    
    final Function<? super T, ? extends SingleSource<? extends R>> mapper;
    
    final SimplePlainQueue<T> queue;
    
    volatile int state;
    
    Disposable upstream;
    
    ConcatMapSingleMainObserver(Observer<? super R> param1Observer, Function<? super T, ? extends SingleSource<? extends R>> param1Function, int param1Int, ErrorMode param1ErrorMode) {
      this.downstream = param1Observer;
      this.mapper = param1Function;
      this.errorMode = param1ErrorMode;
      this.errors = new AtomicThrowable();
      this.inner = new ConcatMapSingleObserver<R>(this);
      this.queue = (SimplePlainQueue<T>)new SpscLinkedArrayQueue(param1Int);
    }
    
    public void dispose() {
      this.cancelled = true;
      this.upstream.dispose();
      this.inner.dispose();
      if (getAndIncrement() == 0) {
        this.queue.clear();
        this.item = null;
      } 
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      Observer<? super R> observer = this.downstream;
      ErrorMode errorMode = this.errorMode;
      SimplePlainQueue<T> simplePlainQueue = this.queue;
      AtomicThrowable atomicThrowable = this.errors;
      int i = 1;
      while (true) {
        if (this.cancelled) {
          simplePlainQueue.clear();
          this.item = null;
        } else {
          int k = this.state;
          if (atomicThrowable.get() != null && (errorMode == ErrorMode.IMMEDIATE || (errorMode == ErrorMode.BOUNDARY && k == 0))) {
            simplePlainQueue.clear();
            this.item = null;
            observer.onError(atomicThrowable.terminate());
            return;
          } 
          boolean bool = false;
          if (k == 0) {
            Throwable throwable;
            boolean bool1 = this.done;
            Object object = simplePlainQueue.poll();
            if (object == null)
              bool = true; 
            if (bool1 && bool) {
              throwable = atomicThrowable.terminate();
              if (throwable == null) {
                observer.onComplete();
              } else {
                observer.onError(throwable);
              } 
              return;
            } 
            if (!bool)
              try {
              
              } finally {
                errorMode = null;
                Exceptions.throwIfFatal((Throwable)errorMode);
                this.upstream.dispose();
                throwable.clear();
                atomicThrowable.addThrowable((Throwable)errorMode);
                observer.onError(atomicThrowable.terminate());
              }  
          } else if (k == 2) {
            R r = this.item;
            this.item = null;
            observer.onNext(r);
            this.state = 0;
            continue;
          } 
        } 
        int j = addAndGet(-i);
        i = j;
        if (j == 0)
          break; 
      } 
    }
    
    void innerError(Throwable param1Throwable) {
      if (this.errors.addThrowable(param1Throwable)) {
        if (this.errorMode != ErrorMode.END)
          this.upstream.dispose(); 
        this.state = 0;
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    void innerSuccess(R param1R) {
      this.item = param1R;
      this.state = 2;
      drain();
    }
    
    public boolean isDisposed() {
      return this.cancelled;
    }
    
    public void onComplete() {
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.errors.addThrowable(param1Throwable)) {
        if (this.errorMode == ErrorMode.IMMEDIATE)
          this.inner.dispose(); 
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
      } 
    }
    
    static final class ConcatMapSingleObserver<R> extends AtomicReference<Disposable> implements SingleObserver<R> {
      private static final long serialVersionUID = -3051469169682093892L;
      
      final ObservableConcatMapSingle.ConcatMapSingleMainObserver<?, R> parent;
      
      ConcatMapSingleObserver(ObservableConcatMapSingle.ConcatMapSingleMainObserver<?, R> param2ConcatMapSingleMainObserver) {
        this.parent = param2ConcatMapSingleMainObserver;
      }
      
      void dispose() {
        DisposableHelper.dispose(this);
      }
      
      public void onError(Throwable param2Throwable) {
        this.parent.innerError(param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.replace(this, param2Disposable);
      }
      
      public void onSuccess(R param2R) {
        this.parent.innerSuccess(param2R);
      }
    }
  }
  
  static final class ConcatMapSingleObserver<R> extends AtomicReference<Disposable> implements SingleObserver<R> {
    private static final long serialVersionUID = -3051469169682093892L;
    
    final ObservableConcatMapSingle.ConcatMapSingleMainObserver<?, R> parent;
    
    ConcatMapSingleObserver(ObservableConcatMapSingle.ConcatMapSingleMainObserver<?, R> param1ConcatMapSingleMainObserver) {
      this.parent = param1ConcatMapSingleMainObserver;
    }
    
    void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.innerError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.replace(this, param1Disposable);
    }
    
    public void onSuccess(R param1R) {
      this.parent.innerSuccess(param1R);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/mixed/ObservableConcatMapSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */