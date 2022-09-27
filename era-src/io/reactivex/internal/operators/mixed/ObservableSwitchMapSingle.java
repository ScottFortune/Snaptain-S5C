package io.reactivex.internal.operators.mixed;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableSwitchMapSingle<T, R> extends Observable<R> {
  final boolean delayErrors;
  
  final Function<? super T, ? extends SingleSource<? extends R>> mapper;
  
  final Observable<T> source;
  
  public ObservableSwitchMapSingle(Observable<T> paramObservable, Function<? super T, ? extends SingleSource<? extends R>> paramFunction, boolean paramBoolean) {
    this.source = paramObservable;
    this.mapper = paramFunction;
    this.delayErrors = paramBoolean;
  }
  
  protected void subscribeActual(Observer<? super R> paramObserver) {
    if (!ScalarXMapZHelper.tryAsSingle(this.source, this.mapper, paramObserver))
      this.source.subscribe(new SwitchMapSingleMainObserver<T, R>(paramObserver, this.mapper, this.delayErrors)); 
  }
  
  static final class SwitchMapSingleMainObserver<T, R> extends AtomicInteger implements Observer<T>, Disposable {
    static final SwitchMapSingleObserver<Object> INNER_DISPOSED = new SwitchMapSingleObserver(null);
    
    private static final long serialVersionUID = -5402190102429853762L;
    
    volatile boolean cancelled;
    
    final boolean delayErrors;
    
    volatile boolean done;
    
    final Observer<? super R> downstream;
    
    final AtomicThrowable errors;
    
    final AtomicReference<SwitchMapSingleObserver<R>> inner;
    
    final Function<? super T, ? extends SingleSource<? extends R>> mapper;
    
    Disposable upstream;
    
    SwitchMapSingleMainObserver(Observer<? super R> param1Observer, Function<? super T, ? extends SingleSource<? extends R>> param1Function, boolean param1Boolean) {
      this.downstream = param1Observer;
      this.mapper = param1Function;
      this.delayErrors = param1Boolean;
      this.errors = new AtomicThrowable();
      this.inner = new AtomicReference<SwitchMapSingleObserver<R>>();
    }
    
    public void dispose() {
      this.cancelled = true;
      this.upstream.dispose();
      disposeInner();
    }
    
    void disposeInner() {
      SwitchMapSingleObserver<Object> switchMapSingleObserver = (SwitchMapSingleObserver)this.inner.getAndSet(INNER_DISPOSED);
      if (switchMapSingleObserver != null && switchMapSingleObserver != INNER_DISPOSED)
        switchMapSingleObserver.dispose(); 
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      Observer<? super R> observer = this.downstream;
      AtomicThrowable atomicThrowable = this.errors;
      AtomicReference<SwitchMapSingleObserver<R>> atomicReference = this.inner;
      int i = 1;
      while (true) {
        Throwable throwable;
        int j;
        if (this.cancelled)
          return; 
        if (atomicThrowable.get() != null && !this.delayErrors) {
          observer.onError(atomicThrowable.terminate());
          return;
        } 
        boolean bool = this.done;
        SwitchMapSingleObserver switchMapSingleObserver = atomicReference.get();
        if (switchMapSingleObserver == null) {
          j = 1;
        } else {
          j = 0;
        } 
        if (bool && j) {
          throwable = atomicThrowable.terminate();
          if (throwable != null) {
            observer.onError(throwable);
          } else {
            observer.onComplete();
          } 
          return;
        } 
        if (j || ((SwitchMapSingleObserver)throwable).item == null) {
          j = addAndGet(-i);
          i = j;
          if (j == 0)
            break; 
          continue;
        } 
        atomicReference.compareAndSet(throwable, null);
        observer.onNext(((SwitchMapSingleObserver)throwable).item);
      } 
    }
    
    void innerError(SwitchMapSingleObserver<R> param1SwitchMapSingleObserver, Throwable param1Throwable) {
      if (this.inner.compareAndSet(param1SwitchMapSingleObserver, null) && this.errors.addThrowable(param1Throwable)) {
        if (!this.delayErrors) {
          this.upstream.dispose();
          disposeInner();
        } 
        drain();
        return;
      } 
      RxJavaPlugins.onError(param1Throwable);
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
        if (!this.delayErrors)
          disposeInner(); 
        this.done = true;
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      SwitchMapSingleObserver<Object> switchMapSingleObserver = (SwitchMapSingleObserver)this.inner.get();
      if (switchMapSingleObserver != null)
        switchMapSingleObserver.dispose(); 
      try {
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        this.upstream.dispose();
        this.inner.getAndSet(INNER_DISPOSED);
        onError((Throwable)param1T);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
    
    static final class SwitchMapSingleObserver<R> extends AtomicReference<Disposable> implements SingleObserver<R> {
      private static final long serialVersionUID = 8042919737683345351L;
      
      volatile R item;
      
      final ObservableSwitchMapSingle.SwitchMapSingleMainObserver<?, R> parent;
      
      SwitchMapSingleObserver(ObservableSwitchMapSingle.SwitchMapSingleMainObserver<?, R> param2SwitchMapSingleMainObserver) {
        this.parent = param2SwitchMapSingleMainObserver;
      }
      
      void dispose() {
        DisposableHelper.dispose(this);
      }
      
      public void onError(Throwable param2Throwable) {
        this.parent.innerError(this, param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.setOnce(this, param2Disposable);
      }
      
      public void onSuccess(R param2R) {
        this.item = param2R;
        this.parent.drain();
      }
    }
  }
  
  static final class SwitchMapSingleObserver<R> extends AtomicReference<Disposable> implements SingleObserver<R> {
    private static final long serialVersionUID = 8042919737683345351L;
    
    volatile R item;
    
    final ObservableSwitchMapSingle.SwitchMapSingleMainObserver<?, R> parent;
    
    SwitchMapSingleObserver(ObservableSwitchMapSingle.SwitchMapSingleMainObserver<?, R> param1SwitchMapSingleMainObserver) {
      this.parent = param1SwitchMapSingleMainObserver;
    }
    
    void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.innerError(this, param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
    
    public void onSuccess(R param1R) {
      this.item = param1R;
      this.parent.drain();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/mixed/ObservableSwitchMapSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */