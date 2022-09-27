package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.SerializedObserver;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableDebounce<T, U> extends AbstractObservableWithUpstream<T, T> {
  final Function<? super T, ? extends ObservableSource<U>> debounceSelector;
  
  public ObservableDebounce(ObservableSource<T> paramObservableSource, Function<? super T, ? extends ObservableSource<U>> paramFunction) {
    super(paramObservableSource);
    this.debounceSelector = paramFunction;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new DebounceObserver<T, U>((Observer<? super T>)new SerializedObserver(paramObserver), this.debounceSelector));
  }
  
  static final class DebounceObserver<T, U> implements Observer<T>, Disposable {
    final Function<? super T, ? extends ObservableSource<U>> debounceSelector;
    
    final AtomicReference<Disposable> debouncer = new AtomicReference<Disposable>();
    
    boolean done;
    
    final Observer<? super T> downstream;
    
    volatile long index;
    
    Disposable upstream;
    
    DebounceObserver(Observer<? super T> param1Observer, Function<? super T, ? extends ObservableSource<U>> param1Function) {
      this.downstream = param1Observer;
      this.debounceSelector = param1Function;
    }
    
    public void dispose() {
      this.upstream.dispose();
      DisposableHelper.dispose(this.debouncer);
    }
    
    void emit(long param1Long, T param1T) {
      if (param1Long == this.index)
        this.downstream.onNext(param1T); 
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      Disposable disposable = this.debouncer.get();
      if (disposable != DisposableHelper.DISPOSED) {
        DebounceInnerObserver debounceInnerObserver = (DebounceInnerObserver)disposable;
        if (debounceInnerObserver != null)
          debounceInnerObserver.emit(); 
        DisposableHelper.dispose(this.debouncer);
        this.downstream.onComplete();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      DisposableHelper.dispose(this.debouncer);
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      long l = this.index + 1L;
      this.index = l;
      Disposable disposable = this.debouncer.get();
      if (disposable != null)
        disposable.dispose(); 
      try {
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        dispose();
        this.downstream.onError((Throwable)param1T);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
    
    static final class DebounceInnerObserver<T, U> extends DisposableObserver<U> {
      boolean done;
      
      final long index;
      
      final AtomicBoolean once = new AtomicBoolean();
      
      final ObservableDebounce.DebounceObserver<T, U> parent;
      
      final T value;
      
      DebounceInnerObserver(ObservableDebounce.DebounceObserver<T, U> param2DebounceObserver, long param2Long, T param2T) {
        this.parent = param2DebounceObserver;
        this.index = param2Long;
        this.value = param2T;
      }
      
      void emit() {
        if (this.once.compareAndSet(false, true))
          this.parent.emit(this.index, this.value); 
      }
      
      public void onComplete() {
        if (this.done)
          return; 
        this.done = true;
        emit();
      }
      
      public void onError(Throwable param2Throwable) {
        if (this.done) {
          RxJavaPlugins.onError(param2Throwable);
          return;
        } 
        this.done = true;
        this.parent.onError(param2Throwable);
      }
      
      public void onNext(U param2U) {
        if (this.done)
          return; 
        this.done = true;
        dispose();
        emit();
      }
    }
  }
  
  static final class DebounceInnerObserver<T, U> extends DisposableObserver<U> {
    boolean done;
    
    final long index;
    
    final AtomicBoolean once = new AtomicBoolean();
    
    final ObservableDebounce.DebounceObserver<T, U> parent;
    
    final T value;
    
    DebounceInnerObserver(ObservableDebounce.DebounceObserver<T, U> param1DebounceObserver, long param1Long, T param1T) {
      this.parent = param1DebounceObserver;
      this.index = param1Long;
      this.value = param1T;
    }
    
    void emit() {
      if (this.once.compareAndSet(false, true))
        this.parent.emit(this.index, this.value); 
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      emit();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.parent.onError(param1Throwable);
    }
    
    public void onNext(U param1U) {
      if (this.done)
        return; 
      this.done = true;
      dispose();
      emit();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableDebounce.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */