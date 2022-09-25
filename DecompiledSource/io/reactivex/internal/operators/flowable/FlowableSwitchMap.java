package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.Function;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableSwitchMap<T, R> extends AbstractFlowableWithUpstream<T, R> {
  final int bufferSize;
  
  final boolean delayErrors;
  
  final Function<? super T, ? extends Publisher<? extends R>> mapper;
  
  public FlowableSwitchMap(Flowable<T> paramFlowable, Function<? super T, ? extends Publisher<? extends R>> paramFunction, int paramInt, boolean paramBoolean) {
    super(paramFlowable);
    this.mapper = paramFunction;
    this.bufferSize = paramInt;
    this.delayErrors = paramBoolean;
  }
  
  protected void subscribeActual(Subscriber<? super R> paramSubscriber) {
    if (FlowableScalarXMap.tryScalarXMapSubscribe((Publisher<T>)this.source, paramSubscriber, this.mapper))
      return; 
    this.source.subscribe(new SwitchMapSubscriber<T, R>(paramSubscriber, this.mapper, this.bufferSize, this.delayErrors));
  }
  
  static final class SwitchMapInnerSubscriber<T, R> extends AtomicReference<Subscription> implements FlowableSubscriber<R> {
    private static final long serialVersionUID = 3837284832786408377L;
    
    final int bufferSize;
    
    volatile boolean done;
    
    int fusionMode;
    
    final long index;
    
    final FlowableSwitchMap.SwitchMapSubscriber<T, R> parent;
    
    volatile SimpleQueue<R> queue;
    
    SwitchMapInnerSubscriber(FlowableSwitchMap.SwitchMapSubscriber<T, R> param1SwitchMapSubscriber, long param1Long, int param1Int) {
      this.parent = param1SwitchMapSubscriber;
      this.index = param1Long;
      this.bufferSize = param1Int;
    }
    
    public void cancel() {
      SubscriptionHelper.cancel(this);
    }
    
    public void onComplete() {
      FlowableSwitchMap.SwitchMapSubscriber<T, R> switchMapSubscriber = this.parent;
      if (this.index == switchMapSubscriber.unique) {
        this.done = true;
        switchMapSubscriber.drain();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      FlowableSwitchMap.SwitchMapSubscriber<T, R> switchMapSubscriber = this.parent;
      if (this.index == switchMapSubscriber.unique && switchMapSubscriber.error.addThrowable(param1Throwable)) {
        if (!switchMapSubscriber.delayErrors) {
          switchMapSubscriber.upstream.cancel();
          switchMapSubscriber.done = true;
        } 
        this.done = true;
        switchMapSubscriber.drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(R param1R) {
      FlowableSwitchMap.SwitchMapSubscriber<T, R> switchMapSubscriber = this.parent;
      if (this.index == switchMapSubscriber.unique) {
        if (this.fusionMode == 0 && !this.queue.offer(param1R)) {
          onError((Throwable)new MissingBackpressureException("Queue full?!"));
          return;
        } 
        switchMapSubscriber.drain();
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.setOnce(this, param1Subscription)) {
        if (param1Subscription instanceof QueueSubscription) {
          QueueSubscription queueSubscription = (QueueSubscription)param1Subscription;
          int i = queueSubscription.requestFusion(7);
          if (i == 1) {
            this.fusionMode = i;
            this.queue = (SimpleQueue<R>)queueSubscription;
            this.done = true;
            this.parent.drain();
            return;
          } 
          if (i == 2) {
            this.fusionMode = i;
            this.queue = (SimpleQueue<R>)queueSubscription;
            param1Subscription.request(this.bufferSize);
            return;
          } 
        } 
        this.queue = (SimpleQueue<R>)new SpscArrayQueue(this.bufferSize);
        param1Subscription.request(this.bufferSize);
      } 
    }
    
    public void request(long param1Long) {
      if (this.fusionMode != 1)
        get().request(param1Long); 
    }
  }
  
  static final class SwitchMapSubscriber<T, R> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
    static final FlowableSwitchMap.SwitchMapInnerSubscriber<Object, Object> CANCELLED = new FlowableSwitchMap.SwitchMapInnerSubscriber<Object, Object>(null, -1L, 1);
    
    private static final long serialVersionUID = -3491074160481096299L;
    
    final AtomicReference<FlowableSwitchMap.SwitchMapInnerSubscriber<T, R>> active = new AtomicReference<FlowableSwitchMap.SwitchMapInnerSubscriber<T, R>>();
    
    final int bufferSize;
    
    volatile boolean cancelled;
    
    final boolean delayErrors;
    
    volatile boolean done;
    
    final Subscriber<? super R> downstream;
    
    final AtomicThrowable error;
    
    final Function<? super T, ? extends Publisher<? extends R>> mapper;
    
    final AtomicLong requested = new AtomicLong();
    
    volatile long unique;
    
    Subscription upstream;
    
    static {
      CANCELLED.cancel();
    }
    
    SwitchMapSubscriber(Subscriber<? super R> param1Subscriber, Function<? super T, ? extends Publisher<? extends R>> param1Function, int param1Int, boolean param1Boolean) {
      this.downstream = param1Subscriber;
      this.mapper = param1Function;
      this.bufferSize = param1Int;
      this.delayErrors = param1Boolean;
      this.error = new AtomicThrowable();
    }
    
    public void cancel() {
      if (!this.cancelled) {
        this.cancelled = true;
        this.upstream.cancel();
        disposeInner();
      } 
    }
    
    void disposeInner() {
      FlowableSwitchMap.SwitchMapInnerSubscriber<Object, Object> switchMapInnerSubscriber1 = (FlowableSwitchMap.SwitchMapInnerSubscriber)this.active.get();
      FlowableSwitchMap.SwitchMapInnerSubscriber<Object, Object> switchMapInnerSubscriber2 = CANCELLED;
      if (switchMapInnerSubscriber1 != switchMapInnerSubscriber2) {
        switchMapInnerSubscriber2 = (FlowableSwitchMap.SwitchMapInnerSubscriber<Object, Object>)this.active.getAndSet(switchMapInnerSubscriber2);
        if (switchMapInnerSubscriber2 != CANCELLED && switchMapInnerSubscriber2 != null)
          switchMapInnerSubscriber2.cancel(); 
      } 
    }
    
    void drain() {
      // Byte code:
      //   0: aload_0
      //   1: invokevirtual getAndIncrement : ()I
      //   4: ifeq -> 8
      //   7: return
      //   8: aload_0
      //   9: getfield downstream : Lorg/reactivestreams/Subscriber;
      //   12: astore_1
      //   13: iconst_1
      //   14: istore_2
      //   15: aload_0
      //   16: getfield cancelled : Z
      //   19: ifeq -> 31
      //   22: aload_0
      //   23: getfield active : Ljava/util/concurrent/atomic/AtomicReference;
      //   26: aconst_null
      //   27: invokevirtual lazySet : (Ljava/lang/Object;)V
      //   30: return
      //   31: aload_0
      //   32: getfield done : Z
      //   35: ifeq -> 139
      //   38: aload_0
      //   39: getfield delayErrors : Z
      //   42: ifeq -> 91
      //   45: aload_0
      //   46: getfield active : Ljava/util/concurrent/atomic/AtomicReference;
      //   49: invokevirtual get : ()Ljava/lang/Object;
      //   52: ifnonnull -> 139
      //   55: aload_0
      //   56: getfield error : Lio/reactivex/internal/util/AtomicThrowable;
      //   59: invokevirtual get : ()Ljava/lang/Object;
      //   62: checkcast java/lang/Throwable
      //   65: ifnull -> 84
      //   68: aload_1
      //   69: aload_0
      //   70: getfield error : Lio/reactivex/internal/util/AtomicThrowable;
      //   73: invokevirtual terminate : ()Ljava/lang/Throwable;
      //   76: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   81: goto -> 90
      //   84: aload_1
      //   85: invokeinterface onComplete : ()V
      //   90: return
      //   91: aload_0
      //   92: getfield error : Lio/reactivex/internal/util/AtomicThrowable;
      //   95: invokevirtual get : ()Ljava/lang/Object;
      //   98: checkcast java/lang/Throwable
      //   101: ifnull -> 122
      //   104: aload_0
      //   105: invokevirtual disposeInner : ()V
      //   108: aload_1
      //   109: aload_0
      //   110: getfield error : Lio/reactivex/internal/util/AtomicThrowable;
      //   113: invokevirtual terminate : ()Ljava/lang/Throwable;
      //   116: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   121: return
      //   122: aload_0
      //   123: getfield active : Ljava/util/concurrent/atomic/AtomicReference;
      //   126: invokevirtual get : ()Ljava/lang/Object;
      //   129: ifnonnull -> 139
      //   132: aload_1
      //   133: invokeinterface onComplete : ()V
      //   138: return
      //   139: aload_0
      //   140: getfield active : Ljava/util/concurrent/atomic/AtomicReference;
      //   143: invokevirtual get : ()Ljava/lang/Object;
      //   146: checkcast io/reactivex/internal/operators/flowable/FlowableSwitchMap$SwitchMapInnerSubscriber
      //   149: astore_3
      //   150: aload_3
      //   151: ifnull -> 163
      //   154: aload_3
      //   155: getfield queue : Lio/reactivex/internal/fuseable/SimpleQueue;
      //   158: astore #4
      //   160: goto -> 166
      //   163: aconst_null
      //   164: astore #4
      //   166: aload #4
      //   168: ifnull -> 525
      //   171: aload_3
      //   172: getfield done : Z
      //   175: ifeq -> 262
      //   178: aload_0
      //   179: getfield delayErrors : Z
      //   182: ifne -> 239
      //   185: aload_0
      //   186: getfield error : Lio/reactivex/internal/util/AtomicThrowable;
      //   189: invokevirtual get : ()Ljava/lang/Object;
      //   192: checkcast java/lang/Throwable
      //   195: ifnull -> 216
      //   198: aload_0
      //   199: invokevirtual disposeInner : ()V
      //   202: aload_1
      //   203: aload_0
      //   204: getfield error : Lio/reactivex/internal/util/AtomicThrowable;
      //   207: invokevirtual terminate : ()Ljava/lang/Throwable;
      //   210: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   215: return
      //   216: aload #4
      //   218: invokeinterface isEmpty : ()Z
      //   223: ifeq -> 262
      //   226: aload_0
      //   227: getfield active : Ljava/util/concurrent/atomic/AtomicReference;
      //   230: aload_3
      //   231: aconst_null
      //   232: invokevirtual compareAndSet : (Ljava/lang/Object;Ljava/lang/Object;)Z
      //   235: pop
      //   236: goto -> 15
      //   239: aload #4
      //   241: invokeinterface isEmpty : ()Z
      //   246: ifeq -> 262
      //   249: aload_0
      //   250: getfield active : Ljava/util/concurrent/atomic/AtomicReference;
      //   253: aload_3
      //   254: aconst_null
      //   255: invokevirtual compareAndSet : (Ljava/lang/Object;Ljava/lang/Object;)Z
      //   258: pop
      //   259: goto -> 15
      //   262: aload_0
      //   263: getfield requested : Ljava/util/concurrent/atomic/AtomicLong;
      //   266: invokevirtual get : ()J
      //   269: lstore #5
      //   271: lconst_0
      //   272: lstore #7
      //   274: iconst_0
      //   275: istore #9
      //   277: iload #9
      //   279: istore #10
      //   281: lload #7
      //   283: lload #5
      //   285: lcmp
      //   286: ifeq -> 477
      //   289: aload_0
      //   290: getfield cancelled : Z
      //   293: ifeq -> 297
      //   296: return
      //   297: aload_3
      //   298: getfield done : Z
      //   301: istore #11
      //   303: aload #4
      //   305: invokeinterface poll : ()Ljava/lang/Object;
      //   310: astore #12
      //   312: goto -> 342
      //   315: astore #12
      //   317: aload #12
      //   319: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
      //   322: aload_3
      //   323: invokevirtual cancel : ()V
      //   326: aload_0
      //   327: getfield error : Lio/reactivex/internal/util/AtomicThrowable;
      //   330: aload #12
      //   332: invokevirtual addThrowable : (Ljava/lang/Throwable;)Z
      //   335: pop
      //   336: aconst_null
      //   337: astore #12
      //   339: iconst_1
      //   340: istore #11
      //   342: aload #12
      //   344: ifnonnull -> 353
      //   347: iconst_1
      //   348: istore #10
      //   350: goto -> 356
      //   353: iconst_0
      //   354: istore #10
      //   356: aload_3
      //   357: aload_0
      //   358: getfield active : Ljava/util/concurrent/atomic/AtomicReference;
      //   361: invokevirtual get : ()Ljava/lang/Object;
      //   364: if_acmpeq -> 373
      //   367: iconst_1
      //   368: istore #10
      //   370: goto -> 477
      //   373: iload #11
      //   375: ifeq -> 448
      //   378: aload_0
      //   379: getfield delayErrors : Z
      //   382: ifne -> 430
      //   385: aload_0
      //   386: getfield error : Lio/reactivex/internal/util/AtomicThrowable;
      //   389: invokevirtual get : ()Ljava/lang/Object;
      //   392: checkcast java/lang/Throwable
      //   395: ifnull -> 412
      //   398: aload_1
      //   399: aload_0
      //   400: getfield error : Lio/reactivex/internal/util/AtomicThrowable;
      //   403: invokevirtual terminate : ()Ljava/lang/Throwable;
      //   406: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   411: return
      //   412: iload #10
      //   414: ifeq -> 448
      //   417: aload_0
      //   418: getfield active : Ljava/util/concurrent/atomic/AtomicReference;
      //   421: aload_3
      //   422: aconst_null
      //   423: invokevirtual compareAndSet : (Ljava/lang/Object;Ljava/lang/Object;)Z
      //   426: pop
      //   427: goto -> 367
      //   430: iload #10
      //   432: ifeq -> 448
      //   435: aload_0
      //   436: getfield active : Ljava/util/concurrent/atomic/AtomicReference;
      //   439: aload_3
      //   440: aconst_null
      //   441: invokevirtual compareAndSet : (Ljava/lang/Object;Ljava/lang/Object;)Z
      //   444: pop
      //   445: goto -> 367
      //   448: iload #10
      //   450: ifeq -> 460
      //   453: iload #9
      //   455: istore #10
      //   457: goto -> 477
      //   460: aload_1
      //   461: aload #12
      //   463: invokeinterface onNext : (Ljava/lang/Object;)V
      //   468: lload #7
      //   470: lconst_1
      //   471: ladd
      //   472: lstore #7
      //   474: goto -> 274
      //   477: lload #7
      //   479: lconst_0
      //   480: lcmp
      //   481: ifeq -> 517
      //   484: aload_0
      //   485: getfield cancelled : Z
      //   488: ifne -> 517
      //   491: lload #5
      //   493: ldc2_w 9223372036854775807
      //   496: lcmp
      //   497: ifeq -> 511
      //   500: aload_0
      //   501: getfield requested : Ljava/util/concurrent/atomic/AtomicLong;
      //   504: lload #7
      //   506: lneg
      //   507: invokevirtual addAndGet : (J)J
      //   510: pop2
      //   511: aload_3
      //   512: lload #7
      //   514: invokevirtual request : (J)V
      //   517: iload #10
      //   519: ifeq -> 525
      //   522: goto -> 15
      //   525: aload_0
      //   526: iload_2
      //   527: ineg
      //   528: invokevirtual addAndGet : (I)I
      //   531: istore #10
      //   533: iload #10
      //   535: istore_2
      //   536: iload #10
      //   538: ifne -> 15
      //   541: return
      // Exception table:
      //   from	to	target	type
      //   303	312	315	finally
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      if (!this.done && this.error.addThrowable(param1Throwable)) {
        if (!this.delayErrors)
          disposeInner(); 
        this.done = true;
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      long l = this.unique + 1L;
      this.unique = l;
      FlowableSwitchMap.SwitchMapInnerSubscriber<Object, Object> switchMapInnerSubscriber = (FlowableSwitchMap.SwitchMapInnerSubscriber)this.active.get();
      if (switchMapInnerSubscriber != null)
        switchMapInnerSubscriber.cancel(); 
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
      } 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.add(this.requested, param1Long);
        if (this.unique == 0L) {
          this.upstream.request(Long.MAX_VALUE);
        } else {
          drain();
        } 
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableSwitchMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */