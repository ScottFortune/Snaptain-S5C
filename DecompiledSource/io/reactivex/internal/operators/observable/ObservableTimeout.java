package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableTimeout<T, U, V> extends AbstractObservableWithUpstream<T, T> {
  final ObservableSource<U> firstTimeoutIndicator;
  
  final Function<? super T, ? extends ObservableSource<V>> itemTimeoutIndicator;
  
  final ObservableSource<? extends T> other;
  
  public ObservableTimeout(Observable<T> paramObservable, ObservableSource<U> paramObservableSource, Function<? super T, ? extends ObservableSource<V>> paramFunction, ObservableSource<? extends T> paramObservableSource1) {
    super((ObservableSource<T>)paramObservable);
    this.firstTimeoutIndicator = paramObservableSource;
    this.itemTimeoutIndicator = paramFunction;
    this.other = paramObservableSource1;
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    TimeoutObserver<T> timeoutObserver;
    ObservableSource<? extends T> observableSource = this.other;
    if (observableSource == null) {
      timeoutObserver = new TimeoutObserver<T>(paramObserver, this.itemTimeoutIndicator);
      paramObserver.onSubscribe(timeoutObserver);
      timeoutObserver.startFirstTimeout(this.firstTimeoutIndicator);
      this.source.subscribe(timeoutObserver);
    } else {
      TimeoutFallbackObserver<T> timeoutFallbackObserver = new TimeoutFallbackObserver<T>(paramObserver, this.itemTimeoutIndicator, (ObservableSource<? extends T>)timeoutObserver);
      paramObserver.onSubscribe(timeoutFallbackObserver);
      timeoutFallbackObserver.startFirstTimeout(this.firstTimeoutIndicator);
      this.source.subscribe(timeoutFallbackObserver);
    } 
  }
  
  static final class TimeoutConsumer extends AtomicReference<Disposable> implements Observer<Object>, Disposable {
    private static final long serialVersionUID = 8708641127342403073L;
    
    final long idx;
    
    final ObservableTimeout.TimeoutSelectorSupport parent;
    
    TimeoutConsumer(long param1Long, ObservableTimeout.TimeoutSelectorSupport param1TimeoutSelectorSupport) {
      this.idx = param1Long;
      this.parent = param1TimeoutSelectorSupport;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      if (get() != DisposableHelper.DISPOSED) {
        lazySet((Disposable)DisposableHelper.DISPOSED);
        this.parent.onTimeout(this.idx);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (get() != DisposableHelper.DISPOSED) {
        lazySet((Disposable)DisposableHelper.DISPOSED);
        this.parent.onTimeoutError(this.idx, param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(Object param1Object) {
      param1Object = get();
      if (param1Object != DisposableHelper.DISPOSED) {
        param1Object.dispose();
        lazySet((Disposable)DisposableHelper.DISPOSED);
        this.parent.onTimeout(this.idx);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
  }
  
  static final class TimeoutFallbackObserver<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable, TimeoutSelectorSupport {
    private static final long serialVersionUID = -7508389464265974549L;
    
    final Observer<? super T> downstream;
    
    ObservableSource<? extends T> fallback;
    
    final AtomicLong index;
    
    final Function<? super T, ? extends ObservableSource<?>> itemTimeoutIndicator;
    
    final SequentialDisposable task;
    
    final AtomicReference<Disposable> upstream;
    
    TimeoutFallbackObserver(Observer<? super T> param1Observer, Function<? super T, ? extends ObservableSource<?>> param1Function, ObservableSource<? extends T> param1ObservableSource) {
      this.downstream = param1Observer;
      this.itemTimeoutIndicator = param1Function;
      this.task = new SequentialDisposable();
      this.fallback = param1ObservableSource;
      this.index = new AtomicLong();
      this.upstream = new AtomicReference<Disposable>();
    }
    
    public void dispose() {
      DisposableHelper.dispose(this.upstream);
      DisposableHelper.dispose(this);
      this.task.dispose();
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
        this.task.dispose();
        this.downstream.onComplete();
        this.task.dispose();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
        this.task.dispose();
        this.downstream.onError(param1Throwable);
        this.task.dispose();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      long l = this.index.get();
      if (l != Long.MAX_VALUE) {
        AtomicLong atomicLong = this.index;
        long l1 = 1L + l;
        if (atomicLong.compareAndSet(l, l1)) {
          Disposable disposable = (Disposable)this.task.get();
          if (disposable != null)
            disposable.dispose(); 
          this.downstream.onNext(param1T);
          try {
            return;
          } finally {
            param1T = null;
            Exceptions.throwIfFatal((Throwable)param1T);
            ((Disposable)this.upstream.get()).dispose();
            this.index.getAndSet(Long.MAX_VALUE);
          } 
        } 
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this.upstream, param1Disposable);
    }
    
    public void onTimeout(long param1Long) {
      if (this.index.compareAndSet(param1Long, Long.MAX_VALUE)) {
        DisposableHelper.dispose(this.upstream);
        ObservableSource<? extends T> observableSource = this.fallback;
        this.fallback = null;
        observableSource.subscribe(new ObservableTimeoutTimed.FallbackObserver<T>(this.downstream, this));
      } 
    }
    
    public void onTimeoutError(long param1Long, Throwable param1Throwable) {
      if (this.index.compareAndSet(param1Long, Long.MAX_VALUE)) {
        DisposableHelper.dispose(this);
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    void startFirstTimeout(ObservableSource<?> param1ObservableSource) {
      if (param1ObservableSource != null) {
        ObservableTimeout.TimeoutConsumer timeoutConsumer = new ObservableTimeout.TimeoutConsumer(0L, this);
        if (this.task.replace(timeoutConsumer))
          param1ObservableSource.subscribe(timeoutConsumer); 
      } 
    }
  }
  
  static final class TimeoutObserver<T> extends AtomicLong implements Observer<T>, Disposable, TimeoutSelectorSupport {
    private static final long serialVersionUID = 3764492702657003550L;
    
    final Observer<? super T> downstream;
    
    final Function<? super T, ? extends ObservableSource<?>> itemTimeoutIndicator;
    
    final SequentialDisposable task;
    
    final AtomicReference<Disposable> upstream;
    
    TimeoutObserver(Observer<? super T> param1Observer, Function<? super T, ? extends ObservableSource<?>> param1Function) {
      this.downstream = param1Observer;
      this.itemTimeoutIndicator = param1Function;
      this.task = new SequentialDisposable();
      this.upstream = new AtomicReference<Disposable>();
    }
    
    public void dispose() {
      DisposableHelper.dispose(this.upstream);
      this.task.dispose();
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(this.upstream.get());
    }
    
    public void onComplete() {
      if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
        this.task.dispose();
        this.downstream.onComplete();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
        this.task.dispose();
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      long l = get();
      if (l != Long.MAX_VALUE) {
        long l1 = 1L + l;
        if (compareAndSet(l, l1)) {
          Disposable disposable = (Disposable)this.task.get();
          if (disposable != null)
            disposable.dispose(); 
          this.downstream.onNext(param1T);
          try {
            return;
          } finally {
            param1T = null;
            Exceptions.throwIfFatal((Throwable)param1T);
            ((Disposable)this.upstream.get()).dispose();
            getAndSet(Long.MAX_VALUE);
          } 
        } 
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this.upstream, param1Disposable);
    }
    
    public void onTimeout(long param1Long) {
      if (compareAndSet(param1Long, Long.MAX_VALUE)) {
        DisposableHelper.dispose(this.upstream);
        this.downstream.onError(new TimeoutException());
      } 
    }
    
    public void onTimeoutError(long param1Long, Throwable param1Throwable) {
      if (compareAndSet(param1Long, Long.MAX_VALUE)) {
        DisposableHelper.dispose(this.upstream);
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    void startFirstTimeout(ObservableSource<?> param1ObservableSource) {
      if (param1ObservableSource != null) {
        ObservableTimeout.TimeoutConsumer timeoutConsumer = new ObservableTimeout.TimeoutConsumer(0L, this);
        if (this.task.replace(timeoutConsumer))
          param1ObservableSource.subscribe(timeoutConsumer); 
      } 
    }
  }
  
  static interface TimeoutSelectorSupport extends ObservableTimeoutTimed.TimeoutSupport {
    void onTimeoutError(long param1Long, Throwable param1Throwable);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableTimeout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */