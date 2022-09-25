package io.reactivex.internal.util;

import io.reactivex.disposables.Disposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.CountDownLatch;

public final class BlockingHelper {
  private BlockingHelper() {
    throw new IllegalStateException("No instances!");
  }
  
  public static void awaitForComplete(CountDownLatch paramCountDownLatch, Disposable paramDisposable) {
    if (paramCountDownLatch.getCount() == 0L)
      return; 
    try {
      verifyNonBlocking();
      paramCountDownLatch.await();
      return;
    } catch (InterruptedException interruptedException) {
      paramDisposable.dispose();
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Interrupted while waiting for subscription to complete.", interruptedException);
    } 
  }
  
  public static void verifyNonBlocking() {
    if (!RxJavaPlugins.isFailOnNonBlockingScheduler() || (!(Thread.currentThread() instanceof io.reactivex.internal.schedulers.NonBlockingThread) && !RxJavaPlugins.onBeforeBlocking()))
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Attempt to block on a Scheduler ");
    stringBuilder.append(Thread.currentThread().getName());
    stringBuilder.append(" that doesn't support blocking operators as they may lead to deadlock");
    throw new IllegalStateException(stringBuilder.toString());
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/util/BlockingHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */