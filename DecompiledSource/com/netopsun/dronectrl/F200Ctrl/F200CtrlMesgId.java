package com.netopsun.dronectrl.F200Ctrl;

public enum F200CtrlMesgId {
  MSP_AHRS_DISPLAY, MSP_CAMER_CTRL, MSP_Circle, MSP_F200_ONEKEY_FUN, MSP_F200_Unknown, MSP_FollowMe, MSP_GPS_DISPLAY_NAVIGATION, MSP_RC_DATA, MSP_READ_WayPoint, MSP_SENSOR_CALIBRATION_SETUP, MSP_SETUP_WayPoint, MSP_WayPointMode;
  
  static {
    MSP_AHRS_DISPLAY = new F200CtrlMesgId("MSP_AHRS_DISPLAY", 2);
    MSP_GPS_DISPLAY_NAVIGATION = new F200CtrlMesgId("MSP_GPS_DISPLAY_NAVIGATION", 3);
    MSP_FollowMe = new F200CtrlMesgId("MSP_FollowMe", 4);
    MSP_Circle = new F200CtrlMesgId("MSP_Circle", 5);
    MSP_WayPointMode = new F200CtrlMesgId("MSP_WayPointMode", 6);
    MSP_RC_DATA = new F200CtrlMesgId("MSP_RC_DATA", 7);
    MSP_CAMER_CTRL = new F200CtrlMesgId("MSP_CAMER_CTRL", 8);
    MSP_READ_WayPoint = new F200CtrlMesgId("MSP_READ_WayPoint", 9);
    MSP_SETUP_WayPoint = new F200CtrlMesgId("MSP_SETUP_WayPoint", 10);
    MSP_F200_Unknown = new F200CtrlMesgId("MSP_F200_Unknown", 11);
    $VALUES = new F200CtrlMesgId[] { 
        MSP_F200_ONEKEY_FUN, MSP_SENSOR_CALIBRATION_SETUP, MSP_AHRS_DISPLAY, MSP_GPS_DISPLAY_NAVIGATION, MSP_FollowMe, MSP_Circle, MSP_WayPointMode, MSP_RC_DATA, MSP_CAMER_CTRL, MSP_READ_WayPoint, 
        MSP_SETUP_WayPoint, MSP_F200_Unknown };
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/F200Ctrl/F200CtrlMesgId.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */