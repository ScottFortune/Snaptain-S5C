package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_NetConfig_t extends Structure {
  public byte btDynamicDNS;
  
  public byte btDynamicIP;
  
  public byte[] btRes1 = new byte[3];
  
  public byte[] btRes2 = new byte[3];
  
  public byte[] chMAC = new byte[32];
  
  public int dwPort;
  
  public FHNP_IPAddr_t stGateWay;
  
  public FHNP_IPAddr_t stHostDNS;
  
  public FHNP_IPAddr_t stIP;
  
  public FHNP_IPAddr_t stNetMask;
  
  public FHNP_IPAddr_t stSecondDNS;
  
  public FHNP_NetConfig_t() {}
  
  public FHNP_NetConfig_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { 
          "btDynamicIP", "btRes1", "stIP", "stNetMask", "stGateWay", "chMAC", "dwPort", "btDynamicDNS", "btRes2", "stHostDNS", 
          "stSecondDNS" });
  }
  
  public static class ByReference extends FHNP_NetConfig_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_NetConfig_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_NetConfig_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */