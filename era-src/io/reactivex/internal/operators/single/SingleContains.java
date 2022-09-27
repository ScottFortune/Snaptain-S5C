package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiPredicate;

public final class SingleContains<T> extends Single<Boolean> {
  final BiPredicate<Object, Object> comparer;
  
  final SingleSource<T> source;
  
  final Object value;
  
  public SingleContains(SingleSource<T> paramSingleSource, Object paramObject, BiPredicate<Object, Object> paramBiPredicate) {
    this.source = paramSingleSource;
    this.value = paramObject;
    this.comparer = paramBiPredicate;
  }
  
  protected void subscribeActual(SingleObserver<? super Boolean> paramSingleObserver) {
    this.source.subscribe(new ContainsSingleObserver(paramSingleObserver));
  }
  
  final class ContainsSingleObserver implements SingleObserver<T> {
    private final SingleObserver<? super Boolean> downstream;
    
    ContainsSingleObserver(SingleObserver<? super Boolean> param1SingleObserver) {
      this.downstream = param1SingleObserver;
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.downstream.onSubscribe(param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      try {
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        this.downstream.onError((Throwable)param1T);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleContains.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */