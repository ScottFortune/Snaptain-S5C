package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.disposables.ResettableConnectable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.HasUpstreamObservableSource;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Timed;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableReplay<T> extends ConnectableObservable<T> implements HasUpstreamObservableSource<T>, ResettableConnectable {
  static final BufferSupplier DEFAULT_UNBOUNDED_FACTORY = new UnBoundedFactory();
  
  final BufferSupplier<T> bufferFactory;
  
  final AtomicReference<ReplayObserver<T>> current;
  
  final ObservableSource<T> onSubscribe;
  
  final ObservableSource<T> source;
  
  private ObservableReplay(ObservableSource<T> paramObservableSource1, ObservableSource<T> paramObservableSource2, AtomicReference<ReplayObserver<T>> paramAtomicReference, BufferSupplier<T> paramBufferSupplier) {
    this.onSubscribe = paramObservableSource1;
    this.source = paramObservableSource2;
    this.current = paramAtomicReference;
    this.bufferFactory = paramBufferSupplier;
  }
  
  public static <T> ConnectableObservable<T> create(ObservableSource<T> paramObservableSource, int paramInt) {
    return (paramInt == Integer.MAX_VALUE) ? createFrom(paramObservableSource) : create(paramObservableSource, new ReplayBufferSupplier<T>(paramInt));
  }
  
  public static <T> ConnectableObservable<T> create(ObservableSource<T> paramObservableSource, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return create(paramObservableSource, paramLong, paramTimeUnit, paramScheduler, 2147483647);
  }
  
  public static <T> ConnectableObservable<T> create(ObservableSource<T> paramObservableSource, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, int paramInt) {
    return create(paramObservableSource, new ScheduledReplaySupplier<T>(paramInt, paramLong, paramTimeUnit, paramScheduler));
  }
  
  static <T> ConnectableObservable<T> create(ObservableSource<T> paramObservableSource, BufferSupplier<T> paramBufferSupplier) {
    AtomicReference<ReplayObserver<T>> atomicReference = new AtomicReference();
    return RxJavaPlugins.onAssembly(new ObservableReplay<T>(new ReplaySource<T>(atomicReference, paramBufferSupplier), paramObservableSource, atomicReference, paramBufferSupplier));
  }
  
  public static <T> ConnectableObservable<T> createFrom(ObservableSource<? extends T> paramObservableSource) {
    return create((ObservableSource)paramObservableSource, DEFAULT_UNBOUNDED_FACTORY);
  }
  
  public static <U, R> Observable<R> multicastSelector(Callable<? extends ConnectableObservable<U>> paramCallable, Function<? super Observable<U>, ? extends ObservableSource<R>> paramFunction) {
    return RxJavaPlugins.onAssembly(new MulticastReplay<R, U>(paramCallable, paramFunction));
  }
  
  public static <T> ConnectableObservable<T> observeOn(ConnectableObservable<T> paramConnectableObservable, Scheduler paramScheduler) {
    return RxJavaPlugins.onAssembly(new Replay<T>(paramConnectableObservable, paramConnectableObservable.observeOn(paramScheduler)));
  }
  
  public void connect(Consumer<? super Disposable> paramConsumer) {
    // Byte code:
    //   0: aload_0
    //   1: getfield current : Ljava/util/concurrent/atomic/AtomicReference;
    //   4: invokevirtual get : ()Ljava/lang/Object;
    //   7: checkcast io/reactivex/internal/operators/observable/ObservableReplay$ReplayObserver
    //   10: astore_2
    //   11: aload_2
    //   12: ifnull -> 24
    //   15: aload_2
    //   16: astore_3
    //   17: aload_2
    //   18: invokevirtual isDisposed : ()Z
    //   21: ifeq -> 56
    //   24: new io/reactivex/internal/operators/observable/ObservableReplay$ReplayObserver
    //   27: dup
    //   28: aload_0
    //   29: getfield bufferFactory : Lio/reactivex/internal/operators/observable/ObservableReplay$BufferSupplier;
    //   32: invokeinterface call : ()Lio/reactivex/internal/operators/observable/ObservableReplay$ReplayBuffer;
    //   37: invokespecial <init> : (Lio/reactivex/internal/operators/observable/ObservableReplay$ReplayBuffer;)V
    //   40: astore_3
    //   41: aload_0
    //   42: getfield current : Ljava/util/concurrent/atomic/AtomicReference;
    //   45: aload_2
    //   46: aload_3
    //   47: invokevirtual compareAndSet : (Ljava/lang/Object;Ljava/lang/Object;)Z
    //   50: ifne -> 56
    //   53: goto -> 0
    //   56: aload_3
    //   57: getfield shouldConnect : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   60: invokevirtual get : ()Z
    //   63: ifne -> 84
    //   66: aload_3
    //   67: getfield shouldConnect : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   70: iconst_0
    //   71: iconst_1
    //   72: invokevirtual compareAndSet : (ZZ)Z
    //   75: ifeq -> 84
    //   78: iconst_1
    //   79: istore #4
    //   81: goto -> 87
    //   84: iconst_0
    //   85: istore #4
    //   87: aload_1
    //   88: aload_3
    //   89: invokeinterface accept : (Ljava/lang/Object;)V
    //   94: iload #4
    //   96: ifeq -> 109
    //   99: aload_0
    //   100: getfield source : Lio/reactivex/ObservableSource;
    //   103: aload_3
    //   104: invokeinterface subscribe : (Lio/reactivex/Observer;)V
    //   109: return
    //   110: astore_1
    //   111: iload #4
    //   113: ifeq -> 126
    //   116: aload_3
    //   117: getfield shouldConnect : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   120: iconst_1
    //   121: iconst_0
    //   122: invokevirtual compareAndSet : (ZZ)Z
    //   125: pop
    //   126: aload_1
    //   127: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
    //   130: aload_1
    //   131: invokestatic wrapOrThrow : (Ljava/lang/Throwable;)Ljava/lang/RuntimeException;
    //   134: astore_1
    //   135: goto -> 140
    //   138: aload_1
    //   139: athrow
    //   140: goto -> 138
    // Exception table:
    //   from	to	target	type
    //   87	94	110	finally
  }
  
  public void resetIf(Disposable paramDisposable) {
    this.current.compareAndSet((ReplayObserver<T>)paramDisposable, null);
  }
  
  public ObservableSource<T> source() {
    return this.source;
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    this.onSubscribe.subscribe(paramObserver);
  }
  
  static abstract class BoundedReplayBuffer<T> extends AtomicReference<Node> implements ReplayBuffer<T> {
    private static final long serialVersionUID = 2346567790059478686L;
    
    int size;
    
    ObservableReplay.Node tail;
    
    BoundedReplayBuffer() {
      ObservableReplay.Node node = new ObservableReplay.Node(null);
      this.tail = node;
      set(node);
    }
    
    final void addLast(ObservableReplay.Node param1Node) {
      this.tail.set(param1Node);
      this.tail = param1Node;
      this.size++;
    }
    
    final void collect(Collection<? super T> param1Collection) {
      ObservableReplay.Node node = getHead();
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
      addLast(new ObservableReplay.Node(enterTransform(NotificationLite.complete())));
      truncateFinal();
    }
    
    Object enterTransform(Object param1Object) {
      return param1Object;
    }
    
    public final void error(Throwable param1Throwable) {
      addLast(new ObservableReplay.Node(enterTransform(NotificationLite.error(param1Throwable))));
      truncateFinal();
    }
    
    ObservableReplay.Node getHead() {
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
      addLast(new ObservableReplay.Node(enterTransform(NotificationLite.next(param1T))));
      truncate();
    }
    
    final void removeFirst() {
      ObservableReplay.Node node = get().get();
      this.size--;
      setFirst(node);
    }
    
    final void removeSome(int param1Int) {
      ObservableReplay.Node node = get();
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
    
    public final void replay(ObservableReplay.InnerDisposable<T> param1InnerDisposable) {
      int j;
      if (param1InnerDisposable.getAndIncrement() != 0)
        return; 
      int i = 1;
      do {
        ObservableReplay.Node node1 = param1InnerDisposable.<ObservableReplay.Node>index();
        ObservableReplay.Node node2 = node1;
        if (node1 == null) {
          node2 = getHead();
          param1InnerDisposable.index = node2;
        } 
        while (true) {
          if (param1InnerDisposable.isDisposed()) {
            param1InnerDisposable.index = null;
            return;
          } 
          node1 = node2.get();
          if (node1 != null) {
            if (NotificationLite.accept(leaveTransform(node1.value), param1InnerDisposable.child)) {
              param1InnerDisposable.index = null;
              return;
            } 
            node2 = node1;
            continue;
          } 
          param1InnerDisposable.index = node2;
          j = param1InnerDisposable.addAndGet(-i);
          i = j;
          break;
        } 
      } while (j != 0);
    }
    
    final void setFirst(ObservableReplay.Node param1Node) {
      set(param1Node);
    }
    
    final void trimHead() {
      ObservableReplay.Node node = get();
      if (node.value != null) {
        ObservableReplay.Node node1 = new ObservableReplay.Node(null);
        node1.lazySet(node.get());
        set(node1);
      } 
    }
    
    abstract void truncate();
    
    void truncateFinal() {
      trimHead();
    }
  }
  
  static interface BufferSupplier<T> {
    ObservableReplay.ReplayBuffer<T> call();
  }
  
  static final class DisposeConsumer<R> implements Consumer<Disposable> {
    private final ObserverResourceWrapper<R> srw;
    
    DisposeConsumer(ObserverResourceWrapper<R> param1ObserverResourceWrapper) {
      this.srw = param1ObserverResourceWrapper;
    }
    
    public void accept(Disposable param1Disposable) {
      this.srw.setResource(param1Disposable);
    }
  }
  
  static final class InnerDisposable<T> extends AtomicInteger implements Disposable {
    private static final long serialVersionUID = 2728361546769921047L;
    
    volatile boolean cancelled;
    
    final Observer<? super T> child;
    
    Object index;
    
    final ObservableReplay.ReplayObserver<T> parent;
    
    InnerDisposable(ObservableReplay.ReplayObserver<T> param1ReplayObserver, Observer<? super T> param1Observer) {
      this.parent = param1ReplayObserver;
      this.child = param1Observer;
    }
    
    public void dispose() {
      if (!this.cancelled) {
        this.cancelled = true;
        this.parent.remove(this);
        this.index = null;
      } 
    }
    
    <U> U index() {
      return (U)this.index;
    }
    
    public boolean isDisposed() {
      return this.cancelled;
    }
  }
  
  static final class MulticastReplay<R, U> extends Observable<R> {
    private final Callable<? extends ConnectableObservable<U>> connectableFactory;
    
    private final Function<? super Observable<U>, ? extends ObservableSource<R>> selector;
    
    MulticastReplay(Callable<? extends ConnectableObservable<U>> param1Callable, Function<? super Observable<U>, ? extends ObservableSource<R>> param1Function) {
      this.connectableFactory = param1Callable;
      this.selector = param1Function;
    }
    
    protected void subscribeActual(Observer<? super R> param1Observer) {
      try {
        ConnectableObservable connectableObservable = (ConnectableObservable)ObjectHelper.requireNonNull(this.connectableFactory.call(), "The connectableFactory returned a null ConnectableObservable");
        ObservableSource observableSource = (ObservableSource)ObjectHelper.requireNonNull(this.selector.apply(connectableObservable), "The selector returned a null ObservableSource");
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        EmptyDisposable.error(exception, param1Observer);
      } 
    }
  }
  
  static final class Node extends AtomicReference<Node> {
    private static final long serialVersionUID = 245354315435971818L;
    
    final Object value;
    
    Node(Object param1Object) {
      this.value = param1Object;
    }
  }
  
  static final class Replay<T> extends ConnectableObservable<T> {
    private final ConnectableObservable<T> co;
    
    private final Observable<T> observable;
    
    Replay(ConnectableObservable<T> param1ConnectableObservable, Observable<T> param1Observable) {
      this.co = param1ConnectableObservable;
      this.observable = param1Observable;
    }
    
    public void connect(Consumer<? super Disposable> param1Consumer) {
      this.co.connect(param1Consumer);
    }
    
    protected void subscribeActual(Observer<? super T> param1Observer) {
      this.observable.subscribe(param1Observer);
    }
  }
  
  static interface ReplayBuffer<T> {
    void complete();
    
    void error(Throwable param1Throwable);
    
    void next(T param1T);
    
    void replay(ObservableReplay.InnerDisposable<T> param1InnerDisposable);
  }
  
  static final class ReplayBufferSupplier<T> implements BufferSupplier<T> {
    private final int bufferSize;
    
    ReplayBufferSupplier(int param1Int) {
      this.bufferSize = param1Int;
    }
    
    public ObservableReplay.ReplayBuffer<T> call() {
      return new ObservableReplay.SizeBoundReplayBuffer<T>(this.bufferSize);
    }
  }
  
  static final class ReplayObserver<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable {
    static final ObservableReplay.InnerDisposable[] EMPTY = new ObservableReplay.InnerDisposable[0];
    
    static final ObservableReplay.InnerDisposable[] TERMINATED = new ObservableReplay.InnerDisposable[0];
    
    private static final long serialVersionUID = -533785617179540163L;
    
    final ObservableReplay.ReplayBuffer<T> buffer;
    
    boolean done;
    
    final AtomicReference<ObservableReplay.InnerDisposable[]> observers;
    
    final AtomicBoolean shouldConnect;
    
    ReplayObserver(ObservableReplay.ReplayBuffer<T> param1ReplayBuffer) {
      this.buffer = param1ReplayBuffer;
      this.observers = (AtomicReference)new AtomicReference<ObservableReplay.InnerDisposable>(EMPTY);
      this.shouldConnect = new AtomicBoolean();
    }
    
    boolean add(ObservableReplay.InnerDisposable<T> param1InnerDisposable) {
      while (true) {
        ObservableReplay.InnerDisposable[] arrayOfInnerDisposable1 = this.observers.get();
        if (arrayOfInnerDisposable1 == TERMINATED)
          return false; 
        int i = arrayOfInnerDisposable1.length;
        ObservableReplay.InnerDisposable[] arrayOfInnerDisposable2 = new ObservableReplay.InnerDisposable[i + 1];
        System.arraycopy(arrayOfInnerDisposable1, 0, arrayOfInnerDisposable2, 0, i);
        arrayOfInnerDisposable2[i] = param1InnerDisposable;
        if (this.observers.compareAndSet(arrayOfInnerDisposable1, arrayOfInnerDisposable2))
          return true; 
      } 
    }
    
    public void dispose() {
      this.observers.set(TERMINATED);
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (this.observers.get() == TERMINATED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onComplete() {
      if (!this.done) {
        this.done = true;
        this.buffer.complete();
        replayFinal();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (!this.done) {
        this.done = true;
        this.buffer.error(param1Throwable);
        replayFinal();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      if (!this.done) {
        this.buffer.next(param1T);
        replay();
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.setOnce(this, param1Disposable))
        replay(); 
    }
    
    void remove(ObservableReplay.InnerDisposable<T> param1InnerDisposable) {
      ObservableReplay.InnerDisposable[] arrayOfInnerDisposable1;
      ObservableReplay.InnerDisposable[] arrayOfInnerDisposable2;
      do {
        byte b2;
        arrayOfInnerDisposable1 = this.observers.get();
        int i = arrayOfInnerDisposable1.length;
        if (i == 0)
          return; 
        byte b1 = -1;
        byte b = 0;
        while (true) {
          b2 = b1;
          if (b < i) {
            if (arrayOfInnerDisposable1[b].equals(param1InnerDisposable)) {
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
          arrayOfInnerDisposable2 = EMPTY;
        } else {
          arrayOfInnerDisposable2 = new ObservableReplay.InnerDisposable[i - 1];
          System.arraycopy(arrayOfInnerDisposable1, 0, arrayOfInnerDisposable2, 0, b2);
          System.arraycopy(arrayOfInnerDisposable1, b2 + 1, arrayOfInnerDisposable2, b2, i - b2 - 1);
        } 
      } while (!this.observers.compareAndSet(arrayOfInnerDisposable1, arrayOfInnerDisposable2));
    }
    
    void replay() {
      for (ObservableReplay.InnerDisposable<T> innerDisposable : (ObservableReplay.InnerDisposable[])this.observers.get())
        this.buffer.replay(innerDisposable); 
    }
    
    void replayFinal() {
      for (ObservableReplay.InnerDisposable<T> innerDisposable : (ObservableReplay.InnerDisposable[])this.observers.getAndSet(TERMINATED))
        this.buffer.replay(innerDisposable); 
    }
  }
  
  static final class ReplaySource<T> implements ObservableSource<T> {
    private final ObservableReplay.BufferSupplier<T> bufferFactory;
    
    private final AtomicReference<ObservableReplay.ReplayObserver<T>> curr;
    
    ReplaySource(AtomicReference<ObservableReplay.ReplayObserver<T>> param1AtomicReference, ObservableReplay.BufferSupplier<T> param1BufferSupplier) {
      this.curr = param1AtomicReference;
      this.bufferFactory = param1BufferSupplier;
    }
    
    public void subscribe(Observer<? super T> param1Observer) {
      ObservableReplay.ReplayObserver<T> replayObserver;
      while (true) {
        ObservableReplay.ReplayObserver<T> replayObserver1 = this.curr.get();
        replayObserver = replayObserver1;
        if (replayObserver1 == null) {
          replayObserver = new ObservableReplay.ReplayObserver(this.bufferFactory.call());
          if (!this.curr.compareAndSet(null, replayObserver))
            continue; 
        } 
        break;
      } 
      ObservableReplay.InnerDisposable<T> innerDisposable = new ObservableReplay.InnerDisposable<T>(replayObserver, param1Observer);
      param1Observer.onSubscribe(innerDisposable);
      replayObserver.add(innerDisposable);
      if (innerDisposable.isDisposed()) {
        replayObserver.remove(innerDisposable);
        return;
      } 
      replayObserver.buffer.replay(innerDisposable);
    }
  }
  
  static final class ScheduledReplaySupplier<T> implements BufferSupplier<T> {
    private final int bufferSize;
    
    private final long maxAge;
    
    private final Scheduler scheduler;
    
    private final TimeUnit unit;
    
    ScheduledReplaySupplier(int param1Int, long param1Long, TimeUnit param1TimeUnit, Scheduler param1Scheduler) {
      this.bufferSize = param1Int;
      this.maxAge = param1Long;
      this.unit = param1TimeUnit;
      this.scheduler = param1Scheduler;
    }
    
    public ObservableReplay.ReplayBuffer<T> call() {
      return new ObservableReplay.SizeAndTimeBoundReplayBuffer<T>(this.bufferSize, this.maxAge, this.unit, this.scheduler);
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
    
    ObservableReplay.Node getHead() {
      long l1 = this.scheduler.now(this.unit);
      long l2 = this.maxAge;
      ObservableReplay.Node node1 = get();
      ObservableReplay.Node node2 = node1.get();
      while (node2 != null) {
        Timed timed = (Timed)node2.value;
        if (!NotificationLite.isComplete(timed.value()) && !NotificationLite.isError(timed.value()) && timed.time() <= l1 - l2) {
          ObservableReplay.Node node = node2.get();
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
      ObservableReplay.Node node1 = get();
      ObservableReplay.Node node2 = node1.get();
      byte b = 0;
      while (node2 != null) {
        if (this.size > this.limit && this.size > 1) {
          b++;
          this.size--;
          ObservableReplay.Node node = node2.get();
          node1 = node2;
          node2 = node;
          continue;
        } 
        if (((Timed)node2.value).time() <= l1 - l2) {
          b++;
          this.size--;
          ObservableReplay.Node node = node2.get();
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
      ObservableReplay.Node node1 = get();
      ObservableReplay.Node node2 = node1.get();
      byte b = 0;
      while (node2 != null && this.size > 1 && ((Timed)node2.value).time() <= l1 - l2) {
        b++;
        this.size--;
        ObservableReplay.Node node = node2.get();
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
  
  static final class UnBoundedFactory implements BufferSupplier<Object> {
    public ObservableReplay.ReplayBuffer<Object> call() {
      return new ObservableReplay.UnboundedReplayBuffer(16);
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
    
    public void replay(ObservableReplay.InnerDisposable<T> param1InnerDisposable) {
      int j;
      if (param1InnerDisposable.getAndIncrement() != 0)
        return; 
      Observer<? super T> observer = param1InnerDisposable.child;
      int i = 1;
      do {
        if (param1InnerDisposable.isDisposed())
          return; 
        int k = this.size;
        Integer integer = param1InnerDisposable.<Integer>index();
        if (integer != null) {
          j = integer.intValue();
        } else {
          j = 0;
        } 
        while (j < k) {
          if (NotificationLite.accept(get(j), observer))
            return; 
          if (param1InnerDisposable.isDisposed())
            return; 
          j++;
        } 
        param1InnerDisposable.index = Integer.valueOf(j);
        j = param1InnerDisposable.addAndGet(-i);
        i = j;
      } while (j != 0);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableReplay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */