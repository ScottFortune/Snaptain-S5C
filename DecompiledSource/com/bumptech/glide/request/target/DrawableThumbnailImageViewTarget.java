package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class DrawableThumbnailImageViewTarget extends ThumbnailImageViewTarget<Drawable> {
  public DrawableThumbnailImageViewTarget(ImageView paramImageView) {
    super(paramImageView);
  }
  
  @Deprecated
  public DrawableThumbnailImageViewTarget(ImageView paramImageView, boolean paramBoolean) {
    super(paramImageView, paramBoolean);
  }
  
  protected Drawable getDrawable(Drawable paramDrawable) {
    return paramDrawable;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/target/DrawableThumbnailImageViewTarget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */