package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.HalfSerializer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableTakeUntil<T, U> extends AbstractObservableWithUpstream<T, T> {
  final ObservableSource<? extends U> other;
  
  public ObservableTakeUntil(ObservableSource<T> paramObservableSource, ObservableSource<? extends U> paramObservableSource1) {
    super(paramObservableSource);
    this.other = paramObservableSource1;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    TakeUntilMainObserver<T, Object> takeUntilMainObserver = new TakeUntilMainObserver<T, Object>(paramObserver);
    paramObserver.onSubscribe(takeUntilMainObserver);
    this.other.subscribe(takeUntilMainObserver.otherObserver);
    this.source.subscribe(takeUntilMainObserver);
  }
  
  static final class TakeUntilMainObserver<T, U> extends AtomicInteger implements Observer<T>, Disposable {
    private static final long serialVersionUID = 1418547743690811973L;
    
    final Observer<? super T> downstream;
    
    final AtomicThrowable error;
    
    final OtherObserver otherObserver;
    
    final AtomicReference<Disposable> upstream;
    
    TakeUntilMainObserver(Observer<? super T> param1Observer) {
      this.downstream = param1Observer;
      this.upstream = new AtomicReference<Disposable>();
      this.otherObserver = new OtherObserver();
      this.error = new AtomicThrowable();
    }
    
    public void dispose() {
      DisposableHelper.dispose(this.upstream);
      DisposableHelper.dispose(this.otherObserver);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(this.upstream.get());
    }
    
    public void onComplete() {
      DisposableHelper.dispose(this.otherObserver);
      HalfSerializer.onComplete(this.downstream, this, this.error);
    }
    
    public void onError(Throwable param1Throwable) {
      DisposableHelper.dispose(this.otherObserver);
      HalfSerializer.onError(this.downstream, param1Throwable, this, this.error);
    }
    
    public void onNext(T param1T) {
      HalfSerializer.onNext(this.downstream, param1T, this, this.error);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this.upstream, param1Disposable);
    }
    
    void otherComplete() {
      DisposableHelper.dispose(this.upstream);
      HalfSerializer.onComplete(this.downstream, this, this.error);
    }
    
    void otherError(Throwable param1Throwable) {
      DisposableHelper.dispose(this.upstream);
      HalfSerializer.onError(this.downstream, param1Throwable, this, this.error);
    }
    
    final class OtherObserver extends AtomicReference<Disposable> implements Observer<U> {
      private static final long serialVersionUID = -8693423678067375039L;
      
      public void onComplete() {
        ObservableTakeUntil.TakeUntilMainObserver.this.otherComplete();
      }
      
      public void onError(Throwable param2Throwable) {
        ObservableTakeUntil.TakeUntilMainObserver.this.otherError(param2Throwable);
      }
      
      public void onNext(U param2U) {
        DisposableHelper.dispose(this);
        ObservableTakeUntil.TakeUntilMainObserver.this.otherComplete();
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.setOnce(this, param2Disposable);
      }
    }
  }
  
  final class OtherObserver extends AtomicReference<Disposable> implements Observer<U> {
    private static final long serialVersionUID = -8693423678067375039L;
    
    public void onComplete() {
      this.this$0.otherComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.this$0.otherError(param1Throwable);
    }
    
    public void onNext(U param1U) {
      DisposableHelper.dispose(this);
      this.this$0.otherComplete();
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableTakeUntil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */