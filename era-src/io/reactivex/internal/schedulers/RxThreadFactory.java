package io.reactivex.internal.schedulers;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public final class RxThreadFactory extends AtomicLong implements ThreadFactory {
  private static final long serialVersionUID = -7789753024099756196L;
  
  final boolean nonBlocking;
  
  final String prefix;
  
  final int priority;
  
  public RxThreadFactory(String paramString) {
    this(paramString, 5, false);
  }
  
  public RxThreadFactory(String paramString, int paramInt) {
    this(paramString, paramInt, false);
  }
  
  public RxThreadFactory(String paramString, int paramInt, boolean paramBoolean) {
    this.prefix = paramString;
    this.priority = paramInt;
    this.nonBlocking = paramBoolean;
  }
  
  public Thread newThread(Runnable paramRunnable) {
    StringBuilder stringBuilder = new StringBuilder(this.prefix);
    stringBuilder.append('-');
    stringBuilder.append(incrementAndGet());
    String str = stringBuilder.toString();
    if (this.nonBlocking) {
      paramRunnable = new RxCustomThread(paramRunnable, str);
    } else {
      paramRunnable = new Thread(paramRunnable, str);
    } 
    paramRunnable.setPriority(this.priority);
    paramRunnable.setDaemon(true);
    return (Thread)paramRunnable;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("RxThreadFactory[");
    stringBuilder.append(this.prefix);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
  
  static final class RxCustomThread extends Thread implements NonBlockingThread {
    RxCustomThread(Runnable param1Runnable, String param1String) {
      super(param1Runnable, param1String);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/schedulers/RxThreadFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */