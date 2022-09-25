package com.vilyever.resource;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import androidx.core.content.ContextCompat;
import com.vilyever.contextholder.ContextHolder;
import java.io.File;

public class Resource {
  final Resource self = this;
  
  public static boolean getBoolean(int paramInt) {
    return ContextHolder.getContext().getResources().getBoolean(paramInt);
  }
  
  public static int getColor(int paramInt) {
    return ContextCompat.getColor(ContextHolder.getContext(), paramInt);
  }
  
  public static ColorStateList getColorStateList(int paramInt) {
    return ContextCompat.getColorStateList(ContextHolder.getContext(), paramInt);
  }
  
  public static float getDimension(int paramInt) {
    return ContextHolder.getContext().getResources().getDimension(paramInt);
  }
  
  public static int getDimensionPixelSize(int paramInt) {
    return ContextHolder.getContext().getResources().getDimensionPixelSize(paramInt);
  }
  
  public static DisplayMetrics getDisplayMetrics() {
    return ContextHolder.getContext().getResources().getDisplayMetrics();
  }
  
  public static Drawable getDrawable(int paramInt) {
    return ContextCompat.getDrawable(ContextHolder.getContext(), paramInt);
  }
  
  public static File[] getExternalCacheDirs() {
    return ContextCompat.getExternalCacheDirs(ContextHolder.getContext());
  }
  
  public static File[] getExternalFilesDirs(String paramString) {
    return ContextCompat.getExternalFilesDirs(ContextHolder.getContext(), paramString);
  }
  
  public static int[] getIntArray(int paramInt) {
    return ContextHolder.getContext().getResources().getIntArray(paramInt);
  }
  
  public static int getInteger(int paramInt) {
    return ContextHolder.getContext().getResources().getInteger(paramInt);
  }
  
  public static File[] getObbDirs() {
    return ContextCompat.getObbDirs(ContextHolder.getContext());
  }
  
  public static Resources getResources() {
    return ContextHolder.getContext().getResources();
  }
  
  public static String getString(int paramInt) {
    return ContextHolder.getContext().getResources().getString(paramInt);
  }
  
  public static String getString(int paramInt, Object... paramVarArgs) {
    return ContextHolder.getContext().getResources().getString(paramInt, paramVarArgs);
  }
  
  public static String[] getStringArray(int paramInt) {
    return ContextHolder.getContext().getResources().getStringArray(paramInt);
  }
  
  public static TypedArray obtainAttributes(AttributeSet paramAttributeSet, int[] paramArrayOfint) {
    return ContextHolder.getContext().getResources().obtainAttributes(paramAttributeSet, paramArrayOfint);
  }
  
  public static TypedArray obtainTypedArray(int paramInt) {
    return ContextHolder.getContext().getResources().obtainTypedArray(paramInt);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/vilyever/resource/Resource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */