package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public final class SingleAmb<T> extends Single<T> {
  private final SingleSource<? extends T>[] sources;
  
  private final Iterable<? extends SingleSource<? extends T>> sourcesIterable;
  
  public SingleAmb(SingleSource<? extends T>[] paramArrayOfSingleSource, Iterable<? extends SingleSource<? extends T>> paramIterable) {
    this.sources = paramArrayOfSingleSource;
    this.sourcesIterable = paramIterable;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    SingleSource[] arrayOfSingleSource;
    int i;
    SingleSource<? extends T>[] arrayOfSingleSource1 = this.sources;
    if (arrayOfSingleSource1 == null) {
      SingleSource[] arrayOfSingleSource2 = new SingleSource[8];
      try {
        Iterator<? extends SingleSource<? extends T>> iterator = this.sourcesIterable.iterator();
        byte b1 = 0;
        while (true) {
          arrayOfSingleSource = arrayOfSingleSource2;
          i = b1;
          if (iterator.hasNext()) {
            NullPointerException nullPointerException2;
            SingleSource singleSource = iterator.next();
            if (singleSource == null)
              return; 
            NullPointerException nullPointerException1 = nullPointerException2;
            if (b1 == nullPointerException2.length) {
              arrayOfSingleSource = new SingleSource[(b1 >> 2) + b1];
              System.arraycopy(nullPointerException2, 0, arrayOfSingleSource, 0, b1);
            } 
            arrayOfSingleSource[b1] = singleSource;
            b1++;
            SingleSource[] arrayOfSingleSource3 = arrayOfSingleSource;
            continue;
          } 
          break;
        } 
      } finally {
        arrayOfSingleSource2 = null;
        Exceptions.throwIfFatal((Throwable)arrayOfSingleSource2);
        EmptyDisposable.error((Throwable)arrayOfSingleSource2, paramSingleObserver);
      } 
    } else {
      i = arrayOfSingleSource.length;
    } 
    AtomicBoolean atomicBoolean = new AtomicBoolean();
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    paramSingleObserver.onSubscribe((Disposable)compositeDisposable);
    for (byte b = 0; b < i; b++) {
      SingleSource singleSource = arrayOfSingleSource[b];
      if (compositeDisposable.isDisposed())
        return; 
      if (singleSource == null) {
        compositeDisposable.dispose();
        NullPointerException nullPointerException = new NullPointerException("One of the sources is null");
        if (atomicBoolean.compareAndSet(false, true)) {
          paramSingleObserver.onError(nullPointerException);
        } else {
          RxJavaPlugins.onError(nullPointerException);
        } 
        return;
      } 
      singleSource.subscribe(new AmbSingleObserver<T>(paramSingleObserver, compositeDisposable, atomicBoolean));
    } 
  }
  
  static final class AmbSingleObserver<T> implements SingleObserver<T> {
    final SingleObserver<? super T> downstream;
    
    final CompositeDisposable set;
    
    Disposable upstream;
    
    final AtomicBoolean winner;
    
    AmbSingleObserver(SingleObserver<? super T> param1SingleObserver, CompositeDisposable param1CompositeDisposable, AtomicBoolean param1AtomicBoolean) {
      this.downstream = param1SingleObserver;
      this.set = param1CompositeDisposable;
      this.winner = param1AtomicBoolean;
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.winner.compareAndSet(false, true)) {
        this.set.delete(this.upstream);
        this.set.dispose();
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.upstream = param1Disposable;
      this.set.add(param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      if (this.winner.compareAndSet(false, true)) {
        this.set.delete(this.upstream);
        this.set.dispose();
        this.downstream.onSuccess(param1T);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleAmb.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */