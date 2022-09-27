package io.reactivex.internal.operators.observable;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableConcatWithMaybe<T> extends AbstractObservableWithUpstream<T, T> {
  final MaybeSource<? extends T> other;
  
  public ObservableConcatWithMaybe(Observable<T> paramObservable, MaybeSource<? extends T> paramMaybeSource) {
    super((ObservableSource<T>)paramObservable);
    this.other = paramMaybeSource;
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new ConcatWithObserver<T>(paramObserver, this.other));
  }
  
  static final class ConcatWithObserver<T> extends AtomicReference<Disposable> implements Observer<T>, MaybeObserver<T>, Disposable {
    private static final long serialVersionUID = -1953724749712440952L;
    
    final Observer<? super T> downstream;
    
    boolean inMaybe;
    
    MaybeSource<? extends T> other;
    
    ConcatWithObserver(Observer<? super T> param1Observer, MaybeSource<? extends T> param1MaybeSource) {
      this.downstream = param1Observer;
      this.other = param1MaybeSource;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      if (this.inMaybe) {
        this.downstream.onComplete();
      } else {
        this.inMaybe = true;
        DisposableHelper.replace(this, null);
        MaybeSource<? extends T> maybeSource = this.other;
        this.other = null;
        maybeSource.subscribe(this);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.downstream.onNext(param1T);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.setOnce(this, param1Disposable) && !this.inMaybe)
        this.downstream.onSubscribe(this); 
    }
    
    public void onSuccess(T param1T) {
      this.downstream.onNext(param1T);
      this.downstream.onComplete();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableConcatWithMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */