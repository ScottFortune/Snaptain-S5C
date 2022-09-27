package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_ScheduleWeekly_t extends Structure {
  public FHNP_ScheduleDaily_t[] stDaily = new FHNP_ScheduleDaily_t[7];
  
  public FHNP_ScheduleWeekly_t() {}
  
  public FHNP_ScheduleWeekly_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  public FHNP_ScheduleWeekly_t(FHNP_ScheduleDaily_t[] paramArrayOfFHNP_ScheduleDaily_t) {
    if (paramArrayOfFHNP_ScheduleDaily_t.length == this.stDaily.length) {
      this.stDaily = paramArrayOfFHNP_ScheduleDaily_t;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "stDaily" });
  }
  
  public static class ByReference extends FHNP_ScheduleWeekly_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_ScheduleWeekly_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_ScheduleWeekly_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */