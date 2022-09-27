package io.reactivex.internal.operators.mixed;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class CompletableAndThenObservable<R> extends Observable<R> {
  final ObservableSource<? extends R> other;
  
  final CompletableSource source;
  
  public CompletableAndThenObservable(CompletableSource paramCompletableSource, ObservableSource<? extends R> paramObservableSource) {
    this.source = paramCompletableSource;
    this.other = paramObservableSource;
  }
  
  protected void subscribeActual(Observer<? super R> paramObserver) {
    AndThenObservableObserver<R> andThenObservableObserver = new AndThenObservableObserver<R>(paramObserver, this.other);
    paramObserver.onSubscribe(andThenObservableObserver);
    this.source.subscribe(andThenObservableObserver);
  }
  
  static final class AndThenObservableObserver<R> extends AtomicReference<Disposable> implements Observer<R>, CompletableObserver, Disposable {
    private static final long serialVersionUID = -8948264376121066672L;
    
    final Observer<? super R> downstream;
    
    ObservableSource<? extends R> other;
    
    AndThenObservableObserver(Observer<? super R> param1Observer, ObservableSource<? extends R> param1ObservableSource) {
      this.other = param1ObservableSource;
      this.downstream = param1Observer;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      ObservableSource<? extends R> observableSource = this.other;
      if (observableSource == null) {
        this.downstream.onComplete();
      } else {
        this.other = null;
        observableSource.subscribe(this);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(R param1R) {
      this.downstream.onNext(param1R);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.replace(this, param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/mixed/CompletableAndThenObservable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */