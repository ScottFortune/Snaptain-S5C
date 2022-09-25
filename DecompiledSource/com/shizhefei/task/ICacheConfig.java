package com.shizhefei.task;

public interface ICacheConfig<DATA> {
  String getTaskKey(Object paramObject);
  
  boolean isNeedSave(Object paramObject, long paramLong1, long paramLong2, DATA paramDATA);
  
  boolean isUsefulCacheData(Object paramObject, long paramLong1, long paramLong2, DATA paramDATA);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/task/ICacheConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */