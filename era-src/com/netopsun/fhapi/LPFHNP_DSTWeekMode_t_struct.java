package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_DSTWeekMode_t_struct extends Structure {
  public FHNP_DSTWeekModeTime_t stStartTime;
  
  public FHNP_DSTWeekModeTime_t stStopTime;
  
  public LPFHNP_DSTWeekMode_t_struct() {}
  
  public LPFHNP_DSTWeekMode_t_struct(FHNP_DSTWeekModeTime_t paramFHNP_DSTWeekModeTime_t1, FHNP_DSTWeekModeTime_t paramFHNP_DSTWeekModeTime_t2) {
    this.stStartTime = paramFHNP_DSTWeekModeTime_t1;
    this.stStopTime = paramFHNP_DSTWeekModeTime_t2;
  }
  
  public LPFHNP_DSTWeekMode_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "stStartTime", "stStopTime" });
  }
  
  public static class ByReference extends LPFHNP_DSTWeekMode_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_DSTWeekMode_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_DSTWeekMode_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */