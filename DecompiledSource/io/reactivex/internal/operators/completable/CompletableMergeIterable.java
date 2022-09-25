package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public final class CompletableMergeIterable extends Completable {
  final Iterable<? extends CompletableSource> sources;
  
  public CompletableMergeIterable(Iterable<? extends CompletableSource> paramIterable) {
    this.sources = paramIterable;
  }
  
  public void subscribeActual(CompletableObserver paramCompletableObserver) {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    paramCompletableObserver.onSubscribe((Disposable)compositeDisposable);
    try {
      Iterator iterator = (Iterator)ObjectHelper.requireNonNull(this.sources.iterator(), "The source iterator returned is null");
      return;
    } finally {
      compositeDisposable = null;
      Exceptions.throwIfFatal((Throwable)compositeDisposable);
      paramCompletableObserver.onError((Throwable)compositeDisposable);
    } 
  }
  
  static final class MergeCompletableObserver extends AtomicBoolean implements CompletableObserver {
    private static final long serialVersionUID = -7730517613164279224L;
    
    final CompletableObserver downstream;
    
    final CompositeDisposable set;
    
    final AtomicInteger wip;
    
    MergeCompletableObserver(CompletableObserver param1CompletableObserver, CompositeDisposable param1CompositeDisposable, AtomicInteger param1AtomicInteger) {
      this.downstream = param1CompletableObserver;
      this.set = param1CompositeDisposable;
      this.wip = param1AtomicInteger;
    }
    
    public void onComplete() {
      if (this.wip.decrementAndGet() == 0 && compareAndSet(false, true))
        this.downstream.onComplete(); 
    }
    
    public void onError(Throwable param1Throwable) {
      this.set.dispose();
      if (compareAndSet(false, true)) {
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableMergeIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */