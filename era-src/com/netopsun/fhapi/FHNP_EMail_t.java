package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_EMail_t extends Structure {
  public byte btSslEnable;
  
  public byte[] chDstMail = new byte[240];
  
  public byte[] chPassword = new byte[32];
  
  public byte[] chServerName = new byte[128];
  
  public byte[] chUser = new byte[48];
  
  public int dwServerPort;
  
  public FHNP_EMail_t() {}
  
  public FHNP_EMail_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  public FHNP_EMail_t(byte[] paramArrayOfbyte1, int paramInt, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, byte[] paramArrayOfbyte4, byte paramByte) {
    if (paramArrayOfbyte1.length == this.chServerName.length) {
      this.chServerName = paramArrayOfbyte1;
      this.dwServerPort = paramInt;
      if (paramArrayOfbyte2.length == this.chUser.length) {
        this.chUser = paramArrayOfbyte2;
        if (paramArrayOfbyte3.length == this.chPassword.length) {
          this.chPassword = paramArrayOfbyte3;
          if (paramArrayOfbyte4.length == this.chDstMail.length) {
            this.chDstMail = paramArrayOfbyte4;
            this.btSslEnable = (byte)paramByte;
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
    return Arrays.asList(new String[] { "chServerName", "dwServerPort", "chUser", "chPassword", "chDstMail", "btSslEnable" });
  }
  
  public static class ByReference extends FHNP_EMail_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_EMail_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_EMail_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */