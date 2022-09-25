package com.netopsun.deviceshub;

import com.netopsun.deviceshub.base.Devices;
import java.lang.reflect.InvocationTargetException;

public class DevicesHub {
  public static Devices open(String paramString) {
    try {
      Devices devices = (Devices)Class.forName("com.netopsun.tutkdevices.TUTKDevices").getMethod("open", new Class[] { String.class }).invoke(null, new Object[] { paramString });
      if (devices != null)
        return devices; 
    } catch (ClassNotFoundException classNotFoundException) {
      classNotFoundException.printStackTrace();
    } catch (NoSuchMethodException noSuchMethodException) {
      noSuchMethodException.printStackTrace();
    } catch (IllegalAccessException illegalAccessException) {
      illegalAccessException.printStackTrace();
    } catch (InvocationTargetException invocationTargetException) {
      invocationTargetException.printStackTrace();
    } 
    try {
      Devices devices = (Devices)Class.forName("com.netopsun.mr100devices.MR100Devices").getMethod("open", new Class[] { String.class }).invoke(null, new Object[] { paramString });
      if (devices != null)
        return devices; 
    } catch (ClassNotFoundException classNotFoundException) {
      classNotFoundException.printStackTrace();
    } catch (NoSuchMethodException noSuchMethodException) {
      noSuchMethodException.printStackTrace();
    } catch (IllegalAccessException illegalAccessException) {
      illegalAccessException.printStackTrace();
    } catch (InvocationTargetException invocationTargetException) {
      invocationTargetException.printStackTrace();
    } 
    try {
      Devices devices = (Devices)Class.forName("com.netopsun.fhdevices.FHDevices").getMethod("open", new Class[] { String.class }).invoke(null, new Object[] { paramString });
      if (devices != null)
        return devices; 
    } catch (ClassNotFoundException classNotFoundException) {
      classNotFoundException.printStackTrace();
    } catch (NoSuchMethodException noSuchMethodException) {
      noSuchMethodException.printStackTrace();
    } catch (IllegalAccessException illegalAccessException) {
      illegalAccessException.printStackTrace();
    } catch (InvocationTargetException invocationTargetException) {
      invocationTargetException.printStackTrace();
    } 
    try {
      Devices devices = (Devices)Class.forName("com.netopsun.anykadevices.AnykaDevices").getMethod("open", new Class[] { String.class }).invoke(null, new Object[] { paramString });
      if (devices != null)
        return devices; 
    } catch (ClassNotFoundException classNotFoundException) {
      classNotFoundException.printStackTrace();
    } catch (NoSuchMethodException noSuchMethodException) {
      noSuchMethodException.printStackTrace();
    } catch (IllegalAccessException illegalAccessException) {
      illegalAccessException.printStackTrace();
    } catch (InvocationTargetException invocationTargetException) {
      invocationTargetException.printStackTrace();
    } 
    try {
      Devices devices = (Devices)Class.forName("com.netopsun.jrdevices.JRDevices").getMethod("open", new Class[] { String.class }).invoke(null, new Object[] { paramString });
      if (devices != null)
        return devices; 
    } catch (ClassNotFoundException classNotFoundException) {
      classNotFoundException.printStackTrace();
    } catch (NoSuchMethodException noSuchMethodException) {
      noSuchMethodException.printStackTrace();
    } catch (IllegalAccessException illegalAccessException) {
      illegalAccessException.printStackTrace();
    } catch (InvocationTargetException invocationTargetException) {
      invocationTargetException.printStackTrace();
    } 
    try {
      Devices devices = (Devices)Class.forName("com.netopsun.bkdevices.BKDevices").getMethod("open", new Class[] { String.class }).invoke(null, new Object[] { paramString });
      if (devices != null)
        return devices; 
    } catch (ClassNotFoundException classNotFoundException) {
      classNotFoundException.printStackTrace();
    } catch (NoSuchMethodException noSuchMethodException) {
      noSuchMethodException.printStackTrace();
    } catch (IllegalAccessException illegalAccessException) {
      illegalAccessException.printStackTrace();
    } catch (InvocationTargetException invocationTargetException) {
      invocationTargetException.printStackTrace();
    } 
    return null;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/deviceshub/DevicesHub.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */