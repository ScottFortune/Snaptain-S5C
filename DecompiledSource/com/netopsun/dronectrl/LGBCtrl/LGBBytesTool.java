package com.netopsun.dronectrl.LGBCtrl;

import android.util.Log;

public class LGBBytesTool {
  public static String bytesToHex(byte[] paramArrayOfbyte) {
    char[] arrayOfChar1 = "0123456789ABCDEF".toCharArray();
    char[] arrayOfChar2 = new char[paramArrayOfbyte.length * 2];
    for (byte b = 0; b < paramArrayOfbyte.length; b++) {
      int i = paramArrayOfbyte[b] & 0xFF;
      int j = b * 2;
      arrayOfChar2[j] = (char)arrayOfChar1[i >>> 4];
      arrayOfChar2[j + 1] = (char)arrayOfChar1[i & 0xF];
    } 
    return new String(arrayOfChar2);
  }
  
  public static int caculateXOR(byte[] paramArrayOfbyte) {
    byte b = 0;
    int i = 0;
    while (b < paramArrayOfbyte.length) {
      i ^= paramArrayOfbyte[b] & 0xFF;
      b++;
    } 
    return i;
  }
  
  public static int covertToInt16(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte.length < 2)
      Log.e("LGBBytesTool", "covertToInt16 error"); 
    byte b = paramArrayOfbyte[0];
    return (short)((paramArrayOfbyte[1] & 0xFF) << 8 | b & 0xFF);
  }
  
  public static int covertToInt32(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte.length < 4)
      Log.e("LGBBytesTool", "covertToInt32 error"); 
    byte b1 = paramArrayOfbyte[0];
    byte b2 = paramArrayOfbyte[1];
    byte b3 = paramArrayOfbyte[2];
    return (paramArrayOfbyte[3] & 0xFF) << 24 | b1 & 0xFF | (b2 & 0xFF) << 8 | (b3 & 0xFF) << 16;
  }
  
  public static int covertToInt8(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte.length < 1)
      Log.e("LGBBytesTool", "covertToInt8 error"); 
    return paramArrayOfbyte[0];
  }
  
  public static int covertToUInt16(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte.length < 2)
      Log.e("LGBBytesTool", "covertToUInt16 error"); 
    byte b = paramArrayOfbyte[0];
    return ((paramArrayOfbyte[1] & 0xFF) << 8 | b & 0xFF) & 0xFFFF;
  }
  
  public static long covertToUInt32(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte.length < 4)
      Log.e("RxModel", "covertToUInt32 error"); 
    byte b1 = paramArrayOfbyte[0];
    byte b2 = paramArrayOfbyte[1];
    byte b3 = paramArrayOfbyte[2];
    return (((paramArrayOfbyte[3] & 0xFF) << 24 | b1 & 0xFF | (b2 & 0xFF) << 8 | (b3 & 0xFF) << 16) & 0xFFFFFFFF);
  }
  
  public static int covertToUInt8(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte.length < 1)
      Log.e("LGBBytesTool", "covertToUInt8 error"); 
    return paramArrayOfbyte[0] & 0xFF;
  }
  
  public static byte[] subBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    byte[] arrayOfByte = new byte[paramInt2];
    System.arraycopy(paramArrayOfbyte, paramInt1, arrayOfByte, 0, paramInt2);
    return arrayOfByte;
  }
  
  public static byte[] subBytes(byte[] paramArrayOfbyte, Range paramRange) {
    return subBytes(paramArrayOfbyte, paramRange.index, paramRange.len);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LGBCtrl/LGBBytesTool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */