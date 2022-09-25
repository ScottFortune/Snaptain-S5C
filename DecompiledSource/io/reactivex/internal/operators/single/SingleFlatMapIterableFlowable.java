package io.reactivex.internal.operators.single;

import io.reactivex.Flowable;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class SingleFlatMapIterableFlowable<T, R> extends Flowable<R> {
  final Function<? super T, ? extends Iterable<? extends R>> mapper;
  
  final SingleSource<T> source;
  
  public SingleFlatMapIterableFlowable(SingleSource<T> paramSingleSource, Function<? super T, ? extends Iterable<? extends R>> paramFunction) {
    this.source = paramSingleSource;
    this.mapper = paramFunction;
  }
  
  protected void subscribeActual(Subscriber<? super R> paramSubscriber) {
    this.source.subscribe(new FlatMapIterableObserver<T, R>(paramSubscriber, this.mapper));
  }
  
  static final class FlatMapIterableObserver<T, R> extends BasicIntQueueSubscription<R> implements SingleObserver<T> {
    private static final long serialVersionUID = -8938804753851907758L;
    
    volatile boolean cancelled;
    
    final Subscriber<? super R> downstream;
    
    volatile Iterator<? extends R> it;
    
    final Function<? super T, ? extends Iterable<? extends R>> mapper;
    
    boolean outputFused;
    
    final AtomicLong requested;
    
    Disposable upstream;
    
    FlatMapIterableObserver(Subscriber<? super R> param1Subscriber, Function<? super T, ? extends Iterable<? extends R>> param1Function) {
      this.downstream = param1Subscriber;
      this.mapper = param1Function;
      this.requested = new AtomicLong();
    }
    
    public void cancel() {
      this.cancelled = true;
      this.upstream.dispose();
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
    }
    
    public void clear() {
      this.it = null;
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      Subscriber<? super R> subscriber = this.downstream;
      Iterator<? extends R> iterator = this.it;
      if (this.outputFused && iterator != null) {
        subscriber.onNext(null);
        subscriber.onComplete();
        return;
      } 
      int i = 1;
      while (true) {
        if (iterator != null) {
          long l1 = this.requested.get();
          if (l1 == Long.MAX_VALUE) {
            slowPath(subscriber, iterator);
            return;
          } 
          long l2 = 0L;
          while (l2 != l1) {
            if (this.cancelled)
              return; 
            try {
              Object object = ObjectHelper.requireNonNull(iterator.next(), "The iterator returned a null value");
              subscriber.onNext(object);
            } finally {
              iterator = null;
              Exceptions.throwIfFatal((Throwable)iterator);
              subscriber.onError((Throwable)iterator);
            } 
          } 
          if (l2 != 0L)
            BackpressureHelper.produced(this.requested, l2); 
        } 
        int j = addAndGet(-i);
        if (j == 0)
          return; 
        i = j;
        if (iterator == null) {
          iterator = this.it;
          i = j;
        } 
      } 
    }
    
    public boolean isEmpty() {
      boolean bool;
      if (this.it == null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onError(Throwable param1Throwable) {
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      this.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe((Subscription)this);
      } 
    }
    
    public void onSuccess(T param1T) {
      try {
        Iterator<? extends R> iterator = ((Iterable)this.mapper.apply(param1T)).iterator();
        boolean bool = iterator.hasNext();
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        this.downstream.onError((Throwable)param1T);
      } 
    }
    
    public R poll() throws Exception {
      Iterator<? extends R> iterator = this.it;
      if (iterator != null) {
        Object object = ObjectHelper.requireNonNull(iterator.next(), "The iterator returned a null value");
        if (!iterator.hasNext())
          this.it = null; 
        return (R)object;
      } 
      return null;
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.add(this.requested, param1Long);
        drain();
      } 
    }
    
    public int requestFusion(int param1Int) {
      if ((param1Int & 0x2) != 0) {
        this.outputFused = true;
        return 2;
      } 
      return 0;
    }
    
    void slowPath(Subscriber<? super R> param1Subscriber, Iterator<? extends R> param1Iterator) {
      while (true) {
        if (this.cancelled)
          return; 
        try {
          R r = param1Iterator.next();
        } finally {
          param1Iterator = null;
          Exceptions.throwIfFatal((Throwable)param1Iterator);
          param1Subscriber.onError((Throwable)param1Iterator);
        } 
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleFlatMapIterableFlowable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */