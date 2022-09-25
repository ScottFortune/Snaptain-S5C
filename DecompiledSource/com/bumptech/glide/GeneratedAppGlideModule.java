package com.bumptech.glide;

import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.module.AppGlideModule;
import java.util.Set;

abstract class GeneratedAppGlideModule extends AppGlideModule {
  abstract Set<Class<?>> getExcludedModuleClasses();
  
  RequestManagerRetriever.RequestManagerFactory getRequestManagerFactory() {
    return null;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/GeneratedAppGlideModule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */