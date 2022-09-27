package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_DSTDateMode_t_struct extends Structure {
  public FHNP_Time_t stStartTime;
  
  public FHNP_Time_t stStopTime;
  
  public LPFHNP_DSTDateMode_t_struct() {}
  
  public LPFHNP_DSTDateMode_t_struct(FHNP_Time_t paramFHNP_Time_t1, FHNP_Time_t paramFHNP_Time_t2) {
    this.stStartTime = paramFHNP_Time_t1;
    this.stStopTime = paramFHNP_Time_t2;
  }
  
  public LPFHNP_DSTDateMode_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "stStartTime", "stStopTime" });
  }
  
  public static class ByReference extends LPFHNP_DSTDateMode_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_DSTDateMode_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_DSTDateMode_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */