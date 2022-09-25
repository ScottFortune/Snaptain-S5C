package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class CompletableAndThenCompletable extends Completable {
  final CompletableSource next;
  
  final CompletableSource source;
  
  public CompletableAndThenCompletable(CompletableSource paramCompletableSource1, CompletableSource paramCompletableSource2) {
    this.source = paramCompletableSource1;
    this.next = paramCompletableSource2;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    this.source.subscribe(new SourceObserver(paramCompletableObserver, this.next));
  }
  
  static final class NextObserver implements CompletableObserver {
    final CompletableObserver downstream;
    
    final AtomicReference<Disposable> parent;
    
    NextObserver(AtomicReference<Disposable> param1AtomicReference, CompletableObserver param1CompletableObserver) {
      this.parent = param1AtomicReference;
      this.downstream = param1CompletableObserver;
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.replace(this.parent, param1Disposable);
    }
  }
  
  static final class SourceObserver extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
    private static final long serialVersionUID = -4101678820158072998L;
    
    final CompletableObserver actualObserver;
    
    final CompletableSource next;
    
    SourceObserver(CompletableObserver param1CompletableObserver, CompletableSource param1CompletableSource) {
      this.actualObserver = param1CompletableObserver;
      this.next = param1CompletableSource;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      this.next.subscribe(new CompletableAndThenCompletable.NextObserver(this, this.actualObserver));
    }
    
    public void onError(Throwable param1Throwable) {
      this.actualObserver.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.setOnce(this, param1Disposable))
        this.actualObserver.onSubscribe(this); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableAndThenCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */