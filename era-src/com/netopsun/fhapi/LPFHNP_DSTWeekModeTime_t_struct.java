package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_DSTWeekModeTime_t_struct extends Structure {
  public byte[] btRes = new byte[3];
  
  public byte hour;
  
  public byte minute;
  
  public byte month;
  
  public byte wDay;
  
  public byte wdayIndex;
  
  public LPFHNP_DSTWeekModeTime_t_struct() {}
  
  public LPFHNP_DSTWeekModeTime_t_struct(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, byte paramByte5, byte[] paramArrayOfbyte) {
    this.month = (byte)paramByte1;
    this.wdayIndex = (byte)paramByte2;
    this.wDay = (byte)paramByte3;
    this.hour = (byte)paramByte4;
    this.minute = (byte)paramByte5;
    if (paramArrayOfbyte.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public LPFHNP_DSTWeekModeTime_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "month", "wdayIndex", "wDay", "hour", "minute", "btRes" });
  }
  
  public static class ByReference extends LPFHNP_DSTWeekModeTime_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_DSTWeekModeTime_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_DSTWeekModeTime_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */