package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ObservableBuffer<T, U extends Collection<? super T>> extends AbstractObservableWithUpstream<T, U> {
  final Callable<U> bufferSupplier;
  
  final int count;
  
  final int skip;
  
  public ObservableBuffer(ObservableSource<T> paramObservableSource, int paramInt1, int paramInt2, Callable<U> paramCallable) {
    super(paramObservableSource);
    this.count = paramInt1;
    this.skip = paramInt2;
    this.bufferSupplier = paramCallable;
  }
  
  protected void subscribeActual(Observer<? super U> paramObserver) {
    int i = this.skip;
    int j = this.count;
    if (i == j) {
      paramObserver = new BufferExactObserver<U, U>(paramObserver, j, this.bufferSupplier);
      if (paramObserver.createBuffer())
        this.source.subscribe(paramObserver); 
    } else {
      this.source.subscribe(new BufferSkipObserver<Object, U>(paramObserver, this.count, this.skip, this.bufferSupplier));
    } 
  }
  
  static final class BufferExactObserver<T, U extends Collection<? super T>> implements Observer<T>, Disposable {
    U buffer;
    
    final Callable<U> bufferSupplier;
    
    final int count;
    
    final Observer<? super U> downstream;
    
    int size;
    
    Disposable upstream;
    
    BufferExactObserver(Observer<? super U> param1Observer, int param1Int, Callable<U> param1Callable) {
      this.downstream = param1Observer;
      this.count = param1Int;
      this.bufferSupplier = param1Callable;
    }
    
    boolean createBuffer() {
      try {
        return true;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        this.buffer = null;
        Disposable disposable = this.upstream;
        if (disposable == null) {
          EmptyDisposable.error(exception, this.downstream);
        } else {
          disposable.dispose();
          this.downstream.onError(exception);
        } 
      } 
    }
    
    public void dispose() {
      this.upstream.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      U u = this.buffer;
      if (u != null) {
        this.buffer = null;
        if (!u.isEmpty())
          this.downstream.onNext(u); 
        this.downstream.onComplete();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      this.buffer = null;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      U u = this.buffer;
      if (u != null) {
        u.add(param1T);
        int i = this.size + 1;
        this.size = i;
        if (i >= this.count) {
          this.downstream.onNext(u);
          this.size = 0;
          createBuffer();
        } 
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
  
  static final class BufferSkipObserver<T, U extends Collection<? super T>> extends AtomicBoolean implements Observer<T>, Disposable {
    private static final long serialVersionUID = -8223395059921494546L;
    
    final Callable<U> bufferSupplier;
    
    final ArrayDeque<U> buffers;
    
    final int count;
    
    final Observer<? super U> downstream;
    
    long index;
    
    final int skip;
    
    Disposable upstream;
    
    BufferSkipObserver(Observer<? super U> param1Observer, int param1Int1, int param1Int2, Callable<U> param1Callable) {
      this.downstream = param1Observer;
      this.count = param1Int1;
      this.skip = param1Int2;
      this.bufferSupplier = param1Callable;
      this.buffers = new ArrayDeque<U>();
    }
    
    public void dispose() {
      this.upstream.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      while (!this.buffers.isEmpty())
        this.downstream.onNext(this.buffers.poll()); 
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.buffers.clear();
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      long l = this.index;
      this.index = 1L + l;
      if (l % this.skip == 0L)
        try {
        
        } finally {
          param1T = null;
          this.buffers.clear();
          this.upstream.dispose();
          this.downstream.onError((Throwable)param1T);
        }  
      Iterator<U> iterator = this.buffers.iterator();
      while (iterator.hasNext()) {
        Collection<T> collection = (Collection)iterator.next();
        collection.add(param1T);
        if (this.count <= collection.size()) {
          iterator.remove();
          this.downstream.onNext(collection);
        } 
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */