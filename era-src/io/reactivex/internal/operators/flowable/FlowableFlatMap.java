package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.Function;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableFlatMap<T, U> extends AbstractFlowableWithUpstream<T, U> {
  final int bufferSize;
  
  final boolean delayErrors;
  
  final Function<? super T, ? extends Publisher<? extends U>> mapper;
  
  final int maxConcurrency;
  
  public FlowableFlatMap(Flowable<T> paramFlowable, Function<? super T, ? extends Publisher<? extends U>> paramFunction, boolean paramBoolean, int paramInt1, int paramInt2) {
    super(paramFlowable);
    this.mapper = paramFunction;
    this.delayErrors = paramBoolean;
    this.maxConcurrency = paramInt1;
    this.bufferSize = paramInt2;
  }
  
  public static <T, U> FlowableSubscriber<T> subscribe(Subscriber<? super U> paramSubscriber, Function<? super T, ? extends Publisher<? extends U>> paramFunction, boolean paramBoolean, int paramInt1, int paramInt2) {
    return new MergeSubscriber<T, U>(paramSubscriber, paramFunction, paramBoolean, paramInt1, paramInt2);
  }
  
  protected void subscribeActual(Subscriber<? super U> paramSubscriber) {
    if (FlowableScalarXMap.tryScalarXMapSubscribe((Publisher<T>)this.source, paramSubscriber, this.mapper))
      return; 
    this.source.subscribe(subscribe(paramSubscriber, this.mapper, this.delayErrors, this.maxConcurrency, this.bufferSize));
  }
  
  static final class InnerSubscriber<T, U> extends AtomicReference<Subscription> implements FlowableSubscriber<U>, Disposable {
    private static final long serialVersionUID = -4606175640614850599L;
    
    final int bufferSize;
    
    volatile boolean done;
    
    int fusionMode;
    
    final long id;
    
    final int limit;
    
    final FlowableFlatMap.MergeSubscriber<T, U> parent;
    
    long produced;
    
    volatile SimpleQueue<U> queue;
    
    InnerSubscriber(FlowableFlatMap.MergeSubscriber<T, U> param1MergeSubscriber, long param1Long) {
      this.id = param1Long;
      this.parent = param1MergeSubscriber;
      this.bufferSize = param1MergeSubscriber.bufferSize;
      this.limit = this.bufferSize >> 2;
    }
    
    public void dispose() {
      SubscriptionHelper.cancel(this);
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (get() == SubscriptionHelper.CANCELLED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onComplete() {
      this.done = true;
      this.parent.drain();
    }
    
    public void onError(Throwable param1Throwable) {
      lazySet((Subscription)SubscriptionHelper.CANCELLED);
      this.parent.innerError(this, param1Throwable);
    }
    
    public void onNext(U param1U) {
      if (this.fusionMode != 2) {
        this.parent.tryEmit(param1U, this);
      } else {
        this.parent.drain();
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.setOnce(this, param1Subscription)) {
        if (param1Subscription instanceof QueueSubscription) {
          QueueSubscription queueSubscription = (QueueSubscription)param1Subscription;
          int i = queueSubscription.requestFusion(7);
          if (i == 1) {
            this.fusionMode = i;
            this.queue = (SimpleQueue<U>)queueSubscription;
            this.done = true;
            this.parent.drain();
            return;
          } 
          if (i == 2) {
            this.fusionMode = i;
            this.queue = (SimpleQueue<U>)queueSubscription;
          } 
        } 
        param1Subscription.request(this.bufferSize);
      } 
    }
    
    void requestMore(long param1Long) {
      if (this.fusionMode != 1) {
        param1Long = this.produced + param1Long;
        if (param1Long >= this.limit) {
          this.produced = 0L;
          get().request(param1Long);
        } else {
          this.produced = param1Long;
        } 
      } 
    }
  }
  
  static final class MergeSubscriber<T, U> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
    static final FlowableFlatMap.InnerSubscriber<?, ?>[] CANCELLED = (FlowableFlatMap.InnerSubscriber<?, ?>[])new FlowableFlatMap.InnerSubscriber[0];
    
    static final FlowableFlatMap.InnerSubscriber<?, ?>[] EMPTY = (FlowableFlatMap.InnerSubscriber<?, ?>[])new FlowableFlatMap.InnerSubscriber[0];
    
    private static final long serialVersionUID = -2117620485640801370L;
    
    final int bufferSize;
    
    volatile boolean cancelled;
    
    final boolean delayErrors;
    
    volatile boolean done;
    
    final Subscriber<? super U> downstream;
    
    final AtomicThrowable errs = new AtomicThrowable();
    
    long lastId;
    
    int lastIndex;
    
    final Function<? super T, ? extends Publisher<? extends U>> mapper;
    
    final int maxConcurrency;
    
    volatile SimplePlainQueue<U> queue;
    
    final AtomicLong requested = new AtomicLong();
    
    int scalarEmitted;
    
    final int scalarLimit;
    
    final AtomicReference<FlowableFlatMap.InnerSubscriber<?, ?>[]> subscribers = (AtomicReference)new AtomicReference<FlowableFlatMap.InnerSubscriber<?, ?>>();
    
    long uniqueId;
    
    Subscription upstream;
    
    static {
    
    }
    
    MergeSubscriber(Subscriber<? super U> param1Subscriber, Function<? super T, ? extends Publisher<? extends U>> param1Function, boolean param1Boolean, int param1Int1, int param1Int2) {
      this.downstream = param1Subscriber;
      this.mapper = param1Function;
      this.delayErrors = param1Boolean;
      this.maxConcurrency = param1Int1;
      this.bufferSize = param1Int2;
      this.scalarLimit = Math.max(1, param1Int1 >> 1);
      this.subscribers.lazySet(EMPTY);
    }
    
    boolean addInner(FlowableFlatMap.InnerSubscriber<T, U> param1InnerSubscriber) {
      while (true) {
        FlowableFlatMap.InnerSubscriber[] arrayOfInnerSubscriber1 = (FlowableFlatMap.InnerSubscriber[])this.subscribers.get();
        if (arrayOfInnerSubscriber1 == CANCELLED) {
          param1InnerSubscriber.dispose();
          return false;
        } 
        int i = arrayOfInnerSubscriber1.length;
        FlowableFlatMap.InnerSubscriber[] arrayOfInnerSubscriber2 = new FlowableFlatMap.InnerSubscriber[i + 1];
        System.arraycopy(arrayOfInnerSubscriber1, 0, arrayOfInnerSubscriber2, 0, i);
        arrayOfInnerSubscriber2[i] = param1InnerSubscriber;
        if (this.subscribers.compareAndSet(arrayOfInnerSubscriber1, arrayOfInnerSubscriber2))
          return true; 
      } 
    }
    
    public void cancel() {
      if (!this.cancelled) {
        this.cancelled = true;
        this.upstream.cancel();
        disposeAll();
        if (getAndIncrement() == 0) {
          SimplePlainQueue<U> simplePlainQueue = this.queue;
          if (simplePlainQueue != null)
            simplePlainQueue.clear(); 
        } 
      } 
    }
    
    boolean checkTerminate() {
      if (this.cancelled) {
        clearScalarQueue();
        return true;
      } 
      if (!this.delayErrors && this.errs.get() != null) {
        clearScalarQueue();
        Throwable throwable = this.errs.terminate();
        if (throwable != ExceptionHelper.TERMINATED)
          this.downstream.onError(throwable); 
        return true;
      } 
      return false;
    }
    
    void clearScalarQueue() {
      SimplePlainQueue<U> simplePlainQueue = this.queue;
      if (simplePlainQueue != null)
        simplePlainQueue.clear(); 
    }
    
    void disposeAll() {
      FlowableFlatMap.InnerSubscriber[] arrayOfInnerSubscriber = (FlowableFlatMap.InnerSubscriber[])this.subscribers.get();
      FlowableFlatMap.InnerSubscriber<?, ?>[] arrayOfInnerSubscriber1 = CANCELLED;
      if (arrayOfInnerSubscriber != arrayOfInnerSubscriber1) {
        FlowableFlatMap.InnerSubscriber[] arrayOfInnerSubscriber2 = (FlowableFlatMap.InnerSubscriber[])this.subscribers.getAndSet(arrayOfInnerSubscriber1);
        if (arrayOfInnerSubscriber2 != CANCELLED) {
          int i = arrayOfInnerSubscriber2.length;
          for (byte b = 0; b < i; b++)
            arrayOfInnerSubscriber2[b].dispose(); 
          Throwable throwable = this.errs.terminate();
          if (throwable != null && throwable != ExceptionHelper.TERMINATED)
            RxJavaPlugins.onError(throwable); 
        } 
      } 
    }
    
    void drain() {
      if (getAndIncrement() == 0)
        drainLoop(); 
    }
    
    void drainLoop() {
      // Byte code:
      //   0: aload_0
      //   1: getfield downstream : Lorg/reactivestreams/Subscriber;
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
      //   20: aload_0
      //   21: getfield requested : Ljava/util/concurrent/atomic/AtomicLong;
      //   24: invokevirtual get : ()J
      //   27: lstore #4
      //   29: lload #4
      //   31: ldc2_w 9223372036854775807
      //   34: lcmp
      //   35: ifne -> 44
      //   38: iconst_1
      //   39: istore #6
      //   41: goto -> 47
      //   44: iconst_0
      //   45: istore #6
      //   47: lconst_0
      //   48: lstore #7
      //   50: lload #4
      //   52: lstore #9
      //   54: lload #7
      //   56: lstore #11
      //   58: aload_3
      //   59: ifnull -> 194
      //   62: lconst_0
      //   63: lstore #11
      //   65: aconst_null
      //   66: astore #13
      //   68: lload #4
      //   70: lconst_0
      //   71: lcmp
      //   72: ifeq -> 128
      //   75: aload_3
      //   76: invokeinterface poll : ()Ljava/lang/Object;
      //   81: astore #13
      //   83: aload_0
      //   84: invokevirtual checkTerminate : ()Z
      //   87: ifeq -> 91
      //   90: return
      //   91: aload #13
      //   93: ifnonnull -> 99
      //   96: goto -> 128
      //   99: aload_1
      //   100: aload #13
      //   102: invokeinterface onNext : (Ljava/lang/Object;)V
      //   107: lload #7
      //   109: lconst_1
      //   110: ladd
      //   111: lstore #7
      //   113: lload #11
      //   115: lconst_1
      //   116: ladd
      //   117: lstore #11
      //   119: lload #4
      //   121: lconst_1
      //   122: lsub
      //   123: lstore #4
      //   125: goto -> 68
      //   128: lload #11
      //   130: lconst_0
      //   131: lcmp
      //   132: ifeq -> 160
      //   135: iload #6
      //   137: ifeq -> 148
      //   140: ldc2_w 9223372036854775807
      //   143: lstore #4
      //   145: goto -> 160
      //   148: aload_0
      //   149: getfield requested : Ljava/util/concurrent/atomic/AtomicLong;
      //   152: lload #11
      //   154: lneg
      //   155: invokevirtual addAndGet : (J)J
      //   158: lstore #4
      //   160: lload #4
      //   162: lstore #9
      //   164: lload #7
      //   166: lstore #11
      //   168: lload #4
      //   170: lconst_0
      //   171: lcmp
      //   172: ifeq -> 194
      //   175: aload #13
      //   177: ifnonnull -> 191
      //   180: lload #4
      //   182: lstore #9
      //   184: lload #7
      //   186: lstore #11
      //   188: goto -> 194
      //   191: goto -> 62
      //   194: aload_0
      //   195: getfield done : Z
      //   198: istore #14
      //   200: aload_0
      //   201: getfield queue : Lio/reactivex/internal/fuseable/SimplePlainQueue;
      //   204: astore_3
      //   205: aload_0
      //   206: getfield subscribers : Ljava/util/concurrent/atomic/AtomicReference;
      //   209: invokevirtual get : ()Ljava/lang/Object;
      //   212: checkcast [Lio/reactivex/internal/operators/flowable/FlowableFlatMap$InnerSubscriber;
      //   215: astore #13
      //   217: aload #13
      //   219: arraylength
      //   220: istore #15
      //   222: iload #14
      //   224: ifeq -> 285
      //   227: aload_3
      //   228: ifnull -> 240
      //   231: aload_3
      //   232: invokeinterface isEmpty : ()Z
      //   237: ifeq -> 285
      //   240: iload #15
      //   242: ifne -> 285
      //   245: aload_0
      //   246: getfield errs : Lio/reactivex/internal/util/AtomicThrowable;
      //   249: invokevirtual terminate : ()Ljava/lang/Throwable;
      //   252: astore #13
      //   254: aload #13
      //   256: getstatic io/reactivex/internal/util/ExceptionHelper.TERMINATED : Ljava/lang/Throwable;
      //   259: if_acmpeq -> 284
      //   262: aload #13
      //   264: ifnonnull -> 276
      //   267: aload_1
      //   268: invokeinterface onComplete : ()V
      //   273: goto -> 284
      //   276: aload_1
      //   277: aload #13
      //   279: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   284: return
      //   285: iload #15
      //   287: ifeq -> 811
      //   290: aload_0
      //   291: getfield lastId : J
      //   294: lstore #4
      //   296: aload_0
      //   297: getfield lastIndex : I
      //   300: istore #16
      //   302: iload #15
      //   304: iload #16
      //   306: if_icmple -> 327
      //   309: iload #16
      //   311: istore #17
      //   313: aload #13
      //   315: iload #16
      //   317: aaload
      //   318: getfield id : J
      //   321: lload #4
      //   323: lcmp
      //   324: ifeq -> 412
      //   327: iload #16
      //   329: istore #17
      //   331: iload #15
      //   333: iload #16
      //   335: if_icmpgt -> 341
      //   338: iconst_0
      //   339: istore #17
      //   341: iconst_0
      //   342: istore #16
      //   344: iload #16
      //   346: iload #15
      //   348: if_icmpge -> 394
      //   351: aload #13
      //   353: iload #17
      //   355: aaload
      //   356: getfield id : J
      //   359: lload #4
      //   361: lcmp
      //   362: ifne -> 368
      //   365: goto -> 394
      //   368: iload #17
      //   370: iconst_1
      //   371: iadd
      //   372: istore #18
      //   374: iload #18
      //   376: istore #17
      //   378: iload #18
      //   380: iload #15
      //   382: if_icmpne -> 388
      //   385: iconst_0
      //   386: istore #17
      //   388: iinc #16, 1
      //   391: goto -> 344
      //   394: aload_0
      //   395: iload #17
      //   397: putfield lastIndex : I
      //   400: aload_0
      //   401: aload #13
      //   403: iload #17
      //   405: aaload
      //   406: getfield id : J
      //   409: putfield lastId : J
      //   412: iconst_0
      //   413: istore #19
      //   415: iconst_0
      //   416: istore #20
      //   418: lload #11
      //   420: lstore #4
      //   422: iload #15
      //   424: istore #16
      //   426: lload #9
      //   428: lstore #11
      //   430: iload #17
      //   432: istore #18
      //   434: iload #20
      //   436: istore #15
      //   438: iload #19
      //   440: istore #17
      //   442: iload #15
      //   444: iload #16
      //   446: if_icmpge -> 786
      //   449: aload_0
      //   450: invokevirtual checkTerminate : ()Z
      //   453: ifeq -> 457
      //   456: return
      //   457: aload #13
      //   459: iload #18
      //   461: aaload
      //   462: astore #21
      //   464: aconst_null
      //   465: astore_3
      //   466: aload_0
      //   467: invokevirtual checkTerminate : ()Z
      //   470: ifeq -> 474
      //   473: return
      //   474: aload #21
      //   476: getfield queue : Lio/reactivex/internal/fuseable/SimpleQueue;
      //   479: astore #22
      //   481: aload #22
      //   483: ifnonnull -> 493
      //   486: lload #11
      //   488: lstore #7
      //   490: goto -> 680
      //   493: lconst_0
      //   494: lstore #7
      //   496: lload #11
      //   498: lconst_0
      //   499: lcmp
      //   500: ifeq -> 606
      //   503: aload #22
      //   505: invokeinterface poll : ()Ljava/lang/Object;
      //   510: astore_3
      //   511: aload_3
      //   512: ifnonnull -> 518
      //   515: goto -> 606
      //   518: aload_1
      //   519: aload_3
      //   520: invokeinterface onNext : (Ljava/lang/Object;)V
      //   525: aload_0
      //   526: invokevirtual checkTerminate : ()Z
      //   529: ifeq -> 533
      //   532: return
      //   533: lload #11
      //   535: lconst_1
      //   536: lsub
      //   537: lstore #11
      //   539: lload #7
      //   541: lconst_1
      //   542: ladd
      //   543: lstore #7
      //   545: goto -> 496
      //   548: astore_3
      //   549: aload_3
      //   550: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
      //   553: aload #21
      //   555: invokevirtual dispose : ()V
      //   558: aload_0
      //   559: getfield errs : Lio/reactivex/internal/util/AtomicThrowable;
      //   562: aload_3
      //   563: invokevirtual addThrowable : (Ljava/lang/Throwable;)Z
      //   566: pop
      //   567: aload_0
      //   568: getfield delayErrors : Z
      //   571: ifne -> 583
      //   574: aload_0
      //   575: getfield upstream : Lorg/reactivestreams/Subscription;
      //   578: invokeinterface cancel : ()V
      //   583: aload_0
      //   584: invokevirtual checkTerminate : ()Z
      //   587: ifeq -> 591
      //   590: return
      //   591: aload_0
      //   592: aload #21
      //   594: invokevirtual removeInner : (Lio/reactivex/internal/operators/flowable/FlowableFlatMap$InnerSubscriber;)V
      //   597: iinc #15, 1
      //   600: iconst_1
      //   601: istore #17
      //   603: goto -> 780
      //   606: lload #7
      //   608: lconst_0
      //   609: lcmp
      //   610: ifeq -> 651
      //   613: iload #6
      //   615: ifne -> 633
      //   618: aload_0
      //   619: getfield requested : Ljava/util/concurrent/atomic/AtomicLong;
      //   622: lload #7
      //   624: lneg
      //   625: invokevirtual addAndGet : (J)J
      //   628: lstore #11
      //   630: goto -> 638
      //   633: ldc2_w 9223372036854775807
      //   636: lstore #11
      //   638: aload #21
      //   640: lload #7
      //   642: invokevirtual requestMore : (J)V
      //   645: lconst_0
      //   646: lstore #9
      //   648: goto -> 654
      //   651: lconst_0
      //   652: lstore #9
      //   654: lload #11
      //   656: lstore #7
      //   658: lload #11
      //   660: lload #9
      //   662: lcmp
      //   663: ifeq -> 680
      //   666: aload_3
      //   667: ifnonnull -> 677
      //   670: lload #11
      //   672: lstore #7
      //   674: goto -> 680
      //   677: goto -> 466
      //   680: aload #13
      //   682: astore_3
      //   683: aload #21
      //   685: getfield done : Z
      //   688: istore #14
      //   690: aload #21
      //   692: getfield queue : Lio/reactivex/internal/fuseable/SimpleQueue;
      //   695: astore #22
      //   697: iload #14
      //   699: ifeq -> 743
      //   702: aload #22
      //   704: ifnull -> 717
      //   707: aload #22
      //   709: invokeinterface isEmpty : ()Z
      //   714: ifeq -> 743
      //   717: aload_0
      //   718: aload #21
      //   720: invokevirtual removeInner : (Lio/reactivex/internal/operators/flowable/FlowableFlatMap$InnerSubscriber;)V
      //   723: aload_0
      //   724: invokevirtual checkTerminate : ()Z
      //   727: ifeq -> 731
      //   730: return
      //   731: lload #4
      //   733: lconst_1
      //   734: ladd
      //   735: lstore #4
      //   737: iconst_1
      //   738: istore #17
      //   740: goto -> 743
      //   743: lload #7
      //   745: lconst_0
      //   746: lcmp
      //   747: ifne -> 756
      //   750: aload_3
      //   751: astore #13
      //   753: goto -> 786
      //   756: iinc #18, 1
      //   759: iload #18
      //   761: iload #16
      //   763: if_icmpne -> 776
      //   766: iconst_0
      //   767: istore #18
      //   769: lload #7
      //   771: lstore #11
      //   773: goto -> 780
      //   776: lload #7
      //   778: lstore #11
      //   780: iinc #15, 1
      //   783: goto -> 442
      //   786: aload_0
      //   787: iload #18
      //   789: putfield lastIndex : I
      //   792: aload_0
      //   793: aload #13
      //   795: iload #18
      //   797: aaload
      //   798: getfield id : J
      //   801: putfield lastId : J
      //   804: lload #4
      //   806: lstore #11
      //   808: goto -> 814
      //   811: iconst_0
      //   812: istore #17
      //   814: lload #11
      //   816: lconst_0
      //   817: lcmp
      //   818: ifeq -> 839
      //   821: aload_0
      //   822: getfield cancelled : Z
      //   825: ifne -> 839
      //   828: aload_0
      //   829: getfield upstream : Lorg/reactivestreams/Subscription;
      //   832: lload #11
      //   834: invokeinterface request : (J)V
      //   839: iload #17
      //   841: ifeq -> 847
      //   844: goto -> 7
      //   847: aload_0
      //   848: iload_2
      //   849: ineg
      //   850: invokevirtual addAndGet : (I)I
      //   853: istore #17
      //   855: iload #17
      //   857: istore_2
      //   858: iload #17
      //   860: ifne -> 7
      //   863: return
      // Exception table:
      //   from	to	target	type
      //   503	511	548	finally
    }
    
    SimpleQueue<U> getInnerQueue(FlowableFlatMap.InnerSubscriber<T, U> param1InnerSubscriber) {
      SpscArrayQueue spscArrayQueue;
      SimpleQueue<U> simpleQueue1 = param1InnerSubscriber.queue;
      SimpleQueue<U> simpleQueue2 = simpleQueue1;
      if (simpleQueue1 == null) {
        spscArrayQueue = new SpscArrayQueue(this.bufferSize);
        param1InnerSubscriber.queue = (SimpleQueue<U>)spscArrayQueue;
      } 
      return (SimpleQueue<U>)spscArrayQueue;
    }
    
    SimpleQueue<U> getMainQueue() {
      SpscArrayQueue spscArrayQueue;
      SimplePlainQueue<U> simplePlainQueue1 = this.queue;
      SimplePlainQueue<U> simplePlainQueue2 = simplePlainQueue1;
      if (simplePlainQueue1 == null) {
        int i = this.maxConcurrency;
        if (i == Integer.MAX_VALUE) {
          SpscLinkedArrayQueue spscLinkedArrayQueue = new SpscLinkedArrayQueue(this.bufferSize);
        } else {
          spscArrayQueue = new SpscArrayQueue(i);
        } 
        this.queue = (SimplePlainQueue<U>)spscArrayQueue;
      } 
      return (SimpleQueue<U>)spscArrayQueue;
    }
    
    void innerError(FlowableFlatMap.InnerSubscriber<T, U> param1InnerSubscriber, Throwable param1Throwable) {
      if (this.errs.addThrowable(param1Throwable)) {
        param1InnerSubscriber.done = true;
        if (!this.delayErrors) {
          this.upstream.cancel();
          FlowableFlatMap.InnerSubscriber[] arrayOfInnerSubscriber = (FlowableFlatMap.InnerSubscriber[])this.subscribers.getAndSet(CANCELLED);
          int i = arrayOfInnerSubscriber.length;
          for (byte b = 0; b < i; b++)
            arrayOfInnerSubscriber[b].dispose(); 
        } 
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
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
      if (this.errs.addThrowable(param1Throwable)) {
        this.done = true;
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
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
        if (!this.cancelled) {
          int i = this.maxConcurrency;
          if (i == Integer.MAX_VALUE) {
            param1Subscription.request(Long.MAX_VALUE);
          } else {
            param1Subscription.request(i);
          } 
        } 
      } 
    }
    
    void removeInner(FlowableFlatMap.InnerSubscriber<T, U> param1InnerSubscriber) {
      FlowableFlatMap.InnerSubscriber[] arrayOfInnerSubscriber1;
      FlowableFlatMap.InnerSubscriber[] arrayOfInnerSubscriber2;
      do {
        byte b2;
        arrayOfInnerSubscriber1 = (FlowableFlatMap.InnerSubscriber[])this.subscribers.get();
        int i = arrayOfInnerSubscriber1.length;
        if (i == 0)
          return; 
        byte b1 = -1;
        byte b = 0;
        while (true) {
          b2 = b1;
          if (b < i) {
            if (arrayOfInnerSubscriber1[b] == param1InnerSubscriber) {
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
          FlowableFlatMap.InnerSubscriber<?, ?>[] arrayOfInnerSubscriber = EMPTY;
        } else {
          arrayOfInnerSubscriber2 = new FlowableFlatMap.InnerSubscriber[i - 1];
          System.arraycopy(arrayOfInnerSubscriber1, 0, arrayOfInnerSubscriber2, 0, b2);
          System.arraycopy(arrayOfInnerSubscriber1, b2 + 1, arrayOfInnerSubscriber2, b2, i - b2 - 1);
        } 
      } while (!this.subscribers.compareAndSet(arrayOfInnerSubscriber1, arrayOfInnerSubscriber2));
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.add(this.requested, param1Long);
        drain();
      } 
    }
    
    void tryEmit(U param1U, FlowableFlatMap.InnerSubscriber<T, U> param1InnerSubscriber) {
      if (get() == 0 && compareAndSet(0, 1)) {
        long l = this.requested.get();
        SimpleQueue<U> simpleQueue = param1InnerSubscriber.queue;
        if (l != 0L && (simpleQueue == null || simpleQueue.isEmpty())) {
          this.downstream.onNext(param1U);
          if (l != Long.MAX_VALUE)
            this.requested.decrementAndGet(); 
          param1InnerSubscriber.requestMore(1L);
        } else {
          SimpleQueue<U> simpleQueue1 = simpleQueue;
          if (simpleQueue == null)
            simpleQueue1 = getInnerQueue(param1InnerSubscriber); 
          if (!simpleQueue1.offer(param1U)) {
            onError((Throwable)new MissingBackpressureException("Inner queue full?!"));
            return;
          } 
        } 
        if (decrementAndGet() == 0)
          return; 
      } else {
        SpscArrayQueue spscArrayQueue;
        SimpleQueue<U> simpleQueue1 = param1InnerSubscriber.queue;
        SimpleQueue<U> simpleQueue2 = simpleQueue1;
        if (simpleQueue1 == null) {
          spscArrayQueue = new SpscArrayQueue(this.bufferSize);
          param1InnerSubscriber.queue = (SimpleQueue<U>)spscArrayQueue;
        } 
        if (!spscArrayQueue.offer(param1U)) {
          onError((Throwable)new MissingBackpressureException("Inner queue full?!"));
          return;
        } 
        if (getAndIncrement() != 0)
          return; 
      } 
      drainLoop();
    }
    
    void tryEmitScalar(U param1U) {
      if (get() == 0 && compareAndSet(0, 1)) {
        long l = this.requested.get();
        SimplePlainQueue<U> simplePlainQueue = this.queue;
        if (l != 0L && (simplePlainQueue == null || simplePlainQueue.isEmpty())) {
          this.downstream.onNext(param1U);
          if (l != Long.MAX_VALUE)
            this.requested.decrementAndGet(); 
          if (this.maxConcurrency != Integer.MAX_VALUE && !this.cancelled) {
            int i = this.scalarEmitted + 1;
            this.scalarEmitted = i;
            int j = this.scalarLimit;
            if (i == j) {
              this.scalarEmitted = 0;
              this.upstream.request(j);
            } 
          } 
        } else {
          SimpleQueue<U> simpleQueue;
          SimplePlainQueue<U> simplePlainQueue1 = simplePlainQueue;
          if (simplePlainQueue == null)
            simpleQueue = getMainQueue(); 
          if (!simpleQueue.offer(param1U)) {
            onError(new IllegalStateException("Scalar queue full?!"));
            return;
          } 
        } 
        if (decrementAndGet() == 0)
          return; 
      } else {
        if (!getMainQueue().offer(param1U)) {
          onError(new IllegalStateException("Scalar queue full?!"));
          return;
        } 
        if (getAndIncrement() != 0)
          return; 
      } 
      drainLoop();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableFlatMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */