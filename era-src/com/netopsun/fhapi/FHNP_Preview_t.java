package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_Preview_t extends Structure {
  public byte btBlocked;
  
  public byte btChannel;
  
  public byte btEncID;
  
  public byte btTransMode;
  
  public byte[] chMultiCastIP = new byte[32];
  
  public FHNP_Preview_t() {}
  
  public FHNP_Preview_t(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, byte[] paramArrayOfbyte) {
    this.btChannel = (byte)paramByte1;
    this.btEncID = (byte)paramByte2;
    this.btTransMode = (byte)paramByte3;
    this.btBlocked = (byte)paramByte4;
    if (paramArrayOfbyte.length == this.chMultiCastIP.length) {
      this.chMultiCastIP = paramArrayOfbyte;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_Preview_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btChannel", "btEncID", "btTransMode", "btBlocked", "chMultiCastIP" });
  }
  
  public static class ByReference extends FHNP_Preview_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_Preview_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_Preview_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */