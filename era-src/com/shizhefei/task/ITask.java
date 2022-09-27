package com.shizhefei.task;

import com.shizhefei.mvc.ProgressSender;

public interface ITask<DATA> extends ISuperTask<DATA> {
  void cancel();
  
  DATA execute(ProgressSender paramProgressSender) throws Exception;
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/task/ITask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */