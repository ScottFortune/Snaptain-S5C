package io.reactivex.internal.operators.flowable;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscribers.BlockingSubscriber;
import io.reactivex.internal.subscribers.BoundedSubscriber;
import io.reactivex.internal.subscribers.LambdaSubscriber;
import io.reactivex.internal.util.BlockingHelper;
import io.reactivex.internal.util.BlockingIgnoringReceiver;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.NotificationLite;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class FlowableBlockingSubscribe {
  private FlowableBlockingSubscribe() {
    throw new IllegalStateException("No instances!");
  }
  
  public static <T> void subscribe(Publisher<? extends T> paramPublisher) {
    BlockingIgnoringReceiver blockingIgnoringReceiver = new BlockingIgnoringReceiver();
    LambdaSubscriber lambdaSubscriber = new LambdaSubscriber(Functions.emptyConsumer(), (Consumer)blockingIgnoringReceiver, (Action)blockingIgnoringReceiver, Functions.REQUEST_MAX);
    paramPublisher.subscribe((Subscriber)lambdaSubscriber);
    BlockingHelper.awaitForComplete((CountDownLatch)blockingIgnoringReceiver, (Disposable)lambdaSubscriber);
    Throwable throwable = blockingIgnoringReceiver.error;
    if (throwable == null)
      return; 
    throw ExceptionHelper.wrapOrThrow(throwable);
  }
  
  public static <T> void subscribe(Publisher<? extends T> paramPublisher, Consumer<? super T> paramConsumer, Consumer<? super Throwable> paramConsumer1, Action paramAction) {
    ObjectHelper.requireNonNull(paramConsumer, "onNext is null");
    ObjectHelper.requireNonNull(paramConsumer1, "onError is null");
    ObjectHelper.requireNonNull(paramAction, "onComplete is null");
    subscribe(paramPublisher, (Subscriber<? super T>)new LambdaSubscriber(paramConsumer, paramConsumer1, paramAction, Functions.REQUEST_MAX));
  }
  
  public static <T> void subscribe(Publisher<? extends T> paramPublisher, Consumer<? super T> paramConsumer, Consumer<? super Throwable> paramConsumer1, Action paramAction, int paramInt) {
    ObjectHelper.requireNonNull(paramConsumer, "onNext is null");
    ObjectHelper.requireNonNull(paramConsumer1, "onError is null");
    ObjectHelper.requireNonNull(paramAction, "onComplete is null");
    ObjectHelper.verifyPositive(paramInt, "number > 0 required");
    subscribe(paramPublisher, (Subscriber<? super T>)new BoundedSubscriber(paramConsumer, paramConsumer1, paramAction, Functions.boundedConsumer(paramInt), paramInt));
  }
  
  public static <T> void subscribe(Publisher<? extends T> paramPublisher, Subscriber<? super T> paramSubscriber) {
    LinkedBlockingQueue<Object> linkedBlockingQueue = new LinkedBlockingQueue();
    BlockingSubscriber blockingSubscriber = new BlockingSubscriber(linkedBlockingQueue);
    paramPublisher.subscribe((Subscriber)blockingSubscriber);
    try {
      while (!blockingSubscriber.isCancelled()) {
        Publisher publisher = (Publisher)linkedBlockingQueue.poll();
        paramPublisher = publisher;
        if (publisher == null) {
          if (blockingSubscriber.isCancelled())
            break; 
          BlockingHelper.verifyNonBlocking();
          paramPublisher = (Publisher<? extends T>)linkedBlockingQueue.take();
        } 
        if (!blockingSubscriber.isCancelled() && paramPublisher != BlockingSubscriber.TERMINATED) {
          boolean bool = NotificationLite.acceptFull(paramPublisher, paramSubscriber);
          if (bool)
            break; 
          continue;
        } 
        break;
      } 
    } catch (InterruptedException interruptedException) {
      blockingSubscriber.cancel();
      paramSubscriber.onError(interruptedException);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableBlockingSubscribe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */