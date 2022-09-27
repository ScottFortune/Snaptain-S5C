package com.vilyever.socketclient.server;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.helper.SocketClientAddress;
import com.vilyever.socketclient.helper.SocketClientDelegate;
import com.vilyever.socketclient.helper.SocketConfigure;
import com.vilyever.socketclient.helper.SocketHeartBeatHelper;
import com.vilyever.socketclient.helper.SocketPacketHelper;
import com.vilyever.socketclient.helper.SocketResponsePacket;
import com.vilyever.socketclient.util.IPUtil;
import com.vilyever.socketclient.util.StringValidation;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SocketServer implements SocketClientDelegate {
  public static final int MaxPort = 65535;
  
  public static final int NoPort = -1;
  
  private String charsetName;
  
  private SocketHeartBeatHelper heartBeatHelper;
  
  private ListenThread listenThread;
  
  private boolean listening;
  
  private int port = -1;
  
  private ServerSocket runningServerSocket;
  
  private ArrayList<SocketServerClient> runningSocketServerClients;
  
  final SocketServer self = this;
  
  private SocketConfigure socketConfigure;
  
  private SocketPacketHelper socketPacketHelper;
  
  private ArrayList<SocketServerDelegate> socketServerDelegates;
  
  private UIHandler uiHandler;
  
  private boolean __i__checkServerSocketAvailable() {
    boolean bool;
    if (getRunningServerSocket() != null && !getRunningServerSocket().isClosed()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void __i__disconnectAllClients() {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      getUiHandler().post(new Runnable() {
            public void run() {
              SocketServer.this.self.__i__disconnectAllClients();
            }
          });
      return;
    } 
    while (getRunningSocketServerClients().size() > 0) {
      SocketServerClient socketServerClient = getRunningSocketServerClients().get(0);
      getRunningSocketServerClients().remove(socketServerClient);
      socketServerClient.disconnect();
    } 
  }
  
  private void __i__onSocketServerBeginListen() {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      getUiHandler().post(new Runnable() {
            public void run() {
              SocketServer.this.self.__i__onSocketServerBeginListen();
            }
          });
      return;
    } 
    ArrayList<SocketServerDelegate> arrayList = (ArrayList)getSocketServerDelegates().clone();
    int i = arrayList.size();
    for (byte b = 0; b < i; b++)
      ((SocketServerDelegate)arrayList.get(b)).onServerBeginListen(this, getPort()); 
    getListenThread().start();
  }
  
  private void __i__onSocketServerClientConnected(final SocketServerClient socketServerClient) {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      getUiHandler().post(new Runnable() {
            public void run() {
              SocketServer.this.self.__i__onSocketServerClientConnected(socketServerClient);
            }
          });
      return;
    } 
    ArrayList<SocketServerDelegate> arrayList = (ArrayList)getSocketServerDelegates().clone();
    int i = arrayList.size();
    for (byte b = 0; b < i; b++)
      ((SocketServerDelegate)arrayList.get(b)).onClientConnected(this, socketServerClient); 
  }
  
  private void __i__onSocketServerClientDisconnected(final SocketServerClient socketServerClient) {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      getUiHandler().post(new Runnable() {
            public void run() {
              SocketServer.this.self.__i__onSocketServerClientDisconnected(socketServerClient);
            }
          });
      return;
    } 
    ArrayList<SocketServerDelegate> arrayList = (ArrayList)getSocketServerDelegates().clone();
    int i = arrayList.size();
    for (byte b = 0; b < i; b++)
      ((SocketServerDelegate)arrayList.get(b)).onClientDisconnected(this, socketServerClient); 
  }
  
  private void __i__onSocketServerStopListen() {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      getUiHandler().post(new Runnable() {
            public void run() {
              SocketServer.this.self.__i__onSocketServerStopListen();
            }
          });
      return;
    } 
    ArrayList<SocketServerDelegate> arrayList = (ArrayList)getSocketServerDelegates().clone();
    int i = arrayList.size();
    for (byte b = 0; b < i; b++)
      ((SocketServerDelegate)arrayList.get(b)).onServerStopListen(this, getPort()); 
  }
  
  public boolean beginListen(int paramInt) {
    if (isListening())
      return false; 
    setPort(paramInt);
    SocketConfigure socketConfigure = getSocketConfigure().setCharsetName(getCharsetName());
    String str = IPUtil.getLocalIPAddress(true);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("");
    stringBuilder.append(paramInt);
    socketConfigure.setAddress(new SocketClientAddress(str, stringBuilder.toString())).setHeartBeatHelper(getHeartBeatHelper()).setSocketPacketHelper(getSocketPacketHelper());
    if (getRunningServerSocket() == null)
      return false; 
    setListening(true);
    __i__onSocketServerBeginListen();
    return true;
  }
  
  public int beginListenFromPort(int paramInt) {
    if (isListening())
      return -1; 
    while (paramInt <= 65535) {
      if (beginListen(paramInt))
        return paramInt; 
      paramInt++;
    } 
    return -1;
  }
  
  public String getCharsetName() {
    return this.charsetName;
  }
  
  public SocketHeartBeatHelper getHeartBeatHelper() {
    if (this.heartBeatHelper == null)
      this.heartBeatHelper = new SocketHeartBeatHelper(); 
    return this.heartBeatHelper;
  }
  
  public String getIP() {
    return getRunningServerSocket().getLocalSocketAddress().toString().substring(1);
  }
  
  protected ListenThread getListenThread() {
    if (this.listenThread == null)
      this.listenThread = new ListenThread(); 
    return this.listenThread;
  }
  
  public SocketClientAddress getListeningAddress() {
    return getSocketConfigure().getAddress();
  }
  
  public int getPort() {
    return this.port;
  }
  
  protected ServerSocket getRunningServerSocket() {
    if (this.runningServerSocket == null)
      try {
        ServerSocket serverSocket = new ServerSocket();
        this(getPort());
        this.runningServerSocket = serverSocket;
      } catch (IOException iOException) {
        iOException.printStackTrace();
      }  
    return this.runningServerSocket;
  }
  
  protected ArrayList<SocketServerClient> getRunningSocketServerClients() {
    if (this.runningSocketServerClients == null)
      this.runningSocketServerClients = new ArrayList<SocketServerClient>(); 
    return this.runningSocketServerClients;
  }
  
  protected SocketConfigure getSocketConfigure() {
    if (this.socketConfigure == null)
      this.socketConfigure = new SocketConfigure(); 
    return this.socketConfigure;
  }
  
  public SocketPacketHelper getSocketPacketHelper() {
    if (this.socketPacketHelper == null)
      this.socketPacketHelper = new SocketPacketHelper(); 
    return this.socketPacketHelper;
  }
  
  protected ArrayList<SocketServerDelegate> getSocketServerDelegates() {
    if (this.socketServerDelegates == null)
      this.socketServerDelegates = new ArrayList<SocketServerDelegate>(); 
    return this.socketServerDelegates;
  }
  
  protected UIHandler getUiHandler() {
    if (this.uiHandler == null)
      this.uiHandler = new UIHandler(this); 
    return this.uiHandler;
  }
  
  protected SocketServerClient internalGetSocketServerClient(Socket paramSocket) {
    return new SocketServerClient(paramSocket, getSocketConfigure());
  }
  
  public boolean isListening() {
    return this.listening;
  }
  
  public void onConnected(SocketClient paramSocketClient) {}
  
  public void onDisconnected(SocketClient paramSocketClient) {
    getRunningSocketServerClients().remove(paramSocketClient);
    __i__onSocketServerClientDisconnected((SocketServerClient)paramSocketClient);
  }
  
  public void onResponse(SocketClient paramSocketClient, SocketResponsePacket paramSocketResponsePacket) {}
  
  public SocketServer registerSocketServerDelegate(SocketServerDelegate paramSocketServerDelegate) {
    if (!getSocketServerDelegates().contains(paramSocketServerDelegate))
      getSocketServerDelegates().add(paramSocketServerDelegate); 
    return this;
  }
  
  public SocketServer removeSocketServerDelegate(SocketServerDelegate paramSocketServerDelegate) {
    getSocketServerDelegates().remove(paramSocketServerDelegate);
    return this;
  }
  
  public SocketServer setCharsetName(String paramString) {
    this.charsetName = paramString;
    return this;
  }
  
  public SocketServer setHeartBeatHelper(SocketHeartBeatHelper paramSocketHeartBeatHelper) {
    this.heartBeatHelper = paramSocketHeartBeatHelper;
    return this;
  }
  
  protected SocketServer setListenThread(ListenThread paramListenThread) {
    this.listenThread = paramListenThread;
    return this;
  }
  
  protected SocketServer setListening(boolean paramBoolean) {
    this.listening = paramBoolean;
    return this;
  }
  
  protected SocketServer setPort(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("");
    stringBuilder.append(paramInt);
    if (StringValidation.validateRegex(stringBuilder.toString(), "^6553[0-5]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{0,3}$")) {
      if (isListening())
        return this; 
      this.port = paramInt;
      return this;
    } 
    throw new IllegalArgumentException("we need a correct remote port to listen");
  }
  
  protected SocketServer setRunningServerSocket(ServerSocket paramServerSocket) {
    this.runningServerSocket = paramServerSocket;
    return this;
  }
  
  public SocketServer setSocketPacketHelper(SocketPacketHelper paramSocketPacketHelper) {
    this.socketPacketHelper = paramSocketPacketHelper;
    return this;
  }
  
  public void stopListen() {
    if (isListening()) {
      getListenThread().interrupt();
      try {
        getRunningServerSocket().close();
      } catch (IOException iOException) {
        iOException.printStackTrace();
      } 
    } 
  }
  
  private class ListenThread extends Thread {
    private boolean running;
    
    private ListenThread() {}
    
    protected boolean isRunning() {
      return this.running;
    }
    
    public void run() {
      super.run();
      setRunning(true);
      while (!Thread.interrupted() && SocketServer.this.self.__i__checkServerSocketAvailable()) {
        try {
          Socket socket = SocketServer.this.self.getRunningServerSocket().accept();
          SocketServerClient socketServerClient = SocketServer.this.self.internalGetSocketServerClient(socket);
          SocketServer.this.getRunningSocketServerClients().add(socketServerClient);
          socketServerClient.registerSocketClientDelegate(SocketServer.this.self);
          SocketServer.this.self.__i__onSocketServerClientConnected(socketServerClient);
        } catch (IOException iOException) {}
      } 
      setRunning(false);
      SocketServer.this.self.setListening(false);
      SocketServer.this.self.setListenThread(null);
      SocketServer.this.self.setRunningServerSocket(null);
      SocketServer.this.self.__i__disconnectAllClients();
      SocketServer.this.self.__i__onSocketServerStopListen();
    }
    
    protected ListenThread setRunning(boolean param1Boolean) {
      this.running = param1Boolean;
      return this;
    }
  }
  
  private static class UIHandler extends Handler {
    private WeakReference<SocketServer> referenceSocketServer;
    
    public UIHandler(SocketServer param1SocketServer) {
      super(Looper.getMainLooper());
      this.referenceSocketServer = new WeakReference<SocketServer>(param1SocketServer);
    }
    
    public void handleMessage(Message param1Message) {
      super.handleMessage(param1Message);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/vilyever/socketclient/server/SocketServer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */