package com.vilyever.resource;

import android.graphics.Color;

public class Colour {
  final Colour self = this;
  
  public static int changeAlpha(int paramInt1, int paramInt2) {
    return Color.argb(paramInt2, Color.red(paramInt1), Color.green(paramInt1), Color.blue(paramInt1));
  }
  
  public static int changeBlue(int paramInt1, int paramInt2) {
    return Color.argb(Color.alpha(paramInt1), Color.red(paramInt1), Color.green(paramInt1), paramInt2);
  }
  
  public static int changeGreen(int paramInt1, int paramInt2) {
    return Color.argb(Color.alpha(paramInt1), Color.red(paramInt1), paramInt2, Color.blue(paramInt1));
  }
  
  public static int changeHSVHue(int paramInt, float paramFloat) {
    float[] arrayOfFloat = new float[3];
    Color.colorToHSV(paramInt, arrayOfFloat);
    arrayOfFloat[0] = paramFloat;
    return Color.HSVToColor(Color.alpha(paramInt), arrayOfFloat);
  }
  
  public static int changeHSVSaturation(int paramInt, float paramFloat) {
    float[] arrayOfFloat = new float[3];
    Color.colorToHSV(paramInt, arrayOfFloat);
    arrayOfFloat[1] = paramFloat;
    return Color.HSVToColor(Color.alpha(paramInt), arrayOfFloat);
  }
  
  public static int changeHSVValue(int paramInt, float paramFloat) {
    float[] arrayOfFloat = new float[3];
    Color.colorToHSV(paramInt, arrayOfFloat);
    arrayOfFloat[2] = paramFloat;
    return Color.HSVToColor(Color.alpha(paramInt), arrayOfFloat);
  }
  
  public static int changeRed(int paramInt1, int paramInt2) {
    return Color.argb(Color.alpha(paramInt1), paramInt2, Color.green(paramInt1), Color.blue(paramInt1));
  }
  
  public static String getHex(int paramInt) {
    return ((paramInt & 0xFF000000) != -16777216) ? String.format("#%08X", new Object[] { Integer.valueOf(paramInt) }) : String.format("#%06X", new Object[] { Integer.valueOf(paramInt & 0xFFFFFF) });
  }
  
  public static int getInvertColor(int paramInt) {
    float[] arrayOfFloat = new float[3];
    Color.colorToHSV(paramInt, arrayOfFloat);
    arrayOfFloat[0] = (arrayOfFloat[0] + 180.0F) % 360.0F;
    arrayOfFloat[2] = 1.0F - arrayOfFloat[2];
    return Color.HSVToColor(Color.alpha(paramInt), arrayOfFloat);
  }
  
  public static int res(int paramInt) {
    return Resource.getColor(paramInt);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/vilyever/resource/Colour.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */