package com.netopsun.opencvapi;

import android.graphics.Bitmap;
import java.nio.ByteBuffer;

public class OpenCVAPI {
  static {
    System.loadLibrary("opencv_api");
  }
  
  public static native void caffeNetDelete(long paramLong);
  
  public static native float[] caffeNetDetect(long paramLong, Bitmap paramBitmap, float paramFloat);
  
  public static native long caffeNetInit(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2);
  
  public static native void kcfDelete(long paramLong);
  
  public static native long kcfInit(Bitmap paramBitmap, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  public static native float[] kcfUpdate(long paramLong, Bitmap paramBitmap);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/opencvapi/OpenCVAPI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */