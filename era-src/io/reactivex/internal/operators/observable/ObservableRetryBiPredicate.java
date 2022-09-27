package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiPredicate;
import io.reactivex.internal.disposables.SequentialDisposable;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableRetryBiPredicate<T> extends AbstractObservableWithUpstream<T, T> {
  final BiPredicate<? super Integer, ? super Throwable> predicate;
  
  public ObservableRetryBiPredicate(Observable<T> paramObservable, BiPredicate<? super Integer, ? super Throwable> paramBiPredicate) {
    super((ObservableSource<T>)paramObservable);
    this.predicate = paramBiPredicate;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    SequentialDisposable sequentialDisposable = new SequentialDisposable();
    paramObserver.onSubscribe((Disposable)sequentialDisposable);
    (new RetryBiObserver(paramObserver, this.predicate, sequentialDisposable, this.source)).subscribeNext();
  }
  
  static final class RetryBiObserver<T> extends AtomicInteger implements Observer<T> {
    private static final long serialVersionUID = -7098360935104053232L;
    
    final Observer<? super T> downstream;
    
    final BiPredicate<? super Integer, ? super Throwable> predicate;
    
    int retries;
    
    final ObservableSource<? extends T> source;
    
    final SequentialDisposable upstream;
    
    RetryBiObserver(Observer<? super T> param1Observer, BiPredicate<? super Integer, ? super Throwable> param1BiPredicate, SequentialDisposable param1SequentialDisposable, ObservableSource<? extends T> param1ObservableSource) {
      this.downstream = param1Observer;
      this.upstream = param1SequentialDisposable;
      this.source = param1ObservableSource;
      this.predicate = param1BiPredicate;
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      try {
        BiPredicate<? super Integer, ? super Throwable> biPredicate = this.predicate;
        int i = this.retries + 1;
        this.retries = i;
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        this.downstream.onError((Throwable)new CompositeException(new Throwable[] { param1Throwable, exception }));
      } 
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
          if (this.upstream.isDisposed())
            return; 
          this.source.subscribe(this);
          j = addAndGet(-i);
          i = j;
        } while (j != 0);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableRetryBiPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */