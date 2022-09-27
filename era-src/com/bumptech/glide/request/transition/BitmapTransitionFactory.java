package com.bumptech.glide.request.transition;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class BitmapTransitionFactory extends BitmapContainerTransitionFactory<Bitmap> {
  public BitmapTransitionFactory(TransitionFactory<Drawable> paramTransitionFactory) {
    super(paramTransitionFactory);
  }
  
  protected Bitmap getBitmap(Bitmap paramBitmap) {
    return paramBitmap;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/transition/BitmapTransitionFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */