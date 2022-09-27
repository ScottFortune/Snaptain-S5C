package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableConcatWithSingle<T> extends AbstractObservableWithUpstream<T, T> {
  final SingleSource<? extends T> other;
  
  public ObservableConcatWithSingle(Observable<T> paramObservable, SingleSource<? extends T> paramSingleSource) {
    super((ObservableSource<T>)paramObservable);
    this.other = paramSingleSource;
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new ConcatWithObserver<T>(paramObserver, this.other));
  }
  
  static final class ConcatWithObserver<T> extends AtomicReference<Disposable> implements Observer<T>, SingleObserver<T>, Disposable {
    private static final long serialVersionUID = -1953724749712440952L;
    
    final Observer<? super T> downstream;
    
    boolean inSingle;
    
    SingleSource<? extends T> other;
    
    ConcatWithObserver(Observer<? super T> param1Observer, SingleSource<? extends T> param1SingleSource) {
      this.downstream = param1Observer;
      this.other = param1SingleSource;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      this.inSingle = true;
      DisposableHelper.replace(this, null);
      SingleSource<? extends T> singleSource = this.other;
      this.other = null;
      singleSource.subscribe(this);
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.downstream.onNext(param1T);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.setOnce(this, param1Disposable) && !this.inSingle)
        this.downstream.onSubscribe(this); 
    }
    
    public void onSuccess(T param1T) {
      this.downstream.onNext(param1T);
      this.downstream.onComplete();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableConcatWithSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */