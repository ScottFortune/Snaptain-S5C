package io.reactivex.processors;

import io.reactivex.Scheduler;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ReplayProcessor<T> extends FlowableProcessor<T> {
  static final ReplaySubscription[] EMPTY;
  
  private static final Object[] EMPTY_ARRAY = new Object[0];
  
  static final ReplaySubscription[] TERMINATED;
  
  final ReplayBuffer<T> buffer;
  
  boolean done;
  
  final AtomicReference<ReplaySubscription<T>[]> subscribers;
  
  static {
    EMPTY = new ReplaySubscription[0];
    TERMINATED = new ReplaySubscription[0];
  }
  
  ReplayProcessor(ReplayBuffer<T> paramReplayBuffer) {
    this.buffer = paramReplayBuffer;
    this.subscribers = new AtomicReference(EMPTY);
  }
  
  @CheckReturnValue
  public static <T> ReplayProcessor<T> create() {
    return new ReplayProcessor<T>(new UnboundedReplayBuffer<T>(16));
  }
  
  @CheckReturnValue
  public static <T> ReplayProcessor<T> create(int paramInt) {
    return new ReplayProcessor<T>(new UnboundedReplayBuffer<T>(paramInt));
  }
  
  static <T> ReplayProcessor<T> createUnbounded() {
    return new ReplayProcessor<T>(new SizeBoundReplayBuffer<T>(2147483647));
  }
  
  @CheckReturnValue
  public static <T> ReplayProcessor<T> createWithSize(int paramInt) {
    return new ReplayProcessor<T>(new SizeBoundReplayBuffer<T>(paramInt));
  }
  
  @CheckReturnValue
  public static <T> ReplayProcessor<T> createWithTime(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return new ReplayProcessor<T>(new SizeAndTimeBoundReplayBuffer<T>(2147483647, paramLong, paramTimeUnit, paramScheduler));
  }
  
  @CheckReturnValue
  public static <T> ReplayProcessor<T> createWithTimeAndSize(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, int paramInt) {
    return new ReplayProcessor<T>(new SizeAndTimeBoundReplayBuffer<T>(paramInt, paramLong, paramTimeUnit, paramScheduler));
  }
  
  boolean add(ReplaySubscription<T> paramReplaySubscription) {
    while (true) {
      ReplaySubscription[] arrayOfReplaySubscription1 = (ReplaySubscription[])this.subscribers.get();
      if (arrayOfReplaySubscription1 == TERMINATED)
        return false; 
      int i = arrayOfReplaySubscription1.length;
      ReplaySubscription[] arrayOfReplaySubscription2 = new ReplaySubscription[i + 1];
      System.arraycopy(arrayOfReplaySubscription1, 0, arrayOfReplaySubscription2, 0, i);
      arrayOfReplaySubscription2[i] = paramReplaySubscription;
      if (this.subscribers.compareAndSet(arrayOfReplaySubscription1, arrayOfReplaySubscription2))
        return true; 
    } 
  }
  
  public void cleanupBuffer() {
    this.buffer.trimHead();
  }
  
  public Throwable getThrowable() {
    ReplayBuffer<T> replayBuffer = this.buffer;
    return replayBuffer.isDone() ? replayBuffer.getError() : null;
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
    boolean bool;
    ReplayBuffer<T> replayBuffer = this.buffer;
    if (replayBuffer.isDone() && replayBuffer.getError() == null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean hasSubscribers() {
    boolean bool;
    if (((ReplaySubscription[])this.subscribers.get()).length != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean hasThrowable() {
    boolean bool;
    ReplayBuffer<T> replayBuffer = this.buffer;
    if (replayBuffer.isDone() && replayBuffer.getError() != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
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
  
  public void onComplete() {
    if (this.done)
      return; 
    this.done = true;
    ReplayBuffer<T> replayBuffer = this.buffer;
    replayBuffer.complete();
    ReplaySubscription[] arrayOfReplaySubscription = (ReplaySubscription[])this.subscribers.getAndSet(TERMINATED);
    int i = arrayOfReplaySubscription.length;
    for (byte b = 0; b < i; b++)
      replayBuffer.replay(arrayOfReplaySubscription[b]); 
  }
  
  public void onError(Throwable paramThrowable) {
    ObjectHelper.requireNonNull(paramThrowable, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
    if (this.done) {
      RxJavaPlugins.onError(paramThrowable);
      return;
    } 
    this.done = true;
    ReplayBuffer<T> replayBuffer = this.buffer;
    replayBuffer.error(paramThrowable);
    ReplaySubscription[] arrayOfReplaySubscription = (ReplaySubscription[])this.subscribers.getAndSet(TERMINATED);
    int i = arrayOfReplaySubscription.length;
    for (byte b = 0; b < i; b++)
      replayBuffer.replay(arrayOfReplaySubscription[b]); 
  }
  
  public void onNext(T paramT) {
    ObjectHelper.requireNonNull(paramT, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
    if (this.done)
      return; 
    ReplayBuffer<T> replayBuffer = this.buffer;
    replayBuffer.next(paramT);
    ReplaySubscription[] arrayOfReplaySubscription = (ReplaySubscription[])this.subscribers.get();
    int i = arrayOfReplaySubscription.length;
    for (byte b = 0; b < i; b++)
      replayBuffer.replay(arrayOfReplaySubscription[b]); 
  }
  
  public void onSubscribe(Subscription paramSubscription) {
    if (this.done) {
      paramSubscription.cancel();
      return;
    } 
    paramSubscription.request(Long.MAX_VALUE);
  }
  
  void remove(ReplaySubscription<T> paramReplaySubscription) {
    ReplaySubscription[] arrayOfReplaySubscription1;
    ReplaySubscription[] arrayOfReplaySubscription2;
    do {
      byte b2;
      arrayOfReplaySubscription1 = (ReplaySubscription[])this.subscribers.get();
      if (arrayOfReplaySubscription1 == TERMINATED || arrayOfReplaySubscription1 == EMPTY)
        break; 
      int i = arrayOfReplaySubscription1.length;
      byte b1 = -1;
      byte b = 0;
      while (true) {
        b2 = b1;
        if (b < i) {
          if (arrayOfReplaySubscription1[b] == paramReplaySubscription) {
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
        arrayOfReplaySubscription2 = EMPTY;
      } else {
        arrayOfReplaySubscription2 = new ReplaySubscription[i - 1];
        System.arraycopy(arrayOfReplaySubscription1, 0, arrayOfReplaySubscription2, 0, b2);
        System.arraycopy(arrayOfReplaySubscription1, b2 + 1, arrayOfReplaySubscription2, b2, i - b2 - 1);
      } 
    } while (!this.subscribers.compareAndSet(arrayOfReplaySubscription1, arrayOfReplaySubscription2));
  }
  
  int size() {
    return this.buffer.size();
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    ReplaySubscription<T> replaySubscription = new ReplaySubscription<T>(paramSubscriber, this);
    paramSubscriber.onSubscribe(replaySubscription);
    if (add(replaySubscription) && replaySubscription.cancelled) {
      remove(replaySubscription);
      return;
    } 
    this.buffer.replay(replaySubscription);
  }
  
  int subscriberCount() {
    return ((ReplaySubscription[])this.subscribers.get()).length;
  }
  
  static final class Node<T> extends AtomicReference<Node<T>> {
    private static final long serialVersionUID = 6404226426336033100L;
    
    final T value;
    
    Node(T param1T) {
      this.value = param1T;
    }
  }
  
  static interface ReplayBuffer<T> {
    void complete();
    
    void error(Throwable param1Throwable);
    
    Throwable getError();
    
    T getValue();
    
    T[] getValues(T[] param1ArrayOfT);
    
    boolean isDone();
    
    void next(T param1T);
    
    void replay(ReplayProcessor.ReplaySubscription<T> param1ReplaySubscription);
    
    int size();
    
    void trimHead();
  }
  
  static final class ReplaySubscription<T> extends AtomicInteger implements Subscription {
    private static final long serialVersionUID = 466549804534799122L;
    
    volatile boolean cancelled;
    
    final Subscriber<? super T> downstream;
    
    long emitted;
    
    Object index;
    
    final AtomicLong requested;
    
    final ReplayProcessor<T> state;
    
    ReplaySubscription(Subscriber<? super T> param1Subscriber, ReplayProcessor<T> param1ReplayProcessor) {
      this.downstream = param1Subscriber;
      this.state = param1ReplayProcessor;
      this.requested = new AtomicLong();
    }
    
    public void cancel() {
      if (!this.cancelled) {
        this.cancelled = true;
        this.state.remove(this);
      } 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.add(this.requested, param1Long);
        this.state.buffer.replay(this);
      } 
    }
  }
  
  static final class SizeAndTimeBoundReplayBuffer<T> implements ReplayBuffer<T> {
    volatile boolean done;
    
    Throwable error;
    
    volatile ReplayProcessor.TimedNode<T> head;
    
    final long maxAge;
    
    final int maxSize;
    
    final Scheduler scheduler;
    
    int size;
    
    ReplayProcessor.TimedNode<T> tail;
    
    final TimeUnit unit;
    
    SizeAndTimeBoundReplayBuffer(int param1Int, long param1Long, TimeUnit param1TimeUnit, Scheduler param1Scheduler) {
      this.maxSize = ObjectHelper.verifyPositive(param1Int, "maxSize");
      this.maxAge = ObjectHelper.verifyPositive(param1Long, "maxAge");
      this.unit = (TimeUnit)ObjectHelper.requireNonNull(param1TimeUnit, "unit is null");
      this.scheduler = (Scheduler)ObjectHelper.requireNonNull(param1Scheduler, "scheduler is null");
      ReplayProcessor.TimedNode<T> timedNode = new ReplayProcessor.TimedNode(null, 0L);
      this.tail = timedNode;
      this.head = timedNode;
    }
    
    public void complete() {
      trimFinal();
      this.done = true;
    }
    
    public void error(Throwable param1Throwable) {
      trimFinal();
      this.error = param1Throwable;
      this.done = true;
    }
    
    public Throwable getError() {
      return this.error;
    }
    
    ReplayProcessor.TimedNode<T> getHead() {
      ReplayProcessor.TimedNode<T> timedNode1 = this.head;
      long l1 = this.scheduler.now(this.unit);
      long l2 = this.maxAge;
      for (ReplayProcessor.TimedNode<T> timedNode2 = timedNode1.get(); timedNode2 != null && timedNode2.time <= l1 - l2; timedNode2 = timedNode) {
        ReplayProcessor.TimedNode<T> timedNode = timedNode2.get();
        timedNode1 = timedNode2;
      } 
      return timedNode1;
    }
    
    public T getValue() {
      for (ReplayProcessor.TimedNode<T> timedNode = this.head;; timedNode = timedNode1) {
        ReplayProcessor.TimedNode<T> timedNode1 = timedNode.get();
        if (timedNode1 == null) {
          long l1 = this.scheduler.now(this.unit);
          long l2 = this.maxAge;
          return (timedNode.time < l1 - l2) ? null : timedNode.value;
        } 
      } 
    }
    
    public T[] getValues(T[] param1ArrayOfT) {
      T[] arrayOfT;
      ReplayProcessor.TimedNode<T> timedNode = getHead();
      int i = size(timedNode);
      byte b = 0;
      if (i == 0) {
        arrayOfT = param1ArrayOfT;
        if (param1ArrayOfT.length != 0) {
          param1ArrayOfT[0] = null;
          arrayOfT = param1ArrayOfT;
        } 
      } else {
        ReplayProcessor.TimedNode<T> timedNode1 = timedNode;
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
    
    public boolean isDone() {
      return this.done;
    }
    
    public void next(T param1T) {
      ReplayProcessor.TimedNode<T> timedNode2 = new ReplayProcessor.TimedNode<T>(param1T, this.scheduler.now(this.unit));
      ReplayProcessor.TimedNode<T> timedNode1 = this.tail;
      this.tail = timedNode2;
      this.size++;
      timedNode1.set(timedNode2);
      trim();
    }
    
    public void replay(ReplayProcessor.ReplaySubscription<T> param1ReplaySubscription) {
      int j;
      if (param1ReplaySubscription.getAndIncrement() != 0)
        return; 
      Subscriber<? super T> subscriber = param1ReplaySubscription.downstream;
      ReplayProcessor.TimedNode<T> timedNode1 = (ReplayProcessor.TimedNode)param1ReplaySubscription.index;
      ReplayProcessor.TimedNode<T> timedNode2 = timedNode1;
      if (timedNode1 == null)
        timedNode2 = getHead(); 
      long l = param1ReplaySubscription.emitted;
      int i = 1;
      do {
        Throwable throwable;
        long l1 = param1ReplaySubscription.requested.get();
        while (l != l1) {
          boolean bool1;
          if (param1ReplaySubscription.cancelled) {
            param1ReplaySubscription.index = null;
            return;
          } 
          boolean bool = this.done;
          timedNode1 = timedNode2.get();
          if (timedNode1 == null) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          if (bool && bool1) {
            param1ReplaySubscription.index = null;
            param1ReplaySubscription.cancelled = true;
            throwable = this.error;
            if (throwable == null) {
              subscriber.onComplete();
            } else {
              subscriber.onError(throwable);
            } 
            return;
          } 
          if (bool1)
            break; 
          subscriber.onNext(timedNode1.value);
          l++;
          timedNode2 = timedNode1;
        } 
        if (l == l1) {
          if (((ReplayProcessor.ReplaySubscription)throwable).cancelled) {
            ((ReplayProcessor.ReplaySubscription)throwable).index = null;
            return;
          } 
          if (this.done && timedNode2.get() == null) {
            ((ReplayProcessor.ReplaySubscription)throwable).index = null;
            ((ReplayProcessor.ReplaySubscription)throwable).cancelled = true;
            throwable = this.error;
            if (throwable == null) {
              subscriber.onComplete();
            } else {
              subscriber.onError(throwable);
            } 
            return;
          } 
        } 
        ((ReplayProcessor.ReplaySubscription)throwable).index = timedNode2;
        ((ReplayProcessor.ReplaySubscription)throwable).emitted = l;
        j = throwable.addAndGet(-i);
        i = j;
      } while (j != 0);
    }
    
    public int size() {
      return size(getHead());
    }
    
    int size(ReplayProcessor.TimedNode<T> param1TimedNode) {
      byte b;
      for (b = 0; b != Integer.MAX_VALUE; b++) {
        param1TimedNode = param1TimedNode.get();
        if (param1TimedNode == null)
          break; 
      } 
      return b;
    }
    
    void trim() {
      int i = this.size;
      if (i > this.maxSize) {
        this.size = i - 1;
        this.head = this.head.get();
      } 
      long l1 = this.scheduler.now(this.unit);
      long l2 = this.maxAge;
      ReplayProcessor.TimedNode<T> timedNode = this.head;
      while (true) {
        if (this.size <= 1) {
          this.head = timedNode;
        } else {
          ReplayProcessor.TimedNode<T> timedNode1 = timedNode.get();
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
      ReplayProcessor.TimedNode<T> timedNode = this.head;
      while (true) {
        ReplayProcessor.TimedNode<T> timedNode1 = timedNode.get();
        if (timedNode1 == null) {
          if (timedNode.value != null) {
            this.head = new ReplayProcessor.TimedNode<T>(null, 0L);
          } else {
            this.head = timedNode;
          } 
        } else {
          if (timedNode1.time > l1 - l2) {
            if (timedNode.value != null) {
              timedNode1 = new ReplayProcessor.TimedNode<T>(null, 0L);
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
      if (this.head.value != null) {
        ReplayProcessor.TimedNode<T> timedNode = new ReplayProcessor.TimedNode(null, 0L);
        timedNode.lazySet(this.head.get());
        this.head = timedNode;
      } 
    }
  }
  
  static final class SizeBoundReplayBuffer<T> implements ReplayBuffer<T> {
    volatile boolean done;
    
    Throwable error;
    
    volatile ReplayProcessor.Node<T> head;
    
    final int maxSize;
    
    int size;
    
    ReplayProcessor.Node<T> tail;
    
    SizeBoundReplayBuffer(int param1Int) {
      this.maxSize = ObjectHelper.verifyPositive(param1Int, "maxSize");
      ReplayProcessor.Node<T> node = new ReplayProcessor.Node(null);
      this.tail = node;
      this.head = node;
    }
    
    public void complete() {
      trimHead();
      this.done = true;
    }
    
    public void error(Throwable param1Throwable) {
      this.error = param1Throwable;
      trimHead();
      this.done = true;
    }
    
    public Throwable getError() {
      return this.error;
    }
    
    public T getValue() {
      for (ReplayProcessor.Node<T> node = this.head;; node = node1) {
        ReplayProcessor.Node<T> node1 = node.get();
        if (node1 == null)
          return node.value; 
      } 
    }
    
    public T[] getValues(T[] param1ArrayOfT) {
      ReplayProcessor.Node<T> node1 = this.head;
      byte b1 = 0;
      ReplayProcessor.Node<T> node2 = node1;
      for (byte b2 = 0;; b2++) {
        node2 = node2.get();
        if (node2 == null) {
          ReplayProcessor.Node<T> node = node1;
          byte b = b1;
          T[] arrayOfT = param1ArrayOfT;
          if (param1ArrayOfT.length < b2) {
            arrayOfT = (T[])Array.newInstance(param1ArrayOfT.getClass().getComponentType(), b2);
            b = b1;
            node = node1;
          } 
          while (b < b2) {
            node = node.get();
            arrayOfT[b] = node.value;
            b++;
          } 
          if (arrayOfT.length > b2)
            arrayOfT[b2] = null; 
          return arrayOfT;
        } 
      } 
    }
    
    public boolean isDone() {
      return this.done;
    }
    
    public void next(T param1T) {
      ReplayProcessor.Node<T> node2 = new ReplayProcessor.Node<T>(param1T);
      ReplayProcessor.Node<T> node1 = this.tail;
      this.tail = node2;
      this.size++;
      node1.set(node2);
      trim();
    }
    
    public void replay(ReplayProcessor.ReplaySubscription<T> param1ReplaySubscription) {
      int j;
      if (param1ReplaySubscription.getAndIncrement() != 0)
        return; 
      Subscriber<? super T> subscriber = param1ReplaySubscription.downstream;
      ReplayProcessor.Node<T> node1 = (ReplayProcessor.Node)param1ReplaySubscription.index;
      ReplayProcessor.Node<T> node2 = node1;
      if (node1 == null)
        node2 = this.head; 
      long l = param1ReplaySubscription.emitted;
      int i = 1;
      do {
        Throwable throwable;
        long l1 = param1ReplaySubscription.requested.get();
        while (l != l1) {
          boolean bool1;
          if (param1ReplaySubscription.cancelled) {
            param1ReplaySubscription.index = null;
            return;
          } 
          boolean bool = this.done;
          node1 = node2.get();
          if (node1 == null) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          if (bool && bool1) {
            param1ReplaySubscription.index = null;
            param1ReplaySubscription.cancelled = true;
            throwable = this.error;
            if (throwable == null) {
              subscriber.onComplete();
            } else {
              subscriber.onError(throwable);
            } 
            return;
          } 
          if (bool1)
            break; 
          subscriber.onNext(node1.value);
          l++;
          node2 = node1;
        } 
        if (l == l1) {
          if (((ReplayProcessor.ReplaySubscription)throwable).cancelled) {
            ((ReplayProcessor.ReplaySubscription)throwable).index = null;
            return;
          } 
          if (this.done && node2.get() == null) {
            ((ReplayProcessor.ReplaySubscription)throwable).index = null;
            ((ReplayProcessor.ReplaySubscription)throwable).cancelled = true;
            throwable = this.error;
            if (throwable == null) {
              subscriber.onComplete();
            } else {
              subscriber.onError(throwable);
            } 
            return;
          } 
        } 
        ((ReplayProcessor.ReplaySubscription)throwable).index = node2;
        ((ReplayProcessor.ReplaySubscription)throwable).emitted = l;
        j = throwable.addAndGet(-i);
        i = j;
      } while (j != 0);
    }
    
    public int size() {
      ReplayProcessor.Node<T> node = this.head;
      byte b;
      for (b = 0; b != Integer.MAX_VALUE; b++) {
        node = node.get();
        if (node == null)
          break; 
      } 
      return b;
    }
    
    void trim() {
      int i = this.size;
      if (i > this.maxSize) {
        this.size = i - 1;
        this.head = this.head.get();
      } 
    }
    
    public void trimHead() {
      if (this.head.value != null) {
        ReplayProcessor.Node<T> node = new ReplayProcessor.Node(null);
        node.lazySet(this.head.get());
        this.head = node;
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
  
  static final class UnboundedReplayBuffer<T> implements ReplayBuffer<T> {
    final List<T> buffer;
    
    volatile boolean done;
    
    Throwable error;
    
    volatile int size;
    
    UnboundedReplayBuffer(int param1Int) {
      this.buffer = new ArrayList<T>(ObjectHelper.verifyPositive(param1Int, "capacityHint"));
    }
    
    public void complete() {
      this.done = true;
    }
    
    public void error(Throwable param1Throwable) {
      this.error = param1Throwable;
      this.done = true;
    }
    
    public Throwable getError() {
      return this.error;
    }
    
    public T getValue() {
      int i = this.size;
      return (i == 0) ? null : this.buffer.get(i - 1);
    }
    
    public T[] getValues(T[] param1ArrayOfT) {
      int i = this.size;
      byte b1 = 0;
      if (i == 0) {
        if (param1ArrayOfT.length != 0)
          param1ArrayOfT[0] = null; 
        return param1ArrayOfT;
      } 
      List<T> list = this.buffer;
      byte b2 = b1;
      T[] arrayOfT = param1ArrayOfT;
      if (param1ArrayOfT.length < i) {
        arrayOfT = (T[])Array.newInstance(param1ArrayOfT.getClass().getComponentType(), i);
        b2 = b1;
      } 
      while (b2 < i) {
        arrayOfT[b2] = list.get(b2);
        b2++;
      } 
      if (arrayOfT.length > i)
        arrayOfT[i] = null; 
      return arrayOfT;
    }
    
    public boolean isDone() {
      return this.done;
    }
    
    public void next(T param1T) {
      this.buffer.add(param1T);
      this.size++;
    }
    
    public void replay(ReplayProcessor.ReplaySubscription<T> param1ReplaySubscription) {
      int k;
      if (param1ReplaySubscription.getAndIncrement() != 0)
        return; 
      List<T> list = this.buffer;
      Subscriber<? super T> subscriber = param1ReplaySubscription.downstream;
      Integer integer = (Integer)param1ReplaySubscription.index;
      int i = 0;
      if (integer != null) {
        i = integer.intValue();
      } else {
        param1ReplaySubscription.index = Integer.valueOf(0);
      } 
      long l = param1ReplaySubscription.emitted;
      int j = 1;
      do {
        Throwable throwable;
        long l1 = param1ReplaySubscription.requested.get();
        while (l != l1) {
          if (param1ReplaySubscription.cancelled) {
            param1ReplaySubscription.index = null;
            return;
          } 
          boolean bool = this.done;
          int m = this.size;
          if (bool && i == m) {
            param1ReplaySubscription.index = null;
            param1ReplaySubscription.cancelled = true;
            throwable = this.error;
            if (throwable == null) {
              subscriber.onComplete();
            } else {
              subscriber.onError(throwable);
            } 
            return;
          } 
          if (i == m)
            break; 
          subscriber.onNext(list.get(i));
          i++;
          l++;
        } 
        if (l == l1) {
          if (((ReplayProcessor.ReplaySubscription)throwable).cancelled) {
            ((ReplayProcessor.ReplaySubscription)throwable).index = null;
            return;
          } 
          boolean bool = this.done;
          int m = this.size;
          if (bool && i == m) {
            ((ReplayProcessor.ReplaySubscription)throwable).index = null;
            ((ReplayProcessor.ReplaySubscription)throwable).cancelled = true;
            throwable = this.error;
            if (throwable == null) {
              subscriber.onComplete();
            } else {
              subscriber.onError(throwable);
            } 
            return;
          } 
        } 
        ((ReplayProcessor.ReplaySubscription)throwable).index = Integer.valueOf(i);
        ((ReplayProcessor.ReplaySubscription)throwable).emitted = l;
        k = throwable.addAndGet(-j);
        j = k;
      } while (k != 0);
    }
    
    public int size() {
      return this.size;
    }
    
    public void trimHead() {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/processors/ReplayProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */