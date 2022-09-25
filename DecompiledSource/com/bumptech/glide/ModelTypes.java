package com.bumptech.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import java.io.File;
import java.net.URL;

interface ModelTypes<T> {
  T load(Bitmap paramBitmap);
  
  T load(Drawable paramDrawable);
  
  T load(Uri paramUri);
  
  T load(File paramFile);
  
  T load(Integer paramInteger);
  
  T load(Object paramObject);
  
  T load(String paramString);
  
  @Deprecated
  T load(URL paramURL);
  
  T load(byte[] paramArrayOfbyte);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/ModelTypes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */