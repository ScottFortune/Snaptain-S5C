package com.shizhefei.task;

import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;

public interface IAsyncTask<DATA> extends ISuperTask<DATA> {
  RequestHandle execute(ResponseSender<DATA> paramResponseSender) throws Exception;
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/task/IAsyncTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */