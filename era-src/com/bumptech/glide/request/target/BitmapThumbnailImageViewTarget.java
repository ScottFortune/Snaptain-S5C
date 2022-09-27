package com.bumptech.glide.request.target;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class BitmapThumbnailImageViewTarget extends ThumbnailImageViewTarget<Bitmap> {
  public BitmapThumbnailImageViewTarget(ImageView paramImageView) {
    super(paramImageView);
  }
  
  @Deprecated
  public BitmapThumbnailImageViewTarget(ImageView paramImageView, boolean paramBoolean) {
    super(paramImageView, paramBoolean);
  }
  
  protected Drawable getDrawable(Bitmap paramBitmap) {
    return (Drawable)new BitmapDrawable(this.view.getResources(), paramBitmap);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/target/BitmapThumbnailImageViewTarget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */