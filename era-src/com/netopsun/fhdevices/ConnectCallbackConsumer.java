package com.netopsun.fhdevices;

import com.netopsun.deviceshub.interfaces.ConnectResultCallback;
import io.reactivex.functions.Consumer;

public class ConnectCallbackConsumer implements Consumer<ConnectCallbackConsumer.ConnectResultBean> {
  final ConnectResultCallback connectResultCallback;
  
  public ConnectCallbackConsumer(ConnectResultCallback paramConnectResultCallback) {
    this.connectResultCallback = paramConnectResultCallback;
  }
  
  public void accept(ConnectResultBean paramConnectResultBean) throws Exception {
    if (this.connectResultCallback == null)
      return; 
    if (paramConnectResultBean.errorCode < 0) {
      this.connectResultCallback.onConnectFail(paramConnectResultBean.errorCode, paramConnectResultBean.msg);
    } else {
      this.connectResultCallback.onConnectSuccess(paramConnectResultBean.errorCode, paramConnectResultBean.msg);
    } 
  }
  
  public static class ConnectResultBean {
    int errorCode;
    
    String msg = "";
    
    public ConnectResultBean(int param1Int, String param1String) {
      this.errorCode = param1Int;
      this.msg = param1String;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhdevices/ConnectCallbackConsumer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */