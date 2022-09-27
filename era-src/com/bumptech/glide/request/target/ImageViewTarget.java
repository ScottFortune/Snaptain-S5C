package com.bumptech.glide.request.target;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.bumptech.glide.request.transition.Transition;

public abstract class ImageViewTarget<Z> extends ViewTarget<ImageView, Z> implements Transition.ViewAdapter {
  private Animatable animatable;
  
  public ImageViewTarget(ImageView paramImageView) {
    super(paramImageView);
  }
  
  @Deprecated
  public ImageViewTarget(ImageView paramImageView, boolean paramBoolean) {
    super(paramImageView, paramBoolean);
  }
  
  private void maybeUpdateAnimatable(Z paramZ) {
    if (paramZ instanceof Animatable) {
      this.animatable = (Animatable)paramZ;
      this.animatable.start();
    } else {
      this.animatable = null;
    } 
  }
  
  private void setResourceInternal(Z paramZ) {
    setResource(paramZ);
    maybeUpdateAnimatable(paramZ);
  }
  
  public Drawable getCurrentDrawable() {
    return this.view.getDrawable();
  }
  
  public void onLoadCleared(Drawable paramDrawable) {
    super.onLoadCleared(paramDrawable);
    Animatable animatable = this.animatable;
    if (animatable != null)
      animatable.stop(); 
    setResourceInternal(null);
    setDrawable(paramDrawable);
  }
  
  public void onLoadFailed(Drawable paramDrawable) {
    super.onLoadFailed(paramDrawable);
    setResourceInternal(null);
    setDrawable(paramDrawable);
  }
  
  public void onLoadStarted(Drawable paramDrawable) {
    super.onLoadStarted(paramDrawable);
    setResourceInternal(null);
    setDrawable(paramDrawable);
  }
  
  public void onResourceReady(Z paramZ, Transition<? super Z> paramTransition) {
    if (paramTransition == null || !paramTransition.transition(paramZ, this)) {
      setResourceInternal(paramZ);
      return;
    } 
    maybeUpdateAnimatable(paramZ);
  }
  
  public void onStart() {
    Animatable animatable = this.animatable;
    if (animatable != null)
      animatable.start(); 
  }
  
  public void onStop() {
    Animatable animatable = this.animatable;
    if (animatable != null)
      animatable.stop(); 
  }
  
  public void setDrawable(Drawable paramDrawable) {
    this.view.setImageDrawable(paramDrawable);
  }
  
  protected abstract void setResource(Z paramZ);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/target/ImageViewTarget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */