package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableZip<T, R> extends Flowable<R> {
  final int bufferSize;
  
  final boolean delayError;
  
  final Publisher<? extends T>[] sources;
  
  final Iterable<? extends Publisher<? extends T>> sourcesIterable;
  
  final Function<? super Object[], ? extends R> zipper;
  
  public FlowableZip(Publisher<? extends T>[] paramArrayOfPublisher, Iterable<? extends Publisher<? extends T>> paramIterable, Function<? super Object[], ? extends R> paramFunction, int paramInt, boolean paramBoolean) {
    this.sources = paramArrayOfPublisher;
    this.sourcesIterable = paramIterable;
    this.zipper = paramFunction;
    this.bufferSize = paramInt;
    this.delayError = paramBoolean;
  }
  
  public void subscribeActual(Subscriber<? super R> paramSubscriber) {
    Publisher[] arrayOfPublisher;
    int i;
    Publisher<? extends T>[] arrayOfPublisher1 = this.sources;
    if (arrayOfPublisher1 == null) {
      Publisher[] arrayOfPublisher2 = new Publisher[8];
      Iterator<? extends Publisher<? extends T>> iterator = this.sourcesIterable.iterator();
      byte b = 0;
      while (true) {
        arrayOfPublisher = arrayOfPublisher2;
        i = b;
        if (iterator.hasNext()) {
          Publisher publisher = iterator.next();
          arrayOfPublisher = arrayOfPublisher2;
          if (b == arrayOfPublisher2.length) {
            arrayOfPublisher = new Publisher[(b >> 2) + b];
            System.arraycopy(arrayOfPublisher2, 0, arrayOfPublisher, 0, b);
          } 
          arrayOfPublisher[b] = publisher;
          b++;
          arrayOfPublisher2 = arrayOfPublisher;
          continue;
        } 
        break;
      } 
    } else {
      i = arrayOfPublisher.length;
    } 
    if (i == 0) {
      EmptySubscription.complete(paramSubscriber);
      return;
    } 
    ZipCoordinator<Object, R> zipCoordinator = new ZipCoordinator<Object, R>(paramSubscriber, this.zipper, i, this.bufferSize, this.delayError);
    paramSubscriber.onSubscribe(zipCoordinator);
    zipCoordinator.subscribe((Publisher<?>[])arrayOfPublisher, i);
  }
  
  static final class ZipCoordinator<T, R> extends AtomicInteger implements Subscription {
    private static final long serialVersionUID = -2434867452883857743L;
    
    volatile boolean cancelled;
    
    final Object[] current;
    
    final boolean delayErrors;
    
    final Subscriber<? super R> downstream;
    
    final AtomicThrowable errors;
    
    final AtomicLong requested;
    
    final FlowableZip.ZipSubscriber<T, R>[] subscribers;
    
    final Function<? super Object[], ? extends R> zipper;
    
    ZipCoordinator(Subscriber<? super R> param1Subscriber, Function<? super Object[], ? extends R> param1Function, int param1Int1, int param1Int2, boolean param1Boolean) {
      this.downstream = param1Subscriber;
      this.zipper = param1Function;
      this.delayErrors = param1Boolean;
      FlowableZip.ZipSubscriber[] arrayOfZipSubscriber = new FlowableZip.ZipSubscriber[param1Int1];
      for (byte b = 0; b < param1Int1; b++)
        arrayOfZipSubscriber[b] = new FlowableZip.ZipSubscriber<Object, Object>(this, param1Int2); 
      this.current = new Object[param1Int1];
      this.subscribers = (FlowableZip.ZipSubscriber<T, R>[])arrayOfZipSubscriber;
      this.requested = new AtomicLong();
      this.errors = new AtomicThrowable();
    }
    
    public void cancel() {
      if (!this.cancelled) {
        this.cancelled = true;
        cancelAll();
      } 
    }
    
    void cancelAll() {
      FlowableZip.ZipSubscriber<T, R>[] arrayOfZipSubscriber = this.subscribers;
      int i = arrayOfZipSubscriber.length;
      for (byte b = 0; b < i; b++)
        arrayOfZipSubscriber[b].cancel(); 
    }
    
    void drain() {
      int k;
      if (getAndIncrement() != 0)
        return; 
      Subscriber<? super R> subscriber = this.downstream;
      FlowableZip.ZipSubscriber<T, R>[] arrayOfZipSubscriber = this.subscribers;
      int i = arrayOfZipSubscriber.length;
      Object[] arrayOfObject = this.current;
      int j = 1;
      do {
        long l1 = this.requested.get();
        long l2 = 0L;
        while (l1 != l2) {
          Object object;
          if (this.cancelled)
            return; 
          if (!this.delayErrors && this.errors.get() != null) {
            cancelAll();
            subscriber.onError(this.errors.terminate());
            return;
          } 
          boolean bool = false;
          byte b = 0;
          while (b < i) {
            FlowableZip.ZipSubscriber<T, R> zipSubscriber = arrayOfZipSubscriber[b];
            Object object1 = object;
            if (arrayOfObject[b] == null) {
              try {
                boolean bool3 = zipSubscriber.done;
                SimpleQueue<T> simpleQueue = zipSubscriber.queue;
                if (simpleQueue != null) {
                  Object object2 = simpleQueue.poll();
                } else {
                  simpleQueue = null;
                } 
              } finally {
                zipSubscriber = null;
                Exceptions.throwIfFatal((Throwable)zipSubscriber);
                this.errors.addThrowable((Throwable)zipSubscriber);
              } 
              boolean bool1 = true;
            } 
            continue;
            b++;
            object = SYNTHETIC_LOCAL_VARIABLE_13;
          } 
          if (object != null)
            break; 
          try {
          
          } finally {
            Exception exception = null;
            Exceptions.throwIfFatal(exception);
            cancelAll();
            this.errors.addThrowable(exception);
            subscriber.onError(this.errors.terminate());
          } 
        } 
        if (l1 == l2) {
          if (this.cancelled)
            return; 
          if (!this.delayErrors && this.errors.get() != null) {
            cancelAll();
            subscriber.onError(this.errors.terminate());
            return;
          } 
          for (byte b = 0; b < i; b++) {
            FlowableZip.ZipSubscriber<T, R> zipSubscriber = arrayOfZipSubscriber[b];
            if (arrayOfObject[b] == null)
              try {
                boolean bool1 = zipSubscriber.done;
                SimpleQueue<T> simpleQueue = zipSubscriber.queue;
                if (simpleQueue != null) {
                  Object object = simpleQueue.poll();
                } else {
                  simpleQueue = null;
                } 
              } finally {
                zipSubscriber = null;
                Exceptions.throwIfFatal((Throwable)zipSubscriber);
                this.errors.addThrowable((Throwable)zipSubscriber);
              }  
          } 
        } 
        if (l2 != 0L) {
          int m = arrayOfZipSubscriber.length;
          for (byte b = 0; b < m; b++)
            arrayOfZipSubscriber[b].request(l2); 
          if (l1 != Long.MAX_VALUE)
            this.requested.addAndGet(-l2); 
        } 
        k = addAndGet(-j);
        j = k;
      } while (k != 0);
    }
    
    void error(FlowableZip.ZipSubscriber<T, R> param1ZipSubscriber, Throwable param1Throwable) {
      if (this.errors.addThrowable(param1Throwable)) {
        param1ZipSubscriber.done = true;
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.add(this.requested, param1Long);
        drain();
      } 
    }
    
    void subscribe(Publisher<? extends T>[] param1ArrayOfPublisher, int param1Int) {
      FlowableZip.ZipSubscriber<T, R>[] arrayOfZipSubscriber = this.subscribers;
      for (byte b = 0; b < param1Int && !this.cancelled && (this.delayErrors || this.errors.get() == null); b++)
        param1ArrayOfPublisher[b].subscribe((Subscriber)arrayOfZipSubscriber[b]); 
    }
  }
  
  static final class ZipSubscriber<T, R> extends AtomicReference<Subscription> implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = -4627193790118206028L;
    
    volatile boolean done;
    
    final int limit;
    
    final FlowableZip.ZipCoordinator<T, R> parent;
    
    final int prefetch;
    
    long produced;
    
    SimpleQueue<T> queue;
    
    int sourceMode;
    
    ZipSubscriber(FlowableZip.ZipCoordinator<T, R> param1ZipCoordinator, int param1Int) {
      this.parent = param1ZipCoordinator;
      this.prefetch = param1Int;
      this.limit = param1Int - (param1Int >> 2);
    }
    
    public void cancel() {
      SubscriptionHelper.cancel(this);
    }
    
    public void onComplete() {
      this.done = true;
      this.parent.drain();
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.error(this, param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.sourceMode != 2)
        this.queue.offer(param1T); 
      this.parent.drain();
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.setOnce(this, param1Subscription)) {
        if (param1Subscription instanceof QueueSubscription) {
          QueueSubscription queueSubscription = (QueueSubscription)param1Subscription;
          int i = queueSubscription.requestFusion(7);
          if (i == 1) {
            this.sourceMode = i;
            this.queue = (SimpleQueue<T>)queueSubscription;
            this.done = true;
            this.parent.drain();
            return;
          } 
          if (i == 2) {
            this.sourceMode = i;
            this.queue = (SimpleQueue<T>)queueSubscription;
            param1Subscription.request(this.prefetch);
            return;
          } 
        } 
        this.queue = (SimpleQueue<T>)new SpscArrayQueue(this.prefetch);
        param1Subscription.request(this.prefetch);
      } 
    }
    
    public void request(long param1Long) {
      if (this.sourceMode != 1) {
        param1Long = this.produced + param1Long;
        if (param1Long >= this.limit) {
          this.produced = 0L;
          get().request(param1Long);
        } else {
          this.produced = param1Long;
        } 
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableZip.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */