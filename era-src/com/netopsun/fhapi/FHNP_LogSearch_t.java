package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_LogSearch_t extends Structure {
  public byte[] btRes = new byte[3];
  
  public byte btType;
  
  public FHNP_Time_t stStartTime;
  
  public FHNP_Time_t stStopTime;
  
  public FHNP_LogSearch_t() {}
  
  public FHNP_LogSearch_t(byte paramByte, byte[] paramArrayOfbyte, FHNP_Time_t paramFHNP_Time_t1, FHNP_Time_t paramFHNP_Time_t2) {
    this.btType = (byte)paramByte;
    if (paramArrayOfbyte.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte;
      this.stStartTime = paramFHNP_Time_t1;
      this.stStopTime = paramFHNP_Time_t2;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_LogSearch_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btType", "btRes", "stStartTime", "stStopTime" });
  }
  
  public static class ByReference extends FHNP_LogSearch_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_LogSearch_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_LogSearch_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */