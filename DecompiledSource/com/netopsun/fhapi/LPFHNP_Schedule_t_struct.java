package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_Schedule_t_struct extends Structure {
  public FHNP_ScheduleWeekly_t stDay;
  
  public LPFHNP_Schedule_t_struct() {}
  
  public LPFHNP_Schedule_t_struct(FHNP_ScheduleWeekly_t paramFHNP_ScheduleWeekly_t) {
    this.stDay = paramFHNP_ScheduleWeekly_t;
  }
  
  public LPFHNP_Schedule_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "stDay" });
  }
  
  public static class ByReference extends LPFHNP_Schedule_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_Schedule_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_Schedule_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */