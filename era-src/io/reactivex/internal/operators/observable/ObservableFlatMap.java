package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.QueueDisposable;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableFlatMap<T, U> extends AbstractObservableWithUpstream<T, U> {
  final int bufferSize;
  
  final boolean delayErrors;
  
  final Function<? super T, ? extends ObservableSource<? extends U>> mapper;
  
  final int maxConcurrency;
  
  public ObservableFlatMap(ObservableSource<T> paramObservableSource, Function<? super T, ? extends ObservableSource<? extends U>> paramFunction, boolean paramBoolean, int paramInt1, int paramInt2) {
    super(paramObservableSource);
    this.mapper = paramFunction;
    this.delayErrors = paramBoolean;
    this.maxConcurrency = paramInt1;
    this.bufferSize = paramInt2;
  }
  
  public void subscribeActual(Observer<? super U> paramObserver) {
    if (ObservableScalarXMap.tryScalarXMapSubscribe(this.source, paramObserver, this.mapper))
      return; 
    this.source.subscribe(new MergeObserver<T, U>(paramObserver, this.mapper, this.delayErrors, this.maxConcurrency, this.bufferSize));
  }
  
  static final class InnerObserver<T, U> extends AtomicReference<Disposable> implements Observer<U> {
    private static final long serialVersionUID = -4606175640614850599L;
    
    volatile boolean done;
    
    int fusionMode;
    
    final long id;
    
    final ObservableFlatMap.MergeObserver<T, U> parent;
    
    volatile SimpleQueue<U> queue;
    
    InnerObserver(ObservableFlatMap.MergeObserver<T, U> param1MergeObserver, long param1Long) {
      this.id = param1Long;
      this.parent = param1MergeObserver;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public void onComplete() {
      this.done = true;
      this.parent.drain();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.parent.errors.addThrowable(param1Throwable)) {
        if (!this.parent.delayErrors)
          this.parent.disposeAll(); 
        this.done = true;
        this.parent.drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(U param1U) {
      if (this.fusionMode == 0) {
        this.parent.tryEmit(param1U, this);
      } else {
        this.parent.drain();
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.setOnce(this, param1Disposable) && param1Disposable instanceof QueueDisposable) {
        QueueDisposable queueDisposable = (QueueDisposable)param1Disposable;
        int i = queueDisposable.requestFusion(7);
        if (i == 1) {
          this.fusionMode = i;
          this.queue = (SimpleQueue<U>)queueDisposable;
          this.done = true;
          this.parent.drain();
          return;
        } 
        if (i == 2) {
          this.fusionMode = i;
          this.queue = (SimpleQueue<U>)queueDisposable;
        } 
      } 
    }
  }
  
  static final class MergeObserver<T, U> extends AtomicInteger implements Disposable, Observer<T> {
    static final ObservableFlatMap.InnerObserver<?, ?>[] CANCELLED = (ObservableFlatMap.InnerObserver<?, ?>[])new ObservableFlatMap.InnerObserver[0];
    
    static final ObservableFlatMap.InnerObserver<?, ?>[] EMPTY = (ObservableFlatMap.InnerObserver<?, ?>[])new ObservableFlatMap.InnerObserver[0];
    
    private static final long serialVersionUID = -2117620485640801370L;
    
    final int bufferSize;
    
    volatile boolean cancelled;
    
    final boolean delayErrors;
    
    volatile boolean done;
    
    final Observer<? super U> downstream;
    
    final AtomicThrowable errors = new AtomicThrowable();
    
    long lastId;
    
    int lastIndex;
    
    final Function<? super T, ? extends ObservableSource<? extends U>> mapper;
    
    final int maxConcurrency;
    
    final AtomicReference<ObservableFlatMap.InnerObserver<?, ?>[]> observers;
    
    volatile SimplePlainQueue<U> queue;
    
    Queue<ObservableSource<? extends U>> sources;
    
    long uniqueId;
    
    Disposable upstream;
    
    int wip;
    
    static {
    
    }
    
    MergeObserver(Observer<? super U> param1Observer, Function<? super T, ? extends ObservableSource<? extends U>> param1Function, boolean param1Boolean, int param1Int1, int param1Int2) {
      this.downstream = param1Observer;
      this.mapper = param1Function;
      this.delayErrors = param1Boolean;
      this.maxConcurrency = param1Int1;
      this.bufferSize = param1Int2;
      if (param1Int1 != Integer.MAX_VALUE)
        this.sources = new ArrayDeque<ObservableSource<? extends U>>(param1Int1); 
      this.observers = (AtomicReference)new AtomicReference<ObservableFlatMap.InnerObserver<?, ?>>(EMPTY);
    }
    
    boolean addInner(ObservableFlatMap.InnerObserver<T, U> param1InnerObserver) {
      while (true) {
        ObservableFlatMap.InnerObserver[] arrayOfInnerObserver1 = (ObservableFlatMap.InnerObserver[])this.observers.get();
        if (arrayOfInnerObserver1 == CANCELLED) {
          param1InnerObserver.dispose();
          return false;
        } 
        int i = arrayOfInnerObserver1.length;
        ObservableFlatMap.InnerObserver[] arrayOfInnerObserver2 = new ObservableFlatMap.InnerObserver[i + 1];
        System.arraycopy(arrayOfInnerObserver1, 0, arrayOfInnerObserver2, 0, i);
        arrayOfInnerObserver2[i] = param1InnerObserver;
        if (this.observers.compareAndSet(arrayOfInnerObserver1, arrayOfInnerObserver2))
          return true; 
      } 
    }
    
    boolean checkTerminate() {
      if (this.cancelled)
        return true; 
      Throwable throwable = (Throwable)this.errors.get();
      if (!this.delayErrors && throwable != null) {
        disposeAll();
        throwable = this.errors.terminate();
        if (throwable != ExceptionHelper.TERMINATED)
          this.downstream.onError(throwable); 
        return true;
      } 
      return false;
    }
    
    public void dispose() {
      if (!this.cancelled) {
        this.cancelled = true;
        if (disposeAll()) {
          Throwable throwable = this.errors.terminate();
          if (throwable != null && throwable != ExceptionHelper.TERMINATED)
            RxJavaPlugins.onError(throwable); 
        } 
      } 
    }
    
    boolean disposeAll() {
      this.upstream.dispose();
      ObservableFlatMap.InnerObserver[] arrayOfInnerObserver = (ObservableFlatMap.InnerObserver[])this.observers.get();
      ObservableFlatMap.InnerObserver<?, ?>[] arrayOfInnerObserver1 = CANCELLED;
      byte b = 0;
      if (arrayOfInnerObserver != arrayOfInnerObserver1) {
        arrayOfInnerObserver = (ObservableFlatMap.InnerObserver[])this.observers.getAndSet(arrayOfInnerObserver1);
        if (arrayOfInnerObserver != CANCELLED) {
          int i = arrayOfInnerObserver.length;
          while (b < i) {
            arrayOfInnerObserver[b].dispose();
            b++;
          } 
          return true;
        } 
      } 
      return false;
    }
    
    void drain() {
      if (getAndIncrement() == 0)
        drainLoop(); 
    }
    
    void drainLoop() {
      // Byte code:
      //   0: aload_0
      //   1: getfield downstream : Lio/reactivex/Observer;
      //   4: astore_1
      //   5: iconst_1
      //   6: istore_2
      //   7: aload_0
      //   8: invokevirtual checkTerminate : ()Z
      //   11: ifeq -> 15
      //   14: return
      //   15: aload_0
      //   16: getfield queue : Lio/reactivex/internal/fuseable/SimplePlainQueue;
      //   19: astore_3
      //   20: aload_3
      //   21: ifnull -> 59
      //   24: aload_0
      //   25: invokevirtual checkTerminate : ()Z
      //   28: ifeq -> 32
      //   31: return
      //   32: aload_3
      //   33: invokeinterface poll : ()Ljava/lang/Object;
      //   38: astore #4
      //   40: aload #4
      //   42: ifnonnull -> 48
      //   45: goto -> 59
      //   48: aload_1
      //   49: aload #4
      //   51: invokeinterface onNext : (Ljava/lang/Object;)V
      //   56: goto -> 24
      //   59: aload_0
      //   60: getfield done : Z
      //   63: istore #5
      //   65: aload_0
      //   66: getfield queue : Lio/reactivex/internal/fuseable/SimplePlainQueue;
      //   69: astore_3
      //   70: aload_0
      //   71: getfield observers : Ljava/util/concurrent/atomic/AtomicReference;
      //   74: invokevirtual get : ()Ljava/lang/Object;
      //   77: checkcast [Lio/reactivex/internal/operators/observable/ObservableFlatMap$InnerObserver;
      //   80: astore #4
      //   82: aload #4
      //   84: arraylength
      //   85: istore #6
      //   87: aload_0
      //   88: getfield maxConcurrency : I
      //   91: ldc 2147483647
      //   93: if_icmpeq -> 119
      //   96: aload_0
      //   97: monitorenter
      //   98: aload_0
      //   99: getfield sources : Ljava/util/Queue;
      //   102: invokeinterface size : ()I
      //   107: istore #7
      //   109: aload_0
      //   110: monitorexit
      //   111: goto -> 122
      //   114: astore_1
      //   115: aload_0
      //   116: monitorexit
      //   117: aload_1
      //   118: athrow
      //   119: iconst_0
      //   120: istore #7
      //   122: iload #5
      //   124: ifeq -> 190
      //   127: aload_3
      //   128: ifnull -> 140
      //   131: aload_3
      //   132: invokeinterface isEmpty : ()Z
      //   137: ifeq -> 190
      //   140: iload #6
      //   142: ifne -> 190
      //   145: iload #7
      //   147: ifne -> 190
      //   150: aload_0
      //   151: getfield errors : Lio/reactivex/internal/util/AtomicThrowable;
      //   154: invokevirtual terminate : ()Ljava/lang/Throwable;
      //   157: astore #4
      //   159: aload #4
      //   161: getstatic io/reactivex/internal/util/ExceptionHelper.TERMINATED : Ljava/lang/Throwable;
      //   164: if_acmpeq -> 189
      //   167: aload #4
      //   169: ifnonnull -> 181
      //   172: aload_1
      //   173: invokeinterface onComplete : ()V
      //   178: goto -> 189
      //   181: aload_1
      //   182: aload #4
      //   184: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   189: return
      //   190: iload #6
      //   192: ifeq -> 570
      //   195: aload_0
      //   196: getfield lastId : J
      //   199: lstore #8
      //   201: aload_0
      //   202: getfield lastIndex : I
      //   205: istore #10
      //   207: iload #6
      //   209: iload #10
      //   211: if_icmple -> 232
      //   214: iload #10
      //   216: istore #7
      //   218: aload #4
      //   220: iload #10
      //   222: aaload
      //   223: getfield id : J
      //   226: lload #8
      //   228: lcmp
      //   229: ifeq -> 317
      //   232: iload #10
      //   234: istore #7
      //   236: iload #6
      //   238: iload #10
      //   240: if_icmpgt -> 246
      //   243: iconst_0
      //   244: istore #7
      //   246: iconst_0
      //   247: istore #10
      //   249: iload #10
      //   251: iload #6
      //   253: if_icmpge -> 299
      //   256: aload #4
      //   258: iload #7
      //   260: aaload
      //   261: getfield id : J
      //   264: lload #8
      //   266: lcmp
      //   267: ifne -> 273
      //   270: goto -> 299
      //   273: iload #7
      //   275: iconst_1
      //   276: iadd
      //   277: istore #11
      //   279: iload #11
      //   281: istore #7
      //   283: iload #11
      //   285: iload #6
      //   287: if_icmpne -> 293
      //   290: iconst_0
      //   291: istore #7
      //   293: iinc #10, 1
      //   296: goto -> 249
      //   299: aload_0
      //   300: iload #7
      //   302: putfield lastIndex : I
      //   305: aload_0
      //   306: aload #4
      //   308: iload #7
      //   310: aaload
      //   311: getfield id : J
      //   314: putfield lastId : J
      //   317: iconst_0
      //   318: istore #12
      //   320: iconst_0
      //   321: istore #10
      //   323: iload #12
      //   325: iload #6
      //   327: if_icmpge -> 549
      //   330: aload_0
      //   331: invokevirtual checkTerminate : ()Z
      //   334: ifeq -> 338
      //   337: return
      //   338: aload #4
      //   340: iload #7
      //   342: aaload
      //   343: astore_3
      //   344: aload_3
      //   345: getfield queue : Lio/reactivex/internal/fuseable/SimpleQueue;
      //   348: astore #13
      //   350: aload #13
      //   352: ifnull -> 456
      //   355: aload #13
      //   357: invokeinterface poll : ()Ljava/lang/Object;
      //   362: astore #14
      //   364: aload #14
      //   366: ifnonnull -> 372
      //   369: goto -> 456
      //   372: aload_1
      //   373: aload #14
      //   375: invokeinterface onNext : (Ljava/lang/Object;)V
      //   380: aload_0
      //   381: invokevirtual checkTerminate : ()Z
      //   384: ifeq -> 355
      //   387: return
      //   388: astore #13
      //   390: aload #13
      //   392: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
      //   395: aload_3
      //   396: invokevirtual dispose : ()V
      //   399: aload_0
      //   400: getfield errors : Lio/reactivex/internal/util/AtomicThrowable;
      //   403: aload #13
      //   405: invokevirtual addThrowable : (Ljava/lang/Throwable;)Z
      //   408: pop
      //   409: aload_0
      //   410: invokevirtual checkTerminate : ()Z
      //   413: ifeq -> 417
      //   416: return
      //   417: aload_0
      //   418: aload_3
      //   419: invokevirtual removeInner : (Lio/reactivex/internal/operators/observable/ObservableFlatMap$InnerObserver;)V
      //   422: iload #10
      //   424: iconst_1
      //   425: iadd
      //   426: istore #11
      //   428: iload #7
      //   430: iconst_1
      //   431: iadd
      //   432: istore #15
      //   434: iload #11
      //   436: istore #10
      //   438: iload #15
      //   440: istore #7
      //   442: iload #15
      //   444: iload #6
      //   446: if_icmpne -> 543
      //   449: iload #11
      //   451: istore #10
      //   453: goto -> 540
      //   456: aload_3
      //   457: getfield done : Z
      //   460: istore #5
      //   462: aload_3
      //   463: getfield queue : Lio/reactivex/internal/fuseable/SimpleQueue;
      //   466: astore #13
      //   468: iload #10
      //   470: istore #11
      //   472: iload #5
      //   474: ifeq -> 515
      //   477: aload #13
      //   479: ifnull -> 496
      //   482: iload #10
      //   484: istore #11
      //   486: aload #13
      //   488: invokeinterface isEmpty : ()Z
      //   493: ifeq -> 515
      //   496: aload_0
      //   497: aload_3
      //   498: invokevirtual removeInner : (Lio/reactivex/internal/operators/observable/ObservableFlatMap$InnerObserver;)V
      //   501: aload_0
      //   502: invokevirtual checkTerminate : ()Z
      //   505: ifeq -> 509
      //   508: return
      //   509: iload #10
      //   511: iconst_1
      //   512: iadd
      //   513: istore #11
      //   515: iload #7
      //   517: iconst_1
      //   518: iadd
      //   519: istore #15
      //   521: iload #11
      //   523: istore #10
      //   525: iload #15
      //   527: istore #7
      //   529: iload #15
      //   531: iload #6
      //   533: if_icmpne -> 543
      //   536: iload #11
      //   538: istore #10
      //   540: iconst_0
      //   541: istore #7
      //   543: iinc #12, 1
      //   546: goto -> 323
      //   549: aload_0
      //   550: iload #7
      //   552: putfield lastIndex : I
      //   555: aload_0
      //   556: aload #4
      //   558: iload #7
      //   560: aaload
      //   561: getfield id : J
      //   564: putfield lastId : J
      //   567: goto -> 573
      //   570: iconst_0
      //   571: istore #10
      //   573: iload #10
      //   575: ifeq -> 647
      //   578: aload_0
      //   579: getfield maxConcurrency : I
      //   582: ldc 2147483647
      //   584: if_icmpeq -> 7
      //   587: iload #10
      //   589: ifeq -> 7
      //   592: aload_0
      //   593: monitorenter
      //   594: aload_0
      //   595: getfield sources : Ljava/util/Queue;
      //   598: invokeinterface poll : ()Ljava/lang/Object;
      //   603: checkcast io/reactivex/ObservableSource
      //   606: astore #4
      //   608: aload #4
      //   610: ifnonnull -> 628
      //   613: aload_0
      //   614: aload_0
      //   615: getfield wip : I
      //   618: iconst_1
      //   619: isub
      //   620: putfield wip : I
      //   623: aload_0
      //   624: monitorexit
      //   625: goto -> 636
      //   628: aload_0
      //   629: monitorexit
      //   630: aload_0
      //   631: aload #4
      //   633: invokevirtual subscribeInner : (Lio/reactivex/ObservableSource;)V
      //   636: iinc #10, -1
      //   639: goto -> 587
      //   642: astore_1
      //   643: aload_0
      //   644: monitorexit
      //   645: aload_1
      //   646: athrow
      //   647: aload_0
      //   648: iload_2
      //   649: ineg
      //   650: invokevirtual addAndGet : (I)I
      //   653: istore #7
      //   655: iload #7
      //   657: istore_2
      //   658: iload #7
      //   660: ifne -> 7
      //   663: return
      // Exception table:
      //   from	to	target	type
      //   98	111	114	finally
      //   115	117	114	finally
      //   355	364	388	finally
      //   594	608	642	finally
      //   613	625	642	finally
      //   628	630	642	finally
      //   643	645	642	finally
    }
    
    public boolean isDisposed() {
      return this.cancelled;
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      if (this.errors.addThrowable(param1Throwable)) {
        this.done = true;
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      // Byte code:
      //   0: aload_0
      //   1: getfield done : Z
      //   4: ifeq -> 8
      //   7: return
      //   8: aload_0
      //   9: getfield mapper : Lio/reactivex/functions/Function;
      //   12: aload_1
      //   13: invokeinterface apply : (Ljava/lang/Object;)Ljava/lang/Object;
      //   18: ldc 'The mapper returned a null ObservableSource'
      //   20: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
      //   23: checkcast io/reactivex/ObservableSource
      //   26: astore_1
      //   27: aload_0
      //   28: getfield maxConcurrency : I
      //   31: ldc 2147483647
      //   33: if_icmpeq -> 83
      //   36: aload_0
      //   37: monitorenter
      //   38: aload_0
      //   39: getfield wip : I
      //   42: aload_0
      //   43: getfield maxConcurrency : I
      //   46: if_icmpne -> 63
      //   49: aload_0
      //   50: getfield sources : Ljava/util/Queue;
      //   53: aload_1
      //   54: invokeinterface offer : (Ljava/lang/Object;)Z
      //   59: pop
      //   60: aload_0
      //   61: monitorexit
      //   62: return
      //   63: aload_0
      //   64: aload_0
      //   65: getfield wip : I
      //   68: iconst_1
      //   69: iadd
      //   70: putfield wip : I
      //   73: aload_0
      //   74: monitorexit
      //   75: goto -> 83
      //   78: astore_1
      //   79: aload_0
      //   80: monitorexit
      //   81: aload_1
      //   82: athrow
      //   83: aload_0
      //   84: aload_1
      //   85: invokevirtual subscribeInner : (Lio/reactivex/ObservableSource;)V
      //   88: return
      //   89: astore_1
      //   90: aload_1
      //   91: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
      //   94: aload_0
      //   95: getfield upstream : Lio/reactivex/disposables/Disposable;
      //   98: invokeinterface dispose : ()V
      //   103: aload_0
      //   104: aload_1
      //   105: invokevirtual onError : (Ljava/lang/Throwable;)V
      //   108: return
      // Exception table:
      //   from	to	target	type
      //   8	27	89	finally
      //   38	62	78	finally
      //   63	75	78	finally
      //   79	81	78	finally
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
    
    void removeInner(ObservableFlatMap.InnerObserver<T, U> param1InnerObserver) {
      ObservableFlatMap.InnerObserver[] arrayOfInnerObserver1;
      ObservableFlatMap.InnerObserver[] arrayOfInnerObserver2;
      do {
        byte b2;
        arrayOfInnerObserver1 = (ObservableFlatMap.InnerObserver[])this.observers.get();
        int i = arrayOfInnerObserver1.length;
        if (i == 0)
          return; 
        byte b1 = -1;
        byte b = 0;
        while (true) {
          b2 = b1;
          if (b < i) {
            if (arrayOfInnerObserver1[b] == param1InnerObserver) {
              b2 = b;
              break;
            } 
            b++;
            continue;
          } 
          break;
        } 
        if (b2 < 0)
          return; 
        if (i == 1) {
          ObservableFlatMap.InnerObserver<?, ?>[] arrayOfInnerObserver = EMPTY;
        } else {
          arrayOfInnerObserver2 = new ObservableFlatMap.InnerObserver[i - 1];
          System.arraycopy(arrayOfInnerObserver1, 0, arrayOfInnerObserver2, 0, b2);
          System.arraycopy(arrayOfInnerObserver1, b2 + 1, arrayOfInnerObserver2, b2, i - b2 - 1);
        } 
      } while (!this.observers.compareAndSet(arrayOfInnerObserver1, arrayOfInnerObserver2));
    }
    
    void subscribeInner(ObservableSource<? extends U> param1ObservableSource) {
      // Byte code:
      //   0: aload_1
      //   1: instanceof java/util/concurrent/Callable
      //   4: ifeq -> 81
      //   7: aload_0
      //   8: aload_1
      //   9: checkcast java/util/concurrent/Callable
      //   12: invokevirtual tryEmitScalar : (Ljava/util/concurrent/Callable;)Z
      //   15: ifeq -> 121
      //   18: aload_0
      //   19: getfield maxConcurrency : I
      //   22: ldc 2147483647
      //   24: if_icmpeq -> 121
      //   27: iconst_0
      //   28: istore_2
      //   29: aload_0
      //   30: monitorenter
      //   31: aload_0
      //   32: getfield sources : Ljava/util/Queue;
      //   35: invokeinterface poll : ()Ljava/lang/Object;
      //   40: checkcast io/reactivex/ObservableSource
      //   43: astore_1
      //   44: aload_1
      //   45: ifnonnull -> 60
      //   48: aload_0
      //   49: aload_0
      //   50: getfield wip : I
      //   53: iconst_1
      //   54: isub
      //   55: putfield wip : I
      //   58: iconst_1
      //   59: istore_2
      //   60: aload_0
      //   61: monitorexit
      //   62: iload_2
      //   63: ifeq -> 73
      //   66: aload_0
      //   67: invokevirtual drain : ()V
      //   70: goto -> 121
      //   73: goto -> 0
      //   76: astore_1
      //   77: aload_0
      //   78: monitorexit
      //   79: aload_1
      //   80: athrow
      //   81: aload_0
      //   82: getfield uniqueId : J
      //   85: lstore_3
      //   86: aload_0
      //   87: lconst_1
      //   88: lload_3
      //   89: ladd
      //   90: putfield uniqueId : J
      //   93: new io/reactivex/internal/operators/observable/ObservableFlatMap$InnerObserver
      //   96: dup
      //   97: aload_0
      //   98: lload_3
      //   99: invokespecial <init> : (Lio/reactivex/internal/operators/observable/ObservableFlatMap$MergeObserver;J)V
      //   102: astore #5
      //   104: aload_0
      //   105: aload #5
      //   107: invokevirtual addInner : (Lio/reactivex/internal/operators/observable/ObservableFlatMap$InnerObserver;)Z
      //   110: ifeq -> 121
      //   113: aload_1
      //   114: aload #5
      //   116: invokeinterface subscribe : (Lio/reactivex/Observer;)V
      //   121: return
      // Exception table:
      //   from	to	target	type
      //   31	44	76	finally
      //   48	58	76	finally
      //   60	62	76	finally
      //   77	79	76	finally
    }
    
    void tryEmit(U param1U, ObservableFlatMap.InnerObserver<T, U> param1InnerObserver) {
      if (get() == 0 && compareAndSet(0, 1)) {
        this.downstream.onNext(param1U);
        if (decrementAndGet() == 0)
          return; 
      } else {
        SpscLinkedArrayQueue spscLinkedArrayQueue;
        SimpleQueue<U> simpleQueue1 = param1InnerObserver.queue;
        SimpleQueue<U> simpleQueue2 = simpleQueue1;
        if (simpleQueue1 == null) {
          spscLinkedArrayQueue = new SpscLinkedArrayQueue(this.bufferSize);
          param1InnerObserver.queue = (SimpleQueue<U>)spscLinkedArrayQueue;
        } 
        spscLinkedArrayQueue.offer(param1U);
        if (getAndIncrement() != 0)
          return; 
      } 
      drainLoop();
    }
    
    boolean tryEmitScalar(Callable<? extends U> param1Callable) {
      try {
        return true;
      } finally {
        param1Callable = null;
        Exceptions.throwIfFatal((Throwable)param1Callable);
        this.errors.addThrowable((Throwable)param1Callable);
        drain();
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableFlatMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */