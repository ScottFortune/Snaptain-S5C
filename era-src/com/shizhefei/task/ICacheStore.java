package com.shizhefei.task;

public interface ICacheStore {
  Cache getCache(String paramString);
  
  void saveCache(String paramString, long paramLong1, long paramLong2, Object paramObject);
  
  public static class Cache {
    public Object data;
    
    public long requestTime;
    
    public long saveTime;
    
    public Cache() {}
    
    public Cache(long param1Long1, long param1Long2, Object param1Object) {
      this.requestTime = param1Long1;
      this.saveTime = param1Long2;
      this.data = param1Object;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/task/ICacheStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */