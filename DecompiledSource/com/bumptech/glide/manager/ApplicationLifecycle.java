package com.bumptech.glide.manager;

class ApplicationLifecycle implements Lifecycle {
  public void addListener(LifecycleListener paramLifecycleListener) {
    paramLifecycleListener.onStart();
  }
  
  public void removeListener(LifecycleListener paramLifecycleListener) {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/manager/ApplicationLifecycle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */