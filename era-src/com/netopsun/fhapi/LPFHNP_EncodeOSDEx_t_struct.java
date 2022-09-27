package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_EncodeOSDEx_t_struct extends Structure {
  public byte bShowWeek;
  
  public byte btFontEdge;
  
  public byte btFontSize;
  
  public byte[] btRes1 = new byte[2];
  
  public byte[] btRes2 = new byte[3];
  
  public byte[] btRes3 = new byte[3];
  
  public byte[] btRes4 = new byte[3];
  
  public byte btShowFrameNO;
  
  public byte btShowGOSD;
  
  public byte btShowTOSD1;
  
  public byte btShowTOSD2;
  
  public byte btShowTimeOSD;
  
  public byte btTimeFmt;
  
  public byte btTimeStd;
  
  public byte btTosdBGTransparency;
  
  public byte btTosdEdgeTransparency;
  
  public byte btTosdFontTransparency;
  
  public byte[] chTOSD2 = new byte[32];
  
  public int iFrameNoX;
  
  public int iFrameNoY;
  
  public int iGOSDAlpha;
  
  public int iGOSDX;
  
  public int iGOSDY;
  
  public int iTOSD1X;
  
  public int iTOSD1Y;
  
  public int iTOSD2X;
  
  public int iTOSD2Y;
  
  public int iTimeOSDX;
  
  public int iTimeOSDY;
  
  public int tosdBGColor;
  
  public int tosdEdgeColor;
  
  public int tosdFontColor;
  
  public LPFHNP_EncodeOSDEx_t_struct() {}
  
  public LPFHNP_EncodeOSDEx_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { 
          "tosdBGColor", "tosdFontColor", "tosdEdgeColor", "btTosdBGTransparency", "btTosdFontTransparency", "btTosdEdgeTransparency", "btFontSize", "btFontEdge", "btShowTOSD1", "btRes1", 
          "iTOSD1X", "iTOSD1Y", "btShowTOSD2", "btRes2", "iTOSD2X", "iTOSD2Y", "chTOSD2", "btShowTimeOSD", "bShowWeek", "btTimeFmt", 
          "btTimeStd", "iTimeOSDX", "iTimeOSDY", "btShowFrameNO", "btRes3", "iFrameNoX", "iFrameNoY", "btShowGOSD", "btRes4", "iGOSDX", 
          "iGOSDY", "iGOSDAlpha" });
  }
  
  public static class ByReference extends LPFHNP_EncodeOSDEx_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_EncodeOSDEx_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_EncodeOSDEx_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */