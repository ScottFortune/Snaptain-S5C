package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.flowables.GroupedFlowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.EmptyComponent;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableGroupBy<T, K, V> extends AbstractFlowableWithUpstream<T, GroupedFlowable<K, V>> {
  final int bufferSize;
  
  final boolean delayError;
  
  final Function<? super T, ? extends K> keySelector;
  
  final Function<? super Consumer<Object>, ? extends Map<K, Object>> mapFactory;
  
  final Function<? super T, ? extends V> valueSelector;
  
  public FlowableGroupBy(Flowable<T> paramFlowable, Function<? super T, ? extends K> paramFunction, Function<? super T, ? extends V> paramFunction1, int paramInt, boolean paramBoolean, Function<? super Consumer<Object>, ? extends Map<K, Object>> paramFunction2) {
    super(paramFlowable);
    this.keySelector = paramFunction;
    this.valueSelector = paramFunction1;
    this.bufferSize = paramInt;
    this.delayError = paramBoolean;
    this.mapFactory = paramFunction2;
  }
  
  protected void subscribeActual(Subscriber<? super GroupedFlowable<K, V>> paramSubscriber) {
    GroupBySubscriber<T, K, V> groupBySubscriber;
    try {
      ConcurrentLinkedQueue<GroupedUnicast<K, V>> concurrentLinkedQueue;
      Map<Object, GroupedUnicast<K, V>> map;
      if (this.mapFactory == null) {
        concurrentLinkedQueue = null;
        map = (Map)new ConcurrentHashMap<Object, Object>();
      } else {
        concurrentLinkedQueue = new ConcurrentLinkedQueue();
        this();
        EvictionAction<Object, Object> evictionAction = new EvictionAction<Object, Object>();
        this(concurrentLinkedQueue);
        map = (Map)this.mapFactory.apply(evictionAction);
      } 
      groupBySubscriber = new GroupBySubscriber<T, K, V>(paramSubscriber, this.keySelector, this.valueSelector, this.bufferSize, this.delayError, map, concurrentLinkedQueue);
      this.source.subscribe(groupBySubscriber);
      return;
    } catch (Exception exception) {
      Exceptions.throwIfFatal(exception);
      groupBySubscriber.onSubscribe((Subscription)EmptyComponent.INSTANCE);
      groupBySubscriber.onError(exception);
      return;
    } 
  }
  
  static final class EvictionAction<K, V> implements Consumer<GroupedUnicast<K, V>> {
    final Queue<FlowableGroupBy.GroupedUnicast<K, V>> evictedGroups;
    
    EvictionAction(Queue<FlowableGroupBy.GroupedUnicast<K, V>> param1Queue) {
      this.evictedGroups = param1Queue;
    }
    
    public void accept(FlowableGroupBy.GroupedUnicast<K, V> param1GroupedUnicast) {
      this.evictedGroups.offer(param1GroupedUnicast);
    }
  }
  
  public static final class GroupBySubscriber<T, K, V> extends BasicIntQueueSubscription<GroupedFlowable<K, V>> implements FlowableSubscriber<T> {
    static final Object NULL_KEY = new Object();
    
    private static final long serialVersionUID = -3688291656102519502L;
    
    final int bufferSize;
    
    final AtomicBoolean cancelled = new AtomicBoolean();
    
    final boolean delayError;
    
    boolean done;
    
    final Subscriber<? super GroupedFlowable<K, V>> downstream;
    
    Throwable error;
    
    final Queue<FlowableGroupBy.GroupedUnicast<K, V>> evictedGroups;
    
    volatile boolean finished;
    
    final AtomicInteger groupCount = new AtomicInteger(1);
    
    final Map<Object, FlowableGroupBy.GroupedUnicast<K, V>> groups;
    
    final Function<? super T, ? extends K> keySelector;
    
    boolean outputFused;
    
    final SpscLinkedArrayQueue<GroupedFlowable<K, V>> queue;
    
    final AtomicLong requested = new AtomicLong();
    
    Subscription upstream;
    
    final Function<? super T, ? extends V> valueSelector;
    
    public GroupBySubscriber(Subscriber<? super GroupedFlowable<K, V>> param1Subscriber, Function<? super T, ? extends K> param1Function, Function<? super T, ? extends V> param1Function1, int param1Int, boolean param1Boolean, Map<Object, FlowableGroupBy.GroupedUnicast<K, V>> param1Map, Queue<FlowableGroupBy.GroupedUnicast<K, V>> param1Queue) {
      this.downstream = param1Subscriber;
      this.keySelector = param1Function;
      this.valueSelector = param1Function1;
      this.bufferSize = param1Int;
      this.delayError = param1Boolean;
      this.groups = param1Map;
      this.evictedGroups = param1Queue;
      this.queue = new SpscLinkedArrayQueue(param1Int);
    }
    
    private void completeEvictions() {
      if (this.evictedGroups != null) {
        byte b = 0;
        while (true) {
          FlowableGroupBy.GroupedUnicast groupedUnicast = this.evictedGroups.poll();
          if (groupedUnicast != null) {
            groupedUnicast.onComplete();
            b++;
            continue;
          } 
          if (b != 0)
            this.groupCount.addAndGet(-b); 
          break;
        } 
      } 
    }
    
    public void cancel() {
      if (this.cancelled.compareAndSet(false, true)) {
        completeEvictions();
        if (this.groupCount.decrementAndGet() == 0)
          this.upstream.cancel(); 
      } 
    }
    
    public void cancel(K param1K) {
      if (param1K == null)
        param1K = (K)NULL_KEY; 
      this.groups.remove(param1K);
      if (this.groupCount.decrementAndGet() == 0) {
        this.upstream.cancel();
        if (!this.outputFused && getAndIncrement() == 0)
          this.queue.clear(); 
      } 
    }
    
    boolean checkTerminated(boolean param1Boolean1, boolean param1Boolean2, Subscriber<?> param1Subscriber, SpscLinkedArrayQueue<?> param1SpscLinkedArrayQueue) {
      Throwable throwable;
      if (this.cancelled.get()) {
        param1SpscLinkedArrayQueue.clear();
        return true;
      } 
      if (this.delayError) {
        if (param1Boolean1 && param1Boolean2) {
          throwable = this.error;
          if (throwable != null) {
            param1Subscriber.onError(throwable);
          } else {
            param1Subscriber.onComplete();
          } 
          return true;
        } 
      } else if (param1Boolean1) {
        Throwable throwable1 = this.error;
        if (throwable1 != null) {
          throwable.clear();
          param1Subscriber.onError(throwable1);
          return true;
        } 
        if (param1Boolean2) {
          param1Subscriber.onComplete();
          return true;
        } 
      } 
      return false;
    }
    
    public void clear() {
      this.queue.clear();
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      if (this.outputFused) {
        drainFused();
      } else {
        drainNormal();
      } 
    }
    
    void drainFused() {
      int j;
      SpscLinkedArrayQueue<GroupedFlowable<K, V>> spscLinkedArrayQueue = this.queue;
      Subscriber<? super GroupedFlowable<K, V>> subscriber = this.downstream;
      int i = 1;
      do {
        if (this.cancelled.get())
          return; 
        boolean bool = this.finished;
        if (bool && !this.delayError) {
          Throwable throwable = this.error;
          if (throwable != null) {
            spscLinkedArrayQueue.clear();
            subscriber.onError(throwable);
            return;
          } 
        } 
        subscriber.onNext(null);
        if (bool) {
          Throwable throwable = this.error;
          if (throwable != null) {
            subscriber.onError(throwable);
          } else {
            subscriber.onComplete();
          } 
          return;
        } 
        j = addAndGet(-i);
        i = j;
      } while (j != 0);
    }
    
    void drainNormal() {
      int j;
      SpscLinkedArrayQueue<GroupedFlowable<K, V>> spscLinkedArrayQueue = this.queue;
      Subscriber<? super GroupedFlowable<K, V>> subscriber = this.downstream;
      int i = 1;
      do {
        long l1 = this.requested.get();
        long l2;
        for (l2 = 0L; l2 != l1; l2++) {
          boolean bool1;
          boolean bool = this.finished;
          GroupedFlowable groupedFlowable = (GroupedFlowable)spscLinkedArrayQueue.poll();
          if (groupedFlowable == null) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          if (checkTerminated(bool, bool1, subscriber, spscLinkedArrayQueue))
            return; 
          if (bool1)
            break; 
          subscriber.onNext(groupedFlowable);
        } 
        if (l2 == l1 && checkTerminated(this.finished, spscLinkedArrayQueue.isEmpty(), subscriber, spscLinkedArrayQueue))
          return; 
        if (l2 != 0L) {
          if (l1 != Long.MAX_VALUE)
            this.requested.addAndGet(-l2); 
          this.upstream.request(l2);
        } 
        j = addAndGet(-i);
        i = j;
      } while (j != 0);
    }
    
    public boolean isEmpty() {
      return this.queue.isEmpty();
    }
    
    public void onComplete() {
      if (!this.done) {
        Iterator<FlowableGroupBy.GroupedUnicast> iterator = this.groups.values().iterator();
        while (iterator.hasNext())
          ((FlowableGroupBy.GroupedUnicast)iterator.next()).onComplete(); 
        this.groups.clear();
        Queue<FlowableGroupBy.GroupedUnicast<K, V>> queue = this.evictedGroups;
        if (queue != null)
          queue.clear(); 
        this.done = true;
        this.finished = true;
        drain();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      Iterator<FlowableGroupBy.GroupedUnicast> iterator = this.groups.values().iterator();
      while (iterator.hasNext())
        ((FlowableGroupBy.GroupedUnicast)iterator.next()).onError(param1Throwable); 
      this.groups.clear();
      Queue<FlowableGroupBy.GroupedUnicast<K, V>> queue = this.evictedGroups;
      if (queue != null)
        queue.clear(); 
      this.error = param1Throwable;
      this.finished = true;
      drain();
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      SpscLinkedArrayQueue<GroupedFlowable<K, V>> spscLinkedArrayQueue = this.queue;
      try {
        Object object2;
        Object object1 = this.keySelector.apply(param1T);
        boolean bool = false;
        if (object1 != null) {
          object2 = object1;
        } else {
          object2 = NULL_KEY;
        } 
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
        this.downstream.onSubscribe((Subscription)this);
        param1Subscription.request(this.bufferSize);
      } 
    }
    
    public GroupedFlowable<K, V> poll() {
      return (GroupedFlowable<K, V>)this.queue.poll();
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.add(this.requested, param1Long);
        drain();
      } 
    }
    
    public int requestFusion(int param1Int) {
      if ((param1Int & 0x2) != 0) {
        this.outputFused = true;
        return 2;
      } 
      return 0;
    }
  }
  
  static final class GroupedUnicast<K, T> extends GroupedFlowable<K, T> {
    final FlowableGroupBy.State<T, K> state;
    
    protected GroupedUnicast(K param1K, FlowableGroupBy.State<T, K> param1State) {
      super(param1K);
      this.state = param1State;
    }
    
    public static <T, K> GroupedUnicast<K, T> createWith(K param1K, int param1Int, FlowableGroupBy.GroupBySubscriber<?, K, T> param1GroupBySubscriber, boolean param1Boolean) {
      return new GroupedUnicast<K, T>(param1K, new FlowableGroupBy.State<T, K>(param1Int, param1GroupBySubscriber, param1K, param1Boolean));
    }
    
    public void onComplete() {
      this.state.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.state.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.state.onNext(param1T);
    }
    
    protected void subscribeActual(Subscriber<? super T> param1Subscriber) {
      this.state.subscribe(param1Subscriber);
    }
  }
  
  static final class State<T, K> extends BasicIntQueueSubscription<T> implements Publisher<T> {
    private static final long serialVersionUID = -3852313036005250360L;
    
    final AtomicReference<Subscriber<? super T>> actual = new AtomicReference<Subscriber<? super T>>();
    
    final AtomicBoolean cancelled = new AtomicBoolean();
    
    final boolean delayError;
    
    volatile boolean done;
    
    Throwable error;
    
    final K key;
    
    final AtomicBoolean once = new AtomicBoolean();
    
    boolean outputFused;
    
    final FlowableGroupBy.GroupBySubscriber<?, K, T> parent;
    
    int produced;
    
    final SpscLinkedArrayQueue<T> queue;
    
    final AtomicLong requested = new AtomicLong();
    
    State(int param1Int, FlowableGroupBy.GroupBySubscriber<?, K, T> param1GroupBySubscriber, K param1K, boolean param1Boolean) {
      this.queue = new SpscLinkedArrayQueue(param1Int);
      this.parent = param1GroupBySubscriber;
      this.key = param1K;
      this.delayError = param1Boolean;
    }
    
    public void cancel() {
      if (this.cancelled.compareAndSet(false, true))
        this.parent.cancel(this.key); 
    }
    
    boolean checkTerminated(boolean param1Boolean1, boolean param1Boolean2, Subscriber<? super T> param1Subscriber, boolean param1Boolean3) {
      if (this.cancelled.get()) {
        this.queue.clear();
        return true;
      } 
      if (param1Boolean1)
        if (param1Boolean3) {
          if (param1Boolean2) {
            Throwable throwable = this.error;
            if (throwable != null) {
              param1Subscriber.onError(throwable);
            } else {
              param1Subscriber.onComplete();
            } 
            return true;
          } 
        } else {
          Throwable throwable = this.error;
          if (throwable != null) {
            this.queue.clear();
            param1Subscriber.onError(throwable);
            return true;
          } 
          if (param1Boolean2) {
            param1Subscriber.onComplete();
            return true;
          } 
        }  
      return false;
    }
    
    public void clear() {
      this.queue.clear();
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      if (this.outputFused) {
        drainFused();
      } else {
        drainNormal();
      } 
    }
    
    void drainFused() {
      SpscLinkedArrayQueue<T> spscLinkedArrayQueue = this.queue;
      Subscriber subscriber = this.actual.get();
      int i = 1;
      while (true) {
        if (subscriber != null) {
          if (this.cancelled.get()) {
            spscLinkedArrayQueue.clear();
            return;
          } 
          boolean bool = this.done;
          if (bool && !this.delayError) {
            Throwable throwable = this.error;
            if (throwable != null) {
              spscLinkedArrayQueue.clear();
              subscriber.onError(throwable);
              return;
            } 
          } 
          subscriber.onNext(null);
          if (bool) {
            Throwable throwable = this.error;
            if (throwable != null) {
              subscriber.onError(throwable);
            } else {
              subscriber.onComplete();
            } 
            return;
          } 
        } 
        int j = addAndGet(-i);
        if (j == 0)
          return; 
        i = j;
        if (subscriber == null) {
          subscriber = this.actual.get();
          i = j;
        } 
      } 
    }
    
    void drainNormal() {
      SpscLinkedArrayQueue<T> spscLinkedArrayQueue = this.queue;
      boolean bool = this.delayError;
      Subscriber<? super T> subscriber = this.actual.get();
      int i = 1;
      while (true) {
        if (subscriber != null) {
          long l1 = this.requested.get();
          long l2;
          for (l2 = 0L; l2 != l1; l2++) {
            boolean bool2;
            boolean bool1 = this.done;
            Object object = spscLinkedArrayQueue.poll();
            if (object == null) {
              bool2 = true;
            } else {
              bool2 = false;
            } 
            if (checkTerminated(bool1, bool2, subscriber, bool))
              return; 
            if (bool2)
              break; 
            subscriber.onNext(object);
          } 
          if (l2 == l1 && checkTerminated(this.done, spscLinkedArrayQueue.isEmpty(), subscriber, bool))
            return; 
          if (l2 != 0L) {
            if (l1 != Long.MAX_VALUE)
              this.requested.addAndGet(-l2); 
            this.parent.upstream.request(l2);
          } 
        } 
        int j = addAndGet(-i);
        if (j == 0)
          return; 
        i = j;
        if (subscriber == null) {
          subscriber = this.actual.get();
          i = j;
        } 
      } 
    }
    
    public boolean isEmpty() {
      if (this.queue.isEmpty()) {
        tryReplenish();
        return true;
      } 
      return false;
    }
    
    public void onComplete() {
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      this.error = param1Throwable;
      this.done = true;
      drain();
    }
    
    public void onNext(T param1T) {
      this.queue.offer(param1T);
      drain();
    }
    
    public T poll() {
      Object object = this.queue.poll();
      if (object != null) {
        this.produced++;
        return (T)object;
      } 
      tryReplenish();
      return null;
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.add(this.requested, param1Long);
        drain();
      } 
    }
    
    public int requestFusion(int param1Int) {
      if ((param1Int & 0x2) != 0) {
        this.outputFused = true;
        return 2;
      } 
      return 0;
    }
    
    public void subscribe(Subscriber<? super T> param1Subscriber) {
      if (this.once.compareAndSet(false, true)) {
        param1Subscriber.onSubscribe((Subscription)this);
        this.actual.lazySet(param1Subscriber);
        drain();
      } else {
        EmptySubscription.error(new IllegalStateException("Only one Subscriber allowed!"), param1Subscriber);
      } 
    }
    
    void tryReplenish() {
      int i = this.produced;
      if (i != 0) {
        this.produced = 0;
        this.parent.upstream.request(i);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableGroupBy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */