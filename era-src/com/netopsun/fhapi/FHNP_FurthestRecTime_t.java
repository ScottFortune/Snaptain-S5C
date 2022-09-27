package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_FurthestRecTime_t extends Structure {
  public int dwChannelCount;
  
  public FHNP_Time_t[] stTime = new FHNP_Time_t[32];
  
  public FHNP_FurthestRecTime_t() {}
  
  public FHNP_FurthestRecTime_t(int paramInt, FHNP_Time_t[] paramArrayOfFHNP_Time_t) {
    this.dwChannelCount = paramInt;
    if (paramArrayOfFHNP_Time_t.length == this.stTime.length) {
      this.stTime = paramArrayOfFHNP_Time_t;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_FurthestRecTime_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "dwChannelCount", "stTime" });
  }
  
  public static class ByReference extends FHNP_FurthestRecTime_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_FurthestRecTime_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_FurthestRecTime_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */