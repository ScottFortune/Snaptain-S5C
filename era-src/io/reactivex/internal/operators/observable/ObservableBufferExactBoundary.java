package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.observers.QueueDrainObserver;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.SerializedObserver;
import java.util.Collection;
import java.util.concurrent.Callable;

public final class ObservableBufferExactBoundary<T, U extends Collection<? super T>, B> extends AbstractObservableWithUpstream<T, U> {
  final ObservableSource<B> boundary;
  
  final Callable<U> bufferSupplier;
  
  public ObservableBufferExactBoundary(ObservableSource<T> paramObservableSource, ObservableSource<B> paramObservableSource1, Callable<U> paramCallable) {
    super(paramObservableSource);
    this.boundary = paramObservableSource1;
    this.bufferSupplier = paramCallable;
  }
  
  protected void subscribeActual(Observer<? super U> paramObserver) {
    this.source.subscribe(new BufferExactBoundaryObserver<Object, U, B>((Observer<? super U>)new SerializedObserver(paramObserver), this.bufferSupplier, this.boundary));
  }
  
  static final class BufferBoundaryObserver<T, U extends Collection<? super T>, B> extends DisposableObserver<B> {
    final ObservableBufferExactBoundary.BufferExactBoundaryObserver<T, U, B> parent;
    
    BufferBoundaryObserver(ObservableBufferExactBoundary.BufferExactBoundaryObserver<T, U, B> param1BufferExactBoundaryObserver) {
      this.parent = param1BufferExactBoundaryObserver;
    }
    
    public void onComplete() {
      this.parent.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.onError(param1Throwable);
    }
    
    public void onNext(B param1B) {
      this.parent.next();
    }
  }
  
  static final class BufferExactBoundaryObserver<T, U extends Collection<? super T>, B> extends QueueDrainObserver<T, U, U> implements Observer<T>, Disposable {
    final ObservableSource<B> boundary;
    
    U buffer;
    
    final Callable<U> bufferSupplier;
    
    Disposable other;
    
    Disposable upstream;
    
    BufferExactBoundaryObserver(Observer<? super U> param1Observer, Callable<U> param1Callable, ObservableSource<B> param1ObservableSource) {
      super(param1Observer, (SimplePlainQueue)new MpscLinkedQueue());
      this.bufferSupplier = param1Callable;
      this.boundary = param1ObservableSource;
    }
    
    public void accept(Observer<? super U> param1Observer, U param1U) {
      this.downstream.onNext(param1U);
    }
    
    public void dispose() {
      if (!this.cancelled) {
        this.cancelled = true;
        this.other.dispose();
        this.upstream.dispose();
        if (enter())
          this.queue.clear(); 
      } 
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
      //   19: monitorenter
      //   20: aload_0
      //   21: getfield buffer : Ljava/util/Collection;
      //   24: astore_2
      //   25: aload_2
      //   26: ifnonnull -> 32
      //   29: aload_0
      //   30: monitorexit
      //   31: return
      //   32: aload_0
      //   33: aload_1
      //   34: putfield buffer : Ljava/util/Collection;
      //   37: aload_0
      //   38: monitorexit
      //   39: aload_0
      //   40: aload_2
      //   41: iconst_0
      //   42: aload_0
      //   43: invokevirtual fastPathEmit : (Ljava/lang/Object;ZLio/reactivex/disposables/Disposable;)V
      //   46: return
      //   47: astore_2
      //   48: aload_0
      //   49: monitorexit
      //   50: aload_2
      //   51: athrow
      //   52: astore_2
      //   53: aload_2
      //   54: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
      //   57: aload_0
      //   58: invokevirtual dispose : ()V
      //   61: aload_0
      //   62: getfield downstream : Lio/reactivex/Observer;
      //   65: aload_2
      //   66: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   71: return
      // Exception table:
      //   from	to	target	type
      //   0	18	52	finally
      //   20	25	47	finally
      //   29	31	47	finally
      //   32	39	47	finally
      //   48	50	47	finally
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
        ObservableBufferExactBoundary.BufferBoundaryObserver<Object, Collection<?>, Object> bufferBoundaryObserver;
        this.upstream = param1Disposable;
        try {
          Collection collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
          this.buffer = (U)collection;
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableBufferExactBoundary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */