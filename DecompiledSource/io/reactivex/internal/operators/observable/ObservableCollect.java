package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;

public final class ObservableCollect<T, U> extends AbstractObservableWithUpstream<T, U> {
  final BiConsumer<? super U, ? super T> collector;
  
  final Callable<? extends U> initialSupplier;
  
  public ObservableCollect(ObservableSource<T> paramObservableSource, Callable<? extends U> paramCallable, BiConsumer<? super U, ? super T> paramBiConsumer) {
    super(paramObservableSource);
    this.initialSupplier = paramCallable;
    this.collector = paramBiConsumer;
  }
  
  protected void subscribeActual(Observer<? super U> paramObserver) {
    try {
      return;
    } finally {
      Exception exception = null;
      EmptyDisposable.error(exception, paramObserver);
    } 
  }
  
  static final class CollectObserver<T, U> implements Observer<T>, Disposable {
    final BiConsumer<? super U, ? super T> collector;
    
    boolean done;
    
    final Observer<? super U> downstream;
    
    final U u;
    
    Disposable upstream;
    
    CollectObserver(Observer<? super U> param1Observer, U param1U, BiConsumer<? super U, ? super T> param1BiConsumer) {
      this.downstream = param1Observer;
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
      this.downstream.onNext(this.u);
      this.downstream.onComplete();
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableCollect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */