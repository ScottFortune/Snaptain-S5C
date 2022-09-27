package io.reactivex.internal.util;

import io.reactivex.Observer;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Subscriber;

public final class HalfSerializer {
  private HalfSerializer() {
    throw new IllegalStateException("No instances!");
  }
  
  public static void onComplete(Observer<?> paramObserver, AtomicInteger paramAtomicInteger, AtomicThrowable paramAtomicThrowable) {
    if (paramAtomicInteger.getAndIncrement() == 0) {
      Throwable throwable = paramAtomicThrowable.terminate();
      if (throwable != null) {
        paramObserver.onError(throwable);
      } else {
        paramObserver.onComplete();
      } 
    } 
  }
  
  public static void onComplete(Subscriber<?> paramSubscriber, AtomicInteger paramAtomicInteger, AtomicThrowable paramAtomicThrowable) {
    if (paramAtomicInteger.getAndIncrement() == 0) {
      Throwable throwable = paramAtomicThrowable.terminate();
      if (throwable != null) {
        paramSubscriber.onError(throwable);
      } else {
        paramSubscriber.onComplete();
      } 
    } 
  }
  
  public static void onError(Observer<?> paramObserver, Throwable paramThrowable, AtomicInteger paramAtomicInteger, AtomicThrowable paramAtomicThrowable) {
    if (paramAtomicThrowable.addThrowable(paramThrowable)) {
      if (paramAtomicInteger.getAndIncrement() == 0)
        paramObserver.onError(paramAtomicThrowable.terminate()); 
    } else {
      RxJavaPlugins.onError(paramThrowable);
    } 
  }
  
  public static void onError(Subscriber<?> paramSubscriber, Throwable paramThrowable, AtomicInteger paramAtomicInteger, AtomicThrowable paramAtomicThrowable) {
    if (paramAtomicThrowable.addThrowable(paramThrowable)) {
      if (paramAtomicInteger.getAndIncrement() == 0)
        paramSubscriber.onError(paramAtomicThrowable.terminate()); 
    } else {
      RxJavaPlugins.onError(paramThrowable);
    } 
  }
  
  public static <T> void onNext(Observer<? super T> paramObserver, T paramT, AtomicInteger paramAtomicInteger, AtomicThrowable paramAtomicThrowable) {
    if (paramAtomicInteger.get() == 0 && paramAtomicInteger.compareAndSet(0, 1)) {
      paramObserver.onNext(paramT);
      if (paramAtomicInteger.decrementAndGet() != 0) {
        Throwable throwable = paramAtomicThrowable.terminate();
        if (throwable != null) {
          paramObserver.onError(throwable);
        } else {
          paramObserver.onComplete();
        } 
      } 
    } 
  }
  
  public static <T> void onNext(Subscriber<? super T> paramSubscriber, T paramT, AtomicInteger paramAtomicInteger, AtomicThrowable paramAtomicThrowable) {
    if (paramAtomicInteger.get() == 0 && paramAtomicInteger.compareAndSet(0, 1)) {
      paramSubscriber.onNext(paramT);
      if (paramAtomicInteger.decrementAndGet() != 0) {
        Throwable throwable = paramAtomicThrowable.terminate();
        if (throwable != null) {
          paramSubscriber.onError(throwable);
        } else {
          paramSubscriber.onComplete();
        } 
      } 
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/util/HalfSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */