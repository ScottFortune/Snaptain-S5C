package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_GPIOAdvanced_t extends Structure {
  public byte btID;
  
  public byte btMode;
  
  public byte btStatus;
  
  public int dwMS_for_disable;
  
  public int dwMS_for_enable;
  
  public FHNP_GPIOAdvanced_t() {}
  
  public FHNP_GPIOAdvanced_t(byte paramByte1, byte paramByte2, byte paramByte3, int paramInt1, int paramInt2) {
    this.btID = (byte)paramByte1;
    this.btStatus = (byte)paramByte2;
    this.btMode = (byte)paramByte3;
    this.dwMS_for_enable = paramInt1;
    this.dwMS_for_disable = paramInt2;
  }
  
  public FHNP_GPIOAdvanced_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btID", "btStatus", "btMode", "dwMS_for_enable", "dwMS_for_disable" });
  }
  
  public static class ByReference extends FHNP_GPIOAdvanced_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_GPIOAdvanced_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_GPIOAdvanced_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */