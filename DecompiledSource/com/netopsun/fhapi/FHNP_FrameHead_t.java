package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;

public class FHNP_FrameHead_t extends Union {
  public FHNP_AFrameHead_t stAFrameHead;
  
  public FHNP_JFrameHead_t stJFrameHead;
  
  public FHNP_VFrameHead_t stVFrameHead;
  
  public FHNP_FrameHead_t() {}
  
  public FHNP_FrameHead_t(FHNP_AFrameHead_t paramFHNP_AFrameHead_t) {
    this.stAFrameHead = paramFHNP_AFrameHead_t;
    setType(FHNP_AFrameHead_t.class);
  }
  
  public FHNP_FrameHead_t(FHNP_JFrameHead_t paramFHNP_JFrameHead_t) {
    this.stJFrameHead = paramFHNP_JFrameHead_t;
    setType(FHNP_JFrameHead_t.class);
  }
  
  public FHNP_FrameHead_t(FHNP_VFrameHead_t paramFHNP_VFrameHead_t) {
    this.stVFrameHead = paramFHNP_VFrameHead_t;
    setType(FHNP_VFrameHead_t.class);
  }
  
  public FHNP_FrameHead_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  public static class ByReference extends FHNP_FrameHead_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_FrameHead_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_FrameHead_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */