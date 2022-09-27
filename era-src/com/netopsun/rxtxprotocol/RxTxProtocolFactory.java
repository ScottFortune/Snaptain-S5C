package com.netopsun.rxtxprotocol;

import com.netopsun.deviceshub.base.RxTxCommunicator;
import com.netopsun.rxtxprotocol.base.RxTxProtocol;
import com.netopsun.rxtxprotocol.blue_light_gps_protocol.BlueLightGPSProtocol;
import com.netopsun.rxtxprotocol.dfs_gps_protocol.DFSGPSProtocol;
import com.netopsun.rxtxprotocol.f200_gps_protocol.F200GPSProtocol;
import com.netopsun.rxtxprotocol.hy_blue_light_gps_protocol.HYBlueLightGPSProtocol;
import com.netopsun.rxtxprotocol.lw_gps_protocol.LWGPSProtocol;
import com.netopsun.rxtxprotocol.optical_flow_drone_protocol.OpticalFlowDroneProtocol;
import com.netopsun.rxtxprotocol.simple_drone_protocol.SimpleDroneProtocol;
import com.netopsun.rxtxprotocol.snap_blue_light_gps_protocol.SnapBlueLightGPSProtocol;

public class RxTxProtocolFactory {
  public static RxTxProtocol createByName(String paramString, RxTxCommunicator paramRxTxCommunicator) {
    byte b;
    switch (paramString.hashCode()) {
      default:
        b = -1;
        break;
      case 1453753878:
        if (paramString.equals("F200GPSProtocol")) {
          b = 6;
          break;
        } 
      case 852218001:
        if (paramString.equals("DFSGPSProtocol")) {
          b = 7;
          break;
        } 
      case -238051042:
        if (paramString.equals("SimpleDroneProtocol")) {
          b = 0;
          break;
        } 
      case -305313385:
        if (paramString.equals("HYBlueLightGPSProtocol")) {
          b = 5;
          break;
        } 
      case -373621033:
        if (paramString.equals("LWGPSProtocol")) {
          b = 2;
          break;
        } 
      case -932463120:
        if (paramString.equals("SnapBlueLightGPSProtocol")) {
          b = 4;
          break;
        } 
      case -1224510714:
        if (paramString.equals("BlueLightGPSProtocol")) {
          b = 3;
          break;
        } 
      case -1963848118:
        if (paramString.equals("OpticalFlowDroneProtocol")) {
          b = 1;
          break;
        } 
    } 
    switch (b) {
      default:
        return null;
      case 7:
        return (RxTxProtocol)new DFSGPSProtocol(paramRxTxCommunicator);
      case 6:
        return (RxTxProtocol)new F200GPSProtocol(paramRxTxCommunicator);
      case 5:
        return (RxTxProtocol)new HYBlueLightGPSProtocol(paramRxTxCommunicator);
      case 4:
        return (RxTxProtocol)new SnapBlueLightGPSProtocol(paramRxTxCommunicator);
      case 3:
        return (RxTxProtocol)new BlueLightGPSProtocol(paramRxTxCommunicator);
      case 2:
        return (RxTxProtocol)new LWGPSProtocol(paramRxTxCommunicator);
      case 1:
        return (RxTxProtocol)new OpticalFlowDroneProtocol(paramRxTxCommunicator);
      case 0:
        break;
    } 
    return (RxTxProtocol)new SimpleDroneProtocol(paramRxTxCommunicator);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/rxtxprotocol/RxTxProtocolFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */