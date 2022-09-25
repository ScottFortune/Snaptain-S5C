package com.bumptech.glide.request.transition;

import android.graphics.drawable.Drawable;
import android.view.View;

public interface Transition<R> {
  boolean transition(R paramR, ViewAdapter paramViewAdapter);
  
  public static interface ViewAdapter {
    Drawable getCurrentDrawable();
    
    View getView();
    
    void setDrawable(Drawable param1Drawable);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/transition/Transition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */