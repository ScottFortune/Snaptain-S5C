package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.request.transition.BitmapTransitionFactory;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.TransitionFactory;

public final class BitmapTransitionOptions extends TransitionOptions<BitmapTransitionOptions, Bitmap> {
  public static BitmapTransitionOptions with(TransitionFactory<Bitmap> paramTransitionFactory) {
    return (BitmapTransitionOptions)(new BitmapTransitionOptions()).transition(paramTransitionFactory);
  }
  
  public static BitmapTransitionOptions withCrossFade() {
    return (new BitmapTransitionOptions()).crossFade();
  }
  
  public static BitmapTransitionOptions withCrossFade(int paramInt) {
    return (new BitmapTransitionOptions()).crossFade(paramInt);
  }
  
  public static BitmapTransitionOptions withCrossFade(DrawableCrossFadeFactory.Builder paramBuilder) {
    return (new BitmapTransitionOptions()).crossFade(paramBuilder);
  }
  
  public static BitmapTransitionOptions withCrossFade(DrawableCrossFadeFactory paramDrawableCrossFadeFactory) {
    return (new BitmapTransitionOptions()).crossFade(paramDrawableCrossFadeFactory);
  }
  
  public static BitmapTransitionOptions withWrapped(TransitionFactory<Drawable> paramTransitionFactory) {
    return (new BitmapTransitionOptions()).transitionUsing(paramTransitionFactory);
  }
  
  public BitmapTransitionOptions crossFade() {
    return crossFade(new DrawableCrossFadeFactory.Builder());
  }
  
  public BitmapTransitionOptions crossFade(int paramInt) {
    return crossFade(new DrawableCrossFadeFactory.Builder(paramInt));
  }
  
  public BitmapTransitionOptions crossFade(DrawableCrossFadeFactory.Builder paramBuilder) {
    return transitionUsing((TransitionFactory<Drawable>)paramBuilder.build());
  }
  
  public BitmapTransitionOptions crossFade(DrawableCrossFadeFactory paramDrawableCrossFadeFactory) {
    return transitionUsing((TransitionFactory<Drawable>)paramDrawableCrossFadeFactory);
  }
  
  public BitmapTransitionOptions transitionUsing(TransitionFactory<Drawable> paramTransitionFactory) {
    return (BitmapTransitionOptions)transition((TransitionFactory)new BitmapTransitionFactory(paramTransitionFactory));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bitmap/BitmapTransitionOptions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */