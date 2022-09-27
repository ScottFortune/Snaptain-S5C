package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;

public class FHDS_DevPrivateInfo_t extends Union {
  public FHDS_DECPriInfo_t stDECPriInfo;
  
  public FHDS_DVRPriInfo_t stDVRPriInfo;
  
  public FHDS_DVSPriInfo_t stDVSPriInfo;
  
  public FHDS_IPCPriInfo_t stIPCPriInfo;
  
  public FHDS_NVRPriInfo_t stNVRPriInfo;
  
  public FHDS_NVSPriInfo_t stNVSPriInfo;
  
  public FHDS_DevPrivateInfo_t() {}
  
  public FHDS_DevPrivateInfo_t(FHDS_DECPriInfo_t paramFHDS_DECPriInfo_t) {
    this.stDECPriInfo = paramFHDS_DECPriInfo_t;
    setType(FHDS_DECPriInfo_t.class);
  }
  
  public FHDS_DevPrivateInfo_t(FHDS_DVRPriInfo_t paramFHDS_DVRPriInfo_t) {
    this.stDVRPriInfo = paramFHDS_DVRPriInfo_t;
    setType(FHDS_DVRPriInfo_t.class);
  }
  
  public FHDS_DevPrivateInfo_t(FHDS_DVSPriInfo_t paramFHDS_DVSPriInfo_t) {
    this.stDVSPriInfo = paramFHDS_DVSPriInfo_t;
    setType(FHDS_DVSPriInfo_t.class);
  }
  
  public FHDS_DevPrivateInfo_t(FHDS_IPCPriInfo_t paramFHDS_IPCPriInfo_t) {
    this.stIPCPriInfo = paramFHDS_IPCPriInfo_t;
    setType(FHDS_IPCPriInfo_t.class);
  }
  
  public FHDS_DevPrivateInfo_t(FHDS_NVRPriInfo_t paramFHDS_NVRPriInfo_t) {
    this.stNVRPriInfo = paramFHDS_NVRPriInfo_t;
    setType(FHDS_NVRPriInfo_t.class);
  }
  
  public FHDS_DevPrivateInfo_t(FHDS_NVSPriInfo_t paramFHDS_NVSPriInfo_t) {
    this.stNVSPriInfo = paramFHDS_NVSPriInfo_t;
    setType(FHDS_NVSPriInfo_t.class);
  }
  
  public FHDS_DevPrivateInfo_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  public static class ByReference extends FHDS_DevPrivateInfo_t implements Structure.ByReference {}
  
  public static class ByValue extends FHDS_DevPrivateInfo_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHDS_DevPrivateInfo_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */