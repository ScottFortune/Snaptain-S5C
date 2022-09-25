package io.reactivex.internal.operators.flowable;

import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.ResettableConnectable;
import io.reactivex.internal.fuseable.HasUpstreamPublisher;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowablePublishAlt<T> extends ConnectableFlowable<T> implements HasUpstreamPublisher<T>, ResettableConnectable {
  final int bufferSize;
  
  final AtomicReference<PublishConnection<T>> current;
  
  final Publisher<T> source;
  
  public FlowablePublishAlt(Publisher<T> paramPublisher, int paramInt) {
    this.source = paramPublisher;
    this.bufferSize = paramInt;
    this.current = new AtomicReference<PublishConnection<T>>();
  }
  
  public void connect(Consumer<? super Disposable> paramConsumer) {
    // Byte code:
    //   0: aload_0
    //   1: getfield current : Ljava/util/concurrent/atomic/AtomicReference;
    //   4: invokevirtual get : ()Ljava/lang/Object;
    //   7: checkcast io/reactivex/internal/operators/flowable/FlowablePublishAlt$PublishConnection
    //   10: astore_2
    //   11: aload_2
    //   12: ifnull -> 24
    //   15: aload_2
    //   16: astore_3
    //   17: aload_2
    //   18: invokevirtual isDisposed : ()Z
    //   21: ifeq -> 55
    //   24: new io/reactivex/internal/operators/flowable/FlowablePublishAlt$PublishConnection
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
    //   56: getfield connect : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   59: invokevirtual get : ()Z
    //   62: istore #4
    //   64: iconst_1
    //   65: istore #5
    //   67: iload #4
    //   69: ifne -> 87
    //   72: aload_3
    //   73: getfield connect : Ljava/util/concurrent/atomic/AtomicBoolean;
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
    //   99: ifeq -> 112
    //   102: aload_0
    //   103: getfield source : Lorg/reactivestreams/Publisher;
    //   106: aload_3
    //   107: invokeinterface subscribe : (Lorg/reactivestreams/Subscriber;)V
    //   112: return
    //   113: astore_1
    //   114: aload_1
    //   115: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
    //   118: aload_1
    //   119: invokestatic wrapOrThrow : (Ljava/lang/Throwable;)Ljava/lang/RuntimeException;
    //   122: astore_1
    //   123: goto -> 128
    //   126: aload_1
    //   127: athrow
    //   128: goto -> 126
    // Exception table:
    //   from	to	target	type
    //   90	97	113	finally
  }
  
  public int publishBufferSize() {
    return this.bufferSize;
  }
  
  public void resetIf(Disposable paramDisposable) {
    this.current.compareAndSet((PublishConnection<T>)paramDisposable, null);
  }
  
  public Publisher<T> source() {
    return this.source;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    PublishConnection<T> publishConnection;
    while (true) {
      PublishConnection<T> publishConnection1 = this.current.get();
      publishConnection = publishConnection1;
      if (publishConnection1 == null) {
        publishConnection = new PublishConnection<T>(this.current, this.bufferSize);
        if (!this.current.compareAndSet(publishConnection1, publishConnection))
          continue; 
      } 
      break;
    } 
    InnerSubscription<T> innerSubscription = new InnerSubscription<T>(paramSubscriber, publishConnection);
    paramSubscriber.onSubscribe(innerSubscription);
    if (publishConnection.add(innerSubscription)) {
      if (innerSubscription.isCancelled()) {
        publishConnection.remove(innerSubscription);
      } else {
        publishConnection.drain();
      } 
      return;
    } 
    Throwable throwable = publishConnection.error;
    if (throwable != null) {
      paramSubscriber.onError(throwable);
    } else {
      paramSubscriber.onComplete();
    } 
  }
  
  static final class InnerSubscription<T> extends AtomicLong implements Subscription {
    private static final long serialVersionUID = 2845000326761540265L;
    
    final Subscriber<? super T> downstream;
    
    long emitted;
    
    final FlowablePublishAlt.PublishConnection<T> parent;
    
    InnerSubscription(Subscriber<? super T> param1Subscriber, FlowablePublishAlt.PublishConnection<T> param1PublishConnection) {
      this.downstream = param1Subscriber;
      this.parent = param1PublishConnection;
    }
    
    public void cancel() {
      if (getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
        this.parent.remove(this);
        this.parent.drain();
      } 
    }
    
    public boolean isCancelled() {
      boolean bool;
      if (get() == Long.MIN_VALUE) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void request(long param1Long) {
      BackpressureHelper.addCancel(this, param1Long);
      this.parent.drain();
    }
  }
  
  static final class PublishConnection<T> extends AtomicInteger implements FlowableSubscriber<T>, Disposable {
    static final FlowablePublishAlt.InnerSubscription[] EMPTY = new FlowablePublishAlt.InnerSubscription[0];
    
    static final FlowablePublishAlt.InnerSubscription[] TERMINATED = new FlowablePublishAlt.InnerSubscription[0];
    
    private static final long serialVersionUID = -1672047311619175801L;
    
    final int bufferSize;
    
    final AtomicBoolean connect;
    
    int consumed;
    
    final AtomicReference<PublishConnection<T>> current;
    
    volatile boolean done;
    
    Throwable error;
    
    volatile SimpleQueue<T> queue;
    
    int sourceMode;
    
    final AtomicReference<FlowablePublishAlt.InnerSubscription<T>[]> subscribers;
    
    final AtomicReference<Subscription> upstream;
    
    PublishConnection(AtomicReference<PublishConnection<T>> param1AtomicReference, int param1Int) {
      this.current = param1AtomicReference;
      this.upstream = new AtomicReference<Subscription>();
      this.connect = new AtomicBoolean();
      this.bufferSize = param1Int;
      this.subscribers = new AtomicReference(EMPTY);
    }
    
    boolean add(FlowablePublishAlt.InnerSubscription<T> param1InnerSubscription) {
      while (true) {
        FlowablePublishAlt.InnerSubscription[] arrayOfInnerSubscription1 = (FlowablePublishAlt.InnerSubscription[])this.subscribers.get();
        if (arrayOfInnerSubscription1 == TERMINATED)
          return false; 
        int i = arrayOfInnerSubscription1.length;
        FlowablePublishAlt.InnerSubscription[] arrayOfInnerSubscription2 = new FlowablePublishAlt.InnerSubscription[i + 1];
        System.arraycopy(arrayOfInnerSubscription1, 0, arrayOfInnerSubscription2, 0, i);
        arrayOfInnerSubscription2[i] = param1InnerSubscription;
        if (this.subscribers.compareAndSet(arrayOfInnerSubscription1, arrayOfInnerSubscription2))
          return true; 
      } 
    }
    
    boolean checkTerminated(boolean param1Boolean1, boolean param1Boolean2) {
      byte b = 0;
      if (param1Boolean1 && param1Boolean2) {
        Throwable throwable = this.error;
        if (throwable != null) {
          signalError(throwable);
        } else {
          FlowablePublishAlt.InnerSubscription[] arrayOfInnerSubscription = (FlowablePublishAlt.InnerSubscription[])this.subscribers.getAndSet(TERMINATED);
          int i = arrayOfInnerSubscription.length;
          while (b < i) {
            FlowablePublishAlt.InnerSubscription innerSubscription = arrayOfInnerSubscription[b];
            if (!innerSubscription.isCancelled())
              innerSubscription.downstream.onComplete(); 
            b++;
          } 
        } 
        return true;
      } 
      return false;
    }
    
    public void dispose() {
      this.subscribers.getAndSet(TERMINATED);
      this.current.compareAndSet(this, null);
      SubscriptionHelper.cancel(this.upstream);
    }
    
    void drain() {
      boolean bool;
      if (getAndIncrement() != 0)
        return; 
      SimpleQueue<T> simpleQueue = this.queue;
      int i = this.consumed;
      int j = this.bufferSize;
      int k = j - (j >> 2);
      if (this.sourceMode != 1) {
        bool = true;
      } else {
        bool = false;
      } 
      int m = 1;
      label68: while (true) {
        if (simpleQueue != null) {
          FlowablePublishAlt.InnerSubscription[] arrayOfInnerSubscription = (FlowablePublishAlt.InnerSubscription[])this.subscribers.get();
          int i1 = arrayOfInnerSubscription.length;
          long l = Long.MAX_VALUE;
          j = 0;
          byte b = 0;
          while (j < i1) {
            FlowablePublishAlt.InnerSubscription innerSubscription = arrayOfInnerSubscription[j];
            long l1 = innerSubscription.get();
            long l2 = l;
            if (l1 != Long.MIN_VALUE) {
              l2 = Math.min(l1 - innerSubscription.emitted, l);
              b = 1;
            } 
            j++;
            l = l2;
          } 
          j = i;
          if (!b) {
            l = 0L;
            j = i;
          } 
          while (l != 0L) {
            boolean bool1 = this.done;
            try {
              boolean bool2;
              Object<T> object = (Object<T>)simpleQueue.poll();
              if (object == null) {
                bool2 = true;
              } else {
                bool2 = false;
              } 
              if (checkTerminated(bool1, bool2))
                return; 
              if (bool2)
                break; 
              i = arrayOfInnerSubscription.length;
            } finally {
              Exception exception = null;
              Exceptions.throwIfFatal(exception);
              ((Subscription)this.upstream.get()).cancel();
              simpleQueue.clear();
              this.done = true;
              signalError(exception);
            } 
          } 
          i = j;
          if (checkTerminated(this.done, simpleQueue.isEmpty()))
            return; 
        } 
        this.consumed = i;
        int n = addAndGet(-m);
        if (n == 0)
          return; 
        m = n;
        SimpleQueue<T> simpleQueue1 = simpleQueue;
        j = i;
        if (simpleQueue == null) {
          simpleQueue1 = this.queue;
          j = i;
          m = n;
        } 
        simpleQueue = simpleQueue1;
        i = j;
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
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
      } else {
        this.error = param1Throwable;
        this.done = true;
        drain();
      } 
    }
    
    public void onNext(T param1T) {
      if (this.sourceMode == 0 && !this.queue.offer(param1T)) {
        onError((Throwable)new MissingBackpressureException("Prefetch queue is full?!"));
        return;
      } 
      drain();
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.setOnce(this.upstream, param1Subscription)) {
        if (param1Subscription instanceof QueueSubscription) {
          QueueSubscription queueSubscription = (QueueSubscription)param1Subscription;
          int i = queueSubscription.requestFusion(7);
          if (i == 1) {
            this.sourceMode = i;
            this.queue = (SimpleQueue<T>)queueSubscription;
            this.done = true;
            drain();
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
    
    void remove(FlowablePublishAlt.InnerSubscription<T> param1InnerSubscription) {
      FlowablePublishAlt.InnerSubscription[] arrayOfInnerSubscription1;
      FlowablePublishAlt.InnerSubscription[] arrayOfInnerSubscription2;
      do {
        byte b2;
        arrayOfInnerSubscription1 = (FlowablePublishAlt.InnerSubscription[])this.subscribers.get();
        int i = arrayOfInnerSubscription1.length;
        if (i == 0)
          break; 
        byte b1 = -1;
        byte b = 0;
        while (true) {
          b2 = b1;
          if (b < i) {
            if (arrayOfInnerSubscription1[b] == param1InnerSubscription) {
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
          arrayOfInnerSubscription2 = new FlowablePublishAlt.InnerSubscription[i - 1];
          System.arraycopy(arrayOfInnerSubscription1, 0, arrayOfInnerSubscription2, 0, b2);
          System.arraycopy(arrayOfInnerSubscription1, b2 + 1, arrayOfInnerSubscription2, b2, i - b2 - 1);
        } 
      } while (!this.subscribers.compareAndSet(arrayOfInnerSubscription1, arrayOfInnerSubscription2));
    }
    
    void signalError(Throwable param1Throwable) {
      for (FlowablePublishAlt.InnerSubscription innerSubscription : (FlowablePublishAlt.InnerSubscription[])this.subscribers.getAndSet(TERMINATED)) {
        if (!innerSubscription.isCancelled())
          innerSubscription.downstream.onError(param1Throwable); 
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowablePublishAlt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */