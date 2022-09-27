package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.fuseable.HasUpstreamPublisher;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowablePublish<T> extends ConnectableFlowable<T> implements HasUpstreamPublisher<T>, FlowablePublishClassic<T> {
  static final long CANCELLED = -9223372036854775808L;
  
  final int bufferSize;
  
  final AtomicReference<PublishSubscriber<T>> current;
  
  final Publisher<T> onSubscribe;
  
  final Flowable<T> source;
  
  private FlowablePublish(Publisher<T> paramPublisher, Flowable<T> paramFlowable, AtomicReference<PublishSubscriber<T>> paramAtomicReference, int paramInt) {
    this.onSubscribe = paramPublisher;
    this.source = paramFlowable;
    this.current = paramAtomicReference;
    this.bufferSize = paramInt;
  }
  
  public static <T> ConnectableFlowable<T> create(Flowable<T> paramFlowable, int paramInt) {
    AtomicReference<PublishSubscriber<T>> atomicReference = new AtomicReference();
    return RxJavaPlugins.onAssembly(new FlowablePublish<T>(new FlowablePublisher<T>(atomicReference, paramInt), paramFlowable, atomicReference, paramInt));
  }
  
  public void connect(Consumer<? super Disposable> paramConsumer) {
    // Byte code:
    //   0: aload_0
    //   1: getfield current : Ljava/util/concurrent/atomic/AtomicReference;
    //   4: invokevirtual get : ()Ljava/lang/Object;
    //   7: checkcast io/reactivex/internal/operators/flowable/FlowablePublish$PublishSubscriber
    //   10: astore_2
    //   11: aload_2
    //   12: ifnull -> 24
    //   15: aload_2
    //   16: astore_3
    //   17: aload_2
    //   18: invokevirtual isDisposed : ()Z
    //   21: ifeq -> 55
    //   24: new io/reactivex/internal/operators/flowable/FlowablePublish$PublishSubscriber
    //   27: dup
    //   28: aload_0
    //   29: getfield current : Ljava/util/concurrent/atomic/AtomicReference;
    //   32: aload_0
    //   33: getfield bufferSize : I
    //   36: invokespecial <init> : (Ljava/util/concurrent/atomic/AtomicReference;I)V
    //   39: astore_3
    //   40: aload_0
    //   41: getfield current : Ljava/util/concurrent/atomic/AtomicReference;
    //   44: aload_2
    //   45: aload_3
    //   46: invokevirtual compareAndSet : (Ljava/lang/Object;Ljava/lang/Object;)Z
    //   49: ifne -> 55
    //   52: goto -> 0
    //   55: aload_3
    //   56: getfield shouldConnect : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   59: invokevirtual get : ()Z
    //   62: istore #4
    //   64: iconst_1
    //   65: istore #5
    //   67: iload #4
    //   69: ifne -> 87
    //   72: aload_3
    //   73: getfield shouldConnect : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   76: iconst_0
    //   77: iconst_1
    //   78: invokevirtual compareAndSet : (ZZ)Z
    //   81: ifeq -> 87
    //   84: goto -> 90
    //   87: iconst_0
    //   88: istore #5
    //   90: aload_1
    //   91: aload_3
    //   92: invokeinterface accept : (Ljava/lang/Object;)V
    //   97: iload #5
    //   99: ifeq -> 110
    //   102: aload_0
    //   103: getfield source : Lio/reactivex/Flowable;
    //   106: aload_3
    //   107: invokevirtual subscribe : (Lio/reactivex/FlowableSubscriber;)V
    //   110: return
    //   111: astore_1
    //   112: aload_1
    //   113: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
    //   116: aload_1
    //   117: invokestatic wrapOrThrow : (Ljava/lang/Throwable;)Ljava/lang/RuntimeException;
    //   120: astore_1
    //   121: goto -> 126
    //   124: aload_1
    //   125: athrow
    //   126: goto -> 124
    // Exception table:
    //   from	to	target	type
    //   90	97	111	finally
  }
  
  public int publishBufferSize() {
    return this.bufferSize;
  }
  
  public Publisher<T> publishSource() {
    return (Publisher<T>)this.source;
  }
  
  public Publisher<T> source() {
    return (Publisher<T>)this.source;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.onSubscribe.subscribe(paramSubscriber);
  }
  
  static final class FlowablePublisher<T> implements Publisher<T> {
    private final int bufferSize;
    
    private final AtomicReference<FlowablePublish.PublishSubscriber<T>> curr;
    
    FlowablePublisher(AtomicReference<FlowablePublish.PublishSubscriber<T>> param1AtomicReference, int param1Int) {
      this.curr = param1AtomicReference;
      this.bufferSize = param1Int;
    }
    
    public void subscribe(Subscriber<? super T> param1Subscriber) {
      FlowablePublish.InnerSubscriber<T> innerSubscriber = new FlowablePublish.InnerSubscriber<T>(param1Subscriber);
      param1Subscriber.onSubscribe(innerSubscriber);
      while (true) {
        while (true) {
          FlowablePublish.PublishSubscriber publishSubscriber = this.curr.get();
          break;
        } 
        if (param1Subscriber.add(innerSubscriber)) {
          if (innerSubscriber.get() == Long.MIN_VALUE) {
            param1Subscriber.remove(innerSubscriber);
          } else {
            innerSubscriber.parent = (FlowablePublish.PublishSubscriber)param1Subscriber;
          } 
          param1Subscriber.dispatch();
          return;
        } 
      } 
    }
  }
  
  static final class InnerSubscriber<T> extends AtomicLong implements Subscription {
    private static final long serialVersionUID = -4453897557930727610L;
    
    final Subscriber<? super T> child;
    
    long emitted;
    
    volatile FlowablePublish.PublishSubscriber<T> parent;
    
    InnerSubscriber(Subscriber<? super T> param1Subscriber) {
      this.child = param1Subscriber;
    }
    
    public void cancel() {
      if (get() != Long.MIN_VALUE && getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
        FlowablePublish.PublishSubscriber<T> publishSubscriber = this.parent;
        if (publishSubscriber != null) {
          publishSubscriber.remove(this);
          publishSubscriber.dispatch();
        } 
      } 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.addCancel(this, param1Long);
        FlowablePublish.PublishSubscriber<T> publishSubscriber = this.parent;
        if (publishSubscriber != null)
          publishSubscriber.dispatch(); 
      } 
    }
  }
  
  static final class PublishSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T>, Disposable {
    static final FlowablePublish.InnerSubscriber[] EMPTY = new FlowablePublish.InnerSubscriber[0];
    
    static final FlowablePublish.InnerSubscriber[] TERMINATED = new FlowablePublish.InnerSubscriber[0];
    
    private static final long serialVersionUID = -202316842419149694L;
    
    final int bufferSize;
    
    final AtomicReference<PublishSubscriber<T>> current;
    
    volatile SimpleQueue<T> queue;
    
    final AtomicBoolean shouldConnect;
    
    int sourceMode;
    
    final AtomicReference<FlowablePublish.InnerSubscriber<T>[]> subscribers = new AtomicReference(EMPTY);
    
    volatile Object terminalEvent;
    
    final AtomicReference<Subscription> upstream = new AtomicReference<Subscription>();
    
    PublishSubscriber(AtomicReference<PublishSubscriber<T>> param1AtomicReference, int param1Int) {
      this.current = param1AtomicReference;
      this.shouldConnect = new AtomicBoolean();
      this.bufferSize = param1Int;
    }
    
    boolean add(FlowablePublish.InnerSubscriber<T> param1InnerSubscriber) {
      while (true) {
        FlowablePublish.InnerSubscriber[] arrayOfInnerSubscriber1 = (FlowablePublish.InnerSubscriber[])this.subscribers.get();
        if (arrayOfInnerSubscriber1 == TERMINATED)
          return false; 
        int i = arrayOfInnerSubscriber1.length;
        FlowablePublish.InnerSubscriber[] arrayOfInnerSubscriber2 = new FlowablePublish.InnerSubscriber[i + 1];
        System.arraycopy(arrayOfInnerSubscriber1, 0, arrayOfInnerSubscriber2, 0, i);
        arrayOfInnerSubscriber2[i] = param1InnerSubscriber;
        if (this.subscribers.compareAndSet(arrayOfInnerSubscriber1, arrayOfInnerSubscriber2))
          return true; 
      } 
    }
    
    boolean checkTerminated(Object param1Object, boolean param1Boolean) {
      int i = 0;
      int j = 0;
      if (param1Object != null)
        if (NotificationLite.isComplete(param1Object)) {
          if (param1Boolean) {
            this.current.compareAndSet(this, null);
            param1Object = this.subscribers.getAndSet(TERMINATED);
            i = param1Object.length;
            while (j < i) {
              ((FlowablePublish.InnerSubscriber)param1Object[j]).child.onComplete();
              j++;
            } 
            return true;
          } 
        } else {
          param1Object = NotificationLite.getError(param1Object);
          this.current.compareAndSet(this, null);
          FlowablePublish.InnerSubscriber[] arrayOfInnerSubscriber = (FlowablePublish.InnerSubscriber[])this.subscribers.getAndSet(TERMINATED);
          if (arrayOfInnerSubscriber.length != 0) {
            int k = arrayOfInnerSubscriber.length;
            for (j = i; j < k; j++)
              (arrayOfInnerSubscriber[j]).child.onError((Throwable)param1Object); 
          } else {
            RxJavaPlugins.onError((Throwable)param1Object);
          } 
          return true;
        }  
      return false;
    }
    
    void dispatch() {
      // Byte code:
      //   0: aload_0
      //   1: invokevirtual getAndIncrement : ()I
      //   4: ifeq -> 8
      //   7: return
      //   8: aload_0
      //   9: getfield subscribers : Ljava/util/concurrent/atomic/AtomicReference;
      //   12: astore_1
      //   13: aload_1
      //   14: invokevirtual get : ()Ljava/lang/Object;
      //   17: checkcast [Lio/reactivex/internal/operators/flowable/FlowablePublish$InnerSubscriber;
      //   20: astore_2
      //   21: iconst_1
      //   22: istore_3
      //   23: aload_0
      //   24: getfield terminalEvent : Ljava/lang/Object;
      //   27: astore #4
      //   29: aload_0
      //   30: getfield queue : Lio/reactivex/internal/fuseable/SimpleQueue;
      //   33: astore #5
      //   35: aload #5
      //   37: ifnull -> 59
      //   40: aload #5
      //   42: invokeinterface isEmpty : ()Z
      //   47: ifeq -> 53
      //   50: goto -> 59
      //   53: iconst_0
      //   54: istore #6
      //   56: goto -> 62
      //   59: iconst_1
      //   60: istore #6
      //   62: aload_0
      //   63: aload #4
      //   65: iload #6
      //   67: invokevirtual checkTerminated : (Ljava/lang/Object;Z)Z
      //   70: ifeq -> 74
      //   73: return
      //   74: iload #6
      //   76: ifne -> 589
      //   79: aload_2
      //   80: arraylength
      //   81: istore #7
      //   83: aload_2
      //   84: arraylength
      //   85: istore #8
      //   87: iconst_0
      //   88: istore #9
      //   90: iconst_0
      //   91: istore #10
      //   93: ldc2_w 9223372036854775807
      //   96: lstore #11
      //   98: iload #9
      //   100: iload #8
      //   102: if_icmpge -> 154
      //   105: aload_2
      //   106: iload #9
      //   108: aaload
      //   109: astore #4
      //   111: aload #4
      //   113: invokevirtual get : ()J
      //   116: lstore #13
      //   118: lload #13
      //   120: ldc2_w -9223372036854775808
      //   123: lcmp
      //   124: ifeq -> 145
      //   127: lload #11
      //   129: lload #13
      //   131: aload #4
      //   133: getfield emitted : J
      //   136: lsub
      //   137: invokestatic min : (JJ)J
      //   140: lstore #11
      //   142: goto -> 148
      //   145: iinc #10, 1
      //   148: iinc #9, 1
      //   151: goto -> 98
      //   154: iload #7
      //   156: iload #10
      //   158: if_icmpne -> 274
      //   161: aload_0
      //   162: getfield terminalEvent : Ljava/lang/Object;
      //   165: astore #15
      //   167: aload #5
      //   169: invokeinterface poll : ()Ljava/lang/Object;
      //   174: astore #4
      //   176: aload #15
      //   178: astore #5
      //   180: goto -> 221
      //   183: astore #5
      //   185: aload #5
      //   187: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
      //   190: aload_0
      //   191: getfield upstream : Ljava/util/concurrent/atomic/AtomicReference;
      //   194: invokevirtual get : ()Ljava/lang/Object;
      //   197: checkcast org/reactivestreams/Subscription
      //   200: invokeinterface cancel : ()V
      //   205: aload #5
      //   207: invokestatic error : (Ljava/lang/Throwable;)Ljava/lang/Object;
      //   210: astore #5
      //   212: aload_0
      //   213: aload #5
      //   215: putfield terminalEvent : Ljava/lang/Object;
      //   218: aconst_null
      //   219: astore #4
      //   221: aload #4
      //   223: ifnonnull -> 232
      //   226: iconst_1
      //   227: istore #6
      //   229: goto -> 235
      //   232: iconst_0
      //   233: istore #6
      //   235: aload_0
      //   236: aload #5
      //   238: iload #6
      //   240: invokevirtual checkTerminated : (Ljava/lang/Object;Z)Z
      //   243: ifeq -> 247
      //   246: return
      //   247: aload_0
      //   248: getfield sourceMode : I
      //   251: iconst_1
      //   252: if_icmpeq -> 271
      //   255: aload_0
      //   256: getfield upstream : Ljava/util/concurrent/atomic/AtomicReference;
      //   259: invokevirtual get : ()Ljava/lang/Object;
      //   262: checkcast org/reactivestreams/Subscription
      //   265: lconst_1
      //   266: invokeinterface request : (J)V
      //   271: goto -> 583
      //   274: iconst_0
      //   275: istore #9
      //   277: iload #9
      //   279: i2l
      //   280: lstore #13
      //   282: lload #13
      //   284: lload #11
      //   286: lcmp
      //   287: ifge -> 538
      //   290: aload_0
      //   291: getfield terminalEvent : Ljava/lang/Object;
      //   294: astore #15
      //   296: aload #5
      //   298: invokeinterface poll : ()Ljava/lang/Object;
      //   303: astore #4
      //   305: goto -> 346
      //   308: astore #4
      //   310: aload #4
      //   312: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
      //   315: aload_0
      //   316: getfield upstream : Ljava/util/concurrent/atomic/AtomicReference;
      //   319: invokevirtual get : ()Ljava/lang/Object;
      //   322: checkcast org/reactivestreams/Subscription
      //   325: invokeinterface cancel : ()V
      //   330: aload #4
      //   332: invokestatic error : (Ljava/lang/Throwable;)Ljava/lang/Object;
      //   335: astore #15
      //   337: aload_0
      //   338: aload #15
      //   340: putfield terminalEvent : Ljava/lang/Object;
      //   343: aconst_null
      //   344: astore #4
      //   346: aload #4
      //   348: ifnonnull -> 357
      //   351: iconst_1
      //   352: istore #6
      //   354: goto -> 360
      //   357: iconst_0
      //   358: istore #6
      //   360: aload_0
      //   361: aload #15
      //   363: iload #6
      //   365: invokevirtual checkTerminated : (Ljava/lang/Object;Z)Z
      //   368: ifeq -> 372
      //   371: return
      //   372: iload #6
      //   374: ifeq -> 380
      //   377: goto -> 538
      //   380: aload #4
      //   382: invokestatic getValue : (Ljava/lang/Object;)Ljava/lang/Object;
      //   385: astore #4
      //   387: aload_2
      //   388: arraylength
      //   389: istore #8
      //   391: iconst_0
      //   392: istore #7
      //   394: iconst_0
      //   395: istore #10
      //   397: iload #7
      //   399: iload #8
      //   401: if_icmpge -> 471
      //   404: aload_2
      //   405: iload #7
      //   407: aaload
      //   408: astore #15
      //   410: aload #15
      //   412: invokevirtual get : ()J
      //   415: lstore #13
      //   417: lload #13
      //   419: ldc2_w -9223372036854775808
      //   422: lcmp
      //   423: ifeq -> 462
      //   426: lload #13
      //   428: ldc2_w 9223372036854775807
      //   431: lcmp
      //   432: ifeq -> 447
      //   435: aload #15
      //   437: aload #15
      //   439: getfield emitted : J
      //   442: lconst_1
      //   443: ladd
      //   444: putfield emitted : J
      //   447: aload #15
      //   449: getfield child : Lorg/reactivestreams/Subscriber;
      //   452: aload #4
      //   454: invokeinterface onNext : (Ljava/lang/Object;)V
      //   459: goto -> 465
      //   462: iconst_1
      //   463: istore #10
      //   465: iinc #7, 1
      //   468: goto -> 397
      //   471: iinc #9, 1
      //   474: aload_1
      //   475: invokevirtual get : ()Ljava/lang/Object;
      //   478: checkcast [Lio/reactivex/internal/operators/flowable/FlowablePublish$InnerSubscriber;
      //   481: astore #4
      //   483: iload #10
      //   485: ifne -> 500
      //   488: aload #4
      //   490: aload_2
      //   491: if_acmpeq -> 497
      //   494: goto -> 500
      //   497: goto -> 277
      //   500: iload #9
      //   502: ifeq -> 531
      //   505: aload_0
      //   506: getfield sourceMode : I
      //   509: iconst_1
      //   510: if_icmpeq -> 531
      //   513: aload_0
      //   514: getfield upstream : Ljava/util/concurrent/atomic/AtomicReference;
      //   517: invokevirtual get : ()Ljava/lang/Object;
      //   520: checkcast org/reactivestreams/Subscription
      //   523: iload #9
      //   525: i2l
      //   526: invokeinterface request : (J)V
      //   531: aload #4
      //   533: astore #5
      //   535: goto -> 610
      //   538: iload #9
      //   540: ifeq -> 571
      //   543: aload_0
      //   544: getfield sourceMode : I
      //   547: iconst_1
      //   548: if_icmpeq -> 571
      //   551: aload_0
      //   552: getfield upstream : Ljava/util/concurrent/atomic/AtomicReference;
      //   555: invokevirtual get : ()Ljava/lang/Object;
      //   558: checkcast org/reactivestreams/Subscription
      //   561: lload #13
      //   563: invokeinterface request : (J)V
      //   568: goto -> 571
      //   571: lload #11
      //   573: lconst_0
      //   574: lcmp
      //   575: ifeq -> 589
      //   578: iload #6
      //   580: ifne -> 589
      //   583: aload_2
      //   584: astore #5
      //   586: goto -> 610
      //   589: aload_0
      //   590: iload_3
      //   591: ineg
      //   592: invokevirtual addAndGet : (I)I
      //   595: istore_3
      //   596: iload_3
      //   597: ifne -> 601
      //   600: return
      //   601: aload_1
      //   602: invokevirtual get : ()Ljava/lang/Object;
      //   605: checkcast [Lio/reactivex/internal/operators/flowable/FlowablePublish$InnerSubscriber;
      //   608: astore #5
      //   610: aload #5
      //   612: astore_2
      //   613: goto -> 23
      // Exception table:
      //   from	to	target	type
      //   167	176	183	finally
      //   296	305	308	finally
    }
    
    public void dispose() {
      FlowablePublish.InnerSubscriber[] arrayOfInnerSubscriber1 = (FlowablePublish.InnerSubscriber[])this.subscribers.get();
      FlowablePublish.InnerSubscriber[] arrayOfInnerSubscriber2 = TERMINATED;
      if (arrayOfInnerSubscriber1 != arrayOfInnerSubscriber2 && (FlowablePublish.InnerSubscriber[])this.subscribers.getAndSet(arrayOfInnerSubscriber2) != TERMINATED) {
        this.current.compareAndSet(this, null);
        SubscriptionHelper.cancel(this.upstream);
      } 
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (this.subscribers.get() == TERMINATED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onComplete() {
      if (this.terminalEvent == null) {
        this.terminalEvent = NotificationLite.complete();
        dispatch();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.terminalEvent == null) {
        this.terminalEvent = NotificationLite.error(param1Throwable);
        dispatch();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      if (this.sourceMode == 0 && !this.queue.offer(param1T)) {
        onError((Throwable)new MissingBackpressureException("Prefetch queue is full?!"));
        return;
      } 
      dispatch();
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.setOnce(this.upstream, param1Subscription)) {
        if (param1Subscription instanceof QueueSubscription) {
          QueueSubscription queueSubscription = (QueueSubscription)param1Subscription;
          int i = queueSubscription.requestFusion(7);
          if (i == 1) {
            this.sourceMode = i;
            this.queue = (SimpleQueue<T>)queueSubscription;
            this.terminalEvent = NotificationLite.complete();
            dispatch();
            return;
          } 
          if (i == 2) {
            this.sourceMode = i;
            this.queue = (SimpleQueue<T>)queueSubscription;
            param1Subscription.request(this.bufferSize);
            return;
          } 
        } 
        this.queue = (SimpleQueue<T>)new SpscArrayQueue(this.bufferSize);
        param1Subscription.request(this.bufferSize);
      } 
    }
    
    void remove(FlowablePublish.InnerSubscriber<T> param1InnerSubscriber) {
      FlowablePublish.InnerSubscriber[] arrayOfInnerSubscriber1;
      FlowablePublish.InnerSubscriber[] arrayOfInnerSubscriber2;
      do {
        byte b2;
        arrayOfInnerSubscriber1 = (FlowablePublish.InnerSubscriber[])this.subscribers.get();
        int i = arrayOfInnerSubscriber1.length;
        if (i == 0)
          break; 
        byte b1 = -1;
        byte b = 0;
        while (true) {
          b2 = b1;
          if (b < i) {
            if (arrayOfInnerSubscriber1[b].equals(param1InnerSubscriber)) {
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
          arrayOfInnerSubscriber2 = EMPTY;
        } else {
          arrayOfInnerSubscriber2 = new FlowablePublish.InnerSubscriber[i - 1];
          System.arraycopy(arrayOfInnerSubscriber1, 0, arrayOfInnerSubscriber2, 0, b2);
          System.arraycopy(arrayOfInnerSubscriber1, b2 + 1, arrayOfInnerSubscriber2, b2, i - b2 - 1);
        } 
      } while (!this.subscribers.compareAndSet(arrayOfInnerSubscriber1, arrayOfInnerSubscriber2));
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowablePublish.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */