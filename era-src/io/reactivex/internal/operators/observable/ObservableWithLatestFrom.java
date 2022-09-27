package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.observers.SerializedObserver;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableWithLatestFrom<T, U, R> extends AbstractObservableWithUpstream<T, R> {
  final BiFunction<? super T, ? super U, ? extends R> combiner;
  
  final ObservableSource<? extends U> other;
  
  public ObservableWithLatestFrom(ObservableSource<T> paramObservableSource, BiFunction<? super T, ? super U, ? extends R> paramBiFunction, ObservableSource<? extends U> paramObservableSource1) {
    super(paramObservableSource);
    this.combiner = paramBiFunction;
    this.other = paramObservableSource1;
  }
  
  public void subscribeActual(Observer<? super R> paramObserver) {
    SerializedObserver serializedObserver = new SerializedObserver(paramObserver);
    paramObserver = new WithLatestFromObserver<R, U, R>((Observer<? super R>)serializedObserver, this.combiner);
    serializedObserver.onSubscribe((Disposable)paramObserver);
    this.other.subscribe(new WithLatestFromOtherObserver((WithLatestFromObserver)paramObserver));
    this.source.subscribe(paramObserver);
  }
  
  static final class WithLatestFromObserver<T, U, R> extends AtomicReference<U> implements Observer<T>, Disposable {
    private static final long serialVersionUID = -312246233408980075L;
    
    final BiFunction<? super T, ? super U, ? extends R> combiner;
    
    final Observer<? super R> downstream;
    
    final AtomicReference<Disposable> other = new AtomicReference<Disposable>();
    
    final AtomicReference<Disposable> upstream = new AtomicReference<Disposable>();
    
    WithLatestFromObserver(Observer<? super R> param1Observer, BiFunction<? super T, ? super U, ? extends R> param1BiFunction) {
      this.downstream = param1Observer;
      this.combiner = param1BiFunction;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this.upstream);
      DisposableHelper.dispose(this.other);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(this.upstream.get());
    }
    
    public void onComplete() {
      DisposableHelper.dispose(this.other);
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      DisposableHelper.dispose(this.other);
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      U u = get();
      if (u != null)
        try {
        
        } finally {
          param1T = null;
          Exceptions.throwIfFatal((Throwable)param1T);
          dispose();
        }  
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this.upstream, param1Disposable);
    }
    
    public void otherError(Throwable param1Throwable) {
      DisposableHelper.dispose(this.upstream);
      this.downstream.onError(param1Throwable);
    }
    
    public boolean setOther(Disposable param1Disposable) {
      return DisposableHelper.setOnce(this.other, param1Disposable);
    }
  }
  
  final class WithLatestFromOtherObserver implements Observer<U> {
    private final ObservableWithLatestFrom.WithLatestFromObserver<T, U, R> parent;
    
    WithLatestFromOtherObserver(ObservableWithLatestFrom.WithLatestFromObserver<T, U, R> param1WithLatestFromObserver) {
      this.parent = param1WithLatestFromObserver;
    }
    
    public void onComplete() {}
    
    public void onError(Throwable param1Throwable) {
      this.parent.otherError(param1Throwable);
    }
    
    public void onNext(U param1U) {
      this.parent.lazySet(param1U);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.parent.setOther(param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableWithLatestFrom.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */