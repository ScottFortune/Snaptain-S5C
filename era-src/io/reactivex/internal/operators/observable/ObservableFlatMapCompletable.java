package io.reactivex.internal.operators.observable;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.observers.BasicIntQueueDisposable;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableFlatMapCompletable<T> extends AbstractObservableWithUpstream<T, T> {
  final boolean delayErrors;
  
  final Function<? super T, ? extends CompletableSource> mapper;
  
  public ObservableFlatMapCompletable(ObservableSource<T> paramObservableSource, Function<? super T, ? extends CompletableSource> paramFunction, boolean paramBoolean) {
    super(paramObservableSource);
    this.mapper = paramFunction;
    this.delayErrors = paramBoolean;
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new FlatMapCompletableMainObserver<T>(paramObserver, this.mapper, this.delayErrors));
  }
  
  static final class FlatMapCompletableMainObserver<T> extends BasicIntQueueDisposable<T> implements Observer<T> {
    private static final long serialVersionUID = 8443155186132538303L;
    
    final boolean delayErrors;
    
    volatile boolean disposed;
    
    final Observer<? super T> downstream;
    
    final AtomicThrowable errors;
    
    final Function<? super T, ? extends CompletableSource> mapper;
    
    final CompositeDisposable set;
    
    Disposable upstream;
    
    FlatMapCompletableMainObserver(Observer<? super T> param1Observer, Function<? super T, ? extends CompletableSource> param1Function, boolean param1Boolean) {
      this.downstream = param1Observer;
      this.mapper = param1Function;
      this.delayErrors = param1Boolean;
      this.errors = new AtomicThrowable();
      this.set = new CompositeDisposable();
      lazySet(1);
    }
    
    public void clear() {}
    
    public void dispose() {
      this.disposed = true;
      this.upstream.dispose();
      this.set.dispose();
    }
    
    void innerComplete(InnerObserver param1InnerObserver) {
      this.set.delete(param1InnerObserver);
      onComplete();
    }
    
    void innerError(InnerObserver param1InnerObserver, Throwable param1Throwable) {
      this.set.delete(param1InnerObserver);
      onError(param1Throwable);
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public boolean isEmpty() {
      return true;
    }
    
    public void onComplete() {
      if (decrementAndGet() == 0) {
        Throwable throwable = this.errors.terminate();
        if (throwable != null) {
          this.downstream.onError(throwable);
        } else {
          this.downstream.onComplete();
        } 
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.errors.addThrowable(param1Throwable)) {
        if (this.delayErrors) {
          if (decrementAndGet() == 0) {
            param1Throwable = this.errors.terminate();
            this.downstream.onError(param1Throwable);
          } 
        } else {
          dispose();
          if (getAndSet(0) > 0) {
            param1Throwable = this.errors.terminate();
            this.downstream.onError(param1Throwable);
          } 
        } 
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      try {
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        this.upstream.dispose();
        onError((Throwable)param1T);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe((Disposable)this);
      } 
    }
    
    public T poll() throws Exception {
      return null;
    }
    
    public int requestFusion(int param1Int) {
      return param1Int & 0x2;
    }
    
    final class InnerObserver extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
      private static final long serialVersionUID = 8606673141535671828L;
      
      public void dispose() {
        DisposableHelper.dispose(this);
      }
      
      public boolean isDisposed() {
        return DisposableHelper.isDisposed(get());
      }
      
      public void onComplete() {
        ObservableFlatMapCompletable.FlatMapCompletableMainObserver.this.innerComplete(this);
      }
      
      public void onError(Throwable param2Throwable) {
        ObservableFlatMapCompletable.FlatMapCompletableMainObserver.this.innerError(this, param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.setOnce(this, param2Disposable);
      }
    }
  }
  
  final class InnerObserver extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
    private static final long serialVersionUID = 8606673141535671828L;
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      this.this$0.innerComplete(this);
    }
    
    public void onError(Throwable param1Throwable) {
      this.this$0.innerError(this, param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableFlatMapCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */