package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_Notify_t extends Structure {
  public byte btChannel;
  
  public byte btFlag;
  
  public byte btNotify;
  
  public byte btRes;
  
  public byte[] chBuffer = new byte[4096];
  
  public int dwBufLen;
  
  public Pointer dwUserID;
  
  public FHNP_Notify_t() {}
  
  public FHNP_Notify_t(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, Pointer paramPointer, int paramInt, byte[] paramArrayOfbyte) {
    this.btNotify = (byte)paramByte1;
    this.btFlag = (byte)paramByte2;
    this.btChannel = (byte)paramByte3;
    this.btRes = (byte)paramByte4;
    this.dwUserID = paramPointer;
    this.dwBufLen = paramInt;
    if (paramArrayOfbyte.length == this.chBuffer.length) {
      this.chBuffer = paramArrayOfbyte;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_Notify_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btNotify", "btFlag", "btChannel", "btRes", "dwUserID", "dwBufLen", "chBuffer" });
  }
  
  public static class ByReference extends FHNP_Notify_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_Notify_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_Notify_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */