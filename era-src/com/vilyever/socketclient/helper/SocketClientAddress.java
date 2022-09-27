package com.vilyever.socketclient.helper;

import com.vilyever.socketclient.util.StringValidation;
import java.net.InetSocketAddress;

public class SocketClientAddress {
  public static final int DefaultConnectionTimeout = 15000;
  
  private int connectionTimeout;
  
  private SocketClientAddress original;
  
  private String remoteIP;
  
  private String remotePort;
  
  final SocketClientAddress self = this;
  
  public SocketClientAddress() {
    this((String)null, (String)null);
  }
  
  public SocketClientAddress(String paramString, int paramInt) {
    this(paramString, stringBuilder.toString());
  }
  
  public SocketClientAddress(String paramString, int paramInt1, int paramInt2) {
    this(paramString, stringBuilder.toString(), paramInt2);
  }
  
  public SocketClientAddress(String paramString1, String paramString2) {
    this(paramString1, paramString2, 15000);
  }
  
  public SocketClientAddress(String paramString1, String paramString2, int paramInt) {
    this.remoteIP = paramString1;
    this.remotePort = paramString2;
    this.connectionTimeout = paramInt;
  }
  
  public void checkValidation() {
    if (StringValidation.validateRegex(getRemoteIP(), "^(25[0-5]|2[0-4][0-9]|1{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|1{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|1{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|1{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$")) {
      if (StringValidation.validateRegex(getRemotePort(), "^6553[0-5]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{0,3}$")) {
        if (getConnectionTimeout() >= 0)
          return; 
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("we need connectionTimeout > 0. Current is ");
        stringBuilder2.append(getConnectionTimeout());
        throw new IllegalArgumentException(stringBuilder2.toString());
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("we need a correct remote port to connect. Current is ");
      stringBuilder1.append(getRemotePort());
      throw new IllegalArgumentException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("we need a correct remote IP to connect. Current is ");
    stringBuilder.append(getRemoteIP());
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public SocketClientAddress copy() {
    SocketClientAddress socketClientAddress = new SocketClientAddress(getRemoteIP(), getRemotePort(), getConnectionTimeout());
    socketClientAddress.setOriginal(this);
    return socketClientAddress;
  }
  
  public int getConnectionTimeout() {
    return this.connectionTimeout;
  }
  
  public InetSocketAddress getInetSocketAddress() {
    return new InetSocketAddress(getRemoteIP(), getRemotePortIntegerValue());
  }
  
  public SocketClientAddress getOriginal() {
    SocketClientAddress socketClientAddress = this.original;
    return (socketClientAddress == null) ? this : socketClientAddress;
  }
  
  public String getRemoteIP() {
    return this.remoteIP;
  }
  
  public String getRemotePort() {
    return this.remotePort;
  }
  
  public int getRemotePortIntegerValue() {
    return (getRemotePort() == null) ? 0 : Integer.valueOf(getRemotePort()).intValue();
  }
  
  public SocketClientAddress setConnectionTimeout(int paramInt) {
    this.connectionTimeout = paramInt;
    return this;
  }
  
  protected SocketClientAddress setOriginal(SocketClientAddress paramSocketClientAddress) {
    this.original = paramSocketClientAddress;
    return this;
  }
  
  public SocketClientAddress setRemoteIP(String paramString) {
    this.remoteIP = paramString;
    return this;
  }
  
  public SocketClientAddress setRemotePort(String paramString) {
    this.remotePort = paramString;
    return this;
  }
  
  public SocketClientAddress setRemotePortWithInteger(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("");
    stringBuilder.append(paramInt);
    setRemotePort(stringBuilder.toString());
    return this;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/vilyever/socketclient/helper/SocketClientAddress.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */