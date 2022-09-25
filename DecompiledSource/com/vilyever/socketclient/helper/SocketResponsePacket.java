package com.vilyever.socketclient.helper;

import com.vilyever.socketclient.util.CharsetUtil;
import java.util.Arrays;

public class SocketResponsePacket {
  private byte[] data;
  
  private byte[] headerData;
  
  private boolean heartBeat;
  
  private String message;
  
  private byte[] packetLengthData;
  
  final SocketResponsePacket self = this;
  
  private byte[] trailerData;
  
  public void buildStringWithCharsetName(String paramString) {
    if (getData() != null)
      setMessage(CharsetUtil.dataToString(getData(), paramString)); 
  }
  
  public byte[] getData() {
    return this.data;
  }
  
  public byte[] getHeaderData() {
    return this.headerData;
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
  
  public boolean isDataEqual(byte[] paramArrayOfbyte) {
    return Arrays.equals(getData(), paramArrayOfbyte);
  }
  
  public boolean isHeartBeat() {
    return this.heartBeat;
  }
  
  public SocketResponsePacket setData(byte[] paramArrayOfbyte) {
    this.data = paramArrayOfbyte;
    return this;
  }
  
  public SocketResponsePacket setHeaderData(byte[] paramArrayOfbyte) {
    this.headerData = paramArrayOfbyte;
    return this;
  }
  
  public SocketResponsePacket setHeartBeat(boolean paramBoolean) {
    this.heartBeat = paramBoolean;
    return this;
  }
  
  public SocketResponsePacket setMessage(String paramString) {
    this.message = paramString;
    return this;
  }
  
  public SocketResponsePacket setPacketLengthData(byte[] paramArrayOfbyte) {
    this.packetLengthData = paramArrayOfbyte;
    return this;
  }
  
  public SocketResponsePacket setTrailerData(byte[] paramArrayOfbyte) {
    this.trailerData = paramArrayOfbyte;
    return this;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/vilyever/socketclient/helper/SocketResponsePacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */