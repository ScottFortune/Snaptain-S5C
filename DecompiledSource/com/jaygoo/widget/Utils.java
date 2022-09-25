package com.jaygoo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import androidx.core.content.ContextCompat;

public class Utils {
  private static final String TAG = "RangeSeekBar";
  
  public static int compareFloat(float paramFloat1, float paramFloat2) {
    int i = Math.round(paramFloat1 * 1000000.0F);
    int j = Math.round(paramFloat2 * 1000000.0F);
    return (i > j) ? 1 : ((i < j) ? -1 : 0);
  }
  
  public static int compareFloat(float paramFloat1, float paramFloat2, int paramInt) {
    return (Math.abs(paramFloat1 - paramFloat2) < Math.pow(0.1D, paramInt)) ? 0 : ((paramFloat1 < paramFloat2) ? -1 : 1);
  }
  
  public static int dp2px(Context paramContext, float paramFloat) {
    return (paramContext == null || compareFloat(0.0F, paramFloat) == 0) ? 0 : (int)(paramFloat * (paramContext.getResources().getDisplayMetrics()).density + 0.5F);
  }
  
  public static void drawBitmap(Canvas paramCanvas, Paint paramPaint, Bitmap paramBitmap, Rect paramRect) {
    try {
      if (NinePatch.isNinePatchChunk(paramBitmap.getNinePatchChunk())) {
        drawNinePath(paramCanvas, paramBitmap, paramRect);
        return;
      } 
    } catch (Exception exception) {}
    paramCanvas.drawBitmap(paramBitmap, paramRect.left, paramRect.top, paramPaint);
  }
  
  public static void drawNinePath(Canvas paramCanvas, Bitmap paramBitmap, Rect paramRect) {
    NinePatch.isNinePatchChunk(paramBitmap.getNinePatchChunk());
    (new NinePatch(paramBitmap, paramBitmap.getNinePatchChunk(), null)).draw(paramCanvas, paramRect);
  }
  
  public static Bitmap drawableToBitmap(int paramInt1, int paramInt2, Drawable paramDrawable) {
    Bitmap bitmap1 = null;
    Bitmap bitmap2 = null;
    Bitmap bitmap3 = bitmap1;
    try {
      Matrix matrix;
      if (paramDrawable instanceof BitmapDrawable) {
        bitmap3 = bitmap1;
        bitmap1 = ((BitmapDrawable)paramDrawable).getBitmap();
        bitmap2 = bitmap1;
        if (bitmap1 != null) {
          bitmap2 = bitmap1;
          bitmap3 = bitmap1;
          if (bitmap1.getHeight() > 0) {
            bitmap3 = bitmap1;
            matrix = new Matrix();
            bitmap3 = bitmap1;
            this();
            bitmap3 = bitmap1;
            matrix.postScale(paramInt1 * 1.0F / bitmap1.getWidth(), paramInt2 * 1.0F / bitmap1.getHeight());
            bitmap3 = bitmap1;
            return Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), matrix, true);
          } 
        } 
      } 
      bitmap3 = bitmap2;
      bitmap2 = Bitmap.createBitmap(paramInt1, paramInt2, Bitmap.Config.ARGB_8888);
      bitmap3 = bitmap2;
      Canvas canvas = new Canvas();
      bitmap3 = bitmap2;
      this(bitmap2);
      bitmap3 = bitmap2;
      matrix.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
      bitmap3 = bitmap2;
      matrix.draw(canvas);
      bitmap3 = bitmap2;
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    return bitmap3;
  }
  
  public static Bitmap drawableToBitmap(Context paramContext, int paramInt1, int paramInt2, int paramInt3) {
    return (paramContext == null || paramInt1 <= 0 || paramInt2 <= 0 || paramInt3 == 0) ? null : ((Build.VERSION.SDK_INT >= 21) ? drawableToBitmap(paramInt1, paramInt2, paramContext.getResources().getDrawable(paramInt3, null)) : drawableToBitmap(paramInt1, paramInt2, paramContext.getResources().getDrawable(paramInt3)));
  }
  
  public static int getColor(Context paramContext, int paramInt) {
    return (paramContext != null) ? ContextCompat.getColor(paramContext.getApplicationContext(), paramInt) : -1;
  }
  
  public static Rect measureText(String paramString, float paramFloat) {
    Paint paint = new Paint();
    Rect rect = new Rect();
    paint.setTextSize(paramFloat);
    paint.getTextBounds(paramString, 0, paramString.length(), rect);
    paint.reset();
    return rect;
  }
  
  public static float parseFloat(String paramString) {
    try {
      return Float.parseFloat(paramString);
    } catch (NumberFormatException numberFormatException) {
      return 0.0F;
    } 
  }
  
  public static void print(String paramString) {
    Log.d("RangeSeekBar", paramString);
  }
  
  public static void print(Object... paramVarArgs) {
    StringBuilder stringBuilder = new StringBuilder();
    int i = paramVarArgs.length;
    for (byte b = 0; b < i; b++)
      stringBuilder.append(paramVarArgs[b]); 
    Log.d("RangeSeekBar", stringBuilder.toString());
  }
  
  public static boolean verifyBitmap(Bitmap paramBitmap) {
    return !(paramBitmap == null || paramBitmap.isRecycled() || paramBitmap.getWidth() <= 0 || paramBitmap.getHeight() <= 0);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/jaygoo/widget/Utils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */