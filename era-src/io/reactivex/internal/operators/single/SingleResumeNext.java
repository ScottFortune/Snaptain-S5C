package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleResumeNext<T> extends Single<T> {
  final Function<? super Throwable, ? extends SingleSource<? extends T>> nextFunction;
  
  final SingleSource<? extends T> source;
  
  public SingleResumeNext(SingleSource<? extends T> paramSingleSource, Function<? super Throwable, ? extends SingleSource<? extends T>> paramFunction) {
    this.source = paramSingleSource;
    this.nextFunction = paramFunction;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    this.source.subscribe(new ResumeMainSingleObserver<T>(paramSingleObserver, this.nextFunction));
  }
  
  static final class ResumeMainSingleObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T>, Disposable {
    private static final long serialVersionUID = -5314538511045349925L;
    
    final SingleObserver<? super T> downstream;
    
    final Function<? super Throwable, ? extends SingleSource<? extends T>> nextFunction;
    
    ResumeMainSingleObserver(SingleObserver<? super T> param1SingleObserver, Function<? super Throwable, ? extends SingleSource<? extends T>> param1Function) {
      this.downstream = param1SingleObserver;
      this.nextFunction = param1Function;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onError(Throwable param1Throwable) {
      try {
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        this.downstream.onError((Throwable)new CompositeException(new Throwable[] { param1Throwable, exception }));
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.setOnce(this, param1Disposable))
        this.downstream.onSubscribe(this); 
    }
    
    public void onSuccess(T param1T) {
      this.downstream.onSuccess(param1T);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleResumeNext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */