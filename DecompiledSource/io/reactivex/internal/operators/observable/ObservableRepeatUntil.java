package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.internal.disposables.SequentialDisposable;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableRepeatUntil<T> extends AbstractObservableWithUpstream<T, T> {
  final BooleanSupplier until;
  
  public ObservableRepeatUntil(Observable<T> paramObservable, BooleanSupplier paramBooleanSupplier) {
    super((ObservableSource<T>)paramObservable);
    this.until = paramBooleanSupplier;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    SequentialDisposable sequentialDisposable = new SequentialDisposable();
    paramObserver.onSubscribe((Disposable)sequentialDisposable);
    (new RepeatUntilObserver(paramObserver, this.until, sequentialDisposable, this.source)).subscribeNext();
  }
  
  static final class RepeatUntilObserver<T> extends AtomicInteger implements Observer<T> {
    private static final long serialVersionUID = -7098360935104053232L;
    
    final Observer<? super T> downstream;
    
    final ObservableSource<? extends T> source;
    
    final BooleanSupplier stop;
    
    final SequentialDisposable upstream;
    
    RepeatUntilObserver(Observer<? super T> param1Observer, BooleanSupplier param1BooleanSupplier, SequentialDisposable param1SequentialDisposable, ObservableSource<? extends T> param1ObservableSource) {
      this.downstream = param1Observer;
      this.upstream = param1SequentialDisposable;
      this.source = param1ObservableSource;
      this.stop = param1BooleanSupplier;
    }
    
    public void onComplete() {
      try {
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        this.downstream.onError(exception);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.downstream.onNext(param1T);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.upstream.replace(param1Disposable);
    }
    
    void subscribeNext() {
      if (getAndIncrement() == 0) {
        int j;
        int i = 1;
        do {
          this.source.subscribe(this);
          j = addAndGet(-i);
          i = j;
        } while (j != 0);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableRepeatUntil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */