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
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;

public final class BlockingFlowableLatest<T> implements Iterable<T> {
  final Publisher<? extends T> source;
  
  public BlockingFlowableLatest(Publisher<? extends T> paramPublisher) {
    this.source = paramPublisher;
  }
  
  public Iterator<T> iterator() {
    LatestSubscriberIterator<T> latestSubscriberIterator = new LatestSubscriberIterator();
    Flowable.fromPublisher(this.source).materialize().subscribe((FlowableSubscriber)latestSubscriberIterator);
    return latestSubscriberIterator;
  }
  
  static final class LatestSubscriberIterator<T> extends DisposableSubscriber<Notification<T>> implements Iterator<T> {
    Notification<T> iteratorNotification;
    
    final Semaphore notify = new Semaphore(0);
    
    final AtomicReference<Notification<T>> value = new AtomicReference<Notification<T>>();
    
    public boolean hasNext() {
      Notification<T> notification = this.iteratorNotification;
      if (notification == null || !notification.isOnError()) {
        notification = this.iteratorNotification;
        if ((notification == null || notification.isOnNext()) && this.iteratorNotification == null)
          try {
            BlockingHelper.verifyNonBlocking();
            this.notify.acquire();
            notification = this.value.getAndSet(null);
            this.iteratorNotification = notification;
            if (notification.isOnError())
              throw ExceptionHelper.wrapOrThrow(notification.getError()); 
          } catch (InterruptedException interruptedException) {
            dispose();
            this.iteratorNotification = Notification.createOnError(interruptedException);
            throw ExceptionHelper.wrapOrThrow(interruptedException);
          }  
        return this.iteratorNotification.isOnNext();
      } 
      throw ExceptionHelper.wrapOrThrow(this.iteratorNotification.getError());
    }
    
    public T next() {
      if (hasNext() && this.iteratorNotification.isOnNext()) {
        Object object = this.iteratorNotification.getValue();
        this.iteratorNotification = null;
        return (T)object;
      } 
      throw new NoSuchElementException();
    }
    
    public void onComplete() {}
    
    public void onError(Throwable param1Throwable) {
      RxJavaPlugins.onError(param1Throwable);
    }
    
    public void onNext(Notification<T> param1Notification) {
      boolean bool;
      if (this.value.getAndSet(param1Notification) == null) {
        bool = true;
      } else {
        bool = false;
      } 
      if (bool)
        this.notify.release(); 
    }
    
    public void remove() {
      throw new UnsupportedOperationException("Read-only iterator.");
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/BlockingFlowableLatest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */