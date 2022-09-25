package com.bumptech.glide.request.target;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class BitmapImageViewTarget extends ImageViewTarget<Bitmap> {
  public BitmapImageViewTarget(ImageView paramImageView) {
    super(paramImageView);
  }
  
  @Deprecated
  public BitmapImageViewTarget(ImageView paramImageView, boolean paramBoolean) {
    super(paramImageView, paramBoolean);
  }
  
  protected void setResource(Bitmap paramBitmap) {
    this.view.setImageBitmap(paramBitmap);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/target/BitmapImageViewTarget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */