package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_ReferenceMacro_t extends Structure {
  public int iBaseHeight;
  
  public int iBaseWidth;
  
  public int iUnitHeight;
  
  public int iUnitWidth;
  
  public FHNP_ReferenceMacro_t() {}
  
  public FHNP_ReferenceMacro_t(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.iBaseWidth = paramInt1;
    this.iBaseHeight = paramInt2;
    this.iUnitWidth = paramInt3;
    this.iUnitHeight = paramInt4;
  }
  
  public FHNP_ReferenceMacro_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "iBaseWidth", "iBaseHeight", "iUnitWidth", "iUnitHeight" });
  }
  
  public static class ByReference extends FHNP_ReferenceMacro_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_ReferenceMacro_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_ReferenceMacro_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */