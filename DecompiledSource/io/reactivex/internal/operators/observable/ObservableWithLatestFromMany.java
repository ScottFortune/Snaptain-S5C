package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.HalfSerializer;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

public final class ObservableWithLatestFromMany<T, R> extends AbstractObservableWithUpstream<T, R> {
  final Function<? super Object[], R> combiner;
  
  final ObservableSource<?>[] otherArray = null;
  
  final Iterable<? extends ObservableSource<?>> otherIterable;
  
  public ObservableWithLatestFromMany(ObservableSource<T> paramObservableSource, Iterable<? extends ObservableSource<?>> paramIterable, Function<? super Object[], R> paramFunction) {
    super(paramObservableSource);
    this.otherIterable = paramIterable;
    this.combiner = paramFunction;
  }
  
  public ObservableWithLatestFromMany(ObservableSource<T> paramObservableSource, ObservableSource<?>[] paramArrayOfObservableSource, Function<? super Object[], R> paramFunction) {
    super(paramObservableSource);
    this.otherIterable = null;
    this.combiner = paramFunction;
  }
  
  protected void subscribeActual(Observer<? super R> paramObserver) {
    ObservableSource[] arrayOfObservableSource;
    int i;
    ObservableSource<?>[] arrayOfObservableSource1 = this.otherArray;
    if (arrayOfObservableSource1 == null) {
      ObservableSource[] arrayOfObservableSource2 = new ObservableSource[8];
      try {
        Iterator<? extends ObservableSource<?>> iterator = this.otherIterable.iterator();
        byte b = 0;
        while (true) {
          arrayOfObservableSource = arrayOfObservableSource2;
          i = b;
          if (iterator.hasNext()) {
            ObservableSource observableSource = iterator.next();
            arrayOfObservableSource = arrayOfObservableSource2;
            if (b == arrayOfObservableSource2.length)
              arrayOfObservableSource = Arrays.<ObservableSource>copyOf(arrayOfObservableSource2, (b >> 1) + b); 
            arrayOfObservableSource[b] = observableSource;
            b++;
            arrayOfObservableSource2 = arrayOfObservableSource;
            continue;
          } 
          break;
        } 
      } finally {
        arrayOfObservableSource2 = null;
        Exceptions.throwIfFatal((Throwable)arrayOfObservableSource2);
        EmptyDisposable.error((Throwable)arrayOfObservableSource2, paramObserver);
      } 
    } else {
      i = arrayOfObservableSource.length;
    } 
    if (i == 0) {
      (new ObservableMap<Object, R>(this.source, new SingletonArrayFunc())).subscribeActual(paramObserver);
      return;
    } 
    WithLatestFromObserver<Object, R> withLatestFromObserver = new WithLatestFromObserver<Object, R>(paramObserver, this.combiner, i);
    paramObserver.onSubscribe(withLatestFromObserver);
    withLatestFromObserver.subscribe((ObservableSource<?>[])arrayOfObservableSource, i);
    this.source.subscribe(withLatestFromObserver);
  }
  
  final class SingletonArrayFunc implements Function<T, R> {
    public R apply(T param1T) throws Exception {
      return (R)ObjectHelper.requireNonNull(ObservableWithLatestFromMany.this.combiner.apply(new Object[] { param1T }, ), "The combiner returned a null value");
    }
  }
  
  static final class WithLatestFromObserver<T, R> extends AtomicInteger implements Observer<T>, Disposable {
    private static final long serialVersionUID = 1577321883966341961L;
    
    final Function<? super Object[], R> combiner;
    
    volatile boolean done;
    
    final Observer<? super R> downstream;
    
    final AtomicThrowable error;
    
    final ObservableWithLatestFromMany.WithLatestInnerObserver[] observers;
    
    final AtomicReference<Disposable> upstream;
    
    final AtomicReferenceArray<Object> values;
    
    WithLatestFromObserver(Observer<? super R> param1Observer, Function<? super Object[], R> param1Function, int param1Int) {
      this.downstream = param1Observer;
      this.combiner = param1Function;
      ObservableWithLatestFromMany.WithLatestInnerObserver[] arrayOfWithLatestInnerObserver = new ObservableWithLatestFromMany.WithLatestInnerObserver[param1Int];
      for (byte b = 0; b < param1Int; b++)
        arrayOfWithLatestInnerObserver[b] = new ObservableWithLatestFromMany.WithLatestInnerObserver(this, b); 
      this.observers = arrayOfWithLatestInnerObserver;
      this.values = new AtomicReferenceArray(param1Int);
      this.upstream = new AtomicReference<Disposable>();
      this.error = new AtomicThrowable();
    }
    
    void cancelAllBut(int param1Int) {
      ObservableWithLatestFromMany.WithLatestInnerObserver[] arrayOfWithLatestInnerObserver = this.observers;
      for (byte b = 0; b < arrayOfWithLatestInnerObserver.length; b++) {
        if (b != param1Int)
          arrayOfWithLatestInnerObserver[b].dispose(); 
      } 
    }
    
    public void dispose() {
      DisposableHelper.dispose(this.upstream);
      ObservableWithLatestFromMany.WithLatestInnerObserver[] arrayOfWithLatestInnerObserver = this.observers;
      int i = arrayOfWithLatestInnerObserver.length;
      for (byte b = 0; b < i; b++)
        arrayOfWithLatestInnerObserver[b].dispose(); 
    }
    
    void innerComplete(int param1Int, boolean param1Boolean) {
      if (!param1Boolean) {
        this.done = true;
        cancelAllBut(param1Int);
        HalfSerializer.onComplete(this.downstream, this, this.error);
      } 
    }
    
    void innerError(int param1Int, Throwable param1Throwable) {
      this.done = true;
      DisposableHelper.dispose(this.upstream);
      cancelAllBut(param1Int);
      HalfSerializer.onError(this.downstream, param1Throwable, this, this.error);
    }
    
    void innerNext(int param1Int, Object param1Object) {
      this.values.set(param1Int, param1Object);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(this.upstream.get());
    }
    
    public void onComplete() {
      if (!this.done) {
        this.done = true;
        cancelAllBut(-1);
        HalfSerializer.onComplete(this.downstream, this, this.error);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      cancelAllBut(-1);
      HalfSerializer.onError(this.downstream, param1Throwable, this, this.error);
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      AtomicReferenceArray<Object> atomicReferenceArray = this.values;
      int i = atomicReferenceArray.length();
      Object[] arrayOfObject = new Object[i + 1];
      byte b = 0;
      arrayOfObject[0] = param1T;
      while (b < i) {
        param1T = (T)atomicReferenceArray.get(b);
        if (param1T == null)
          return; 
        arrayOfObject[++b] = param1T;
      } 
      try {
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        dispose();
        onError((Throwable)param1T);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this.upstream, param1Disposable);
    }
    
    void subscribe(ObservableSource<?>[] param1ArrayOfObservableSource, int param1Int) {
      ObservableWithLatestFromMany.WithLatestInnerObserver[] arrayOfWithLatestInnerObserver = this.observers;
      AtomicReference<Disposable> atomicReference = this.upstream;
      for (byte b = 0; b < param1Int && !DisposableHelper.isDisposed(atomicReference.get()) && !this.done; b++)
        param1ArrayOfObservableSource[b].subscribe(arrayOfWithLatestInnerObserver[b]); 
    }
  }
  
  static final class WithLatestInnerObserver extends AtomicReference<Disposable> implements Observer<Object> {
    private static final long serialVersionUID = 3256684027868224024L;
    
    boolean hasValue;
    
    final int index;
    
    final ObservableWithLatestFromMany.WithLatestFromObserver<?, ?> parent;
    
    WithLatestInnerObserver(ObservableWithLatestFromMany.WithLatestFromObserver<?, ?> param1WithLatestFromObserver, int param1Int) {
      this.parent = param1WithLatestFromObserver;
      this.index = param1Int;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public void onComplete() {
      this.parent.innerComplete(this.index, this.hasValue);
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.innerError(this.index, param1Throwable);
    }
    
    public void onNext(Object param1Object) {
      if (!this.hasValue)
        this.hasValue = true; 
      this.parent.innerNext(this.index, param1Object);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableWithLatestFromMany.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */