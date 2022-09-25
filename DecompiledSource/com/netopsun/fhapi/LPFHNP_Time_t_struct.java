package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_Time_t_struct extends Structure {
  public byte day;
  
  public byte hour;
  
  public byte minute;
  
  public byte month;
  
  public int msecond;
  
  public byte second;
  
  public byte wday;
  
  public short year;
  
  public LPFHNP_Time_t_struct() {}
  
  public LPFHNP_Time_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  public LPFHNP_Time_t_struct(short paramShort, byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, byte paramByte5, byte paramByte6, int paramInt) {
    this.year = (short)paramShort;
    this.month = (byte)paramByte1;
    this.day = (byte)paramByte2;
    this.wday = (byte)paramByte3;
    this.hour = (byte)paramByte4;
    this.minute = (byte)paramByte5;
    this.second = (byte)paramByte6;
    this.msecond = paramInt;
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "year", "month", "day", "wday", "hour", "minute", "second", "msecond" });
  }
  
  public static class ByReference extends LPFHNP_Time_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_Time_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_Time_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */