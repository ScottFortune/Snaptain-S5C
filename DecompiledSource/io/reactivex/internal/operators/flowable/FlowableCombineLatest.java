package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableCombineLatest<T, R> extends Flowable<R> {
  final Publisher<? extends T>[] array = null;
  
  final int bufferSize;
  
  final Function<? super Object[], ? extends R> combiner;
  
  final boolean delayErrors;
  
  final Iterable<? extends Publisher<? extends T>> iterable;
  
  public FlowableCombineLatest(Iterable<? extends Publisher<? extends T>> paramIterable, Function<? super Object[], ? extends R> paramFunction, int paramInt, boolean paramBoolean) {
    this.iterable = paramIterable;
    this.combiner = paramFunction;
    this.bufferSize = paramInt;
    this.delayErrors = paramBoolean;
  }
  
  public FlowableCombineLatest(Publisher<? extends T>[] paramArrayOfPublisher, Function<? super Object[], ? extends R> paramFunction, int paramInt, boolean paramBoolean) {
    this.iterable = null;
    this.combiner = paramFunction;
    this.bufferSize = paramInt;
    this.delayErrors = paramBoolean;
  }
  
  public void subscribeActual(Subscriber<? super R> paramSubscriber) {
    Publisher[] arrayOfPublisher;
    int i;
    Publisher<? extends T>[] arrayOfPublisher1 = this.array;
    if (arrayOfPublisher1 == null) {
      arrayOfPublisher = new Publisher[8];
      try {
      
      } finally {
        arrayOfPublisher = null;
        Exceptions.throwIfFatal((Throwable)arrayOfPublisher);
        EmptySubscription.error((Throwable)arrayOfPublisher, paramSubscriber);
      } 
    } else {
      i = arrayOfPublisher.length;
    } 
    if (i == 0) {
      EmptySubscription.complete(paramSubscriber);
      return;
    } 
    if (i == 1) {
      arrayOfPublisher[0].subscribe((Subscriber)new FlowableMap.MapSubscriber<Object, R>(paramSubscriber, new SingletonArrayFunc()));
      return;
    } 
    CombineLatestCoordinator<Object, R> combineLatestCoordinator = new CombineLatestCoordinator<Object, R>(paramSubscriber, this.combiner, i, this.bufferSize, this.delayErrors);
    paramSubscriber.onSubscribe((Subscription)combineLatestCoordinator);
    combineLatestCoordinator.subscribe((Publisher<?>[])arrayOfPublisher, i);
  }
  
  static final class CombineLatestCoordinator<T, R> extends BasicIntQueueSubscription<R> {
    private static final long serialVersionUID = -5082275438355852221L;
    
    volatile boolean cancelled;
    
    final Function<? super Object[], ? extends R> combiner;
    
    int completedSources;
    
    final boolean delayErrors;
    
    volatile boolean done;
    
    final Subscriber<? super R> downstream;
    
    final AtomicReference<Throwable> error;
    
    final Object[] latest;
    
    int nonEmptySources;
    
    boolean outputFused;
    
    final SpscLinkedArrayQueue<Object> queue;
    
    final AtomicLong requested;
    
    final FlowableCombineLatest.CombineLatestInnerSubscriber<T>[] subscribers;
    
    CombineLatestCoordinator(Subscriber<? super R> param1Subscriber, Function<? super Object[], ? extends R> param1Function, int param1Int1, int param1Int2, boolean param1Boolean) {
      this.downstream = param1Subscriber;
      this.combiner = param1Function;
      FlowableCombineLatest.CombineLatestInnerSubscriber[] arrayOfCombineLatestInnerSubscriber = new FlowableCombineLatest.CombineLatestInnerSubscriber[param1Int1];
      for (byte b = 0; b < param1Int1; b++)
        arrayOfCombineLatestInnerSubscriber[b] = new FlowableCombineLatest.CombineLatestInnerSubscriber(this, b, param1Int2); 
      this.subscribers = (FlowableCombineLatest.CombineLatestInnerSubscriber<T>[])arrayOfCombineLatestInnerSubscriber;
      this.latest = new Object[param1Int1];
      this.queue = new SpscLinkedArrayQueue(param1Int2);
      this.requested = new AtomicLong();
      this.error = new AtomicReference<Throwable>();
      this.delayErrors = param1Boolean;
    }
    
    public void cancel() {
      this.cancelled = true;
      cancelAll();
    }
    
    void cancelAll() {
      FlowableCombineLatest.CombineLatestInnerSubscriber<T>[] arrayOfCombineLatestInnerSubscriber = this.subscribers;
      int i = arrayOfCombineLatestInnerSubscriber.length;
      for (byte b = 0; b < i; b++)
        arrayOfCombineLatestInnerSubscriber[b].cancel(); 
    }
    
    boolean checkTerminated(boolean param1Boolean1, boolean param1Boolean2, Subscriber<?> param1Subscriber, SpscLinkedArrayQueue<?> param1SpscLinkedArrayQueue) {
      if (this.cancelled) {
        cancelAll();
        param1SpscLinkedArrayQueue.clear();
        return true;
      } 
      if (param1Boolean1) {
        Throwable throwable;
        if (this.delayErrors) {
          if (param1Boolean2) {
            cancelAll();
            throwable = ExceptionHelper.terminate(this.error);
            if (throwable != null && throwable != ExceptionHelper.TERMINATED) {
              param1Subscriber.onError(throwable);
            } else {
              param1Subscriber.onComplete();
            } 
            return true;
          } 
        } else {
          Throwable throwable1 = ExceptionHelper.terminate(this.error);
          if (throwable1 != null && throwable1 != ExceptionHelper.TERMINATED) {
            cancelAll();
            throwable.clear();
            param1Subscriber.onError(throwable1);
            return true;
          } 
          if (param1Boolean2) {
            cancelAll();
            param1Subscriber.onComplete();
            return true;
          } 
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
        drainOutput();
      } else {
        drainAsync();
      } 
    }
    
    void drainAsync() {
      int j;
      Subscriber<? super R> subscriber = this.downstream;
      SpscLinkedArrayQueue<Object> spscLinkedArrayQueue = this.queue;
      int i = 1;
      do {
        long l1 = this.requested.get();
        long l2 = 0L;
        while (l2 != l1) {
          boolean bool1;
          boolean bool = this.done;
          Object object = spscLinkedArrayQueue.poll();
          if (object == null) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          if (checkTerminated(bool, bool1, subscriber, spscLinkedArrayQueue))
            return; 
          if (bool1)
            break; 
          Object[] arrayOfObject = (Object[])spscLinkedArrayQueue.poll();
          try {
          
          } finally {
            spscLinkedArrayQueue = null;
            Exceptions.throwIfFatal((Throwable)spscLinkedArrayQueue);
            cancelAll();
            ExceptionHelper.addThrowable(this.error, (Throwable)spscLinkedArrayQueue);
            subscriber.onError(ExceptionHelper.terminate(this.error));
          } 
        } 
        if (l2 == l1 && checkTerminated(this.done, spscLinkedArrayQueue.isEmpty(), subscriber, spscLinkedArrayQueue))
          return; 
        if (l2 != 0L && l1 != Long.MAX_VALUE)
          this.requested.addAndGet(-l2); 
        j = addAndGet(-i);
        i = j;
      } while (j != 0);
    }
    
    void drainOutput() {
      int j;
      Subscriber<? super R> subscriber = this.downstream;
      SpscLinkedArrayQueue<Object> spscLinkedArrayQueue = this.queue;
      int i = 1;
      do {
        if (this.cancelled) {
          spscLinkedArrayQueue.clear();
          return;
        } 
        Throwable throwable = this.error.get();
        if (throwable != null) {
          spscLinkedArrayQueue.clear();
          subscriber.onError(throwable);
          return;
        } 
        boolean bool1 = this.done;
        boolean bool2 = spscLinkedArrayQueue.isEmpty();
        if (!bool2)
          subscriber.onNext(null); 
        if (bool1 && bool2) {
          subscriber.onComplete();
          return;
        } 
        j = addAndGet(-i);
        i = j;
      } while (j != 0);
    }
    
    void innerComplete(int param1Int) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield latest : [Ljava/lang/Object;
      //   6: astore_2
      //   7: aload_2
      //   8: iload_1
      //   9: aaload
      //   10: ifnull -> 42
      //   13: aload_0
      //   14: getfield completedSources : I
      //   17: iconst_1
      //   18: iadd
      //   19: istore_1
      //   20: iload_1
      //   21: aload_2
      //   22: arraylength
      //   23: if_icmpne -> 34
      //   26: aload_0
      //   27: iconst_1
      //   28: putfield done : Z
      //   31: goto -> 47
      //   34: aload_0
      //   35: iload_1
      //   36: putfield completedSources : I
      //   39: aload_0
      //   40: monitorexit
      //   41: return
      //   42: aload_0
      //   43: iconst_1
      //   44: putfield done : Z
      //   47: aload_0
      //   48: monitorexit
      //   49: aload_0
      //   50: invokevirtual drain : ()V
      //   53: return
      //   54: astore_2
      //   55: aload_0
      //   56: monitorexit
      //   57: aload_2
      //   58: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	54	finally
      //   13	31	54	finally
      //   34	41	54	finally
      //   42	47	54	finally
      //   47	49	54	finally
      //   55	57	54	finally
    }
    
    void innerError(int param1Int, Throwable param1Throwable) {
      if (ExceptionHelper.addThrowable(this.error, param1Throwable)) {
        if (!this.delayErrors) {
          cancelAll();
          this.done = true;
          drain();
        } else {
          innerComplete(param1Int);
        } 
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    void innerValue(int param1Int, T param1T) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield latest : [Ljava/lang/Object;
      //   6: astore_3
      //   7: aload_0
      //   8: getfield nonEmptySources : I
      //   11: istore #4
      //   13: iload #4
      //   15: istore #5
      //   17: aload_3
      //   18: iload_1
      //   19: aaload
      //   20: ifnonnull -> 35
      //   23: iload #4
      //   25: iconst_1
      //   26: iadd
      //   27: istore #5
      //   29: aload_0
      //   30: iload #5
      //   32: putfield nonEmptySources : I
      //   35: aload_3
      //   36: iload_1
      //   37: aload_2
      //   38: aastore
      //   39: aload_3
      //   40: arraylength
      //   41: iload #5
      //   43: if_icmpne -> 70
      //   46: aload_0
      //   47: getfield queue : Lio/reactivex/internal/queue/SpscLinkedArrayQueue;
      //   50: aload_0
      //   51: getfield subscribers : [Lio/reactivex/internal/operators/flowable/FlowableCombineLatest$CombineLatestInnerSubscriber;
      //   54: iload_1
      //   55: aaload
      //   56: aload_3
      //   57: invokevirtual clone : ()Ljava/lang/Object;
      //   60: invokevirtual offer : (Ljava/lang/Object;Ljava/lang/Object;)Z
      //   63: pop
      //   64: iconst_0
      //   65: istore #5
      //   67: goto -> 73
      //   70: iconst_1
      //   71: istore #5
      //   73: aload_0
      //   74: monitorexit
      //   75: iload #5
      //   77: ifeq -> 92
      //   80: aload_0
      //   81: getfield subscribers : [Lio/reactivex/internal/operators/flowable/FlowableCombineLatest$CombineLatestInnerSubscriber;
      //   84: iload_1
      //   85: aaload
      //   86: invokevirtual requestOne : ()V
      //   89: goto -> 96
      //   92: aload_0
      //   93: invokevirtual drain : ()V
      //   96: return
      //   97: astore_2
      //   98: aload_0
      //   99: monitorexit
      //   100: aload_2
      //   101: athrow
      // Exception table:
      //   from	to	target	type
      //   2	13	97	finally
      //   29	35	97	finally
      //   39	64	97	finally
      //   73	75	97	finally
      //   98	100	97	finally
    }
    
    public boolean isEmpty() {
      return this.queue.isEmpty();
    }
    
    public R poll() throws Exception {
      Object object1 = this.queue.poll();
      if (object1 == null)
        return null; 
      Object[] arrayOfObject = (Object[])this.queue.poll();
      Object object2 = ObjectHelper.requireNonNull(this.combiner.apply(arrayOfObject), "The combiner returned a null value");
      ((FlowableCombineLatest.CombineLatestInnerSubscriber)object1).requestOne();
      return (R)object2;
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.add(this.requested, param1Long);
        drain();
      } 
    }
    
    public int requestFusion(int param1Int) {
      boolean bool = false;
      if ((param1Int & 0x4) != 0)
        return 0; 
      param1Int &= 0x2;
      if (param1Int != 0)
        bool = true; 
      this.outputFused = bool;
      return param1Int;
    }
    
    void subscribe(Publisher<? extends T>[] param1ArrayOfPublisher, int param1Int) {
      FlowableCombineLatest.CombineLatestInnerSubscriber<T>[] arrayOfCombineLatestInnerSubscriber = this.subscribers;
      for (byte b = 0; b < param1Int && !this.done && !this.cancelled; b++)
        param1ArrayOfPublisher[b].subscribe((Subscriber)arrayOfCombineLatestInnerSubscriber[b]); 
    }
  }
  
  static final class CombineLatestInnerSubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<T> {
    private static final long serialVersionUID = -8730235182291002949L;
    
    final int index;
    
    final int limit;
    
    final FlowableCombineLatest.CombineLatestCoordinator<T, ?> parent;
    
    final int prefetch;
    
    int produced;
    
    CombineLatestInnerSubscriber(FlowableCombineLatest.CombineLatestCoordinator<T, ?> param1CombineLatestCoordinator, int param1Int1, int param1Int2) {
      this.parent = param1CombineLatestCoordinator;
      this.index = param1Int1;
      this.prefetch = param1Int2;
      this.limit = param1Int2 - (param1Int2 >> 2);
    }
    
    public void cancel() {
      SubscriptionHelper.cancel(this);
    }
    
    public void onComplete() {
      this.parent.innerComplete(this.index);
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.innerError(this.index, param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.parent.innerValue(this.index, param1T);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.setOnce(this, param1Subscription, this.prefetch);
    }
    
    public void requestOne() {
      int i = this.produced + 1;
      if (i == this.limit) {
        this.produced = 0;
        get().request(i);
      } else {
        this.produced = i;
      } 
    }
  }
  
  final class SingletonArrayFunc implements Function<T, R> {
    public R apply(T param1T) throws Exception {
      return (R)FlowableCombineLatest.this.combiner.apply(new Object[] { param1T });
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableCombineLatest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */