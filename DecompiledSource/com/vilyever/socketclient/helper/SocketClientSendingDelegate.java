package com.vilyever.socketclient.helper;

import com.vilyever.socketclient.SocketClient;

public interface SocketClientSendingDelegate {
  void onSendPacketBegin(SocketClient paramSocketClient, SocketPacket paramSocketPacket);
  
  void onSendPacketCancel(SocketClient paramSocketClient, SocketPacket paramSocketPacket);
  
  void onSendPacketEnd(SocketClient paramSocketClient, SocketPacket paramSocketPacket);
  
  void onSendingPacketInProgress(SocketClient paramSocketClient, SocketPacket paramSocketPacket, float paramFloat, int paramInt);
  
  public static class SimpleSocketClientSendingDelegate implements SocketClientSendingDelegate {
    public void onSendPacketBegin(SocketClient param1SocketClient, SocketPacket param1SocketPacket) {}
    
    public void onSendPacketCancel(SocketClient param1SocketClient, SocketPacket param1SocketPacket) {}
    
    public void onSendPacketEnd(SocketClient param1SocketClient, SocketPacket param1SocketPacket) {}
    
    public void onSendingPacketInProgress(SocketClient param1SocketClient, SocketPacket param1SocketPacket, float param1Float, int param1Int) {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/vilyever/socketclient/helper/SocketClientSendingDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */