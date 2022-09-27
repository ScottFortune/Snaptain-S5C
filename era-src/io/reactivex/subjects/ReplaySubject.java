package io.reactivex.subjects;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.plugins.RxJavaPlugins;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ReplaySubject<T> extends Subject<T> {
  static final ReplayDisposable[] EMPTY = new ReplayDisposable[0];
  
  private static final Object[] EMPTY_ARRAY;
  
  static final ReplayDisposable[] TERMINATED = new ReplayDisposable[0];
  
  final ReplayBuffer<T> buffer;
  
  boolean done;
  
  final AtomicReference<ReplayDisposable<T>[]> observers;
  
  static {
    EMPTY_ARRAY = new Object[0];
  }
  
  ReplaySubject(ReplayBuffer<T> paramReplayBuffer) {
    this.buffer = paramReplayBuffer;
    this.observers = new AtomicReference(EMPTY);
  }
  
  @CheckReturnValue
  public static <T> ReplaySubject<T> create() {
    return new ReplaySubject<T>(new UnboundedReplayBuffer<T>(16));
  }
  
  @CheckReturnValue
  public static <T> ReplaySubject<T> create(int paramInt) {
    return new ReplaySubject<T>(new UnboundedReplayBuffer<T>(paramInt));
  }
  
  static <T> ReplaySubject<T> createUnbounded() {
    return new ReplaySubject<T>(new SizeBoundReplayBuffer<T>(2147483647));
  }
  
  @CheckReturnValue
  public static <T> ReplaySubject<T> createWithSize(int paramInt) {
    return new ReplaySubject<T>(new SizeBoundReplayBuffer<T>(paramInt));
  }
  
  @CheckReturnValue
  public static <T> ReplaySubject<T> createWithTime(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return new ReplaySubject<T>(new SizeAndTimeBoundReplayBuffer<T>(2147483647, paramLong, paramTimeUnit, paramScheduler));
  }
  
  @CheckReturnValue
  public static <T> ReplaySubject<T> createWithTimeAndSize(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, int paramInt) {
    return new ReplaySubject<T>(new SizeAndTimeBoundReplayBuffer<T>(paramInt, paramLong, paramTimeUnit, paramScheduler));
  }
  
  boolean add(ReplayDisposable<T> paramReplayDisposable) {
    while (true) {
      ReplayDisposable[] arrayOfReplayDisposable1 = (ReplayDisposable[])this.observers.get();
      if (arrayOfReplayDisposable1 == TERMINATED)
        return false; 
      int i = arrayOfReplayDisposable1.length;
      ReplayDisposable[] arrayOfReplayDisposable2 = new ReplayDisposable[i + 1];
      System.arraycopy(arrayOfReplayDisposable1, 0, arrayOfReplayDisposable2, 0, i);
      arrayOfReplayDisposable2[i] = paramReplayDisposable;
      if (this.observers.compareAndSet(arrayOfReplayDisposable1, arrayOfReplayDisposable2))
        return true; 
    } 
  }
  
  public void cleanupBuffer() {
    this.buffer.trimHead();
  }
  
  public Throwable getThrowable() {
    Object object = this.buffer.get();
    return NotificationLite.isError(object) ? NotificationLite.getError(object) : null;
  }
  
  public T getValue() {
    return this.buffer.getValue();
  }
  
  public Object[] getValues() {
    T[] arrayOfT1 = getValues((T[])EMPTY_ARRAY);
    T[] arrayOfT2 = arrayOfT1;
    if (arrayOfT1 == EMPTY_ARRAY)
      arrayOfT2 = (T[])new Object[0]; 
    return (Object[])arrayOfT2;
  }
  
  public T[] getValues(T[] paramArrayOfT) {
    return this.buffer.getValues(paramArrayOfT);
  }
  
  public boolean hasComplete() {
    return NotificationLite.isComplete(this.buffer.get());
  }
  
  public boolean hasObservers() {
    boolean bool;
    if (((ReplayDisposable[])this.observers.get()).length != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean hasThrowable() {
    return NotificationLite.isError(this.buffer.get());
  }
  
  public boolean hasValue() {
    boolean bool;
    if (this.buffer.size() != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  int observerCount() {
    return ((ReplayDisposable[])this.observers.get()).length;
  }
  
  public void onComplete() {
    if (this.done)
      return; 
    this.done = true;
    Object object = NotificationLite.complete();
    ReplayBuffer<T> replayBuffer = this.buffer;
    replayBuffer.addFinal(object);
    object = terminate(object);
    int i = object.length;
    for (byte b = 0; b < i; b++)
      replayBuffer.replay((ReplayDisposable<T>)object[b]); 
  }
  
  public void onError(Throwable paramThrowable) {
    ObjectHelper.requireNonNull(paramThrowable, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
    if (this.done) {
      RxJavaPlugins.onError(paramThrowable);
      return;
    } 
    this.done = true;
    Object object = NotificationLite.error(paramThrowable);
    ReplayBuffer<T> replayBuffer = this.buffer;
    replayBuffer.addFinal(object);
    object = terminate(object);
    int i = object.length;
    for (byte b = 0; b < i; b++)
      replayBuffer.replay((ReplayDisposable<T>)object[b]); 
  }
  
  public void onNext(T paramT) {
    ObjectHelper.requireNonNull(paramT, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
    if (this.done)
      return; 
    ReplayBuffer<T> replayBuffer = this.buffer;
    replayBuffer.add(paramT);
    ReplayDisposable[] arrayOfReplayDisposable = (ReplayDisposable[])this.observers.get();
    int i = arrayOfReplayDisposable.length;
    for (byte b = 0; b < i; b++)
      replayBuffer.replay(arrayOfReplayDisposable[b]); 
  }
  
  public void onSubscribe(Disposable paramDisposable) {
    if (this.done)
      paramDisposable.dispose(); 
  }
  
  void remove(ReplayDisposable<T> paramReplayDisposable) {
    ReplayDisposable[] arrayOfReplayDisposable1;
    ReplayDisposable[] arrayOfReplayDisposable2;
    do {
      byte b2;
      arrayOfReplayDisposable1 = (ReplayDisposable[])this.observers.get();
      if (arrayOfReplayDisposable1 == TERMINATED || arrayOfReplayDisposable1 == EMPTY)
        break; 
      int i = arrayOfReplayDisposable1.length;
      byte b1 = -1;
      byte b = 0;
      while (true) {
        b2 = b1;
        if (b < i) {
          if (arrayOfReplayDisposable1[b] == paramReplayDisposable) {
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
        arrayOfReplayDisposable2 = EMPTY;
      } else {
        arrayOfReplayDisposable2 = new ReplayDisposable[i - 1];
        System.arraycopy(arrayOfReplayDisposable1, 0, arrayOfReplayDisposable2, 0, b2);
        System.arraycopy(arrayOfReplayDisposable1, b2 + 1, arrayOfReplayDisposable2, b2, i - b2 - 1);
      } 
    } while (!this.observers.compareAndSet(arrayOfReplayDisposable1, arrayOfReplayDisposable2));
  }
  
  int size() {
    return this.buffer.size();
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    ReplayDisposable<T> replayDisposable = new ReplayDisposable<T>(paramObserver, this);
    paramObserver.onSubscribe(replayDisposable);
    if (!replayDisposable.cancelled) {
      if (add(replayDisposable) && replayDisposable.cancelled) {
        remove(replayDisposable);
        return;
      } 
      this.buffer.replay(replayDisposable);
    } 
  }
  
  ReplayDisposable<T>[] terminate(Object paramObject) {
    return (ReplayDisposable<T>[])(this.buffer.compareAndSet(null, paramObject) ? this.observers.getAndSet(TERMINATED) : TERMINATED);
  }
  
  static final class Node<T> extends AtomicReference<Node<T>> {
    private static final long serialVersionUID = 6404226426336033100L;
    
    final T value;
    
    Node(T param1T) {
      this.value = param1T;
    }
  }
  
  static interface ReplayBuffer<T> {
    void add(T param1T);
    
    void addFinal(Object param1Object);
    
    boolean compareAndSet(Object param1Object1, Object param1Object2);
    
    Object get();
    
    T getValue();
    
    T[] getValues(T[] param1ArrayOfT);
    
    void replay(ReplaySubject.ReplayDisposable<T> param1ReplayDisposable);
    
    int size();
    
    void trimHead();
  }
  
  static final class ReplayDisposable<T> extends AtomicInteger implements Disposable {
    private static final long serialVersionUID = 466549804534799122L;
    
    volatile boolean cancelled;
    
    final Observer<? super T> downstream;
    
    Object index;
    
    final ReplaySubject<T> state;
    
    ReplayDisposable(Observer<? super T> param1Observer, ReplaySubject<T> param1ReplaySubject) {
      this.downstream = param1Observer;
      this.state = param1ReplaySubject;
    }
    
    public void dispose() {
      if (!this.cancelled) {
        this.cancelled = true;
        this.state.remove(this);
      } 
    }
    
    public boolean isDisposed() {
      return this.cancelled;
    }
  }
  
  static final class SizeAndTimeBoundReplayBuffer<T> extends AtomicReference<Object> implements ReplayBuffer<T> {
    private static final long serialVersionUID = -8056260896137901749L;
    
    volatile boolean done;
    
    volatile ReplaySubject.TimedNode<Object> head;
    
    final long maxAge;
    
    final int maxSize;
    
    final Scheduler scheduler;
    
    int size;
    
    ReplaySubject.TimedNode<Object> tail;
    
    final TimeUnit unit;
    
    SizeAndTimeBoundReplayBuffer(int param1Int, long param1Long, TimeUnit param1TimeUnit, Scheduler param1Scheduler) {
      this.maxSize = ObjectHelper.verifyPositive(param1Int, "maxSize");
      this.maxAge = ObjectHelper.verifyPositive(param1Long, "maxAge");
      this.unit = (TimeUnit)ObjectHelper.requireNonNull(param1TimeUnit, "unit is null");
      this.scheduler = (Scheduler)ObjectHelper.requireNonNull(param1Scheduler, "scheduler is null");
      ReplaySubject.TimedNode<Object> timedNode = new ReplaySubject.TimedNode(null, 0L);
      this.tail = timedNode;
      this.head = timedNode;
    }
    
    public void add(T param1T) {
      ReplaySubject.TimedNode<T> timedNode = new ReplaySubject.TimedNode<T>(param1T, this.scheduler.now(this.unit));
      ReplaySubject.TimedNode<Object> timedNode1 = this.tail;
      this.tail = (ReplaySubject.TimedNode)timedNode;
      this.size++;
      timedNode1.set(timedNode);
      trim();
    }
    
    public void addFinal(Object<Object> param1Object) {
      ReplaySubject.TimedNode<Object> timedNode = new ReplaySubject.TimedNode(param1Object, Long.MAX_VALUE);
      param1Object = (Object<Object>)this.tail;
      this.tail = timedNode;
      this.size++;
      param1Object.lazySet(timedNode);
      trimFinal();
      this.done = true;
    }
    
    ReplaySubject.TimedNode<Object> getHead() {
      ReplaySubject.TimedNode<Object> timedNode1 = this.head;
      long l1 = this.scheduler.now(this.unit);
      long l2 = this.maxAge;
      for (ReplaySubject.TimedNode<Object> timedNode2 = timedNode1.get(); timedNode2 != null && timedNode2.time <= l1 - l2; timedNode2 = timedNode) {
        ReplaySubject.TimedNode<Object> timedNode = timedNode2.get();
        timedNode1 = timedNode2;
      } 
      return timedNode1;
    }
    
    public T getValue() {
      ReplaySubject.TimedNode<Object> timedNode = this.head;
      Object object = null;
      while (true) {
        T t1;
        ReplaySubject.TimedNode timedNode2 = timedNode.get();
        if (timedNode2 == null) {
          long l1 = this.scheduler.now(this.unit);
          long l2 = this.maxAge;
          if (timedNode.time < l1 - l2)
            return null; 
          t1 = timedNode.value;
          return (t1 == null) ? null : ((NotificationLite.isComplete(t1) || NotificationLite.isError(t1)) ? ((ReplaySubject.TimedNode)object).value : t1);
        } 
        T t2 = t1;
        ReplaySubject.TimedNode timedNode1 = timedNode2;
      } 
    }
    
    public T[] getValues(T[] param1ArrayOfT) {
      T[] arrayOfT;
      ReplaySubject.TimedNode<Object> timedNode = getHead();
      int i = size(timedNode);
      byte b = 0;
      if (i == 0) {
        arrayOfT = param1ArrayOfT;
        if (param1ArrayOfT.length != 0) {
          param1ArrayOfT[0] = null;
          arrayOfT = param1ArrayOfT;
        } 
      } else {
        ReplaySubject.TimedNode<Object> timedNode1 = timedNode;
        int j = b;
        T[] arrayOfT1 = param1ArrayOfT;
        if (param1ArrayOfT.length < i) {
          arrayOfT1 = (T[])Array.newInstance(param1ArrayOfT.getClass().getComponentType(), i);
          j = b;
          timedNode1 = timedNode;
        } 
        while (j != i) {
          timedNode1 = timedNode1.get();
          arrayOfT1[j] = timedNode1.value;
          j++;
        } 
        arrayOfT = arrayOfT1;
        if (arrayOfT1.length > i) {
          arrayOfT1[i] = null;
          arrayOfT = arrayOfT1;
        } 
      } 
      return arrayOfT;
    }
    
    public void replay(ReplaySubject.ReplayDisposable<T> param1ReplayDisposable) {
      if (param1ReplayDisposable.getAndIncrement() != 0)
        return; 
      Observer<? super T> observer = param1ReplayDisposable.downstream;
      ReplaySubject.TimedNode<Object> timedNode1 = (ReplaySubject.TimedNode)param1ReplayDisposable.index;
      ReplaySubject.TimedNode<Object> timedNode2 = timedNode1;
      if (timedNode1 == null)
        timedNode2 = getHead(); 
      int i = 1;
      label34: while (true) {
        if (param1ReplayDisposable.cancelled) {
          param1ReplayDisposable.index = null;
          return;
        } 
        while (true) {
          if (param1ReplayDisposable.cancelled) {
            param1ReplayDisposable.index = null;
            return;
          } 
          timedNode1 = timedNode2.get();
          if (timedNode1 == null) {
            if (timedNode2.get() != null)
              continue label34; 
            param1ReplayDisposable.index = timedNode2;
            int j = param1ReplayDisposable.addAndGet(-i);
            i = j;
            if (j == 0)
              return; 
            continue label34;
          } 
          T t = timedNode1.value;
          if (this.done && timedNode1.get() == null) {
            if (NotificationLite.isComplete(t)) {
              observer.onComplete();
            } else {
              observer.onError(NotificationLite.getError(t));
            } 
            param1ReplayDisposable.index = null;
            param1ReplayDisposable.cancelled = true;
            return;
          } 
          observer.onNext(t);
          ReplaySubject.TimedNode<Object> timedNode = timedNode1;
        } 
        break;
      } 
    }
    
    public int size() {
      return size(getHead());
    }
    
    int size(ReplaySubject.TimedNode<Object> param1TimedNode) {
      // Byte code:
      //   0: iconst_0
      //   1: istore_2
      //   2: iload_2
      //   3: istore_3
      //   4: iload_2
      //   5: ldc 2147483647
      //   7: if_icmpeq -> 61
      //   10: aload_1
      //   11: invokevirtual get : ()Ljava/lang/Object;
      //   14: checkcast io/reactivex/subjects/ReplaySubject$TimedNode
      //   17: astore #4
      //   19: aload #4
      //   21: ifnonnull -> 52
      //   24: aload_1
      //   25: getfield value : Ljava/lang/Object;
      //   28: astore_1
      //   29: aload_1
      //   30: invokestatic isComplete : (Ljava/lang/Object;)Z
      //   33: ifne -> 45
      //   36: iload_2
      //   37: istore_3
      //   38: aload_1
      //   39: invokestatic isError : (Ljava/lang/Object;)Z
      //   42: ifeq -> 61
      //   45: iload_2
      //   46: iconst_1
      //   47: isub
      //   48: istore_3
      //   49: goto -> 61
      //   52: iinc #2, 1
      //   55: aload #4
      //   57: astore_1
      //   58: goto -> 2
      //   61: iload_3
      //   62: ireturn
    }
    
    void trim() {
      int i = this.size;
      if (i > this.maxSize) {
        this.size = i - 1;
        this.head = this.head.get();
      } 
      long l1 = this.scheduler.now(this.unit);
      long l2 = this.maxAge;
      ReplaySubject.TimedNode<Object> timedNode = this.head;
      while (true) {
        if (this.size <= 1) {
          this.head = timedNode;
        } else {
          ReplaySubject.TimedNode<Object> timedNode1 = timedNode.get();
          if (timedNode1 == null) {
            this.head = timedNode;
          } else {
            if (timedNode1.time > l1 - l2) {
              this.head = timedNode;
              return;
            } 
            this.size--;
            timedNode = timedNode1;
            continue;
          } 
        } 
        return;
      } 
    }
    
    void trimFinal() {
      long l1 = this.scheduler.now(this.unit);
      long l2 = this.maxAge;
      ReplaySubject.TimedNode<Object> timedNode = this.head;
      while (true) {
        ReplaySubject.TimedNode<Object> timedNode1 = timedNode.get();
        if (timedNode1.get() == null) {
          if (timedNode.value != null) {
            timedNode1 = new ReplaySubject.TimedNode(null, 0L);
            timedNode1.lazySet(timedNode.get());
            this.head = timedNode1;
          } else {
            this.head = timedNode;
          } 
        } else {
          if (timedNode1.time > l1 - l2) {
            if (timedNode.value != null) {
              timedNode1 = new ReplaySubject.TimedNode(null, 0L);
              timedNode1.lazySet(timedNode.get());
              this.head = timedNode1;
            } else {
              this.head = timedNode;
            } 
            return;
          } 
          timedNode = timedNode1;
          continue;
        } 
        return;
      } 
    }
    
    public void trimHead() {
      ReplaySubject.TimedNode<Object> timedNode = this.head;
      if (timedNode.value != null) {
        ReplaySubject.TimedNode<Object> timedNode1 = new ReplaySubject.TimedNode(null, 0L);
        timedNode1.lazySet(timedNode.get());
        this.head = timedNode1;
      } 
    }
  }
  
  static final class SizeBoundReplayBuffer<T> extends AtomicReference<Object> implements ReplayBuffer<T> {
    private static final long serialVersionUID = 1107649250281456395L;
    
    volatile boolean done;
    
    volatile ReplaySubject.Node<Object> head;
    
    final int maxSize;
    
    int size;
    
    ReplaySubject.Node<Object> tail;
    
    SizeBoundReplayBuffer(int param1Int) {
      this.maxSize = ObjectHelper.verifyPositive(param1Int, "maxSize");
      ReplaySubject.Node<Object> node = new ReplaySubject.Node(null);
      this.tail = node;
      this.head = node;
    }
    
    public void add(T param1T) {
      ReplaySubject.Node<T> node1 = new ReplaySubject.Node<T>(param1T);
      ReplaySubject.Node<Object> node = this.tail;
      this.tail = (ReplaySubject.Node)node1;
      this.size++;
      node.set(node1);
      trim();
    }
    
    public void addFinal(Object<Object> param1Object) {
      ReplaySubject.Node<Object> node = new ReplaySubject.Node(param1Object);
      param1Object = (Object<Object>)this.tail;
      this.tail = node;
      this.size++;
      param1Object.lazySet(node);
      trimHead();
      this.done = true;
    }
    
    public T getValue() {
      ReplaySubject.Node<Object> node = this.head;
      Object object = null;
      while (true) {
        T t1;
        ReplaySubject.Node node2 = node.get();
        if (node2 == null) {
          t1 = node.value;
          return (t1 == null) ? null : ((NotificationLite.isComplete(t1) || NotificationLite.isError(t1)) ? ((ReplaySubject.Node)object).value : t1);
        } 
        T t2 = t1;
        ReplaySubject.Node node1 = node2;
      } 
    }
    
    public T[] getValues(T[] param1ArrayOfT) {
      T[] arrayOfT;
      ReplaySubject.Node<Object> node = this.head;
      int i = size();
      byte b = 0;
      if (i == 0) {
        arrayOfT = param1ArrayOfT;
        if (param1ArrayOfT.length != 0) {
          param1ArrayOfT[0] = null;
          arrayOfT = param1ArrayOfT;
        } 
      } else {
        ReplaySubject.Node<Object> node1 = node;
        int j = b;
        T[] arrayOfT1 = param1ArrayOfT;
        if (param1ArrayOfT.length < i) {
          arrayOfT1 = (T[])Array.newInstance(param1ArrayOfT.getClass().getComponentType(), i);
          j = b;
          node1 = node;
        } 
        while (j != i) {
          node1 = node1.get();
          arrayOfT1[j] = node1.value;
          j++;
        } 
        arrayOfT = arrayOfT1;
        if (arrayOfT1.length > i) {
          arrayOfT1[i] = null;
          arrayOfT = arrayOfT1;
        } 
      } 
      return arrayOfT;
    }
    
    public void replay(ReplaySubject.ReplayDisposable<T> param1ReplayDisposable) {
      if (param1ReplayDisposable.getAndIncrement() != 0)
        return; 
      Observer<? super T> observer = param1ReplayDisposable.downstream;
      ReplaySubject.Node<Object> node1 = (ReplaySubject.Node)param1ReplayDisposable.index;
      ReplaySubject.Node<Object> node2 = node1;
      if (node1 == null)
        node2 = this.head; 
      int i = 1;
      while (true) {
        if (param1ReplayDisposable.cancelled) {
          param1ReplayDisposable.index = null;
          return;
        } 
        node1 = node2.get();
        if (node1 == null) {
          if (node2.get() != null)
            continue; 
          param1ReplayDisposable.index = node2;
          int j = param1ReplayDisposable.addAndGet(-i);
          i = j;
          if (j == 0)
            return; 
          continue;
        } 
        T t = node1.value;
        if (this.done && node1.get() == null) {
          if (NotificationLite.isComplete(t)) {
            observer.onComplete();
          } else {
            observer.onError(NotificationLite.getError(t));
          } 
          param1ReplayDisposable.index = null;
          param1ReplayDisposable.cancelled = true;
          return;
        } 
        observer.onNext(t);
        ReplaySubject.Node<Object> node = node1;
      } 
    }
    
    public int size() {
      // Byte code:
      //   0: aload_0
      //   1: getfield head : Lio/reactivex/subjects/ReplaySubject$Node;
      //   4: astore_1
      //   5: iconst_0
      //   6: istore_2
      //   7: iload_2
      //   8: istore_3
      //   9: iload_2
      //   10: ldc 2147483647
      //   12: if_icmpeq -> 66
      //   15: aload_1
      //   16: invokevirtual get : ()Ljava/lang/Object;
      //   19: checkcast io/reactivex/subjects/ReplaySubject$Node
      //   22: astore #4
      //   24: aload #4
      //   26: ifnonnull -> 57
      //   29: aload_1
      //   30: getfield value : Ljava/lang/Object;
      //   33: astore_1
      //   34: aload_1
      //   35: invokestatic isComplete : (Ljava/lang/Object;)Z
      //   38: ifne -> 50
      //   41: iload_2
      //   42: istore_3
      //   43: aload_1
      //   44: invokestatic isError : (Ljava/lang/Object;)Z
      //   47: ifeq -> 66
      //   50: iload_2
      //   51: iconst_1
      //   52: isub
      //   53: istore_3
      //   54: goto -> 66
      //   57: iinc #2, 1
      //   60: aload #4
      //   62: astore_1
      //   63: goto -> 7
      //   66: iload_3
      //   67: ireturn
    }
    
    void trim() {
      int i = this.size;
      if (i > this.maxSize) {
        this.size = i - 1;
        this.head = this.head.get();
      } 
    }
    
    public void trimHead() {
      ReplaySubject.Node<Object> node = this.head;
      if (node.value != null) {
        ReplaySubject.Node<Object> node1 = new ReplaySubject.Node(null);
        node1.lazySet(node.get());
        this.head = node1;
      } 
    }
  }
  
  static final class TimedNode<T> extends AtomicReference<TimedNode<T>> {
    private static final long serialVersionUID = 6404226426336033100L;
    
    final long time;
    
    final T value;
    
    TimedNode(T param1T, long param1Long) {
      this.value = param1T;
      this.time = param1Long;
    }
  }
  
  static final class UnboundedReplayBuffer<T> extends AtomicReference<Object> implements ReplayBuffer<T> {
    private static final long serialVersionUID = -733876083048047795L;
    
    final List<Object> buffer;
    
    volatile boolean done;
    
    volatile int size;
    
    UnboundedReplayBuffer(int param1Int) {
      this.buffer = new ArrayList(ObjectHelper.verifyPositive(param1Int, "capacityHint"));
    }
    
    public void add(T param1T) {
      this.buffer.add(param1T);
      this.size++;
    }
    
    public void addFinal(Object param1Object) {
      this.buffer.add(param1Object);
      trimHead();
      this.size++;
      this.done = true;
    }
    
    public T getValue() {
      int i = this.size;
      if (i != 0) {
        List<Object> list = this.buffer;
        Object object = list.get(i - 1);
        return (T)((NotificationLite.isComplete(object) || NotificationLite.isError(object)) ? ((i == 1) ? null : list.get(i - 2)) : object);
      } 
      return null;
    }
    
    public T[] getValues(T[] param1ArrayOfT) {
      // Byte code:
      //   0: aload_0
      //   1: getfield size : I
      //   4: istore_2
      //   5: iconst_0
      //   6: istore_3
      //   7: iload_2
      //   8: ifne -> 22
      //   11: aload_1
      //   12: arraylength
      //   13: ifeq -> 20
      //   16: aload_1
      //   17: iconst_0
      //   18: aconst_null
      //   19: aastore
      //   20: aload_1
      //   21: areturn
      //   22: aload_0
      //   23: getfield buffer : Ljava/util/List;
      //   26: astore #4
      //   28: aload #4
      //   30: iload_2
      //   31: iconst_1
      //   32: isub
      //   33: invokeinterface get : (I)Ljava/lang/Object;
      //   38: astore #5
      //   40: aload #5
      //   42: invokestatic isComplete : (Ljava/lang/Object;)Z
      //   45: ifne -> 59
      //   48: iload_2
      //   49: istore #6
      //   51: aload #5
      //   53: invokestatic isError : (Ljava/lang/Object;)Z
      //   56: ifeq -> 80
      //   59: iinc #2, -1
      //   62: iload_2
      //   63: istore #6
      //   65: iload_2
      //   66: ifne -> 80
      //   69: aload_1
      //   70: arraylength
      //   71: ifeq -> 78
      //   74: aload_1
      //   75: iconst_0
      //   76: aconst_null
      //   77: aastore
      //   78: aload_1
      //   79: areturn
      //   80: iload_3
      //   81: istore_2
      //   82: aload_1
      //   83: astore #5
      //   85: aload_1
      //   86: arraylength
      //   87: iload #6
      //   89: if_icmpge -> 111
      //   92: aload_1
      //   93: invokevirtual getClass : ()Ljava/lang/Class;
      //   96: invokevirtual getComponentType : ()Ljava/lang/Class;
      //   99: iload #6
      //   101: invokestatic newInstance : (Ljava/lang/Class;I)Ljava/lang/Object;
      //   104: checkcast [Ljava/lang/Object;
      //   107: astore #5
      //   109: iload_3
      //   110: istore_2
      //   111: iload_2
      //   112: iload #6
      //   114: if_icmpge -> 135
      //   117: aload #5
      //   119: iload_2
      //   120: aload #4
      //   122: iload_2
      //   123: invokeinterface get : (I)Ljava/lang/Object;
      //   128: aastore
      //   129: iinc #2, 1
      //   132: goto -> 111
      //   135: aload #5
      //   137: arraylength
      //   138: iload #6
      //   140: if_icmple -> 149
      //   143: aload #5
      //   145: iload #6
      //   147: aconst_null
      //   148: aastore
      //   149: aload #5
      //   151: areturn
    }
    
    public void replay(ReplaySubject.ReplayDisposable<T> param1ReplayDisposable) {
      if (param1ReplayDisposable.getAndIncrement() != 0)
        return; 
      List<Object> list = this.buffer;
      Observer<? super T> observer = param1ReplayDisposable.downstream;
      Integer integer = (Integer)param1ReplayDisposable.index;
      int i = 0;
      if (integer != null) {
        i = integer.intValue();
      } else {
        param1ReplayDisposable.index = Integer.valueOf(0);
      } 
      int j = 1;
      int k = i;
      i = j;
      while (true) {
        if (param1ReplayDisposable.cancelled) {
          param1ReplayDisposable.index = null;
          return;
        } 
        int m;
        for (m = this.size; m != k; m = j) {
          if (param1ReplayDisposable.cancelled) {
            param1ReplayDisposable.index = null;
            return;
          } 
          integer = (Integer)list.get(k);
          j = m;
          if (this.done) {
            int n = k + 1;
            j = m;
            if (n == m) {
              m = this.size;
              j = m;
              if (n == m) {
                if (NotificationLite.isComplete(integer)) {
                  observer.onComplete();
                } else {
                  observer.onError(NotificationLite.getError(integer));
                } 
                param1ReplayDisposable.index = null;
                param1ReplayDisposable.cancelled = true;
                return;
              } 
            } 
          } 
          observer.onNext(integer);
          k++;
        } 
        if (k != this.size)
          continue; 
        param1ReplayDisposable.index = Integer.valueOf(k);
        j = param1ReplayDisposable.addAndGet(-i);
        i = j;
        if (j == 0)
          break; 
      } 
    }
    
    public int size() {
      int i = this.size;
      if (i != 0) {
        List<Object> list = this.buffer;
        int j = i - 1;
        list = (List<Object>)list.get(j);
        return (NotificationLite.isComplete(list) || NotificationLite.isError(list)) ? j : i;
      } 
      return 0;
    }
    
    public void trimHead() {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/subjects/ReplaySubject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */