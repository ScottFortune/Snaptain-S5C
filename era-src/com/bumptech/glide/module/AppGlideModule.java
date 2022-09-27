package com.bumptech.glide.module;

import android.content.Context;
import com.bumptech.glide.GlideBuilder;

public abstract class AppGlideModule extends LibraryGlideModule implements AppliesOptions {
  public void applyOptions(Context paramContext, GlideBuilder paramGlideBuilder) {}
  
  public boolean isManifestParsingEnabled() {
    return true;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/module/AppGlideModule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */