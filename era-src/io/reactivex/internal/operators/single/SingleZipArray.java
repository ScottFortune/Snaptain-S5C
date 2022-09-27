package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleZipArray<T, R> extends Single<R> {
  final SingleSource<? extends T>[] sources;
  
  final Function<? super Object[], ? extends R> zipper;
  
  public SingleZipArray(SingleSource<? extends T>[] paramArrayOfSingleSource, Function<? super Object[], ? extends R> paramFunction) {
    this.sources = paramArrayOfSingleSource;
    this.zipper = paramFunction;
  }
  
  protected void subscribeActual(SingleObserver<? super R> paramSingleObserver) {
    SingleSource<? extends T>[] arrayOfSingleSource = this.sources;
    int i = arrayOfSingleSource.length;
    byte b = 0;
    if (i == 1) {
      arrayOfSingleSource[0].subscribe(new SingleMap.MapSingleObserver<Object, R>(paramSingleObserver, new SingletonArrayFunc()));
      return;
    } 
    ZipCoordinator<Object, R> zipCoordinator = new ZipCoordinator<Object, R>(paramSingleObserver, i, this.zipper);
    paramSingleObserver.onSubscribe(zipCoordinator);
    while (b < i) {
      if (zipCoordinator.isDisposed())
        return; 
      SingleSource<? extends T> singleSource = arrayOfSingleSource[b];
      if (singleSource == null) {
        zipCoordinator.innerError(new NullPointerException("One of the sources is null"), b);
        return;
      } 
      singleSource.subscribe(zipCoordinator.observers[b]);
      b++;
    } 
  }
  
  final class SingletonArrayFunc implements Function<T, R> {
    public R apply(T param1T) throws Exception {
      return (R)ObjectHelper.requireNonNull(SingleZipArray.this.zipper.apply(new Object[] { param1T }, ), "The zipper returned a null value");
    }
  }
  
  static final class ZipCoordinator<T, R> extends AtomicInteger implements Disposable {
    private static final long serialVersionUID = -5556924161382950569L;
    
    final SingleObserver<? super R> downstream;
    
    final SingleZipArray.ZipSingleObserver<T>[] observers;
    
    final Object[] values;
    
    final Function<? super Object[], ? extends R> zipper;
    
    ZipCoordinator(SingleObserver<? super R> param1SingleObserver, int param1Int, Function<? super Object[], ? extends R> param1Function) {
      super(param1Int);
      this.downstream = param1SingleObserver;
      this.zipper = param1Function;
      SingleZipArray.ZipSingleObserver[] arrayOfZipSingleObserver = new SingleZipArray.ZipSingleObserver[param1Int];
      for (byte b = 0; b < param1Int; b++)
        arrayOfZipSingleObserver[b] = new SingleZipArray.ZipSingleObserver(this, b); 
      this.observers = (SingleZipArray.ZipSingleObserver<T>[])arrayOfZipSingleObserver;
      this.values = new Object[param1Int];
    }
    
    public void dispose() {
      byte b = 0;
      if (getAndSet(0) > 0) {
        SingleZipArray.ZipSingleObserver<T>[] arrayOfZipSingleObserver = this.observers;
        int i = arrayOfZipSingleObserver.length;
        while (b < i) {
          arrayOfZipSingleObserver[b].dispose();
          b++;
        } 
      } 
    }
    
    void disposeExcept(int param1Int) {
      int j;
      SingleZipArray.ZipSingleObserver<T>[] arrayOfZipSingleObserver = this.observers;
      int i = arrayOfZipSingleObserver.length;
      byte b = 0;
      while (true) {
        j = param1Int;
        if (b < param1Int) {
          arrayOfZipSingleObserver[b].dispose();
          b++;
          continue;
        } 
        break;
      } 
      while (++j < i)
        arrayOfZipSingleObserver[j].dispose(); 
    }
    
    void innerError(Throwable param1Throwable, int param1Int) {
      if (getAndSet(0) > 0) {
        disposeExcept(param1Int);
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    void innerSuccess(T param1T, int param1Int) {
      this.values[param1Int] = param1T;
      if (decrementAndGet() == 0)
        try {
        
        } finally {
          param1T = null;
          Exceptions.throwIfFatal((Throwable)param1T);
        }  
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (get() <= 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
  
  static final class ZipSingleObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T> {
    private static final long serialVersionUID = 3323743579927613702L;
    
    final int index;
    
    final SingleZipArray.ZipCoordinator<T, ?> parent;
    
    ZipSingleObserver(SingleZipArray.ZipCoordinator<T, ?> param1ZipCoordinator, int param1Int) {
      this.parent = param1ZipCoordinator;
      this.index = param1Int;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.innerError(param1Throwable, this.index);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      this.parent.innerSuccess(param1T, this.index);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleZipArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */