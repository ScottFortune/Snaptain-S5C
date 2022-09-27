package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_RecScheduleDaily_t_struct extends Structure {
  public byte btEnable;
  
  public byte[] btReserve = new byte[3];
  
  public FHNP_RecScheduleSegment_t[] stSegment = new FHNP_RecScheduleSegment_t[3];
  
  public LPFHNP_RecScheduleDaily_t_struct() {}
  
  public LPFHNP_RecScheduleDaily_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  public LPFHNP_RecScheduleDaily_t_struct(FHNP_RecScheduleSegment_t[] paramArrayOfFHNP_RecScheduleSegment_t, byte paramByte, byte[] paramArrayOfbyte) {
    if (paramArrayOfFHNP_RecScheduleSegment_t.length == this.stSegment.length) {
      this.stSegment = paramArrayOfFHNP_RecScheduleSegment_t;
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
    return Arrays.asList(new String[] { "stSegment", "btEnable", "btReserve" });
  }
  
  public static class ByReference extends LPFHNP_RecScheduleDaily_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_RecScheduleDaily_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_RecScheduleDaily_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */