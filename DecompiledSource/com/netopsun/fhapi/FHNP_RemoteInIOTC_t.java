package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_RemoteInIOTC_t extends Structure {
  public byte btEnable;
  
  public byte[] btRes = new byte[3];
  
  public byte[] chUID = new byte[24];
  
  public FHNP_RemoteInIOTC_t() {}
  
  public FHNP_RemoteInIOTC_t(byte paramByte, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    this.btEnable = (byte)paramByte;
    if (paramArrayOfbyte1.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte1;
      if (paramArrayOfbyte2.length == this.chUID.length) {
        this.chUID = paramArrayOfbyte2;
        return;
      } 
      throw new IllegalArgumentException("Wrong array size !");
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_RemoteInIOTC_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btEnable", "btRes", "chUID" });
  }
  
  public static class ByReference extends FHNP_RemoteInIOTC_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_RemoteInIOTC_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_RemoteInIOTC_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */