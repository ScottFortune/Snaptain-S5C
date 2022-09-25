package io.reactivex.internal.operators.single;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.observers.ResumeSingleObserver;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleDelayWithObservable<T, U> extends Single<T> {
  final ObservableSource<U> other;
  
  final SingleSource<T> source;
  
  public SingleDelayWithObservable(SingleSource<T> paramSingleSource, ObservableSource<U> paramObservableSource) {
    this.source = paramSingleSource;
    this.other = paramObservableSource;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    this.other.subscribe(new OtherSubscriber<T, Object>(paramSingleObserver, this.source));
  }
  
  static final class OtherSubscriber<T, U> extends AtomicReference<Disposable> implements Observer<U>, Disposable {
    private static final long serialVersionUID = -8565274649390031272L;
    
    boolean done;
    
    final SingleObserver<? super T> downstream;
    
    final SingleSource<T> source;
    
    OtherSubscriber(SingleObserver<? super T> param1SingleObserver, SingleSource<T> param1SingleSource) {
      this.downstream = param1SingleObserver;
      this.source = param1SingleSource;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      this.source.subscribe((SingleObserver)new ResumeSingleObserver(this, this.downstream));
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(U param1U) {
      get().dispose();
      onComplete();
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.set(this, param1Disposable))
        this.downstream.onSubscribe(this); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleDelayWithObservable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */