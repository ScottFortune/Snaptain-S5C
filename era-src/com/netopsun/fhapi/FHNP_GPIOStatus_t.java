package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_GPIOStatus_t extends Structure {
  public byte btID;
  
  public byte btStatus;
  
  public FHNP_GPIOStatus_t() {}
  
  public FHNP_GPIOStatus_t(byte paramByte1, byte paramByte2) {
    this.btID = (byte)paramByte1;
    this.btStatus = (byte)paramByte2;
  }
  
  public FHNP_GPIOStatus_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btID", "btStatus" });
  }
  
  public static class ByReference extends FHNP_GPIOStatus_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_GPIOStatus_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_GPIOStatus_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */