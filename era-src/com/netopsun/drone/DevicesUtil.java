package com.netopsun.drone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import com.netopsun.deviceshub.DevicesHub;
import com.netopsun.deviceshub.base.Devices;
import java.util.Objects;

public class DevicesUtil {
  private static Devices currentConnectDevices;
  
  private static String lastIpAddress;
  
  public static Devices getCurrentConnectDevices() {
    return currentConnectDevices;
  }
  
  public static String getLocalIpAddress(Context paramContext) {
    try {
      return int2ip(((WifiManager)paramContext.getApplicationContext().getSystemService("wifi")).getConnectionInfo().getIpAddress());
    } catch (Exception exception) {
      exception.printStackTrace();
      return "";
    } 
  }
  
  public static void initDevices(Context paramContext) {
    paramContext = paramContext.getApplicationContext();
    if (((ConnectivityManager)paramContext.getSystemService("connectivity")).getNetworkInfo(1) != null) {
      String str = getLocalIpAddress(paramContext);
      if (TextUtils.equals(str, lastIpAddress))
        return; 
      if (str.contains("172.19")) {
        Devices devices = currentConnectDevices;
        if (!(devices instanceof com.netopsun.fhdevices.FHDevices)) {
          if (devices != null) {
            devices.close();
            currentConnectDevices.release();
          } 
          currentConnectDevices = DevicesHub.open("fh://baudRate=19200");
        } else {
          devices.updateParams("fh://baudRate=19200");
        } 
      } else if (str.contains("172.17.")) {
        Devices devices = currentConnectDevices;
        if (!(devices instanceof com.netopsun.fhdevices.FHDevices)) {
          if (devices != null) {
            devices.close();
            currentConnectDevices.release();
          } 
          currentConnectDevices = DevicesHub.open("fh://ip=172.17.10.1&port=8888&baudRate=19200");
        } else {
          devices.updateParams("fh://ip=172.17.10.1&port=8888&baudRate=19200");
        } 
      } else if (str.contains("192.168.1")) {
        Devices devices = currentConnectDevices;
        if (!(devices instanceof com.netopsun.jrdevices.JRDevices)) {
          if (devices != null) {
            devices.close();
            currentConnectDevices.release();
          } 
          currentConnectDevices = DevicesHub.open("jr://rtspip=192.168.1.1");
        } else {
          devices.updateParams("jr://rtspip=192.168.1.1");
        } 
      } else if (str.contains("192.168.99")) {
        Devices devices = currentConnectDevices;
        if (!(devices instanceof com.netopsun.jrdevices.JRDevices)) {
          if (devices != null) {
            devices.close();
            currentConnectDevices.release();
          } 
          currentConnectDevices = DevicesHub.open("jr://rtspip=192.168.99.1");
        } else {
          devices.updateParams("jr://rtspip=192.168.99.1");
        } 
      } else if (str.contains("192.168.0")) {
        Devices devices = currentConnectDevices;
        if (!(devices instanceof com.netopsun.anykadevices.AnykaDevices)) {
          if (devices != null) {
            devices.close();
            currentConnectDevices.release();
          } 
          currentConnectDevices = DevicesHub.open("anyka://rxtxmode=tcp");
        } else {
          devices.updateParams("anyka://rxtxmode=tcp");
        } 
      } else {
        Devices devices = currentConnectDevices;
        if (devices != null) {
          devices.close();
          currentConnectDevices.release();
          currentConnectDevices = null;
        } 
      } 
      lastIpAddress = str;
    } else {
      Devices devices = currentConnectDevices;
      if (devices != null) {
        devices.close();
        currentConnectDevices.release();
        currentConnectDevices = null;
      } 
      lastIpAddress = null;
    } 
  }
  
  public static String int2ip(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramInt & 0xFF);
    stringBuilder.append(".");
    stringBuilder.append(paramInt >> 8 & 0xFF);
    stringBuilder.append(".");
    stringBuilder.append(paramInt >> 16 & 0xFF);
    stringBuilder.append(".");
    stringBuilder.append(paramInt >> 24 & 0xFF);
    return stringBuilder.toString();
  }
  
  public static boolean isDevicesStillConnected(Context paramContext) {
    String str = getLocalIpAddress(paramContext);
    return Objects.equals(lastIpAddress, str);
  }
  
  public static void releaseDevices() {
    Devices devices = currentConnectDevices;
    if (devices != null) {
      devices.release();
      lastIpAddress = null;
      currentConnectDevices = null;
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/DevicesUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */