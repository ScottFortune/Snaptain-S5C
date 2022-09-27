package com.vilyever.socketclient.helper;

import com.vilyever.socketclient.SocketClient;

public interface SocketClientDelegate {
  void onConnected(SocketClient paramSocketClient);
  
  void onDisconnected(SocketClient paramSocketClient);
  
  void onResponse(SocketClient paramSocketClient, SocketResponsePacket paramSocketResponsePacket);
  
  public static class SimpleSocketClientDelegate implements SocketClientDelegate {
    public void onConnected(SocketClient param1SocketClient) {}
    
    public void onDisconnected(SocketClient param1SocketClient) {}
    
    public void onResponse(SocketClient param1SocketClient, SocketResponsePacket param1SocketResponsePacket) {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/vilyever/socketclient/helper/SocketClientDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */