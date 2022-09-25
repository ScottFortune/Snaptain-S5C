package com.bumptech.glide.load.resource.drawable;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import java.io.IOException;

public class UnitDrawableDecoder implements ResourceDecoder<Drawable, Drawable> {
  public Resource<Drawable> decode(Drawable paramDrawable, int paramInt1, int paramInt2, Options paramOptions) {
    return NonOwnedDrawableResource.newInstance(paramDrawable);
  }
  
  public boolean handles(Drawable paramDrawable, Options paramOptions) {
    return true;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/drawable/UnitDrawableDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */