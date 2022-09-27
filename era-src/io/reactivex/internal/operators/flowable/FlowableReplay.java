package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.ResettableConnectable;
import io.reactivex.internal.fuseable.HasUpstreamPublisher;
import io.reactivex.internal.subscribers.SubscriberResourceWrapper;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Timed;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableReplay<T> extends ConnectableFlowable<T> implements HasUpstreamPublisher<T>, ResettableConnectable {
  static final Callable DEFAULT_UNBOUNDED_FACTORY = new DefaultUnboundedFactory();
  
  final Callable<? extends ReplayBuffer<T>> bufferFactory;
  
  final AtomicReference<ReplaySubscriber<T>> current;
  
  final Publisher<T> onSubscribe;
  
  final Flowable<T> source;
  
  private FlowableReplay(Publisher<T> paramPublisher, Flowable<T> paramFlowable, AtomicReference<ReplaySubscriber<T>> paramAtomicReference, Callable<? extends ReplayBuffer<T>> paramCallable) {
    this.onSubscribe = paramPublisher;
    this.source = paramFlowable;
    this.current = paramAtomicReference;
    this.bufferFactory = paramCallable;
  }
  
  public static <T> ConnectableFlowable<T> create(Flowable<T> paramFlowable, int paramInt) {
    return (paramInt == Integer.MAX_VALUE) ? createFrom(paramFlowable) : create(paramFlowable, new ReplayBufferTask(paramInt));
  }
  
  public static <T> ConnectableFlowable<T> create(Flowable<T> paramFlowable, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return create(paramFlowable, paramLong, paramTimeUnit, paramScheduler, 2147483647);
  }
  
  public static <T> ConnectableFlowable<T> create(Flowable<T> paramFlowable, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, int paramInt) {
    return create(paramFlowable, new ScheduledReplayBufferTask(paramInt, paramLong, paramTimeUnit, paramScheduler));
  }
  
  static <T> ConnectableFlowable<T> create(Flowable<T> paramFlowable, Callable<? extends ReplayBuffer<T>> paramCallable) {
    AtomicReference<ReplaySubscriber<T>> atomicReference = new AtomicReference();
    return RxJavaPlugins.onAssembly(new FlowableReplay<T>(new ReplayPublisher<T>(atomicReference, paramCallable), paramFlowable, atomicReference, paramCallable));
  }
  
  public static <T> ConnectableFlowable<T> createFrom(Flowable<? extends T> paramFlowable) {
    return create((Flowable)paramFlowable, DEFAULT_UNBOUNDED_FACTORY);
  }
  
  public static <U, R> Flowable<R> multicastSelector(Callable<? extends ConnectableFlowable<U>> paramCallable, Function<? super Flowable<U>, ? extends Publisher<R>> paramFunction) {
    return new MulticastFlowable<R, U>(paramCallable, paramFunction);
  }
  
  public static <T> ConnectableFlowable<T> observeOn(ConnectableFlowable<T> paramConnectableFlowable, Scheduler paramScheduler) {
    return RxJavaPlugins.onAssembly(new ConnectableFlowableReplay<T>(paramConnectableFlowable, paramConnectableFlowable.observeOn(paramScheduler)));
  }
  
  public void connect(Consumer<? super Disposable> paramConsumer) {
    // Byte code:
    //   0: aload_0
    //   1: getfield current : Ljava/util/concurrent/atomic/AtomicReference;
    //   4: invokevirtual get : ()Ljava/lang/Object;
    //   7: checkcast io/reactivex/internal/operators/flowable/FlowableReplay$ReplaySubscriber
    //   10: astore_2
    //   11: aload_2
    //   12: ifnull -> 24
    //   15: aload_2
    //   16: astore_3
    //   17: aload_2
    //   18: invokevirtual isDisposed : ()Z
    //   21: ifeq -> 61
    //   24: aload_0
    //   25: getfield bufferFactory : Ljava/util/concurrent/Callable;
    //   28: invokeinterface call : ()Ljava/lang/Object;
    //   33: checkcast io/reactivex/internal/operators/flowable/FlowableReplay$ReplayBuffer
    //   36: astore_3
    //   37: new io/reactivex/internal/operators/flowable/FlowableReplay$ReplaySubscriber
    //   40: dup
    //   41: aload_3
    //   42: invokespecial <init> : (Lio/reactivex/internal/operators/flowable/FlowableReplay$ReplayBuffer;)V
    //   45: astore_3
    //   46: aload_0
    //   47: getfield current : Ljava/util/concurrent/atomic/AtomicReference;
    //   50: aload_2
    //   51: aload_3
    //   52: invokevirtual compareAndSet : (Ljava/lang/Object;Ljava/lang/Object;)Z
    //   55: ifne -> 61
    //   58: goto -> 0
    //   61: aload_3
    //   62: getfield shouldConnect : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   65: invokevirtual get : ()Z
    //   68: ifne -> 89
    //   71: aload_3
    //   72: getfield shouldConnect : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   75: iconst_0
    //   76: iconst_1
    //   77: invokevirtual compareAndSet : (ZZ)Z
    //   80: ifeq -> 89
    //   83: iconst_1
    //   84: istore #4
    //   86: goto -> 92
    //   89: iconst_0
    //   90: istore #4
    //   92: aload_1
    //   93: aload_3
    //   94: invokeinterface accept : (Ljava/lang/Object;)V
    //   99: iload #4
    //   101: ifeq -> 112
    //   104: aload_0
    //   105: getfield source : Lio/reactivex/Flowable;
    //   108: aload_3
    //   109: invokevirtual subscribe : (Lio/reactivex/FlowableSubscriber;)V
    //   112: return
    //   113: astore_1
    //   114: iload #4
    //   116: ifeq -> 129
    //   119: aload_3
    //   120: getfield shouldConnect : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   123: iconst_1
    //   124: iconst_0
    //   125: invokevirtual compareAndSet : (ZZ)Z
    //   128: pop
    //   129: aload_1
    //   130: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
    //   133: aload_1
    //   134: invokestatic wrapOrThrow : (Ljava/lang/Throwable;)Ljava/lang/RuntimeException;
    //   137: athrow
    //   138: astore_1
    //   139: aload_1
    //   140: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
    //   143: aload_1
    //   144: invokestatic wrapOrThrow : (Ljava/lang/Throwable;)Ljava/lang/RuntimeException;
    //   147: astore_1
    //   148: goto -> 153
    //   151: aload_1
    //   152: athrow
    //   153: goto -> 151
    // Exception table:
    //   from	to	target	type
    //   24	37	138	finally
    //   92	99	113	finally
  }
  
  public void resetIf(Disposable paramDisposable) {
    this.current.compareAndSet((ReplaySubscriber<T>)paramDisposable, null);
  }
  
  public Publisher<T> source() {
    return (Publisher<T>)this.source;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.onSubscribe.subscribe(paramSubscriber);
  }
  
  static class BoundedReplayBuffer<T> extends AtomicReference<Node> implements ReplayBuffer<T> {
    private static final long serialVersionUID = 2346567790059478686L;
    
    long index;
    
    int size;
    
    FlowableReplay.Node tail;
    
    BoundedReplayBuffer() {
      FlowableReplay.Node node = new FlowableReplay.Node(null, 0L);
      this.tail = node;
      set(node);
    }
    
    final void addLast(FlowableReplay.Node param1Node) {
      this.tail.set(param1Node);
      this.tail = param1Node;
      this.size++;
    }
    
    final void collect(Collection<? super T> param1Collection) {
      FlowableReplay.Node node = getHead();
      while (true) {
        node = node.get();
        if (node != null) {
          Object object = leaveTransform(node.value);
          if (NotificationLite.isComplete(object) || NotificationLite.isError(object))
            break; 
          param1Collection.add((T)NotificationLite.getValue(object));
          continue;
        } 
        break;
      } 
    }
    
    public final void complete() {
      Object object = enterTransform(NotificationLite.complete());
      long l = this.index + 1L;
      this.index = l;
      addLast(new FlowableReplay.Node(object, l));
      truncateFinal();
    }
    
    Object enterTransform(Object param1Object) {
      return param1Object;
    }
    
    public final void error(Throwable param1Throwable) {
      Object object = enterTransform(NotificationLite.error(param1Throwable));
      long l = this.index + 1L;
      this.index = l;
      addLast(new FlowableReplay.Node(object, l));
      truncateFinal();
    }
    
    FlowableReplay.Node getHead() {
      return get();
    }
    
    boolean hasCompleted() {
      boolean bool;
      if (this.tail.value != null && NotificationLite.isComplete(leaveTransform(this.tail.value))) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean hasError() {
      boolean bool;
      if (this.tail.value != null && NotificationLite.isError(leaveTransform(this.tail.value))) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    Object leaveTransform(Object param1Object) {
      return param1Object;
    }
    
    public final void next(T param1T) {
      param1T = (T)enterTransform(NotificationLite.next(param1T));
      long l = this.index + 1L;
      this.index = l;
      addLast(new FlowableReplay.Node(param1T, l));
      truncate();
    }
    
    final void removeFirst() {
      FlowableReplay.Node node = get().get();
      if (node != null) {
        this.size--;
        setFirst(node);
        return;
      } 
      throw new IllegalStateException("Empty list!");
    }
    
    final void removeSome(int param1Int) {
      FlowableReplay.Node node = get();
      while (param1Int > 0) {
        node = node.get();
        param1Int--;
        this.size--;
      } 
      setFirst(node);
      node = get();
      if (node.get() == null)
        this.tail = node; 
    }
    
    public final void replay(FlowableReplay.InnerSubscription<T> param1InnerSubscription) {
      // Byte code:
      //   0: aload_1
      //   1: monitorenter
      //   2: aload_1
      //   3: getfield emitting : Z
      //   6: ifeq -> 17
      //   9: aload_1
      //   10: iconst_1
      //   11: putfield missed : Z
      //   14: aload_1
      //   15: monitorexit
      //   16: return
      //   17: aload_1
      //   18: iconst_1
      //   19: putfield emitting : Z
      //   22: aload_1
      //   23: monitorexit
      //   24: aload_1
      //   25: invokevirtual isDisposed : ()Z
      //   28: ifeq -> 37
      //   31: aload_1
      //   32: aconst_null
      //   33: putfield index : Ljava/lang/Object;
      //   36: return
      //   37: aload_1
      //   38: invokevirtual get : ()J
      //   41: lstore_2
      //   42: lload_2
      //   43: ldc2_w 9223372036854775807
      //   46: lcmp
      //   47: ifne -> 56
      //   50: iconst_1
      //   51: istore #4
      //   53: goto -> 59
      //   56: iconst_0
      //   57: istore #4
      //   59: aload_1
      //   60: invokevirtual index : ()Ljava/lang/Object;
      //   63: checkcast io/reactivex/internal/operators/flowable/FlowableReplay$Node
      //   66: astore #5
      //   68: aload #5
      //   70: astore #6
      //   72: aload #5
      //   74: ifnonnull -> 102
      //   77: aload_0
      //   78: invokevirtual getHead : ()Lio/reactivex/internal/operators/flowable/FlowableReplay$Node;
      //   81: astore #6
      //   83: aload_1
      //   84: aload #6
      //   86: putfield index : Ljava/lang/Object;
      //   89: aload_1
      //   90: getfield totalRequested : Ljava/util/concurrent/atomic/AtomicLong;
      //   93: aload #6
      //   95: getfield index : J
      //   98: invokestatic add : (Ljava/util/concurrent/atomic/AtomicLong;J)J
      //   101: pop2
      //   102: lconst_0
      //   103: lstore #7
      //   105: lload_2
      //   106: lconst_0
      //   107: lcmp
      //   108: ifeq -> 229
      //   111: aload #6
      //   113: invokevirtual get : ()Ljava/lang/Object;
      //   116: checkcast io/reactivex/internal/operators/flowable/FlowableReplay$Node
      //   119: astore #5
      //   121: aload #5
      //   123: ifnull -> 229
      //   126: aload_0
      //   127: aload #5
      //   129: getfield value : Ljava/lang/Object;
      //   132: invokevirtual leaveTransform : (Ljava/lang/Object;)Ljava/lang/Object;
      //   135: astore #6
      //   137: aload #6
      //   139: aload_1
      //   140: getfield child : Lorg/reactivestreams/Subscriber;
      //   143: invokestatic accept : (Ljava/lang/Object;Lorg/reactivestreams/Subscriber;)Z
      //   146: ifeq -> 155
      //   149: aload_1
      //   150: aconst_null
      //   151: putfield index : Ljava/lang/Object;
      //   154: return
      //   155: lload #7
      //   157: lconst_1
      //   158: ladd
      //   159: lstore #7
      //   161: lload_2
      //   162: lconst_1
      //   163: lsub
      //   164: lstore_2
      //   165: aload_1
      //   166: invokevirtual isDisposed : ()Z
      //   169: ifeq -> 178
      //   172: aload_1
      //   173: aconst_null
      //   174: putfield index : Ljava/lang/Object;
      //   177: return
      //   178: aload #5
      //   180: astore #6
      //   182: goto -> 105
      //   185: astore #5
      //   187: aload #5
      //   189: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
      //   192: aload_1
      //   193: aconst_null
      //   194: putfield index : Ljava/lang/Object;
      //   197: aload_1
      //   198: invokevirtual dispose : ()V
      //   201: aload #6
      //   203: invokestatic isError : (Ljava/lang/Object;)Z
      //   206: ifne -> 228
      //   209: aload #6
      //   211: invokestatic isComplete : (Ljava/lang/Object;)Z
      //   214: ifne -> 228
      //   217: aload_1
      //   218: getfield child : Lorg/reactivestreams/Subscriber;
      //   221: aload #5
      //   223: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   228: return
      //   229: lload #7
      //   231: lconst_0
      //   232: lcmp
      //   233: ifeq -> 254
      //   236: aload_1
      //   237: aload #6
      //   239: putfield index : Ljava/lang/Object;
      //   242: iload #4
      //   244: ifne -> 254
      //   247: aload_1
      //   248: lload #7
      //   250: invokevirtual produced : (J)J
      //   253: pop2
      //   254: aload_1
      //   255: monitorenter
      //   256: aload_1
      //   257: getfield missed : Z
      //   260: ifne -> 271
      //   263: aload_1
      //   264: iconst_0
      //   265: putfield emitting : Z
      //   268: aload_1
      //   269: monitorexit
      //   270: return
      //   271: aload_1
      //   272: iconst_0
      //   273: putfield missed : Z
      //   276: aload_1
      //   277: monitorexit
      //   278: goto -> 24
      //   281: astore #6
      //   283: aload_1
      //   284: monitorexit
      //   285: aload #6
      //   287: athrow
      //   288: astore #6
      //   290: aload_1
      //   291: monitorexit
      //   292: goto -> 298
      //   295: aload #6
      //   297: athrow
      //   298: goto -> 295
      // Exception table:
      //   from	to	target	type
      //   2	16	288	finally
      //   17	24	288	finally
      //   137	154	185	finally
      //   256	270	281	finally
      //   271	278	281	finally
      //   283	285	281	finally
      //   290	292	288	finally
    }
    
    final void setFirst(FlowableReplay.Node param1Node) {
      set(param1Node);
    }
    
    final void trimHead() {
      FlowableReplay.Node node = get();
      if (node.value != null) {
        FlowableReplay.Node node1 = new FlowableReplay.Node(null, 0L);
        node1.lazySet(node.get());
        set(node1);
      } 
    }
    
    void truncate() {}
    
    void truncateFinal() {
      trimHead();
    }
  }
  
  static final class ConnectableFlowableReplay<T> extends ConnectableFlowable<T> {
    private final ConnectableFlowable<T> cf;
    
    private final Flowable<T> flowable;
    
    ConnectableFlowableReplay(ConnectableFlowable<T> param1ConnectableFlowable, Flowable<T> param1Flowable) {
      this.cf = param1ConnectableFlowable;
      this.flowable = param1Flowable;
    }
    
    public void connect(Consumer<? super Disposable> param1Consumer) {
      this.cf.connect(param1Consumer);
    }
    
    protected void subscribeActual(Subscriber<? super T> param1Subscriber) {
      this.flowable.subscribe(param1Subscriber);
    }
  }
  
  static final class DefaultUnboundedFactory implements Callable<Object> {
    public Object call() {
      return new FlowableReplay.UnboundedReplayBuffer(16);
    }
  }
  
  static final class InnerSubscription<T> extends AtomicLong implements Subscription, Disposable {
    static final long CANCELLED = -9223372036854775808L;
    
    private static final long serialVersionUID = -4453897557930727610L;
    
    final Subscriber<? super T> child;
    
    boolean emitting;
    
    Object index;
    
    boolean missed;
    
    final FlowableReplay.ReplaySubscriber<T> parent;
    
    final AtomicLong totalRequested;
    
    InnerSubscription(FlowableReplay.ReplaySubscriber<T> param1ReplaySubscriber, Subscriber<? super T> param1Subscriber) {
      this.parent = param1ReplaySubscriber;
      this.child = param1Subscriber;
      this.totalRequested = new AtomicLong();
    }
    
    public void cancel() {
      dispose();
    }
    
    public void dispose() {
      if (getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
        this.parent.remove(this);
        this.parent.manageRequests();
        this.index = null;
      } 
    }
    
    <U> U index() {
      return (U)this.index;
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (get() == Long.MIN_VALUE) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public long produced(long param1Long) {
      return BackpressureHelper.producedCancel(this, param1Long);
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long) && BackpressureHelper.addCancel(this, param1Long) != Long.MIN_VALUE) {
        BackpressureHelper.add(this.totalRequested, param1Long);
        this.parent.manageRequests();
        this.parent.buffer.replay(this);
      } 
    }
  }
  
  static final class MulticastFlowable<R, U> extends Flowable<R> {
    private final Callable<? extends ConnectableFlowable<U>> connectableFactory;
    
    private final Function<? super Flowable<U>, ? extends Publisher<R>> selector;
    
    MulticastFlowable(Callable<? extends ConnectableFlowable<U>> param1Callable, Function<? super Flowable<U>, ? extends Publisher<R>> param1Function) {
      this.connectableFactory = param1Callable;
      this.selector = param1Function;
    }
    
    protected void subscribeActual(Subscriber<? super R> param1Subscriber) {
      SubscriberResourceWrapper<R> subscriberResourceWrapper;
      try {
      
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        EmptySubscription.error(exception, (Subscriber)subscriberResourceWrapper);
      } 
    }
    
    final class DisposableConsumer implements Consumer<Disposable> {
      private final SubscriberResourceWrapper<R> srw;
      
      DisposableConsumer(SubscriberResourceWrapper<R> param2SubscriberResourceWrapper) {
        this.srw = param2SubscriberResourceWrapper;
      }
      
      public void accept(Disposable param2Disposable) {
        this.srw.setResource(param2Disposable);
      }
    }
  }
  
  final class DisposableConsumer implements Consumer<Disposable> {
    private final SubscriberResourceWrapper<R> srw;
    
    DisposableConsumer(SubscriberResourceWrapper<R> param1SubscriberResourceWrapper) {
      this.srw = param1SubscriberResourceWrapper;
    }
    
    public void accept(Disposable param1Disposable) {
      this.srw.setResource(param1Disposable);
    }
  }
  
  static final class Node extends AtomicReference<Node> {
    private static final long serialVersionUID = 245354315435971818L;
    
    final long index;
    
    final Object value;
    
    Node(Object param1Object, long param1Long) {
      this.value = param1Object;
      this.index = param1Long;
    }
  }
  
  static interface ReplayBuffer<T> {
    void complete();
    
    void error(Throwable param1Throwable);
    
    void next(T param1T);
    
    void replay(FlowableReplay.InnerSubscription<T> param1InnerSubscription);
  }
  
  static final class ReplayBufferTask<T> implements Callable<ReplayBuffer<T>> {
    private final int bufferSize;
    
    ReplayBufferTask(int param1Int) {
      this.bufferSize = param1Int;
    }
    
    public FlowableReplay.ReplayBuffer<T> call() {
      return new FlowableReplay.SizeBoundReplayBuffer<T>(this.bufferSize);
    }
  }
  
  static final class ReplayPublisher<T> implements Publisher<T> {
    private final Callable<? extends FlowableReplay.ReplayBuffer<T>> bufferFactory;
    
    private final AtomicReference<FlowableReplay.ReplaySubscriber<T>> curr;
    
    ReplayPublisher(AtomicReference<FlowableReplay.ReplaySubscriber<T>> param1AtomicReference, Callable<? extends FlowableReplay.ReplayBuffer<T>> param1Callable) {
      this.curr = param1AtomicReference;
      this.bufferFactory = param1Callable;
    }
    
    public void subscribe(Subscriber<? super T> param1Subscriber) {
      FlowableReplay.ReplaySubscriber<T> replaySubscriber;
      while (true) {
        FlowableReplay.ReplaySubscriber<T> replaySubscriber1 = this.curr.get();
        replaySubscriber = replaySubscriber1;
        if (replaySubscriber1 == null)
          try {
          
          } finally {
            replaySubscriber = null;
            Exceptions.throwIfFatal((Throwable)replaySubscriber);
            EmptySubscription.error((Throwable)replaySubscriber, param1Subscriber);
          }  
        break;
      } 
      FlowableReplay.InnerSubscription<T> innerSubscription = new FlowableReplay.InnerSubscription<T>(replaySubscriber, param1Subscriber);
      param1Subscriber.onSubscribe(innerSubscription);
      replaySubscriber.add(innerSubscription);
      if (innerSubscription.isDisposed()) {
        replaySubscriber.remove(innerSubscription);
        return;
      } 
      replaySubscriber.manageRequests();
      replaySubscriber.buffer.replay(innerSubscription);
    }
  }
  
  static final class ReplaySubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<T>, Disposable {
    static final FlowableReplay.InnerSubscription[] EMPTY = new FlowableReplay.InnerSubscription[0];
    
    static final FlowableReplay.InnerSubscription[] TERMINATED = new FlowableReplay.InnerSubscription[0];
    
    private static final long serialVersionUID = 7224554242710036740L;
    
    final FlowableReplay.ReplayBuffer<T> buffer;
    
    boolean done;
    
    final AtomicInteger management;
    
    long maxChildRequested;
    
    long maxUpstreamRequested;
    
    final AtomicBoolean shouldConnect;
    
    final AtomicReference<FlowableReplay.InnerSubscription<T>[]> subscribers;
    
    ReplaySubscriber(FlowableReplay.ReplayBuffer<T> param1ReplayBuffer) {
      this.buffer = param1ReplayBuffer;
      this.management = new AtomicInteger();
      this.subscribers = new AtomicReference(EMPTY);
      this.shouldConnect = new AtomicBoolean();
    }
    
    boolean add(FlowableReplay.InnerSubscription<T> param1InnerSubscription) {
      if (param1InnerSubscription != null)
        while (true) {
          FlowableReplay.InnerSubscription[] arrayOfInnerSubscription1 = (FlowableReplay.InnerSubscription[])this.subscribers.get();
          if (arrayOfInnerSubscription1 == TERMINATED)
            return false; 
          int i = arrayOfInnerSubscription1.length;
          FlowableReplay.InnerSubscription[] arrayOfInnerSubscription2 = new FlowableReplay.InnerSubscription[i + 1];
          System.arraycopy(arrayOfInnerSubscription1, 0, arrayOfInnerSubscription2, 0, i);
          arrayOfInnerSubscription2[i] = param1InnerSubscription;
          if (this.subscribers.compareAndSet(arrayOfInnerSubscription1, arrayOfInnerSubscription2))
            return true; 
        }  
      NullPointerException nullPointerException = new NullPointerException();
      throw nullPointerException;
    }
    
    public void dispose() {
      this.subscribers.set(TERMINATED);
      SubscriptionHelper.cancel(this);
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
    
    void manageRequests() {
      int j;
      if (this.management.getAndIncrement() != 0)
        return; 
      int i = 1;
      do {
        if (isDisposed())
          return; 
        FlowableReplay.InnerSubscription[] arrayOfInnerSubscription = (FlowableReplay.InnerSubscription[])this.subscribers.get();
        long l1 = this.maxChildRequested;
        int k = arrayOfInnerSubscription.length;
        j = 0;
        long l2 = l1;
        while (j < k) {
          l2 = Math.max(l2, (arrayOfInnerSubscription[j]).totalRequested.get());
          j++;
        } 
        long l3 = this.maxUpstreamRequested;
        Subscription subscription = get();
        l1 = l2 - l1;
        if (l1 != 0L) {
          this.maxChildRequested = l2;
          if (subscription != null) {
            if (l3 != 0L) {
              this.maxUpstreamRequested = 0L;
              subscription.request(l3 + l1);
            } else {
              subscription.request(l1);
            } 
          } else {
            l1 = l3 + l1;
            l2 = l1;
            if (l1 < 0L)
              l2 = Long.MAX_VALUE; 
            this.maxUpstreamRequested = l2;
          } 
        } else if (l3 != 0L && subscription != null) {
          this.maxUpstreamRequested = 0L;
          subscription.request(l3);
        } 
        j = this.management.addAndGet(-i);
        i = j;
      } while (j != 0);
    }
    
    public void onComplete() {
      if (!this.done) {
        this.done = true;
        this.buffer.complete();
        for (FlowableReplay.InnerSubscription<T> innerSubscription : (FlowableReplay.InnerSubscription[])this.subscribers.getAndSet(TERMINATED))
          this.buffer.replay(innerSubscription); 
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (!this.done) {
        this.done = true;
        this.buffer.error(param1Throwable);
        for (FlowableReplay.InnerSubscription<T> innerSubscription : (FlowableReplay.InnerSubscription[])this.subscribers.getAndSet(TERMINATED))
          this.buffer.replay(innerSubscription); 
      } else {
        RxJavaPlugins.onError((Throwable)innerSubscription);
      } 
    }
    
    public void onNext(T param1T) {
      if (!this.done) {
        this.buffer.next(param1T);
        for (FlowableReplay.InnerSubscription<T> innerSubscription : (FlowableReplay.InnerSubscription[])this.subscribers.get())
          this.buffer.replay(innerSubscription); 
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.setOnce(this, param1Subscription)) {
        manageRequests();
        for (Subscription param1Subscription : (FlowableReplay.InnerSubscription[])this.subscribers.get())
          this.buffer.replay((FlowableReplay.InnerSubscription<T>)param1Subscription); 
      } 
    }
    
    void remove(FlowableReplay.InnerSubscription<T> param1InnerSubscription) {
      FlowableReplay.InnerSubscription[] arrayOfInnerSubscription1;
      FlowableReplay.InnerSubscription[] arrayOfInnerSubscription2;
      do {
        byte b2;
        arrayOfInnerSubscription1 = (FlowableReplay.InnerSubscription[])this.subscribers.get();
        int i = arrayOfInnerSubscription1.length;
        if (i == 0)
          return; 
        byte b1 = -1;
        byte b = 0;
        while (true) {
          b2 = b1;
          if (b < i) {
            if (arrayOfInnerSubscription1[b].equals(param1InnerSubscription)) {
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
          arrayOfInnerSubscription2 = EMPTY;
        } else {
          arrayOfInnerSubscription2 = new FlowableReplay.InnerSubscription[i - 1];
          System.arraycopy(arrayOfInnerSubscription1, 0, arrayOfInnerSubscription2, 0, b2);
          System.arraycopy(arrayOfInnerSubscription1, b2 + 1, arrayOfInnerSubscription2, b2, i - b2 - 1);
        } 
      } while (!this.subscribers.compareAndSet(arrayOfInnerSubscription1, arrayOfInnerSubscription2));
    }
  }
  
  static final class ScheduledReplayBufferTask<T> implements Callable<ReplayBuffer<T>> {
    private final int bufferSize;
    
    private final long maxAge;
    
    private final Scheduler scheduler;
    
    private final TimeUnit unit;
    
    ScheduledReplayBufferTask(int param1Int, long param1Long, TimeUnit param1TimeUnit, Scheduler param1Scheduler) {
      this.bufferSize = param1Int;
      this.maxAge = param1Long;
      this.unit = param1TimeUnit;
      this.scheduler = param1Scheduler;
    }
    
    public FlowableReplay.ReplayBuffer<T> call() {
      return new FlowableReplay.SizeAndTimeBoundReplayBuffer<T>(this.bufferSize, this.maxAge, this.unit, this.scheduler);
    }
  }
  
  static final class SizeAndTimeBoundReplayBuffer<T> extends BoundedReplayBuffer<T> {
    private static final long serialVersionUID = 3457957419649567404L;
    
    final int limit;
    
    final long maxAge;
    
    final Scheduler scheduler;
    
    final TimeUnit unit;
    
    SizeAndTimeBoundReplayBuffer(int param1Int, long param1Long, TimeUnit param1TimeUnit, Scheduler param1Scheduler) {
      this.scheduler = param1Scheduler;
      this.limit = param1Int;
      this.maxAge = param1Long;
      this.unit = param1TimeUnit;
    }
    
    Object enterTransform(Object param1Object) {
      return new Timed(param1Object, this.scheduler.now(this.unit), this.unit);
    }
    
    FlowableReplay.Node getHead() {
      long l1 = this.scheduler.now(this.unit);
      long l2 = this.maxAge;
      FlowableReplay.Node node1 = get();
      FlowableReplay.Node node2 = node1.get();
      while (node2 != null) {
        Timed timed = (Timed)node2.value;
        if (!NotificationLite.isComplete(timed.value()) && !NotificationLite.isError(timed.value()) && timed.time() <= l1 - l2) {
          FlowableReplay.Node node = node2.get();
          node1 = node2;
          node2 = node;
          continue;
        } 
        break;
      } 
      return node1;
    }
    
    Object leaveTransform(Object param1Object) {
      return ((Timed)param1Object).value();
    }
    
    void truncate() {
      long l1 = this.scheduler.now(this.unit);
      long l2 = this.maxAge;
      FlowableReplay.Node node1 = get();
      FlowableReplay.Node node2 = node1.get();
      byte b = 0;
      while (node2 != null) {
        if (this.size > this.limit && this.size > 1) {
          b++;
          this.size--;
          FlowableReplay.Node node = node2.get();
          node1 = node2;
          node2 = node;
          continue;
        } 
        if (((Timed)node2.value).time() <= l1 - l2) {
          b++;
          this.size--;
          FlowableReplay.Node node = node2.get();
          node1 = node2;
          node2 = node;
        } 
      } 
      if (b != 0)
        setFirst(node1); 
    }
    
    void truncateFinal() {
      long l1 = this.scheduler.now(this.unit);
      long l2 = this.maxAge;
      FlowableReplay.Node node1 = get();
      FlowableReplay.Node node2 = node1.get();
      byte b = 0;
      while (node2 != null && this.size > 1 && ((Timed)node2.value).time() <= l1 - l2) {
        b++;
        this.size--;
        FlowableReplay.Node node = node2.get();
        node1 = node2;
        node2 = node;
      } 
      if (b != 0)
        setFirst(node1); 
    }
  }
  
  static final class SizeBoundReplayBuffer<T> extends BoundedReplayBuffer<T> {
    private static final long serialVersionUID = -5898283885385201806L;
    
    final int limit;
    
    SizeBoundReplayBuffer(int param1Int) {
      this.limit = param1Int;
    }
    
    void truncate() {
      if (this.size > this.limit)
        removeFirst(); 
    }
  }
  
  static final class UnboundedReplayBuffer<T> extends ArrayList<Object> implements ReplayBuffer<T> {
    private static final long serialVersionUID = 7063189396499112664L;
    
    volatile int size;
    
    UnboundedReplayBuffer(int param1Int) {
      super(param1Int);
    }
    
    public void complete() {
      add(NotificationLite.complete());
      this.size++;
    }
    
    public void error(Throwable param1Throwable) {
      add(NotificationLite.error(param1Throwable));
      this.size++;
    }
    
    public void next(T param1T) {
      add(NotificationLite.next(param1T));
      this.size++;
    }
    
    public void replay(FlowableReplay.InnerSubscription<T> param1InnerSubscription) {
      // Byte code:
      //   0: aload_1
      //   1: monitorenter
      //   2: aload_1
      //   3: getfield emitting : Z
      //   6: ifeq -> 17
      //   9: aload_1
      //   10: iconst_1
      //   11: putfield missed : Z
      //   14: aload_1
      //   15: monitorexit
      //   16: return
      //   17: aload_1
      //   18: iconst_1
      //   19: putfield emitting : Z
      //   22: aload_1
      //   23: monitorexit
      //   24: aload_1
      //   25: getfield child : Lorg/reactivestreams/Subscriber;
      //   28: astore_2
      //   29: aload_1
      //   30: invokevirtual isDisposed : ()Z
      //   33: ifeq -> 37
      //   36: return
      //   37: aload_0
      //   38: getfield size : I
      //   41: istore_3
      //   42: aload_1
      //   43: invokevirtual index : ()Ljava/lang/Object;
      //   46: checkcast java/lang/Integer
      //   49: astore #4
      //   51: aload #4
      //   53: ifnull -> 66
      //   56: aload #4
      //   58: invokevirtual intValue : ()I
      //   61: istore #5
      //   63: goto -> 69
      //   66: iconst_0
      //   67: istore #5
      //   69: aload_1
      //   70: invokevirtual get : ()J
      //   73: lstore #6
      //   75: lload #6
      //   77: lstore #8
      //   79: lconst_0
      //   80: lstore #10
      //   82: lload #8
      //   84: lconst_0
      //   85: lcmp
      //   86: ifeq -> 179
      //   89: iload #5
      //   91: iload_3
      //   92: if_icmpge -> 179
      //   95: aload_0
      //   96: iload #5
      //   98: invokevirtual get : (I)Ljava/lang/Object;
      //   101: astore #12
      //   103: aload #12
      //   105: aload_2
      //   106: invokestatic accept : (Ljava/lang/Object;Lorg/reactivestreams/Subscriber;)Z
      //   109: istore #13
      //   111: iload #13
      //   113: ifeq -> 117
      //   116: return
      //   117: aload_1
      //   118: invokevirtual isDisposed : ()Z
      //   121: ifeq -> 125
      //   124: return
      //   125: iinc #5, 1
      //   128: lload #8
      //   130: lconst_1
      //   131: lsub
      //   132: lstore #8
      //   134: lload #10
      //   136: lconst_1
      //   137: ladd
      //   138: lstore #10
      //   140: goto -> 82
      //   143: astore #4
      //   145: aload #4
      //   147: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
      //   150: aload_1
      //   151: invokevirtual dispose : ()V
      //   154: aload #12
      //   156: invokestatic isError : (Ljava/lang/Object;)Z
      //   159: ifne -> 178
      //   162: aload #12
      //   164: invokestatic isComplete : (Ljava/lang/Object;)Z
      //   167: ifne -> 178
      //   170: aload_2
      //   171: aload #4
      //   173: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   178: return
      //   179: lload #10
      //   181: lconst_0
      //   182: lcmp
      //   183: ifeq -> 211
      //   186: aload_1
      //   187: iload #5
      //   189: invokestatic valueOf : (I)Ljava/lang/Integer;
      //   192: putfield index : Ljava/lang/Object;
      //   195: lload #6
      //   197: ldc2_w 9223372036854775807
      //   200: lcmp
      //   201: ifeq -> 211
      //   204: aload_1
      //   205: lload #10
      //   207: invokevirtual produced : (J)J
      //   210: pop2
      //   211: aload_1
      //   212: monitorenter
      //   213: aload_1
      //   214: getfield missed : Z
      //   217: ifne -> 228
      //   220: aload_1
      //   221: iconst_0
      //   222: putfield emitting : Z
      //   225: aload_1
      //   226: monitorexit
      //   227: return
      //   228: aload_1
      //   229: iconst_0
      //   230: putfield missed : Z
      //   233: aload_1
      //   234: monitorexit
      //   235: goto -> 29
      //   238: astore_2
      //   239: aload_1
      //   240: monitorexit
      //   241: aload_2
      //   242: athrow
      //   243: astore_2
      //   244: aload_1
      //   245: monitorexit
      //   246: goto -> 251
      //   249: aload_2
      //   250: athrow
      //   251: goto -> 249
      // Exception table:
      //   from	to	target	type
      //   2	16	243	finally
      //   17	24	243	finally
      //   103	111	143	finally
      //   213	227	238	finally
      //   228	235	238	finally
      //   239	241	238	finally
      //   244	246	243	finally
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableReplay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */