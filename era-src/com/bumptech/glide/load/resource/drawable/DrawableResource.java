package com.bumptech.glide.load.resource.drawable;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.engine.Initializable;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.util.Preconditions;

public abstract class DrawableResource<T extends Drawable> implements Resource<T>, Initializable {
  protected final T drawable;
  
  public DrawableResource(T paramT) {
    this.drawable = (T)Preconditions.checkNotNull(paramT);
  }
  
  public final T get() {
    Drawable.ConstantState constantState = this.drawable.getConstantState();
    return (T)((constantState == null) ? (Object)this.drawable : constantState.newDrawable());
  }
  
  public void initialize() {
    T t = this.drawable;
    if (t instanceof BitmapDrawable) {
      ((BitmapDrawable)t).getBitmap().prepareToDraw();
    } else if (t instanceof GifDrawable) {
      ((GifDrawable)t).getFirstFrame().prepareToDraw();
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/drawable/DrawableResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */