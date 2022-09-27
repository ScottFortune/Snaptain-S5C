package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_Functions_t extends Structure {
  public byte btFuncAlarm;
  
  public byte btFuncAudio;
  
  public byte btFuncCoverDetect;
  
  public byte btFuncDefence;
  
  public byte btFuncEtherNet;
  
  public byte btFuncGlobal;
  
  public byte btFuncHardDisk;
  
  public byte btFuncHeartbeat;
  
  public byte btFuncIPV4;
  
  public byte btFuncIPV6;
  
  public byte btFuncIotc;
  
  public byte btFuncIsp;
  
  public byte btFuncJpeg;
  
  public byte btFuncLog;
  
  public byte btFuncMoveDetect;
  
  public byte btFuncOnvif;
  
  public byte btFuncPlayback;
  
  public byte btFuncPreview;
  
  public byte btFuncPtz;
  
  public byte btFuncRecord;
  
  public byte[] btFuncReserve = new byte[33];
  
  public byte btFuncRfs;
  
  public byte btFuncRtsp;
  
  public byte btFuncSDCard;
  
  public byte btFuncSerialPort;
  
  public byte btFuncTalk;
  
  public byte btFuncUsbDisk;
  
  public byte btFuncUser;
  
  public byte btFuncVideo;
  
  public byte btFuncVideoCover;
  
  public byte btFuncVveye;
  
  public byte btFuncWifi;
  
  public FHNP_Functions_t() {}
  
  public FHNP_Functions_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { 
          "btFuncGlobal", "btFuncVideo", "btFuncAudio", "btFuncTalk", "btFuncJpeg", "btFuncMoveDetect", "btFuncCoverDetect", "btFuncVideoCover", "btFuncPreview", "btFuncIsp", 
          "btFuncUser", "btFuncPtz", "btFuncLog", "btFuncHeartbeat", "btFuncDefence", "btFuncHardDisk", "btFuncUsbDisk", "btFuncSDCard", "btFuncRecord", "btFuncPlayback", 
          "btFuncRfs", "btFuncAlarm", "btFuncEtherNet", "btFuncWifi", "btFuncSerialPort", "btFuncOnvif", "btFuncRtsp", "btFuncIotc", "btFuncVveye", "btFuncIPV4", 
          "btFuncIPV6", "btFuncReserve" });
  }
  
  public static class ByReference extends FHNP_Functions_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_Functions_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_Functions_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */