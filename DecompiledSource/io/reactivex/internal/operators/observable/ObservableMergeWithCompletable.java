package io.reactivex.internal.operators.observable;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.HalfSerializer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableMergeWithCompletable<T> extends AbstractObservableWithUpstream<T, T> {
  final CompletableSource other;
  
  public ObservableMergeWithCompletable(Observable<T> paramObservable, CompletableSource paramCompletableSource) {
    super((ObservableSource<T>)paramObservable);
    this.other = paramCompletableSource;
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    MergeWithObserver<T> mergeWithObserver = new MergeWithObserver<T>(paramObserver);
    paramObserver.onSubscribe(mergeWithObserver);
    this.source.subscribe(mergeWithObserver);
    this.other.subscribe(mergeWithObserver.otherObserver);
  }
  
  static final class MergeWithObserver<T> extends AtomicInteger implements Observer<T>, Disposable {
    private static final long serialVersionUID = -4592979584110982903L;
    
    final Observer<? super T> downstream;
    
    final AtomicThrowable error;
    
    final AtomicReference<Disposable> mainDisposable;
    
    volatile boolean mainDone;
    
    volatile boolean otherDone;
    
    final OtherObserver otherObserver;
    
    MergeWithObserver(Observer<? super T> param1Observer) {
      this.downstream = param1Observer;
      this.mainDisposable = new AtomicReference<Disposable>();
      this.otherObserver = new OtherObserver(this);
      this.error = new AtomicThrowable();
    }
    
    public void dispose() {
      DisposableHelper.dispose(this.mainDisposable);
      DisposableHelper.dispose(this.otherObserver);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(this.mainDisposable.get());
    }
    
    public void onComplete() {
      this.mainDone = true;
      if (this.otherDone)
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
      DisposableHelper.setOnce(this.mainDisposable, param1Disposable);
    }
    
    void otherComplete() {
      this.otherDone = true;
      if (this.mainDone)
        HalfSerializer.onComplete(this.downstream, this, this.error); 
    }
    
    void otherError(Throwable param1Throwable) {
      DisposableHelper.dispose(this.mainDisposable);
      HalfSerializer.onError(this.downstream, param1Throwable, this, this.error);
    }
    
    static final class OtherObserver extends AtomicReference<Disposable> implements CompletableObserver {
      private static final long serialVersionUID = -2935427570954647017L;
      
      final ObservableMergeWithCompletable.MergeWithObserver<?> parent;
      
      OtherObserver(ObservableMergeWithCompletable.MergeWithObserver<?> param2MergeWithObserver) {
        this.parent = param2MergeWithObserver;
      }
      
      public void onComplete() {
        this.parent.otherComplete();
      }
      
      public void onError(Throwable param2Throwable) {
        this.parent.otherError(param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.setOnce(this, param2Disposable);
      }
    }
  }
  
  static final class OtherObserver extends AtomicReference<Disposable> implements CompletableObserver {
    private static final long serialVersionUID = -2935427570954647017L;
    
    final ObservableMergeWithCompletable.MergeWithObserver<?> parent;
    
    OtherObserver(ObservableMergeWithCompletable.MergeWithObserver<?> param1MergeWithObserver) {
      this.parent = param1MergeWithObserver;
    }
    
    public void onComplete() {
      this.parent.otherComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.otherError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableMergeWithCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */