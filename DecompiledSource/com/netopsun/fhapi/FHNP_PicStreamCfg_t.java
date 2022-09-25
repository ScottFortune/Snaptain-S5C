package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_PicStreamCfg_t extends Structure {
  public int dwFramerate;
  
  public int[] dwReserve = new int[3];
  
  public FHNP_PicStreamCfg_t() {}
  
  public FHNP_PicStreamCfg_t(int paramInt, int[] paramArrayOfint) {
    this.dwFramerate = paramInt;
    if (paramArrayOfint.length == this.dwReserve.length) {
      this.dwReserve = paramArrayOfint;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_PicStreamCfg_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "dwFramerate", "dwReserve" });
  }
  
  public static class ByReference extends FHNP_PicStreamCfg_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_PicStreamCfg_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_PicStreamCfg_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */