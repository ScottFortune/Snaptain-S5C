package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_ServerPort_t extends Structure {
  public int dwHttpPort;
  
  public FHNP_ServerPort_t() {}
  
  public FHNP_ServerPort_t(int paramInt) {
    this.dwHttpPort = paramInt;
  }
  
  public FHNP_ServerPort_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "dwHttpPort" });
  }
  
  public static class ByReference extends FHNP_ServerPort_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_ServerPort_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_ServerPort_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */