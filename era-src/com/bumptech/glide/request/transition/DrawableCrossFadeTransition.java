package com.bumptech.glide.request.transition;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;

public class DrawableCrossFadeTransition implements Transition<Drawable> {
  private final int duration;
  
  private final boolean isCrossFadeEnabled;
  
  public DrawableCrossFadeTransition(int paramInt, boolean paramBoolean) {
    this.duration = paramInt;
    this.isCrossFadeEnabled = paramBoolean;
  }
  
  public boolean transition(Drawable paramDrawable, Transition.ViewAdapter paramViewAdapter) {
    ColorDrawable colorDrawable;
    Drawable drawable1 = paramViewAdapter.getCurrentDrawable();
    Drawable drawable2 = drawable1;
    if (drawable1 == null)
      colorDrawable = new ColorDrawable(0); 
    TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[] { (Drawable)colorDrawable, paramDrawable });
    transitionDrawable.setCrossFadeEnabled(this.isCrossFadeEnabled);
    transitionDrawable.startTransition(this.duration);
    paramViewAdapter.setDrawable((Drawable)transitionDrawable);
    return true;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/transition/DrawableCrossFadeTransition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */