package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.internal.subscriptions.SubscriptionArbiter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableConcatArray<T> extends Flowable<T> {
  final boolean delayError;
  
  final Publisher<? extends T>[] sources;
  
  public FlowableConcatArray(Publisher<? extends T>[] paramArrayOfPublisher, boolean paramBoolean) {
    this.sources = paramArrayOfPublisher;
    this.delayError = paramBoolean;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    ConcatArraySubscriber<T> concatArraySubscriber = new ConcatArraySubscriber<T>(this.sources, this.delayError, paramSubscriber);
    paramSubscriber.onSubscribe((Subscription)concatArraySubscriber);
    concatArraySubscriber.onComplete();
  }
  
  static final class ConcatArraySubscriber<T> extends SubscriptionArbiter implements FlowableSubscriber<T> {
    private static final long serialVersionUID = -8158322871608889516L;
    
    final boolean delayError;
    
    final Subscriber<? super T> downstream;
    
    List<Throwable> errors;
    
    int index;
    
    long produced;
    
    final Publisher<? extends T>[] sources;
    
    final AtomicInteger wip;
    
    ConcatArraySubscriber(Publisher<? extends T>[] param1ArrayOfPublisher, boolean param1Boolean, Subscriber<? super T> param1Subscriber) {
      super(false);
      this.downstream = param1Subscriber;
      this.sources = param1ArrayOfPublisher;
      this.delayError = param1Boolean;
      this.wip = new AtomicInteger();
    }
    
    public void onComplete() {
      if (this.wip.getAndIncrement() == 0) {
        Publisher<? extends T>[] arrayOfPublisher = this.sources;
        int i = arrayOfPublisher.length;
        int j = this.index;
        while (true) {
          List<Throwable> list;
          if (j == i) {
            list = this.errors;
            if (list != null) {
              if (list.size() == 1) {
                this.downstream.onError(list.get(0));
              } else {
                this.downstream.onError((Throwable)new CompositeException(list));
              } 
            } else {
              this.downstream.onComplete();
            } 
            return;
          } 
          Publisher<? extends T> publisher = arrayOfPublisher[j];
          if (publisher == null) {
            NullPointerException nullPointerException = new NullPointerException("A Publisher entry is null");
            if (this.delayError) {
              List<Throwable> list1 = this.errors;
              list = list1;
              if (list1 == null) {
                list = new ArrayList<Throwable>(i - j + 1);
                this.errors = list;
              } 
              list.add(nullPointerException);
              j++;
              continue;
            } 
            this.downstream.onError(nullPointerException);
            return;
          } 
          long l = this.produced;
          if (l != 0L) {
            this.produced = 0L;
            produced(l);
          } 
          list.subscribe((Subscriber)this);
          this.index = ++j;
          if (this.wip.decrementAndGet() == 0)
            break; 
        } 
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.delayError) {
        List<Throwable> list1 = this.errors;
        List<Throwable> list2 = list1;
        if (list1 == null) {
          list2 = new ArrayList<Throwable>(this.sources.length - this.index + 1);
          this.errors = list2;
        } 
        list2.add(param1Throwable);
        onComplete();
      } else {
        this.downstream.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      this.produced++;
      this.downstream.onNext(param1T);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      setSubscription(param1Subscription);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableConcatArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */