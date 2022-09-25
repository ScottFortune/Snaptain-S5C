package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.ArrayDeque;

public final class ObservableTakeLast<T> extends AbstractObservableWithUpstream<T, T> {
  final int count;
  
  public ObservableTakeLast(ObservableSource<T> paramObservableSource, int paramInt) {
    super(paramObservableSource);
    this.count = paramInt;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new TakeLastObserver<T>(paramObserver, this.count));
  }
  
  static final class TakeLastObserver<T> extends ArrayDeque<T> implements Observer<T>, Disposable {
    private static final long serialVersionUID = 7240042530241604978L;
    
    volatile boolean cancelled;
    
    final int count;
    
    final Observer<? super T> downstream;
    
    Disposable upstream;
    
    TakeLastObserver(Observer<? super T> param1Observer, int param1Int) {
      this.downstream = param1Observer;
      this.count = param1Int;
    }
    
    public void dispose() {
      if (!this.cancelled) {
        this.cancelled = true;
        this.upstream.dispose();
      } 
    }
    
    public boolean isDisposed() {
      return this.cancelled;
    }
    
    public void onComplete() {
      Observer<? super T> observer = this.downstream;
      while (true) {
        if (this.cancelled)
          return; 
        T t = poll();
        if (t == null) {
          if (!this.cancelled)
            observer.onComplete(); 
          return;
        } 
        observer.onNext(t);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.count == size())
        poll(); 
      offer(param1T);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableTakeLast.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */