package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_TEMP_RH_t extends Structure {
  public int dwHumidity;
  
  public float fTemperature;
  
  public FHNP_TEMP_RH_t() {}
  
  public FHNP_TEMP_RH_t(float paramFloat, int paramInt) {
    this.fTemperature = paramFloat;
    this.dwHumidity = paramInt;
  }
  
  public FHNP_TEMP_RH_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "fTemperature", "dwHumidity" });
  }
  
  public static class ByReference extends FHNP_TEMP_RH_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_TEMP_RH_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_TEMP_RH_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */