package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

public final class CompletableDetach extends Completable {
  final CompletableSource source;
  
  public CompletableDetach(CompletableSource paramCompletableSource) {
    this.source = paramCompletableSource;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    this.source.subscribe(new DetachCompletableObserver(paramCompletableObserver));
  }
  
  static final class DetachCompletableObserver implements CompletableObserver, Disposable {
    CompletableObserver downstream;
    
    Disposable upstream;
    
    DetachCompletableObserver(CompletableObserver param1CompletableObserver) {
      this.downstream = param1CompletableObserver;
    }
    
    public void dispose() {
      this.downstream = null;
      this.upstream.dispose();
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      CompletableObserver completableObserver = this.downstream;
      if (completableObserver != null) {
        this.downstream = null;
        completableObserver.onComplete();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      CompletableObserver completableObserver = this.downstream;
      if (completableObserver != null) {
        this.downstream = null;
        completableObserver.onError(param1Throwable);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableDetach.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */