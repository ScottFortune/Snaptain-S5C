package com.vilyever.socketclient.helper;

import java.util.Arrays;

public class SocketPacketHelper {
  private SocketPacketHelper original;
  
  private ReadStrategy readStrategy = ReadStrategy.Manually;
  
  private byte[] receiveHeaderData;
  
  private ReceivePacketDataLengthConvertor receivePacketDataLengthConvertor;
  
  private int receivePacketLengthDataLength;
  
  private boolean receiveSegmentEnabled;
  
  private int receiveSegmentLength;
  
  private long receiveTimeout;
  
  private boolean receiveTimeoutEnabled;
  
  private byte[] receiveTrailerData;
  
  final SocketPacketHelper self = this;
  
  private byte[] sendHeaderData;
  
  private SendPacketLengthDataConvertor sendPacketLengthDataConvertor;
  
  private boolean sendSegmentEnabled;
  
  private int sendSegmentLength;
  
  private long sendTimeout;
  
  private boolean sendTimeoutEnabled;
  
  private byte[] sendTrailerData;
  
  public void checkValidation() {
    int i = null.$SwitchMap$com$vilyever$socketclient$helper$SocketPacketHelper$ReadStrategy[getReadStrategy().ordinal()];
    if (i != 1) {
      if (i != 2) {
        if (i == 3) {
          if (getReceivePacketLengthDataLength() > 0 && getReceivePacketDataLengthConvertor() != null)
            return; 
          throw new IllegalArgumentException("we need ReceivePacketLengthDataLength and ReceivePacketDataLengthConvertor for AutoReadByLength");
        } 
        throw new IllegalArgumentException("we need a correct ReadStrategy");
      } 
      if (getReceiveTrailerData() != null && (getReceiveTrailerData()).length > 0)
        return; 
      throw new IllegalArgumentException("we need ReceiveTrailerData for AutoReadToTrailer");
    } 
  }
  
  public SocketPacketHelper copy() {
    SocketPacketHelper socketPacketHelper = new SocketPacketHelper();
    socketPacketHelper.setOriginal(this);
    socketPacketHelper.setSendHeaderData(getSendHeaderData());
    socketPacketHelper.setSendPacketLengthDataConvertor(getSendPacketLengthDataConvertor());
    socketPacketHelper.setSendTrailerData(getSendTrailerData());
    socketPacketHelper.setSendSegmentLength(getSendSegmentLength());
    socketPacketHelper.setSendSegmentEnabled(isSendSegmentEnabled());
    socketPacketHelper.setSendTimeout(getSendTimeout());
    socketPacketHelper.setSendTimeoutEnabled(isSendTimeoutEnabled());
    socketPacketHelper.setReadStrategy(getReadStrategy());
    socketPacketHelper.setReceiveHeaderData(getReceiveHeaderData());
    socketPacketHelper.setReceivePacketLengthDataLength(getReceivePacketLengthDataLength());
    socketPacketHelper.setReceivePacketDataLengthConvertor(getReceivePacketDataLengthConvertor());
    socketPacketHelper.setReceiveTrailerData(getReceiveTrailerData());
    socketPacketHelper.setReceiveSegmentLength(getReceiveSegmentLength());
    socketPacketHelper.setReceiveSegmentEnabled(isReceiveSegmentEnabled());
    socketPacketHelper.setReceiveTimeout(getReceiveTimeout());
    socketPacketHelper.setReceiveTimeoutEnabled(isReceiveTimeoutEnabled());
    return socketPacketHelper;
  }
  
  public SocketPacketHelper getOriginal() {
    SocketPacketHelper socketPacketHelper = this.original;
    return (socketPacketHelper == null) ? this : socketPacketHelper;
  }
  
  public ReadStrategy getReadStrategy() {
    return this.readStrategy;
  }
  
  public byte[] getReceiveHeaderData() {
    return this.receiveHeaderData;
  }
  
  public int getReceivePacketDataLength(byte[] paramArrayOfbyte) {
    return (getReadStrategy() == ReadStrategy.AutoReadByLength && getReceivePacketDataLengthConvertor() != null) ? getReceivePacketDataLengthConvertor().obtainReceivePacketDataLength(getOriginal(), paramArrayOfbyte) : 0;
  }
  
  public ReceivePacketDataLengthConvertor getReceivePacketDataLengthConvertor() {
    return this.receivePacketDataLengthConvertor;
  }
  
  public int getReceivePacketLengthDataLength() {
    return this.receivePacketLengthDataLength;
  }
  
  public int getReceiveSegmentLength() {
    return this.receiveSegmentLength;
  }
  
  public long getReceiveTimeout() {
    return this.receiveTimeout;
  }
  
  public byte[] getReceiveTrailerData() {
    return this.receiveTrailerData;
  }
  
  public byte[] getSendHeaderData() {
    return this.sendHeaderData;
  }
  
  public byte[] getSendPacketLengthData(int paramInt) {
    return (getSendPacketLengthDataConvertor() != null) ? getSendPacketLengthDataConvertor().obtainSendPacketLengthDataForPacketLength(getOriginal(), paramInt) : null;
  }
  
  public SendPacketLengthDataConvertor getSendPacketLengthDataConvertor() {
    return this.sendPacketLengthDataConvertor;
  }
  
  public int getSendSegmentLength() {
    return this.sendSegmentLength;
  }
  
  public long getSendTimeout() {
    return this.sendTimeout;
  }
  
  public byte[] getSendTrailerData() {
    return this.sendTrailerData;
  }
  
  public boolean isReceiveSegmentEnabled() {
    return (getReceiveSegmentLength() <= 0) ? false : this.receiveSegmentEnabled;
  }
  
  public boolean isReceiveTimeoutEnabled() {
    return this.receiveTimeoutEnabled;
  }
  
  public boolean isSendSegmentEnabled() {
    return (getSendSegmentLength() <= 0) ? false : this.sendSegmentEnabled;
  }
  
  public boolean isSendTimeoutEnabled() {
    return this.sendTimeoutEnabled;
  }
  
  protected SocketPacketHelper setOriginal(SocketPacketHelper paramSocketPacketHelper) {
    this.original = paramSocketPacketHelper;
    return this;
  }
  
  public SocketPacketHelper setReadStrategy(ReadStrategy paramReadStrategy) {
    this.readStrategy = paramReadStrategy;
    return this;
  }
  
  public SocketPacketHelper setReceiveHeaderData(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte != null) {
      this.receiveHeaderData = Arrays.copyOf(paramArrayOfbyte, paramArrayOfbyte.length);
    } else {
      this.receiveHeaderData = null;
    } 
    return this;
  }
  
  public SocketPacketHelper setReceivePacketDataLengthConvertor(ReceivePacketDataLengthConvertor paramReceivePacketDataLengthConvertor) {
    this.receivePacketDataLengthConvertor = paramReceivePacketDataLengthConvertor;
    return this;
  }
  
  public SocketPacketHelper setReceivePacketLengthDataLength(int paramInt) {
    this.receivePacketLengthDataLength = paramInt;
    return this;
  }
  
  public SocketPacketHelper setReceiveSegmentEnabled(boolean paramBoolean) {
    this.receiveSegmentEnabled = paramBoolean;
    return this;
  }
  
  public SocketPacketHelper setReceiveSegmentLength(int paramInt) {
    this.receiveSegmentLength = paramInt;
    return this;
  }
  
  public SocketPacketHelper setReceiveTimeout(long paramLong) {
    this.receiveTimeout = paramLong;
    return this;
  }
  
  public SocketPacketHelper setReceiveTimeoutEnabled(boolean paramBoolean) {
    this.receiveTimeoutEnabled = paramBoolean;
    return this;
  }
  
  public SocketPacketHelper setReceiveTrailerData(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte != null) {
      this.receiveTrailerData = Arrays.copyOf(paramArrayOfbyte, paramArrayOfbyte.length);
    } else {
      this.receiveTrailerData = null;
    } 
    return this;
  }
  
  public SocketPacketHelper setSendHeaderData(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte != null) {
      this.sendHeaderData = Arrays.copyOf(paramArrayOfbyte, paramArrayOfbyte.length);
    } else {
      this.sendHeaderData = null;
    } 
    return this;
  }
  
  public SocketPacketHelper setSendPacketLengthDataConvertor(SendPacketLengthDataConvertor paramSendPacketLengthDataConvertor) {
    this.sendPacketLengthDataConvertor = paramSendPacketLengthDataConvertor;
    return this;
  }
  
  public SocketPacketHelper setSendSegmentEnabled(boolean paramBoolean) {
    this.sendSegmentEnabled = paramBoolean;
    return this;
  }
  
  public SocketPacketHelper setSendSegmentLength(int paramInt) {
    this.sendSegmentLength = paramInt;
    return this;
  }
  
  public SocketPacketHelper setSendTimeout(long paramLong) {
    this.sendTimeout = paramLong;
    return this;
  }
  
  public SocketPacketHelper setSendTimeoutEnabled(boolean paramBoolean) {
    this.sendTimeoutEnabled = paramBoolean;
    return this;
  }
  
  public SocketPacketHelper setSendTrailerData(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte != null) {
      this.sendTrailerData = Arrays.copyOf(paramArrayOfbyte, paramArrayOfbyte.length);
    } else {
      this.sendTrailerData = null;
    } 
    return this;
  }
  
  public enum ReadStrategy {
    AutoReadByLength, AutoReadToTrailer, Manually;
    
    static {
      AutoReadByLength = new ReadStrategy("AutoReadByLength", 2);
      $VALUES = new ReadStrategy[] { Manually, AutoReadToTrailer, AutoReadByLength };
    }
  }
  
  public static interface ReceivePacketDataLengthConvertor {
    int obtainReceivePacketDataLength(SocketPacketHelper param1SocketPacketHelper, byte[] param1ArrayOfbyte);
  }
  
  public static interface SendPacketLengthDataConvertor {
    byte[] obtainSendPacketLengthDataForPacketLength(SocketPacketHelper param1SocketPacketHelper, int param1Int);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/vilyever/socketclient/helper/SocketPacketHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */