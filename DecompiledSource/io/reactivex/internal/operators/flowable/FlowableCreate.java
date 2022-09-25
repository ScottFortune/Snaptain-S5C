package io.reactivex.internal.operators.flowable;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.Cancellable;
import io.reactivex.internal.disposables.CancellableDisposable;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableCreate<T> extends Flowable<T> {
  final BackpressureStrategy backpressure;
  
  final FlowableOnSubscribe<T> source;
  
  public FlowableCreate(FlowableOnSubscribe<T> paramFlowableOnSubscribe, BackpressureStrategy paramBackpressureStrategy) {
    this.source = paramFlowableOnSubscribe;
    this.backpressure = paramBackpressureStrategy;
  }
  
  public void subscribeActual(Subscriber<? super T> paramSubscriber) {
    MissingEmitter<T> missingEmitter;
    int i = null.$SwitchMap$io$reactivex$BackpressureStrategy[this.backpressure.ordinal()];
    if (i != 1) {
      if (i != 2) {
        if (i != 3) {
          if (i != 4) {
            BufferAsyncEmitter<T> bufferAsyncEmitter = new BufferAsyncEmitter<T>(paramSubscriber, bufferSize());
          } else {
            LatestAsyncEmitter<T> latestAsyncEmitter = new LatestAsyncEmitter<T>(paramSubscriber);
          } 
        } else {
          DropAsyncEmitter<T> dropAsyncEmitter = new DropAsyncEmitter<T>(paramSubscriber);
        } 
      } else {
        ErrorAsyncEmitter<T> errorAsyncEmitter = new ErrorAsyncEmitter<T>(paramSubscriber);
      } 
    } else {
      missingEmitter = new MissingEmitter<T>(paramSubscriber);
    } 
    paramSubscriber.onSubscribe(missingEmitter);
  }
  
  static abstract class BaseEmitter<T> extends AtomicLong implements FlowableEmitter<T>, Subscription {
    private static final long serialVersionUID = 7326289992464377023L;
    
    final Subscriber<? super T> downstream;
    
    final SequentialDisposable serial;
    
    BaseEmitter(Subscriber<? super T> param1Subscriber) {
      this.downstream = param1Subscriber;
      this.serial = new SequentialDisposable();
    }
    
    public final void cancel() {
      this.serial.dispose();
      onUnsubscribed();
    }
    
    protected void complete() {
      if (isCancelled())
        return; 
      try {
        this.downstream.onComplete();
        return;
      } finally {
        this.serial.dispose();
      } 
    }
    
    protected boolean error(Throwable param1Throwable) {
      Throwable throwable = param1Throwable;
      if (param1Throwable == null)
        throwable = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources."); 
      if (isCancelled())
        return false; 
      try {
        this.downstream.onError(throwable);
        return true;
      } finally {
        this.serial.dispose();
      } 
    }
    
    public final boolean isCancelled() {
      return this.serial.isDisposed();
    }
    
    public void onComplete() {
      complete();
    }
    
    public final void onError(Throwable param1Throwable) {
      if (!tryOnError(param1Throwable))
        RxJavaPlugins.onError(param1Throwable); 
    }
    
    void onRequested() {}
    
    void onUnsubscribed() {}
    
    public final void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.add(this, param1Long);
        onRequested();
      } 
    }
    
    public final long requested() {
      return get();
    }
    
    public final FlowableEmitter<T> serialize() {
      return new FlowableCreate.SerializedEmitter<T>(this);
    }
    
    public final void setCancellable(Cancellable param1Cancellable) {
      setDisposable((Disposable)new CancellableDisposable(param1Cancellable));
    }
    
    public final void setDisposable(Disposable param1Disposable) {
      this.serial.update(param1Disposable);
    }
    
    public String toString() {
      return String.format("%s{%s}", new Object[] { getClass().getSimpleName(), super.toString() });
    }
    
    public boolean tryOnError(Throwable param1Throwable) {
      return error(param1Throwable);
    }
  }
  
  static final class BufferAsyncEmitter<T> extends BaseEmitter<T> {
    private static final long serialVersionUID = 2427151001689639875L;
    
    volatile boolean done;
    
    Throwable error;
    
    final SpscLinkedArrayQueue<T> queue;
    
    final AtomicInteger wip;
    
    BufferAsyncEmitter(Subscriber<? super T> param1Subscriber, int param1Int) {
      super(param1Subscriber);
      this.queue = new SpscLinkedArrayQueue(param1Int);
      this.wip = new AtomicInteger();
    }
    
    void drain() {
      int j;
      if (this.wip.getAndIncrement() != 0)
        return; 
      Subscriber<? super T> subscriber = this.downstream;
      SpscLinkedArrayQueue<T> spscLinkedArrayQueue = this.queue;
      int i = 1;
      do {
        Throwable throwable;
        long l1 = get();
        long l2;
        for (l2 = 0L; l2 != l1; l2++) {
          boolean bool1;
          if (isCancelled()) {
            spscLinkedArrayQueue.clear();
            return;
          } 
          boolean bool = this.done;
          Object object = spscLinkedArrayQueue.poll();
          if (object == null) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          if (bool && bool1) {
            throwable = this.error;
            if (throwable != null) {
              error(throwable);
            } else {
              complete();
            } 
            return;
          } 
          if (bool1)
            break; 
          subscriber.onNext(object);
        } 
        if (l2 == l1) {
          if (isCancelled()) {
            throwable.clear();
            return;
          } 
          boolean bool2 = this.done;
          boolean bool1 = throwable.isEmpty();
          if (bool2 && bool1) {
            throwable = this.error;
            if (throwable != null) {
              error(throwable);
            } else {
              complete();
            } 
            return;
          } 
        } 
        if (l2 != 0L)
          BackpressureHelper.produced(this, l2); 
        j = this.wip.addAndGet(-i);
        i = j;
      } while (j != 0);
    }
    
    public void onComplete() {
      this.done = true;
      drain();
    }
    
    public void onNext(T param1T) {
      if (!this.done && !isCancelled()) {
        if (param1T == null) {
          onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
          return;
        } 
        this.queue.offer(param1T);
        drain();
      } 
    }
    
    void onRequested() {
      drain();
    }
    
    void onUnsubscribed() {
      if (this.wip.getAndIncrement() == 0)
        this.queue.clear(); 
    }
    
    public boolean tryOnError(Throwable param1Throwable) {
      if (this.done || isCancelled())
        return false; 
      Throwable throwable = param1Throwable;
      if (param1Throwable == null)
        throwable = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources."); 
      this.error = throwable;
      this.done = true;
      drain();
      return true;
    }
  }
  
  static final class DropAsyncEmitter<T> extends NoOverflowBaseAsyncEmitter<T> {
    private static final long serialVersionUID = 8360058422307496563L;
    
    DropAsyncEmitter(Subscriber<? super T> param1Subscriber) {
      super(param1Subscriber);
    }
    
    void onOverflow() {}
  }
  
  static final class ErrorAsyncEmitter<T> extends NoOverflowBaseAsyncEmitter<T> {
    private static final long serialVersionUID = 338953216916120960L;
    
    ErrorAsyncEmitter(Subscriber<? super T> param1Subscriber) {
      super(param1Subscriber);
    }
    
    void onOverflow() {
      onError((Throwable)new MissingBackpressureException("create: could not emit value due to lack of requests"));
    }
  }
  
  static final class LatestAsyncEmitter<T> extends BaseEmitter<T> {
    private static final long serialVersionUID = 4023437720691792495L;
    
    volatile boolean done;
    
    Throwable error;
    
    final AtomicReference<T> queue = new AtomicReference<T>();
    
    final AtomicInteger wip = new AtomicInteger();
    
    LatestAsyncEmitter(Subscriber<? super T> param1Subscriber) {
      super(param1Subscriber);
    }
    
    void drain() {
      int j;
      if (this.wip.getAndIncrement() != 0)
        return; 
      Subscriber<? super T> subscriber = this.downstream;
      AtomicReference<T> atomicReference = this.queue;
      int i = 1;
      do {
        boolean bool;
        long l1 = get();
        long l2 = 0L;
        while (true) {
          bool = false;
          if (l2 != l1) {
            Throwable throwable;
            boolean bool2;
            if (isCancelled()) {
              atomicReference.lazySet(null);
              return;
            } 
            boolean bool1 = this.done;
            T t = atomicReference.getAndSet(null);
            if (t == null) {
              bool2 = true;
            } else {
              bool2 = false;
            } 
            if (bool1 && bool2) {
              throwable = this.error;
              if (throwable != null) {
                error(throwable);
              } else {
                complete();
              } 
              return;
            } 
            if (bool2)
              break; 
            subscriber.onNext(throwable);
            l2++;
            continue;
          } 
          break;
        } 
        if (l2 == l1) {
          if (isCancelled()) {
            atomicReference.lazySet(null);
            return;
          } 
          boolean bool1 = this.done;
          boolean bool2 = bool;
          if (atomicReference.get() == null)
            bool2 = true; 
          if (bool1 && bool2) {
            Throwable throwable = this.error;
            if (throwable != null) {
              error(throwable);
            } else {
              complete();
            } 
            return;
          } 
        } 
        if (l2 != 0L)
          BackpressureHelper.produced(this, l2); 
        j = this.wip.addAndGet(-i);
        i = j;
      } while (j != 0);
    }
    
    public void onComplete() {
      this.done = true;
      drain();
    }
    
    public void onNext(T param1T) {
      if (!this.done && !isCancelled()) {
        if (param1T == null) {
          onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
          return;
        } 
        this.queue.set(param1T);
        drain();
      } 
    }
    
    void onRequested() {
      drain();
    }
    
    void onUnsubscribed() {
      if (this.wip.getAndIncrement() == 0)
        this.queue.lazySet(null); 
    }
    
    public boolean tryOnError(Throwable param1Throwable) {
      if (this.done || isCancelled())
        return false; 
      if (param1Throwable == null)
        onError(new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.")); 
      this.error = param1Throwable;
      this.done = true;
      drain();
      return true;
    }
  }
  
  static final class MissingEmitter<T> extends BaseEmitter<T> {
    private static final long serialVersionUID = 3776720187248809713L;
    
    MissingEmitter(Subscriber<? super T> param1Subscriber) {
      super(param1Subscriber);
    }
    
    public void onNext(T param1T) {
      if (isCancelled())
        return; 
      if (param1T != null) {
        long l;
        this.downstream.onNext(param1T);
        do {
          l = get();
        } while (l != 0L && !compareAndSet(l, l - 1L));
        return;
      } 
      onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
    }
  }
  
  static abstract class NoOverflowBaseAsyncEmitter<T> extends BaseEmitter<T> {
    private static final long serialVersionUID = 4127754106204442833L;
    
    NoOverflowBaseAsyncEmitter(Subscriber<? super T> param1Subscriber) {
      super(param1Subscriber);
    }
    
    public final void onNext(T param1T) {
      if (isCancelled())
        return; 
      if (param1T == null) {
        onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
        return;
      } 
      if (get() != 0L) {
        this.downstream.onNext(param1T);
        BackpressureHelper.produced(this, 1L);
      } else {
        onOverflow();
      } 
    }
    
    abstract void onOverflow();
  }
  
  static final class SerializedEmitter<T> extends AtomicInteger implements FlowableEmitter<T> {
    private static final long serialVersionUID = 4883307006032401862L;
    
    volatile boolean done;
    
    final FlowableCreate.BaseEmitter<T> emitter;
    
    final AtomicThrowable error;
    
    final SimplePlainQueue<T> queue;
    
    SerializedEmitter(FlowableCreate.BaseEmitter<T> param1BaseEmitter) {
      this.emitter = param1BaseEmitter;
      this.error = new AtomicThrowable();
      this.queue = (SimplePlainQueue<T>)new SpscLinkedArrayQueue(16);
    }
    
    void drain() {
      if (getAndIncrement() == 0)
        drainLoop(); 
    }
    
    void drainLoop() {
      FlowableCreate.BaseEmitter<T> baseEmitter = this.emitter;
      SimplePlainQueue<T> simplePlainQueue = this.queue;
      AtomicThrowable atomicThrowable = this.error;
      int i = 1;
      while (true) {
        int j;
        if (baseEmitter.isCancelled()) {
          simplePlainQueue.clear();
          return;
        } 
        if (atomicThrowable.get() != null) {
          simplePlainQueue.clear();
          baseEmitter.onError(atomicThrowable.terminate());
          return;
        } 
        boolean bool = this.done;
        Object object = simplePlainQueue.poll();
        if (object == null) {
          j = 1;
        } else {
          j = 0;
        } 
        if (bool && j) {
          baseEmitter.onComplete();
          return;
        } 
        if (j) {
          j = addAndGet(-i);
          i = j;
          if (j == 0)
            return; 
          continue;
        } 
        baseEmitter.onNext(object);
      } 
    }
    
    public boolean isCancelled() {
      return this.emitter.isCancelled();
    }
    
    public void onComplete() {
      if (!this.emitter.isCancelled() && !this.done) {
        this.done = true;
        drain();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (!tryOnError(param1Throwable))
        RxJavaPlugins.onError(param1Throwable); 
    }
    
    public void onNext(T param1T) {
      if (this.emitter.isCancelled() || this.done)
        return; 
      if (param1T == null) {
        onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
        return;
      } 
      if (get() == 0 && compareAndSet(0, 1)) {
        this.emitter.onNext(param1T);
        if (decrementAndGet() == 0)
          return; 
      } else {
        synchronized (this.queue) {
          null.offer(param1T);
          if (getAndIncrement() != 0)
            return; 
          drainLoop();
          return;
        } 
      } 
      drainLoop();
    }
    
    public long requested() {
      return this.emitter.requested();
    }
    
    public FlowableEmitter<T> serialize() {
      return this;
    }
    
    public void setCancellable(Cancellable param1Cancellable) {
      this.emitter.setCancellable(param1Cancellable);
    }
    
    public void setDisposable(Disposable param1Disposable) {
      this.emitter.setDisposable(param1Disposable);
    }
    
    public String toString() {
      return this.emitter.toString();
    }
    
    public boolean tryOnError(Throwable param1Throwable) {
      if (!this.emitter.isCancelled() && !this.done) {
        Throwable throwable = param1Throwable;
        if (param1Throwable == null)
          throwable = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources."); 
        if (this.error.addThrowable(throwable)) {
          this.done = true;
          drain();
          return true;
        } 
      } 
      return false;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableCreate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */