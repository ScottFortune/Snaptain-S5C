package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_MDConfig_t extends Structure {
  public byte btEnable;
  
  public byte btSensitivity;
  
  public int dwFrameDelay;
  
  public FHNP_Rect_t[] stRect = new FHNP_Rect_t[32];
  
  public FHNP_MDConfig_t() {}
  
  public FHNP_MDConfig_t(byte paramByte1, byte paramByte2, int paramInt, FHNP_Rect_t[] paramArrayOfFHNP_Rect_t) {
    this.btEnable = (byte)paramByte1;
    this.btSensitivity = (byte)paramByte2;
    this.dwFrameDelay = paramInt;
    if (paramArrayOfFHNP_Rect_t.length == this.stRect.length) {
      this.stRect = paramArrayOfFHNP_Rect_t;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_MDConfig_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btEnable", "btSensitivity", "dwFrameDelay", "stRect" });
  }
  
  public static class ByReference extends FHNP_MDConfig_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_MDConfig_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_MDConfig_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */