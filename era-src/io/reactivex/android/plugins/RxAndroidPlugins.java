package io.reactivex.android.plugins;

import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import java.util.concurrent.Callable;

public final class RxAndroidPlugins {
  private static volatile Function<Callable<Scheduler>, Scheduler> onInitMainThreadHandler;
  
  private static volatile Function<Scheduler, Scheduler> onMainThreadHandler;
  
  private RxAndroidPlugins() {
    throw new AssertionError("No instances.");
  }
  
  static <T, R> R apply(Function<T, R> paramFunction, T paramT) {
    try {
      return (R)paramFunction.apply(paramT);
    } finally {
      paramFunction = null;
    } 
  }
  
  static Scheduler applyRequireNonNull(Function<Callable<Scheduler>, Scheduler> paramFunction, Callable<Scheduler> paramCallable) {
    Scheduler scheduler = apply(paramFunction, paramCallable);
    if (scheduler != null)
      return scheduler; 
    throw new NullPointerException("Scheduler Callable returned null");
  }
  
  static Scheduler callRequireNonNull(Callable<Scheduler> paramCallable) {
    try {
      Scheduler scheduler = paramCallable.call();
      if (scheduler != null)
        return scheduler; 
      NullPointerException nullPointerException = new NullPointerException();
      this("Scheduler Callable returned null");
      throw nullPointerException;
    } finally {
      paramCallable = null;
    } 
  }
  
  public static Function<Callable<Scheduler>, Scheduler> getInitMainThreadSchedulerHandler() {
    return onInitMainThreadHandler;
  }
  
  public static Function<Scheduler, Scheduler> getOnMainThreadSchedulerHandler() {
    return onMainThreadHandler;
  }
  
  public static Scheduler initMainThreadScheduler(Callable<Scheduler> paramCallable) {
    if (paramCallable != null) {
      Function<Callable<Scheduler>, Scheduler> function = onInitMainThreadHandler;
      return (function == null) ? callRequireNonNull(paramCallable) : applyRequireNonNull(function, paramCallable);
    } 
    throw new NullPointerException("scheduler == null");
  }
  
  public static Scheduler onMainThreadScheduler(Scheduler paramScheduler) {
    if (paramScheduler != null) {
      Function<Scheduler, Scheduler> function = onMainThreadHandler;
      return (function == null) ? paramScheduler : apply(function, paramScheduler);
    } 
    throw new NullPointerException("scheduler == null");
  }
  
  public static void reset() {
    setInitMainThreadSchedulerHandler(null);
    setMainThreadSchedulerHandler(null);
  }
  
  public static void setInitMainThreadSchedulerHandler(Function<Callable<Scheduler>, Scheduler> paramFunction) {
    onInitMainThreadHandler = paramFunction;
  }
  
  public static void setMainThreadSchedulerHandler(Function<Scheduler, Scheduler> paramFunction) {
    onMainThreadHandler = paramFunction;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/android/plugins/RxAndroidPlugins.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */