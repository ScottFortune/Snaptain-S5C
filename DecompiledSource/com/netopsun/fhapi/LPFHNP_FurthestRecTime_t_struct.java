package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_FurthestRecTime_t_struct extends Structure {
  public int dwChannelCount;
  
  public FHNP_Time_t[] stTime = new FHNP_Time_t[32];
  
  public LPFHNP_FurthestRecTime_t_struct() {}
  
  public LPFHNP_FurthestRecTime_t_struct(int paramInt, FHNP_Time_t[] paramArrayOfFHNP_Time_t) {
    this.dwChannelCount = paramInt;
    if (paramArrayOfFHNP_Time_t.length == this.stTime.length) {
      this.stTime = paramArrayOfFHNP_Time_t;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public LPFHNP_FurthestRecTime_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "dwChannelCount", "stTime" });
  }
  
  public static class ByReference extends LPFHNP_FurthestRecTime_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_FurthestRecTime_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_FurthestRecTime_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */