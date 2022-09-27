package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_PriDebugParam_t extends Structure {
  public byte bParseFramePush;
  
  public byte bRecvToFile;
  
  public byte bSaveRecWaitKeyFrame;
  
  public byte bStatisticParse;
  
  public byte bStatisticRecv;
  
  public byte bStatisticRecvFrame;
  
  public byte bTCPRecvNoCopy;
  
  public byte bTCPRecvNoDelay;
  
  public byte bTSAudioTypeIsAAC;
  
  public int dwContrlType;
  
  public int dwParseFrameMaxLen;
  
  public int dwRecvBufMaxLen;
  
  public FHNP_PriDebugParam_t() {}
  
  public FHNP_PriDebugParam_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { 
          "dwContrlType", "dwRecvBufMaxLen", "dwParseFrameMaxLen", "bStatisticRecvFrame", "bStatisticRecv", "bRecvToFile", "bStatisticParse", "bTCPRecvNoCopy", "bTCPRecvNoDelay", "bTSAudioTypeIsAAC", 
          "bParseFramePush", "bSaveRecWaitKeyFrame" });
  }
  
  public static class ByReference extends FHNP_PriDebugParam_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_PriDebugParam_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_PriDebugParam_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */