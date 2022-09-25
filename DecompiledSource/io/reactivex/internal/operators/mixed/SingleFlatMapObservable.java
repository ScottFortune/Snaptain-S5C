package io.reactivex.internal.operators.mixed;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleFlatMapObservable<T, R> extends Observable<R> {
  final Function<? super T, ? extends ObservableSource<? extends R>> mapper;
  
  final SingleSource<T> source;
  
  public SingleFlatMapObservable(SingleSource<T> paramSingleSource, Function<? super T, ? extends ObservableSource<? extends R>> paramFunction) {
    this.source = paramSingleSource;
    this.mapper = paramFunction;
  }
  
  protected void subscribeActual(Observer<? super R> paramObserver) {
    FlatMapObserver<T, R> flatMapObserver = new FlatMapObserver<T, R>(paramObserver, this.mapper);
    paramObserver.onSubscribe(flatMapObserver);
    this.source.subscribe(flatMapObserver);
  }
  
  static final class FlatMapObserver<T, R> extends AtomicReference<Disposable> implements Observer<R>, SingleObserver<T>, Disposable {
    private static final long serialVersionUID = -8948264376121066672L;
    
    final Observer<? super R> downstream;
    
    final Function<? super T, ? extends ObservableSource<? extends R>> mapper;
    
    FlatMapObserver(Observer<? super R> param1Observer, Function<? super T, ? extends ObservableSource<? extends R>> param1Function) {
      this.downstream = param1Observer;
      this.mapper = param1Function;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      this.downstream.onComplete();
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
    
    public void onSuccess(T param1T) {
      try {
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        this.downstream.onError((Throwable)param1T);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/mixed/SingleFlatMapObservable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */