package com.bumptech.glide.load;

import android.content.Context;
import com.bumptech.glide.load.engine.Resource;

public interface Transformation<T> extends Key {
  Resource<T> transform(Context paramContext, Resource<T> paramResource, int paramInt1, int paramInt2);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/Transformation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */