package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_EncodeConfig_t_struct extends Structure {
  public FHNP_EncodeAudio_t stAudio;
  
  public FHNP_EncodeCover_t stCover;
  
  public FHNP_EncodeJpeg_t stJpeg;
  
  public FHNP_EncodeVAdvanced_t stVAdv;
  
  public FHNP_EncodeVideo_t stVideo;
  
  public FHNP_EncodeWaterMark_t stWM;
  
  public LPFHNP_EncodeConfig_t_struct() {}
  
  public LPFHNP_EncodeConfig_t_struct(FHNP_EncodeVideo_t paramFHNP_EncodeVideo_t, FHNP_EncodeAudio_t paramFHNP_EncodeAudio_t, FHNP_EncodeJpeg_t paramFHNP_EncodeJpeg_t, FHNP_EncodeCover_t paramFHNP_EncodeCover_t, FHNP_EncodeWaterMark_t paramFHNP_EncodeWaterMark_t, FHNP_EncodeVAdvanced_t paramFHNP_EncodeVAdvanced_t) {
    this.stVideo = paramFHNP_EncodeVideo_t;
    this.stAudio = paramFHNP_EncodeAudio_t;
    this.stJpeg = paramFHNP_EncodeJpeg_t;
    this.stCover = paramFHNP_EncodeCover_t;
    this.stWM = paramFHNP_EncodeWaterMark_t;
    this.stVAdv = paramFHNP_EncodeVAdvanced_t;
  }
  
  public LPFHNP_EncodeConfig_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "stVideo", "stAudio", "stJpeg", "stCover", "stWM", "stVAdv" });
  }
  
  public static class ByReference extends LPFHNP_EncodeConfig_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_EncodeConfig_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_EncodeConfig_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */