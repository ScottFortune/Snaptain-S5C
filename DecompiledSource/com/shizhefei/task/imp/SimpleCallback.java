package com.shizhefei.task.imp;

import com.shizhefei.task.ICallback;

public abstract class SimpleCallback<DATA> implements ICallback<DATA> {
  public void onPreExecute(Object paramObject) {}
  
  public void onProgress(Object paramObject1, int paramInt, long paramLong1, long paramLong2, Object paramObject2) {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/task/imp/SimpleCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */