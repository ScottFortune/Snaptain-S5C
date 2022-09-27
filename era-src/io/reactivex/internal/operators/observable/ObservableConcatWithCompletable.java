package io.reactivex.internal.operators.observable;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableConcatWithCompletable<T> extends AbstractObservableWithUpstream<T, T> {
  final CompletableSource other;
  
  public ObservableConcatWithCompletable(Observable<T> paramObservable, CompletableSource paramCompletableSource) {
    super((ObservableSource<T>)paramObservable);
    this.other = paramCompletableSource;
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new ConcatWithObserver<T>(paramObserver, this.other));
  }
  
  static final class ConcatWithObserver<T> extends AtomicReference<Disposable> implements Observer<T>, CompletableObserver, Disposable {
    private static final long serialVersionUID = -1953724749712440952L;
    
    final Observer<? super T> downstream;
    
    boolean inCompletable;
    
    CompletableSource other;
    
    ConcatWithObserver(Observer<? super T> param1Observer, CompletableSource param1CompletableSource) {
      this.downstream = param1Observer;
      this.other = param1CompletableSource;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      if (this.inCompletable) {
        this.downstream.onComplete();
      } else {
        this.inCompletable = true;
        DisposableHelper.replace(this, null);
        CompletableSource completableSource = this.other;
        this.other = null;
        completableSource.subscribe(this);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.downstream.onNext(param1T);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.setOnce(this, param1Disposable) && !this.inCompletable)
        this.downstream.onSubscribe(this); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableConcatWithCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */