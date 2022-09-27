package com.bumptech.glide.manager;

import com.bumptech.glide.util.Util;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

class ActivityFragmentLifecycle implements Lifecycle {
  private boolean isDestroyed;
  
  private boolean isStarted;
  
  private final Set<LifecycleListener> lifecycleListeners = Collections.newSetFromMap(new WeakHashMap<LifecycleListener, Boolean>());
  
  public void addListener(LifecycleListener paramLifecycleListener) {
    this.lifecycleListeners.add(paramLifecycleListener);
    if (this.isDestroyed) {
      paramLifecycleListener.onDestroy();
    } else if (this.isStarted) {
      paramLifecycleListener.onStart();
    } else {
      paramLifecycleListener.onStop();
    } 
  }
  
  void onDestroy() {
    this.isDestroyed = true;
    Iterator<LifecycleListener> iterator = Util.getSnapshot(this.lifecycleListeners).iterator();
    while (iterator.hasNext())
      ((LifecycleListener)iterator.next()).onDestroy(); 
  }
  
  void onStart() {
    this.isStarted = true;
    Iterator<LifecycleListener> iterator = Util.getSnapshot(this.lifecycleListeners).iterator();
    while (iterator.hasNext())
      ((LifecycleListener)iterator.next()).onStart(); 
  }
  
  void onStop() {
    this.isStarted = false;
    Iterator<LifecycleListener> iterator = Util.getSnapshot(this.lifecycleListeners).iterator();
    while (iterator.hasNext())
      ((LifecycleListener)iterator.next()).onStop(); 
  }
  
  public void removeListener(LifecycleListener paramLifecycleListener) {
    this.lifecycleListeners.remove(paramLifecycleListener);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/manager/ActivityFragmentLifecycle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */