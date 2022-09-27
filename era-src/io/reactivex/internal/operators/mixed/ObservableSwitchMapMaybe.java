package io.reactivex.internal.operators.mixed;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableSwitchMapMaybe<T, R> extends Observable<R> {
  final boolean delayErrors;
  
  final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
  
  final Observable<T> source;
  
  public ObservableSwitchMapMaybe(Observable<T> paramObservable, Function<? super T, ? extends MaybeSource<? extends R>> paramFunction, boolean paramBoolean) {
    this.source = paramObservable;
    this.mapper = paramFunction;
    this.delayErrors = paramBoolean;
  }
  
  protected void subscribeActual(Observer<? super R> paramObserver) {
    if (!ScalarXMapZHelper.tryAsMaybe(this.source, this.mapper, paramObserver))
      this.source.subscribe(new SwitchMapMaybeMainObserver<T, R>(paramObserver, this.mapper, this.delayErrors)); 
  }
  
  static final class SwitchMapMaybeMainObserver<T, R> extends AtomicInteger implements Observer<T>, Disposable {
    static final SwitchMapMaybeObserver<Object> INNER_DISPOSED = new SwitchMapMaybeObserver(null);
    
    private static final long serialVersionUID = -5402190102429853762L;
    
    volatile boolean cancelled;
    
    final boolean delayErrors;
    
    volatile boolean done;
    
    final Observer<? super R> downstream;
    
    final AtomicThrowable errors;
    
    final AtomicReference<SwitchMapMaybeObserver<R>> inner;
    
    final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
    
    Disposable upstream;
    
    SwitchMapMaybeMainObserver(Observer<? super R> param1Observer, Function<? super T, ? extends MaybeSource<? extends R>> param1Function, boolean param1Boolean) {
      this.downstream = param1Observer;
      this.mapper = param1Function;
      this.delayErrors = param1Boolean;
      this.errors = new AtomicThrowable();
      this.inner = new AtomicReference<SwitchMapMaybeObserver<R>>();
    }
    
    public void dispose() {
      this.cancelled = true;
      this.upstream.dispose();
      disposeInner();
    }
    
    void disposeInner() {
      SwitchMapMaybeObserver<Object> switchMapMaybeObserver = (SwitchMapMaybeObserver)this.inner.getAndSet(INNER_DISPOSED);
      if (switchMapMaybeObserver != null && switchMapMaybeObserver != INNER_DISPOSED)
        switchMapMaybeObserver.dispose(); 
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      Observer<? super R> observer = this.downstream;
      AtomicThrowable atomicThrowable = this.errors;
      AtomicReference<SwitchMapMaybeObserver<R>> atomicReference = this.inner;
      int i = 1;
      while (true) {
        int j;
        if (this.cancelled)
          return; 
        if (atomicThrowable.get() != null && !this.delayErrors) {
          observer.onError(atomicThrowable.terminate());
          return;
        } 
        boolean bool = this.done;
        SwitchMapMaybeObserver<R> switchMapMaybeObserver = atomicReference.get();
        if (switchMapMaybeObserver == null) {
          j = 1;
        } else {
          j = 0;
        } 
        if (bool && j) {
          Throwable throwable = atomicThrowable.terminate();
          if (throwable != null) {
            observer.onError(throwable);
          } else {
            observer.onComplete();
          } 
          return;
        } 
        if (j || switchMapMaybeObserver.item == null) {
          j = addAndGet(-i);
          i = j;
          if (j == 0)
            break; 
          continue;
        } 
        atomicReference.compareAndSet(switchMapMaybeObserver, null);
        observer.onNext(switchMapMaybeObserver.item);
      } 
    }
    
    void innerComplete(SwitchMapMaybeObserver<R> param1SwitchMapMaybeObserver) {
      if (this.inner.compareAndSet(param1SwitchMapMaybeObserver, null))
        drain(); 
    }
    
    void innerError(SwitchMapMaybeObserver<R> param1SwitchMapMaybeObserver, Throwable param1Throwable) {
      if (this.inner.compareAndSet(param1SwitchMapMaybeObserver, null) && this.errors.addThrowable(param1Throwable)) {
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
      SwitchMapMaybeObserver<Object> switchMapMaybeObserver = (SwitchMapMaybeObserver)this.inner.get();
      if (switchMapMaybeObserver != null)
        switchMapMaybeObserver.dispose(); 
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
    
    static final class SwitchMapMaybeObserver<R> extends AtomicReference<Disposable> implements MaybeObserver<R> {
      private static final long serialVersionUID = 8042919737683345351L;
      
      volatile R item;
      
      final ObservableSwitchMapMaybe.SwitchMapMaybeMainObserver<?, R> parent;
      
      SwitchMapMaybeObserver(ObservableSwitchMapMaybe.SwitchMapMaybeMainObserver<?, R> param2SwitchMapMaybeMainObserver) {
        this.parent = param2SwitchMapMaybeMainObserver;
      }
      
      void dispose() {
        DisposableHelper.dispose(this);
      }
      
      public void onComplete() {
        this.parent.innerComplete(this);
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
  
  static final class SwitchMapMaybeObserver<R> extends AtomicReference<Disposable> implements MaybeObserver<R> {
    private static final long serialVersionUID = 8042919737683345351L;
    
    volatile R item;
    
    final ObservableSwitchMapMaybe.SwitchMapMaybeMainObserver<?, R> parent;
    
    SwitchMapMaybeObserver(ObservableSwitchMapMaybe.SwitchMapMaybeMainObserver<?, R> param1SwitchMapMaybeMainObserver) {
      this.parent = param1SwitchMapMaybeMainObserver;
    }
    
    void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public void onComplete() {
      this.parent.innerComplete(this);
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/mixed/ObservableSwitchMapMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */