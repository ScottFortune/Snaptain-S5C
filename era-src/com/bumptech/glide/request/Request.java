package com.bumptech.glide.request;

public interface Request {
  void begin();
  
  void clear();
  
  boolean isCleared();
  
  boolean isComplete();
  
  boolean isEquivalentTo(Request paramRequest);
  
  boolean isRunning();
  
  void pause();
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/Request.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */