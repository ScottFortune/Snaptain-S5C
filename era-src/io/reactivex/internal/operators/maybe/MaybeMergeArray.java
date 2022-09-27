package io.reactivex.internal.operators.maybe;

import io.reactivex.Flowable;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReferenceArray;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class MaybeMergeArray<T> extends Flowable<T> {
  final MaybeSource<? extends T>[] sources;
  
  public MaybeMergeArray(MaybeSource<? extends T>[] paramArrayOfMaybeSource) {
    this.sources = paramArrayOfMaybeSource;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    ClqSimpleQueue clqSimpleQueue;
    MaybeSource<? extends T>[] arrayOfMaybeSource = this.sources;
    int i = arrayOfMaybeSource.length;
    if (i <= bufferSize()) {
      MpscFillOnceSimpleQueue mpscFillOnceSimpleQueue = new MpscFillOnceSimpleQueue(i);
    } else {
      clqSimpleQueue = new ClqSimpleQueue();
    } 
    MergeMaybeObserver<T> mergeMaybeObserver = new MergeMaybeObserver<T>(paramSubscriber, i, clqSimpleQueue);
    paramSubscriber.onSubscribe((Subscription)mergeMaybeObserver);
    AtomicThrowable atomicThrowable = mergeMaybeObserver.error;
    int j = arrayOfMaybeSource.length;
    for (i = 0; i < j; i++) {
      MaybeSource<? extends T> maybeSource = arrayOfMaybeSource[i];
      if (mergeMaybeObserver.isCancelled() || atomicThrowable.get() != null)
        break; 
      maybeSource.subscribe(mergeMaybeObserver);
    } 
  }
  
  static final class ClqSimpleQueue<T> extends ConcurrentLinkedQueue<T> implements SimpleQueueWithConsumerIndex<T> {
    private static final long serialVersionUID = -4025173261791142821L;
    
    int consumerIndex;
    
    final AtomicInteger producerIndex = new AtomicInteger();
    
    public int consumerIndex() {
      return this.consumerIndex;
    }
    
    public void drop() {
      poll();
    }
    
    public boolean offer(T param1T) {
      this.producerIndex.getAndIncrement();
      return super.offer(param1T);
    }
    
    public boolean offer(T param1T1, T param1T2) {
      throw new UnsupportedOperationException();
    }
    
    public T poll() {
      T t = super.poll();
      if (t != null)
        this.consumerIndex++; 
      return t;
    }
    
    public int producerIndex() {
      return this.producerIndex.get();
    }
  }
  
  static final class MergeMaybeObserver<T> extends BasicIntQueueSubscription<T> implements MaybeObserver<T> {
    private static final long serialVersionUID = -660395290758764731L;
    
    volatile boolean cancelled;
    
    long consumed;
    
    final Subscriber<? super T> downstream;
    
    final AtomicThrowable error;
    
    boolean outputFused;
    
    final MaybeMergeArray.SimpleQueueWithConsumerIndex<Object> queue;
    
    final AtomicLong requested;
    
    final CompositeDisposable set;
    
    final int sourceCount;
    
    MergeMaybeObserver(Subscriber<? super T> param1Subscriber, int param1Int, MaybeMergeArray.SimpleQueueWithConsumerIndex<Object> param1SimpleQueueWithConsumerIndex) {
      this.downstream = param1Subscriber;
      this.sourceCount = param1Int;
      this.set = new CompositeDisposable();
      this.requested = new AtomicLong();
      this.error = new AtomicThrowable();
      this.queue = param1SimpleQueueWithConsumerIndex;
    }
    
    public void cancel() {
      if (!this.cancelled) {
        this.cancelled = true;
        this.set.dispose();
        if (getAndIncrement() == 0)
          this.queue.clear(); 
      } 
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
      Subscriber<? super T> subscriber = this.downstream;
      MaybeMergeArray.SimpleQueueWithConsumerIndex<Object> simpleQueueWithConsumerIndex = this.queue;
      int i = 1;
      do {
        if (this.cancelled) {
          simpleQueueWithConsumerIndex.clear();
          return;
        } 
        Throwable throwable = (Throwable)this.error.get();
        if (throwable != null) {
          simpleQueueWithConsumerIndex.clear();
          subscriber.onError(throwable);
          return;
        } 
        if (simpleQueueWithConsumerIndex.producerIndex() == this.sourceCount) {
          j = 1;
        } else {
          j = 0;
        } 
        if (!simpleQueueWithConsumerIndex.isEmpty())
          subscriber.onNext(null); 
        if (j) {
          subscriber.onComplete();
          return;
        } 
        j = addAndGet(-i);
        i = j;
      } while (j != 0);
    }
    
    void drainNormal() {
      int j;
      Subscriber<? super T> subscriber = this.downstream;
      MaybeMergeArray.SimpleQueueWithConsumerIndex<Object> simpleQueueWithConsumerIndex = this.queue;
      long l = this.consumed;
      int i = 1;
      do {
        long l1 = this.requested.get();
        while (l != l1) {
          if (this.cancelled) {
            simpleQueueWithConsumerIndex.clear();
            return;
          } 
          if ((Throwable)this.error.get() != null) {
            simpleQueueWithConsumerIndex.clear();
            subscriber.onError(this.error.terminate());
            return;
          } 
          if (simpleQueueWithConsumerIndex.consumerIndex() == this.sourceCount) {
            subscriber.onComplete();
            return;
          } 
          Object object = simpleQueueWithConsumerIndex.poll();
          if (object == null)
            break; 
          if (object != NotificationLite.COMPLETE) {
            subscriber.onNext(object);
            l++;
          } 
        } 
        if (l == l1) {
          if ((Throwable)this.error.get() != null) {
            simpleQueueWithConsumerIndex.clear();
            subscriber.onError(this.error.terminate());
            return;
          } 
          while (simpleQueueWithConsumerIndex.peek() == NotificationLite.COMPLETE)
            simpleQueueWithConsumerIndex.drop(); 
          if (simpleQueueWithConsumerIndex.consumerIndex() == this.sourceCount) {
            subscriber.onComplete();
            return;
          } 
        } 
        this.consumed = l;
        j = addAndGet(-i);
        i = j;
      } while (j != 0);
    }
    
    boolean isCancelled() {
      return this.cancelled;
    }
    
    public boolean isEmpty() {
      return this.queue.isEmpty();
    }
    
    public void onComplete() {
      this.queue.offer(NotificationLite.COMPLETE);
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.error.addThrowable(param1Throwable)) {
        this.set.dispose();
        this.queue.offer(NotificationLite.COMPLETE);
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.set.add(param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      this.queue.offer(param1T);
      drain();
    }
    
    public T poll() throws Exception {
      while (true) {
        Object object = this.queue.poll();
        if (object != NotificationLite.COMPLETE)
          return (T)object; 
      } 
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
  
  static final class MpscFillOnceSimpleQueue<T> extends AtomicReferenceArray<T> implements SimpleQueueWithConsumerIndex<T> {
    private static final long serialVersionUID = -7969063454040569579L;
    
    int consumerIndex;
    
    final AtomicInteger producerIndex = new AtomicInteger();
    
    MpscFillOnceSimpleQueue(int param1Int) {
      super(param1Int);
    }
    
    public void clear() {
      while (poll() != null && !isEmpty());
    }
    
    public int consumerIndex() {
      return this.consumerIndex;
    }
    
    public void drop() {
      int i = this.consumerIndex;
      lazySet(i, null);
      this.consumerIndex = i + 1;
    }
    
    public boolean isEmpty() {
      boolean bool;
      if (this.consumerIndex == producerIndex()) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean offer(T param1T) {
      ObjectHelper.requireNonNull(param1T, "value is null");
      int i = this.producerIndex.getAndIncrement();
      if (i < length()) {
        lazySet(i, param1T);
        return true;
      } 
      return false;
    }
    
    public boolean offer(T param1T1, T param1T2) {
      throw new UnsupportedOperationException();
    }
    
    public T peek() {
      int i = this.consumerIndex;
      return (i == length()) ? null : get(i);
    }
    
    public T poll() {
      int i = this.consumerIndex;
      if (i == length())
        return null; 
      AtomicInteger atomicInteger = this.producerIndex;
      while (true) {
        T t = get(i);
        if (t != null) {
          this.consumerIndex = i + 1;
          lazySet(i, null);
          return t;
        } 
        if (atomicInteger.get() == i)
          return null; 
      } 
    }
    
    public int producerIndex() {
      return this.producerIndex.get();
    }
  }
  
  static interface SimpleQueueWithConsumerIndex<T> extends SimpleQueue<T> {
    int consumerIndex();
    
    void drop();
    
    T peek();
    
    T poll();
    
    int producerIndex();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeMergeArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */