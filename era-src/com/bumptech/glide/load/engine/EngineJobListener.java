package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;

interface EngineJobListener {
  void onEngineJobCancelled(EngineJob<?> paramEngineJob, Key paramKey);
  
  void onEngineJobComplete(EngineJob<?> paramEngineJob, Key paramKey, EngineResource<?> paramEngineResource);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/EngineJobListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */