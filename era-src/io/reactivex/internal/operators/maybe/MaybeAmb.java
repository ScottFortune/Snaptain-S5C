package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public final class MaybeAmb<T> extends Maybe<T> {
  private final MaybeSource<? extends T>[] sources;
  
  private final Iterable<? extends MaybeSource<? extends T>> sourcesIterable;
  
  public MaybeAmb(MaybeSource<? extends T>[] paramArrayOfMaybeSource, Iterable<? extends MaybeSource<? extends T>> paramIterable) {
    this.sources = paramArrayOfMaybeSource;
    this.sourcesIterable = paramIterable;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    MaybeSource[] arrayOfMaybeSource;
    int i;
    MaybeSource<? extends T>[] arrayOfMaybeSource1 = this.sources;
    if (arrayOfMaybeSource1 == null) {
      MaybeSource[] arrayOfMaybeSource2 = new MaybeSource[8];
      try {
        Iterator<? extends MaybeSource<? extends T>> iterator = this.sourcesIterable.iterator();
        byte b1 = 0;
        while (true) {
          arrayOfMaybeSource = arrayOfMaybeSource2;
          i = b1;
          if (iterator.hasNext()) {
            NullPointerException nullPointerException2;
            MaybeSource maybeSource = iterator.next();
            if (maybeSource == null)
              return; 
            NullPointerException nullPointerException1 = nullPointerException2;
            if (b1 == nullPointerException2.length) {
              arrayOfMaybeSource = new MaybeSource[(b1 >> 2) + b1];
              System.arraycopy(nullPointerException2, 0, arrayOfMaybeSource, 0, b1);
            } 
            arrayOfMaybeSource[b1] = maybeSource;
            b1++;
            MaybeSource[] arrayOfMaybeSource3 = arrayOfMaybeSource;
            continue;
          } 
          break;
        } 
      } finally {
        arrayOfMaybeSource2 = null;
        Exceptions.throwIfFatal((Throwable)arrayOfMaybeSource2);
        EmptyDisposable.error((Throwable)arrayOfMaybeSource2, paramMaybeObserver);
      } 
    } else {
      i = arrayOfMaybeSource.length;
    } 
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    paramMaybeObserver.onSubscribe((Disposable)compositeDisposable);
    AtomicBoolean atomicBoolean = new AtomicBoolean();
    for (byte b = 0; b < i; b++) {
      MaybeSource maybeSource = arrayOfMaybeSource[b];
      if (compositeDisposable.isDisposed())
        return; 
      if (maybeSource == null) {
        compositeDisposable.dispose();
        NullPointerException nullPointerException = new NullPointerException("One of the MaybeSources is null");
        if (atomicBoolean.compareAndSet(false, true)) {
          paramMaybeObserver.onError(nullPointerException);
        } else {
          RxJavaPlugins.onError(nullPointerException);
        } 
        return;
      } 
      maybeSource.subscribe(new AmbMaybeObserver<T>(paramMaybeObserver, compositeDisposable, atomicBoolean));
    } 
    if (i == 0)
      paramMaybeObserver.onComplete(); 
  }
  
  static final class AmbMaybeObserver<T> implements MaybeObserver<T> {
    final MaybeObserver<? super T> downstream;
    
    final CompositeDisposable set;
    
    Disposable upstream;
    
    final AtomicBoolean winner;
    
    AmbMaybeObserver(MaybeObserver<? super T> param1MaybeObserver, CompositeDisposable param1CompositeDisposable, AtomicBoolean param1AtomicBoolean) {
      this.downstream = param1MaybeObserver;
      this.set = param1CompositeDisposable;
      this.winner = param1AtomicBoolean;
    }
    
    public void onComplete() {
      if (this.winner.compareAndSet(false, true)) {
        this.set.delete(this.upstream);
        this.set.dispose();
        this.downstream.onComplete();
      } 
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeAmb.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */