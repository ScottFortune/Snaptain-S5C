package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_ScheduleDaily_t extends Structure {
  public FHNP_ScheduleSegment_t[] stSegment = new FHNP_ScheduleSegment_t[8];
  
  public FHNP_ScheduleDaily_t() {}
  
  public FHNP_ScheduleDaily_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  public FHNP_ScheduleDaily_t(FHNP_ScheduleSegment_t[] paramArrayOfFHNP_ScheduleSegment_t) {
    if (paramArrayOfFHNP_ScheduleSegment_t.length == this.stSegment.length) {
      this.stSegment = paramArrayOfFHNP_ScheduleSegment_t;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "stSegment" });
  }
  
  public static class ByReference extends FHNP_ScheduleDaily_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_ScheduleDaily_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_ScheduleDaily_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */