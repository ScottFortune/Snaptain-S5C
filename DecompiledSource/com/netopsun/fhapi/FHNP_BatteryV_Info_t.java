package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_BatteryV_Info_t extends Structure {
  public byte btChargeFlag;
  
  public byte btFullFlag;
  
  public byte btLowVoltageFlag;
  
  public byte btWillShutdownFlag;
  
  public int dwCurrentVoltage;
  
  public int dwFullVoltage;
  
  public FHNP_BatteryV_Info_t() {}
  
  public FHNP_BatteryV_Info_t(int paramInt1, int paramInt2, byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4) {
    this.dwFullVoltage = paramInt1;
    this.dwCurrentVoltage = paramInt2;
    this.btChargeFlag = (byte)paramByte1;
    this.btFullFlag = (byte)paramByte2;
    this.btLowVoltageFlag = (byte)paramByte3;
    this.btWillShutdownFlag = (byte)paramByte4;
  }
  
  public FHNP_BatteryV_Info_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "dwFullVoltage", "dwCurrentVoltage", "btChargeFlag", "btFullFlag", "btLowVoltageFlag", "btWillShutdownFlag" });
  }
  
  public static class ByReference extends FHNP_BatteryV_Info_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_BatteryV_Info_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_BatteryV_Info_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */