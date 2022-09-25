package com.bumptech.glide;

import com.bumptech.glide.request.transition.NoTransition;
import com.bumptech.glide.request.transition.TransitionFactory;
import com.bumptech.glide.request.transition.ViewAnimationFactory;
import com.bumptech.glide.request.transition.ViewPropertyAnimationFactory;
import com.bumptech.glide.request.transition.ViewPropertyTransition;
import com.bumptech.glide.util.Preconditions;

public abstract class TransitionOptions<CHILD extends TransitionOptions<CHILD, TranscodeType>, TranscodeType> implements Cloneable {
  private TransitionFactory<? super TranscodeType> transitionFactory = NoTransition.getFactory();
  
  private CHILD self() {
    return (CHILD)this;
  }
  
  public final CHILD clone() {
    try {
      return (CHILD)super.clone();
    } catch (CloneNotSupportedException cloneNotSupportedException) {
      throw new RuntimeException(cloneNotSupportedException);
    } 
  }
  
  public final CHILD dontTransition() {
    return transition(NoTransition.getFactory());
  }
  
  final TransitionFactory<? super TranscodeType> getTransitionFactory() {
    return this.transitionFactory;
  }
  
  public final CHILD transition(int paramInt) {
    return transition((TransitionFactory<? super TranscodeType>)new ViewAnimationFactory(paramInt));
  }
  
  public final CHILD transition(TransitionFactory<? super TranscodeType> paramTransitionFactory) {
    this.transitionFactory = (TransitionFactory<? super TranscodeType>)Preconditions.checkNotNull(paramTransitionFactory);
    return self();
  }
  
  public final CHILD transition(ViewPropertyTransition.Animator paramAnimator) {
    return transition((TransitionFactory<? super TranscodeType>)new ViewPropertyAnimationFactory(paramAnimator));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/TransitionOptions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */