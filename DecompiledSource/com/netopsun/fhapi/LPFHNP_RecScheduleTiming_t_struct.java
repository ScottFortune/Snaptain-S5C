package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_RecScheduleTiming_t_struct extends Structure {
  public byte btEnable;
  
  public byte[] btReserve = new byte[3];
  
  public FHNP_RecScheduleDaily_t[] stDaily = new FHNP_RecScheduleDaily_t[7];
  
  public LPFHNP_RecScheduleTiming_t_struct() {}
  
  public LPFHNP_RecScheduleTiming_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  public LPFHNP_RecScheduleTiming_t_struct(FHNP_RecScheduleDaily_t[] paramArrayOfFHNP_RecScheduleDaily_t, byte paramByte, byte[] paramArrayOfbyte) {
    if (paramArrayOfFHNP_RecScheduleDaily_t.length == this.stDaily.length) {
      this.stDaily = paramArrayOfFHNP_RecScheduleDaily_t;
      this.btEnable = (byte)paramByte;
      if (paramArrayOfbyte.length == this.btReserve.length) {
        this.btReserve = paramArrayOfbyte;
        return;
      } 
      throw new IllegalArgumentException("Wrong array size !");
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "stDaily", "btEnable", "btReserve" });
  }
  
  public static class ByReference extends LPFHNP_RecScheduleTiming_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_RecScheduleTiming_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_RecScheduleTiming_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */