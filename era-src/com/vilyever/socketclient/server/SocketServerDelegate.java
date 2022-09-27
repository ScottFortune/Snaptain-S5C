package com.vilyever.socketclient.server;

public interface SocketServerDelegate {
  void onClientConnected(SocketServer paramSocketServer, SocketServerClient paramSocketServerClient);
  
  void onClientDisconnected(SocketServer paramSocketServer, SocketServerClient paramSocketServerClient);
  
  void onServerBeginListen(SocketServer paramSocketServer, int paramInt);
  
  void onServerStopListen(SocketServer paramSocketServer, int paramInt);
  
  public static class SimpleSocketServerDelegate implements SocketServerDelegate {
    public void onClientConnected(SocketServer param1SocketServer, SocketServerClient param1SocketServerClient) {}
    
    public void onClientDisconnected(SocketServer param1SocketServer, SocketServerClient param1SocketServerClient) {}
    
    public void onServerBeginListen(SocketServer param1SocketServer, int param1Int) {}
    
    public void onServerStopListen(SocketServer param1SocketServer, int param1Int) {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/vilyever/socketclient/server/SocketServerDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */