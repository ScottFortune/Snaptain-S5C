package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableDelaySubscriptionOther<T, U> extends Observable<T> {
  final ObservableSource<? extends T> main;
  
  final ObservableSource<U> other;
  
  public ObservableDelaySubscriptionOther(ObservableSource<? extends T> paramObservableSource, ObservableSource<U> paramObservableSource1) {
    this.main = paramObservableSource;
    this.other = paramObservableSource1;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    SequentialDisposable sequentialDisposable = new SequentialDisposable();
    paramObserver.onSubscribe((Disposable)sequentialDisposable);
    paramObserver = new DelayObserver(sequentialDisposable, paramObserver);
    this.other.subscribe(paramObserver);
  }
  
  final class DelayObserver implements Observer<U> {
    final Observer<? super T> child;
    
    boolean done;
    
    final SequentialDisposable serial;
    
    DelayObserver(SequentialDisposable param1SequentialDisposable, Observer<? super T> param1Observer) {
      this.serial = param1SequentialDisposable;
      this.child = param1Observer;
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      ObservableDelaySubscriptionOther.this.main.subscribe(new OnComplete());
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.child.onError(param1Throwable);
    }
    
    public void onNext(U param1U) {
      onComplete();
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.serial.update(param1Disposable);
    }
    
    final class OnComplete implements Observer<T> {
      public void onComplete() {
        ObservableDelaySubscriptionOther.DelayObserver.this.child.onComplete();
      }
      
      public void onError(Throwable param2Throwable) {
        ObservableDelaySubscriptionOther.DelayObserver.this.child.onError(param2Throwable);
      }
      
      public void onNext(T param2T) {
        ObservableDelaySubscriptionOther.DelayObserver.this.child.onNext(param2T);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        ObservableDelaySubscriptionOther.DelayObserver.this.serial.update(param2Disposable);
      }
    }
  }
  
  final class OnComplete implements Observer<T> {
    public void onComplete() {
      this.this$1.child.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.this$1.child.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.this$1.child.onNext(param1T);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.this$1.serial.update(param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableDelaySubscriptionOther.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */