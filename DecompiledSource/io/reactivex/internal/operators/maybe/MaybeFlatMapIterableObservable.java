package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.observers.BasicQueueDisposable;
import java.util.Iterator;

public final class MaybeFlatMapIterableObservable<T, R> extends Observable<R> {
  final Function<? super T, ? extends Iterable<? extends R>> mapper;
  
  final MaybeSource<T> source;
  
  public MaybeFlatMapIterableObservable(MaybeSource<T> paramMaybeSource, Function<? super T, ? extends Iterable<? extends R>> paramFunction) {
    this.source = paramMaybeSource;
    this.mapper = paramFunction;
  }
  
  protected void subscribeActual(Observer<? super R> paramObserver) {
    this.source.subscribe(new FlatMapIterableObserver<T, R>(paramObserver, this.mapper));
  }
  
  static final class FlatMapIterableObserver<T, R> extends BasicQueueDisposable<R> implements MaybeObserver<T> {
    volatile boolean cancelled;
    
    final Observer<? super R> downstream;
    
    volatile Iterator<? extends R> it;
    
    final Function<? super T, ? extends Iterable<? extends R>> mapper;
    
    boolean outputFused;
    
    Disposable upstream;
    
    FlatMapIterableObserver(Observer<? super R> param1Observer, Function<? super T, ? extends Iterable<? extends R>> param1Function) {
      this.downstream = param1Observer;
      this.mapper = param1Function;
    }
    
    public void clear() {
      this.it = null;
    }
    
    public void dispose() {
      this.cancelled = true;
      this.upstream.dispose();
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
    }
    
    public boolean isDisposed() {
      return this.cancelled;
    }
    
    public boolean isEmpty() {
      boolean bool;
      if (this.it == null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      this.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe((Disposable)this);
      } 
    }
    
    public void onSuccess(T param1T) {
      Observer<? super R> observer = this.downstream;
      try {
        Iterator<? extends R> iterator = ((Iterable)this.mapper.apply(param1T)).iterator();
        boolean bool = iterator.hasNext();
        if (!bool)
          return; 
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        observer.onError((Throwable)param1T);
      } 
    }
    
    public R poll() throws Exception {
      Iterator<? extends R> iterator = this.it;
      if (iterator != null) {
        Object object = ObjectHelper.requireNonNull(iterator.next(), "The iterator returned a null value");
        if (!iterator.hasNext())
          this.it = null; 
        return (R)object;
      } 
      return null;
    }
    
    public int requestFusion(int param1Int) {
      if ((param1Int & 0x2) != 0) {
        this.outputFused = true;
        return 2;
      } 
      return 0;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeFlatMapIterableObservable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */