package com.stx.xhb.xbanner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import androidx.core.view.ViewCompat;
import java.util.List;

public class XBannerUtils {
  public static int dp2px(Context paramContext, float paramFloat) {
    return (int)TypedValue.applyDimension(1, paramFloat, paramContext.getResources().getDisplayMetrics());
  }
  
  public static Drawable getDrawable(Context paramContext, int paramInt) {
    return (Build.VERSION.SDK_INT >= 21) ? paramContext.getDrawable(paramInt) : paramContext.getResources().getDrawable(paramInt);
  }
  
  public static int getScreenWidth(Context paramContext) {
    return (paramContext.getResources().getDisplayMetrics()).widthPixels;
  }
  
  public static StateListDrawable getSelector(Drawable paramDrawable1, Drawable paramDrawable2) {
    StateListDrawable stateListDrawable = new StateListDrawable();
    stateListDrawable.addState(new int[] { 16842910 }, paramDrawable2);
    stateListDrawable.addState(new int[0], paramDrawable1);
    return stateListDrawable;
  }
  
  public static void resetPageTransformer(List<? extends View> paramList) {
    if (paramList == null)
      return; 
    for (View view : paramList) {
      view.setVisibility(0);
      ViewCompat.setAlpha(view, 1.0F);
      ViewCompat.setPivotX(view, view.getMeasuredWidth() * 0.5F);
      ViewCompat.setPivotY(view, view.getMeasuredHeight() * 0.5F);
      ViewCompat.setTranslationX(view, 0.0F);
      ViewCompat.setTranslationY(view, 0.0F);
      ViewCompat.setScaleX(view, 1.0F);
      ViewCompat.setScaleY(view, 1.0F);
      ViewCompat.setRotationX(view, 0.0F);
      ViewCompat.setRotationY(view, 0.0F);
      ViewCompat.setRotation(view, 0.0F);
    } 
  }
  
  public static int sp2px(Context paramContext, float paramFloat) {
    return (int)TypedValue.applyDimension(2, paramFloat, paramContext.getResources().getDisplayMetrics());
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/stx/xhb/xbanner/XBannerUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */