package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;

public final class SingleEquals<T> extends Single<Boolean> {
  final SingleSource<? extends T> first;
  
  final SingleSource<? extends T> second;
  
  public SingleEquals(SingleSource<? extends T> paramSingleSource1, SingleSource<? extends T> paramSingleSource2) {
    this.first = paramSingleSource1;
    this.second = paramSingleSource2;
  }
  
  protected void subscribeActual(SingleObserver<? super Boolean> paramSingleObserver) {
    AtomicInteger atomicInteger = new AtomicInteger();
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = null;
    arrayOfObject[1] = null;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    paramSingleObserver.onSubscribe((Disposable)compositeDisposable);
    this.first.subscribe(new InnerObserver(0, compositeDisposable, arrayOfObject, paramSingleObserver, atomicInteger));
    this.second.subscribe(new InnerObserver(1, compositeDisposable, arrayOfObject, paramSingleObserver, atomicInteger));
  }
  
  static class InnerObserver<T> implements SingleObserver<T> {
    final AtomicInteger count;
    
    final SingleObserver<? super Boolean> downstream;
    
    final int index;
    
    final CompositeDisposable set;
    
    final Object[] values;
    
    InnerObserver(int param1Int, CompositeDisposable param1CompositeDisposable, Object[] param1ArrayOfObject, SingleObserver<? super Boolean> param1SingleObserver, AtomicInteger param1AtomicInteger) {
      this.index = param1Int;
      this.set = param1CompositeDisposable;
      this.values = param1ArrayOfObject;
      this.downstream = param1SingleObserver;
      this.count = param1AtomicInteger;
    }
    
    public void onError(Throwable param1Throwable) {
      while (true) {
        int i = this.count.get();
        if (i >= 2) {
          RxJavaPlugins.onError(param1Throwable);
          return;
        } 
        if (this.count.compareAndSet(i, 2)) {
          this.set.dispose();
          this.downstream.onError(param1Throwable);
          return;
        } 
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.set.add(param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      this.values[this.index] = param1T;
      if (this.count.incrementAndGet() == 2) {
        SingleObserver<? super Boolean> singleObserver = this.downstream;
        Object[] arrayOfObject = this.values;
        singleObserver.onSuccess(Boolean.valueOf(ObjectHelper.equals(arrayOfObject[0], arrayOfObject[1])));
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleEquals.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */