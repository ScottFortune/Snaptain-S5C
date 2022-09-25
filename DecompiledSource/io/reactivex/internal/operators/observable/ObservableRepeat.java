package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.SequentialDisposable;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableRepeat<T> extends AbstractObservableWithUpstream<T, T> {
  final long count;
  
  public ObservableRepeat(Observable<T> paramObservable, long paramLong) {
    super((ObservableSource<T>)paramObservable);
    this.count = paramLong;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    SequentialDisposable sequentialDisposable = new SequentialDisposable();
    paramObserver.onSubscribe((Disposable)sequentialDisposable);
    long l1 = this.count;
    long l2 = Long.MAX_VALUE;
    if (l1 != Long.MAX_VALUE)
      l2 = l1 - 1L; 
    (new RepeatObserver(paramObserver, l2, sequentialDisposable, this.source)).subscribeNext();
  }
  
  static final class RepeatObserver<T> extends AtomicInteger implements Observer<T> {
    private static final long serialVersionUID = -7098360935104053232L;
    
    final Observer<? super T> downstream;
    
    long remaining;
    
    final SequentialDisposable sd;
    
    final ObservableSource<? extends T> source;
    
    RepeatObserver(Observer<? super T> param1Observer, long param1Long, SequentialDisposable param1SequentialDisposable, ObservableSource<? extends T> param1ObservableSource) {
      this.downstream = param1Observer;
      this.sd = param1SequentialDisposable;
      this.source = param1ObservableSource;
      this.remaining = param1Long;
    }
    
    public void onComplete() {
      long l = this.remaining;
      if (l != Long.MAX_VALUE)
        this.remaining = l - 1L; 
      if (l != 0L) {
        subscribeNext();
      } else {
        this.downstream.onComplete();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.downstream.onNext(param1T);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.sd.replace(param1Disposable);
    }
    
    void subscribeNext() {
      if (getAndIncrement() == 0) {
        int j;
        int i = 1;
        do {
          if (this.sd.isDisposed())
            return; 
          this.source.subscribe(this);
          j = addAndGet(-i);
          i = j;
        } while (j != 0);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableRepeat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */