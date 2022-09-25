package com.bumptech.glide.util;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public final class Executors {
  private static final Executor DIRECT_EXECUTOR;
  
  private static final Executor MAIN_THREAD_EXECUTOR = new Executor() {
      private final Handler handler = new Handler(Looper.getMainLooper());
      
      public void execute(Runnable param1Runnable) {
        this.handler.post(param1Runnable);
      }
    };
  
  static {
    DIRECT_EXECUTOR = new Executor() {
        public void execute(Runnable param1Runnable) {
          param1Runnable.run();
        }
      };
  }
  
  public static Executor directExecutor() {
    return DIRECT_EXECUTOR;
  }
  
  public static Executor mainThreadExecutor() {
    return MAIN_THREAD_EXECUTOR;
  }
  
  public static void shutdownAndAwaitTermination(ExecutorService paramExecutorService) {
    paramExecutorService.shutdownNow();
    try {
      if (!paramExecutorService.awaitTermination(5L, TimeUnit.SECONDS)) {
        paramExecutorService.shutdownNow();
        if (!paramExecutorService.awaitTermination(5L, TimeUnit.SECONDS)) {
          RuntimeException runtimeException = new RuntimeException();
          this("Failed to shutdown");
          throw runtimeException;
        } 
      } 
      return;
    } catch (InterruptedException interruptedException) {
      paramExecutorService.shutdownNow();
      Thread.currentThread().interrupt();
      throw new RuntimeException(interruptedException);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/util/Executors.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */