package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_ReferenceMacro_t_struct extends Structure {
  public int iBaseHeight;
  
  public int iBaseWidth;
  
  public int iUnitHeight;
  
  public int iUnitWidth;
  
  public LPFHNP_ReferenceMacro_t_struct() {}
  
  public LPFHNP_ReferenceMacro_t_struct(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.iBaseWidth = paramInt1;
    this.iBaseHeight = paramInt2;
    this.iUnitWidth = paramInt3;
    this.iUnitHeight = paramInt4;
  }
  
  public LPFHNP_ReferenceMacro_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "iBaseWidth", "iBaseHeight", "iUnitWidth", "iUnitHeight" });
  }
  
  public static class ByReference extends LPFHNP_ReferenceMacro_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_ReferenceMacro_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_ReferenceMacro_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */