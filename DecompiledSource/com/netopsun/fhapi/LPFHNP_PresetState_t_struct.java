package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_PresetState_t_struct extends Structure {
  public byte[] btCruisePoint = new byte[512];
  
  public byte[] btPresetPoint = new byte[256];
  
  public byte[] btPresetSpeed = new byte[256];
  
  public short[] wPresetDelay = new short[256];
  
  public LPFHNP_PresetState_t_struct() {}
  
  public LPFHNP_PresetState_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  public LPFHNP_PresetState_t_struct(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, short[] paramArrayOfshort, byte[] paramArrayOfbyte3) {
    if (paramArrayOfbyte1.length == this.btPresetPoint.length) {
      this.btPresetPoint = paramArrayOfbyte1;
      if (paramArrayOfbyte2.length == this.btPresetSpeed.length) {
        this.btPresetSpeed = paramArrayOfbyte2;
        if (paramArrayOfshort.length == this.wPresetDelay.length) {
          this.wPresetDelay = paramArrayOfshort;
          if (paramArrayOfbyte3.length == this.btCruisePoint.length) {
            this.btCruisePoint = paramArrayOfbyte3;
            return;
          } 
          throw new IllegalArgumentException("Wrong array size !");
        } 
        throw new IllegalArgumentException("Wrong array size !");
      } 
      throw new IllegalArgumentException("Wrong array size !");
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btPresetPoint", "btPresetSpeed", "wPresetDelay", "btCruisePoint" });
  }
  
  public static class ByReference extends LPFHNP_PresetState_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_PresetState_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_PresetState_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */