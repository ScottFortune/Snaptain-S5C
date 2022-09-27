package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableFlatMapSingle<T, R> extends AbstractFlowableWithUpstream<T, R> {
  final boolean delayErrors;
  
  final Function<? super T, ? extends SingleSource<? extends R>> mapper;
  
  final int maxConcurrency;
  
  public FlowableFlatMapSingle(Flowable<T> paramFlowable, Function<? super T, ? extends SingleSource<? extends R>> paramFunction, boolean paramBoolean, int paramInt) {
    super(paramFlowable);
    this.mapper = paramFunction;
    this.delayErrors = paramBoolean;
    this.maxConcurrency = paramInt;
  }
  
  protected void subscribeActual(Subscriber<? super R> paramSubscriber) {
    this.source.subscribe(new FlatMapSingleSubscriber<T, R>(paramSubscriber, this.mapper, this.delayErrors, this.maxConcurrency));
  }
  
  static final class FlatMapSingleSubscriber<T, R> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = 8600231336733376951L;
    
    final AtomicInteger active;
    
    volatile boolean cancelled;
    
    final boolean delayErrors;
    
    final Subscriber<? super R> downstream;
    
    final AtomicThrowable errors;
    
    final Function<? super T, ? extends SingleSource<? extends R>> mapper;
    
    final int maxConcurrency;
    
    final AtomicReference<SpscLinkedArrayQueue<R>> queue;
    
    final AtomicLong requested;
    
    final CompositeDisposable set;
    
    Subscription upstream;
    
    FlatMapSingleSubscriber(Subscriber<? super R> param1Subscriber, Function<? super T, ? extends SingleSource<? extends R>> param1Function, boolean param1Boolean, int param1Int) {
      this.downstream = param1Subscriber;
      this.mapper = param1Function;
      this.delayErrors = param1Boolean;
      this.maxConcurrency = param1Int;
      this.requested = new AtomicLong();
      this.set = new CompositeDisposable();
      this.errors = new AtomicThrowable();
      this.active = new AtomicInteger(1);
      this.queue = new AtomicReference<SpscLinkedArrayQueue<R>>();
    }
    
    public void cancel() {
      this.cancelled = true;
      this.upstream.cancel();
      this.set.dispose();
    }
    
    void clear() {
      SpscLinkedArrayQueue spscLinkedArrayQueue = this.queue.get();
      if (spscLinkedArrayQueue != null)
        spscLinkedArrayQueue.clear(); 
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
      //   5: aload_0
      //   6: getfield active : Ljava/util/concurrent/atomic/AtomicInteger;
      //   9: astore_2
      //   10: aload_0
      //   11: getfield queue : Ljava/util/concurrent/atomic/AtomicReference;
      //   14: astore_3
      //   15: iconst_1
      //   16: istore #4
      //   18: aload_0
      //   19: getfield requested : Ljava/util/concurrent/atomic/AtomicLong;
      //   22: invokevirtual get : ()J
      //   25: lstore #5
      //   27: lconst_0
      //   28: lstore #7
      //   30: iconst_0
      //   31: istore #9
      //   33: lload #7
      //   35: lload #5
      //   37: lcmp
      //   38: ifeq -> 219
      //   41: aload_0
      //   42: getfield cancelled : Z
      //   45: ifeq -> 53
      //   48: aload_0
      //   49: invokevirtual clear : ()V
      //   52: return
      //   53: aload_0
      //   54: getfield delayErrors : Z
      //   57: ifne -> 95
      //   60: aload_0
      //   61: getfield errors : Lio/reactivex/internal/util/AtomicThrowable;
      //   64: invokevirtual get : ()Ljava/lang/Object;
      //   67: checkcast java/lang/Throwable
      //   70: ifnull -> 95
      //   73: aload_0
      //   74: getfield errors : Lio/reactivex/internal/util/AtomicThrowable;
      //   77: invokevirtual terminate : ()Ljava/lang/Throwable;
      //   80: astore #10
      //   82: aload_0
      //   83: invokevirtual clear : ()V
      //   86: aload_1
      //   87: aload #10
      //   89: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   94: return
      //   95: aload_2
      //   96: invokevirtual get : ()I
      //   99: ifne -> 108
      //   102: iconst_1
      //   103: istore #11
      //   105: goto -> 111
      //   108: iconst_0
      //   109: istore #11
      //   111: aload_3
      //   112: invokevirtual get : ()Ljava/lang/Object;
      //   115: checkcast io/reactivex/internal/queue/SpscLinkedArrayQueue
      //   118: astore #10
      //   120: aload #10
      //   122: ifnull -> 135
      //   125: aload #10
      //   127: invokevirtual poll : ()Ljava/lang/Object;
      //   130: astore #10
      //   132: goto -> 138
      //   135: aconst_null
      //   136: astore #10
      //   138: aload #10
      //   140: ifnonnull -> 149
      //   143: iconst_1
      //   144: istore #12
      //   146: goto -> 152
      //   149: iconst_0
      //   150: istore #12
      //   152: iload #11
      //   154: ifeq -> 194
      //   157: iload #12
      //   159: ifeq -> 194
      //   162: aload_0
      //   163: getfield errors : Lio/reactivex/internal/util/AtomicThrowable;
      //   166: invokevirtual terminate : ()Ljava/lang/Throwable;
      //   169: astore #10
      //   171: aload #10
      //   173: ifnull -> 187
      //   176: aload_1
      //   177: aload #10
      //   179: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   184: goto -> 193
      //   187: aload_1
      //   188: invokeinterface onComplete : ()V
      //   193: return
      //   194: iload #12
      //   196: ifeq -> 202
      //   199: goto -> 219
      //   202: aload_1
      //   203: aload #10
      //   205: invokeinterface onNext : (Ljava/lang/Object;)V
      //   210: lload #7
      //   212: lconst_1
      //   213: ladd
      //   214: lstore #7
      //   216: goto -> 30
      //   219: lload #7
      //   221: lload #5
      //   223: lcmp
      //   224: ifne -> 368
      //   227: aload_0
      //   228: getfield cancelled : Z
      //   231: ifeq -> 239
      //   234: aload_0
      //   235: invokevirtual clear : ()V
      //   238: return
      //   239: aload_0
      //   240: getfield delayErrors : Z
      //   243: ifne -> 281
      //   246: aload_0
      //   247: getfield errors : Lio/reactivex/internal/util/AtomicThrowable;
      //   250: invokevirtual get : ()Ljava/lang/Object;
      //   253: checkcast java/lang/Throwable
      //   256: ifnull -> 281
      //   259: aload_0
      //   260: getfield errors : Lio/reactivex/internal/util/AtomicThrowable;
      //   263: invokevirtual terminate : ()Ljava/lang/Throwable;
      //   266: astore #10
      //   268: aload_0
      //   269: invokevirtual clear : ()V
      //   272: aload_1
      //   273: aload #10
      //   275: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   280: return
      //   281: aload_2
      //   282: invokevirtual get : ()I
      //   285: ifne -> 294
      //   288: iconst_1
      //   289: istore #11
      //   291: goto -> 297
      //   294: iconst_0
      //   295: istore #11
      //   297: aload_3
      //   298: invokevirtual get : ()Ljava/lang/Object;
      //   301: checkcast io/reactivex/internal/queue/SpscLinkedArrayQueue
      //   304: astore #10
      //   306: aload #10
      //   308: ifnull -> 323
      //   311: iload #9
      //   313: istore #12
      //   315: aload #10
      //   317: invokevirtual isEmpty : ()Z
      //   320: ifeq -> 326
      //   323: iconst_1
      //   324: istore #12
      //   326: iload #11
      //   328: ifeq -> 368
      //   331: iload #12
      //   333: ifeq -> 368
      //   336: aload_0
      //   337: getfield errors : Lio/reactivex/internal/util/AtomicThrowable;
      //   340: invokevirtual terminate : ()Ljava/lang/Throwable;
      //   343: astore #10
      //   345: aload #10
      //   347: ifnull -> 361
      //   350: aload_1
      //   351: aload #10
      //   353: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   358: goto -> 367
      //   361: aload_1
      //   362: invokeinterface onComplete : ()V
      //   367: return
      //   368: lload #7
      //   370: lconst_0
      //   371: lcmp
      //   372: ifeq -> 405
      //   375: aload_0
      //   376: getfield requested : Ljava/util/concurrent/atomic/AtomicLong;
      //   379: lload #7
      //   381: invokestatic produced : (Ljava/util/concurrent/atomic/AtomicLong;J)J
      //   384: pop2
      //   385: aload_0
      //   386: getfield maxConcurrency : I
      //   389: ldc 2147483647
      //   391: if_icmpeq -> 405
      //   394: aload_0
      //   395: getfield upstream : Lorg/reactivestreams/Subscription;
      //   398: lload #7
      //   400: invokeinterface request : (J)V
      //   405: aload_0
      //   406: iload #4
      //   408: ineg
      //   409: invokevirtual addAndGet : (I)I
      //   412: istore #11
      //   414: iload #11
      //   416: istore #4
      //   418: iload #11
      //   420: ifne -> 18
      //   423: return
    }
    
    SpscLinkedArrayQueue<R> getOrCreateQueue() {
      while (true) {
        SpscLinkedArrayQueue<R> spscLinkedArrayQueue = this.queue.get();
        if (spscLinkedArrayQueue != null)
          return spscLinkedArrayQueue; 
        spscLinkedArrayQueue = new SpscLinkedArrayQueue(Flowable.bufferSize());
        if (this.queue.compareAndSet(null, spscLinkedArrayQueue))
          return spscLinkedArrayQueue; 
      } 
    }
    
    void innerError(InnerObserver param1InnerObserver, Throwable param1Throwable) {
      this.set.delete(param1InnerObserver);
      if (this.errors.addThrowable(param1Throwable)) {
        if (!this.delayErrors) {
          this.upstream.cancel();
          this.set.dispose();
        } else if (this.maxConcurrency != Integer.MAX_VALUE) {
          this.upstream.request(1L);
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
      //   13: ifne -> 184
      //   16: iconst_1
      //   17: istore_3
      //   18: aload_0
      //   19: iconst_0
      //   20: iconst_1
      //   21: invokevirtual compareAndSet : (II)Z
      //   24: ifeq -> 184
      //   27: aload_0
      //   28: getfield active : Ljava/util/concurrent/atomic/AtomicInteger;
      //   31: invokevirtual decrementAndGet : ()I
      //   34: ifne -> 40
      //   37: goto -> 42
      //   40: iconst_0
      //   41: istore_3
      //   42: aload_0
      //   43: getfield requested : Ljava/util/concurrent/atomic/AtomicLong;
      //   46: invokevirtual get : ()J
      //   49: lconst_0
      //   50: lcmp
      //   51: ifeq -> 156
      //   54: aload_0
      //   55: getfield downstream : Lorg/reactivestreams/Subscriber;
      //   58: aload_2
      //   59: invokeinterface onNext : (Ljava/lang/Object;)V
      //   64: aload_0
      //   65: getfield queue : Ljava/util/concurrent/atomic/AtomicReference;
      //   68: invokevirtual get : ()Ljava/lang/Object;
      //   71: checkcast io/reactivex/internal/queue/SpscLinkedArrayQueue
      //   74: astore_1
      //   75: iload_3
      //   76: ifeq -> 125
      //   79: aload_1
      //   80: ifnull -> 90
      //   83: aload_1
      //   84: invokevirtual isEmpty : ()Z
      //   87: ifeq -> 125
      //   90: aload_0
      //   91: getfield errors : Lio/reactivex/internal/util/AtomicThrowable;
      //   94: invokevirtual terminate : ()Ljava/lang/Throwable;
      //   97: astore_1
      //   98: aload_1
      //   99: ifnull -> 115
      //   102: aload_0
      //   103: getfield downstream : Lorg/reactivestreams/Subscriber;
      //   106: aload_1
      //   107: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   112: goto -> 124
      //   115: aload_0
      //   116: getfield downstream : Lorg/reactivestreams/Subscriber;
      //   119: invokeinterface onComplete : ()V
      //   124: return
      //   125: aload_0
      //   126: getfield requested : Ljava/util/concurrent/atomic/AtomicLong;
      //   129: lconst_1
      //   130: invokestatic produced : (Ljava/util/concurrent/atomic/AtomicLong;J)J
      //   133: pop2
      //   134: aload_0
      //   135: getfield maxConcurrency : I
      //   138: ldc 2147483647
      //   140: if_icmpeq -> 171
      //   143: aload_0
      //   144: getfield upstream : Lorg/reactivestreams/Subscription;
      //   147: lconst_1
      //   148: invokeinterface request : (J)V
      //   153: goto -> 171
      //   156: aload_0
      //   157: invokevirtual getOrCreateQueue : ()Lio/reactivex/internal/queue/SpscLinkedArrayQueue;
      //   160: astore_1
      //   161: aload_1
      //   162: monitorenter
      //   163: aload_1
      //   164: aload_2
      //   165: invokevirtual offer : (Ljava/lang/Object;)Z
      //   168: pop
      //   169: aload_1
      //   170: monitorexit
      //   171: aload_0
      //   172: invokevirtual decrementAndGet : ()I
      //   175: ifne -> 215
      //   178: return
      //   179: astore_2
      //   180: aload_1
      //   181: monitorexit
      //   182: aload_2
      //   183: athrow
      //   184: aload_0
      //   185: invokevirtual getOrCreateQueue : ()Lio/reactivex/internal/queue/SpscLinkedArrayQueue;
      //   188: astore_1
      //   189: aload_1
      //   190: monitorenter
      //   191: aload_1
      //   192: aload_2
      //   193: invokevirtual offer : (Ljava/lang/Object;)Z
      //   196: pop
      //   197: aload_1
      //   198: monitorexit
      //   199: aload_0
      //   200: getfield active : Ljava/util/concurrent/atomic/AtomicInteger;
      //   203: invokevirtual decrementAndGet : ()I
      //   206: pop
      //   207: aload_0
      //   208: invokevirtual getAndIncrement : ()I
      //   211: ifeq -> 215
      //   214: return
      //   215: aload_0
      //   216: invokevirtual drainLoop : ()V
      //   219: return
      //   220: astore_2
      //   221: aload_1
      //   222: monitorexit
      //   223: aload_2
      //   224: athrow
      // Exception table:
      //   from	to	target	type
      //   163	171	179	finally
      //   180	182	179	finally
      //   191	199	220	finally
      //   221	223	220	finally
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
        this.upstream.cancel();
        onError((Throwable)param1T);
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        int i = this.maxConcurrency;
        if (i == Integer.MAX_VALUE) {
          param1Subscription.request(Long.MAX_VALUE);
        } else {
          param1Subscription.request(i);
        } 
      } 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.add(this.requested, param1Long);
        drain();
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
        FlowableFlatMapSingle.FlatMapSingleSubscriber.this.innerError(this, param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.setOnce(this, param2Disposable);
      }
      
      public void onSuccess(R param2R) {
        FlowableFlatMapSingle.FlatMapSingleSubscriber.this.innerSuccess(this, param2R);
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableFlatMapSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */