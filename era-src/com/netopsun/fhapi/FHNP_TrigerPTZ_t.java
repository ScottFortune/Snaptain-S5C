package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_TrigerPTZ_t extends Structure {
  public byte btEnable;
  
  public byte btPTZType;
  
  public byte[] btRes = new byte[2];
  
  public int dwCtrlID;
  
  public int dwPTZID;
  
  public FHNP_TrigerPTZ_t() {}
  
  public FHNP_TrigerPTZ_t(byte paramByte1, byte paramByte2, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    this.btEnable = (byte)paramByte1;
    this.btPTZType = (byte)paramByte2;
    if (paramArrayOfbyte.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte;
      this.dwPTZID = paramInt1;
      this.dwCtrlID = paramInt2;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_TrigerPTZ_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btEnable", "btPTZType", "btRes", "dwPTZID", "dwCtrlID" });
  }
  
  public static class ByReference extends FHNP_TrigerPTZ_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_TrigerPTZ_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_TrigerPTZ_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */