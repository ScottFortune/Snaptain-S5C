package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_Defence_EnventRecEx_t extends Structure {
  public byte btCfgID;
  
  public byte btEncID;
  
  public byte btRecAudio;
  
  public byte btRes;
  
  public int dwExtRecTime;
  
  public int dwPreRecTime;
  
  public int dwShotFrame;
  
  public int dwShotInterval;
  
  public FHNP_Defence_EnventRecEx_t() {}
  
  public FHNP_Defence_EnventRecEx_t(int paramInt1, int paramInt2, byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, int paramInt3, int paramInt4) {
    this.dwPreRecTime = paramInt1;
    this.dwExtRecTime = paramInt2;
    this.btEncID = (byte)paramByte1;
    this.btCfgID = (byte)paramByte2;
    this.btRecAudio = (byte)paramByte3;
    this.btRes = (byte)paramByte4;
    this.dwShotInterval = paramInt3;
    this.dwShotFrame = paramInt4;
  }
  
  public FHNP_Defence_EnventRecEx_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "dwPreRecTime", "dwExtRecTime", "btEncID", "btCfgID", "btRecAudio", "btRes", "dwShotInterval", "dwShotFrame" });
  }
  
  public static class ByReference extends FHNP_Defence_EnventRecEx_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_Defence_EnventRecEx_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_Defence_EnventRecEx_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */