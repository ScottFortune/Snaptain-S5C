package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.observers.QueueDrainObserver;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.observers.SerializedObserver;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableBufferTimed<T, U extends Collection<? super T>> extends AbstractObservableWithUpstream<T, U> {
  final Callable<U> bufferSupplier;
  
  final int maxSize;
  
  final boolean restartTimerOnMaxSize;
  
  final Scheduler scheduler;
  
  final long timeskip;
  
  final long timespan;
  
  final TimeUnit unit;
  
  public ObservableBufferTimed(ObservableSource<T> paramObservableSource, long paramLong1, long paramLong2, TimeUnit paramTimeUnit, Scheduler paramScheduler, Callable<U> paramCallable, int paramInt, boolean paramBoolean) {
    super(paramObservableSource);
    this.timespan = paramLong1;
    this.timeskip = paramLong2;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
    this.bufferSupplier = paramCallable;
    this.maxSize = paramInt;
    this.restartTimerOnMaxSize = paramBoolean;
  }
  
  protected void subscribeActual(Observer<? super U> paramObserver) {
    if (this.timespan == this.timeskip && this.maxSize == Integer.MAX_VALUE) {
      this.source.subscribe((Observer)new BufferExactUnboundedObserver<Object, U>((Observer<? super U>)new SerializedObserver(paramObserver), this.bufferSupplier, this.timespan, this.unit, this.scheduler));
      return;
    } 
    Scheduler.Worker worker = this.scheduler.createWorker();
    if (this.timespan == this.timeskip) {
      this.source.subscribe((Observer)new BufferExactBoundedObserver<Object, U>((Observer<? super U>)new SerializedObserver(paramObserver), this.bufferSupplier, this.timespan, this.unit, this.maxSize, this.restartTimerOnMaxSize, worker));
      return;
    } 
    this.source.subscribe((Observer)new BufferSkipBoundedObserver<Object, U>((Observer<? super U>)new SerializedObserver(paramObserver), this.bufferSupplier, this.timespan, this.timeskip, this.unit, worker));
  }
  
  static final class BufferExactBoundedObserver<T, U extends Collection<? super T>> extends QueueDrainObserver<T, U, U> implements Runnable, Disposable {
    U buffer;
    
    final Callable<U> bufferSupplier;
    
    long consumerIndex;
    
    final int maxSize;
    
    long producerIndex;
    
    final boolean restartTimerOnMaxSize;
    
    Disposable timer;
    
    final long timespan;
    
    final TimeUnit unit;
    
    Disposable upstream;
    
    final Scheduler.Worker w;
    
    BufferExactBoundedObserver(Observer<? super U> param1Observer, Callable<U> param1Callable, long param1Long, TimeUnit param1TimeUnit, int param1Int, boolean param1Boolean, Scheduler.Worker param1Worker) {
      super(param1Observer, (SimplePlainQueue)new MpscLinkedQueue());
      this.bufferSupplier = param1Callable;
      this.timespan = param1Long;
      this.unit = param1TimeUnit;
      this.maxSize = param1Int;
      this.restartTimerOnMaxSize = param1Boolean;
      this.w = param1Worker;
    }
    
    public void accept(Observer<? super U> param1Observer, U param1U) {
      param1Observer.onNext(param1U);
    }
    
    public void dispose() {
      // Byte code:
      //   0: aload_0
      //   1: getfield cancelled : Z
      //   4: ifne -> 45
      //   7: aload_0
      //   8: iconst_1
      //   9: putfield cancelled : Z
      //   12: aload_0
      //   13: getfield upstream : Lio/reactivex/disposables/Disposable;
      //   16: invokeinterface dispose : ()V
      //   21: aload_0
      //   22: getfield w : Lio/reactivex/Scheduler$Worker;
      //   25: invokevirtual dispose : ()V
      //   28: aload_0
      //   29: monitorenter
      //   30: aload_0
      //   31: aconst_null
      //   32: putfield buffer : Ljava/util/Collection;
      //   35: aload_0
      //   36: monitorexit
      //   37: goto -> 45
      //   40: astore_1
      //   41: aload_0
      //   42: monitorexit
      //   43: aload_1
      //   44: athrow
      //   45: return
      // Exception table:
      //   from	to	target	type
      //   30	37	40	finally
      //   41	43	40	finally
    }
    
    public boolean isDisposed() {
      return this.cancelled;
    }
    
    public void onComplete() {
      // Byte code:
      //   0: aload_0
      //   1: getfield w : Lio/reactivex/Scheduler$Worker;
      //   4: invokevirtual dispose : ()V
      //   7: aload_0
      //   8: monitorenter
      //   9: aload_0
      //   10: getfield buffer : Ljava/util/Collection;
      //   13: astore_1
      //   14: aload_0
      //   15: aconst_null
      //   16: putfield buffer : Ljava/util/Collection;
      //   19: aload_0
      //   20: monitorexit
      //   21: aload_1
      //   22: ifnull -> 62
      //   25: aload_0
      //   26: getfield queue : Lio/reactivex/internal/fuseable/SimplePlainQueue;
      //   29: aload_1
      //   30: invokeinterface offer : (Ljava/lang/Object;)Z
      //   35: pop
      //   36: aload_0
      //   37: iconst_1
      //   38: putfield done : Z
      //   41: aload_0
      //   42: invokevirtual enter : ()Z
      //   45: ifeq -> 62
      //   48: aload_0
      //   49: getfield queue : Lio/reactivex/internal/fuseable/SimplePlainQueue;
      //   52: aload_0
      //   53: getfield downstream : Lio/reactivex/Observer;
      //   56: iconst_0
      //   57: aload_0
      //   58: aload_0
      //   59: invokestatic drainLoop : (Lio/reactivex/internal/fuseable/SimplePlainQueue;Lio/reactivex/Observer;ZLio/reactivex/disposables/Disposable;Lio/reactivex/internal/util/ObservableQueueDrain;)V
      //   62: return
      //   63: astore_1
      //   64: aload_0
      //   65: monitorexit
      //   66: aload_1
      //   67: athrow
      // Exception table:
      //   from	to	target	type
      //   9	21	63	finally
      //   64	66	63	finally
    }
    
    public void onError(Throwable param1Throwable) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: aconst_null
      //   4: putfield buffer : Ljava/util/Collection;
      //   7: aload_0
      //   8: monitorexit
      //   9: aload_0
      //   10: getfield downstream : Lio/reactivex/Observer;
      //   13: aload_1
      //   14: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   19: aload_0
      //   20: getfield w : Lio/reactivex/Scheduler$Worker;
      //   23: invokevirtual dispose : ()V
      //   26: return
      //   27: astore_1
      //   28: aload_0
      //   29: monitorexit
      //   30: aload_1
      //   31: athrow
      // Exception table:
      //   from	to	target	type
      //   2	9	27	finally
      //   28	30	27	finally
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
      //   22: aload_2
      //   23: invokeinterface size : ()I
      //   28: aload_0
      //   29: getfield maxSize : I
      //   32: if_icmpge -> 38
      //   35: aload_0
      //   36: monitorexit
      //   37: return
      //   38: aload_0
      //   39: aconst_null
      //   40: putfield buffer : Ljava/util/Collection;
      //   43: aload_0
      //   44: aload_0
      //   45: getfield producerIndex : J
      //   48: lconst_1
      //   49: ladd
      //   50: putfield producerIndex : J
      //   53: aload_0
      //   54: monitorexit
      //   55: aload_0
      //   56: getfield restartTimerOnMaxSize : Z
      //   59: ifeq -> 71
      //   62: aload_0
      //   63: getfield timer : Lio/reactivex/disposables/Disposable;
      //   66: invokeinterface dispose : ()V
      //   71: aload_0
      //   72: aload_2
      //   73: iconst_0
      //   74: aload_0
      //   75: invokevirtual fastPathOrderedEmit : (Ljava/lang/Object;ZLio/reactivex/disposables/Disposable;)V
      //   78: aload_0
      //   79: getfield bufferSupplier : Ljava/util/concurrent/Callable;
      //   82: invokeinterface call : ()Ljava/lang/Object;
      //   87: ldc 'The buffer supplied is null'
      //   89: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
      //   92: checkcast java/util/Collection
      //   95: astore_1
      //   96: aload_0
      //   97: monitorenter
      //   98: aload_0
      //   99: aload_1
      //   100: putfield buffer : Ljava/util/Collection;
      //   103: aload_0
      //   104: aload_0
      //   105: getfield consumerIndex : J
      //   108: lconst_1
      //   109: ladd
      //   110: putfield consumerIndex : J
      //   113: aload_0
      //   114: monitorexit
      //   115: aload_0
      //   116: getfield restartTimerOnMaxSize : Z
      //   119: ifeq -> 147
      //   122: aload_0
      //   123: getfield w : Lio/reactivex/Scheduler$Worker;
      //   126: astore_1
      //   127: aload_0
      //   128: getfield timespan : J
      //   131: lstore_3
      //   132: aload_0
      //   133: aload_1
      //   134: aload_0
      //   135: lload_3
      //   136: lload_3
      //   137: aload_0
      //   138: getfield unit : Ljava/util/concurrent/TimeUnit;
      //   141: invokevirtual schedulePeriodically : (Ljava/lang/Runnable;JJLjava/util/concurrent/TimeUnit;)Lio/reactivex/disposables/Disposable;
      //   144: putfield timer : Lio/reactivex/disposables/Disposable;
      //   147: return
      //   148: astore_1
      //   149: aload_0
      //   150: monitorexit
      //   151: aload_1
      //   152: athrow
      //   153: astore_1
      //   154: aload_1
      //   155: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
      //   158: aload_0
      //   159: getfield downstream : Lio/reactivex/Observer;
      //   162: aload_1
      //   163: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   168: aload_0
      //   169: invokevirtual dispose : ()V
      //   172: return
      //   173: astore_1
      //   174: aload_0
      //   175: monitorexit
      //   176: aload_1
      //   177: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	173	finally
      //   11	13	173	finally
      //   14	37	173	finally
      //   38	55	173	finally
      //   78	96	153	finally
      //   98	115	148	finally
      //   149	151	148	finally
      //   174	176	173	finally
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        Scheduler.Worker worker;
        this.upstream = param1Disposable;
        try {
          Collection collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
          this.buffer = (U)collection;
        } finally {
          Exception exception = null;
          Exceptions.throwIfFatal(exception);
          worker.dispose();
          EmptyDisposable.error(exception, this.downstream);
        } 
      } 
    }
    
    public void run() {
      // Byte code:
      //   0: aload_0
      //   1: getfield bufferSupplier : Ljava/util/concurrent/Callable;
      //   4: invokeinterface call : ()Ljava/lang/Object;
      //   9: ldc 'The bufferSupplier returned a null buffer'
      //   11: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
      //   14: checkcast java/util/Collection
      //   17: astore_1
      //   18: aload_0
      //   19: monitorenter
      //   20: aload_0
      //   21: getfield buffer : Ljava/util/Collection;
      //   24: astore_2
      //   25: aload_2
      //   26: ifnull -> 59
      //   29: aload_0
      //   30: getfield producerIndex : J
      //   33: aload_0
      //   34: getfield consumerIndex : J
      //   37: lcmp
      //   38: ifeq -> 44
      //   41: goto -> 59
      //   44: aload_0
      //   45: aload_1
      //   46: putfield buffer : Ljava/util/Collection;
      //   49: aload_0
      //   50: monitorexit
      //   51: aload_0
      //   52: aload_2
      //   53: iconst_0
      //   54: aload_0
      //   55: invokevirtual fastPathOrderedEmit : (Ljava/lang/Object;ZLio/reactivex/disposables/Disposable;)V
      //   58: return
      //   59: aload_0
      //   60: monitorexit
      //   61: return
      //   62: astore_2
      //   63: aload_0
      //   64: monitorexit
      //   65: aload_2
      //   66: athrow
      //   67: astore_2
      //   68: aload_2
      //   69: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
      //   72: aload_0
      //   73: invokevirtual dispose : ()V
      //   76: aload_0
      //   77: getfield downstream : Lio/reactivex/Observer;
      //   80: aload_2
      //   81: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   86: return
      // Exception table:
      //   from	to	target	type
      //   0	18	67	finally
      //   20	25	62	finally
      //   29	41	62	finally
      //   44	51	62	finally
      //   59	61	62	finally
      //   63	65	62	finally
    }
  }
  
  static final class BufferExactUnboundedObserver<T, U extends Collection<? super T>> extends QueueDrainObserver<T, U, U> implements Runnable, Disposable {
    U buffer;
    
    final Callable<U> bufferSupplier;
    
    final Scheduler scheduler;
    
    final AtomicReference<Disposable> timer = new AtomicReference<Disposable>();
    
    final long timespan;
    
    final TimeUnit unit;
    
    Disposable upstream;
    
    BufferExactUnboundedObserver(Observer<? super U> param1Observer, Callable<U> param1Callable, long param1Long, TimeUnit param1TimeUnit, Scheduler param1Scheduler) {
      super(param1Observer, (SimplePlainQueue)new MpscLinkedQueue());
      this.bufferSupplier = param1Callable;
      this.timespan = param1Long;
      this.unit = param1TimeUnit;
      this.scheduler = param1Scheduler;
    }
    
    public void accept(Observer<? super U> param1Observer, U param1U) {
      this.downstream.onNext(param1U);
    }
    
    public void dispose() {
      DisposableHelper.dispose(this.timer);
      this.upstream.dispose();
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (this.timer.get() == DisposableHelper.DISPOSED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onComplete() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield buffer : Ljava/util/Collection;
      //   6: astore_1
      //   7: aload_0
      //   8: aconst_null
      //   9: putfield buffer : Ljava/util/Collection;
      //   12: aload_0
      //   13: monitorexit
      //   14: aload_1
      //   15: ifnull -> 55
      //   18: aload_0
      //   19: getfield queue : Lio/reactivex/internal/fuseable/SimplePlainQueue;
      //   22: aload_1
      //   23: invokeinterface offer : (Ljava/lang/Object;)Z
      //   28: pop
      //   29: aload_0
      //   30: iconst_1
      //   31: putfield done : Z
      //   34: aload_0
      //   35: invokevirtual enter : ()Z
      //   38: ifeq -> 55
      //   41: aload_0
      //   42: getfield queue : Lio/reactivex/internal/fuseable/SimplePlainQueue;
      //   45: aload_0
      //   46: getfield downstream : Lio/reactivex/Observer;
      //   49: iconst_0
      //   50: aconst_null
      //   51: aload_0
      //   52: invokestatic drainLoop : (Lio/reactivex/internal/fuseable/SimplePlainQueue;Lio/reactivex/Observer;ZLio/reactivex/disposables/Disposable;Lio/reactivex/internal/util/ObservableQueueDrain;)V
      //   55: aload_0
      //   56: getfield timer : Ljava/util/concurrent/atomic/AtomicReference;
      //   59: invokestatic dispose : (Ljava/util/concurrent/atomic/AtomicReference;)Z
      //   62: pop
      //   63: return
      //   64: astore_1
      //   65: aload_0
      //   66: monitorexit
      //   67: aload_1
      //   68: athrow
      // Exception table:
      //   from	to	target	type
      //   2	14	64	finally
      //   65	67	64	finally
    }
    
    public void onError(Throwable param1Throwable) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: aconst_null
      //   4: putfield buffer : Ljava/util/Collection;
      //   7: aload_0
      //   8: monitorexit
      //   9: aload_0
      //   10: getfield downstream : Lio/reactivex/Observer;
      //   13: aload_1
      //   14: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   19: aload_0
      //   20: getfield timer : Ljava/util/concurrent/atomic/AtomicReference;
      //   23: invokestatic dispose : (Ljava/util/concurrent/atomic/AtomicReference;)Z
      //   26: pop
      //   27: return
      //   28: astore_1
      //   29: aload_0
      //   30: monitorexit
      //   31: aload_1
      //   32: athrow
      // Exception table:
      //   from	to	target	type
      //   2	9	28	finally
      //   29	31	28	finally
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
        this.upstream = param1Disposable;
        try {
          Collection collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
        } finally {
          param1Disposable = null;
          Exceptions.throwIfFatal((Throwable)param1Disposable);
          dispose();
        } 
      } 
    }
    
    public void run() {
      // Byte code:
      //   0: aload_0
      //   1: getfield bufferSupplier : Ljava/util/concurrent/Callable;
      //   4: invokeinterface call : ()Ljava/lang/Object;
      //   9: ldc 'The bufferSupplier returned a null buffer'
      //   11: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
      //   14: checkcast java/util/Collection
      //   17: astore_1
      //   18: aload_0
      //   19: monitorenter
      //   20: aload_0
      //   21: getfield buffer : Ljava/util/Collection;
      //   24: astore_2
      //   25: aload_2
      //   26: ifnull -> 34
      //   29: aload_0
      //   30: aload_1
      //   31: putfield buffer : Ljava/util/Collection;
      //   34: aload_0
      //   35: monitorexit
      //   36: aload_2
      //   37: ifnonnull -> 49
      //   40: aload_0
      //   41: getfield timer : Ljava/util/concurrent/atomic/AtomicReference;
      //   44: invokestatic dispose : (Ljava/util/concurrent/atomic/AtomicReference;)Z
      //   47: pop
      //   48: return
      //   49: aload_0
      //   50: aload_2
      //   51: iconst_0
      //   52: aload_0
      //   53: invokevirtual fastPathEmit : (Ljava/lang/Object;ZLio/reactivex/disposables/Disposable;)V
      //   56: return
      //   57: astore_1
      //   58: aload_0
      //   59: monitorexit
      //   60: aload_1
      //   61: athrow
      //   62: astore_1
      //   63: aload_1
      //   64: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
      //   67: aload_0
      //   68: getfield downstream : Lio/reactivex/Observer;
      //   71: aload_1
      //   72: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   77: aload_0
      //   78: invokevirtual dispose : ()V
      //   81: return
      // Exception table:
      //   from	to	target	type
      //   0	18	62	finally
      //   20	25	57	finally
      //   29	34	57	finally
      //   34	36	57	finally
      //   58	60	57	finally
    }
  }
  
  static final class BufferSkipBoundedObserver<T, U extends Collection<? super T>> extends QueueDrainObserver<T, U, U> implements Runnable, Disposable {
    final Callable<U> bufferSupplier;
    
    final List<U> buffers;
    
    final long timeskip;
    
    final long timespan;
    
    final TimeUnit unit;
    
    Disposable upstream;
    
    final Scheduler.Worker w;
    
    BufferSkipBoundedObserver(Observer<? super U> param1Observer, Callable<U> param1Callable, long param1Long1, long param1Long2, TimeUnit param1TimeUnit, Scheduler.Worker param1Worker) {
      super(param1Observer, (SimplePlainQueue)new MpscLinkedQueue());
      this.bufferSupplier = param1Callable;
      this.timespan = param1Long1;
      this.timeskip = param1Long2;
      this.unit = param1TimeUnit;
      this.w = param1Worker;
      this.buffers = new LinkedList<U>();
    }
    
    public void accept(Observer<? super U> param1Observer, U param1U) {
      param1Observer.onNext(param1U);
    }
    
    void clear() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield buffers : Ljava/util/List;
      //   6: invokeinterface clear : ()V
      //   11: aload_0
      //   12: monitorexit
      //   13: return
      //   14: astore_1
      //   15: aload_0
      //   16: monitorexit
      //   17: aload_1
      //   18: athrow
      // Exception table:
      //   from	to	target	type
      //   2	13	14	finally
      //   15	17	14	finally
    }
    
    public void dispose() {
      if (!this.cancelled) {
        this.cancelled = true;
        clear();
        this.upstream.dispose();
        this.w.dispose();
      } 
    }
    
    public boolean isDisposed() {
      return this.cancelled;
    }
    
    public void onComplete() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: new java/util/ArrayList
      //   5: astore_1
      //   6: aload_1
      //   7: aload_0
      //   8: getfield buffers : Ljava/util/List;
      //   11: invokespecial <init> : (Ljava/util/Collection;)V
      //   14: aload_0
      //   15: getfield buffers : Ljava/util/List;
      //   18: invokeinterface clear : ()V
      //   23: aload_0
      //   24: monitorexit
      //   25: aload_1
      //   26: invokeinterface iterator : ()Ljava/util/Iterator;
      //   31: astore_2
      //   32: aload_2
      //   33: invokeinterface hasNext : ()Z
      //   38: ifeq -> 65
      //   41: aload_2
      //   42: invokeinterface next : ()Ljava/lang/Object;
      //   47: checkcast java/util/Collection
      //   50: astore_1
      //   51: aload_0
      //   52: getfield queue : Lio/reactivex/internal/fuseable/SimplePlainQueue;
      //   55: aload_1
      //   56: invokeinterface offer : (Ljava/lang/Object;)Z
      //   61: pop
      //   62: goto -> 32
      //   65: aload_0
      //   66: iconst_1
      //   67: putfield done : Z
      //   70: aload_0
      //   71: invokevirtual enter : ()Z
      //   74: ifeq -> 94
      //   77: aload_0
      //   78: getfield queue : Lio/reactivex/internal/fuseable/SimplePlainQueue;
      //   81: aload_0
      //   82: getfield downstream : Lio/reactivex/Observer;
      //   85: iconst_0
      //   86: aload_0
      //   87: getfield w : Lio/reactivex/Scheduler$Worker;
      //   90: aload_0
      //   91: invokestatic drainLoop : (Lio/reactivex/internal/fuseable/SimplePlainQueue;Lio/reactivex/Observer;ZLio/reactivex/disposables/Disposable;Lio/reactivex/internal/util/ObservableQueueDrain;)V
      //   94: return
      //   95: astore_1
      //   96: aload_0
      //   97: monitorexit
      //   98: goto -> 103
      //   101: aload_1
      //   102: athrow
      //   103: goto -> 101
      // Exception table:
      //   from	to	target	type
      //   2	25	95	finally
      //   96	98	95	finally
    }
    
    public void onError(Throwable param1Throwable) {
      this.done = true;
      clear();
      this.downstream.onError(param1Throwable);
      this.w.dispose();
    }
    
    public void onNext(T param1T) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield buffers : Ljava/util/List;
      //   6: invokeinterface iterator : ()Ljava/util/Iterator;
      //   11: astore_2
      //   12: aload_2
      //   13: invokeinterface hasNext : ()Z
      //   18: ifeq -> 40
      //   21: aload_2
      //   22: invokeinterface next : ()Ljava/lang/Object;
      //   27: checkcast java/util/Collection
      //   30: aload_1
      //   31: invokeinterface add : (Ljava/lang/Object;)Z
      //   36: pop
      //   37: goto -> 12
      //   40: aload_0
      //   41: monitorexit
      //   42: return
      //   43: astore_1
      //   44: aload_0
      //   45: monitorexit
      //   46: goto -> 51
      //   49: aload_1
      //   50: athrow
      //   51: goto -> 49
      // Exception table:
      //   from	to	target	type
      //   2	12	43	finally
      //   12	37	43	finally
      //   40	42	43	finally
      //   44	46	43	finally
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        Scheduler.Worker worker;
        this.upstream = param1Disposable;
        try {
          Collection collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
          this.buffers.add((U)collection);
          this.downstream.onSubscribe(this);
        } finally {
          Exception exception = null;
          Exceptions.throwIfFatal(exception);
          worker.dispose();
          EmptyDisposable.error(exception, this.downstream);
        } 
      } 
    }
    
    public void run() {
      // Byte code:
      //   0: aload_0
      //   1: getfield cancelled : Z
      //   4: ifeq -> 8
      //   7: return
      //   8: aload_0
      //   9: getfield bufferSupplier : Ljava/util/concurrent/Callable;
      //   12: invokeinterface call : ()Ljava/lang/Object;
      //   17: ldc 'The bufferSupplier returned a null buffer'
      //   19: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
      //   22: checkcast java/util/Collection
      //   25: astore_1
      //   26: aload_0
      //   27: monitorenter
      //   28: aload_0
      //   29: getfield cancelled : Z
      //   32: ifeq -> 38
      //   35: aload_0
      //   36: monitorexit
      //   37: return
      //   38: aload_0
      //   39: getfield buffers : Ljava/util/List;
      //   42: aload_1
      //   43: invokeinterface add : (Ljava/lang/Object;)Z
      //   48: pop
      //   49: aload_0
      //   50: monitorexit
      //   51: aload_0
      //   52: getfield w : Lio/reactivex/Scheduler$Worker;
      //   55: new io/reactivex/internal/operators/observable/ObservableBufferTimed$BufferSkipBoundedObserver$RemoveFromBuffer
      //   58: dup
      //   59: aload_0
      //   60: aload_1
      //   61: invokespecial <init> : (Lio/reactivex/internal/operators/observable/ObservableBufferTimed$BufferSkipBoundedObserver;Ljava/util/Collection;)V
      //   64: aload_0
      //   65: getfield timespan : J
      //   68: aload_0
      //   69: getfield unit : Ljava/util/concurrent/TimeUnit;
      //   72: invokevirtual schedule : (Ljava/lang/Runnable;JLjava/util/concurrent/TimeUnit;)Lio/reactivex/disposables/Disposable;
      //   75: pop
      //   76: return
      //   77: astore_1
      //   78: aload_0
      //   79: monitorexit
      //   80: aload_1
      //   81: athrow
      //   82: astore_1
      //   83: aload_1
      //   84: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
      //   87: aload_0
      //   88: getfield downstream : Lio/reactivex/Observer;
      //   91: aload_1
      //   92: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   97: aload_0
      //   98: invokevirtual dispose : ()V
      //   101: return
      // Exception table:
      //   from	to	target	type
      //   8	26	82	finally
      //   28	37	77	finally
      //   38	51	77	finally
      //   78	80	77	finally
    }
    
    final class RemoveFromBuffer implements Runnable {
      private final U b;
      
      RemoveFromBuffer(U param2U) {
        this.b = param2U;
      }
      
      public void run() {
        synchronized (ObservableBufferTimed.BufferSkipBoundedObserver.this) {
          ObservableBufferTimed.BufferSkipBoundedObserver.this.buffers.remove(this.b);
          ObservableBufferTimed.BufferSkipBoundedObserver bufferSkipBoundedObserver = ObservableBufferTimed.BufferSkipBoundedObserver.this;
          bufferSkipBoundedObserver.fastPathOrderedEmit(this.b, false, (Disposable)bufferSkipBoundedObserver.w);
          return;
        } 
      }
    }
    
    final class RemoveFromBufferEmit implements Runnable {
      private final U buffer;
      
      RemoveFromBufferEmit(U param2U) {
        this.buffer = param2U;
      }
      
      public void run() {
        synchronized (ObservableBufferTimed.BufferSkipBoundedObserver.this) {
          ObservableBufferTimed.BufferSkipBoundedObserver.this.buffers.remove(this.buffer);
          null = ObservableBufferTimed.BufferSkipBoundedObserver.this;
          null.fastPathOrderedEmit(this.buffer, false, (Disposable)null.w);
          return;
        } 
      }
    }
  }
  
  final class RemoveFromBuffer implements Runnable {
    private final U b;
    
    RemoveFromBuffer(U param1U) {
      this.b = param1U;
    }
    
    public void run() {
      synchronized (this.this$0) {
        this.this$0.buffers.remove(this.b);
        ObservableBufferTimed.BufferSkipBoundedObserver bufferSkipBoundedObserver = this.this$0;
        bufferSkipBoundedObserver.fastPathOrderedEmit(this.b, false, (Disposable)bufferSkipBoundedObserver.w);
        return;
      } 
    }
  }
  
  final class RemoveFromBufferEmit implements Runnable {
    private final U buffer;
    
    RemoveFromBufferEmit(U param1U) {
      this.buffer = param1U;
    }
    
    public void run() {
      synchronized (this.this$0) {
        this.this$0.buffers.remove(this.buffer);
        null = this.this$0;
        null.fastPathOrderedEmit(this.buffer, false, (Disposable)null.w);
        return;
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableBufferTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */