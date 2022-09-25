package com.shizhefei.task;

public interface ICallback<DATA> {
  void onPostExecute(Object paramObject, Code paramCode, Exception paramException, DATA paramDATA);
  
  void onPreExecute(Object paramObject);
  
  void onProgress(Object paramObject1, int paramInt, long paramLong1, long paramLong2, Object paramObject2);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/task/ICallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */