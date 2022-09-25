package io.reactivex.internal.operators.single;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleFlatMapCompletable<T> extends Completable {
  final Function<? super T, ? extends CompletableSource> mapper;
  
  final SingleSource<T> source;
  
  public SingleFlatMapCompletable(SingleSource<T> paramSingleSource, Function<? super T, ? extends CompletableSource> paramFunction) {
    this.source = paramSingleSource;
    this.mapper = paramFunction;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    FlatMapCompletableObserver<T> flatMapCompletableObserver = new FlatMapCompletableObserver<T>(paramCompletableObserver, this.mapper);
    paramCompletableObserver.onSubscribe(flatMapCompletableObserver);
    this.source.subscribe(flatMapCompletableObserver);
  }
  
  static final class FlatMapCompletableObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T>, CompletableObserver, Disposable {
    private static final long serialVersionUID = -2177128922851101253L;
    
    final CompletableObserver downstream;
    
    final Function<? super T, ? extends CompletableSource> mapper;
    
    FlatMapCompletableObserver(CompletableObserver param1CompletableObserver, Function<? super T, ? extends CompletableSource> param1Function) {
      this.downstream = param1CompletableObserver;
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
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.replace(this, param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      try {
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        onError((Throwable)param1T);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleFlatMapCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */