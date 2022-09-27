package com.shizhefei.mvc;

public interface ResponseSender<DATA> extends ProgressSender {
  void sendData(DATA paramDATA);
  
  void sendError(Exception paramException);
  
  void sendProgress(long paramLong1, long paramLong2, Object paramObject);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/mvc/ResponseSender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */