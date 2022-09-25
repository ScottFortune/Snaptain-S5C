package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Notification;
import io.reactivex.internal.util.BlockingHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subscribers.DisposableSubscriber;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;

public final class BlockingFlowableNext<T> implements Iterable<T> {
  final Publisher<? extends T> source;
  
  public BlockingFlowableNext(Publisher<? extends T> paramPublisher) {
    this.source = paramPublisher;
  }
  
  public Iterator<T> iterator() {
    NextSubscriber<T> nextSubscriber = new NextSubscriber();
    return new NextIterator<T>(this.source, nextSubscriber);
  }
  
  static final class NextIterator<T> implements Iterator<T> {
    private Throwable error;
    
    private boolean hasNext = true;
    
    private boolean isNextConsumed = true;
    
    private final Publisher<? extends T> items;
    
    private T next;
    
    private boolean started;
    
    private final BlockingFlowableNext.NextSubscriber<T> subscriber;
    
    NextIterator(Publisher<? extends T> param1Publisher, BlockingFlowableNext.NextSubscriber<T> param1NextSubscriber) {
      this.items = param1Publisher;
      this.subscriber = param1NextSubscriber;
    }
    
    private boolean moveToNext() {
      try {
        if (!this.started) {
          this.started = true;
          this.subscriber.setWaiting();
          Flowable.fromPublisher(this.items).materialize().subscribe((FlowableSubscriber)this.subscriber);
        } 
        Notification<T> notification = this.subscriber.takeNext();
        if (notification.isOnNext()) {
          this.isNextConsumed = false;
          this.next = (T)notification.getValue();
          return true;
        } 
        this.hasNext = false;
        if (notification.isOnComplete())
          return false; 
        if (notification.isOnError()) {
          this.error = notification.getError();
          throw ExceptionHelper.wrapOrThrow(this.error);
        } 
        IllegalStateException illegalStateException = new IllegalStateException();
        this("Should not reach here");
        throw illegalStateException;
      } catch (InterruptedException interruptedException) {
        this.subscriber.dispose();
        this.error = interruptedException;
        throw ExceptionHelper.wrapOrThrow(interruptedException);
      } 
    }
    
    public boolean hasNext() {
      Throwable throwable = this.error;
      if (throwable == null) {
        boolean bool = this.hasNext;
        boolean bool1 = false;
        if (!bool)
          return false; 
        if (!this.isNextConsumed || moveToNext())
          bool1 = true; 
        return bool1;
      } 
      throw ExceptionHelper.wrapOrThrow(throwable);
    }
    
    public T next() {
      Throwable throwable = this.error;
      if (throwable == null) {
        if (hasNext()) {
          this.isNextConsumed = true;
          return this.next;
        } 
        throw new NoSuchElementException("No more elements");
      } 
      throw ExceptionHelper.wrapOrThrow(throwable);
    }
    
    public void remove() {
      throw new UnsupportedOperationException("Read only iterator");
    }
  }
  
  static final class NextSubscriber<T> extends DisposableSubscriber<Notification<T>> {
    private final BlockingQueue<Notification<T>> buf = new ArrayBlockingQueue<Notification<T>>(1);
    
    final AtomicInteger waiting = new AtomicInteger();
    
    public void onComplete() {}
    
    public void onError(Throwable param1Throwable) {
      RxJavaPlugins.onError(param1Throwable);
    }
    
    public void onNext(Notification<T> param1Notification) {
      Notification<T> notification = param1Notification;
      if (this.waiting.getAndSet(0) != 1)
        if (!param1Notification.isOnNext()) {
          notification = param1Notification;
        } else {
          return;
        }  
      while (!this.buf.offer(notification)) {
        param1Notification = this.buf.poll();
        if (param1Notification != null && !param1Notification.isOnNext())
          notification = param1Notification; 
      } 
    }
    
    void setWaiting() {
      this.waiting.set(1);
    }
    
    public Notification<T> takeNext() throws InterruptedException {
      setWaiting();
      BlockingHelper.verifyNonBlocking();
      return this.buf.take();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/BlockingFlowableNext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */