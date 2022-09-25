package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableFlatMapSingle<T, R> extends AbstractObservableWithUpstream<T, R> {
  final boolean delayErrors;
  
  final Function<? super T, ? extends SingleSource<? extends R>> mapper;
  
  public ObservableFlatMapSingle(ObservableSource<T> paramObservableSource, Function<? super T, ? extends SingleSource<? extends R>> paramFunction, boolean paramBoolean) {
    super(paramObservableSource);
    this.mapper = paramFunction;
    this.delayErrors = paramBoolean;
  }
  
  protected void subscribeActual(Observer<? super R> paramObserver) {
    this.source.subscribe(new FlatMapSingleObserver<T, R>(paramObserver, this.mapper, this.delayErrors));
  }
  
  static final class FlatMapSingleObserver<T, R> extends AtomicInteger implements Observer<T>, Disposable {
    private static final long serialVersionUID = 8600231336733376951L;
    
    final AtomicInteger active;
    
    volatile boolean cancelled;
    
    final boolean delayErrors;
    
    final Observer<? super R> downstream;
    
    final AtomicThrowable errors;
    
    final Function<? super T, ? extends SingleSource<? extends R>> mapper;
    
    final AtomicReference<SpscLinkedArrayQueue<R>> queue;
    
    final CompositeDisposable set;
    
    Disposable upstream;
    
    FlatMapSingleObserver(Observer<? super R> param1Observer, Function<? super T, ? extends SingleSource<? extends R>> param1Function, boolean param1Boolean) {
      this.downstream = param1Observer;
      this.mapper = param1Function;
      this.delayErrors = param1Boolean;
      this.set = new CompositeDisposable();
      this.errors = new AtomicThrowable();
      this.active = new AtomicInteger(1);
      this.queue = new AtomicReference<SpscLinkedArrayQueue<R>>();
    }
    
    void clear() {
      SpscLinkedArrayQueue spscLinkedArrayQueue = this.queue.get();
      if (spscLinkedArrayQueue != null)
        spscLinkedArrayQueue.clear(); 
    }
    
    public void dispose() {
      this.cancelled = true;
      this.upstream.dispose();
      this.set.dispose();
    }
    
    void drain() {
      if (getAndIncrement() == 0)
        drainLoop(); 
    }
    
    void drainLoop() {
      Observer<? super R> observer = this.downstream;
      AtomicInteger atomicInteger = this.active;
      AtomicReference<SpscLinkedArrayQueue<R>> atomicReference = this.queue;
      int i = 1;
      while (true) {
        Object object;
        if (this.cancelled) {
          clear();
          return;
        } 
        if (!this.delayErrors && (Throwable)this.errors.get() != null) {
          object = this.errors.terminate();
          clear();
          observer.onError((Throwable)object);
          return;
        } 
        int j = atomicInteger.get();
        boolean bool = false;
        if (j == 0) {
          j = 1;
        } else {
          j = 0;
        } 
        SpscLinkedArrayQueue spscLinkedArrayQueue = atomicReference.get();
        if (spscLinkedArrayQueue != null) {
          object = spscLinkedArrayQueue.poll();
        } else {
          spscLinkedArrayQueue = null;
        } 
        if (spscLinkedArrayQueue == null)
          bool = true; 
        if (j != 0 && bool) {
          object = this.errors.terminate();
          if (object != null) {
            observer.onError((Throwable)object);
          } else {
            observer.onComplete();
          } 
          return;
        } 
        if (bool) {
          j = addAndGet(-i);
          i = j;
          if (j == 0)
            return; 
          continue;
        } 
        observer.onNext(object);
      } 
    }
    
    SpscLinkedArrayQueue<R> getOrCreateQueue() {
      while (true) {
        SpscLinkedArrayQueue<R> spscLinkedArrayQueue = this.queue.get();
        if (spscLinkedArrayQueue != null)
          return spscLinkedArrayQueue; 
        spscLinkedArrayQueue = new SpscLinkedArrayQueue(Observable.bufferSize());
        if (this.queue.compareAndSet(null, spscLinkedArrayQueue))
          return spscLinkedArrayQueue; 
      } 
    }
    
    void innerError(InnerObserver param1InnerObserver, Throwable param1Throwable) {
      this.set.delete(param1InnerObserver);
      if (this.errors.addThrowable(param1Throwable)) {
        if (!this.delayErrors) {
          this.upstream.dispose();
          this.set.dispose();
        } 
        this.active.decrementAndGet();
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    void innerSuccess(InnerObserver param1InnerObserver, R param1R) {
      // Byte code:
      //   0: aload_0
      //   1: getfield set : Lio/reactivex/disposables/CompositeDisposable;
      //   4: aload_1
      //   5: invokevirtual delete : (Lio/reactivex/disposables/Disposable;)Z
      //   8: pop
      //   9: aload_0
      //   10: invokevirtual get : ()I
      //   13: ifne -> 121
      //   16: iconst_1
      //   17: istore_3
      //   18: aload_0
      //   19: iconst_0
      //   20: iconst_1
      //   21: invokevirtual compareAndSet : (II)Z
      //   24: ifeq -> 121
      //   27: aload_0
      //   28: getfield downstream : Lio/reactivex/Observer;
      //   31: aload_2
      //   32: invokeinterface onNext : (Ljava/lang/Object;)V
      //   37: aload_0
      //   38: getfield active : Ljava/util/concurrent/atomic/AtomicInteger;
      //   41: invokevirtual decrementAndGet : ()I
      //   44: ifne -> 50
      //   47: goto -> 52
      //   50: iconst_0
      //   51: istore_3
      //   52: aload_0
      //   53: getfield queue : Ljava/util/concurrent/atomic/AtomicReference;
      //   56: invokevirtual get : ()Ljava/lang/Object;
      //   59: checkcast io/reactivex/internal/queue/SpscLinkedArrayQueue
      //   62: astore_1
      //   63: iload_3
      //   64: ifeq -> 113
      //   67: aload_1
      //   68: ifnull -> 78
      //   71: aload_1
      //   72: invokevirtual isEmpty : ()Z
      //   75: ifeq -> 113
      //   78: aload_0
      //   79: getfield errors : Lio/reactivex/internal/util/AtomicThrowable;
      //   82: invokevirtual terminate : ()Ljava/lang/Throwable;
      //   85: astore_1
      //   86: aload_1
      //   87: ifnull -> 103
      //   90: aload_0
      //   91: getfield downstream : Lio/reactivex/Observer;
      //   94: aload_1
      //   95: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   100: goto -> 112
      //   103: aload_0
      //   104: getfield downstream : Lio/reactivex/Observer;
      //   107: invokeinterface onComplete : ()V
      //   112: return
      //   113: aload_0
      //   114: invokevirtual decrementAndGet : ()I
      //   117: ifne -> 152
      //   120: return
      //   121: aload_0
      //   122: invokevirtual getOrCreateQueue : ()Lio/reactivex/internal/queue/SpscLinkedArrayQueue;
      //   125: astore_1
      //   126: aload_1
      //   127: monitorenter
      //   128: aload_1
      //   129: aload_2
      //   130: invokevirtual offer : (Ljava/lang/Object;)Z
      //   133: pop
      //   134: aload_1
      //   135: monitorexit
      //   136: aload_0
      //   137: getfield active : Ljava/util/concurrent/atomic/AtomicInteger;
      //   140: invokevirtual decrementAndGet : ()I
      //   143: pop
      //   144: aload_0
      //   145: invokevirtual getAndIncrement : ()I
      //   148: ifeq -> 152
      //   151: return
      //   152: aload_0
      //   153: invokevirtual drainLoop : ()V
      //   156: return
      //   157: astore_2
      //   158: aload_1
      //   159: monitorexit
      //   160: aload_2
      //   161: athrow
      // Exception table:
      //   from	to	target	type
      //   128	136	157	finally
      //   158	160	157	finally
    }
    
    public boolean isDisposed() {
      return this.cancelled;
    }
    
    public void onComplete() {
      this.active.decrementAndGet();
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      this.active.decrementAndGet();
      if (this.errors.addThrowable(param1Throwable)) {
        if (!this.delayErrors)
          this.set.dispose(); 
        drain();
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
        this.downstream.onSubscribe(this);
      } 
    }
    
    final class InnerObserver extends AtomicReference<Disposable> implements SingleObserver<R>, Disposable {
      private static final long serialVersionUID = -502562646270949838L;
      
      public void dispose() {
        DisposableHelper.dispose(this);
      }
      
      public boolean isDisposed() {
        return DisposableHelper.isDisposed(get());
      }
      
      public void onError(Throwable param2Throwable) {
        ObservableFlatMapSingle.FlatMapSingleObserver.this.innerError(this, param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.setOnce(this, param2Disposable);
      }
      
      public void onSuccess(R param2R) {
        ObservableFlatMapSingle.FlatMapSingleObserver.this.innerSuccess(this, param2R);
      }
    }
  }
  
  final class InnerObserver extends AtomicReference<Disposable> implements SingleObserver<R>, Disposable {
    private static final long serialVersionUID = -502562646270949838L;
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onError(Throwable param1Throwable) {
      this.this$0.innerError(this, param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
    
    public void onSuccess(R param1R) {
      this.this$0.innerSuccess(this, param1R);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableFlatMapSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */