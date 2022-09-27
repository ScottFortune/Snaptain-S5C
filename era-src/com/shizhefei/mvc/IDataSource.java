package com.shizhefei.mvc;

import com.shizhefei.task.ISuperTask;

public interface IDataSource<DATA> extends ISuperTask<DATA> {
  boolean hasMore();
  
  DATA loadMore() throws Exception;
  
  DATA refresh() throws Exception;
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/mvc/IDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */