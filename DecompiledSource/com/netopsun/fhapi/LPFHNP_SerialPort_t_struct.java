package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_SerialPort_t_struct extends Structure {
  public byte btFlowCtrl;
  
  public byte btParity;
  
  public byte[] btRes = new byte[2];
  
  public int iBaudRate;
  
  public int iDataBit;
  
  public int iStopBit;
  
  public LPFHNP_SerialPort_t_struct() {}
  
  public LPFHNP_SerialPort_t_struct(int paramInt1, int paramInt2, int paramInt3, byte paramByte1, byte paramByte2, byte[] paramArrayOfbyte) {
    this.iBaudRate = paramInt1;
    this.iDataBit = paramInt2;
    this.iStopBit = paramInt3;
    this.btParity = (byte)paramByte1;
    this.btFlowCtrl = (byte)paramByte2;
    if (paramArrayOfbyte.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public LPFHNP_SerialPort_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "iBaudRate", "iDataBit", "iStopBit", "btParity", "btFlowCtrl", "btRes" });
  }
  
  public static class ByReference extends LPFHNP_SerialPort_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_SerialPort_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_SerialPort_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */