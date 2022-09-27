package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.BasicQueueSubscription;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;

public final class FlowableFromIterable<T> extends Flowable<T> {
  final Iterable<? extends T> source;
  
  public FlowableFromIterable(Iterable<? extends T> paramIterable) {
    this.source = paramIterable;
  }
  
  public static <T> void subscribe(Subscriber<? super T> paramSubscriber, Iterator<? extends T> paramIterator) {
    try {
      return;
    } finally {
      paramIterator = null;
      Exceptions.throwIfFatal((Throwable)paramIterator);
      EmptySubscription.error((Throwable)paramIterator, paramSubscriber);
    } 
  }
  
  public void subscribeActual(Subscriber<? super T> paramSubscriber) {
    try {
      return;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptySubscription.error(exception, paramSubscriber);
    } 
  }
  
  static abstract class BaseRangeSubscription<T> extends BasicQueueSubscription<T> {
    private static final long serialVersionUID = -2252972430506210021L;
    
    volatile boolean cancelled;
    
    Iterator<? extends T> it;
    
    boolean once;
    
    BaseRangeSubscription(Iterator<? extends T> param1Iterator) {
      this.it = param1Iterator;
    }
    
    public final void cancel() {
      this.cancelled = true;
    }
    
    public final void clear() {
      this.it = null;
    }
    
    abstract void fastPath();
    
    public final boolean isEmpty() {
      Iterator<? extends T> iterator = this.it;
      return (iterator == null || !iterator.hasNext());
    }
    
    public final T poll() {
      Iterator<? extends T> iterator = this.it;
      if (iterator == null)
        return null; 
      if (!this.once) {
        this.once = true;
      } else if (!iterator.hasNext()) {
        return null;
      } 
      return (T)ObjectHelper.requireNonNull(this.it.next(), "Iterator.next() returned a null value");
    }
    
    public final void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long) && BackpressureHelper.add((AtomicLong)this, param1Long) == 0L)
        if (param1Long == Long.MAX_VALUE) {
          fastPath();
        } else {
          slowPath(param1Long);
        }  
    }
    
    public final int requestFusion(int param1Int) {
      return param1Int & 0x1;
    }
    
    abstract void slowPath(long param1Long);
  }
  
  static final class IteratorConditionalSubscription<T> extends BaseRangeSubscription<T> {
    private static final long serialVersionUID = -6022804456014692607L;
    
    final ConditionalSubscriber<? super T> downstream;
    
    IteratorConditionalSubscription(ConditionalSubscriber<? super T> param1ConditionalSubscriber, Iterator<? extends T> param1Iterator) {
      super(param1Iterator);
      this.downstream = param1ConditionalSubscriber;
    }
    
    void fastPath() {
      Iterator<? extends T> iterator = this.it;
      ConditionalSubscriber<? super T> conditionalSubscriber = this.downstream;
      while (true) {
        if (this.cancelled)
          return; 
        try {
          T t = iterator.next();
          if (this.cancelled)
            return; 
          if (t == null)
            return; 
        } finally {
          iterator = null;
          Exceptions.throwIfFatal((Throwable)iterator);
          conditionalSubscriber.onError((Throwable)iterator);
        } 
      } 
    }
    
    void slowPath(long param1Long) {
      long l;
      Iterator<? extends T> iterator = this.it;
      ConditionalSubscriber<? super T> conditionalSubscriber = this.downstream;
      do {
        l = 0L;
        while (true) {
          while (l != param1Long) {
            if (this.cancelled)
              return; 
            try {
              T t = iterator.next();
              if (this.cancelled)
                return; 
              if (t == null)
                return; 
            } finally {
              iterator = null;
              Exceptions.throwIfFatal((Throwable)iterator);
              conditionalSubscriber.onError((Throwable)iterator);
            } 
          } 
          long l1 = get();
          param1Long = l1;
          if (l == l1) {
            l = addAndGet(-l);
            param1Long = l;
            break;
          } 
        } 
      } while (l != 0L);
    }
  }
  
  static final class IteratorSubscription<T> extends BaseRangeSubscription<T> {
    private static final long serialVersionUID = -6022804456014692607L;
    
    final Subscriber<? super T> downstream;
    
    IteratorSubscription(Subscriber<? super T> param1Subscriber, Iterator<? extends T> param1Iterator) {
      super(param1Iterator);
      this.downstream = param1Subscriber;
    }
    
    void fastPath() {
      Iterator<? extends T> iterator = this.it;
      Subscriber<? super T> subscriber = this.downstream;
      while (true) {
        if (this.cancelled)
          return; 
        try {
          T t = iterator.next();
          if (this.cancelled)
            return; 
          if (t == null)
            return; 
        } finally {
          Exception exception = null;
          Exceptions.throwIfFatal(exception);
          subscriber.onError(exception);
        } 
      } 
    }
    
    void slowPath(long param1Long) {
      long l;
      Iterator<? extends T> iterator = this.it;
      Subscriber<? super T> subscriber = this.downstream;
      do {
        l = 0L;
        while (true) {
          while (l != param1Long) {
            if (this.cancelled)
              return; 
            try {
              T t = iterator.next();
              if (this.cancelled)
                return; 
              if (t == null)
                return; 
            } finally {
              iterator = null;
              Exceptions.throwIfFatal((Throwable)iterator);
              subscriber.onError((Throwable)iterator);
            } 
          } 
          long l1 = get();
          param1Long = l1;
          if (l == l1) {
            l = addAndGet(-l);
            param1Long = l;
            break;
          } 
        } 
      } while (l != 0L);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableFromIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */