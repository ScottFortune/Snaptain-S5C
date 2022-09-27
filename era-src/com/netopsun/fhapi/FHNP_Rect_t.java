package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_Rect_t extends Structure {
  public int h;
  
  public int w;
  
  public int x;
  
  public int y;
  
  public FHNP_Rect_t() {}
  
  public FHNP_Rect_t(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.x = paramInt1;
    this.y = paramInt2;
    this.w = paramInt3;
    this.h = paramInt4;
  }
  
  public FHNP_Rect_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "x", "y", "w", "h" });
  }
  
  public static class ByReference extends FHNP_Rect_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_Rect_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_Rect_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */