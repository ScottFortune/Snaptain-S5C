package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_EncodeOSD_t extends Structure {
  public byte btRes;
  
  public byte[] btRes1 = new byte[3];
  
  public byte btShowCustomTOSD;
  
  public byte btShowTimeOSD;
  
  public byte btTimeFmt;
  
  public byte btTimeStd;
  
  public byte[] chCustomTOSD = new byte[32];
  
  public int colCustomOSD;
  
  public int colTimeOSD;
  
  public int iCustomTOSDX;
  
  public int iCustomTOSDY;
  
  public int iTimeOSDX;
  
  public int iTimeOSDY;
  
  public FHNP_EncodeOSD_t() {}
  
  public FHNP_EncodeOSD_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { 
          "btShowTimeOSD", "btTimeFmt", "btTimeStd", "btRes", "iTimeOSDX", "iTimeOSDY", "colTimeOSD", "btShowCustomTOSD", "btRes1", "iCustomTOSDX", 
          "iCustomTOSDY", "chCustomTOSD", "colCustomOSD" });
  }
  
  public static class ByReference extends FHNP_EncodeOSD_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_EncodeOSD_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_EncodeOSD_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */