package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeZipArray<T, R> extends Maybe<R> {
  final MaybeSource<? extends T>[] sources;
  
  final Function<? super Object[], ? extends R> zipper;
  
  public MaybeZipArray(MaybeSource<? extends T>[] paramArrayOfMaybeSource, Function<? super Object[], ? extends R> paramFunction) {
    this.sources = paramArrayOfMaybeSource;
    this.zipper = paramFunction;
  }
  
  protected void subscribeActual(MaybeObserver<? super R> paramMaybeObserver) {
    MaybeSource<? extends T>[] arrayOfMaybeSource = this.sources;
    int i = arrayOfMaybeSource.length;
    byte b = 0;
    if (i == 1) {
      arrayOfMaybeSource[0].subscribe(new MaybeMap.MapMaybeObserver<Object, R>(paramMaybeObserver, new SingletonArrayFunc()));
      return;
    } 
    ZipCoordinator<Object, R> zipCoordinator = new ZipCoordinator<Object, R>(paramMaybeObserver, i, this.zipper);
    paramMaybeObserver.onSubscribe(zipCoordinator);
    while (b < i) {
      if (zipCoordinator.isDisposed())
        return; 
      MaybeSource<? extends T> maybeSource = arrayOfMaybeSource[b];
      if (maybeSource == null) {
        zipCoordinator.innerError(new NullPointerException("One of the sources is null"), b);
        return;
      } 
      maybeSource.subscribe(zipCoordinator.observers[b]);
      b++;
    } 
  }
  
  final class SingletonArrayFunc implements Function<T, R> {
    public R apply(T param1T) throws Exception {
      return (R)ObjectHelper.requireNonNull(MaybeZipArray.this.zipper.apply(new Object[] { param1T }, ), "The zipper returned a null value");
    }
  }
  
  static final class ZipCoordinator<T, R> extends AtomicInteger implements Disposable {
    private static final long serialVersionUID = -5556924161382950569L;
    
    final MaybeObserver<? super R> downstream;
    
    final MaybeZipArray.ZipMaybeObserver<T>[] observers;
    
    final Object[] values;
    
    final Function<? super Object[], ? extends R> zipper;
    
    ZipCoordinator(MaybeObserver<? super R> param1MaybeObserver, int param1Int, Function<? super Object[], ? extends R> param1Function) {
      super(param1Int);
      this.downstream = param1MaybeObserver;
      this.zipper = param1Function;
      MaybeZipArray.ZipMaybeObserver[] arrayOfZipMaybeObserver = new MaybeZipArray.ZipMaybeObserver[param1Int];
      for (byte b = 0; b < param1Int; b++)
        arrayOfZipMaybeObserver[b] = new MaybeZipArray.ZipMaybeObserver(this, b); 
      this.observers = (MaybeZipArray.ZipMaybeObserver<T>[])arrayOfZipMaybeObserver;
      this.values = new Object[param1Int];
    }
    
    public void dispose() {
      byte b = 0;
      if (getAndSet(0) > 0) {
        MaybeZipArray.ZipMaybeObserver<T>[] arrayOfZipMaybeObserver = this.observers;
        int i = arrayOfZipMaybeObserver.length;
        while (b < i) {
          arrayOfZipMaybeObserver[b].dispose();
          b++;
        } 
      } 
    }
    
    void disposeExcept(int param1Int) {
      int j;
      MaybeZipArray.ZipMaybeObserver<T>[] arrayOfZipMaybeObserver = this.observers;
      int i = arrayOfZipMaybeObserver.length;
      byte b = 0;
      while (true) {
        j = param1Int;
        if (b < param1Int) {
          arrayOfZipMaybeObserver[b].dispose();
          b++;
          continue;
        } 
        break;
      } 
      while (++j < i)
        arrayOfZipMaybeObserver[j].dispose(); 
    }
    
    void innerComplete(int param1Int) {
      if (getAndSet(0) > 0) {
        disposeExcept(param1Int);
        this.downstream.onComplete();
      } 
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
  
  static final class ZipMaybeObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T> {
    private static final long serialVersionUID = 3323743579927613702L;
    
    final int index;
    
    final MaybeZipArray.ZipCoordinator<T, ?> parent;
    
    ZipMaybeObserver(MaybeZipArray.ZipCoordinator<T, ?> param1ZipCoordinator, int param1Int) {
      this.parent = param1ZipCoordinator;
      this.index = param1Int;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public void onComplete() {
      this.parent.innerComplete(this.index);
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeZipArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */