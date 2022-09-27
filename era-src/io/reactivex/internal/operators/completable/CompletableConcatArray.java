package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.SequentialDisposable;
import java.util.concurrent.atomic.AtomicInteger;

public final class CompletableConcatArray extends Completable {
  final CompletableSource[] sources;
  
  public CompletableConcatArray(CompletableSource[] paramArrayOfCompletableSource) {
    this.sources = paramArrayOfCompletableSource;
  }
  
  public void subscribeActual(CompletableObserver paramCompletableObserver) {
    ConcatInnerObserver concatInnerObserver = new ConcatInnerObserver(paramCompletableObserver, this.sources);
    paramCompletableObserver.onSubscribe((Disposable)concatInnerObserver.sd);
    concatInnerObserver.next();
  }
  
  static final class ConcatInnerObserver extends AtomicInteger implements CompletableObserver {
    private static final long serialVersionUID = -7965400327305809232L;
    
    final CompletableObserver downstream;
    
    int index;
    
    final SequentialDisposable sd;
    
    final CompletableSource[] sources;
    
    ConcatInnerObserver(CompletableObserver param1CompletableObserver, CompletableSource[] param1ArrayOfCompletableSource) {
      this.downstream = param1CompletableObserver;
      this.sources = param1ArrayOfCompletableSource;
      this.sd = new SequentialDisposable();
    }
    
    void next() {
      if (this.sd.isDisposed())
        return; 
      if (getAndIncrement() != 0)
        return; 
      CompletableSource[] arrayOfCompletableSource = this.sources;
      do {
        if (this.sd.isDisposed())
          return; 
        int i = this.index;
        this.index = i + 1;
        if (i == arrayOfCompletableSource.length) {
          this.downstream.onComplete();
          return;
        } 
        arrayOfCompletableSource[i].subscribe(this);
      } while (decrementAndGet() != 0);
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableConcatArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */