package io.reactivex.internal.operators.observable;

import io.reactivex.Notification;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.internal.util.BlockingHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public final class BlockingObservableNext<T> implements Iterable<T> {
  final ObservableSource<T> source;
  
  public BlockingObservableNext(ObservableSource<T> paramObservableSource) {
    this.source = paramObservableSource;
  }
  
  public Iterator<T> iterator() {
    NextObserver<T> nextObserver = new NextObserver();
    return new NextIterator<T>(this.source, nextObserver);
  }
  
  static final class NextIterator<T> implements Iterator<T> {
    private Throwable error;
    
    private boolean hasNext = true;
    
    private boolean isNextConsumed = true;
    
    private final ObservableSource<T> items;
    
    private T next;
    
    private final BlockingObservableNext.NextObserver<T> observer;
    
    private boolean started;
    
    NextIterator(ObservableSource<T> param1ObservableSource, BlockingObservableNext.NextObserver<T> param1NextObserver) {
      this.items = param1ObservableSource;
      this.observer = param1NextObserver;
    }
    
    private boolean moveToNext() {
      if (!this.started) {
        this.started = true;
        this.observer.setWaiting();
        (new ObservableMaterialize(this.items)).subscribe((Observer)this.observer);
      } 
      try {
        Notification<T> notification = this.observer.takeNext();
        if (notification.isOnNext()) {
          this.isNextConsumed = false;
          this.next = (T)notification.getValue();
          return true;
        } 
        this.hasNext = false;
        if (notification.isOnComplete())
          return false; 
        this.error = notification.getError();
        throw ExceptionHelper.wrapOrThrow(this.error);
      } catch (InterruptedException interruptedException) {
        this.observer.dispose();
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
  
  static final class NextObserver<T> extends DisposableObserver<Notification<T>> {
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/BlockingObservableNext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */