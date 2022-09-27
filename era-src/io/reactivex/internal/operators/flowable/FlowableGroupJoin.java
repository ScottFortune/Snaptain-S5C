package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.processors.UnicastProcessor;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableGroupJoin<TLeft, TRight, TLeftEnd, TRightEnd, R> extends AbstractFlowableWithUpstream<TLeft, R> {
  final Function<? super TLeft, ? extends Publisher<TLeftEnd>> leftEnd;
  
  final Publisher<? extends TRight> other;
  
  final BiFunction<? super TLeft, ? super Flowable<TRight>, ? extends R> resultSelector;
  
  final Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd;
  
  public FlowableGroupJoin(Flowable<TLeft> paramFlowable, Publisher<? extends TRight> paramPublisher, Function<? super TLeft, ? extends Publisher<TLeftEnd>> paramFunction, Function<? super TRight, ? extends Publisher<TRightEnd>> paramFunction1, BiFunction<? super TLeft, ? super Flowable<TRight>, ? extends R> paramBiFunction) {
    super(paramFlowable);
    this.other = paramPublisher;
    this.leftEnd = paramFunction;
    this.rightEnd = paramFunction1;
    this.resultSelector = paramBiFunction;
  }
  
  protected void subscribeActual(Subscriber<? super R> paramSubscriber) {
    GroupJoinSubscription<TLeft, TRight, TLeftEnd, TRightEnd, R> groupJoinSubscription = new GroupJoinSubscription<TLeft, TRight, TLeftEnd, TRightEnd, R>(paramSubscriber, this.leftEnd, this.rightEnd, this.resultSelector);
    paramSubscriber.onSubscribe(groupJoinSubscription);
    LeftRightSubscriber leftRightSubscriber1 = new LeftRightSubscriber(groupJoinSubscription, true);
    groupJoinSubscription.disposables.add(leftRightSubscriber1);
    LeftRightSubscriber leftRightSubscriber2 = new LeftRightSubscriber(groupJoinSubscription, false);
    groupJoinSubscription.disposables.add(leftRightSubscriber2);
    this.source.subscribe(leftRightSubscriber1);
    this.other.subscribe((Subscriber)leftRightSubscriber2);
  }
  
  static final class GroupJoinSubscription<TLeft, TRight, TLeftEnd, TRightEnd, R> extends AtomicInteger implements Subscription, JoinSupport {
    static final Integer LEFT_CLOSE = Integer.valueOf(3);
    
    static final Integer LEFT_VALUE = Integer.valueOf(1);
    
    static final Integer RIGHT_CLOSE = Integer.valueOf(4);
    
    static final Integer RIGHT_VALUE = Integer.valueOf(2);
    
    private static final long serialVersionUID = -6071216598687999801L;
    
    final AtomicInteger active;
    
    volatile boolean cancelled;
    
    final CompositeDisposable disposables;
    
    final Subscriber<? super R> downstream;
    
    final AtomicReference<Throwable> error;
    
    final Function<? super TLeft, ? extends Publisher<TLeftEnd>> leftEnd;
    
    int leftIndex;
    
    final Map<Integer, UnicastProcessor<TRight>> lefts;
    
    final SpscLinkedArrayQueue<Object> queue;
    
    final AtomicLong requested;
    
    final BiFunction<? super TLeft, ? super Flowable<TRight>, ? extends R> resultSelector;
    
    final Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd;
    
    int rightIndex;
    
    final Map<Integer, TRight> rights;
    
    static {
    
    }
    
    GroupJoinSubscription(Subscriber<? super R> param1Subscriber, Function<? super TLeft, ? extends Publisher<TLeftEnd>> param1Function, Function<? super TRight, ? extends Publisher<TRightEnd>> param1Function1, BiFunction<? super TLeft, ? super Flowable<TRight>, ? extends R> param1BiFunction) {
      this.downstream = param1Subscriber;
      this.requested = new AtomicLong();
      this.disposables = new CompositeDisposable();
      this.queue = new SpscLinkedArrayQueue(Flowable.bufferSize());
      this.lefts = new LinkedHashMap<Integer, UnicastProcessor<TRight>>();
      this.rights = new LinkedHashMap<Integer, TRight>();
      this.error = new AtomicReference<Throwable>();
      this.leftEnd = param1Function;
      this.rightEnd = param1Function1;
      this.resultSelector = param1BiFunction;
      this.active = new AtomicInteger(2);
    }
    
    public void cancel() {
      if (this.cancelled)
        return; 
      this.cancelled = true;
      cancelAll();
      if (getAndIncrement() == 0)
        this.queue.clear(); 
    }
    
    void cancelAll() {
      this.disposables.dispose();
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      SpscLinkedArrayQueue<Object> spscLinkedArrayQueue = this.queue;
      Subscriber<? super R> subscriber = this.downstream;
      int i = 1;
      while (true) {
        Iterator<UnicastProcessor> iterator1;
        int j;
        UnicastProcessor<TRight> unicastProcessor;
        Iterator<UnicastProcessor> iterator2;
        FlowableGroupJoin.LeftRightEndSubscriber leftRightEndSubscriber;
        boolean bool;
        if (this.cancelled) {
          spscLinkedArrayQueue.clear();
          return;
        } 
        if ((Throwable)this.error.get() != null) {
          spscLinkedArrayQueue.clear();
          cancelAll();
          errorAll(subscriber);
          return;
        } 
        if (this.active.get() == 0) {
          j = 1;
        } else {
          j = 0;
        } 
        Integer integer = (Integer)spscLinkedArrayQueue.poll();
        if (integer == null) {
          bool = true;
        } else {
          bool = false;
        } 
        if (j && bool) {
          iterator1 = this.lefts.values().iterator();
          while (iterator1.hasNext())
            ((UnicastProcessor)iterator1.next()).onComplete(); 
          this.lefts.clear();
          this.rights.clear();
          this.disposables.dispose();
          subscriber.onComplete();
          return;
        } 
        if (bool) {
          j = addAndGet(-i);
          i = j;
          if (j == 0)
            return; 
          continue;
        } 
        Object object = iterator1.poll();
        if (integer == LEFT_VALUE) {
          unicastProcessor = UnicastProcessor.create();
          j = this.leftIndex;
          this.leftIndex = j + 1;
          this.lefts.put(Integer.valueOf(j), unicastProcessor);
          try {
            Publisher publisher = (Publisher)ObjectHelper.requireNonNull(this.leftEnd.apply(object), "The leftEnd returned a null Publisher");
            FlowableGroupJoin.LeftRightEndSubscriber leftRightEndSubscriber1 = new FlowableGroupJoin.LeftRightEndSubscriber(this, true, j);
            this.disposables.add(leftRightEndSubscriber1);
            publisher.subscribe((Subscriber)leftRightEndSubscriber1);
          } finally {
            object = null;
            fail((Throwable)object, subscriber, (SimpleQueue<?>)iterator1);
          } 
        } 
        if (unicastProcessor == RIGHT_VALUE) {
          j = this.rightIndex;
          this.rightIndex = j + 1;
          this.rights.put(Integer.valueOf(j), (TRight)object);
          try {
            Publisher publisher = (Publisher)ObjectHelper.requireNonNull(this.rightEnd.apply(object), "The rightEnd returned a null Publisher");
            leftRightEndSubscriber = new FlowableGroupJoin.LeftRightEndSubscriber(this, false, j);
            this.disposables.add(leftRightEndSubscriber);
            publisher.subscribe((Subscriber)leftRightEndSubscriber);
            if ((Throwable)this.error.get() != null) {
              iterator1.clear();
              return;
            } 
          } finally {
            object = null;
            fail((Throwable)object, subscriber, (SimpleQueue<?>)iterator1);
          } 
          continue;
        } 
        if (iterator2 == LEFT_CLOSE) {
          leftRightEndSubscriber = (FlowableGroupJoin.LeftRightEndSubscriber)object;
          object = this.lefts.remove(Integer.valueOf(leftRightEndSubscriber.index));
          this.disposables.remove(leftRightEndSubscriber);
          if (object != null)
            object.onComplete(); 
          continue;
        } 
        if (leftRightEndSubscriber == RIGHT_CLOSE) {
          object = object;
          this.rights.remove(Integer.valueOf(((FlowableGroupJoin.LeftRightEndSubscriber)object).index));
          this.disposables.remove((Disposable)object);
        } 
      } 
    }
    
    void errorAll(Subscriber<?> param1Subscriber) {
      Throwable throwable = ExceptionHelper.terminate(this.error);
      Iterator<UnicastProcessor> iterator = this.lefts.values().iterator();
      while (iterator.hasNext())
        ((UnicastProcessor)iterator.next()).onError(throwable); 
      this.lefts.clear();
      this.rights.clear();
      param1Subscriber.onError(throwable);
    }
    
    void fail(Throwable param1Throwable, Subscriber<?> param1Subscriber, SimpleQueue<?> param1SimpleQueue) {
      Exceptions.throwIfFatal(param1Throwable);
      ExceptionHelper.addThrowable(this.error, param1Throwable);
      param1SimpleQueue.clear();
      cancelAll();
      errorAll(param1Subscriber);
    }
    
    public void innerClose(boolean param1Boolean, FlowableGroupJoin.LeftRightEndSubscriber param1LeftRightEndSubscriber) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield queue : Lio/reactivex/internal/queue/SpscLinkedArrayQueue;
      //   6: astore_3
      //   7: iload_1
      //   8: ifeq -> 19
      //   11: getstatic io/reactivex/internal/operators/flowable/FlowableGroupJoin$GroupJoinSubscription.LEFT_CLOSE : Ljava/lang/Integer;
      //   14: astore #4
      //   16: goto -> 24
      //   19: getstatic io/reactivex/internal/operators/flowable/FlowableGroupJoin$GroupJoinSubscription.RIGHT_CLOSE : Ljava/lang/Integer;
      //   22: astore #4
      //   24: aload_3
      //   25: aload #4
      //   27: aload_2
      //   28: invokevirtual offer : (Ljava/lang/Object;Ljava/lang/Object;)Z
      //   31: pop
      //   32: aload_0
      //   33: monitorexit
      //   34: aload_0
      //   35: invokevirtual drain : ()V
      //   38: return
      //   39: astore_2
      //   40: aload_0
      //   41: monitorexit
      //   42: aload_2
      //   43: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	39	finally
      //   11	16	39	finally
      //   19	24	39	finally
      //   24	34	39	finally
      //   40	42	39	finally
    }
    
    public void innerCloseError(Throwable param1Throwable) {
      if (ExceptionHelper.addThrowable(this.error, param1Throwable)) {
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void innerComplete(FlowableGroupJoin.LeftRightSubscriber param1LeftRightSubscriber) {
      this.disposables.delete(param1LeftRightSubscriber);
      this.active.decrementAndGet();
      drain();
    }
    
    public void innerError(Throwable param1Throwable) {
      if (ExceptionHelper.addThrowable(this.error, param1Throwable)) {
        this.active.decrementAndGet();
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void innerValue(boolean param1Boolean, Object param1Object) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield queue : Lio/reactivex/internal/queue/SpscLinkedArrayQueue;
      //   6: astore_3
      //   7: iload_1
      //   8: ifeq -> 19
      //   11: getstatic io/reactivex/internal/operators/flowable/FlowableGroupJoin$GroupJoinSubscription.LEFT_VALUE : Ljava/lang/Integer;
      //   14: astore #4
      //   16: goto -> 24
      //   19: getstatic io/reactivex/internal/operators/flowable/FlowableGroupJoin$GroupJoinSubscription.RIGHT_VALUE : Ljava/lang/Integer;
      //   22: astore #4
      //   24: aload_3
      //   25: aload #4
      //   27: aload_2
      //   28: invokevirtual offer : (Ljava/lang/Object;Ljava/lang/Object;)Z
      //   31: pop
      //   32: aload_0
      //   33: monitorexit
      //   34: aload_0
      //   35: invokevirtual drain : ()V
      //   38: return
      //   39: astore_2
      //   40: aload_0
      //   41: monitorexit
      //   42: aload_2
      //   43: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	39	finally
      //   11	16	39	finally
      //   19	24	39	finally
      //   24	34	39	finally
      //   40	42	39	finally
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long))
        BackpressureHelper.add(this.requested, param1Long); 
    }
  }
  
  static interface JoinSupport {
    void innerClose(boolean param1Boolean, FlowableGroupJoin.LeftRightEndSubscriber param1LeftRightEndSubscriber);
    
    void innerCloseError(Throwable param1Throwable);
    
    void innerComplete(FlowableGroupJoin.LeftRightSubscriber param1LeftRightSubscriber);
    
    void innerError(Throwable param1Throwable);
    
    void innerValue(boolean param1Boolean, Object param1Object);
  }
  
  static final class LeftRightEndSubscriber extends AtomicReference<Subscription> implements FlowableSubscriber<Object>, Disposable {
    private static final long serialVersionUID = 1883890389173668373L;
    
    final int index;
    
    final boolean isLeft;
    
    final FlowableGroupJoin.JoinSupport parent;
    
    LeftRightEndSubscriber(FlowableGroupJoin.JoinSupport param1JoinSupport, boolean param1Boolean, int param1Int) {
      this.parent = param1JoinSupport;
      this.isLeft = param1Boolean;
      this.index = param1Int;
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
      this.parent.innerClose(this.isLeft, this);
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.innerCloseError(param1Throwable);
    }
    
    public void onNext(Object param1Object) {
      if (SubscriptionHelper.cancel(this))
        this.parent.innerClose(this.isLeft, this); 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.setOnce(this, param1Subscription, Long.MAX_VALUE);
    }
  }
  
  static final class LeftRightSubscriber extends AtomicReference<Subscription> implements FlowableSubscriber<Object>, Disposable {
    private static final long serialVersionUID = 1883890389173668373L;
    
    final boolean isLeft;
    
    final FlowableGroupJoin.JoinSupport parent;
    
    LeftRightSubscriber(FlowableGroupJoin.JoinSupport param1JoinSupport, boolean param1Boolean) {
      this.parent = param1JoinSupport;
      this.isLeft = param1Boolean;
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
      this.parent.innerComplete(this);
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.innerError(param1Throwable);
    }
    
    public void onNext(Object param1Object) {
      this.parent.innerValue(this.isLeft, param1Object);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.setOnce(this, param1Subscription, Long.MAX_VALUE);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableGroupJoin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */