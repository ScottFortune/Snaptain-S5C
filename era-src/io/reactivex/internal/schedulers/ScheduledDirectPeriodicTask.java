package io.reactivex.internal.schedulers;

public final class ScheduledDirectPeriodicTask extends AbstractDirectTask implements Runnable {
  private static final long serialVersionUID = 1811839108042568751L;
  
  public ScheduledDirectPeriodicTask(Runnable paramRunnable) {
    super(paramRunnable);
  }
  
  public void run() {
    this.runner = Thread.currentThread();
    try {
      this.runnable.run();
      this.runner = null;
    } finally {
      Exception exception = null;
      this.runner = null;
      lazySet(FINISHED);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/schedulers/ScheduledDirectPeriodicTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */