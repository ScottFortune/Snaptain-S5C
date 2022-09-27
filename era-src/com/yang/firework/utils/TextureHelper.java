package com.yang.firework.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class TextureHelper {
  private static boolean isFirst = true;
  
  public static int loadTexture(Context paramContext, int paramInt) {
    int[] arrayOfInt = new int[1];
    GLES20.glGenTextures(1, arrayOfInt, 0);
    if (arrayOfInt[0] != 0) {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inScaled = false;
      Bitmap bitmap = BitmapFactory.decodeResource(paramContext.getResources(), paramInt, options);
      GLES20.glBindTexture(3553, arrayOfInt[0]);
      GLES20.glTexParameteri(3553, 10241, 9728);
      GLES20.glTexParameteri(3553, 10240, 9728);
      GLUtils.texImage2D(3553, 0, bitmap, 0);
      bitmap.recycle();
    } 
    if (arrayOfInt[0] != 0)
      return arrayOfInt[0]; 
    throw new RuntimeException("Error loading texture.");
  }
  
  public static int loadTexture(Bitmap paramBitmap) {
    int[] arrayOfInt = new int[1];
    GLES20.glGenTextures(1, arrayOfInt, 0);
    if (arrayOfInt[0] != 0) {
      GLES20.glBindTexture(3553, arrayOfInt[0]);
      GLES20.glTexParameteri(3553, 10241, 9728);
      GLES20.glTexParameteri(3553, 10240, 9728);
      GLUtils.texImage2D(3553, 0, paramBitmap, 0);
    } 
    if (arrayOfInt[0] != 0)
      return arrayOfInt[0]; 
    throw new RuntimeException("Error loading texture.");
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/yang/firework/utils/TextureHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */