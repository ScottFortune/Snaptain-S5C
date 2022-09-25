package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_ISPConfig_t_struct extends Structure {
  public int dwFrameRate;
  
  public int dwHeight;
  
  public int dwWidth;
  
  public LPFHNP_ISPConfig_t_struct() {}
  
  public LPFHNP_ISPConfig_t_struct(int paramInt1, int paramInt2, int paramInt3) {
    this.dwWidth = paramInt1;
    this.dwHeight = paramInt2;
    this.dwFrameRate = paramInt3;
  }
  
  public LPFHNP_ISPConfig_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "dwWidth", "dwHeight", "dwFrameRate" });
  }
  
  public static class ByReference extends LPFHNP_ISPConfig_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_ISPConfig_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_ISPConfig_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */