package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BlockingHelper;
import io.reactivex.internal.util.ExceptionHelper;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.reactivestreams.Subscription;

public final class BlockingFlowableIterable<T> implements Iterable<T> {
  final int bufferSize;
  
  final Flowable<T> source;
  
  public BlockingFlowableIterable(Flowable<T> paramFlowable, int paramInt) {
    this.source = paramFlowable;
    this.bufferSize = paramInt;
  }
  
  public Iterator<T> iterator() {
    BlockingFlowableIterator<T> blockingFlowableIterator = new BlockingFlowableIterator(this.bufferSize);
    this.source.subscribe(blockingFlowableIterator);
    return blockingFlowableIterator;
  }
  
  static final class BlockingFlowableIterator<T> extends AtomicReference<Subscription> implements FlowableSubscriber<T>, Iterator<T>, Runnable, Disposable {
    private static final long serialVersionUID = 6695226475494099826L;
    
    final long batchSize;
    
    final Condition condition;
    
    volatile boolean done;
    
    volatile Throwable error;
    
    final long limit;
    
    final Lock lock;
    
    long produced;
    
    final SpscArrayQueue<T> queue;
    
    BlockingFlowableIterator(int param1Int) {
      this.queue = new SpscArrayQueue(param1Int);
      this.batchSize = param1Int;
      this.limit = (param1Int - (param1Int >> 2));
      this.lock = new ReentrantLock();
      this.condition = this.lock.newCondition();
    }
    
    public void dispose() {
      SubscriptionHelper.cancel(this);
      signalConsumer();
    }
    
    public boolean hasNext() {
      while (true) {
        if (isDisposed()) {
          Throwable throwable = this.error;
          if (throwable == null)
            return false; 
          throw ExceptionHelper.wrapOrThrow(throwable);
        } 
        boolean bool1 = this.done;
        boolean bool2 = this.queue.isEmpty();
        if (bool1) {
          Throwable throwable = this.error;
          if (throwable == null) {
            if (bool2)
              return false; 
          } else {
            throw ExceptionHelper.wrapOrThrow(throwable);
          } 
        } 
        if (bool2) {
          Exception exception;
          BlockingHelper.verifyNonBlocking();
          this.lock.lock();
          try {
            while (!this.done && this.queue.isEmpty() && !isDisposed())
              this.condition.await(); 
            this.lock.unlock();
            continue;
          } catch (InterruptedException null) {
            run();
            throw ExceptionHelper.wrapOrThrow(exception);
          } finally {}
          this.lock.unlock();
          throw exception;
        } 
        return true;
      } 
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
    
    public T next() {
      if (hasNext()) {
        Object object = this.queue.poll();
        long l = this.produced + 1L;
        if (l == this.limit) {
          this.produced = 0L;
          get().request(l);
        } else {
          this.produced = l;
        } 
        return (T)object;
      } 
      throw new NoSuchElementException();
    }
    
    public void onComplete() {
      this.done = true;
      signalConsumer();
    }
    
    public void onError(Throwable param1Throwable) {
      this.error = param1Throwable;
      this.done = true;
      signalConsumer();
    }
    
    public void onNext(T param1T) {
      if (!this.queue.offer(param1T)) {
        SubscriptionHelper.cancel(this);
        onError((Throwable)new MissingBackpressureException("Queue full?!"));
      } else {
        signalConsumer();
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.setOnce(this, param1Subscription, this.batchSize);
    }
    
    public void remove() {
      throw new UnsupportedOperationException("remove");
    }
    
    public void run() {
      SubscriptionHelper.cancel(this);
      signalConsumer();
    }
    
    void signalConsumer() {
      this.lock.lock();
      try {
        this.condition.signalAll();
        return;
      } finally {
        this.lock.unlock();
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/BlockingFlowableIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */