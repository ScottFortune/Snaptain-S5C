package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class CompletableResumeNext extends Completable {
  final Function<? super Throwable, ? extends CompletableSource> errorMapper;
  
  final CompletableSource source;
  
  public CompletableResumeNext(CompletableSource paramCompletableSource, Function<? super Throwable, ? extends CompletableSource> paramFunction) {
    this.source = paramCompletableSource;
    this.errorMapper = paramFunction;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    ResumeNextObserver resumeNextObserver = new ResumeNextObserver(paramCompletableObserver, this.errorMapper);
    paramCompletableObserver.onSubscribe(resumeNextObserver);
    this.source.subscribe(resumeNextObserver);
  }
  
  static final class ResumeNextObserver extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
    private static final long serialVersionUID = 5018523762564524046L;
    
    final CompletableObserver downstream;
    
    final Function<? super Throwable, ? extends CompletableSource> errorMapper;
    
    boolean once;
    
    ResumeNextObserver(CompletableObserver param1CompletableObserver, Function<? super Throwable, ? extends CompletableSource> param1Function) {
      this.downstream = param1CompletableObserver;
      this.errorMapper = param1Function;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.once) {
        this.downstream.onError(param1Throwable);
        return;
      } 
      this.once = true;
      try {
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        this.downstream.onError((Throwable)new CompositeException(new Throwable[] { param1Throwable, exception }));
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.replace(this, param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableResumeNext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */