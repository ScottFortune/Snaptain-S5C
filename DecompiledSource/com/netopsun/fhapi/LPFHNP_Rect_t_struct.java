package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_Rect_t_struct extends Structure {
  public int h;
  
  public int w;
  
  public int x;
  
  public int y;
  
  public LPFHNP_Rect_t_struct() {}
  
  public LPFHNP_Rect_t_struct(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.x = paramInt1;
    this.y = paramInt2;
    this.w = paramInt3;
    this.h = paramInt4;
  }
  
  public LPFHNP_Rect_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "x", "y", "w", "h" });
  }
  
  public static class ByReference extends LPFHNP_Rect_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_Rect_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_Rect_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */