package com.netopsun.dronectrl;

import android.util.Log;
import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.helper.SocketClientAddress;
import com.vilyever.socketclient.helper.SocketPacket;

public class DebugSocketClient extends SocketClient {
  public DebugSocketClient(SocketClientAddress paramSocketClientAddress) {
    super(paramSocketClientAddress);
  }
  
  public static String ArrarytoHexString(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte == null)
      return "null"; 
    if (paramArrayOfbyte.length == 0)
      return "[]"; 
    StringBuilder stringBuilder = new StringBuilder(paramArrayOfbyte.length * 6);
    stringBuilder.append('[');
    stringBuilder.append(Integer.toHexString(paramArrayOfbyte[0] & 0xFF));
    for (byte b = 1; b < paramArrayOfbyte.length; b++) {
      stringBuilder.append(", ");
      stringBuilder.append(Integer.toHexString(paramArrayOfbyte[b] & 0xFF));
    } 
    stringBuilder.append(']');
    return stringBuilder.toString();
  }
  
  public SocketPacket sendData(byte[] paramArrayOfbyte) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("");
    stringBuilder.append(ArrarytoHexString(paramArrayOfbyte));
    Log.d("Packet:", stringBuilder.toString());
    return super.sendData(paramArrayOfbyte);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/DebugSocketClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */