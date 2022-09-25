package com.bumptech.glide.load.resource.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

public final class DrawableDecoderCompat {
  private static volatile boolean shouldCallAppCompatResources = true;
  
  public static Drawable getDrawable(Context paramContext, int paramInt, Resources.Theme paramTheme) {
    return getDrawable(paramContext, paramContext, paramInt, paramTheme);
  }
  
  public static Drawable getDrawable(Context paramContext1, Context paramContext2, int paramInt) {
    return getDrawable(paramContext1, paramContext2, paramInt, null);
  }
  
  private static Drawable getDrawable(Context paramContext1, Context paramContext2, int paramInt, Resources.Theme paramTheme) {
    Resources.Theme theme;
    try {
      if (shouldCallAppCompatResources)
        return loadDrawableV7(paramContext2, paramInt, paramTheme); 
    } catch (NoClassDefFoundError noClassDefFoundError) {
      shouldCallAppCompatResources = false;
    } catch (IllegalStateException illegalStateException) {
      if (!noClassDefFoundError.getPackageName().equals(paramContext2.getPackageName()))
        return ContextCompat.getDrawable(paramContext2, paramInt); 
      throw illegalStateException;
    } catch (android.content.res.Resources.NotFoundException notFoundException) {}
    if (illegalStateException == null)
      theme = paramContext2.getTheme(); 
    return loadDrawableV4(paramContext2, paramInt, theme);
  }
  
  private static Drawable loadDrawableV4(Context paramContext, int paramInt, Resources.Theme paramTheme) {
    return ResourcesCompat.getDrawable(paramContext.getResources(), paramInt, paramTheme);
  }
  
  private static Drawable loadDrawableV7(Context paramContext, int paramInt, Resources.Theme paramTheme) {
    ContextThemeWrapper contextThemeWrapper;
    Context context = paramContext;
    if (paramTheme != null)
      contextThemeWrapper = new ContextThemeWrapper(paramContext, paramTheme); 
    return AppCompatResources.getDrawable((Context)contextThemeWrapper, paramInt);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/drawable/DrawableDecoderCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */