package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_NTP_t extends Structure {
  public byte btIsSuccess;
  
  public byte btNTP;
  
  public byte[] btRes = new byte[2];
  
  public byte[] chNTPServer = new byte[128];
  
  public FHNP_Time_t stNextTime;
  
  public FHNP_Time_t stPrevTime;
  
  public FHNP_NTP_t() {}
  
  public FHNP_NTP_t(byte paramByte1, byte paramByte2, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, FHNP_Time_t paramFHNP_Time_t1, FHNP_Time_t paramFHNP_Time_t2) {
    this.btNTP = (byte)paramByte1;
    this.btIsSuccess = (byte)paramByte2;
    if (paramArrayOfbyte1.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte1;
      if (paramArrayOfbyte2.length == this.chNTPServer.length) {
        this.chNTPServer = paramArrayOfbyte2;
        this.stPrevTime = paramFHNP_Time_t1;
        this.stNextTime = paramFHNP_Time_t2;
        return;
      } 
      throw new IllegalArgumentException("Wrong array size !");
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_NTP_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btNTP", "btIsSuccess", "btRes", "chNTPServer", "stPrevTime", "stNextTime" });
  }
  
  public static class ByReference extends FHNP_NTP_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_NTP_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_NTP_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */