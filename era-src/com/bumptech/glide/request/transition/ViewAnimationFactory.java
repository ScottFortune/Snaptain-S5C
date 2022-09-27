package com.bumptech.glide.request.transition;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.bumptech.glide.load.DataSource;

public class ViewAnimationFactory<R> implements TransitionFactory<R> {
  private Transition<R> transition;
  
  private final ViewTransition.ViewTransitionAnimationFactory viewTransitionAnimationFactory;
  
  public ViewAnimationFactory(int paramInt) {
    this(new ResourceViewTransitionAnimationFactory(paramInt));
  }
  
  public ViewAnimationFactory(Animation paramAnimation) {
    this(new ConcreteViewTransitionAnimationFactory(paramAnimation));
  }
  
  ViewAnimationFactory(ViewTransition.ViewTransitionAnimationFactory paramViewTransitionAnimationFactory) {
    this.viewTransitionAnimationFactory = paramViewTransitionAnimationFactory;
  }
  
  public Transition<R> build(DataSource paramDataSource, boolean paramBoolean) {
    if (paramDataSource == DataSource.MEMORY_CACHE || !paramBoolean)
      return NoTransition.get(); 
    if (this.transition == null)
      this.transition = new ViewTransition<R>(this.viewTransitionAnimationFactory); 
    return this.transition;
  }
  
  private static class ConcreteViewTransitionAnimationFactory implements ViewTransition.ViewTransitionAnimationFactory {
    private final Animation animation;
    
    ConcreteViewTransitionAnimationFactory(Animation param1Animation) {
      this.animation = param1Animation;
    }
    
    public Animation build(Context param1Context) {
      return this.animation;
    }
  }
  
  private static class ResourceViewTransitionAnimationFactory implements ViewTransition.ViewTransitionAnimationFactory {
    private final int animationId;
    
    ResourceViewTransitionAnimationFactory(int param1Int) {
      this.animationId = param1Int;
    }
    
    public Animation build(Context param1Context) {
      return AnimationUtils.loadAnimation(param1Context, this.animationId);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/transition/ViewAnimationFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */