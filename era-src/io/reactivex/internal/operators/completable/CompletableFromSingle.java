package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;

public final class CompletableFromSingle<T> extends Completable {
  final SingleSource<T> single;
  
  public CompletableFromSingle(SingleSource<T> paramSingleSource) {
    this.single = paramSingleSource;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    this.single.subscribe(new CompletableFromSingleObserver(paramCompletableObserver));
  }
  
  static final class CompletableFromSingleObserver<T> implements SingleObserver<T> {
    final CompletableObserver co;
    
    CompletableFromSingleObserver(CompletableObserver param1CompletableObserver) {
      this.co = param1CompletableObserver;
    }
    
    public void onError(Throwable param1Throwable) {
      this.co.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.co.onSubscribe(param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      this.co.onComplete();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableFromSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */