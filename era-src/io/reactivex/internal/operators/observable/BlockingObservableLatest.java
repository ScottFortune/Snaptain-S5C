package io.reactivex.internal.operators.observable;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.internal.util.BlockingHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

public final class BlockingObservableLatest<T> implements Iterable<T> {
  final ObservableSource<T> source;
  
  public BlockingObservableLatest(ObservableSource<T> paramObservableSource) {
    this.source = paramObservableSource;
  }
  
  public Iterator<T> iterator() {
    BlockingObservableLatestIterator<T> blockingObservableLatestIterator = new BlockingObservableLatestIterator();
    Observable.wrap(this.source).materialize().subscribe((Observer)blockingObservableLatestIterator);
    return blockingObservableLatestIterator;
  }
  
  static final class BlockingObservableLatestIterator<T> extends DisposableObserver<Notification<T>> implements Iterator<T> {
    Notification<T> iteratorNotification;
    
    final Semaphore notify = new Semaphore(0);
    
    final AtomicReference<Notification<T>> value = new AtomicReference<Notification<T>>();
    
    public boolean hasNext() {
      Notification<T> notification = this.iteratorNotification;
      if (notification == null || !notification.isOnError()) {
        if (this.iteratorNotification == null)
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
      if (hasNext()) {
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/BlockingObservableLatest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */