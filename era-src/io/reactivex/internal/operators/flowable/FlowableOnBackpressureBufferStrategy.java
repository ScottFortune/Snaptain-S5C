package io.reactivex.internal.operators.flowable;

import io.reactivex.BackpressureOverflowStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.functions.Action;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableOnBackpressureBufferStrategy<T> extends AbstractFlowableWithUpstream<T, T> {
  final long bufferSize;
  
  final Action onOverflow;
  
  final BackpressureOverflowStrategy strategy;
  
  public FlowableOnBackpressureBufferStrategy(Flowable<T> paramFlowable, long paramLong, Action paramAction, BackpressureOverflowStrategy paramBackpressureOverflowStrategy) {
    super(paramFlowable);
    this.bufferSize = paramLong;
    this.onOverflow = paramAction;
    this.strategy = paramBackpressureOverflowStrategy;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new OnBackpressureBufferStrategySubscriber<T>(paramSubscriber, this.onOverflow, this.strategy, this.bufferSize));
  }
  
  static final class OnBackpressureBufferStrategySubscriber<T> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = 3240706908776709697L;
    
    final long bufferSize;
    
    volatile boolean cancelled;
    
    final Deque<T> deque;
    
    volatile boolean done;
    
    final Subscriber<? super T> downstream;
    
    Throwable error;
    
    final Action onOverflow;
    
    final AtomicLong requested;
    
    final BackpressureOverflowStrategy strategy;
    
    Subscription upstream;
    
    OnBackpressureBufferStrategySubscriber(Subscriber<? super T> param1Subscriber, Action param1Action, BackpressureOverflowStrategy param1BackpressureOverflowStrategy, long param1Long) {
      this.downstream = param1Subscriber;
      this.onOverflow = param1Action;
      this.strategy = param1BackpressureOverflowStrategy;
      this.bufferSize = param1Long;
      this.requested = new AtomicLong();
      this.deque = new ArrayDeque<T>();
    }
    
    public void cancel() {
      this.cancelled = true;
      this.upstream.cancel();
      if (getAndIncrement() == 0)
        clear(this.deque); 
    }
    
    void clear(Deque<T> param1Deque) {
      // Byte code:
      //   0: aload_1
      //   1: monitorenter
      //   2: aload_1
      //   3: invokeinterface clear : ()V
      //   8: aload_1
      //   9: monitorexit
      //   10: return
      //   11: astore_2
      //   12: aload_1
      //   13: monitorexit
      //   14: aload_2
      //   15: athrow
      // Exception table:
      //   from	to	target	type
      //   2	10	11	finally
      //   12	14	11	finally
    }
    
    void drain() {
      int j;
      if (getAndIncrement() != 0)
        return; 
      Deque<T> deque = this.deque;
      Subscriber<? super T> subscriber = this.downstream;
      int i = 1;
      do {
        long l1 = this.requested.get();
        long l2 = 0L;
        while (l2 != l1) {
          if (this.cancelled) {
            clear(deque);
            return;
          } 
          synchronized (this.done) {
            boolean bool;
            T t = deque.poll();
            /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/util/Deque<GenericType{T}>}, name=null} */
            if (t == null) {
              bool = true;
            } else {
              bool = false;
            } 
            if (null) {
              Throwable throwable = this.error;
              if (throwable != null) {
                clear(deque);
                subscriber.onError(throwable);
                return;
              } 
              if (bool) {
                subscriber.onComplete();
                return;
              } 
            } 
            if (bool)
              break; 
            subscriber.onNext(t);
            l2++;
          } 
        } 
        if (l2 == l1) {
          if (this.cancelled) {
            clear(deque);
            return;
          } 
          synchronized (this.done) {
            boolean bool = deque.isEmpty();
            /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/util/Deque<GenericType{T}>}, name=null} */
            if (null) {
              Throwable throwable = this.error;
              if (throwable != null) {
                clear(deque);
                subscriber.onError(throwable);
                return;
              } 
              if (bool) {
                subscriber.onComplete();
                return;
              } 
            } 
          } 
        } 
        if (l2 != 0L)
          BackpressureHelper.produced(this.requested, l2); 
        j = addAndGet(-i);
        i = j;
      } while (j != 0);
    }
    
    public void onComplete() {
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.error = param1Throwable;
      this.done = true;
      drain();
    }
    
    public void onNext(T param1T) {
      // Byte code:
      //   0: aload_0
      //   1: getfield done : Z
      //   4: ifeq -> 8
      //   7: return
      //   8: aload_0
      //   9: getfield deque : Ljava/util/Deque;
      //   12: astore_2
      //   13: aload_2
      //   14: monitorenter
      //   15: aload_2
      //   16: invokeinterface size : ()I
      //   21: i2l
      //   22: lstore_3
      //   23: aload_0
      //   24: getfield bufferSize : J
      //   27: lstore #5
      //   29: iconst_0
      //   30: istore #7
      //   32: iconst_0
      //   33: istore #8
      //   35: iconst_1
      //   36: istore #9
      //   38: lload_3
      //   39: lload #5
      //   41: lcmp
      //   42: ifne -> 112
      //   45: getstatic io/reactivex/internal/operators/flowable/FlowableOnBackpressureBufferStrategy$1.$SwitchMap$io$reactivex$BackpressureOverflowStrategy : [I
      //   48: aload_0
      //   49: getfield strategy : Lio/reactivex/BackpressureOverflowStrategy;
      //   52: invokevirtual ordinal : ()I
      //   55: iaload
      //   56: istore #8
      //   58: iload #8
      //   60: iconst_1
      //   61: if_icmpeq -> 91
      //   64: iload #8
      //   66: iconst_2
      //   67: if_icmpeq -> 73
      //   70: goto -> 127
      //   73: aload_2
      //   74: invokeinterface poll : ()Ljava/lang/Object;
      //   79: pop
      //   80: aload_2
      //   81: aload_1
      //   82: invokeinterface offer : (Ljava/lang/Object;)Z
      //   87: pop
      //   88: goto -> 106
      //   91: aload_2
      //   92: invokeinterface pollLast : ()Ljava/lang/Object;
      //   97: pop
      //   98: aload_2
      //   99: aload_1
      //   100: invokeinterface offer : (Ljava/lang/Object;)Z
      //   105: pop
      //   106: iconst_1
      //   107: istore #7
      //   109: goto -> 124
      //   112: aload_2
      //   113: aload_1
      //   114: invokeinterface offer : (Ljava/lang/Object;)Z
      //   119: pop
      //   120: iload #8
      //   122: istore #7
      //   124: iconst_0
      //   125: istore #9
      //   127: aload_2
      //   128: monitorexit
      //   129: iload #7
      //   131: ifeq -> 174
      //   134: aload_0
      //   135: getfield onOverflow : Lio/reactivex/functions/Action;
      //   138: astore_1
      //   139: aload_1
      //   140: ifnull -> 206
      //   143: aload_1
      //   144: invokeinterface run : ()V
      //   149: goto -> 206
      //   152: astore_1
      //   153: aload_1
      //   154: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
      //   157: aload_0
      //   158: getfield upstream : Lorg/reactivestreams/Subscription;
      //   161: invokeinterface cancel : ()V
      //   166: aload_0
      //   167: aload_1
      //   168: invokevirtual onError : (Ljava/lang/Throwable;)V
      //   171: goto -> 206
      //   174: iload #9
      //   176: ifeq -> 202
      //   179: aload_0
      //   180: getfield upstream : Lorg/reactivestreams/Subscription;
      //   183: invokeinterface cancel : ()V
      //   188: aload_0
      //   189: new io/reactivex/exceptions/MissingBackpressureException
      //   192: dup
      //   193: invokespecial <init> : ()V
      //   196: invokevirtual onError : (Ljava/lang/Throwable;)V
      //   199: goto -> 206
      //   202: aload_0
      //   203: invokevirtual drain : ()V
      //   206: return
      //   207: astore_1
      //   208: aload_2
      //   209: monitorexit
      //   210: aload_1
      //   211: athrow
      // Exception table:
      //   from	to	target	type
      //   15	29	207	finally
      //   45	58	207	finally
      //   73	88	207	finally
      //   91	106	207	finally
      //   112	120	207	finally
      //   127	129	207	finally
      //   143	149	152	finally
      //   208	210	207	finally
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        param1Subscription.request(Long.MAX_VALUE);
      } 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.add(this.requested, param1Long);
        drain();
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableOnBackpressureBufferStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */