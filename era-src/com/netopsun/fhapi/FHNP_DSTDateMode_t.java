package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_DSTDateMode_t extends Structure {
  public FHNP_Time_t stStartTime;
  
  public FHNP_Time_t stStopTime;
  
  public FHNP_DSTDateMode_t() {}
  
  public FHNP_DSTDateMode_t(FHNP_Time_t paramFHNP_Time_t1, FHNP_Time_t paramFHNP_Time_t2) {
    this.stStartTime = paramFHNP_Time_t1;
    this.stStopTime = paramFHNP_Time_t2;
  }
  
  public FHNP_DSTDateMode_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "stStartTime", "stStopTime" });
  }
  
  public static class ByReference extends FHNP_DSTDateMode_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_DSTDateMode_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_DSTDateMode_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */