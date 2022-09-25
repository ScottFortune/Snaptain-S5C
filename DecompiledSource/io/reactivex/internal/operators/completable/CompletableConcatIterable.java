package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public final class CompletableConcatIterable extends Completable {
  final Iterable<? extends CompletableSource> sources;
  
  public CompletableConcatIterable(Iterable<? extends CompletableSource> paramIterable) {
    this.sources = paramIterable;
  }
  
  public void subscribeActual(CompletableObserver paramCompletableObserver) {
    try {
      Iterator<? extends CompletableSource> iterator = (Iterator)ObjectHelper.requireNonNull(this.sources.iterator(), "The iterator returned is null");
      return;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptyDisposable.error(exception, paramCompletableObserver);
    } 
  }
  
  static final class ConcatInnerObserver extends AtomicInteger implements CompletableObserver {
    private static final long serialVersionUID = -7965400327305809232L;
    
    final CompletableObserver downstream;
    
    final SequentialDisposable sd;
    
    final Iterator<? extends CompletableSource> sources;
    
    ConcatInnerObserver(CompletableObserver param1CompletableObserver, Iterator<? extends CompletableSource> param1Iterator) {
      this.downstream = param1CompletableObserver;
      this.sources = param1Iterator;
      this.sd = new SequentialDisposable();
    }
    
    void next() {
      if (this.sd.isDisposed())
        return; 
      if (getAndIncrement() != 0)
        return; 
      Iterator<? extends CompletableSource> iterator = this.sources;
      while (true) {
        if (this.sd.isDisposed())
          return; 
        try {
        
        } finally {
          iterator = null;
          Exceptions.throwIfFatal((Throwable)iterator);
          this.downstream.onError((Throwable)iterator);
        } 
      } 
    }
    
    public void onComplete() {
      next();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.sd.replace(param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableConcatIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */