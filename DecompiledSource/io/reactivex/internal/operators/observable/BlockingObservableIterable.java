package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.util.BlockingHelper;
import io.reactivex.internal.util.ExceptionHelper;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class BlockingObservableIterable<T> implements Iterable<T> {
  final int bufferSize;
  
  final ObservableSource<? extends T> source;
  
  public BlockingObservableIterable(ObservableSource<? extends T> paramObservableSource, int paramInt) {
    this.source = paramObservableSource;
    this.bufferSize = paramInt;
  }
  
  public Iterator<T> iterator() {
    BlockingObservableIterator<T> blockingObservableIterator = new BlockingObservableIterator(this.bufferSize);
    this.source.subscribe(blockingObservableIterator);
    return blockingObservableIterator;
  }
  
  static final class BlockingObservableIterator<T> extends AtomicReference<Disposable> implements Observer<T>, Iterator<T>, Disposable {
    private static final long serialVersionUID = 6695226475494099826L;
    
    final Condition condition;
    
    volatile boolean done;
    
    volatile Throwable error;
    
    final Lock lock;
    
    final SpscLinkedArrayQueue<T> queue;
    
    BlockingObservableIterator(int param1Int) {
      this.queue = new SpscLinkedArrayQueue(param1Int);
      this.lock = new ReentrantLock();
      this.condition = this.lock.newCondition();
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
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
          try {
            BlockingHelper.verifyNonBlocking();
            this.lock.lock();
            try {
              while (!this.done && this.queue.isEmpty() && !isDisposed())
                this.condition.await(); 
            } finally {
              this.lock.unlock();
            } 
          } catch (InterruptedException interruptedException) {
            DisposableHelper.dispose(this);
            signalConsumer();
            throw ExceptionHelper.wrapOrThrow(interruptedException);
          } 
          continue;
        } 
        return true;
      } 
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public T next() {
      if (hasNext())
        return (T)this.queue.poll(); 
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
      this.queue.offer(param1T);
      signalConsumer();
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
    
    public void remove() {
      throw new UnsupportedOperationException("remove");
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/BlockingObservableIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */