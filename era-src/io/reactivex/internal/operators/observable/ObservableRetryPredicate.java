package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.disposables.SequentialDisposable;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableRetryPredicate<T> extends AbstractObservableWithUpstream<T, T> {
  final long count;
  
  final Predicate<? super Throwable> predicate;
  
  public ObservableRetryPredicate(Observable<T> paramObservable, long paramLong, Predicate<? super Throwable> paramPredicate) {
    super((ObservableSource<T>)paramObservable);
    this.predicate = paramPredicate;
    this.count = paramLong;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    SequentialDisposable sequentialDisposable = new SequentialDisposable();
    paramObserver.onSubscribe((Disposable)sequentialDisposable);
    (new RepeatObserver(paramObserver, this.count, this.predicate, sequentialDisposable, this.source)).subscribeNext();
  }
  
  static final class RepeatObserver<T> extends AtomicInteger implements Observer<T> {
    private static final long serialVersionUID = -7098360935104053232L;
    
    final Observer<? super T> downstream;
    
    final Predicate<? super Throwable> predicate;
    
    long remaining;
    
    final ObservableSource<? extends T> source;
    
    final SequentialDisposable upstream;
    
    RepeatObserver(Observer<? super T> param1Observer, long param1Long, Predicate<? super Throwable> param1Predicate, SequentialDisposable param1SequentialDisposable, ObservableSource<? extends T> param1ObservableSource) {
      this.downstream = param1Observer;
      this.upstream = param1SequentialDisposable;
      this.source = param1ObservableSource;
      this.predicate = param1Predicate;
      this.remaining = param1Long;
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      long l = this.remaining;
      if (l != Long.MAX_VALUE)
        this.remaining = l - 1L; 
      if (l == 0L) {
        this.downstream.onError(param1Throwable);
      } else {
        try {
          return;
        } finally {
          Exception exception = null;
          Exceptions.throwIfFatal(exception);
          this.downstream.onError((Throwable)new CompositeException(new Throwable[] { param1Throwable, exception }));
        } 
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableRetryPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */