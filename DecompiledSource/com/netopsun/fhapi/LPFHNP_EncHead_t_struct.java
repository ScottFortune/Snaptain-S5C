package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_EncHead_t_struct extends Structure {
  public byte btCfgID;
  
  public byte btEncID;
  
  public LPFHNP_EncHead_t_struct() {}
  
  public LPFHNP_EncHead_t_struct(byte paramByte1, byte paramByte2) {
    this.btEncID = (byte)paramByte1;
    this.btCfgID = (byte)paramByte2;
  }
  
  public LPFHNP_EncHead_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btEncID", "btCfgID" });
  }
  
  public static class ByReference extends LPFHNP_EncHead_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_EncHead_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_EncHead_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */