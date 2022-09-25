package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableAmb<T> extends Flowable<T> {
  final Publisher<? extends T>[] sources;
  
  final Iterable<? extends Publisher<? extends T>> sourcesIterable;
  
  public FlowableAmb(Publisher<? extends T>[] paramArrayOfPublisher, Iterable<? extends Publisher<? extends T>> paramIterable) {
    this.sources = paramArrayOfPublisher;
    this.sourcesIterable = paramIterable;
  }
  
  public void subscribeActual(Subscriber<? super T> paramSubscriber) {
    Publisher[] arrayOfPublisher;
    int i;
    Publisher<? extends T>[] arrayOfPublisher1 = this.sources;
    if (arrayOfPublisher1 == null) {
      Publisher[] arrayOfPublisher2 = new Publisher[8];
      try {
        Iterator<? extends Publisher<? extends T>> iterator = this.sourcesIterable.iterator();
        byte b = 0;
        while (true) {
          arrayOfPublisher = arrayOfPublisher2;
          i = b;
          if (iterator.hasNext()) {
            NullPointerException nullPointerException2;
            Publisher publisher = iterator.next();
            if (publisher == null)
              return; 
            NullPointerException nullPointerException1 = nullPointerException2;
            if (b == nullPointerException2.length) {
              arrayOfPublisher = new Publisher[(b >> 2) + b];
              System.arraycopy(nullPointerException2, 0, arrayOfPublisher, 0, b);
            } 
            arrayOfPublisher[b] = publisher;
            b++;
            Publisher[] arrayOfPublisher3 = arrayOfPublisher;
            continue;
          } 
          break;
        } 
      } finally {
        arrayOfPublisher2 = null;
        Exceptions.throwIfFatal((Throwable)arrayOfPublisher2);
        EmptySubscription.error((Throwable)arrayOfPublisher2, paramSubscriber);
      } 
    } else {
      i = arrayOfPublisher.length;
    } 
    if (i == 0) {
      EmptySubscription.complete(paramSubscriber);
      return;
    } 
    if (i == 1) {
      arrayOfPublisher[0].subscribe(paramSubscriber);
      return;
    } 
    (new AmbCoordinator(paramSubscriber, i)).subscribe((Publisher<?>[])arrayOfPublisher);
  }
  
  static final class AmbCoordinator<T> implements Subscription {
    final Subscriber<? super T> downstream;
    
    final FlowableAmb.AmbInnerSubscriber<T>[] subscribers;
    
    final AtomicInteger winner = new AtomicInteger();
    
    AmbCoordinator(Subscriber<? super T> param1Subscriber, int param1Int) {
      this.downstream = param1Subscriber;
      this.subscribers = (FlowableAmb.AmbInnerSubscriber<T>[])new FlowableAmb.AmbInnerSubscriber[param1Int];
    }
    
    public void cancel() {
      if (this.winner.get() != -1) {
        this.winner.lazySet(-1);
        FlowableAmb.AmbInnerSubscriber<T>[] arrayOfAmbInnerSubscriber = this.subscribers;
        int i = arrayOfAmbInnerSubscriber.length;
        for (byte b = 0; b < i; b++)
          arrayOfAmbInnerSubscriber[b].cancel(); 
      } 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        int i = this.winner.get();
        if (i > 0) {
          this.subscribers[i - 1].request(param1Long);
        } else if (i == 0) {
          FlowableAmb.AmbInnerSubscriber<T>[] arrayOfAmbInnerSubscriber = this.subscribers;
          int j = arrayOfAmbInnerSubscriber.length;
          for (i = 0; i < j; i++)
            arrayOfAmbInnerSubscriber[i].request(param1Long); 
        } 
      } 
    }
    
    public void subscribe(Publisher<? extends T>[] param1ArrayOfPublisher) {
      FlowableAmb.AmbInnerSubscriber<T>[] arrayOfAmbInnerSubscriber = this.subscribers;
      int i = arrayOfAmbInnerSubscriber.length;
      boolean bool = false;
      int j;
      for (j = 0; j < i; j = k) {
        int k = j + 1;
        arrayOfAmbInnerSubscriber[j] = new FlowableAmb.AmbInnerSubscriber<T>(this, k, this.downstream);
      } 
      this.winner.lazySet(0);
      this.downstream.onSubscribe(this);
      for (j = bool; j < i; j++) {
        if (this.winner.get() != 0)
          return; 
        param1ArrayOfPublisher[j].subscribe((Subscriber)arrayOfAmbInnerSubscriber[j]);
      } 
    }
    
    public boolean win(int param1Int) {
      int i = this.winner.get();
      int j = 0;
      if (i == 0 && this.winner.compareAndSet(0, param1Int)) {
        FlowableAmb.AmbInnerSubscriber<T>[] arrayOfAmbInnerSubscriber = this.subscribers;
        int k = arrayOfAmbInnerSubscriber.length;
        while (j < k) {
          i = j + 1;
          if (i != param1Int)
            arrayOfAmbInnerSubscriber[j].cancel(); 
          j = i;
        } 
        return true;
      } 
      return false;
    }
  }
  
  static final class AmbInnerSubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = -1185974347409665484L;
    
    final Subscriber<? super T> downstream;
    
    final int index;
    
    final AtomicLong missedRequested = new AtomicLong();
    
    final FlowableAmb.AmbCoordinator<T> parent;
    
    boolean won;
    
    AmbInnerSubscriber(FlowableAmb.AmbCoordinator<T> param1AmbCoordinator, int param1Int, Subscriber<? super T> param1Subscriber) {
      this.parent = param1AmbCoordinator;
      this.index = param1Int;
      this.downstream = param1Subscriber;
    }
    
    public void cancel() {
      SubscriptionHelper.cancel(this);
    }
    
    public void onComplete() {
      if (this.won) {
        this.downstream.onComplete();
      } else if (this.parent.win(this.index)) {
        this.won = true;
        this.downstream.onComplete();
      } else {
        get().cancel();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.won) {
        this.downstream.onError(param1Throwable);
      } else if (this.parent.win(this.index)) {
        this.won = true;
        this.downstream.onError(param1Throwable);
      } else {
        get().cancel();
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      if (this.won) {
        this.downstream.onNext(param1T);
      } else if (this.parent.win(this.index)) {
        this.won = true;
        this.downstream.onNext(param1T);
      } else {
        get().cancel();
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.deferredSetOnce(this, this.missedRequested, param1Subscription);
    }
    
    public void request(long param1Long) {
      SubscriptionHelper.deferredRequest(this, this.missedRequested, param1Long);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableAmb.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */