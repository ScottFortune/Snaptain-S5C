package com.vilyever.socketclient.helper;

import java.util.Arrays;

public class SocketHeartBeatHelper {
  private byte[] defaultReceiveData;
  
  private byte[] defaultSendData;
  
  private long heartBeatInterval;
  
  private SocketHeartBeatHelper original;
  
  private ReceiveHeartBeatPacketChecker receiveHeartBeatPacketChecker;
  
  final SocketHeartBeatHelper self = this;
  
  private SendDataBuilder sendDataBuilder;
  
  private boolean sendHeartBeatEnabled;
  
  public SocketHeartBeatHelper copy() {
    SocketHeartBeatHelper socketHeartBeatHelper = new SocketHeartBeatHelper();
    socketHeartBeatHelper.setOriginal(this);
    socketHeartBeatHelper.setDefaultSendData(getDefaultSendData());
    socketHeartBeatHelper.setSendDataBuilder(getSendDataBuilder());
    socketHeartBeatHelper.setDefaultReceiveData(getDefaultReceiveData());
    socketHeartBeatHelper.setReceiveHeartBeatPacketChecker(getReceiveHeartBeatPacketChecker());
    socketHeartBeatHelper.setHeartBeatInterval(getHeartBeatInterval());
    socketHeartBeatHelper.setSendHeartBeatEnabled(isSendHeartBeatEnabled());
    return socketHeartBeatHelper;
  }
  
  public byte[] getDefaultReceiveData() {
    return this.defaultReceiveData;
  }
  
  public byte[] getDefaultSendData() {
    return this.defaultSendData;
  }
  
  public long getHeartBeatInterval() {
    return this.heartBeatInterval;
  }
  
  public SocketHeartBeatHelper getOriginal() {
    SocketHeartBeatHelper socketHeartBeatHelper = this.original;
    return (socketHeartBeatHelper == null) ? this : socketHeartBeatHelper;
  }
  
  public ReceiveHeartBeatPacketChecker getReceiveHeartBeatPacketChecker() {
    return this.receiveHeartBeatPacketChecker;
  }
  
  public byte[] getSendData() {
    return (getSendDataBuilder() != null) ? getSendDataBuilder().obtainSendHeartBeatData(getOriginal()) : getDefaultSendData();
  }
  
  public SendDataBuilder getSendDataBuilder() {
    return this.sendDataBuilder;
  }
  
  public boolean isReceiveHeartBeatPacket(SocketResponsePacket paramSocketResponsePacket) {
    return (getReceiveHeartBeatPacketChecker() != null) ? getReceiveHeartBeatPacketChecker().isReceiveHeartBeatPacket(getOriginal(), paramSocketResponsePacket) : ((getDefaultReceiveData() != null) ? paramSocketResponsePacket.isDataEqual(getDefaultReceiveData()) : false);
  }
  
  public boolean isSendHeartBeatEnabled() {
    return ((getDefaultSendData() == null && getSendDataBuilder() == null) || getHeartBeatInterval() <= 0L) ? false : this.sendHeartBeatEnabled;
  }
  
  public SocketHeartBeatHelper setDefaultReceiveData(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte != null) {
      this.defaultReceiveData = Arrays.copyOf(paramArrayOfbyte, paramArrayOfbyte.length);
    } else {
      this.defaultReceiveData = null;
    } 
    return this;
  }
  
  public SocketHeartBeatHelper setDefaultSendData(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte != null) {
      this.defaultSendData = Arrays.copyOf(paramArrayOfbyte, paramArrayOfbyte.length);
    } else {
      this.defaultSendData = null;
    } 
    return this;
  }
  
  public SocketHeartBeatHelper setHeartBeatInterval(long paramLong) {
    this.heartBeatInterval = paramLong;
    return this;
  }
  
  protected SocketHeartBeatHelper setOriginal(SocketHeartBeatHelper paramSocketHeartBeatHelper) {
    this.original = paramSocketHeartBeatHelper;
    return this;
  }
  
  public SocketHeartBeatHelper setReceiveHeartBeatPacketChecker(ReceiveHeartBeatPacketChecker paramReceiveHeartBeatPacketChecker) {
    this.receiveHeartBeatPacketChecker = paramReceiveHeartBeatPacketChecker;
    return this;
  }
  
  public SocketHeartBeatHelper setSendDataBuilder(SendDataBuilder paramSendDataBuilder) {
    this.sendDataBuilder = paramSendDataBuilder;
    return this;
  }
  
  public SocketHeartBeatHelper setSendHeartBeatEnabled(boolean paramBoolean) {
    this.sendHeartBeatEnabled = paramBoolean;
    return this;
  }
  
  public static interface ReceiveHeartBeatPacketChecker {
    boolean isReceiveHeartBeatPacket(SocketHeartBeatHelper param1SocketHeartBeatHelper, SocketResponsePacket param1SocketResponsePacket);
  }
  
  public static interface SendDataBuilder {
    byte[] obtainSendHeartBeatData(SocketHeartBeatHelper param1SocketHeartBeatHelper);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/vilyever/socketclient/helper/SocketHeartBeatHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */