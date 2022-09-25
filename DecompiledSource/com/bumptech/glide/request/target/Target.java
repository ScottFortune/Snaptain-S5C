package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.manager.LifecycleListener;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.transition.Transition;

public interface Target<R> extends LifecycleListener {
  public static final int SIZE_ORIGINAL = -2147483648;
  
  Request getRequest();
  
  void getSize(SizeReadyCallback paramSizeReadyCallback);
  
  void onLoadCleared(Drawable paramDrawable);
  
  void onLoadFailed(Drawable paramDrawable);
  
  void onLoadStarted(Drawable paramDrawable);
  
  void onResourceReady(R paramR, Transition<? super R> paramTransition);
  
  void removeCallback(SizeReadyCallback paramSizeReadyCallback);
  
  void setRequest(Request paramRequest);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/target/Target.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */