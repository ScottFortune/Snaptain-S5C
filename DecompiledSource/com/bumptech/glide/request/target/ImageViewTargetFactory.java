package com.bumptech.glide.request.target;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ImageViewTargetFactory {
  public <Z> ViewTarget<ImageView, Z> buildTarget(ImageView paramImageView, Class<Z> paramClass) {
    if (Bitmap.class.equals(paramClass))
      return new BitmapImageViewTarget(paramImageView); 
    if (Drawable.class.isAssignableFrom(paramClass))
      return new DrawableImageViewTarget(paramImageView); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Unhandled class: ");
    stringBuilder.append(paramClass);
    stringBuilder.append(", try .as*(Class).transcode(ResourceTranscoder)");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/target/ImageViewTargetFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */