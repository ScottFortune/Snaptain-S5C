package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class DrawableImageViewTarget extends ImageViewTarget<Drawable> {
  public DrawableImageViewTarget(ImageView paramImageView) {
    super(paramImageView);
  }
  
  @Deprecated
  public DrawableImageViewTarget(ImageView paramImageView, boolean paramBoolean) {
    super(paramImageView, paramBoolean);
  }
  
  protected void setResource(Drawable paramDrawable) {
    this.view.setImageDrawable(paramDrawable);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/target/DrawableImageViewTarget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */