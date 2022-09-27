package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_EncHead_t extends Structure {
  public byte btCfgID;
  
  public byte btEncID;
  
  public FHNP_EncHead_t() {}
  
  public FHNP_EncHead_t(byte paramByte1, byte paramByte2) {
    this.btEncID = (byte)paramByte1;
    this.btCfgID = (byte)paramByte2;
  }
  
  public FHNP_EncHead_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btEncID", "btCfgID" });
  }
  
  public static class ByReference extends FHNP_EncHead_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_EncHead_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_EncHead_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */