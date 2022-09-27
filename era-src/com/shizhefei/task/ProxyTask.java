package com.shizhefei.task;

import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;

public abstract class ProxyTask<DATA> implements IAsyncTask<DATA> {
  public final RequestHandle execute(ResponseSender<DATA> paramResponseSender) throws Exception {
    return getTask().execute(paramResponseSender);
  }
  
  protected abstract IAsyncTask<DATA> getTask();
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/task/ProxyTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */