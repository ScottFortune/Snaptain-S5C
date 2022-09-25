package com.bumptech.glide.request;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Resource;

public interface ResourceCallback {
  Object getLock();
  
  void onLoadFailed(GlideException paramGlideException);
  
  void onResourceReady(Resource<?> paramResource, DataSource paramDataSource);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/ResourceCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */