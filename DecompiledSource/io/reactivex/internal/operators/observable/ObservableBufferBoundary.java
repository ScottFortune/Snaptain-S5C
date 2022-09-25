package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableBufferBoundary<T, U extends Collection<? super T>, Open, Close> extends AbstractObservableWithUpstream<T, U> {
  final Function<? super Open, ? extends ObservableSource<? extends Close>> bufferClose;
  
  final ObservableSource<? extends Open> bufferOpen;
  
  final Callable<U> bufferSupplier;
  
  public ObservableBufferBoundary(ObservableSource<T> paramObservableSource, ObservableSource<? extends Open> paramObservableSource1, Function<? super Open, ? extends ObservableSource<? extends Close>> paramFunction, Callable<U> paramCallable) {
    super(paramObservableSource);
    this.bufferOpen = paramObservableSource1;
    this.bufferClose = paramFunction;
    this.bufferSupplier = paramCallable;
  }
  
  protected void subscribeActual(Observer<? super U> paramObserver) {
    BufferBoundaryObserver<Object, U, Open, Close> bufferBoundaryObserver = new BufferBoundaryObserver<Object, U, Open, Close>(paramObserver, this.bufferOpen, this.bufferClose, this.bufferSupplier);
    paramObserver.onSubscribe(bufferBoundaryObserver);
    this.source.subscribe(bufferBoundaryObserver);
  }
  
  static final class BufferBoundaryObserver<T, C extends Collection<? super T>, Open, Close> extends AtomicInteger implements Observer<T>, Disposable {
    private static final long serialVersionUID = -8466418554264089604L;
    
    final Function<? super Open, ? extends ObservableSource<? extends Close>> bufferClose;
    
    final ObservableSource<? extends Open> bufferOpen;
    
    final Callable<C> bufferSupplier;
    
    Map<Long, C> buffers;
    
    volatile boolean cancelled;
    
    volatile boolean done;
    
    final Observer<? super C> downstream;
    
    final AtomicThrowable errors;
    
    long index;
    
    final CompositeDisposable observers;
    
    final SpscLinkedArrayQueue<C> queue;
    
    final AtomicReference<Disposable> upstream;
    
    BufferBoundaryObserver(Observer<? super C> param1Observer, ObservableSource<? extends Open> param1ObservableSource, Function<? super Open, ? extends ObservableSource<? extends Close>> param1Function, Callable<C> param1Callable) {
      this.downstream = param1Observer;
      this.bufferSupplier = param1Callable;
      this.bufferOpen = param1ObservableSource;
      this.bufferClose = param1Function;
      this.queue = new SpscLinkedArrayQueue(Observable.bufferSize());
      this.observers = new CompositeDisposable();
      this.upstream = new AtomicReference<Disposable>();
      this.buffers = new LinkedHashMap<Long, C>();
      this.errors = new AtomicThrowable();
    }
    
    void boundaryError(Disposable param1Disposable, Throwable param1Throwable) {
      DisposableHelper.dispose(this.upstream);
      this.observers.delete(param1Disposable);
      onError(param1Throwable);
    }
    
    void close(ObservableBufferBoundary.BufferCloseObserver<T, C> param1BufferCloseObserver, long param1Long) {
      // Byte code:
      //   0: aload_0
      //   1: getfield observers : Lio/reactivex/disposables/CompositeDisposable;
      //   4: aload_1
      //   5: invokevirtual delete : (Lio/reactivex/disposables/Disposable;)Z
      //   8: pop
      //   9: aload_0
      //   10: getfield observers : Lio/reactivex/disposables/CompositeDisposable;
      //   13: invokevirtual size : ()I
      //   16: ifne -> 33
      //   19: aload_0
      //   20: getfield upstream : Ljava/util/concurrent/atomic/AtomicReference;
      //   23: invokestatic dispose : (Ljava/util/concurrent/atomic/AtomicReference;)Z
      //   26: pop
      //   27: iconst_1
      //   28: istore #4
      //   30: goto -> 36
      //   33: iconst_0
      //   34: istore #4
      //   36: aload_0
      //   37: monitorenter
      //   38: aload_0
      //   39: getfield buffers : Ljava/util/Map;
      //   42: ifnonnull -> 48
      //   45: aload_0
      //   46: monitorexit
      //   47: return
      //   48: aload_0
      //   49: getfield queue : Lio/reactivex/internal/queue/SpscLinkedArrayQueue;
      //   52: aload_0
      //   53: getfield buffers : Ljava/util/Map;
      //   56: lload_2
      //   57: invokestatic valueOf : (J)Ljava/lang/Long;
      //   60: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
      //   65: invokevirtual offer : (Ljava/lang/Object;)Z
      //   68: pop
      //   69: aload_0
      //   70: monitorexit
      //   71: iload #4
      //   73: ifeq -> 81
      //   76: aload_0
      //   77: iconst_1
      //   78: putfield done : Z
      //   81: aload_0
      //   82: invokevirtual drain : ()V
      //   85: return
      //   86: astore_1
      //   87: aload_0
      //   88: monitorexit
      //   89: aload_1
      //   90: athrow
      // Exception table:
      //   from	to	target	type
      //   38	47	86	finally
      //   48	71	86	finally
      //   87	89	86	finally
    }
    
    public void dispose() {
      // Byte code:
      //   0: aload_0
      //   1: getfield upstream : Ljava/util/concurrent/atomic/AtomicReference;
      //   4: invokestatic dispose : (Ljava/util/concurrent/atomic/AtomicReference;)Z
      //   7: ifeq -> 53
      //   10: aload_0
      //   11: iconst_1
      //   12: putfield cancelled : Z
      //   15: aload_0
      //   16: getfield observers : Lio/reactivex/disposables/CompositeDisposable;
      //   19: invokevirtual dispose : ()V
      //   22: aload_0
      //   23: monitorenter
      //   24: aload_0
      //   25: aconst_null
      //   26: putfield buffers : Ljava/util/Map;
      //   29: aload_0
      //   30: monitorexit
      //   31: aload_0
      //   32: invokevirtual getAndIncrement : ()I
      //   35: ifeq -> 53
      //   38: aload_0
      //   39: getfield queue : Lio/reactivex/internal/queue/SpscLinkedArrayQueue;
      //   42: invokevirtual clear : ()V
      //   45: goto -> 53
      //   48: astore_1
      //   49: aload_0
      //   50: monitorexit
      //   51: aload_1
      //   52: athrow
      //   53: return
      // Exception table:
      //   from	to	target	type
      //   24	31	48	finally
      //   49	51	48	finally
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      Observer<? super C> observer = this.downstream;
      SpscLinkedArrayQueue<C> spscLinkedArrayQueue = this.queue;
      int i = 1;
      while (true) {
        int j;
        if (this.cancelled) {
          spscLinkedArrayQueue.clear();
          return;
        } 
        boolean bool = this.done;
        if (bool && this.errors.get() != null) {
          spscLinkedArrayQueue.clear();
          observer.onError(this.errors.terminate());
          return;
        } 
        Collection collection = (Collection)spscLinkedArrayQueue.poll();
        if (collection == null) {
          j = 1;
        } else {
          j = 0;
        } 
        if (bool && j) {
          observer.onComplete();
          return;
        } 
        if (j) {
          j = addAndGet(-i);
          i = j;
          if (j == 0)
            return; 
          continue;
        } 
        observer.onNext(collection);
      } 
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(this.upstream.get());
    }
    
    public void onComplete() {
      // Byte code:
      //   0: aload_0
      //   1: getfield observers : Lio/reactivex/disposables/CompositeDisposable;
      //   4: invokevirtual dispose : ()V
      //   7: aload_0
      //   8: monitorenter
      //   9: aload_0
      //   10: getfield buffers : Ljava/util/Map;
      //   13: astore_1
      //   14: aload_1
      //   15: ifnonnull -> 21
      //   18: aload_0
      //   19: monitorexit
      //   20: return
      //   21: aload_1
      //   22: invokeinterface values : ()Ljava/util/Collection;
      //   27: invokeinterface iterator : ()Ljava/util/Iterator;
      //   32: astore_1
      //   33: aload_1
      //   34: invokeinterface hasNext : ()Z
      //   39: ifeq -> 64
      //   42: aload_1
      //   43: invokeinterface next : ()Ljava/lang/Object;
      //   48: checkcast java/util/Collection
      //   51: astore_2
      //   52: aload_0
      //   53: getfield queue : Lio/reactivex/internal/queue/SpscLinkedArrayQueue;
      //   56: aload_2
      //   57: invokevirtual offer : (Ljava/lang/Object;)Z
      //   60: pop
      //   61: goto -> 33
      //   64: aload_0
      //   65: aconst_null
      //   66: putfield buffers : Ljava/util/Map;
      //   69: aload_0
      //   70: monitorexit
      //   71: aload_0
      //   72: iconst_1
      //   73: putfield done : Z
      //   76: aload_0
      //   77: invokevirtual drain : ()V
      //   80: return
      //   81: astore_1
      //   82: aload_0
      //   83: monitorexit
      //   84: goto -> 89
      //   87: aload_1
      //   88: athrow
      //   89: goto -> 87
      // Exception table:
      //   from	to	target	type
      //   9	14	81	finally
      //   18	20	81	finally
      //   21	33	81	finally
      //   33	61	81	finally
      //   64	71	81	finally
      //   82	84	81	finally
    }
    
    public void onError(Throwable param1Throwable) {
      // Byte code:
      //   0: aload_0
      //   1: getfield errors : Lio/reactivex/internal/util/AtomicThrowable;
      //   4: aload_1
      //   5: invokevirtual addThrowable : (Ljava/lang/Throwable;)Z
      //   8: ifeq -> 44
      //   11: aload_0
      //   12: getfield observers : Lio/reactivex/disposables/CompositeDisposable;
      //   15: invokevirtual dispose : ()V
      //   18: aload_0
      //   19: monitorenter
      //   20: aload_0
      //   21: aconst_null
      //   22: putfield buffers : Ljava/util/Map;
      //   25: aload_0
      //   26: monitorexit
      //   27: aload_0
      //   28: iconst_1
      //   29: putfield done : Z
      //   32: aload_0
      //   33: invokevirtual drain : ()V
      //   36: goto -> 48
      //   39: astore_1
      //   40: aload_0
      //   41: monitorexit
      //   42: aload_1
      //   43: athrow
      //   44: aload_1
      //   45: invokestatic onError : (Ljava/lang/Throwable;)V
      //   48: return
      // Exception table:
      //   from	to	target	type
      //   20	27	39	finally
      //   40	42	39	finally
    }
    
    public void onNext(T param1T) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield buffers : Ljava/util/Map;
      //   6: astore_2
      //   7: aload_2
      //   8: ifnonnull -> 14
      //   11: aload_0
      //   12: monitorexit
      //   13: return
      //   14: aload_2
      //   15: invokeinterface values : ()Ljava/util/Collection;
      //   20: invokeinterface iterator : ()Ljava/util/Iterator;
      //   25: astore_2
      //   26: aload_2
      //   27: invokeinterface hasNext : ()Z
      //   32: ifeq -> 54
      //   35: aload_2
      //   36: invokeinterface next : ()Ljava/lang/Object;
      //   41: checkcast java/util/Collection
      //   44: aload_1
      //   45: invokeinterface add : (Ljava/lang/Object;)Z
      //   50: pop
      //   51: goto -> 26
      //   54: aload_0
      //   55: monitorexit
      //   56: return
      //   57: astore_1
      //   58: aload_0
      //   59: monitorexit
      //   60: goto -> 65
      //   63: aload_1
      //   64: athrow
      //   65: goto -> 63
      // Exception table:
      //   from	to	target	type
      //   2	7	57	finally
      //   11	13	57	finally
      //   14	26	57	finally
      //   26	51	57	finally
      //   54	56	57	finally
      //   58	60	57	finally
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.setOnce(this.upstream, param1Disposable)) {
        param1Disposable = new BufferOpenObserver(this);
        this.observers.add(param1Disposable);
        this.bufferOpen.subscribe((Observer)param1Disposable);
      } 
    }
    
    void open(Open param1Open) {
      // Byte code:
      //   0: aload_0
      //   1: getfield bufferSupplier : Ljava/util/concurrent/Callable;
      //   4: invokeinterface call : ()Ljava/lang/Object;
      //   9: ldc 'The bufferSupplier returned a null Collection'
      //   11: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
      //   14: checkcast java/util/Collection
      //   17: astore_2
      //   18: aload_0
      //   19: getfield bufferClose : Lio/reactivex/functions/Function;
      //   22: aload_1
      //   23: invokeinterface apply : (Ljava/lang/Object;)Ljava/lang/Object;
      //   28: ldc 'The bufferClose returned a null ObservableSource'
      //   30: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
      //   33: checkcast io/reactivex/ObservableSource
      //   36: astore_1
      //   37: aload_0
      //   38: getfield index : J
      //   41: lstore_3
      //   42: aload_0
      //   43: lconst_1
      //   44: lload_3
      //   45: ladd
      //   46: putfield index : J
      //   49: aload_0
      //   50: monitorenter
      //   51: aload_0
      //   52: getfield buffers : Ljava/util/Map;
      //   55: astore #5
      //   57: aload #5
      //   59: ifnonnull -> 65
      //   62: aload_0
      //   63: monitorexit
      //   64: return
      //   65: aload #5
      //   67: lload_3
      //   68: invokestatic valueOf : (J)Ljava/lang/Long;
      //   71: aload_2
      //   72: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
      //   77: pop
      //   78: aload_0
      //   79: monitorexit
      //   80: new io/reactivex/internal/operators/observable/ObservableBufferBoundary$BufferCloseObserver
      //   83: dup
      //   84: aload_0
      //   85: lload_3
      //   86: invokespecial <init> : (Lio/reactivex/internal/operators/observable/ObservableBufferBoundary$BufferBoundaryObserver;J)V
      //   89: astore_2
      //   90: aload_0
      //   91: getfield observers : Lio/reactivex/disposables/CompositeDisposable;
      //   94: aload_2
      //   95: invokevirtual add : (Lio/reactivex/disposables/Disposable;)Z
      //   98: pop
      //   99: aload_1
      //   100: aload_2
      //   101: invokeinterface subscribe : (Lio/reactivex/Observer;)V
      //   106: return
      //   107: astore_1
      //   108: aload_0
      //   109: monitorexit
      //   110: aload_1
      //   111: athrow
      //   112: astore_1
      //   113: aload_1
      //   114: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
      //   117: aload_0
      //   118: getfield upstream : Ljava/util/concurrent/atomic/AtomicReference;
      //   121: invokestatic dispose : (Ljava/util/concurrent/atomic/AtomicReference;)Z
      //   124: pop
      //   125: aload_0
      //   126: aload_1
      //   127: invokevirtual onError : (Ljava/lang/Throwable;)V
      //   130: return
      // Exception table:
      //   from	to	target	type
      //   0	37	112	finally
      //   51	57	107	finally
      //   62	64	107	finally
      //   65	80	107	finally
      //   108	110	107	finally
    }
    
    void openComplete(BufferOpenObserver<Open> param1BufferOpenObserver) {
      this.observers.delete(param1BufferOpenObserver);
      if (this.observers.size() == 0) {
        DisposableHelper.dispose(this.upstream);
        this.done = true;
        drain();
      } 
    }
    
    static final class BufferOpenObserver<Open> extends AtomicReference<Disposable> implements Observer<Open>, Disposable {
      private static final long serialVersionUID = -8498650778633225126L;
      
      final ObservableBufferBoundary.BufferBoundaryObserver<?, ?, Open, ?> parent;
      
      BufferOpenObserver(ObservableBufferBoundary.BufferBoundaryObserver<?, ?, Open, ?> param2BufferBoundaryObserver) {
        this.parent = param2BufferBoundaryObserver;
      }
      
      public void dispose() {
        DisposableHelper.dispose(this);
      }
      
      public boolean isDisposed() {
        boolean bool;
        if (get() == DisposableHelper.DISPOSED) {
          bool = true;
        } else {
          bool = false;
        } 
        return bool;
      }
      
      public void onComplete() {
        lazySet((Disposable)DisposableHelper.DISPOSED);
        this.parent.openComplete(this);
      }
      
      public void onError(Throwable param2Throwable) {
        lazySet((Disposable)DisposableHelper.DISPOSED);
        this.parent.boundaryError(this, param2Throwable);
      }
      
      public void onNext(Open param2Open) {
        this.parent.open(param2Open);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.setOnce(this, param2Disposable);
      }
    }
  }
  
  static final class BufferOpenObserver<Open> extends AtomicReference<Disposable> implements Observer<Open>, Disposable {
    private static final long serialVersionUID = -8498650778633225126L;
    
    final ObservableBufferBoundary.BufferBoundaryObserver<?, ?, Open, ?> parent;
    
    BufferOpenObserver(ObservableBufferBoundary.BufferBoundaryObserver<?, ?, Open, ?> param1BufferBoundaryObserver) {
      this.parent = param1BufferBoundaryObserver;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (get() == DisposableHelper.DISPOSED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onComplete() {
      lazySet((Disposable)DisposableHelper.DISPOSED);
      this.parent.openComplete(this);
    }
    
    public void onError(Throwable param1Throwable) {
      lazySet((Disposable)DisposableHelper.DISPOSED);
      this.parent.boundaryError(this, param1Throwable);
    }
    
    public void onNext(Open param1Open) {
      this.parent.open(param1Open);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
  }
  
  static final class BufferCloseObserver<T, C extends Collection<? super T>> extends AtomicReference<Disposable> implements Observer<Object>, Disposable {
    private static final long serialVersionUID = -8498650778633225126L;
    
    final long index;
    
    final ObservableBufferBoundary.BufferBoundaryObserver<T, C, ?, ?> parent;
    
    BufferCloseObserver(ObservableBufferBoundary.BufferBoundaryObserver<T, C, ?, ?> param1BufferBoundaryObserver, long param1Long) {
      this.parent = param1BufferBoundaryObserver;
      this.index = param1Long;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (get() == DisposableHelper.DISPOSED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onComplete() {
      if (get() != DisposableHelper.DISPOSED) {
        lazySet((Disposable)DisposableHelper.DISPOSED);
        this.parent.close(this, this.index);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (get() != DisposableHelper.DISPOSED) {
        lazySet((Disposable)DisposableHelper.DISPOSED);
        this.parent.boundaryError(this, param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(Object param1Object) {
      param1Object = get();
      if (param1Object != DisposableHelper.DISPOSED) {
        lazySet((Disposable)DisposableHelper.DISPOSED);
        param1Object.dispose();
        this.parent.close(this, this.index);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableBufferBoundary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */