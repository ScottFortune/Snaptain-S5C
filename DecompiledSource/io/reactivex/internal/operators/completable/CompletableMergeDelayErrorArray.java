package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;

public final class CompletableMergeDelayErrorArray extends Completable {
  final CompletableSource[] sources;
  
  public CompletableMergeDelayErrorArray(CompletableSource[] paramArrayOfCompletableSource) {
    this.sources = paramArrayOfCompletableSource;
  }
  
  public void subscribeActual(CompletableObserver paramCompletableObserver) {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    AtomicInteger atomicInteger = new AtomicInteger(this.sources.length + 1);
    AtomicThrowable atomicThrowable = new AtomicThrowable();
    paramCompletableObserver.onSubscribe((Disposable)compositeDisposable);
    for (CompletableSource completableSource : this.sources) {
      if (compositeDisposable.isDisposed())
        return; 
      if (completableSource == null) {
        atomicThrowable.addThrowable(new NullPointerException("A completable source is null"));
        atomicInteger.decrementAndGet();
      } else {
        completableSource.subscribe(new MergeInnerCompletableObserver(paramCompletableObserver, compositeDisposable, atomicThrowable, atomicInteger));
      } 
    } 
    if (atomicInteger.decrementAndGet() == 0) {
      Throwable throwable = atomicThrowable.terminate();
      if (throwable == null) {
        paramCompletableObserver.onComplete();
      } else {
        paramCompletableObserver.onError(throwable);
      } 
    } 
  }
  
  static final class MergeInnerCompletableObserver implements CompletableObserver {
    final CompletableObserver downstream;
    
    final AtomicThrowable error;
    
    final CompositeDisposable set;
    
    final AtomicInteger wip;
    
    MergeInnerCompletableObserver(CompletableObserver param1CompletableObserver, CompositeDisposable param1CompositeDisposable, AtomicThrowable param1AtomicThrowable, AtomicInteger param1AtomicInteger) {
      this.downstream = param1CompletableObserver;
      this.set = param1CompositeDisposable;
      this.error = param1AtomicThrowable;
      this.wip = param1AtomicInteger;
    }
    
    public void onComplete() {
      tryTerminate();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.error.addThrowable(param1Throwable)) {
        tryTerminate();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.set.add(param1Disposable);
    }
    
    void tryTerminate() {
      if (this.wip.decrementAndGet() == 0) {
        Throwable throwable = this.error.terminate();
        if (throwable == null) {
          this.downstream.onComplete();
        } else {
          this.downstream.onError(throwable);
        } 
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableMergeDelayErrorArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */