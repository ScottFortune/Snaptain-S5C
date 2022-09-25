package io.reactivex.internal.operators.mixed;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.ErrorMode;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;

public final class FlowableConcatMapCompletable<T> extends Completable {
  final ErrorMode errorMode;
  
  final Function<? super T, ? extends CompletableSource> mapper;
  
  final int prefetch;
  
  final Flowable<T> source;
  
  public FlowableConcatMapCompletable(Flowable<T> paramFlowable, Function<? super T, ? extends CompletableSource> paramFunction, ErrorMode paramErrorMode, int paramInt) {
    this.source = paramFlowable;
    this.mapper = paramFunction;
    this.errorMode = paramErrorMode;
    this.prefetch = paramInt;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    this.source.subscribe(new ConcatMapCompletableObserver<T>(paramCompletableObserver, this.mapper, this.errorMode, this.prefetch));
  }
  
  static final class ConcatMapCompletableObserver<T> extends AtomicInteger implements FlowableSubscriber<T>, Disposable {
    private static final long serialVersionUID = 3610901111000061034L;
    
    volatile boolean active;
    
    int consumed;
    
    volatile boolean disposed;
    
    volatile boolean done;
    
    final CompletableObserver downstream;
    
    final ErrorMode errorMode;
    
    final AtomicThrowable errors;
    
    final ConcatMapInnerObserver inner;
    
    final Function<? super T, ? extends CompletableSource> mapper;
    
    final int prefetch;
    
    final SimplePlainQueue<T> queue;
    
    Subscription upstream;
    
    ConcatMapCompletableObserver(CompletableObserver param1CompletableObserver, Function<? super T, ? extends CompletableSource> param1Function, ErrorMode param1ErrorMode, int param1Int) {
      this.downstream = param1CompletableObserver;
      this.mapper = param1Function;
      this.errorMode = param1ErrorMode;
      this.prefetch = param1Int;
      this.errors = new AtomicThrowable();
      this.inner = new ConcatMapInnerObserver(this);
      this.queue = (SimplePlainQueue<T>)new SpscArrayQueue(param1Int);
    }
    
    public void dispose() {
      this.disposed = true;
      this.upstream.cancel();
      this.inner.dispose();
      if (getAndIncrement() == 0)
        this.queue.clear(); 
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      do {
        if (this.disposed) {
          this.queue.clear();
          return;
        } 
        if (this.active)
          continue; 
        if (this.errorMode == ErrorMode.BOUNDARY && this.errors.get() != null) {
          this.queue.clear();
          Throwable throwable = this.errors.terminate();
          this.downstream.onError(throwable);
          return;
        } 
        boolean bool = this.done;
        Object object = this.queue.poll();
        if (object == null) {
          i = 1;
        } else {
          i = 0;
        } 
        if (bool && i) {
          object = this.errors.terminate();
          if (object != null) {
            this.downstream.onError((Throwable)object);
          } else {
            this.downstream.onComplete();
          } 
          return;
        } 
        if (i)
          continue; 
        int i = this.prefetch;
        int j = i - (i >> 1);
        i = this.consumed + 1;
        if (i == j) {
          this.consumed = 0;
          this.upstream.request(j);
        } else {
          this.consumed = i;
        } 
        try {
        
        } finally {
          object = null;
          Exceptions.throwIfFatal((Throwable)object);
          this.queue.clear();
          this.upstream.cancel();
          this.errors.addThrowable((Throwable)object);
          object = this.errors.terminate();
          this.downstream.onError((Throwable)object);
        } 
      } while (decrementAndGet() != 0);
    }
    
    void innerComplete() {
      this.active = false;
      drain();
    }
    
    void innerError(Throwable param1Throwable) {
      if (this.errors.addThrowable(param1Throwable)) {
        if (this.errorMode == ErrorMode.IMMEDIATE) {
          this.upstream.cancel();
          param1Throwable = this.errors.terminate();
          if (param1Throwable != ExceptionHelper.TERMINATED)
            this.downstream.onError(param1Throwable); 
          if (getAndIncrement() == 0)
            this.queue.clear(); 
        } else {
          this.active = false;
          drain();
        } 
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public boolean isDisposed() {
      return this.disposed;
    }
    
    public void onComplete() {
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.errors.addThrowable(param1Throwable)) {
        if (this.errorMode == ErrorMode.IMMEDIATE) {
          this.inner.dispose();
          param1Throwable = this.errors.terminate();
          if (param1Throwable != ExceptionHelper.TERMINATED)
            this.downstream.onError(param1Throwable); 
          if (getAndIncrement() == 0)
            this.queue.clear(); 
        } else {
          this.done = true;
          drain();
        } 
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      if (this.queue.offer(param1T)) {
        drain();
      } else {
        this.upstream.cancel();
        onError((Throwable)new MissingBackpressureException("Queue full?!"));
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        param1Subscription.request(this.prefetch);
      } 
    }
    
    static final class ConcatMapInnerObserver extends AtomicReference<Disposable> implements CompletableObserver {
      private static final long serialVersionUID = 5638352172918776687L;
      
      final FlowableConcatMapCompletable.ConcatMapCompletableObserver<?> parent;
      
      ConcatMapInnerObserver(FlowableConcatMapCompletable.ConcatMapCompletableObserver<?> param2ConcatMapCompletableObserver) {
        this.parent = param2ConcatMapCompletableObserver;
      }
      
      void dispose() {
        DisposableHelper.dispose(this);
      }
      
      public void onComplete() {
        this.parent.innerComplete();
      }
      
      public void onError(Throwable param2Throwable) {
        this.parent.innerError(param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.replace(this, param2Disposable);
      }
    }
  }
  
  static final class ConcatMapInnerObserver extends AtomicReference<Disposable> implements CompletableObserver {
    private static final long serialVersionUID = 5638352172918776687L;
    
    final FlowableConcatMapCompletable.ConcatMapCompletableObserver<?> parent;
    
    ConcatMapInnerObserver(FlowableConcatMapCompletable.ConcatMapCompletableObserver<?> param1ConcatMapCompletableObserver) {
      this.parent = param1ConcatMapCompletableObserver;
    }
    
    void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public void onComplete() {
      this.parent.innerComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.innerError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.replace(this, param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/mixed/FlowableConcatMapCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */