package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_DebugParam_t extends Structure {
  public byte bSO_RCVBUF_UDP;
  
  public byte bSO_RCVTIMEO;
  
  public byte bSO_SNDTIMEO;
  
  public int dwSO_RCVBUF_UDP;
  
  public int dwSO_RCVTIMEO;
  
  public int dwSO_SNDTIMEO;
  
  public FHNP_DebugParam61_t stDbg61;
  
  public FHNP_DebugParam62_t stDbg62;
  
  public FHNP_DebugParam63_t stDbg63;
  
  public FHNP_DebugParam81_t stDbg81;
  
  public FHNP_DebugParam83_t stDbg83;
  
  public short wUDPPreviewBasePort;
  
  public FHNP_DebugParam_t() {}
  
  public FHNP_DebugParam_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { 
          "wUDPPreviewBasePort", "bSO_RCVBUF_UDP", "dwSO_RCVBUF_UDP", "bSO_SNDTIMEO", "dwSO_SNDTIMEO", "bSO_RCVTIMEO", "dwSO_RCVTIMEO", "stDbg61", "stDbg81", "stDbg62", 
          "stDbg63", "stDbg83" });
  }
  
  public static class ByReference extends FHNP_DebugParam_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_DebugParam_t implements Structure.ByValue {}
  
  public static class FHNP_DebugParam61_t extends Structure {
    public int dwRecvLoopBufLen;
    
    public FHNP_DebugParam61_t() {}
    
    public FHNP_DebugParam61_t(int param1Int) {
      this.dwRecvLoopBufLen = param1Int;
    }
    
    public FHNP_DebugParam61_t(Pointer param1Pointer) {
      super(param1Pointer);
    }
    
    protected List<String> getFieldOrder() {
      return Arrays.asList(new String[] { "dwRecvLoopBufLen" });
    }
    
    public static class ByReference extends FHNP_DebugParam61_t implements Structure.ByReference {}
    
    public static class ByValue extends FHNP_DebugParam61_t implements Structure.ByValue {}
  }
  
  public static class ByReference extends FHNP_DebugParam61_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_DebugParam61_t implements Structure.ByValue {}
  
  public static class FHNP_DebugParam62_t extends Structure {
    public byte bCheckFrameLen;
    
    public byte bCheckNextFHead;
    
    public int dwCheckMaxLen;
    
    public int dwRecvLoopBufLen;
    
    public FHNP_DebugParam62_t() {}
    
    public FHNP_DebugParam62_t(int param1Int1, byte param1Byte1, int param1Int2, byte param1Byte2) {
      this.dwRecvLoopBufLen = param1Int1;
      this.bCheckFrameLen = (byte)param1Byte1;
      this.dwCheckMaxLen = param1Int2;
      this.bCheckNextFHead = (byte)param1Byte2;
    }
    
    public FHNP_DebugParam62_t(Pointer param1Pointer) {
      super(param1Pointer);
    }
    
    protected List<String> getFieldOrder() {
      return Arrays.asList(new String[] { "dwRecvLoopBufLen", "bCheckFrameLen", "dwCheckMaxLen", "bCheckNextFHead" });
    }
    
    public static class ByReference extends FHNP_DebugParam62_t implements Structure.ByReference {}
    
    public static class ByValue extends FHNP_DebugParam62_t implements Structure.ByValue {}
  }
  
  public static class ByReference extends FHNP_DebugParam62_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_DebugParam62_t implements Structure.ByValue {}
  
  public static class FHNP_DebugParam63_t extends Structure {
    public byte bCheckFrameLen;
    
    public byte bCheckNextFHead;
    
    public int dwCheckMaxLen;
    
    public int dwRecvLoopBufLen;
    
    public FHNP_DebugParam63_t() {}
    
    public FHNP_DebugParam63_t(int param1Int1, byte param1Byte1, int param1Int2, byte param1Byte2) {
      this.dwRecvLoopBufLen = param1Int1;
      this.bCheckFrameLen = (byte)param1Byte1;
      this.dwCheckMaxLen = param1Int2;
      this.bCheckNextFHead = (byte)param1Byte2;
    }
    
    public FHNP_DebugParam63_t(Pointer param1Pointer) {
      super(param1Pointer);
    }
    
    protected List<String> getFieldOrder() {
      return Arrays.asList(new String[] { "dwRecvLoopBufLen", "bCheckFrameLen", "dwCheckMaxLen", "bCheckNextFHead" });
    }
    
    public static class ByReference extends FHNP_DebugParam63_t implements Structure.ByReference {}
    
    public static class ByValue extends FHNP_DebugParam63_t implements Structure.ByValue {}
  }
  
  public static class ByReference extends FHNP_DebugParam63_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_DebugParam63_t implements Structure.ByValue {}
  
  public static class FHNP_DebugParam81_t extends Structure {
    public byte bCheckFrameLen;
    
    public byte bCheckNextFHead;
    
    public int dwCheckMaxLen;
    
    public int dwRecvLoopBufLen;
    
    public FHNP_DebugParam81_t() {}
    
    public FHNP_DebugParam81_t(int param1Int1, byte param1Byte1, int param1Int2, byte param1Byte2) {
      this.dwRecvLoopBufLen = param1Int1;
      this.bCheckFrameLen = (byte)param1Byte1;
      this.dwCheckMaxLen = param1Int2;
      this.bCheckNextFHead = (byte)param1Byte2;
    }
    
    public FHNP_DebugParam81_t(Pointer param1Pointer) {
      super(param1Pointer);
    }
    
    protected List<String> getFieldOrder() {
      return Arrays.asList(new String[] { "dwRecvLoopBufLen", "bCheckFrameLen", "dwCheckMaxLen", "bCheckNextFHead" });
    }
    
    public static class ByReference extends FHNP_DebugParam81_t implements Structure.ByReference {}
    
    public static class ByValue extends FHNP_DebugParam81_t implements Structure.ByValue {}
  }
  
  public static class ByReference extends FHNP_DebugParam81_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_DebugParam81_t implements Structure.ByValue {}
  
  public static class FHNP_DebugParam83_t extends Structure {
    public byte bCheckFrameLen;
    
    public byte bCheckNextFHead;
    
    public int dwCheckMaxLen;
    
    public int dwRecvLoopBufLen;
    
    public FHNP_DebugParam83_t() {}
    
    public FHNP_DebugParam83_t(int param1Int1, byte param1Byte1, int param1Int2, byte param1Byte2) {
      this.dwRecvLoopBufLen = param1Int1;
      this.bCheckFrameLen = (byte)param1Byte1;
      this.dwCheckMaxLen = param1Int2;
      this.bCheckNextFHead = (byte)param1Byte2;
    }
    
    public FHNP_DebugParam83_t(Pointer param1Pointer) {
      super(param1Pointer);
    }
    
    protected List<String> getFieldOrder() {
      return Arrays.asList(new String[] { "dwRecvLoopBufLen", "bCheckFrameLen", "dwCheckMaxLen", "bCheckNextFHead" });
    }
    
    public static class ByReference extends FHNP_DebugParam83_t implements Structure.ByReference {}
    
    public static class ByValue extends FHNP_DebugParam83_t implements Structure.ByValue {}
  }
  
  public static class ByReference extends FHNP_DebugParam83_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_DebugParam83_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_DebugParam_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */