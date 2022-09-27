package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_CruisePoint_t_struct extends Structure {
  public byte btCruisePoint;
  
  public byte btPresetPoint;
  
  public byte[] btRes = new byte[3];
  
  public byte btSpeed;
  
  public short wDelay;
  
  public LPFHNP_CruisePoint_t_struct() {}
  
  public LPFHNP_CruisePoint_t_struct(byte paramByte1, byte paramByte2, short paramShort, byte paramByte3, byte[] paramArrayOfbyte) {
    this.btCruisePoint = (byte)paramByte1;
    this.btPresetPoint = (byte)paramByte2;
    this.wDelay = (short)paramShort;
    this.btSpeed = (byte)paramByte3;
    if (paramArrayOfbyte.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public LPFHNP_CruisePoint_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btCruisePoint", "btPresetPoint", "wDelay", "btSpeed", "btRes" });
  }
  
  public static class ByReference extends LPFHNP_CruisePoint_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_CruisePoint_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_CruisePoint_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */