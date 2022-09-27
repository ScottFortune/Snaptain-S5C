package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.SequentialDisposable;

public final class ObservableSwitchIfEmpty<T> extends AbstractObservableWithUpstream<T, T> {
  final ObservableSource<? extends T> other;
  
  public ObservableSwitchIfEmpty(ObservableSource<T> paramObservableSource, ObservableSource<? extends T> paramObservableSource1) {
    super(paramObservableSource);
    this.other = paramObservableSource1;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    SwitchIfEmptyObserver<T> switchIfEmptyObserver = new SwitchIfEmptyObserver<T>(paramObserver, this.other);
    paramObserver.onSubscribe((Disposable)switchIfEmptyObserver.arbiter);
    this.source.subscribe(switchIfEmptyObserver);
  }
  
  static final class SwitchIfEmptyObserver<T> implements Observer<T> {
    final SequentialDisposable arbiter;
    
    final Observer<? super T> downstream;
    
    boolean empty;
    
    final ObservableSource<? extends T> other;
    
    SwitchIfEmptyObserver(Observer<? super T> param1Observer, ObservableSource<? extends T> param1ObservableSource) {
      this.downstream = param1Observer;
      this.other = param1ObservableSource;
      this.empty = true;
      this.arbiter = new SequentialDisposable();
    }
    
    public void onComplete() {
      if (this.empty) {
        this.empty = false;
        this.other.subscribe(this);
      } else {
        this.downstream.onComplete();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.empty)
        this.empty = false; 
      this.downstream.onNext(param1T);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.arbiter.update(param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableSwitchIfEmpty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */