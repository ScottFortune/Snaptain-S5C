package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.request.Request;

@Deprecated
public abstract class BaseTarget<Z> implements Target<Z> {
  private Request request;
  
  public Request getRequest() {
    return this.request;
  }
  
  public void onDestroy() {}
  
  public void onLoadCleared(Drawable paramDrawable) {}
  
  public void onLoadFailed(Drawable paramDrawable) {}
  
  public void onLoadStarted(Drawable paramDrawable) {}
  
  public void onStart() {}
  
  public void onStop() {}
  
  public void setRequest(Request paramRequest) {
    this.request = paramRequest;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/target/BaseTarget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */