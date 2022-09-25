package io.reactivex.internal.operators.parallel;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelSortedJoin<T> extends Flowable<T> {
  final Comparator<? super T> comparator;
  
  final ParallelFlowable<List<T>> source;
  
  public ParallelSortedJoin(ParallelFlowable<List<T>> paramParallelFlowable, Comparator<? super T> paramComparator) {
    this.source = paramParallelFlowable;
    this.comparator = paramComparator;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    SortedJoinSubscription<T> sortedJoinSubscription = new SortedJoinSubscription<T>(paramSubscriber, this.source.parallelism(), this.comparator);
    paramSubscriber.onSubscribe(sortedJoinSubscription);
    this.source.subscribe((Subscriber[])sortedJoinSubscription.subscribers);
  }
  
  static final class SortedJoinInnerSubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<List<T>> {
    private static final long serialVersionUID = 6751017204873808094L;
    
    final int index;
    
    final ParallelSortedJoin.SortedJoinSubscription<T> parent;
    
    SortedJoinInnerSubscriber(ParallelSortedJoin.SortedJoinSubscription<T> param1SortedJoinSubscription, int param1Int) {
      this.parent = param1SortedJoinSubscription;
      this.index = param1Int;
    }
    
    void cancel() {
      SubscriptionHelper.cancel(this);
    }
    
    public void onComplete() {}
    
    public void onError(Throwable param1Throwable) {
      this.parent.innerError(param1Throwable);
    }
    
    public void onNext(List<T> param1List) {
      this.parent.innerNext(param1List, this.index);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.setOnce(this, param1Subscription, Long.MAX_VALUE);
    }
  }
  
  static final class SortedJoinSubscription<T> extends AtomicInteger implements Subscription {
    private static final long serialVersionUID = 3481980673745556697L;
    
    volatile boolean cancelled;
    
    final Comparator<? super T> comparator;
    
    final Subscriber<? super T> downstream;
    
    final AtomicReference<Throwable> error = new AtomicReference<Throwable>();
    
    final int[] indexes;
    
    final List<T>[] lists;
    
    final AtomicInteger remaining = new AtomicInteger();
    
    final AtomicLong requested = new AtomicLong();
    
    final ParallelSortedJoin.SortedJoinInnerSubscriber<T>[] subscribers;
    
    SortedJoinSubscription(Subscriber<? super T> param1Subscriber, int param1Int, Comparator<? super T> param1Comparator) {
      this.downstream = param1Subscriber;
      this.comparator = param1Comparator;
      ParallelSortedJoin.SortedJoinInnerSubscriber[] arrayOfSortedJoinInnerSubscriber = new ParallelSortedJoin.SortedJoinInnerSubscriber[param1Int];
      for (byte b = 0; b < param1Int; b++)
        arrayOfSortedJoinInnerSubscriber[b] = new ParallelSortedJoin.SortedJoinInnerSubscriber(this, b); 
      this.subscribers = (ParallelSortedJoin.SortedJoinInnerSubscriber<T>[])arrayOfSortedJoinInnerSubscriber;
      this.lists = (List<T>[])new List[param1Int];
      this.indexes = new int[param1Int];
      this.remaining.lazySet(param1Int);
    }
    
    public void cancel() {
      if (!this.cancelled) {
        this.cancelled = true;
        cancelAll();
        if (getAndIncrement() == 0)
          Arrays.fill((Object[])this.lists, (Object)null); 
      } 
    }
    
    void cancelAll() {
      ParallelSortedJoin.SortedJoinInnerSubscriber<T>[] arrayOfSortedJoinInnerSubscriber = this.subscribers;
      int i = arrayOfSortedJoinInnerSubscriber.length;
      for (byte b = 0; b < i; b++)
        arrayOfSortedJoinInnerSubscriber[b].cancel(); 
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      Subscriber<? super T> subscriber = this.downstream;
      List<T>[] arrayOfList = this.lists;
      int[] arrayOfInt = this.indexes;
      int i = arrayOfInt.length;
      int j;
      for (j = 1;; j = k) {
        long l1 = this.requested.get();
        long l2;
        for (l2 = 0L; l2 != l1; l2++) {
          Object object;
          if (this.cancelled) {
            Arrays.fill((Object[])arrayOfList, (Object)null);
            return;
          } 
          Throwable throwable1 = this.error.get();
          if (throwable1 != null) {
            cancelAll();
            Arrays.fill((Object[])arrayOfList, (Object)null);
            subscriber.onError(throwable1);
            return;
          } 
          Throwable throwable2 = null;
          byte b = 0;
          byte b1 = -1;
          while (true) {
            if (b < i) {
              List<T> list = arrayOfList[b];
              int n = arrayOfInt[b];
              throwable1 = throwable2;
              Object object1 = object;
              if (list.size() != n) {
                if (throwable2 == null) {
                  throwable1 = (Throwable)list.get(n);
                } else {
                  list = (List<T>)list.get(n);
                  try {
                  
                  } finally {
                    throwable1 = null;
                    Exceptions.throwIfFatal(throwable1);
                    cancelAll();
                    Arrays.fill((Object[])arrayOfList, (Object)null);
                    if (!this.error.compareAndSet(null, throwable1))
                      RxJavaPlugins.onError(throwable1); 
                    subscriber.onError(this.error.get());
                  } 
                  continue;
                } 
              } else {
                continue;
              } 
            } else {
              break;
            } 
            byte b2 = b;
            b++;
            throwable2 = throwable1;
            object = SYNTHETIC_LOCAL_VARIABLE_16;
          } 
          if (throwable2 == null) {
            Arrays.fill((Object[])arrayOfList, (Object)null);
            subscriber.onComplete();
            return;
          } 
          subscriber.onNext(throwable2);
          arrayOfInt[object] = arrayOfInt[object] + 1;
        } 
        if (l2 == l1) {
          if (this.cancelled) {
            Arrays.fill((Object[])arrayOfList, (Object)null);
            return;
          } 
          Throwable throwable = this.error.get();
          if (throwable != null) {
            cancelAll();
            Arrays.fill((Object[])arrayOfList, (Object)null);
            subscriber.onError(throwable);
            return;
          } 
          byte b = 0;
          while (true) {
            if (b < i) {
              if (arrayOfInt[b] != arrayOfList[b].size()) {
                b = 0;
                break;
              } 
              b++;
              continue;
            } 
            b = 1;
            break;
          } 
          if (b != 0) {
            Arrays.fill((Object[])arrayOfList, (Object)null);
            subscriber.onComplete();
            return;
          } 
        } 
        if (l2 != 0L && l1 != Long.MAX_VALUE)
          this.requested.addAndGet(-l2); 
        int m = get();
        int k = m;
        if (m == j) {
          j = addAndGet(-j);
          k = j;
          if (j == 0)
            return; 
        } 
      } 
    }
    
    void innerError(Throwable param1Throwable) {
      if (this.error.compareAndSet(null, param1Throwable)) {
        drain();
      } else if (param1Throwable != this.error.get()) {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    void innerNext(List<T> param1List, int param1Int) {
      this.lists[param1Int] = param1List;
      if (this.remaining.decrementAndGet() == 0)
        drain(); 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.add(this.requested, param1Long);
        if (this.remaining.get() == 0)
          drain(); 
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/parallel/ParallelSortedJoin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */