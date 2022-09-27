package io.reactivex.internal.util;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class QueueDrainHelper {
  static final long COMPLETED_MASK = -9223372036854775808L;
  
  static final long REQUESTED_MASK = 9223372036854775807L;
  
  private QueueDrainHelper() {
    throw new IllegalStateException("No instances!");
  }
  
  public static <T, U> boolean checkTerminated(boolean paramBoolean1, boolean paramBoolean2, Observer<?> paramObserver, boolean paramBoolean3, SimpleQueue<?> paramSimpleQueue, Disposable paramDisposable, ObservableQueueDrain<T, U> paramObservableQueueDrain) {
    if (paramObservableQueueDrain.cancelled()) {
      paramSimpleQueue.clear();
      paramDisposable.dispose();
      return true;
    } 
    if (paramBoolean1) {
      Throwable throwable;
      if (paramBoolean3) {
        if (paramBoolean2) {
          if (paramDisposable != null)
            paramDisposable.dispose(); 
          throwable = paramObservableQueueDrain.error();
          if (throwable != null) {
            paramObserver.onError(throwable);
          } else {
            paramObserver.onComplete();
          } 
          return true;
        } 
      } else {
        Throwable throwable1 = paramObservableQueueDrain.error();
        if (throwable1 != null) {
          throwable.clear();
          if (paramDisposable != null)
            paramDisposable.dispose(); 
          paramObserver.onError(throwable1);
          return true;
        } 
        if (paramBoolean2) {
          if (paramDisposable != null)
            paramDisposable.dispose(); 
          paramObserver.onComplete();
          return true;
        } 
      } 
    } 
    return false;
  }
  
  public static <T, U> boolean checkTerminated(boolean paramBoolean1, boolean paramBoolean2, Subscriber<?> paramSubscriber, boolean paramBoolean3, SimpleQueue<?> paramSimpleQueue, QueueDrain<T, U> paramQueueDrain) {
    if (paramQueueDrain.cancelled()) {
      paramSimpleQueue.clear();
      return true;
    } 
    if (paramBoolean1) {
      Throwable throwable;
      if (paramBoolean3) {
        if (paramBoolean2) {
          throwable = paramQueueDrain.error();
          if (throwable != null) {
            paramSubscriber.onError(throwable);
          } else {
            paramSubscriber.onComplete();
          } 
          return true;
        } 
      } else {
        Throwable throwable1 = paramQueueDrain.error();
        if (throwable1 != null) {
          throwable.clear();
          paramSubscriber.onError(throwable1);
          return true;
        } 
        if (paramBoolean2) {
          paramSubscriber.onComplete();
          return true;
        } 
      } 
    } 
    return false;
  }
  
  public static <T> SimpleQueue<T> createQueue(int paramInt) {
    return (SimpleQueue<T>)((paramInt < 0) ? new SpscLinkedArrayQueue(-paramInt) : new SpscArrayQueue(paramInt));
  }
  
  public static <T, U> void drainLoop(SimplePlainQueue<T> paramSimplePlainQueue, Observer<? super U> paramObserver, boolean paramBoolean, Disposable paramDisposable, ObservableQueueDrain<T, U> paramObservableQueueDrain) {
    int i = 1;
    label19: while (true) {
      if (checkTerminated(paramObservableQueueDrain.done(), paramSimplePlainQueue.isEmpty(), paramObserver, paramBoolean, (SimpleQueue<?>)paramSimplePlainQueue, paramDisposable, paramObservableQueueDrain))
        return; 
      while (true) {
        boolean bool1;
        boolean bool = paramObservableQueueDrain.done();
        Object object = paramSimplePlainQueue.poll();
        if (object == null) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        if (checkTerminated(bool, bool1, paramObserver, paramBoolean, (SimpleQueue<?>)paramSimplePlainQueue, paramDisposable, paramObservableQueueDrain))
          return; 
        if (bool1) {
          int j = paramObservableQueueDrain.leave(-i);
          i = j;
          if (j == 0)
            return; 
          continue label19;
        } 
        paramObservableQueueDrain.accept(paramObserver, (T)object);
      } 
      break;
    } 
  }
  
  public static <T, U> void drainMaxLoop(SimplePlainQueue<T> paramSimplePlainQueue, Subscriber<? super U> paramSubscriber, boolean paramBoolean, Disposable paramDisposable, QueueDrain<T, U> paramQueueDrain) {
    int i = 1;
    while (true) {
      boolean bool1;
      boolean bool = paramQueueDrain.done();
      Object object = paramSimplePlainQueue.poll();
      if (object == null) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (checkTerminated(bool, bool1, paramSubscriber, paramBoolean, (SimpleQueue<?>)paramSimplePlainQueue, paramQueueDrain)) {
        if (paramDisposable != null)
          paramDisposable.dispose(); 
        return;
      } 
      if (bool1) {
        int j = paramQueueDrain.leave(-i);
        i = j;
        if (j == 0)
          return; 
        continue;
      } 
      long l = paramQueueDrain.requested();
      if (l != 0L) {
        if (paramQueueDrain.accept(paramSubscriber, (T)object) && l != Long.MAX_VALUE)
          paramQueueDrain.produced(1L); 
        continue;
      } 
      paramSimplePlainQueue.clear();
      if (paramDisposable != null)
        paramDisposable.dispose(); 
      paramSubscriber.onError((Throwable)new MissingBackpressureException("Could not emit value due to lack of requests."));
      return;
    } 
  }
  
  static boolean isCancelled(BooleanSupplier paramBooleanSupplier) {
    try {
      return paramBooleanSupplier.getAsBoolean();
    } finally {
      paramBooleanSupplier = null;
      Exceptions.throwIfFatal((Throwable)paramBooleanSupplier);
    } 
  }
  
  public static <T> void postComplete(Subscriber<? super T> paramSubscriber, Queue<T> paramQueue, AtomicLong paramAtomicLong, BooleanSupplier paramBooleanSupplier) {
    if (paramQueue.isEmpty()) {
      paramSubscriber.onComplete();
      return;
    } 
    if (postCompleteDrain(paramAtomicLong.get(), paramSubscriber, paramQueue, paramAtomicLong, paramBooleanSupplier))
      return; 
    while (true) {
      long l1 = paramAtomicLong.get();
      if ((l1 & Long.MIN_VALUE) != 0L)
        return; 
      long l2 = l1 | Long.MIN_VALUE;
      if (paramAtomicLong.compareAndSet(l1, l2)) {
        if (l1 != 0L)
          postCompleteDrain(l2, paramSubscriber, paramQueue, paramAtomicLong, paramBooleanSupplier); 
        return;
      } 
    } 
  }
  
  static <T> boolean postCompleteDrain(long paramLong, Subscriber<? super T> paramSubscriber, Queue<T> paramQueue, AtomicLong paramAtomicLong, BooleanSupplier paramBooleanSupplier) {
    long l = paramLong & Long.MIN_VALUE;
    while (true) {
      while (l != paramLong) {
        if (isCancelled(paramBooleanSupplier))
          return true; 
        T t = paramQueue.poll();
        if (t == null) {
          paramSubscriber.onComplete();
          return true;
        } 
        paramSubscriber.onNext(t);
        l++;
      } 
      if (isCancelled(paramBooleanSupplier))
        return true; 
      if (paramQueue.isEmpty()) {
        paramSubscriber.onComplete();
        return true;
      } 
      long l1 = paramAtomicLong.get();
      paramLong = l1;
      if (l1 == l) {
        l = paramAtomicLong.addAndGet(-(l & Long.MAX_VALUE));
        if ((Long.MAX_VALUE & l) == 0L)
          return false; 
        paramLong = l;
        l &= Long.MIN_VALUE;
      } 
    } 
  }
  
  public static <T> boolean postCompleteRequest(long paramLong, Subscriber<? super T> paramSubscriber, Queue<T> paramQueue, AtomicLong paramAtomicLong, BooleanSupplier paramBooleanSupplier) {
    while (true) {
      long l = paramAtomicLong.get();
      if (paramAtomicLong.compareAndSet(l, BackpressureHelper.addCap(Long.MAX_VALUE & l, paramLong) | l & Long.MIN_VALUE)) {
        if (l == Long.MIN_VALUE) {
          postCompleteDrain(paramLong | Long.MIN_VALUE, paramSubscriber, paramQueue, paramAtomicLong, paramBooleanSupplier);
          return true;
        } 
        return false;
      } 
    } 
  }
  
  public static void request(Subscription paramSubscription, int paramInt) {
    long l;
    if (paramInt < 0) {
      l = Long.MAX_VALUE;
    } else {
      l = paramInt;
    } 
    paramSubscription.request(l);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/util/QueueDrainHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */