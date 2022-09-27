package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_DDNS_t extends Structure {
  public byte btDDNSType;
  
  public byte btEnable;
  
  public byte btStatus;
  
  public byte[] chPassword = new byte[32];
  
  public byte[] chServerDomain = new byte[128];
  
  public byte[] chUser = new byte[32];
  
  public byte[] chUserDomain = new byte[128];
  
  public int iDDNSInterval;
  
  public int iServerPort;
  
  public FHNP_DDNS_t() {}
  
  public FHNP_DDNS_t(byte paramByte1, byte paramByte2, int paramInt1, byte[] paramArrayOfbyte1, int paramInt2, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, byte[] paramArrayOfbyte4, byte paramByte3) {
    this.btEnable = (byte)paramByte1;
    this.btDDNSType = (byte)paramByte2;
    this.iDDNSInterval = paramInt1;
    if (paramArrayOfbyte1.length == this.chServerDomain.length) {
      this.chServerDomain = paramArrayOfbyte1;
      this.iServerPort = paramInt2;
      if (paramArrayOfbyte2.length == this.chUserDomain.length) {
        this.chUserDomain = paramArrayOfbyte2;
        if (paramArrayOfbyte3.length == this.chUser.length) {
          this.chUser = paramArrayOfbyte3;
          if (paramArrayOfbyte4.length == this.chPassword.length) {
            this.chPassword = paramArrayOfbyte4;
            this.btStatus = (byte)paramByte3;
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
  
  public FHNP_DDNS_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btEnable", "btDDNSType", "iDDNSInterval", "chServerDomain", "iServerPort", "chUserDomain", "chUser", "chPassword", "btStatus" });
  }
  
  public static class ByReference extends FHNP_DDNS_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_DDNS_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_DDNS_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */