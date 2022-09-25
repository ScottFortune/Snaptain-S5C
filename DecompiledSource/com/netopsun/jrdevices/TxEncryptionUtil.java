package com.netopsun.jrdevices;

public class TxEncryptionUtil {
  public int key;
  
  public byte[] encrypt(byte[] paramArrayOfbyte) {
    for (byte b = 0; b < paramArrayOfbyte.length; b++)
      paramArrayOfbyte[b] = (byte)(byte)(paramArrayOfbyte[b] ^ this.key); 
    return paramArrayOfbyte;
  }
  
  public void updateKey(String paramString) {
    if (paramString.equals("02:00:00:00:00:00")) {
      this.key = -1;
      return;
    } 
    String[] arrayOfString = paramString.split(":");
    int[] arrayOfInt = new int[arrayOfString.length];
    byte b = 0;
    int i = 0;
    while (b < arrayOfString.length) {
      i += Integer.parseInt(arrayOfString[b], 16);
      b++;
    } 
    this.key = i & 0xFF;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/jrdevices/TxEncryptionUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */