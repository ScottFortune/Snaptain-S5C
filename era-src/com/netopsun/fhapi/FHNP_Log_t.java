package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_Log_t extends Structure {
  public byte btChannel;
  
  public byte[] btRes = new byte[2];
  
  public byte btType;
  
  public byte[] chIP = new byte[32];
  
  public byte[] chLog = new byte[256];
  
  public byte[] chUser = new byte[32];
  
  public FHNP_Time_t stTime;
  
  public FHNP_Log_t() {}
  
  public FHNP_Log_t(FHNP_Time_t paramFHNP_Time_t, byte paramByte1, byte paramByte2, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, byte[] paramArrayOfbyte4) {
    this.stTime = paramFHNP_Time_t;
    this.btType = (byte)paramByte1;
    this.btChannel = (byte)paramByte2;
    if (paramArrayOfbyte1.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte1;
      if (paramArrayOfbyte2.length == this.chUser.length) {
        this.chUser = paramArrayOfbyte2;
        if (paramArrayOfbyte3.length == this.chIP.length) {
          this.chIP = paramArrayOfbyte3;
          if (paramArrayOfbyte4.length == this.chLog.length) {
            this.chLog = paramArrayOfbyte4;
            return;
          } 
          throw new IllegalArgumentException("Wrong array size !");
        } 
        throw new IllegalArgumentException("Wrong array size !");
      } 
      throw new IllegalArgumentException("Wrong array size !");
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_Log_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "stTime", "btType", "btChannel", "btRes", "chUser", "chIP", "chLog" });
  }
  
  public static class ByReference extends FHNP_Log_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_Log_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_Log_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */