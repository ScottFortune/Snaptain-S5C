package io.reactivex.internal.operators.mixed;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.ErrorMode;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableConcatMapSingle<T, R> extends Flowable<R> {
  final ErrorMode errorMode;
  
  final Function<? super T, ? extends SingleSource<? extends R>> mapper;
  
  final int prefetch;
  
  final Flowable<T> source;
  
  public FlowableConcatMapSingle(Flowable<T> paramFlowable, Function<? super T, ? extends SingleSource<? extends R>> paramFunction, ErrorMode paramErrorMode, int paramInt) {
    this.source = paramFlowable;
    this.mapper = paramFunction;
    this.errorMode = paramErrorMode;
    this.prefetch = paramInt;
  }
  
  protected void subscribeActual(Subscriber<? super R> paramSubscriber) {
    this.source.subscribe(new ConcatMapSingleSubscriber<T, R>(paramSubscriber, this.mapper, this.prefetch, this.errorMode));
  }
  
  static final class ConcatMapSingleSubscriber<T, R> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
    static final int STATE_ACTIVE = 1;
    
    static final int STATE_INACTIVE = 0;
    
    static final int STATE_RESULT_VALUE = 2;
    
    private static final long serialVersionUID = -9140123220065488293L;
    
    volatile boolean cancelled;
    
    int consumed;
    
    volatile boolean done;
    
    final Subscriber<? super R> downstream;
    
    long emitted;
    
    final ErrorMode errorMode;
    
    final AtomicThrowable errors;
    
    final ConcatMapSingleObserver<R> inner;
    
    R item;
    
    final Function<? super T, ? extends SingleSource<? extends R>> mapper;
    
    final int prefetch;
    
    final SimplePlainQueue<T> queue;
    
    final AtomicLong requested;
    
    volatile int state;
    
    Subscription upstream;
    
    ConcatMapSingleSubscriber(Subscriber<? super R> param1Subscriber, Function<? super T, ? extends SingleSource<? extends R>> param1Function, int param1Int, ErrorMode param1ErrorMode) {
      this.downstream = param1Subscriber;
      this.mapper = param1Function;
      this.prefetch = param1Int;
      this.errorMode = param1ErrorMode;
      this.requested = new AtomicLong();
      this.errors = new AtomicThrowable();
      this.inner = new ConcatMapSingleObserver<R>(this);
      this.queue = (SimplePlainQueue<T>)new SpscArrayQueue(param1Int);
    }
    
    public void cancel() {
      this.cancelled = true;
      this.upstream.cancel();
      this.inner.dispose();
      if (getAndIncrement() == 0) {
        this.queue.clear();
        this.item = null;
      } 
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      Subscriber<? super R> subscriber = this.downstream;
      ErrorMode errorMode = this.errorMode;
      SimplePlainQueue<T> simplePlainQueue = this.queue;
      AtomicThrowable atomicThrowable = this.errors;
      AtomicLong atomicLong = this.requested;
      int i = this.prefetch;
      int j = i - (i >> 1);
      i = 1;
      while (true) {
        if (this.cancelled) {
          simplePlainQueue.clear();
          this.item = null;
        } else {
          int m = this.state;
          if (atomicThrowable.get() != null && (errorMode == ErrorMode.IMMEDIATE || (errorMode == ErrorMode.BOUNDARY && m == 0))) {
            simplePlainQueue.clear();
            this.item = null;
            subscriber.onError(atomicThrowable.terminate());
            return;
          } 
          if (m == 0) {
            Throwable throwable;
            boolean bool = this.done;
            Object object = simplePlainQueue.poll();
            if (object == null) {
              m = 1;
            } else {
              m = 0;
            } 
            if (bool && m != 0) {
              throwable = atomicThrowable.terminate();
              if (throwable == null) {
                subscriber.onComplete();
              } else {
                subscriber.onError(throwable);
              } 
              return;
            } 
            if (m == 0) {
              m = this.consumed + 1;
              if (m == j) {
                this.consumed = 0;
                this.upstream.request(j);
              } else {
                this.consumed = m;
              } 
              try {
              
              } finally {
                atomicLong = null;
                Exceptions.throwIfFatal((Throwable)atomicLong);
                this.upstream.cancel();
                throwable.clear();
                atomicThrowable.addThrowable((Throwable)atomicLong);
                subscriber.onError(atomicThrowable.terminate());
              } 
            } 
          } else if (m == 2) {
            long l = this.emitted;
            if (l != atomicLong.get()) {
              R r = this.item;
              this.item = null;
              subscriber.onNext(r);
              this.emitted = l + 1L;
              this.state = 0;
              continue;
            } 
          } 
        } 
        int k = addAndGet(-i);
        i = k;
        if (k == 0)
          break; 
      } 
    }
    
    void innerError(Throwable param1Throwable) {
      if (this.errors.addThrowable(param1Throwable)) {
        if (this.errorMode != ErrorMode.END)
          this.upstream.cancel(); 
        this.state = 0;
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    void innerSuccess(R param1R) {
      this.item = param1R;
      this.state = 2;
      drain();
    }
    
    public void onComplete() {
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.errors.addThrowable(param1Throwable)) {
        if (this.errorMode == ErrorMode.IMMEDIATE)
          this.inner.dispose(); 
        this.done = true;
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      if (!this.queue.offer(param1T)) {
        this.upstream.cancel();
        onError((Throwable)new MissingBackpressureException("queue full?!"));
        return;
      } 
      drain();
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        param1Subscription.request(this.prefetch);
      } 
    }
    
    public void request(long param1Long) {
      BackpressureHelper.add(this.requested, param1Long);
      drain();
    }
    
    static final class ConcatMapSingleObserver<R> extends AtomicReference<Disposable> implements SingleObserver<R> {
      private static final long serialVersionUID = -3051469169682093892L;
      
      final FlowableConcatMapSingle.ConcatMapSingleSubscriber<?, R> parent;
      
      ConcatMapSingleObserver(FlowableConcatMapSingle.ConcatMapSingleSubscriber<?, R> param2ConcatMapSingleSubscriber) {
        this.parent = param2ConcatMapSingleSubscriber;
      }
      
      void dispose() {
        DisposableHelper.dispose(this);
      }
      
      public void onError(Throwable param2Throwable) {
        this.parent.innerError(param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.replace(this, param2Disposable);
      }
      
      public void onSuccess(R param2R) {
        this.parent.innerSuccess(param2R);
      }
    }
  }
  
  static final class ConcatMapSingleObserver<R> extends AtomicReference<Disposable> implements SingleObserver<R> {
    private static final long serialVersionUID = -3051469169682093892L;
    
    final FlowableConcatMapSingle.ConcatMapSingleSubscriber<?, R> parent;
    
    ConcatMapSingleObserver(FlowableConcatMapSingle.ConcatMapSingleSubscriber<?, R> param1ConcatMapSingleSubscriber) {
      this.parent = param1ConcatMapSingleSubscriber;
    }
    
    void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.innerError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.replace(this, param1Disposable);
    }
    
    public void onSuccess(R param1R) {
      this.parent.innerSuccess(param1R);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/mixed/FlowableConcatMapSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */