package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHDS_DevState_t_struct extends Structure {
  public int bIsAlive;
  
  public int dwDevType;
  
  public int dwTimeOut;
  
  public byte[] sDevVersion = new byte[48];
  
  public byte[] sDeviceName = new byte[32];
  
  public byte[] sSerialNumber = new byte[48];
  
  public FHDS_PubNetAddr_t stDevNetAddr;
  
  public FHDS_DevPrivateInfo_t stPriData;
  
  public LPFHDS_DevState_t_struct() {}
  
  public LPFHDS_DevState_t_struct(int paramInt1, FHDS_PubNetAddr_t paramFHDS_PubNetAddr_t, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, int paramInt2, int paramInt3, FHDS_DevPrivateInfo_t paramFHDS_DevPrivateInfo_t) {
    this.dwDevType = paramInt1;
    this.stDevNetAddr = paramFHDS_PubNetAddr_t;
    if (paramArrayOfbyte1.length == this.sDevVersion.length) {
      this.sDevVersion = paramArrayOfbyte1;
      if (paramArrayOfbyte2.length == this.sDeviceName.length) {
        this.sDeviceName = paramArrayOfbyte2;
        if (paramArrayOfbyte3.length == this.sSerialNumber.length) {
          this.sSerialNumber = paramArrayOfbyte3;
          this.dwTimeOut = paramInt2;
          this.bIsAlive = paramInt3;
          this.stPriData = paramFHDS_DevPrivateInfo_t;
          return;
        } 
        throw new IllegalArgumentException("Wrong array size !");
      } 
      throw new IllegalArgumentException("Wrong array size !");
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public LPFHDS_DevState_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "dwDevType", "stDevNetAddr", "sDevVersion", "sDeviceName", "sSerialNumber", "dwTimeOut", "bIsAlive", "stPriData" });
  }
  
  public static class ByReference extends LPFHDS_DevState_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHDS_DevState_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHDS_DevState_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */