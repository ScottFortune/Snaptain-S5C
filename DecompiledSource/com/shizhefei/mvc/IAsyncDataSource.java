package com.shizhefei.mvc;

import com.shizhefei.task.ISuperTask;

public interface IAsyncDataSource<DATA> extends ISuperTask<DATA> {
  boolean hasMore();
  
  RequestHandle loadMore(ResponseSender<DATA> paramResponseSender) throws Exception;
  
  RequestHandle refresh(ResponseSender<DATA> paramResponseSender) throws Exception;
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/mvc/IAsyncDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */