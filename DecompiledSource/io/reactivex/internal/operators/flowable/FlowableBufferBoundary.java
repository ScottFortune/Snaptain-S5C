package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableBufferBoundary<T, U extends Collection<? super T>, Open, Close> extends AbstractFlowableWithUpstream<T, U> {
  final Function<? super Open, ? extends Publisher<? extends Close>> bufferClose;
  
  final Publisher<? extends Open> bufferOpen;
  
  final Callable<U> bufferSupplier;
  
  public FlowableBufferBoundary(Flowable<T> paramFlowable, Publisher<? extends Open> paramPublisher, Function<? super Open, ? extends Publisher<? extends Close>> paramFunction, Callable<U> paramCallable) {
    super(paramFlowable);
    this.bufferOpen = paramPublisher;
    this.bufferClose = paramFunction;
    this.bufferSupplier = paramCallable;
  }
  
  protected void subscribeActual(Subscriber<? super U> paramSubscriber) {
    BufferBoundarySubscriber<Object, U, Open, Close> bufferBoundarySubscriber = new BufferBoundarySubscriber<Object, U, Open, Close>(paramSubscriber, this.bufferOpen, this.bufferClose, this.bufferSupplier);
    paramSubscriber.onSubscribe(bufferBoundarySubscriber);
    this.source.subscribe(bufferBoundarySubscriber);
  }
  
  static final class BufferBoundarySubscriber<T, C extends Collection<? super T>, Open, Close> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = -8466418554264089604L;
    
    final Function<? super Open, ? extends Publisher<? extends Close>> bufferClose;
    
    final Publisher<? extends Open> bufferOpen;
    
    final Callable<C> bufferSupplier;
    
    Map<Long, C> buffers;
    
    volatile boolean cancelled;
    
    volatile boolean done;
    
    final Subscriber<? super C> downstream;
    
    long emitted;
    
    final AtomicThrowable errors;
    
    long index;
    
    final SpscLinkedArrayQueue<C> queue;
    
    final AtomicLong requested;
    
    final CompositeDisposable subscribers;
    
    final AtomicReference<Subscription> upstream;
    
    BufferBoundarySubscriber(Subscriber<? super C> param1Subscriber, Publisher<? extends Open> param1Publisher, Function<? super Open, ? extends Publisher<? extends Close>> param1Function, Callable<C> param1Callable) {
      this.downstream = param1Subscriber;
      this.bufferSupplier = param1Callable;
      this.bufferOpen = param1Publisher;
      this.bufferClose = param1Function;
      this.queue = new SpscLinkedArrayQueue(Flowable.bufferSize());
      this.subscribers = new CompositeDisposable();
      this.requested = new AtomicLong();
      this.upstream = new AtomicReference<Subscription>();
      this.buffers = new LinkedHashMap<Long, C>();
      this.errors = new AtomicThrowable();
    }
    
    void boundaryError(Disposable param1Disposable, Throwable param1Throwable) {
      SubscriptionHelper.cancel(this.upstream);
      this.subscribers.delete(param1Disposable);
      onError(param1Throwable);
    }
    
    public void cancel() {
      // Byte code:
      //   0: aload_0
      //   1: getfield upstream : Ljava/util/concurrent/atomic/AtomicReference;
      //   4: invokestatic cancel : (Ljava/util/concurrent/atomic/AtomicReference;)Z
      //   7: ifeq -> 53
      //   10: aload_0
      //   11: iconst_1
      //   12: putfield cancelled : Z
      //   15: aload_0
      //   16: getfield subscribers : Lio/reactivex/disposables/CompositeDisposable;
      //   19: invokevirtual dispose : ()V
      //   22: aload_0
      //   23: monitorenter
      //   24: aload_0
      //   25: aconst_null
      //   26: putfield buffers : Ljava/util/Map;
      //   29: aload_0
      //   30: monitorexit
      //   31: aload_0
      //   32: invokevirtual getAndIncrement : ()I
      //   35: ifeq -> 53
      //   38: aload_0
      //   39: getfield queue : Lio/reactivex/internal/queue/SpscLinkedArrayQueue;
      //   42: invokevirtual clear : ()V
      //   45: goto -> 53
      //   48: astore_1
      //   49: aload_0
      //   50: monitorexit
      //   51: aload_1
      //   52: athrow
      //   53: return
      // Exception table:
      //   from	to	target	type
      //   24	31	48	finally
      //   49	51	48	finally
    }
    
    void close(FlowableBufferBoundary.BufferCloseSubscriber<T, C> param1BufferCloseSubscriber, long param1Long) {
      // Byte code:
      //   0: aload_0
      //   1: getfield subscribers : Lio/reactivex/disposables/CompositeDisposable;
      //   4: aload_1
      //   5: invokevirtual delete : (Lio/reactivex/disposables/Disposable;)Z
      //   8: pop
      //   9: aload_0
      //   10: getfield subscribers : Lio/reactivex/disposables/CompositeDisposable;
      //   13: invokevirtual size : ()I
      //   16: ifne -> 33
      //   19: aload_0
      //   20: getfield upstream : Ljava/util/concurrent/atomic/AtomicReference;
      //   23: invokestatic cancel : (Ljava/util/concurrent/atomic/AtomicReference;)Z
      //   26: pop
      //   27: iconst_1
      //   28: istore #4
      //   30: goto -> 36
      //   33: iconst_0
      //   34: istore #4
      //   36: aload_0
      //   37: monitorenter
      //   38: aload_0
      //   39: getfield buffers : Ljava/util/Map;
      //   42: ifnonnull -> 48
      //   45: aload_0
      //   46: monitorexit
      //   47: return
      //   48: aload_0
      //   49: getfield queue : Lio/reactivex/internal/queue/SpscLinkedArrayQueue;
      //   52: aload_0
      //   53: getfield buffers : Ljava/util/Map;
      //   56: lload_2
      //   57: invokestatic valueOf : (J)Ljava/lang/Long;
      //   60: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
      //   65: invokevirtual offer : (Ljava/lang/Object;)Z
      //   68: pop
      //   69: aload_0
      //   70: monitorexit
      //   71: iload #4
      //   73: ifeq -> 81
      //   76: aload_0
      //   77: iconst_1
      //   78: putfield done : Z
      //   81: aload_0
      //   82: invokevirtual drain : ()V
      //   85: return
      //   86: astore_1
      //   87: aload_0
      //   88: monitorexit
      //   89: aload_1
      //   90: athrow
      // Exception table:
      //   from	to	target	type
      //   38	47	86	finally
      //   48	71	86	finally
      //   87	89	86	finally
    }
    
    void drain() {
      int j;
      if (getAndIncrement() != 0)
        return; 
      long l = this.emitted;
      Subscriber<? super C> subscriber = this.downstream;
      SpscLinkedArrayQueue<C> spscLinkedArrayQueue = this.queue;
      int i = 1;
      do {
        long l1 = this.requested.get();
        while (l != l1) {
          boolean bool1;
          if (this.cancelled) {
            spscLinkedArrayQueue.clear();
            return;
          } 
          boolean bool = this.done;
          if (bool && this.errors.get() != null) {
            spscLinkedArrayQueue.clear();
            subscriber.onError(this.errors.terminate());
            return;
          } 
          Collection collection = (Collection)spscLinkedArrayQueue.poll();
          if (collection == null) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          if (bool && bool1) {
            subscriber.onComplete();
            return;
          } 
          if (bool1)
            break; 
          subscriber.onNext(collection);
          l++;
        } 
        if (l == l1) {
          if (this.cancelled) {
            spscLinkedArrayQueue.clear();
            return;
          } 
          if (this.done) {
            if (this.errors.get() != null) {
              spscLinkedArrayQueue.clear();
              subscriber.onError(this.errors.terminate());
              return;
            } 
            if (spscLinkedArrayQueue.isEmpty()) {
              subscriber.onComplete();
              return;
            } 
          } 
        } 
        this.emitted = l;
        j = addAndGet(-i);
        i = j;
      } while (j != 0);
    }
    
    public void onComplete() {
      // Byte code:
      //   0: aload_0
      //   1: getfield subscribers : Lio/reactivex/disposables/CompositeDisposable;
      //   4: invokevirtual dispose : ()V
      //   7: aload_0
      //   8: monitorenter
      //   9: aload_0
      //   10: getfield buffers : Ljava/util/Map;
      //   13: astore_1
      //   14: aload_1
      //   15: ifnonnull -> 21
      //   18: aload_0
      //   19: monitorexit
      //   20: return
      //   21: aload_1
      //   22: invokeinterface values : ()Ljava/util/Collection;
      //   27: invokeinterface iterator : ()Ljava/util/Iterator;
      //   32: astore_1
      //   33: aload_1
      //   34: invokeinterface hasNext : ()Z
      //   39: ifeq -> 64
      //   42: aload_1
      //   43: invokeinterface next : ()Ljava/lang/Object;
      //   48: checkcast java/util/Collection
      //   51: astore_2
      //   52: aload_0
      //   53: getfield queue : Lio/reactivex/internal/queue/SpscLinkedArrayQueue;
      //   56: aload_2
      //   57: invokevirtual offer : (Ljava/lang/Object;)Z
      //   60: pop
      //   61: goto -> 33
      //   64: aload_0
      //   65: aconst_null
      //   66: putfield buffers : Ljava/util/Map;
      //   69: aload_0
      //   70: monitorexit
      //   71: aload_0
      //   72: iconst_1
      //   73: putfield done : Z
      //   76: aload_0
      //   77: invokevirtual drain : ()V
      //   80: return
      //   81: astore_1
      //   82: aload_0
      //   83: monitorexit
      //   84: goto -> 89
      //   87: aload_1
      //   88: athrow
      //   89: goto -> 87
      // Exception table:
      //   from	to	target	type
      //   9	14	81	finally
      //   18	20	81	finally
      //   21	33	81	finally
      //   33	61	81	finally
      //   64	71	81	finally
      //   82	84	81	finally
    }
    
    public void onError(Throwable param1Throwable) {
      // Byte code:
      //   0: aload_0
      //   1: getfield errors : Lio/reactivex/internal/util/AtomicThrowable;
      //   4: aload_1
      //   5: invokevirtual addThrowable : (Ljava/lang/Throwable;)Z
      //   8: ifeq -> 44
      //   11: aload_0
      //   12: getfield subscribers : Lio/reactivex/disposables/CompositeDisposable;
      //   15: invokevirtual dispose : ()V
      //   18: aload_0
      //   19: monitorenter
      //   20: aload_0
      //   21: aconst_null
      //   22: putfield buffers : Ljava/util/Map;
      //   25: aload_0
      //   26: monitorexit
      //   27: aload_0
      //   28: iconst_1
      //   29: putfield done : Z
      //   32: aload_0
      //   33: invokevirtual drain : ()V
      //   36: goto -> 48
      //   39: astore_1
      //   40: aload_0
      //   41: monitorexit
      //   42: aload_1
      //   43: athrow
      //   44: aload_1
      //   45: invokestatic onError : (Ljava/lang/Throwable;)V
      //   48: return
      // Exception table:
      //   from	to	target	type
      //   20	27	39	finally
      //   40	42	39	finally
    }
    
    public void onNext(T param1T) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield buffers : Ljava/util/Map;
      //   6: astore_2
      //   7: aload_2
      //   8: ifnonnull -> 14
      //   11: aload_0
      //   12: monitorexit
      //   13: return
      //   14: aload_2
      //   15: invokeinterface values : ()Ljava/util/Collection;
      //   20: invokeinterface iterator : ()Ljava/util/Iterator;
      //   25: astore_2
      //   26: aload_2
      //   27: invokeinterface hasNext : ()Z
      //   32: ifeq -> 54
      //   35: aload_2
      //   36: invokeinterface next : ()Ljava/lang/Object;
      //   41: checkcast java/util/Collection
      //   44: aload_1
      //   45: invokeinterface add : (Ljava/lang/Object;)Z
      //   50: pop
      //   51: goto -> 26
      //   54: aload_0
      //   55: monitorexit
      //   56: return
      //   57: astore_1
      //   58: aload_0
      //   59: monitorexit
      //   60: goto -> 65
      //   63: aload_1
      //   64: athrow
      //   65: goto -> 63
      // Exception table:
      //   from	to	target	type
      //   2	7	57	finally
      //   11	13	57	finally
      //   14	26	57	finally
      //   26	51	57	finally
      //   54	56	57	finally
      //   58	60	57	finally
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.setOnce(this.upstream, param1Subscription)) {
        BufferOpenSubscriber bufferOpenSubscriber = new BufferOpenSubscriber(this);
        this.subscribers.add(bufferOpenSubscriber);
        this.bufferOpen.subscribe((Subscriber)bufferOpenSubscriber);
        param1Subscription.request(Long.MAX_VALUE);
      } 
    }
    
    void open(Open param1Open) {
      // Byte code:
      //   0: aload_0
      //   1: getfield bufferSupplier : Ljava/util/concurrent/Callable;
      //   4: invokeinterface call : ()Ljava/lang/Object;
      //   9: ldc 'The bufferSupplier returned a null Collection'
      //   11: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
      //   14: checkcast java/util/Collection
      //   17: astore_2
      //   18: aload_0
      //   19: getfield bufferClose : Lio/reactivex/functions/Function;
      //   22: aload_1
      //   23: invokeinterface apply : (Ljava/lang/Object;)Ljava/lang/Object;
      //   28: ldc_w 'The bufferClose returned a null Publisher'
      //   31: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
      //   34: checkcast org/reactivestreams/Publisher
      //   37: astore_1
      //   38: aload_0
      //   39: getfield index : J
      //   42: lstore_3
      //   43: aload_0
      //   44: lconst_1
      //   45: lload_3
      //   46: ladd
      //   47: putfield index : J
      //   50: aload_0
      //   51: monitorenter
      //   52: aload_0
      //   53: getfield buffers : Ljava/util/Map;
      //   56: astore #5
      //   58: aload #5
      //   60: ifnonnull -> 66
      //   63: aload_0
      //   64: monitorexit
      //   65: return
      //   66: aload #5
      //   68: lload_3
      //   69: invokestatic valueOf : (J)Ljava/lang/Long;
      //   72: aload_2
      //   73: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
      //   78: pop
      //   79: aload_0
      //   80: monitorexit
      //   81: new io/reactivex/internal/operators/flowable/FlowableBufferBoundary$BufferCloseSubscriber
      //   84: dup
      //   85: aload_0
      //   86: lload_3
      //   87: invokespecial <init> : (Lio/reactivex/internal/operators/flowable/FlowableBufferBoundary$BufferBoundarySubscriber;J)V
      //   90: astore_2
      //   91: aload_0
      //   92: getfield subscribers : Lio/reactivex/disposables/CompositeDisposable;
      //   95: aload_2
      //   96: invokevirtual add : (Lio/reactivex/disposables/Disposable;)Z
      //   99: pop
      //   100: aload_1
      //   101: aload_2
      //   102: invokeinterface subscribe : (Lorg/reactivestreams/Subscriber;)V
      //   107: return
      //   108: astore_1
      //   109: aload_0
      //   110: monitorexit
      //   111: aload_1
      //   112: athrow
      //   113: astore_1
      //   114: aload_1
      //   115: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
      //   118: aload_0
      //   119: getfield upstream : Ljava/util/concurrent/atomic/AtomicReference;
      //   122: invokestatic cancel : (Ljava/util/concurrent/atomic/AtomicReference;)Z
      //   125: pop
      //   126: aload_0
      //   127: aload_1
      //   128: invokevirtual onError : (Ljava/lang/Throwable;)V
      //   131: return
      // Exception table:
      //   from	to	target	type
      //   0	38	113	finally
      //   52	58	108	finally
      //   63	65	108	finally
      //   66	81	108	finally
      //   109	111	108	finally
    }
    
    void openComplete(BufferOpenSubscriber<Open> param1BufferOpenSubscriber) {
      this.subscribers.delete(param1BufferOpenSubscriber);
      if (this.subscribers.size() == 0) {
        SubscriptionHelper.cancel(this.upstream);
        this.done = true;
        drain();
      } 
    }
    
    public void request(long param1Long) {
      BackpressureHelper.add(this.requested, param1Long);
      drain();
    }
    
    static final class BufferOpenSubscriber<Open> extends AtomicReference<Subscription> implements FlowableSubscriber<Open>, Disposable {
      private static final long serialVersionUID = -8498650778633225126L;
      
      final FlowableBufferBoundary.BufferBoundarySubscriber<?, ?, Open, ?> parent;
      
      BufferOpenSubscriber(FlowableBufferBoundary.BufferBoundarySubscriber<?, ?, Open, ?> param2BufferBoundarySubscriber) {
        this.parent = param2BufferBoundarySubscriber;
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
        lazySet((Subscription)SubscriptionHelper.CANCELLED);
        this.parent.openComplete(this);
      }
      
      public void onError(Throwable param2Throwable) {
        lazySet((Subscription)SubscriptionHelper.CANCELLED);
        this.parent.boundaryError(this, param2Throwable);
      }
      
      public void onNext(Open param2Open) {
        this.parent.open(param2Open);
      }
      
      public void onSubscribe(Subscription param2Subscription) {
        SubscriptionHelper.setOnce(this, param2Subscription, Long.MAX_VALUE);
      }
    }
  }
  
  static final class BufferOpenSubscriber<Open> extends AtomicReference<Subscription> implements FlowableSubscriber<Open>, Disposable {
    private static final long serialVersionUID = -8498650778633225126L;
    
    final FlowableBufferBoundary.BufferBoundarySubscriber<?, ?, Open, ?> parent;
    
    BufferOpenSubscriber(FlowableBufferBoundary.BufferBoundarySubscriber<?, ?, Open, ?> param1BufferBoundarySubscriber) {
      this.parent = param1BufferBoundarySubscriber;
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
      lazySet((Subscription)SubscriptionHelper.CANCELLED);
      this.parent.openComplete(this);
    }
    
    public void onError(Throwable param1Throwable) {
      lazySet((Subscription)SubscriptionHelper.CANCELLED);
      this.parent.boundaryError(this, param1Throwable);
    }
    
    public void onNext(Open param1Open) {
      this.parent.open(param1Open);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.setOnce(this, param1Subscription, Long.MAX_VALUE);
    }
  }
  
  static final class BufferCloseSubscriber<T, C extends Collection<? super T>> extends AtomicReference<Subscription> implements FlowableSubscriber<Object>, Disposable {
    private static final long serialVersionUID = -8498650778633225126L;
    
    final long index;
    
    final FlowableBufferBoundary.BufferBoundarySubscriber<T, C, ?, ?> parent;
    
    BufferCloseSubscriber(FlowableBufferBoundary.BufferBoundarySubscriber<T, C, ?, ?> param1BufferBoundarySubscriber, long param1Long) {
      this.parent = param1BufferBoundarySubscriber;
      this.index = param1Long;
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
      if (get() != SubscriptionHelper.CANCELLED) {
        lazySet((Subscription)SubscriptionHelper.CANCELLED);
        this.parent.close(this, this.index);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (get() != SubscriptionHelper.CANCELLED) {
        lazySet((Subscription)SubscriptionHelper.CANCELLED);
        this.parent.boundaryError(this, param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(Object param1Object) {
      param1Object = get();
      if (param1Object != SubscriptionHelper.CANCELLED) {
        lazySet((Subscription)SubscriptionHelper.CANCELLED);
        param1Object.cancel();
        this.parent.close(this, this.index);
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.setOnce(this, param1Subscription, Long.MAX_VALUE);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableBufferBoundary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */