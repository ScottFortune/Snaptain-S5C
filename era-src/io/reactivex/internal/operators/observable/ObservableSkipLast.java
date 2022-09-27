package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.ArrayDeque;

public final class ObservableSkipLast<T> extends AbstractObservableWithUpstream<T, T> {
  final int skip;
  
  public ObservableSkipLast(ObservableSource<T> paramObservableSource, int paramInt) {
    super(paramObservableSource);
    this.skip = paramInt;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new SkipLastObserver<T>(paramObserver, this.skip));
  }
  
  static final class SkipLastObserver<T> extends ArrayDeque<T> implements Observer<T>, Disposable {
    private static final long serialVersionUID = -3807491841935125653L;
    
    final Observer<? super T> downstream;
    
    final int skip;
    
    Disposable upstream;
    
    SkipLastObserver(Observer<? super T> param1Observer, int param1Int) {
      super(param1Int);
      this.downstream = param1Observer;
      this.skip = param1Int;
    }
    
    public void dispose() {
      this.upstream.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.skip == size())
        this.downstream.onNext(poll()); 
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableSkipLast.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */