package io.reactivex.internal.operators.mixed;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;

public final class FlowableSwitchMapCompletable<T> extends Completable {
  final boolean delayErrors;
  
  final Function<? super T, ? extends CompletableSource> mapper;
  
  final Flowable<T> source;
  
  public FlowableSwitchMapCompletable(Flowable<T> paramFlowable, Function<? super T, ? extends CompletableSource> paramFunction, boolean paramBoolean) {
    this.source = paramFlowable;
    this.mapper = paramFunction;
    this.delayErrors = paramBoolean;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    this.source.subscribe(new SwitchMapCompletableObserver<T>(paramCompletableObserver, this.mapper, this.delayErrors));
  }
  
  static final class SwitchMapCompletableObserver<T> implements FlowableSubscriber<T>, Disposable {
    static final SwitchMapInnerObserver INNER_DISPOSED = new SwitchMapInnerObserver(null);
    
    final boolean delayErrors;
    
    volatile boolean done;
    
    final CompletableObserver downstream;
    
    final AtomicThrowable errors;
    
    final AtomicReference<SwitchMapInnerObserver> inner;
    
    final Function<? super T, ? extends CompletableSource> mapper;
    
    Subscription upstream;
    
    SwitchMapCompletableObserver(CompletableObserver param1CompletableObserver, Function<? super T, ? extends CompletableSource> param1Function, boolean param1Boolean) {
      this.downstream = param1CompletableObserver;
      this.mapper = param1Function;
      this.delayErrors = param1Boolean;
      this.errors = new AtomicThrowable();
      this.inner = new AtomicReference<SwitchMapInnerObserver>();
    }
    
    public void dispose() {
      this.upstream.cancel();
      disposeInner();
    }
    
    void disposeInner() {
      SwitchMapInnerObserver switchMapInnerObserver = this.inner.getAndSet(INNER_DISPOSED);
      if (switchMapInnerObserver != null && switchMapInnerObserver != INNER_DISPOSED)
        switchMapInnerObserver.dispose(); 
    }
    
    void innerComplete(SwitchMapInnerObserver param1SwitchMapInnerObserver) {
      if (this.inner.compareAndSet(param1SwitchMapInnerObserver, null) && this.done) {
        Throwable throwable = this.errors.terminate();
        if (throwable == null) {
          this.downstream.onComplete();
        } else {
          this.downstream.onError(throwable);
        } 
      } 
    }
    
    void innerError(SwitchMapInnerObserver param1SwitchMapInnerObserver, Throwable param1Throwable) {
      if (this.inner.compareAndSet(param1SwitchMapInnerObserver, null) && this.errors.addThrowable(param1Throwable)) {
        if (this.delayErrors) {
          if (this.done) {
            Throwable throwable = this.errors.terminate();
            this.downstream.onError(throwable);
          } 
        } else {
          dispose();
          Throwable throwable = this.errors.terminate();
          if (throwable != ExceptionHelper.TERMINATED)
            this.downstream.onError(throwable); 
        } 
        return;
      } 
      RxJavaPlugins.onError(param1Throwable);
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (this.inner.get() == INNER_DISPOSED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onComplete() {
      this.done = true;
      if (this.inner.get() == null) {
        Throwable throwable = this.errors.terminate();
        if (throwable == null) {
          this.downstream.onComplete();
        } else {
          this.downstream.onError(throwable);
        } 
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.errors.addThrowable(param1Throwable)) {
        if (this.delayErrors) {
          onComplete();
        } else {
          disposeInner();
          param1Throwable = this.errors.terminate();
          if (param1Throwable != ExceptionHelper.TERMINATED)
            this.downstream.onError(param1Throwable); 
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
        this.upstream.cancel();
        onError((Throwable)param1T);
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        param1Subscription.request(Long.MAX_VALUE);
      } 
    }
    
    static final class SwitchMapInnerObserver extends AtomicReference<Disposable> implements CompletableObserver {
      private static final long serialVersionUID = -8003404460084760287L;
      
      final FlowableSwitchMapCompletable.SwitchMapCompletableObserver<?> parent;
      
      SwitchMapInnerObserver(FlowableSwitchMapCompletable.SwitchMapCompletableObserver<?> param2SwitchMapCompletableObserver) {
        this.parent = param2SwitchMapCompletableObserver;
      }
      
      void dispose() {
        DisposableHelper.dispose(this);
      }
      
      public void onComplete() {
        this.parent.innerComplete(this);
      }
      
      public void onError(Throwable param2Throwable) {
        this.parent.innerError(this, param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.setOnce(this, param2Disposable);
      }
    }
  }
  
  static final class SwitchMapInnerObserver extends AtomicReference<Disposable> implements CompletableObserver {
    private static final long serialVersionUID = -8003404460084760287L;
    
    final FlowableSwitchMapCompletable.SwitchMapCompletableObserver<?> parent;
    
    SwitchMapInnerObserver(FlowableSwitchMapCompletable.SwitchMapCompletableObserver<?> param1SwitchMapCompletableObserver) {
      this.parent = param1SwitchMapCompletableObserver;
    }
    
    void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public void onComplete() {
      this.parent.innerComplete(this);
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.innerError(this, param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/mixed/FlowableSwitchMapCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */