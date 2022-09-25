package com.vilyever.socketclient.helper;

import com.vilyever.socketclient.SocketClient;

public interface SocketClientReceivingDelegate {
  void onReceivePacketBegin(SocketClient paramSocketClient, SocketResponsePacket paramSocketResponsePacket);
  
  void onReceivePacketCancel(SocketClient paramSocketClient, SocketResponsePacket paramSocketResponsePacket);
  
  void onReceivePacketEnd(SocketClient paramSocketClient, SocketResponsePacket paramSocketResponsePacket);
  
  void onReceivingPacketInProgress(SocketClient paramSocketClient, SocketResponsePacket paramSocketResponsePacket, float paramFloat, int paramInt);
  
  public static class SimpleSocketClientReceiveDelegate implements SocketClientReceivingDelegate {
    public void onReceivePacketBegin(SocketClient param1SocketClient, SocketResponsePacket param1SocketResponsePacket) {}
    
    public void onReceivePacketCancel(SocketClient param1SocketClient, SocketResponsePacket param1SocketResponsePacket) {}
    
    public void onReceivePacketEnd(SocketClient param1SocketClient, SocketResponsePacket param1SocketResponsePacket) {}
    
    public void onReceivingPacketInProgress(SocketClient param1SocketClient, SocketResponsePacket param1SocketResponsePacket, float param1Float, int param1Int) {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/vilyever/socketclient/helper/SocketClientReceivingDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */