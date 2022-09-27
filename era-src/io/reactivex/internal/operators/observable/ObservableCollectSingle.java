package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.fuseable.FuseToObservable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;

public final class ObservableCollectSingle<T, U> extends Single<U> implements FuseToObservable<U> {
  final BiConsumer<? super U, ? super T> collector;
  
  final Callable<? extends U> initialSupplier;
  
  final ObservableSource<T> source;
  
  public ObservableCollectSingle(ObservableSource<T> paramObservableSource, Callable<? extends U> paramCallable, BiConsumer<? super U, ? super T> paramBiConsumer) {
    this.source = paramObservableSource;
    this.initialSupplier = paramCallable;
    this.collector = paramBiConsumer;
  }
  
  public Observable<U> fuseToObservable() {
    return RxJavaPlugins.onAssembly(new ObservableCollect<T, U>(this.source, this.initialSupplier, this.collector));
  }
  
  protected void subscribeActual(SingleObserver<? super U> paramSingleObserver) {
    try {
      return;
    } finally {
      Exception exception = null;
      EmptyDisposable.error(exception, paramSingleObserver);
    } 
  }
  
  static final class CollectObserver<T, U> implements Observer<T>, Disposable {
    final BiConsumer<? super U, ? super T> collector;
    
    boolean done;
    
    final SingleObserver<? super U> downstream;
    
    final U u;
    
    Disposable upstream;
    
    CollectObserver(SingleObserver<? super U> param1SingleObserver, U param1U, BiConsumer<? super U, ? super T> param1BiConsumer) {
      this.downstream = param1SingleObserver;
      this.collector = param1BiConsumer;
      this.u = param1U;
    }
    
    public void dispose() {
      this.upstream.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      this.downstream.onSuccess(this.u);
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableCollectSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */