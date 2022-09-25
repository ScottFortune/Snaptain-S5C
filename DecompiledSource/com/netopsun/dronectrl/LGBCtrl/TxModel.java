package com.netopsun.dronectrl.LGBCtrl;

public abstract class TxModel {
  public boolean isLSB = true;
  
  public static byte[] numToBytes(int paramInt1, int paramInt2, boolean paramBoolean) {
    byte[] arrayOfByte = new byte[paramInt2];
    byte b1 = 0;
    byte b2 = 0;
    if (paramBoolean) {
      for (b1 = b2; b1 < paramInt2; b1++)
        arrayOfByte[b1] = (byte)(byte)(paramInt1 >> b1 * 8 & 0xFF); 
    } else {
      while (b1 < paramInt2) {
        arrayOfByte[b1] = (byte)(byte)(paramInt1 >> 24 - b1 * 8 & 0xFF);
        b1++;
      } 
    } 
    return arrayOfByte;
  }
  
  public byte[] getRawData() {
    byte[] arrayOfByte = new byte[modelRawLength()];
    int[] arrayOfInt1 = rawByteLens();
    int[] arrayOfInt2 = modelValues();
    byte b = 0;
    int i = 0;
    while (b < arrayOfInt1.length) {
      System.arraycopy(numToBytes(arrayOfInt2[b], arrayOfInt1[b], this.isLSB), 0, arrayOfByte, i, arrayOfInt1[b]);
      i += arrayOfInt1[b];
      b++;
    } 
    return arrayOfByte;
  }
  
  public int modelRawLength() {
    int[] arrayOfInt = rawByteLens();
    byte b = 0;
    int i = 0;
    while (b < arrayOfInt.length) {
      i += arrayOfInt[b];
      b++;
    } 
    return i;
  }
  
  public abstract int[] modelValues();
  
  public abstract int[] rawByteLens();
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LGBCtrl/TxModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */