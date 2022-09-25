package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.BasicQueueSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableFromArray<T> extends Flowable<T> {
  final T[] array;
  
  public FlowableFromArray(T[] paramArrayOfT) {
    this.array = paramArrayOfT;
  }
  
  public void subscribeActual(Subscriber<? super T> paramSubscriber) {
    if (paramSubscriber instanceof ConditionalSubscriber) {
      paramSubscriber.onSubscribe((Subscription)new ArrayConditionalSubscription<T>((ConditionalSubscriber<? super T>)paramSubscriber, this.array));
    } else {
      paramSubscriber.onSubscribe((Subscription)new ArraySubscription<T>(paramSubscriber, this.array));
    } 
  }
  
  static final class ArrayConditionalSubscription<T> extends BaseArraySubscription<T> {
    private static final long serialVersionUID = 2587302975077663557L;
    
    final ConditionalSubscriber<? super T> downstream;
    
    ArrayConditionalSubscription(ConditionalSubscriber<? super T> param1ConditionalSubscriber, T[] param1ArrayOfT) {
      super(param1ArrayOfT);
      this.downstream = param1ConditionalSubscriber;
    }
    
    void fastPath() {
      T[] arrayOfT = this.array;
      int i = arrayOfT.length;
      ConditionalSubscriber<? super T> conditionalSubscriber = this.downstream;
      for (int j = this.index; j != i; j++) {
        if (this.cancelled)
          return; 
        T t = arrayOfT[j];
        if (t == null) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("The element at index ");
          stringBuilder.append(j);
          stringBuilder.append(" is null");
          conditionalSubscriber.onError(new NullPointerException(stringBuilder.toString()));
          return;
        } 
        conditionalSubscriber.tryOnNext(t);
      } 
      if (this.cancelled)
        return; 
      conditionalSubscriber.onComplete();
    }
    
    void slowPath(long param1Long) {
      long l;
      T[] arrayOfT = this.array;
      int i = arrayOfT.length;
      int j = this.index;
      ConditionalSubscriber<? super T> conditionalSubscriber = this.downstream;
      do {
        l = 0L;
        while (true) {
          if (l != param1Long && j != i) {
            if (this.cancelled)
              return; 
            T t = arrayOfT[j];
            if (t == null) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("The element at index ");
              stringBuilder.append(j);
              stringBuilder.append(" is null");
              conditionalSubscriber.onError(new NullPointerException(stringBuilder.toString()));
              return;
            } 
            long l2 = l;
            if (conditionalSubscriber.tryOnNext(t))
              l2 = l + 1L; 
            j++;
            l = l2;
            continue;
          } 
          if (j == i) {
            if (!this.cancelled)
              conditionalSubscriber.onComplete(); 
            return;
          } 
          long l1 = get();
          param1Long = l1;
          if (l == l1) {
            this.index = j;
            l = addAndGet(-l);
            param1Long = l;
            break;
          } 
        } 
      } while (l != 0L);
    }
  }
  
  static final class ArraySubscription<T> extends BaseArraySubscription<T> {
    private static final long serialVersionUID = 2587302975077663557L;
    
    final Subscriber<? super T> downstream;
    
    ArraySubscription(Subscriber<? super T> param1Subscriber, T[] param1ArrayOfT) {
      super(param1ArrayOfT);
      this.downstream = param1Subscriber;
    }
    
    void fastPath() {
      T[] arrayOfT = this.array;
      int i = arrayOfT.length;
      Subscriber<? super T> subscriber = this.downstream;
      for (int j = this.index; j != i; j++) {
        StringBuilder stringBuilder;
        if (this.cancelled)
          return; 
        T t = arrayOfT[j];
        if (t == null) {
          stringBuilder = new StringBuilder();
          stringBuilder.append("The element at index ");
          stringBuilder.append(j);
          stringBuilder.append(" is null");
          subscriber.onError(new NullPointerException(stringBuilder.toString()));
          return;
        } 
        subscriber.onNext(stringBuilder);
      } 
      if (this.cancelled)
        return; 
      subscriber.onComplete();
    }
    
    void slowPath(long param1Long) {
      long l;
      T[] arrayOfT = this.array;
      int i = arrayOfT.length;
      int j = this.index;
      Subscriber<? super T> subscriber = this.downstream;
      do {
        l = 0L;
        while (true) {
          if (l != param1Long && j != i) {
            StringBuilder stringBuilder;
            if (this.cancelled)
              return; 
            T t = arrayOfT[j];
            if (t == null) {
              stringBuilder = new StringBuilder();
              stringBuilder.append("The element at index ");
              stringBuilder.append(j);
              stringBuilder.append(" is null");
              subscriber.onError(new NullPointerException(stringBuilder.toString()));
              return;
            } 
            subscriber.onNext(stringBuilder);
            l++;
            j++;
            continue;
          } 
          if (j == i) {
            if (!this.cancelled)
              subscriber.onComplete(); 
            return;
          } 
          long l1 = get();
          param1Long = l1;
          if (l == l1) {
            this.index = j;
            l = addAndGet(-l);
            param1Long = l;
            break;
          } 
        } 
      } while (l != 0L);
    }
  }
  
  static abstract class BaseArraySubscription<T> extends BasicQueueSubscription<T> {
    private static final long serialVersionUID = -2252972430506210021L;
    
    final T[] array;
    
    volatile boolean cancelled;
    
    int index;
    
    BaseArraySubscription(T[] param1ArrayOfT) {
      this.array = param1ArrayOfT;
    }
    
    public final void cancel() {
      this.cancelled = true;
    }
    
    public final void clear() {
      this.index = this.array.length;
    }
    
    abstract void fastPath();
    
    public final boolean isEmpty() {
      boolean bool;
      if (this.index == this.array.length) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public final T poll() {
      int i = this.index;
      T[] arrayOfT = this.array;
      if (i == arrayOfT.length)
        return null; 
      this.index = i + 1;
      return (T)ObjectHelper.requireNonNull(arrayOfT[i], "array element is null");
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
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableFromArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */