package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_TrigerGpioOut_t_struct extends Structure {
  public byte[] btEnable = new byte[8];
  
  public LPFHNP_TrigerGpioOut_t_struct() {}
  
  public LPFHNP_TrigerGpioOut_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  public LPFHNP_TrigerGpioOut_t_struct(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte.length == this.btEnable.length) {
      this.btEnable = paramArrayOfbyte;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btEnable" });
  }
  
  public static class ByReference extends LPFHNP_TrigerGpioOut_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_TrigerGpioOut_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_TrigerGpioOut_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */