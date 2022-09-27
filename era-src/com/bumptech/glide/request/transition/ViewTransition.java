package com.bumptech.glide.request.transition;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

public class ViewTransition<R> implements Transition<R> {
  private final ViewTransitionAnimationFactory viewTransitionAnimationFactory;
  
  ViewTransition(ViewTransitionAnimationFactory paramViewTransitionAnimationFactory) {
    this.viewTransitionAnimationFactory = paramViewTransitionAnimationFactory;
  }
  
  public boolean transition(R paramR, Transition.ViewAdapter paramViewAdapter) {
    View view = paramViewAdapter.getView();
    if (view != null) {
      view.clearAnimation();
      view.startAnimation(this.viewTransitionAnimationFactory.build(view.getContext()));
    } 
    return false;
  }
  
  static interface ViewTransitionAnimationFactory {
    Animation build(Context param1Context);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/transition/ViewTransition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */