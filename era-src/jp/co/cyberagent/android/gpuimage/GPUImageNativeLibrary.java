package jp.co.cyberagent.android.gpuimage;

import android.graphics.Bitmap;

public class GPUImageNativeLibrary {
  static {
    System.loadLibrary("yuv-decoder");
  }
  
  public static native void YUVtoARBG(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int[] paramArrayOfint);
  
  public static native void YUVtoRBGA(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int[] paramArrayOfint);
  
  public static native void adjustBitmap(Bitmap paramBitmap);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/GPUImageNativeLibrary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */