package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_ScheduleSegment_t extends Structure {
  public byte btEnable;
  
  public byte btRecAudio;
  
  public byte btRecCfgID;
  
  public byte btRecEncID;
  
  public byte btStartHour;
  
  public byte btStartMinute;
  
  public byte btStopHour;
  
  public byte btStopMinute;
  
  public FHNP_ScheduleSegment_t() {}
  
  public FHNP_ScheduleSegment_t(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, byte paramByte5, byte paramByte6, byte paramByte7, byte paramByte8) {
    this.btEnable = (byte)paramByte1;
    this.btStartHour = (byte)paramByte2;
    this.btStartMinute = (byte)paramByte3;
    this.btStopHour = (byte)paramByte4;
    this.btStopMinute = (byte)paramByte5;
    this.btRecEncID = (byte)paramByte6;
    this.btRecCfgID = (byte)paramByte7;
    this.btRecAudio = (byte)paramByte8;
  }
  
  public FHNP_ScheduleSegment_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btEnable", "btStartHour", "btStartMinute", "btStopHour", "btStopMinute", "btRecEncID", "btRecCfgID", "btRecAudio" });
  }
  
  public static class ByReference extends FHNP_ScheduleSegment_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_ScheduleSegment_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_ScheduleSegment_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */