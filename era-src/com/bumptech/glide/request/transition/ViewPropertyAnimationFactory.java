package com.bumptech.glide.request.transition;

import com.bumptech.glide.load.DataSource;

public class ViewPropertyAnimationFactory<R> implements TransitionFactory<R> {
  private ViewPropertyTransition<R> animation;
  
  private final ViewPropertyTransition.Animator animator;
  
  public ViewPropertyAnimationFactory(ViewPropertyTransition.Animator paramAnimator) {
    this.animator = paramAnimator;
  }
  
  public Transition<R> build(DataSource paramDataSource, boolean paramBoolean) {
    if (paramDataSource == DataSource.MEMORY_CACHE || !paramBoolean)
      return NoTransition.get(); 
    if (this.animation == null)
      this.animation = new ViewPropertyTransition<R>(this.animator); 
    return this.animation;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/transition/ViewPropertyAnimationFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */