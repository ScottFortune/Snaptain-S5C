package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_PPPoE_t extends Structure {
  public byte btEnable;
  
  public byte btStatus;
  
  public byte[] chPassword = new byte[32];
  
  public byte[] chUser = new byte[32];
  
  public FHNP_PPPoE_t() {}
  
  public FHNP_PPPoE_t(byte paramByte1, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte paramByte2) {
    this.btEnable = (byte)paramByte1;
    if (paramArrayOfbyte1.length == this.chUser.length) {
      this.chUser = paramArrayOfbyte1;
      if (paramArrayOfbyte2.length == this.chPassword.length) {
        this.chPassword = paramArrayOfbyte2;
        this.btStatus = (byte)paramByte2;
        return;
      } 
      throw new IllegalArgumentException("Wrong array size !");
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_PPPoE_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btEnable", "chUser", "chPassword", "btStatus" });
  }
  
  public static class ByReference extends FHNP_PPPoE_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_PPPoE_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_PPPoE_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */