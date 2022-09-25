package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public final class CompletableMergeArray extends Completable {
  final CompletableSource[] sources;
  
  public CompletableMergeArray(CompletableSource[] paramArrayOfCompletableSource) {
    this.sources = paramArrayOfCompletableSource;
  }
  
  public void subscribeActual(CompletableObserver paramCompletableObserver) {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    InnerCompletableObserver innerCompletableObserver = new InnerCompletableObserver(paramCompletableObserver, new AtomicBoolean(), compositeDisposable, this.sources.length + 1);
    paramCompletableObserver.onSubscribe((Disposable)compositeDisposable);
    for (CompletableSource completableSource : this.sources) {
      if (compositeDisposable.isDisposed())
        return; 
      if (completableSource == null) {
        compositeDisposable.dispose();
        innerCompletableObserver.onError(new NullPointerException("A completable source is null"));
        return;
      } 
      completableSource.subscribe(innerCompletableObserver);
    } 
    innerCompletableObserver.onComplete();
  }
  
  static final class InnerCompletableObserver extends AtomicInteger implements CompletableObserver {
    private static final long serialVersionUID = -8360547806504310570L;
    
    final CompletableObserver downstream;
    
    final AtomicBoolean once;
    
    final CompositeDisposable set;
    
    InnerCompletableObserver(CompletableObserver param1CompletableObserver, AtomicBoolean param1AtomicBoolean, CompositeDisposable param1CompositeDisposable, int param1Int) {
      this.downstream = param1CompletableObserver;
      this.once = param1AtomicBoolean;
      this.set = param1CompositeDisposable;
      lazySet(param1Int);
    }
    
    public void onComplete() {
      if (decrementAndGet() == 0 && this.once.compareAndSet(false, true))
        this.downstream.onComplete(); 
    }
    
    public void onError(Throwable param1Throwable) {
      this.set.dispose();
      if (this.once.compareAndSet(false, true)) {
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.set.add(param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableMergeArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */