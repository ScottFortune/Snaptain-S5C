package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.subscribers.QueueDrainSubscriber;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subscribers.DisposableSubscriber;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableBufferBoundarySupplier<T, U extends Collection<? super T>, B> extends AbstractFlowableWithUpstream<T, U> {
  final Callable<? extends Publisher<B>> boundarySupplier;
  
  final Callable<U> bufferSupplier;
  
  public FlowableBufferBoundarySupplier(Flowable<T> paramFlowable, Callable<? extends Publisher<B>> paramCallable, Callable<U> paramCallable1) {
    super(paramFlowable);
    this.boundarySupplier = paramCallable;
    this.bufferSupplier = paramCallable1;
  }
  
  protected void subscribeActual(Subscriber<? super U> paramSubscriber) {
    this.source.subscribe(new BufferBoundarySupplierSubscriber<Object, U, B>((Subscriber<? super U>)new SerializedSubscriber(paramSubscriber), this.bufferSupplier, this.boundarySupplier));
  }
  
  static final class BufferBoundarySubscriber<T, U extends Collection<? super T>, B> extends DisposableSubscriber<B> {
    boolean once;
    
    final FlowableBufferBoundarySupplier.BufferBoundarySupplierSubscriber<T, U, B> parent;
    
    BufferBoundarySubscriber(FlowableBufferBoundarySupplier.BufferBoundarySupplierSubscriber<T, U, B> param1BufferBoundarySupplierSubscriber) {
      this.parent = param1BufferBoundarySupplierSubscriber;
    }
    
    public void onComplete() {
      if (this.once)
        return; 
      this.once = true;
      this.parent.next();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.once) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.once = true;
      this.parent.onError(param1Throwable);
    }
    
    public void onNext(B param1B) {
      if (this.once)
        return; 
      this.once = true;
      cancel();
      this.parent.next();
    }
  }
  
  static final class BufferBoundarySupplierSubscriber<T, U extends Collection<? super T>, B> extends QueueDrainSubscriber<T, U, U> implements FlowableSubscriber<T>, Subscription, Disposable {
    final Callable<? extends Publisher<B>> boundarySupplier;
    
    U buffer;
    
    final Callable<U> bufferSupplier;
    
    final AtomicReference<Disposable> other = new AtomicReference<Disposable>();
    
    Subscription upstream;
    
    BufferBoundarySupplierSubscriber(Subscriber<? super U> param1Subscriber, Callable<U> param1Callable, Callable<? extends Publisher<B>> param1Callable1) {
      super(param1Subscriber, (SimplePlainQueue)new MpscLinkedQueue());
      this.bufferSupplier = param1Callable;
      this.boundarySupplier = param1Callable1;
    }
    
    public boolean accept(Subscriber<? super U> param1Subscriber, U param1U) {
      this.downstream.onNext(param1U);
      return true;
    }
    
    public void cancel() {
      if (!this.cancelled) {
        this.cancelled = true;
        this.upstream.cancel();
        disposeOther();
        if (enter())
          this.queue.clear(); 
      } 
    }
    
    public void dispose() {
      this.upstream.cancel();
      disposeOther();
    }
    
    void disposeOther() {
      DisposableHelper.dispose(this.other);
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (this.other.get() == DisposableHelper.DISPOSED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    void next() {
      // Byte code:
      //   0: aload_0
      //   1: getfield bufferSupplier : Ljava/util/concurrent/Callable;
      //   4: invokeinterface call : ()Ljava/lang/Object;
      //   9: ldc 'The buffer supplied is null'
      //   11: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
      //   14: checkcast java/util/Collection
      //   17: astore_1
      //   18: aload_0
      //   19: getfield boundarySupplier : Ljava/util/concurrent/Callable;
      //   22: invokeinterface call : ()Ljava/lang/Object;
      //   27: ldc 'The boundary publisher supplied is null'
      //   29: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
      //   32: checkcast org/reactivestreams/Publisher
      //   35: astore_2
      //   36: new io/reactivex/internal/operators/flowable/FlowableBufferBoundarySupplier$BufferBoundarySubscriber
      //   39: dup
      //   40: aload_0
      //   41: invokespecial <init> : (Lio/reactivex/internal/operators/flowable/FlowableBufferBoundarySupplier$BufferBoundarySupplierSubscriber;)V
      //   44: astore_3
      //   45: aload_0
      //   46: getfield other : Ljava/util/concurrent/atomic/AtomicReference;
      //   49: aload_3
      //   50: invokestatic replace : (Ljava/util/concurrent/atomic/AtomicReference;Lio/reactivex/disposables/Disposable;)Z
      //   53: ifeq -> 102
      //   56: aload_0
      //   57: monitorenter
      //   58: aload_0
      //   59: getfield buffer : Ljava/util/Collection;
      //   62: astore #4
      //   64: aload #4
      //   66: ifnonnull -> 72
      //   69: aload_0
      //   70: monitorexit
      //   71: return
      //   72: aload_0
      //   73: aload_1
      //   74: putfield buffer : Ljava/util/Collection;
      //   77: aload_0
      //   78: monitorexit
      //   79: aload_2
      //   80: aload_3
      //   81: invokeinterface subscribe : (Lorg/reactivestreams/Subscriber;)V
      //   86: aload_0
      //   87: aload #4
      //   89: iconst_0
      //   90: aload_0
      //   91: invokevirtual fastPathEmitMax : (Ljava/lang/Object;ZLio/reactivex/disposables/Disposable;)V
      //   94: goto -> 102
      //   97: astore_2
      //   98: aload_0
      //   99: monitorexit
      //   100: aload_2
      //   101: athrow
      //   102: return
      //   103: astore_2
      //   104: aload_2
      //   105: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
      //   108: aload_0
      //   109: iconst_1
      //   110: putfield cancelled : Z
      //   113: aload_0
      //   114: getfield upstream : Lorg/reactivestreams/Subscription;
      //   117: invokeinterface cancel : ()V
      //   122: aload_0
      //   123: getfield downstream : Lorg/reactivestreams/Subscriber;
      //   126: aload_2
      //   127: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   132: return
      //   133: astore_2
      //   134: aload_2
      //   135: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
      //   138: aload_0
      //   139: invokevirtual cancel : ()V
      //   142: aload_0
      //   143: getfield downstream : Lorg/reactivestreams/Subscriber;
      //   146: aload_2
      //   147: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   152: return
      // Exception table:
      //   from	to	target	type
      //   0	18	133	finally
      //   18	36	103	finally
      //   58	64	97	finally
      //   69	71	97	finally
      //   72	79	97	finally
      //   98	100	97	finally
    }
    
    public void onComplete() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield buffer : Ljava/util/Collection;
      //   6: astore_1
      //   7: aload_1
      //   8: ifnonnull -> 14
      //   11: aload_0
      //   12: monitorexit
      //   13: return
      //   14: aload_0
      //   15: aconst_null
      //   16: putfield buffer : Ljava/util/Collection;
      //   19: aload_0
      //   20: monitorexit
      //   21: aload_0
      //   22: getfield queue : Lio/reactivex/internal/fuseable/SimplePlainQueue;
      //   25: aload_1
      //   26: invokeinterface offer : (Ljava/lang/Object;)Z
      //   31: pop
      //   32: aload_0
      //   33: iconst_1
      //   34: putfield done : Z
      //   37: aload_0
      //   38: invokevirtual enter : ()Z
      //   41: ifeq -> 58
      //   44: aload_0
      //   45: getfield queue : Lio/reactivex/internal/fuseable/SimplePlainQueue;
      //   48: aload_0
      //   49: getfield downstream : Lorg/reactivestreams/Subscriber;
      //   52: iconst_0
      //   53: aload_0
      //   54: aload_0
      //   55: invokestatic drainMaxLoop : (Lio/reactivex/internal/fuseable/SimplePlainQueue;Lorg/reactivestreams/Subscriber;ZLio/reactivex/disposables/Disposable;Lio/reactivex/internal/util/QueueDrain;)V
      //   58: return
      //   59: astore_1
      //   60: aload_0
      //   61: monitorexit
      //   62: aload_1
      //   63: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	59	finally
      //   11	13	59	finally
      //   14	21	59	finally
      //   60	62	59	finally
    }
    
    public void onError(Throwable param1Throwable) {
      cancel();
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield buffer : Ljava/util/Collection;
      //   6: astore_2
      //   7: aload_2
      //   8: ifnonnull -> 14
      //   11: aload_0
      //   12: monitorexit
      //   13: return
      //   14: aload_2
      //   15: aload_1
      //   16: invokeinterface add : (Ljava/lang/Object;)Z
      //   21: pop
      //   22: aload_0
      //   23: monitorexit
      //   24: return
      //   25: astore_1
      //   26: aload_0
      //   27: monitorexit
      //   28: aload_1
      //   29: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	25	finally
      //   11	13	25	finally
      //   14	24	25	finally
      //   26	28	25	finally
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (!SubscriptionHelper.validate(this.upstream, param1Subscription))
        return; 
      this.upstream = param1Subscription;
      Subscriber subscriber = this.downstream;
      try {
      
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        this.cancelled = true;
        param1Subscription.cancel();
        EmptySubscription.error(exception, subscriber);
      } 
    }
    
    public void request(long param1Long) {
      requested(param1Long);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableBufferBoundarySupplier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */