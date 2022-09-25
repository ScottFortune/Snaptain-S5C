package com.netopsun.dronectrl.LWGPSCtrl;

public enum LWGPSCtrlMesgId {
  MSP_AHRS_DISPLAY, MSP_CAMER_CTRL, MSP_Camera_Position, MSP_Circle, MSP_FollowMe, MSP_LWGPS_ONEKEY_FUN, MSP_LWGPS_ONEKEY_FUN2, MSP_LWGPS_ONEKEY_FUN3, MSP_LWGPS_Unknown, MSP_RC_DATA, MSP_READ_WayPoint, MSP_SENSOR_CALIBRATION_SETUP, MSP_SETUP_WayPoint, MSP_WayPointMode;
  
  static {
    MSP_AHRS_DISPLAY = new LWGPSCtrlMesgId("MSP_AHRS_DISPLAY", 4);
    MSP_FollowMe = new LWGPSCtrlMesgId("MSP_FollowMe", 5);
    MSP_Circle = new LWGPSCtrlMesgId("MSP_Circle", 6);
    MSP_WayPointMode = new LWGPSCtrlMesgId("MSP_WayPointMode", 7);
    MSP_RC_DATA = new LWGPSCtrlMesgId("MSP_RC_DATA", 8);
    MSP_CAMER_CTRL = new LWGPSCtrlMesgId("MSP_CAMER_CTRL", 9);
    MSP_READ_WayPoint = new LWGPSCtrlMesgId("MSP_READ_WayPoint", 10);
    MSP_SETUP_WayPoint = new LWGPSCtrlMesgId("MSP_SETUP_WayPoint", 11);
    MSP_LWGPS_Unknown = new LWGPSCtrlMesgId("MSP_LWGPS_Unknown", 12);
    MSP_Camera_Position = new LWGPSCtrlMesgId("MSP_Camera_Position", 13);
    $VALUES = new LWGPSCtrlMesgId[] { 
        MSP_LWGPS_ONEKEY_FUN, MSP_LWGPS_ONEKEY_FUN2, MSP_LWGPS_ONEKEY_FUN3, MSP_SENSOR_CALIBRATION_SETUP, MSP_AHRS_DISPLAY, MSP_FollowMe, MSP_Circle, MSP_WayPointMode, MSP_RC_DATA, MSP_CAMER_CTRL, 
        MSP_READ_WayPoint, MSP_SETUP_WayPoint, MSP_LWGPS_Unknown, MSP_Camera_Position };
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LWGPSCtrl/LWGPSCtrlMesgId.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */