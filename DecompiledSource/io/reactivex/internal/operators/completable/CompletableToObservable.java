package io.reactivex.internal.operators.completable;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.observers.BasicQueueDisposable;

public final class CompletableToObservable<T> extends Observable<T> {
  final CompletableSource source;
  
  public CompletableToObservable(CompletableSource paramCompletableSource) {
    this.source = paramCompletableSource;
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new ObserverCompletableObserver(paramObserver));
  }
  
  static final class ObserverCompletableObserver extends BasicQueueDisposable<Void> implements CompletableObserver {
    final Observer<?> observer;
    
    Disposable upstream;
    
    ObserverCompletableObserver(Observer<?> param1Observer) {
      this.observer = param1Observer;
    }
    
    public void clear() {}
    
    public void dispose() {
      this.upstream.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public boolean isEmpty() {
      return true;
    }
    
    public void onComplete() {
      this.observer.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.observer.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.observer.onSubscribe((Disposable)this);
      } 
    }
    
    public Void poll() throws Exception {
      return null;
    }
    
    public int requestFusion(int param1Int) {
      return param1Int & 0x2;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableToObservable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */