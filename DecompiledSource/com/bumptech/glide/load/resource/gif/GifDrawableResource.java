package com.bumptech.glide.load.resource.gif;

import com.bumptech.glide.load.engine.Initializable;
import com.bumptech.glide.load.resource.drawable.DrawableResource;

public class GifDrawableResource extends DrawableResource<GifDrawable> implements Initializable {
  public GifDrawableResource(GifDrawable paramGifDrawable) {
    super(paramGifDrawable);
  }
  
  public Class<GifDrawable> getResourceClass() {
    return GifDrawable.class;
  }
  
  public int getSize() {
    return ((GifDrawable)this.drawable).getSize();
  }
  
  public void initialize() {
    ((GifDrawable)this.drawable).getFirstFrame().prepareToDraw();
  }
  
  public void recycle() {
    ((GifDrawable)this.drawable).stop();
    ((GifDrawable)this.drawable).recycle();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/gif/GifDrawableResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */