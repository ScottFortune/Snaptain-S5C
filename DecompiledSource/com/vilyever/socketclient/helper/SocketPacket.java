package com.vilyever.socketclient.helper;

import com.vilyever.socketclient.util.CharsetUtil;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketPacket {
  private static final AtomicInteger IDAtomic = new AtomicInteger();
  
  private final int ID = IDAtomic.getAndIncrement();
  
  private byte[] data;
  
  private byte[] headerData;
  
  private boolean heartBeat;
  
  private String message;
  
  private byte[] packetLengthData;
  
  private final SocketPacket self = this;
  
  private byte[] trailerData;
  
  public SocketPacket(String paramString) {
    this.message = paramString;
  }
  
  public SocketPacket(byte[] paramArrayOfbyte) {
    this(paramArrayOfbyte, false);
  }
  
  public SocketPacket(byte[] paramArrayOfbyte, boolean paramBoolean) {
    this.data = Arrays.copyOf(paramArrayOfbyte, paramArrayOfbyte.length);
    this.heartBeat = paramBoolean;
  }
  
  public void buildDataWithCharsetName(String paramString) {
    if (getMessage() != null)
      this.data = CharsetUtil.stringToData(getMessage(), paramString); 
  }
  
  public byte[] getData() {
    return this.data;
  }
  
  public byte[] getHeaderData() {
    return this.headerData;
  }
  
  public int getID() {
    return this.ID;
  }
  
  public String getMessage() {
    return this.message;
  }
  
  public byte[] getPacketLengthData() {
    return this.packetLengthData;
  }
  
  public byte[] getTrailerData() {
    return this.trailerData;
  }
  
  public boolean isHeartBeat() {
    return this.heartBeat;
  }
  
  public SocketPacket setHeaderData(byte[] paramArrayOfbyte) {
    this.headerData = paramArrayOfbyte;
    return this;
  }
  
  public SocketPacket setPacketLengthData(byte[] paramArrayOfbyte) {
    this.packetLengthData = paramArrayOfbyte;
    return this;
  }
  
  public SocketPacket setTrailerData(byte[] paramArrayOfbyte) {
    this.trailerData = paramArrayOfbyte;
    return this;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/vilyever/socketclient/helper/SocketPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */