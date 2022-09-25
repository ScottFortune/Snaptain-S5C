package io.reactivex.internal.schedulers;

import io.reactivex.Scheduler;

public interface SchedulerMultiWorkerSupport {
  void createWorkers(int paramInt, WorkerCallback paramWorkerCallback);
  
  public static interface WorkerCallback {
    void onWorker(int param1Int, Scheduler.Worker param1Worker);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/schedulers/SchedulerMultiWorkerSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */