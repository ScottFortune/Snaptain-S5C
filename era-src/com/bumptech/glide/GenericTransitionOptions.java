package com.bumptech.glide;

import com.bumptech.glide.request.transition.TransitionFactory;
import com.bumptech.glide.request.transition.ViewPropertyTransition;

public final class GenericTransitionOptions<TranscodeType> extends TransitionOptions<GenericTransitionOptions<TranscodeType>, TranscodeType> {
  public static <TranscodeType> GenericTransitionOptions<TranscodeType> with(int paramInt) {
    return (new GenericTransitionOptions<TranscodeType>()).transition(paramInt);
  }
  
  public static <TranscodeType> GenericTransitionOptions<TranscodeType> with(TransitionFactory<? super TranscodeType> paramTransitionFactory) {
    return (new GenericTransitionOptions<TranscodeType>()).transition(paramTransitionFactory);
  }
  
  public static <TranscodeType> GenericTransitionOptions<TranscodeType> with(ViewPropertyTransition.Animator paramAnimator) {
    return (new GenericTransitionOptions<TranscodeType>()).transition(paramAnimator);
  }
  
  public static <TranscodeType> GenericTransitionOptions<TranscodeType> withNoTransition() {
    return (new GenericTransitionOptions<TranscodeType>()).dontTransition();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/GenericTransitionOptions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */