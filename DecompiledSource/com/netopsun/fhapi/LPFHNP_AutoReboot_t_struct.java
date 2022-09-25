package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_AutoReboot_t_struct extends Structure {
  public byte btARebootMode;
  
  public byte btDay;
  
  public byte btHour;
  
  public byte btRes;
  
  public LPFHNP_AutoReboot_t_struct() {}
  
  public LPFHNP_AutoReboot_t_struct(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4) {
    this.btARebootMode = (byte)paramByte1;
    this.btDay = (byte)paramByte2;
    this.btHour = (byte)paramByte3;
    this.btRes = (byte)paramByte4;
  }
  
  public LPFHNP_AutoReboot_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btARebootMode", "btDay", "btHour", "btRes" });
  }
  
  public static class ByReference extends LPFHNP_AutoReboot_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_AutoReboot_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_AutoReboot_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */