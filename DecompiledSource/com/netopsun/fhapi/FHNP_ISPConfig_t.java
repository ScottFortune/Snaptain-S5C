package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_ISPConfig_t extends Structure {
  public int dwFrameRate;
  
  public int dwHeight;
  
  public int dwWidth;
  
  public FHNP_ISPConfig_t() {}
  
  public FHNP_ISPConfig_t(int paramInt1, int paramInt2, int paramInt3) {
    this.dwWidth = paramInt1;
    this.dwHeight = paramInt2;
    this.dwFrameRate = paramInt3;
  }
  
  public FHNP_ISPConfig_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "dwWidth", "dwHeight", "dwFrameRate" });
  }
  
  public static class ByReference extends FHNP_ISPConfig_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_ISPConfig_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_ISPConfig_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */