package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.observers.QueueDrainObserver;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.SerializedObserver;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableBufferBoundarySupplier<T, U extends Collection<? super T>, B> extends AbstractObservableWithUpstream<T, U> {
  final Callable<? extends ObservableSource<B>> boundarySupplier;
  
  final Callable<U> bufferSupplier;
  
  public ObservableBufferBoundarySupplier(ObservableSource<T> paramObservableSource, Callable<? extends ObservableSource<B>> paramCallable, Callable<U> paramCallable1) {
    super(paramObservableSource);
    this.boundarySupplier = paramCallable;
    this.bufferSupplier = paramCallable1;
  }
  
  protected void subscribeActual(Observer<? super U> paramObserver) {
    this.source.subscribe(new BufferBoundarySupplierObserver<Object, U, B>((Observer<? super U>)new SerializedObserver(paramObserver), this.bufferSupplier, this.boundarySupplier));
  }
  
  static final class BufferBoundaryObserver<T, U extends Collection<? super T>, B> extends DisposableObserver<B> {
    boolean once;
    
    final ObservableBufferBoundarySupplier.BufferBoundarySupplierObserver<T, U, B> parent;
    
    BufferBoundaryObserver(ObservableBufferBoundarySupplier.BufferBoundarySupplierObserver<T, U, B> param1BufferBoundarySupplierObserver) {
      this.parent = param1BufferBoundarySupplierObserver;
    }
    
    public void onComplete() {
      if (this.once)
        return; 
      this.once = true;
      this.parent.next();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.once) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.once = true;
      this.parent.onError(param1Throwable);
    }
    
    public void onNext(B param1B) {
      if (this.once)
        return; 
      this.once = true;
      dispose();
      this.parent.next();
    }
  }
  
  static final class BufferBoundarySupplierObserver<T, U extends Collection<? super T>, B> extends QueueDrainObserver<T, U, U> implements Observer<T>, Disposable {
    final Callable<? extends ObservableSource<B>> boundarySupplier;
    
    U buffer;
    
    final Callable<U> bufferSupplier;
    
    final AtomicReference<Disposable> other = new AtomicReference<Disposable>();
    
    Disposable upstream;
    
    BufferBoundarySupplierObserver(Observer<? super U> param1Observer, Callable<U> param1Callable, Callable<? extends ObservableSource<B>> param1Callable1) {
      super(param1Observer, (SimplePlainQueue)new MpscLinkedQueue());
      this.bufferSupplier = param1Callable;
      this.boundarySupplier = param1Callable1;
    }
    
    public void accept(Observer<? super U> param1Observer, U param1U) {
      this.downstream.onNext(param1U);
    }
    
    public void dispose() {
      if (!this.cancelled) {
        this.cancelled = true;
        this.upstream.dispose();
        disposeOther();
        if (enter())
          this.queue.clear(); 
      } 
    }
    
    void disposeOther() {
      DisposableHelper.dispose(this.other);
    }
    
    public boolean isDisposed() {
      return this.cancelled;
    }
    
    void next() {
      // Byte code:
      //   0: aload_0
      //   1: getfield bufferSupplier : Ljava/util/concurrent/Callable;
      //   4: invokeinterface call : ()Ljava/lang/Object;
      //   9: ldc 'The buffer supplied is null'
      //   11: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
      //   14: checkcast java/util/Collection
      //   17: astore_1
      //   18: aload_0
      //   19: getfield boundarySupplier : Ljava/util/concurrent/Callable;
      //   22: invokeinterface call : ()Ljava/lang/Object;
      //   27: ldc 'The boundary ObservableSource supplied is null'
      //   29: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
      //   32: checkcast io/reactivex/ObservableSource
      //   35: astore_2
      //   36: new io/reactivex/internal/operators/observable/ObservableBufferBoundarySupplier$BufferBoundaryObserver
      //   39: dup
      //   40: aload_0
      //   41: invokespecial <init> : (Lio/reactivex/internal/operators/observable/ObservableBufferBoundarySupplier$BufferBoundarySupplierObserver;)V
      //   44: astore_3
      //   45: aload_0
      //   46: getfield other : Ljava/util/concurrent/atomic/AtomicReference;
      //   49: aload_3
      //   50: invokestatic replace : (Ljava/util/concurrent/atomic/AtomicReference;Lio/reactivex/disposables/Disposable;)Z
      //   53: ifeq -> 102
      //   56: aload_0
      //   57: monitorenter
      //   58: aload_0
      //   59: getfield buffer : Ljava/util/Collection;
      //   62: astore #4
      //   64: aload #4
      //   66: ifnonnull -> 72
      //   69: aload_0
      //   70: monitorexit
      //   71: return
      //   72: aload_0
      //   73: aload_1
      //   74: putfield buffer : Ljava/util/Collection;
      //   77: aload_0
      //   78: monitorexit
      //   79: aload_2
      //   80: aload_3
      //   81: invokeinterface subscribe : (Lio/reactivex/Observer;)V
      //   86: aload_0
      //   87: aload #4
      //   89: iconst_0
      //   90: aload_0
      //   91: invokevirtual fastPathEmit : (Ljava/lang/Object;ZLio/reactivex/disposables/Disposable;)V
      //   94: goto -> 102
      //   97: astore_3
      //   98: aload_0
      //   99: monitorexit
      //   100: aload_3
      //   101: athrow
      //   102: return
      //   103: astore_3
      //   104: aload_3
      //   105: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
      //   108: aload_0
      //   109: iconst_1
      //   110: putfield cancelled : Z
      //   113: aload_0
      //   114: getfield upstream : Lio/reactivex/disposables/Disposable;
      //   117: invokeinterface dispose : ()V
      //   122: aload_0
      //   123: getfield downstream : Lio/reactivex/Observer;
      //   126: aload_3
      //   127: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   132: return
      //   133: astore_3
      //   134: aload_3
      //   135: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
      //   138: aload_0
      //   139: invokevirtual dispose : ()V
      //   142: aload_0
      //   143: getfield downstream : Lio/reactivex/Observer;
      //   146: aload_3
      //   147: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   152: return
      // Exception table:
      //   from	to	target	type
      //   0	18	133	finally
      //   18	36	103	finally
      //   58	64	97	finally
      //   69	71	97	finally
      //   72	79	97	finally
      //   98	100	97	finally
    }
    
    public void onComplete() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield buffer : Ljava/util/Collection;
      //   6: astore_1
      //   7: aload_1
      //   8: ifnonnull -> 14
      //   11: aload_0
      //   12: monitorexit
      //   13: return
      //   14: aload_0
      //   15: aconst_null
      //   16: putfield buffer : Ljava/util/Collection;
      //   19: aload_0
      //   20: monitorexit
      //   21: aload_0
      //   22: getfield queue : Lio/reactivex/internal/fuseable/SimplePlainQueue;
      //   25: aload_1
      //   26: invokeinterface offer : (Ljava/lang/Object;)Z
      //   31: pop
      //   32: aload_0
      //   33: iconst_1
      //   34: putfield done : Z
      //   37: aload_0
      //   38: invokevirtual enter : ()Z
      //   41: ifeq -> 58
      //   44: aload_0
      //   45: getfield queue : Lio/reactivex/internal/fuseable/SimplePlainQueue;
      //   48: aload_0
      //   49: getfield downstream : Lio/reactivex/Observer;
      //   52: iconst_0
      //   53: aload_0
      //   54: aload_0
      //   55: invokestatic drainLoop : (Lio/reactivex/internal/fuseable/SimplePlainQueue;Lio/reactivex/Observer;ZLio/reactivex/disposables/Disposable;Lio/reactivex/internal/util/ObservableQueueDrain;)V
      //   58: return
      //   59: astore_1
      //   60: aload_0
      //   61: monitorexit
      //   62: aload_1
      //   63: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	59	finally
      //   11	13	59	finally
      //   14	21	59	finally
      //   60	62	59	finally
    }
    
    public void onError(Throwable param1Throwable) {
      dispose();
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield buffer : Ljava/util/Collection;
      //   6: astore_2
      //   7: aload_2
      //   8: ifnonnull -> 14
      //   11: aload_0
      //   12: monitorexit
      //   13: return
      //   14: aload_2
      //   15: aload_1
      //   16: invokeinterface add : (Ljava/lang/Object;)Z
      //   21: pop
      //   22: aload_0
      //   23: monitorexit
      //   24: return
      //   25: astore_1
      //   26: aload_0
      //   27: monitorexit
      //   28: aload_1
      //   29: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	25	finally
      //   11	13	25	finally
      //   14	24	25	finally
      //   26	28	25	finally
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        ObservableBufferBoundarySupplier.BufferBoundaryObserver<Object, Collection<?>, Object> bufferBoundaryObserver;
        this.upstream = param1Disposable;
        Observer observer = this.downstream;
        try {
        
        } finally {
          Exception exception = null;
          Exceptions.throwIfFatal(exception);
          this.cancelled = true;
          bufferBoundaryObserver.dispose();
        } 
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableBufferBoundarySupplier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */