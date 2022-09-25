package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.observers.SerializedObserver;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableSampleWithObservable<T> extends AbstractObservableWithUpstream<T, T> {
  final boolean emitLast;
  
  final ObservableSource<?> other;
  
  public ObservableSampleWithObservable(ObservableSource<T> paramObservableSource, ObservableSource<?> paramObservableSource1, boolean paramBoolean) {
    super(paramObservableSource);
    this.other = paramObservableSource1;
    this.emitLast = paramBoolean;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    SerializedObserver serializedObserver = new SerializedObserver(paramObserver);
    if (this.emitLast) {
      this.source.subscribe(new SampleMainEmitLast((Observer<?>)serializedObserver, this.other));
    } else {
      this.source.subscribe(new SampleMainNoLast((Observer<?>)serializedObserver, this.other));
    } 
  }
  
  static final class SampleMainEmitLast<T> extends SampleMainObserver<T> {
    private static final long serialVersionUID = -3029755663834015785L;
    
    volatile boolean done;
    
    final AtomicInteger wip = new AtomicInteger();
    
    SampleMainEmitLast(Observer<? super T> param1Observer, ObservableSource<?> param1ObservableSource) {
      super(param1Observer, param1ObservableSource);
    }
    
    void completion() {
      this.done = true;
      if (this.wip.getAndIncrement() == 0) {
        emit();
        this.downstream.onComplete();
      } 
    }
    
    void run() {
      if (this.wip.getAndIncrement() == 0)
        do {
          boolean bool = this.done;
          emit();
          if (bool) {
            this.downstream.onComplete();
            return;
          } 
        } while (this.wip.decrementAndGet() != 0); 
    }
  }
  
  static final class SampleMainNoLast<T> extends SampleMainObserver<T> {
    private static final long serialVersionUID = -3029755663834015785L;
    
    SampleMainNoLast(Observer<? super T> param1Observer, ObservableSource<?> param1ObservableSource) {
      super(param1Observer, param1ObservableSource);
    }
    
    void completion() {
      this.downstream.onComplete();
    }
    
    void run() {
      emit();
    }
  }
  
  static abstract class SampleMainObserver<T> extends AtomicReference<T> implements Observer<T>, Disposable {
    private static final long serialVersionUID = -3517602651313910099L;
    
    final Observer<? super T> downstream;
    
    final AtomicReference<Disposable> other = new AtomicReference<Disposable>();
    
    final ObservableSource<?> sampler;
    
    Disposable upstream;
    
    SampleMainObserver(Observer<? super T> param1Observer, ObservableSource<?> param1ObservableSource) {
      this.downstream = param1Observer;
      this.sampler = param1ObservableSource;
    }
    
    public void complete() {
      this.upstream.dispose();
      completion();
    }
    
    abstract void completion();
    
    public void dispose() {
      DisposableHelper.dispose(this.other);
      this.upstream.dispose();
    }
    
    void emit() {
      T t = getAndSet(null);
      if (t != null)
        this.downstream.onNext(t); 
    }
    
    public void error(Throwable param1Throwable) {
      this.upstream.dispose();
      this.downstream.onError(param1Throwable);
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (this.other.get() == DisposableHelper.DISPOSED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onComplete() {
      DisposableHelper.dispose(this.other);
      completion();
    }
    
    public void onError(Throwable param1Throwable) {
      DisposableHelper.dispose(this.other);
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      lazySet(param1T);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
        if (this.other.get() == null)
          this.sampler.subscribe(new ObservableSampleWithObservable.SamplerObserver(this)); 
      } 
    }
    
    abstract void run();
    
    boolean setOther(Disposable param1Disposable) {
      return DisposableHelper.setOnce(this.other, param1Disposable);
    }
  }
  
  static final class SamplerObserver<T> implements Observer<Object> {
    final ObservableSampleWithObservable.SampleMainObserver<T> parent;
    
    SamplerObserver(ObservableSampleWithObservable.SampleMainObserver<T> param1SampleMainObserver) {
      this.parent = param1SampleMainObserver;
    }
    
    public void onComplete() {
      this.parent.complete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.error(param1Throwable);
    }
    
    public void onNext(Object param1Object) {
      this.parent.run();
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.parent.setOther(param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableSampleWithObservable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */