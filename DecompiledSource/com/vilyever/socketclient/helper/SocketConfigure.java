package com.vilyever.socketclient.helper;

public class SocketConfigure {
  private SocketClientAddress address;
  
  private String charsetName;
  
  private SocketHeartBeatHelper heartBeatHelper;
  
  final SocketConfigure self = this;
  
  private SocketPacketHelper socketPacketHelper;
  
  public SocketClientAddress getAddress() {
    return this.address;
  }
  
  public String getCharsetName() {
    return this.charsetName;
  }
  
  public SocketHeartBeatHelper getHeartBeatHelper() {
    return this.heartBeatHelper;
  }
  
  public SocketPacketHelper getSocketPacketHelper() {
    return this.socketPacketHelper;
  }
  
  public SocketConfigure setAddress(SocketClientAddress paramSocketClientAddress) {
    this.address = paramSocketClientAddress.copy();
    return this;
  }
  
  public SocketConfigure setCharsetName(String paramString) {
    this.charsetName = paramString;
    return this;
  }
  
  public SocketConfigure setHeartBeatHelper(SocketHeartBeatHelper paramSocketHeartBeatHelper) {
    this.heartBeatHelper = paramSocketHeartBeatHelper.copy();
    return this;
  }
  
  public SocketConfigure setSocketPacketHelper(SocketPacketHelper paramSocketPacketHelper) {
    this.socketPacketHelper = paramSocketPacketHelper.copy();
    return this;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/vilyever/socketclient/helper/SocketConfigure.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */