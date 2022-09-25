package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.ArrayCompositeDisposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.observers.SerializedObserver;

public final class ObservableSkipUntil<T, U> extends AbstractObservableWithUpstream<T, T> {
  final ObservableSource<U> other;
  
  public ObservableSkipUntil(ObservableSource<T> paramObservableSource, ObservableSource<U> paramObservableSource1) {
    super(paramObservableSource);
    this.other = paramObservableSource1;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    SerializedObserver<T> serializedObserver = new SerializedObserver(paramObserver);
    ArrayCompositeDisposable arrayCompositeDisposable = new ArrayCompositeDisposable(2);
    serializedObserver.onSubscribe((Disposable)arrayCompositeDisposable);
    SkipUntilObserver<T> skipUntilObserver = new SkipUntilObserver((Observer<?>)serializedObserver, arrayCompositeDisposable);
    this.other.subscribe(new SkipUntil(arrayCompositeDisposable, skipUntilObserver, serializedObserver));
    this.source.subscribe(skipUntilObserver);
  }
  
  final class SkipUntil implements Observer<U> {
    final ArrayCompositeDisposable frc;
    
    final SerializedObserver<T> serial;
    
    final ObservableSkipUntil.SkipUntilObserver<T> sus;
    
    Disposable upstream;
    
    SkipUntil(ArrayCompositeDisposable param1ArrayCompositeDisposable, ObservableSkipUntil.SkipUntilObserver<T> param1SkipUntilObserver, SerializedObserver<T> param1SerializedObserver) {
      this.frc = param1ArrayCompositeDisposable;
      this.sus = param1SkipUntilObserver;
      this.serial = param1SerializedObserver;
    }
    
    public void onComplete() {
      this.sus.notSkipping = true;
    }
    
    public void onError(Throwable param1Throwable) {
      this.frc.dispose();
      this.serial.onError(param1Throwable);
    }
    
    public void onNext(U param1U) {
      this.upstream.dispose();
      this.sus.notSkipping = true;
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.frc.setResource(1, param1Disposable);
      } 
    }
  }
  
  static final class SkipUntilObserver<T> implements Observer<T> {
    final Observer<? super T> downstream;
    
    final ArrayCompositeDisposable frc;
    
    volatile boolean notSkipping;
    
    boolean notSkippingLocal;
    
    Disposable upstream;
    
    SkipUntilObserver(Observer<? super T> param1Observer, ArrayCompositeDisposable param1ArrayCompositeDisposable) {
      this.downstream = param1Observer;
      this.frc = param1ArrayCompositeDisposable;
    }
    
    public void onComplete() {
      this.frc.dispose();
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.frc.dispose();
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.notSkippingLocal) {
        this.downstream.onNext(param1T);
      } else if (this.notSkipping) {
        this.notSkippingLocal = true;
        this.downstream.onNext(param1T);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.frc.setResource(0, param1Disposable);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableSkipUntil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */