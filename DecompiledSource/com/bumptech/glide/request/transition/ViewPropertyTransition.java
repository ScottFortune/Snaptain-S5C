package com.bumptech.glide.request.transition;

import android.view.View;

public class ViewPropertyTransition<R> implements Transition<R> {
  private final Animator animator;
  
  public ViewPropertyTransition(Animator paramAnimator) {
    this.animator = paramAnimator;
  }
  
  public boolean transition(R paramR, Transition.ViewAdapter paramViewAdapter) {
    if (paramViewAdapter.getView() != null)
      this.animator.animate(paramViewAdapter.getView()); 
    return false;
  }
  
  public static interface Animator {
    void animate(View param1View);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/transition/ViewPropertyTransition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */