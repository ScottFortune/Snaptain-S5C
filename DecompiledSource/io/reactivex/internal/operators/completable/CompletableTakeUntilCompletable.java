package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class CompletableTakeUntilCompletable extends Completable {
  final CompletableSource other;
  
  final Completable source;
  
  public CompletableTakeUntilCompletable(Completable paramCompletable, CompletableSource paramCompletableSource) {
    this.source = paramCompletable;
    this.other = paramCompletableSource;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    TakeUntilMainObserver takeUntilMainObserver = new TakeUntilMainObserver(paramCompletableObserver);
    paramCompletableObserver.onSubscribe(takeUntilMainObserver);
    this.other.subscribe(takeUntilMainObserver.other);
    this.source.subscribe(takeUntilMainObserver);
  }
  
  static final class TakeUntilMainObserver extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
    private static final long serialVersionUID = 3533011714830024923L;
    
    final CompletableObserver downstream;
    
    final AtomicBoolean once;
    
    final OtherObserver other;
    
    TakeUntilMainObserver(CompletableObserver param1CompletableObserver) {
      this.downstream = param1CompletableObserver;
      this.other = new OtherObserver(this);
      this.once = new AtomicBoolean();
    }
    
    public void dispose() {
      if (this.once.compareAndSet(false, true)) {
        DisposableHelper.dispose(this);
        DisposableHelper.dispose(this.other);
      } 
    }
    
    void innerComplete() {
      if (this.once.compareAndSet(false, true)) {
        DisposableHelper.dispose(this);
        this.downstream.onComplete();
      } 
    }
    
    void innerError(Throwable param1Throwable) {
      if (this.once.compareAndSet(false, true)) {
        DisposableHelper.dispose(this);
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public boolean isDisposed() {
      return this.once.get();
    }
    
    public void onComplete() {
      if (this.once.compareAndSet(false, true)) {
        DisposableHelper.dispose(this.other);
        this.downstream.onComplete();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.once.compareAndSet(false, true)) {
        DisposableHelper.dispose(this.other);
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
    
    static final class OtherObserver extends AtomicReference<Disposable> implements CompletableObserver {
      private static final long serialVersionUID = 5176264485428790318L;
      
      final CompletableTakeUntilCompletable.TakeUntilMainObserver parent;
      
      OtherObserver(CompletableTakeUntilCompletable.TakeUntilMainObserver param2TakeUntilMainObserver) {
        this.parent = param2TakeUntilMainObserver;
      }
      
      public void onComplete() {
        this.parent.innerComplete();
      }
      
      public void onError(Throwable param2Throwable) {
        this.parent.innerError(param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.setOnce(this, param2Disposable);
      }
    }
  }
  
  static final class OtherObserver extends AtomicReference<Disposable> implements CompletableObserver {
    private static final long serialVersionUID = 5176264485428790318L;
    
    final CompletableTakeUntilCompletable.TakeUntilMainObserver parent;
    
    OtherObserver(CompletableTakeUntilCompletable.TakeUntilMainObserver param1TakeUntilMainObserver) {
      this.parent = param1TakeUntilMainObserver;
    }
    
    public void onComplete() {
      this.parent.innerComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.innerError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableTakeUntilCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */