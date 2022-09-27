package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiPredicate;
import io.reactivex.internal.fuseable.FuseToFlowable;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class FlowableSequenceEqualSingle<T> extends Single<Boolean> implements FuseToFlowable<Boolean> {
  final BiPredicate<? super T, ? super T> comparer;
  
  final Publisher<? extends T> first;
  
  final int prefetch;
  
  final Publisher<? extends T> second;
  
  public FlowableSequenceEqualSingle(Publisher<? extends T> paramPublisher1, Publisher<? extends T> paramPublisher2, BiPredicate<? super T, ? super T> paramBiPredicate, int paramInt) {
    this.first = paramPublisher1;
    this.second = paramPublisher2;
    this.comparer = paramBiPredicate;
    this.prefetch = paramInt;
  }
  
  public Flowable<Boolean> fuseToFlowable() {
    return RxJavaPlugins.onAssembly(new FlowableSequenceEqual<T>(this.first, this.second, this.comparer, this.prefetch));
  }
  
  public void subscribeActual(SingleObserver<? super Boolean> paramSingleObserver) {
    EqualCoordinator<T> equalCoordinator = new EqualCoordinator<T>(paramSingleObserver, this.prefetch, this.comparer);
    paramSingleObserver.onSubscribe(equalCoordinator);
    equalCoordinator.subscribe(this.first, this.second);
  }
  
  static final class EqualCoordinator<T> extends AtomicInteger implements Disposable, FlowableSequenceEqual.EqualCoordinatorHelper {
    private static final long serialVersionUID = -6178010334400373240L;
    
    final BiPredicate<? super T, ? super T> comparer;
    
    final SingleObserver<? super Boolean> downstream;
    
    final AtomicThrowable error;
    
    final FlowableSequenceEqual.EqualSubscriber<T> first;
    
    final FlowableSequenceEqual.EqualSubscriber<T> second;
    
    T v1;
    
    T v2;
    
    EqualCoordinator(SingleObserver<? super Boolean> param1SingleObserver, int param1Int, BiPredicate<? super T, ? super T> param1BiPredicate) {
      this.downstream = param1SingleObserver;
      this.comparer = param1BiPredicate;
      this.first = new FlowableSequenceEqual.EqualSubscriber<T>(this, param1Int);
      this.second = new FlowableSequenceEqual.EqualSubscriber<T>(this, param1Int);
      this.error = new AtomicThrowable();
    }
    
    void cancelAndClear() {
      this.first.cancel();
      this.first.clear();
      this.second.cancel();
      this.second.clear();
    }
    
    public void dispose() {
      this.first.cancel();
      this.second.cancel();
      if (getAndIncrement() == 0) {
        this.first.clear();
        this.second.clear();
      } 
    }
    
    public void drain() {
      int j;
      if (getAndIncrement() != 0)
        return; 
      int i = 1;
      do {
        SimpleQueue<T> simpleQueue1 = this.first.queue;
        SimpleQueue<T> simpleQueue2 = this.second.queue;
        if (simpleQueue1 != null && simpleQueue2 != null) {
          while (true) {
            boolean bool2;
            boolean bool4;
            if (isDisposed()) {
              this.first.clear();
              this.second.clear();
              return;
            } 
            if ((Throwable)this.error.get() != null) {
              cancelAndClear();
              this.downstream.onError(this.error.terminate());
              return;
            } 
            boolean bool1 = this.first.done;
            T t1 = this.v1;
            T t2 = t1;
            if (t1 == null)
              try {
              
              } finally {
                t2 = null;
                Exceptions.throwIfFatal((Throwable)t2);
                cancelAndClear();
                this.error.addThrowable((Throwable)t2);
                this.downstream.onError(this.error.terminate());
              }  
            if (t2 == null) {
              bool2 = true;
            } else {
              bool2 = false;
            } 
            boolean bool3 = this.second.done;
            T t3 = this.v2;
            t1 = t3;
            if (t3 == null)
              try {
              
              } finally {
                t2 = null;
                Exceptions.throwIfFatal((Throwable)t2);
                cancelAndClear();
                this.error.addThrowable((Throwable)t2);
                this.downstream.onError(this.error.terminate());
              }  
            if (t1 == null) {
              bool4 = true;
            } else {
              bool4 = false;
            } 
            if (bool1 && bool3 && bool2 && bool4) {
              this.downstream.onSuccess(Boolean.valueOf(true));
              return;
            } 
            if (bool1 && bool3 && bool2 != bool4) {
              cancelAndClear();
              this.downstream.onSuccess(Boolean.valueOf(false));
              return;
            } 
            if (bool2 || bool4)
              break; 
            try {
              bool1 = this.comparer.test(t2, t1);
            } finally {
              t2 = null;
              Exceptions.throwIfFatal((Throwable)t2);
              cancelAndClear();
              this.error.addThrowable((Throwable)t2);
              this.downstream.onError(this.error.terminate());
            } 
          } 
        } else {
          if (isDisposed()) {
            this.first.clear();
            this.second.clear();
            return;
          } 
          if ((Throwable)this.error.get() != null) {
            cancelAndClear();
            this.downstream.onError(this.error.terminate());
            return;
          } 
        } 
        j = addAndGet(-i);
        i = j;
      } while (j != 0);
    }
    
    public void innerError(Throwable param1Throwable) {
      if (this.error.addThrowable(param1Throwable)) {
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (this.first.get() == SubscriptionHelper.CANCELLED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    void subscribe(Publisher<? extends T> param1Publisher1, Publisher<? extends T> param1Publisher2) {
      param1Publisher1.subscribe((Subscriber)this.first);
      param1Publisher2.subscribe((Subscriber)this.second);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableSequenceEqualSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */