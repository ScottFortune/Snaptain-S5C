package com.vilyever.socketclient.server;

import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.helper.SocketClientAddress;
import com.vilyever.socketclient.helper.SocketConfigure;
import java.net.Socket;

public class SocketServerClient extends SocketClient {
  final SocketServerClient self = this;
  
  public SocketServerClient(Socket paramSocket, SocketConfigure paramSocketConfigure) {
    super(new SocketClientAddress(str, stringBuilder.toString()));
    setRunningSocket(paramSocket);
    getSocketConfigure().setCharsetName(paramSocketConfigure.getCharsetName()).setAddress(getAddress()).setHeartBeatHelper(paramSocketConfigure.getHeartBeatHelper()).setSocketPacketHelper(paramSocketConfigure.getSocketPacketHelper());
    internalOnConnected();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/vilyever/socketclient/server/SocketServerClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */