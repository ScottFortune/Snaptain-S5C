package com.yang.firework.utils;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ESShader {
  public static int loadProgram(String paramString1, String paramString2) {
    int[] arrayOfInt = new int[1];
    int i = loadShader(35633, paramString1);
    if (i == 0)
      return 0; 
    int j = loadShader(35632, paramString2);
    if (j == 0) {
      GLES20.glDeleteShader(i);
      return 0;
    } 
    int k = GLES20.glCreateProgram();
    if (k == 0)
      return 0; 
    GLES20.glAttachShader(k, i);
    GLES20.glAttachShader(k, j);
    GLES20.glLinkProgram(k);
    GLES20.glGetProgramiv(k, 35714, arrayOfInt, 0);
    if (arrayOfInt[0] == 0) {
      Log.e("ESShader", "Error linking program:");
      Log.e("ESShader", GLES20.glGetProgramInfoLog(k));
      GLES20.glDeleteProgram(k);
      return 0;
    } 
    GLES20.glDeleteShader(i);
    GLES20.glDeleteShader(j);
    return k;
  }
  
  public static int loadProgramFromAsset(Context paramContext, String paramString1, String paramString2) {
    int[] arrayOfInt = new int[1];
    paramString1 = readShader(paramContext, paramString1);
    if (paramString1 == null)
      return 0; 
    String str = readShader(paramContext, paramString2);
    if (str == null)
      return 0; 
    int i = loadShader(35633, paramString1);
    if (i == 0)
      return 0; 
    int j = loadShader(35632, str);
    if (j == 0) {
      GLES20.glDeleteShader(i);
      return 0;
    } 
    int k = GLES20.glCreateProgram();
    if (k == 0)
      return 0; 
    GLES20.glAttachShader(k, i);
    GLES20.glAttachShader(k, j);
    GLES20.glLinkProgram(k);
    GLES20.glGetProgramiv(k, 35714, arrayOfInt, 0);
    if (arrayOfInt[0] == 0) {
      Log.e("ESShader", "Error linking program:");
      Log.e("ESShader", GLES20.glGetProgramInfoLog(k));
      GLES20.glDeleteProgram(k);
      return 0;
    } 
    GLES20.glDeleteShader(i);
    GLES20.glDeleteShader(j);
    return k;
  }
  
  public static int loadShader(int paramInt, String paramString) {
    int[] arrayOfInt = new int[1];
    paramInt = GLES20.glCreateShader(paramInt);
    if (paramInt == 0)
      return 0; 
    GLES20.glShaderSource(paramInt, paramString);
    GLES20.glCompileShader(paramInt);
    GLES20.glGetShaderiv(paramInt, 35713, arrayOfInt, 0);
    if (arrayOfInt[0] == 0) {
      Log.e("ESShader", GLES20.glGetShaderInfoLog(paramInt));
      GLES20.glDeleteShader(paramInt);
      return 0;
    } 
    return paramInt;
  }
  
  private static String readShader(Context paramContext, String paramString) {
    IOException iOException2 = null;
    if (paramString == null)
      return null; 
    try {
      InputStream inputStream = paramContext.getAssets().open(paramString);
      byte[] arrayOfByte = new byte[inputStream.available()];
      inputStream.read(arrayOfByte);
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      this();
      byteArrayOutputStream.write(arrayOfByte);
      byteArrayOutputStream.close();
      inputStream.close();
      String str = byteArrayOutputStream.toString();
    } catch (IOException iOException1) {
      paramString = null;
      iOException1 = iOException2;
    } 
    if (paramString == null);
    return (String)iOException1;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/yang/firework/utils/ESShader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */