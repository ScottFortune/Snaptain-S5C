package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.subscribers.DefaultSubscriber;
import java.util.NoSuchElementException;

public final class BlockingFlowableMostRecent<T> implements Iterable<T> {
  final T initialValue;
  
  final Flowable<T> source;
  
  public BlockingFlowableMostRecent(Flowable<T> paramFlowable, T paramT) {
    this.source = paramFlowable;
    this.initialValue = paramT;
  }
  
  public java.util.Iterator<T> iterator() {
    MostRecentSubscriber<T> mostRecentSubscriber = new MostRecentSubscriber<T>(this.initialValue);
    this.source.subscribe((FlowableSubscriber)mostRecentSubscriber);
    return mostRecentSubscriber.getIterable();
  }
  
  static final class MostRecentSubscriber<T> extends DefaultSubscriber<T> {
    volatile Object value;
    
    MostRecentSubscriber(T param1T) {
      this.value = NotificationLite.next(param1T);
    }
    
    public Iterator getIterable() {
      return new Iterator();
    }
    
    public void onComplete() {
      this.value = NotificationLite.complete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.value = NotificationLite.error(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.value = NotificationLite.next(param1T);
    }
    
    final class Iterator implements java.util.Iterator<T> {
      private Object buf;
      
      public boolean hasNext() {
        this.buf = BlockingFlowableMostRecent.MostRecentSubscriber.this.value;
        return NotificationLite.isComplete(this.buf) ^ true;
      }
      
      public T next() {
        try {
          if (this.buf == null)
            this.buf = BlockingFlowableMostRecent.MostRecentSubscriber.this.value; 
          if (!NotificationLite.isComplete(this.buf)) {
            if (!NotificationLite.isError(this.buf))
              return (T)NotificationLite.getValue(this.buf); 
            throw ExceptionHelper.wrapOrThrow(NotificationLite.getError(this.buf));
          } 
          NoSuchElementException noSuchElementException = new NoSuchElementException();
          this();
          throw noSuchElementException;
        } finally {
          this.buf = null;
        } 
      }
      
      public void remove() {
        throw new UnsupportedOperationException("Read only iterator");
      }
    }
  }
  
  final class Iterator implements java.util.Iterator<T> {
    private Object buf;
    
    public boolean hasNext() {
      this.buf = this.this$0.value;
      return NotificationLite.isComplete(this.buf) ^ true;
    }
    
    public T next() {
      try {
        if (this.buf == null)
          this.buf = this.this$0.value; 
        if (!NotificationLite.isComplete(this.buf)) {
          if (!NotificationLite.isError(this.buf))
            return (T)NotificationLite.getValue(this.buf); 
          throw ExceptionHelper.wrapOrThrow(NotificationLite.getError(this.buf));
        } 
        NoSuchElementException noSuchElementException = new NoSuchElementException();
        this();
        throw noSuchElementException;
      } finally {
        this.buf = null;
      } 
    }
    
    public void remove() {
      throw new UnsupportedOperationException("Read only iterator");
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/BlockingFlowableMostRecent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */