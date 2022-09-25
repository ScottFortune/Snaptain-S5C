package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;

public class LPFHNP_FrameHead_t_union extends Union {
  public FHNP_AFrameHead_t stAFrameHead;
  
  public FHNP_JFrameHead_t stJFrameHead;
  
  public FHNP_VFrameHead_t stVFrameHead;
  
  public LPFHNP_FrameHead_t_union() {}
  
  public LPFHNP_FrameHead_t_union(FHNP_AFrameHead_t paramFHNP_AFrameHead_t) {
    this.stAFrameHead = paramFHNP_AFrameHead_t;
    setType(FHNP_AFrameHead_t.class);
  }
  
  public LPFHNP_FrameHead_t_union(FHNP_JFrameHead_t paramFHNP_JFrameHead_t) {
    this.stJFrameHead = paramFHNP_JFrameHead_t;
    setType(FHNP_JFrameHead_t.class);
  }
  
  public LPFHNP_FrameHead_t_union(FHNP_VFrameHead_t paramFHNP_VFrameHead_t) {
    this.stVFrameHead = paramFHNP_VFrameHead_t;
    setType(FHNP_VFrameHead_t.class);
  }
  
  public LPFHNP_FrameHead_t_union(Pointer paramPointer) {
    super(paramPointer);
  }
  
  public static class ByReference extends LPFHNP_FrameHead_t_union implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_FrameHead_t_union implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_FrameHead_t_union.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */