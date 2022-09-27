package androidx.arch.core.executor;

public abstract class TaskExecutor {
  public abstract void executeOnDiskIO(Runnable paramRunnable);
  
  public void executeOnMainThread(Runnable paramRunnable) {
    if (isMainThread()) {
      paramRunnable.run();
    } else {
      postToMainThread(paramRunnable);
    } 
  }
  
  public abstract boolean isMainThread();
  
  public abstract void postToMainThread(Runnable paramRunnable);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/arch/core/executor/TaskExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */