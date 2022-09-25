package com.vilyever.socketclient.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Iterator;

public class IPUtil {
  final IPUtil self = this;
  
  public static String getLocalIPAddress(boolean paramBoolean) {
    try {
      Iterator<NetworkInterface> iterator = Collections.<NetworkInterface>list(NetworkInterface.getNetworkInterfaces()).iterator();
      while (iterator.hasNext()) {
        for (InetAddress inetAddress : Collections.<InetAddress>list(((NetworkInterface)iterator.next()).getInetAddresses())) {
          if (!inetAddress.isLoopbackAddress()) {
            int i;
            String str = inetAddress.getHostAddress();
            if (str.indexOf(':') < 0) {
              i = 1;
            } else {
              i = 0;
            } 
            if (paramBoolean) {
              if (i)
                return str; 
              continue;
            } 
            if (!i) {
              String str1;
              i = str.indexOf('%');
              if (i < 0) {
                str1 = str.toUpperCase();
              } else {
                str1 = str.substring(0, i).toUpperCase();
              } 
              return str1;
            } 
          } 
        } 
      } 
    } catch (Exception exception) {}
    return "";
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/vilyever/socketclient/util/IPUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */