package com.vilyever.socketclient;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.vilyever.socketclient.helper.SocketClientAddress;
import com.vilyever.socketclient.helper.SocketClientDelegate;
import com.vilyever.socketclient.helper.SocketClientReceivingDelegate;
import com.vilyever.socketclient.helper.SocketClientSendingDelegate;
import com.vilyever.socketclient.helper.SocketConfigure;
import com.vilyever.socketclient.helper.SocketHeartBeatHelper;
import com.vilyever.socketclient.helper.SocketInputReader;
import com.vilyever.socketclient.helper.SocketPacket;
import com.vilyever.socketclient.helper.SocketPacketHelper;
import com.vilyever.socketclient.helper.SocketResponsePacket;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketClient {
  private static final long NoSendingTime = -1L;
  
  public static final String TAG = SocketClient.class.getSimpleName();
  
  private SocketClientAddress address;
  
  private String charsetName;
  
  private ConnectionThread connectionThread;
  
  private boolean disconnecting;
  
  private DisconnectionThread disconnectionThread;
  
  private CountDownTimer hearBeatCountDownTimer;
  
  private SocketHeartBeatHelper heartBeatHelper;
  
  private long lastReceiveMessageTime;
  
  private long lastReceiveProgressCallbackTime;
  
  private long lastSendHeartBeatMessageTime;
  
  private long lastSendMessageTime = -1L;
  
  private ReceiveThread receiveThread;
  
  private SocketResponsePacket receivingResponsePacket;
  
  private Socket runningSocket;
  
  final SocketClient self = this;
  
  private SendThread sendThread;
  
  private SocketPacket sendingPacket;
  
  private LinkedBlockingQueue<SocketPacket> sendingPacketQueue;
  
  private ArrayList<SocketClientDelegate> socketClientDelegates;
  
  private ArrayList<SocketClientReceivingDelegate> socketClientReceivingDelegates;
  
  private ArrayList<SocketClientSendingDelegate> socketClientSendingDelegates;
  
  private SocketConfigure socketConfigure;
  
  private SocketInputReader socketInputReader;
  
  private SocketPacketHelper socketPacketHelper;
  
  private State state;
  
  private UIHandler uiHandler;
  
  public SocketClient() {
    this(new SocketClientAddress());
  }
  
  public SocketClient(SocketClientAddress paramSocketClientAddress) {
    this.address = paramSocketClientAddress;
  }
  
  private void __i__enqueueNewPacket(SocketPacket paramSocketPacket) {
    if (!isConnected())
      return; 
    LinkedBlockingQueue<SocketPacket> linkedBlockingQueue = getSendingPacketQueue();
    /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/util/concurrent/LinkedBlockingQueue<ObjectType{com/vilyever/socketclient/helper/SocketPacket}>}, name=null} */
    try {
      getSendingPacketQueue().put(paramSocketPacket);
    } catch (InterruptedException interruptedException) {
      interruptedException.printStackTrace();
    } finally {}
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/util/concurrent/LinkedBlockingQueue<ObjectType{com/vilyever/socketclient/helper/SocketPacket}>}, name=null} */
  }
  
  private void __i__onConnected() {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      getUiHandler().post(new Runnable() {
            public void run() {
              SocketClient.this.self.__i__onConnected();
            }
          });
      return;
    } 
    ArrayList<SocketClientDelegate> arrayList = (ArrayList)getSocketClientDelegates().clone();
    int i = arrayList.size();
    for (byte b = 0; b < i; b++)
      ((SocketClientDelegate)arrayList.get(b)).onConnected(this); 
    getSendThread().start();
    getReceiveThread().start();
    getHearBeatCountDownTimer().start();
  }
  
  private void __i__onDisconnected() {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      getUiHandler().post(new Runnable() {
            public void run() {
              SocketClient.this.self.__i__onDisconnected();
            }
          });
      return;
    } 
    ArrayList<SocketClientDelegate> arrayList = (ArrayList)getSocketClientDelegates().clone();
    int i = arrayList.size();
    for (byte b = 0; b < i; b++)
      ((SocketClientDelegate)arrayList.get(b)).onDisconnected(this); 
  }
  
  private void __i__onReceivePacketBegin(final SocketResponsePacket packet) {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      getUiHandler().post(new Runnable() {
            public void run() {
              SocketClient.this.self.__i__onReceivePacketBegin(packet);
            }
          });
      return;
    } 
    if (getSocketClientReceivingDelegates().size() > 0) {
      ArrayList<SocketClientReceivingDelegate> arrayList = (ArrayList)getSocketClientReceivingDelegates().clone();
      int i = arrayList.size();
      for (byte b = 0; b < i; b++)
        ((SocketClientReceivingDelegate)arrayList.get(b)).onReceivePacketBegin(this, packet); 
    } 
  }
  
  private void __i__onReceivePacketCancel(final SocketResponsePacket packet) {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      getUiHandler().post(new Runnable() {
            public void run() {
              SocketClient.this.self.__i__onReceivePacketCancel(packet);
            }
          });
      return;
    } 
    if (getSocketClientReceivingDelegates().size() > 0) {
      ArrayList<SocketClientReceivingDelegate> arrayList = (ArrayList)getSocketClientReceivingDelegates().clone();
      int i = arrayList.size();
      for (byte b = 0; b < i; b++)
        ((SocketClientReceivingDelegate)arrayList.get(b)).onReceivePacketCancel(this, packet); 
    } 
  }
  
  private void __i__onReceivePacketEnd(final SocketResponsePacket packet) {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      getUiHandler().post(new Runnable() {
            public void run() {
              SocketClient.this.self.__i__onReceivePacketEnd(packet);
            }
          });
      return;
    } 
    if (getSocketClientReceivingDelegates().size() > 0) {
      ArrayList<SocketClientReceivingDelegate> arrayList = (ArrayList)getSocketClientReceivingDelegates().clone();
      int i = arrayList.size();
      for (byte b = 0; b < i; b++)
        ((SocketClientReceivingDelegate)arrayList.get(b)).onReceivePacketEnd(this, packet); 
    } 
  }
  
  private void __i__onReceiveResponse(final SocketResponsePacket responsePacket) {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      getUiHandler().post(new Runnable() {
            public void run() {
              SocketClient.this.self.__i__onReceiveResponse(responsePacket);
            }
          });
      return;
    } 
    setLastReceiveMessageTime(System.currentTimeMillis());
    if (getSocketClientDelegates().size() > 0) {
      ArrayList<SocketClientDelegate> arrayList = (ArrayList)getSocketClientDelegates().clone();
      int i = arrayList.size();
      for (byte b = 0; b < i; b++)
        ((SocketClientDelegate)arrayList.get(b)).onResponse(this, responsePacket); 
    } 
  }
  
  private void __i__onReceivingPacketInProgress(final SocketResponsePacket packet, final int receivedLength, final int headerLength, final int packetLengthDataLength, final int dataLength, final int trailerLength) {
    if (System.currentTimeMillis() - getLastReceiveProgressCallbackTime() < 41L)
      return; 
    if (Looper.myLooper() != Looper.getMainLooper()) {
      getUiHandler().post(new Runnable() {
            public void run() {
              SocketClient.this.self.__i__onReceivingPacketInProgress(packet, receivedLength, headerLength, packetLengthDataLength, dataLength, trailerLength);
            }
          });
      return;
    } 
    float f = receivedLength / (headerLength + packetLengthDataLength + dataLength + trailerLength);
    if (getSocketClientReceivingDelegates().size() > 0) {
      ArrayList<SocketClientReceivingDelegate> arrayList = (ArrayList)getSocketClientReceivingDelegates().clone();
      packetLengthDataLength = arrayList.size();
      for (headerLength = 0; headerLength < packetLengthDataLength; headerLength++)
        ((SocketClientReceivingDelegate)arrayList.get(headerLength)).onReceivingPacketInProgress(this, packet, f, receivedLength); 
    } 
    setLastReceiveProgressCallbackTime(System.currentTimeMillis());
  }
  
  private void __i__onSendPacketBegin(final SocketPacket packet) {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      getUiHandler().post(new Runnable() {
            public void run() {
              SocketClient.this.self.__i__onSendPacketBegin(packet);
            }
          });
      return;
    } 
    if (getSocketClientDelegates().size() > 0) {
      ArrayList<SocketClientSendingDelegate> arrayList = (ArrayList)getSocketClientSendingDelegates().clone();
      int i = arrayList.size();
      for (byte b = 0; b < i; b++)
        ((SocketClientSendingDelegate)arrayList.get(b)).onSendPacketBegin(this, packet); 
    } 
  }
  
  private void __i__onSendPacketCancel(final SocketPacket packet) {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      getUiHandler().post(new Runnable() {
            public void run() {
              SocketClient.this.self.__i__onSendPacketCancel(packet);
            }
          });
      return;
    } 
    if (getSocketClientDelegates().size() > 0) {
      ArrayList<SocketClientSendingDelegate> arrayList = (ArrayList)getSocketClientSendingDelegates().clone();
      int i = arrayList.size();
      for (byte b = 0; b < i; b++)
        ((SocketClientSendingDelegate)arrayList.get(b)).onSendPacketCancel(this, packet); 
    } 
  }
  
  private void __i__onSendPacketEnd(final SocketPacket packet) {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      getUiHandler().post(new Runnable() {
            public void run() {
              SocketClient.this.self.__i__onSendPacketEnd(packet);
            }
          });
      return;
    } 
    if (getSocketClientDelegates().size() > 0) {
      ArrayList<SocketClientSendingDelegate> arrayList = (ArrayList)getSocketClientSendingDelegates().clone();
      int i = arrayList.size();
      for (byte b = 0; b < i; b++)
        ((SocketClientSendingDelegate)arrayList.get(b)).onSendPacketEnd(this, packet); 
    } 
  }
  
  private void __i__onSendingPacketInProgress(final SocketPacket packet, final int sendedLength, final int headerLength, final int packetLengthDataLength, final int dataLength, final int trailerLength) {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      getUiHandler().post(new Runnable() {
            public void run() {
              SocketClient.this.self.__i__onSendingPacketInProgress(packet, sendedLength, headerLength, packetLengthDataLength, dataLength, trailerLength);
            }
          });
      return;
    } 
    float f = sendedLength / (headerLength + packetLengthDataLength + dataLength + trailerLength);
    if (getSocketClientDelegates().size() > 0) {
      ArrayList<SocketClientSendingDelegate> arrayList = (ArrayList)getSocketClientSendingDelegates().clone();
      packetLengthDataLength = arrayList.size();
      for (headerLength = 0; headerLength < packetLengthDataLength; headerLength++)
        ((SocketClientSendingDelegate)arrayList.get(headerLength)).onSendingPacketInProgress(this, packet, f, sendedLength); 
    } 
  }
  
  private void __i__onTimeTick() {
    if (!isConnected())
      return; 
    long l = System.currentTimeMillis();
    if (getSocketConfigure().getHeartBeatHelper().isSendHeartBeatEnabled() && l - getLastSendHeartBeatMessageTime() >= getSocketConfigure().getHeartBeatHelper().getHeartBeatInterval()) {
      __i__sendHeartBeat();
      setLastSendHeartBeatMessageTime(l);
    } 
    if (getSocketConfigure().getSocketPacketHelper().isReceiveTimeoutEnabled() && l - getLastReceiveMessageTime() >= getSocketConfigure().getSocketPacketHelper().getReceiveTimeout())
      disconnect(); 
    if (getSocketConfigure().getSocketPacketHelper().isSendTimeoutEnabled() && getLastSendMessageTime() != -1L && l - getLastSendMessageTime() >= getSocketConfigure().getSocketPacketHelper().getSendTimeout())
      disconnect(); 
  }
  
  private void __i__sendHeartBeat() {
    if (!isConnected())
      return; 
    if (getSocketConfigure() != null && getSocketConfigure().getHeartBeatHelper() != null && getSocketConfigure().getHeartBeatHelper().isSendHeartBeatEnabled())
      (new Thread(new Runnable() {
            public void run() {
              SocketClient.this.self.__i__enqueueNewPacket(packet);
            }
          })).start(); 
  }
  
  public void cancelSend(final SocketPacket packet) {
    (new Thread(new Runnable() {
          public void run() {
            synchronized (SocketClient.this.getSendingPacketQueue()) {
              if (SocketClient.this.self.getSendingPacketQueue().contains(packet)) {
                SocketClient.this.self.getSendingPacketQueue().remove(packet);
                SocketClient.this.self.__i__onSendPacketCancel(packet);
              } 
              return;
            } 
          }
        })).start();
  }
  
  public void connect() {
    if (!isDisconnected())
      return; 
    if (getAddress() != null) {
      getAddress().checkValidation();
      getSocketPacketHelper().checkValidation();
      getSocketConfigure().setCharsetName(getCharsetName()).setAddress(getAddress()).setHeartBeatHelper(getHeartBeatHelper()).setSocketPacketHelper(getSocketPacketHelper());
      setState(State.Connecting);
      getConnectionThread().start();
      return;
    } 
    throw new IllegalArgumentException("we need a SocketClientAddress to connect");
  }
  
  public void disconnect() {
    if (!isDisconnected() && !isDisconnecting()) {
      setDisconnecting(true);
      getDisconnectionThread().start();
    } 
  }
  
  public SocketClientAddress getAddress() {
    if (this.address == null)
      this.address = new SocketClientAddress(); 
    return this.address;
  }
  
  public String getCharsetName() {
    return this.charsetName;
  }
  
  public SocketClientAddress getConnectedAddress() {
    return getSocketConfigure().getAddress();
  }
  
  protected ConnectionThread getConnectionThread() {
    if (this.connectionThread == null)
      this.connectionThread = new ConnectionThread(); 
    return this.connectionThread;
  }
  
  protected DisconnectionThread getDisconnectionThread() {
    if (this.disconnectionThread == null)
      this.disconnectionThread = new DisconnectionThread(); 
    return this.disconnectionThread;
  }
  
  protected CountDownTimer getHearBeatCountDownTimer() {
    if (this.hearBeatCountDownTimer == null)
      this.hearBeatCountDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000L) {
          public void onFinish() {
            if (SocketClient.this.self.isConnected())
              start(); 
          }
          
          public void onTick(long param1Long) {
            (new Thread(new Runnable() {
                  public void run() {
                    SocketClient.this.self.__i__onTimeTick();
                  }
                })).start();
          }
        }; 
    return this.hearBeatCountDownTimer;
  }
  
  public SocketHeartBeatHelper getHeartBeatHelper() {
    if (this.heartBeatHelper == null)
      this.heartBeatHelper = new SocketHeartBeatHelper(); 
    return this.heartBeatHelper;
  }
  
  protected long getLastReceiveMessageTime() {
    return this.lastReceiveMessageTime;
  }
  
  protected long getLastReceiveProgressCallbackTime() {
    return this.lastReceiveProgressCallbackTime;
  }
  
  protected long getLastSendHeartBeatMessageTime() {
    return this.lastSendHeartBeatMessageTime;
  }
  
  protected long getLastSendMessageTime() {
    return this.lastSendMessageTime;
  }
  
  protected ReceiveThread getReceiveThread() {
    if (this.receiveThread == null)
      this.receiveThread = new ReceiveThread(); 
    return this.receiveThread;
  }
  
  protected SocketResponsePacket getReceivingResponsePacket() {
    return this.receivingResponsePacket;
  }
  
  public Socket getRunningSocket() {
    if (this.runningSocket == null)
      this.runningSocket = SocketDecorator.Action.createSocket(); 
    return this.runningSocket;
  }
  
  protected SendThread getSendThread() {
    if (this.sendThread == null)
      this.sendThread = new SendThread(); 
    return this.sendThread;
  }
  
  protected SocketPacket getSendingPacket() {
    return this.sendingPacket;
  }
  
  protected LinkedBlockingQueue<SocketPacket> getSendingPacketQueue() {
    if (this.sendingPacketQueue == null)
      this.sendingPacketQueue = new LinkedBlockingQueue<SocketPacket>(); 
    return this.sendingPacketQueue;
  }
  
  protected ArrayList<SocketClientDelegate> getSocketClientDelegates() {
    if (this.socketClientDelegates == null)
      this.socketClientDelegates = new ArrayList<SocketClientDelegate>(); 
    return this.socketClientDelegates;
  }
  
  protected ArrayList<SocketClientReceivingDelegate> getSocketClientReceivingDelegates() {
    if (this.socketClientReceivingDelegates == null)
      this.socketClientReceivingDelegates = new ArrayList<SocketClientReceivingDelegate>(); 
    return this.socketClientReceivingDelegates;
  }
  
  protected ArrayList<SocketClientSendingDelegate> getSocketClientSendingDelegates() {
    if (this.socketClientSendingDelegates == null)
      this.socketClientSendingDelegates = new ArrayList<SocketClientSendingDelegate>(); 
    return this.socketClientSendingDelegates;
  }
  
  protected SocketConfigure getSocketConfigure() {
    if (this.socketConfigure == null)
      this.socketConfigure = new SocketConfigure(); 
    return this.socketConfigure;
  }
  
  protected SocketInputReader getSocketInputReader() throws IOException {
    if (this.socketInputReader == null)
      this.socketInputReader = new SocketInputReader(getRunningSocket().getInputStream()); 
    return this.socketInputReader;
  }
  
  public SocketPacketHelper getSocketPacketHelper() {
    if (this.socketPacketHelper == null)
      this.socketPacketHelper = new SocketPacketHelper(); 
    return this.socketPacketHelper;
  }
  
  public State getState() {
    State state1 = this.state;
    State state2 = state1;
    if (state1 == null)
      state2 = State.Disconnected; 
    return state2;
  }
  
  protected UIHandler getUiHandler() {
    if (this.uiHandler == null)
      this.uiHandler = new UIHandler(this); 
    return this.uiHandler;
  }
  
  protected void internalOnConnected() {
    setState(State.Connected);
    setLastSendHeartBeatMessageTime(System.currentTimeMillis());
    setLastReceiveMessageTime(System.currentTimeMillis());
    setLastSendMessageTime(-1L);
    setSendingPacket(null);
    setReceivingResponsePacket(null);
    __i__onConnected();
  }
  
  public boolean isConnected() {
    boolean bool;
    if (getState() == State.Connected) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isConnecting() {
    boolean bool;
    if (getState() == State.Connecting) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isDisconnected() {
    boolean bool;
    if (getState() == State.Disconnected) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isDisconnecting() {
    return this.disconnecting;
  }
  
  public SocketResponsePacket readDataToData(byte[] paramArrayOfbyte) {
    return readDataToData(paramArrayOfbyte, true);
  }
  
  public SocketResponsePacket readDataToData(final byte[] data, final boolean includeData) {
    if (!isConnected())
      return null; 
    if (getSocketConfigure().getSocketPacketHelper().getReadStrategy() != SocketPacketHelper.ReadStrategy.Manually)
      return null; 
    if (getReceivingResponsePacket() != null)
      return null; 
    setReceivingResponsePacket(new SocketResponsePacket());
    (new Thread(new Runnable() {
          public void run() {
            SocketClient.this.self.__i__onReceivePacketBegin(SocketClient.this.self.getReceivingResponsePacket());
            try {
              byte[] arrayOfByte = SocketClient.this.self.getSocketInputReader().readToData(data, includeData);
              SocketClient.this.self.getReceivingResponsePacket().setData(arrayOfByte);
              if (SocketClient.this.self.getSocketConfigure().getCharsetName() != null)
                SocketClient.this.self.getReceivingResponsePacket().buildStringWithCharsetName(SocketClient.this.self.getSocketConfigure().getCharsetName()); 
              SocketClient.this.self.__i__onReceivePacketEnd(SocketClient.this.self.getReceivingResponsePacket());
              SocketClient.this.self.__i__onReceiveResponse(SocketClient.this.self.getReceivingResponsePacket());
            } catch (IOException iOException) {
              iOException.printStackTrace();
              if (SocketClient.this.self.getReceivingResponsePacket() != null) {
                SocketClient.this.self.__i__onReceivePacketCancel(SocketClient.this.self.getReceivingResponsePacket());
                SocketClient.this.self.setReceivingResponsePacket(null);
              } 
            } 
          }
        })).start();
    return getReceivingResponsePacket();
  }
  
  public SocketResponsePacket readDataToLength(final int length) {
    if (!isConnected())
      return null; 
    if (getSocketConfigure().getSocketPacketHelper().getReadStrategy() != SocketPacketHelper.ReadStrategy.Manually)
      return null; 
    if (getReceivingResponsePacket() != null)
      return null; 
    setReceivingResponsePacket(new SocketResponsePacket());
    (new Thread(new Runnable() {
          public void run() {
            if (!SocketClient.this.self.isConnected())
              return; 
            SocketClient.this.self.__i__onReceivePacketBegin(SocketClient.this.self.getReceivingResponsePacket());
            try {
              byte[] arrayOfByte = SocketClient.this.self.getSocketInputReader().readToLength(length);
              SocketClient.this.self.getReceivingResponsePacket().setData(arrayOfByte);
              if (SocketClient.this.self.getSocketConfigure().getCharsetName() != null)
                SocketClient.this.self.getReceivingResponsePacket().buildStringWithCharsetName(SocketClient.this.self.getSocketConfigure().getCharsetName()); 
              SocketClient.this.self.__i__onReceivePacketEnd(SocketClient.this.self.getReceivingResponsePacket());
              SocketClient.this.self.__i__onReceiveResponse(SocketClient.this.self.getReceivingResponsePacket());
            } catch (IOException iOException) {
              iOException.printStackTrace();
              if (SocketClient.this.self.getReceivingResponsePacket() != null) {
                SocketClient.this.self.__i__onReceivePacketCancel(SocketClient.this.self.getReceivingResponsePacket());
                SocketClient.this.self.setReceivingResponsePacket(null);
              } 
            } 
          }
        })).start();
    return getReceivingResponsePacket();
  }
  
  public SocketClient registerSocketClientDelegate(SocketClientDelegate paramSocketClientDelegate) {
    if (!getSocketClientDelegates().contains(paramSocketClientDelegate))
      getSocketClientDelegates().add(paramSocketClientDelegate); 
    return this;
  }
  
  public SocketClient registerSocketClientReceiveDelegate(SocketClientReceivingDelegate paramSocketClientReceivingDelegate) {
    if (!getSocketClientReceivingDelegates().contains(paramSocketClientReceivingDelegate))
      getSocketClientReceivingDelegates().add(paramSocketClientReceivingDelegate); 
    return this;
  }
  
  public SocketClient registerSocketClientSendingDelegate(SocketClientSendingDelegate paramSocketClientSendingDelegate) {
    if (!getSocketClientSendingDelegates().contains(paramSocketClientSendingDelegate))
      getSocketClientSendingDelegates().add(paramSocketClientSendingDelegate); 
    return this;
  }
  
  public SocketClient removeSocketClientDelegate(SocketClientDelegate paramSocketClientDelegate) {
    getSocketClientDelegates().remove(paramSocketClientDelegate);
    return this;
  }
  
  public SocketClient removeSocketClientReceiveDelegate(SocketClientReceivingDelegate paramSocketClientReceivingDelegate) {
    getSocketClientReceivingDelegates().remove(paramSocketClientReceivingDelegate);
    return this;
  }
  
  public SocketClient removeSocketClientSendingDelegate(SocketClientSendingDelegate paramSocketClientSendingDelegate) {
    getSocketClientSendingDelegates().remove(paramSocketClientSendingDelegate);
    return this;
  }
  
  public SocketPacket sendData(byte[] paramArrayOfbyte) {
    if (!isConnected())
      return null; 
    SocketPacket socketPacket = new SocketPacket(paramArrayOfbyte);
    sendPacket(socketPacket);
    return socketPacket;
  }
  
  public SocketPacket sendPacket(final SocketPacket packet) {
    if (!isConnected())
      return null; 
    if (packet == null)
      return null; 
    (new Thread(new Runnable() {
          public void run() {
            SocketClient.this.self.__i__enqueueNewPacket(packet);
          }
        })).start();
    return packet;
  }
  
  public SocketPacket sendString(String paramString) {
    if (!isConnected())
      return null; 
    SocketPacket socketPacket = new SocketPacket(paramString);
    sendPacket(socketPacket);
    return socketPacket;
  }
  
  public SocketClient setAddress(SocketClientAddress paramSocketClientAddress) {
    this.address = paramSocketClientAddress;
    return this;
  }
  
  public SocketClient setCharsetName(String paramString) {
    this.charsetName = paramString;
    return this;
  }
  
  protected SocketClient setConnectionThread(ConnectionThread paramConnectionThread) {
    this.connectionThread = paramConnectionThread;
    return this;
  }
  
  protected SocketClient setDisconnecting(boolean paramBoolean) {
    this.disconnecting = paramBoolean;
    return this;
  }
  
  protected SocketClient setDisconnectionThread(DisconnectionThread paramDisconnectionThread) {
    this.disconnectionThread = paramDisconnectionThread;
    return this;
  }
  
  public SocketClient setHeartBeatHelper(SocketHeartBeatHelper paramSocketHeartBeatHelper) {
    this.heartBeatHelper = paramSocketHeartBeatHelper;
    return this;
  }
  
  protected SocketClient setLastReceiveMessageTime(long paramLong) {
    this.lastReceiveMessageTime = paramLong;
    return this;
  }
  
  protected SocketClient setLastReceiveProgressCallbackTime(long paramLong) {
    this.lastReceiveProgressCallbackTime = paramLong;
    return this;
  }
  
  protected SocketClient setLastSendHeartBeatMessageTime(long paramLong) {
    this.lastSendHeartBeatMessageTime = paramLong;
    return this;
  }
  
  protected SocketClient setLastSendMessageTime(long paramLong) {
    this.lastSendMessageTime = paramLong;
    return this;
  }
  
  protected SocketClient setReceiveThread(ReceiveThread paramReceiveThread) {
    this.receiveThread = paramReceiveThread;
    return this;
  }
  
  protected SocketClient setReceivingResponsePacket(SocketResponsePacket paramSocketResponsePacket) {
    this.receivingResponsePacket = paramSocketResponsePacket;
    return this;
  }
  
  protected SocketClient setRunningSocket(Socket paramSocket) {
    this.runningSocket = paramSocket;
    return this;
  }
  
  protected SocketClient setSendThread(SendThread paramSendThread) {
    this.sendThread = paramSendThread;
    return this;
  }
  
  protected SocketClient setSendingPacket(SocketPacket paramSocketPacket) {
    this.sendingPacket = paramSocketPacket;
    return this;
  }
  
  protected SocketClient setSocketConfigure(SocketConfigure paramSocketConfigure) {
    this.socketConfigure = paramSocketConfigure;
    return this;
  }
  
  protected SocketClient setSocketInputReader(SocketInputReader paramSocketInputReader) {
    this.socketInputReader = paramSocketInputReader;
    return this;
  }
  
  public SocketClient setSocketPacketHelper(SocketPacketHelper paramSocketPacketHelper) {
    this.socketPacketHelper = paramSocketPacketHelper;
    return this;
  }
  
  protected SocketClient setState(State paramState) {
    this.state = paramState;
    return this;
  }
  
  private class ConnectionThread extends Thread {
    private ConnectionThread() {}
    
    public void run() {
      super.run();
      try {
        SocketClientAddress socketClientAddress = SocketClient.this.self.getSocketConfigure().getAddress();
        if (Thread.interrupted())
          return; 
        SocketClient.this.self.getRunningSocket().connect(socketClientAddress.getInetSocketAddress(), socketClientAddress.getConnectionTimeout());
        if (Thread.interrupted())
          return; 
        SocketClient.this.self.setState(SocketClient.State.Connected);
        SocketClient.this.self.setLastSendHeartBeatMessageTime(System.currentTimeMillis());
        SocketClient.this.self.setLastReceiveMessageTime(System.currentTimeMillis());
        SocketClient.this.self.setLastSendMessageTime(-1L);
        SocketClient.this.self.setSendingPacket(null);
        SocketClient.this.self.setReceivingResponsePacket(null);
        SocketClient.this.self.setConnectionThread(null);
        SocketClient.this.self.__i__onConnected();
      } catch (IOException iOException) {
        iOException.printStackTrace();
        SocketClient.this.self.disconnect();
      } 
    }
  }
  
  private class DisconnectionThread extends Thread {
    private DisconnectionThread() {}
    
    public void run() {
      // Byte code:
      //   0: aload_0
      //   1: invokespecial run : ()V
      //   4: aload_0
      //   5: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   8: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   11: invokestatic access$1600 : (Lcom/vilyever/socketclient/SocketClient;)Lcom/vilyever/socketclient/SocketClient$ConnectionThread;
      //   14: ifnull -> 42
      //   17: aload_0
      //   18: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   21: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   24: invokevirtual getConnectionThread : ()Lcom/vilyever/socketclient/SocketClient$ConnectionThread;
      //   27: invokevirtual interrupt : ()V
      //   30: aload_0
      //   31: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   34: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   37: aconst_null
      //   38: invokevirtual setConnectionThread : (Lcom/vilyever/socketclient/SocketClient$ConnectionThread;)Lcom/vilyever/socketclient/SocketClient;
      //   41: pop
      //   42: aload_0
      //   43: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   46: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   49: invokevirtual getRunningSocket : ()Ljava/net/Socket;
      //   52: invokevirtual isClosed : ()Z
      //   55: ifeq -> 71
      //   58: aload_0
      //   59: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   62: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   65: invokevirtual isConnecting : ()Z
      //   68: ifeq -> 193
      //   71: aload_0
      //   72: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   75: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   78: invokevirtual getRunningSocket : ()Ljava/net/Socket;
      //   81: invokevirtual getOutputStream : ()Ljava/io/OutputStream;
      //   84: invokevirtual close : ()V
      //   87: aload_0
      //   88: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   91: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   94: invokevirtual getRunningSocket : ()Ljava/net/Socket;
      //   97: invokevirtual getInputStream : ()Ljava/io/InputStream;
      //   100: invokevirtual close : ()V
      //   103: aload_0
      //   104: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   107: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   110: invokevirtual getRunningSocket : ()Ljava/net/Socket;
      //   113: invokevirtual close : ()V
      //   116: goto -> 181
      //   119: astore_1
      //   120: goto -> 177
      //   123: astore_1
      //   124: aload_0
      //   125: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   128: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   131: invokevirtual getRunningSocket : ()Ljava/net/Socket;
      //   134: invokevirtual close : ()V
      //   137: goto -> 145
      //   140: astore_2
      //   141: aload_2
      //   142: invokevirtual printStackTrace : ()V
      //   145: aload_0
      //   146: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   149: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   152: aconst_null
      //   153: invokevirtual setRunningSocket : (Ljava/net/Socket;)Lcom/vilyever/socketclient/SocketClient;
      //   156: pop
      //   157: aload_1
      //   158: athrow
      //   159: astore_1
      //   160: aload_0
      //   161: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   164: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   167: invokevirtual getRunningSocket : ()Ljava/net/Socket;
      //   170: invokevirtual close : ()V
      //   173: goto -> 181
      //   176: astore_1
      //   177: aload_1
      //   178: invokevirtual printStackTrace : ()V
      //   181: aload_0
      //   182: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   185: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   188: aconst_null
      //   189: invokevirtual setRunningSocket : (Ljava/net/Socket;)Lcom/vilyever/socketclient/SocketClient;
      //   192: pop
      //   193: aload_0
      //   194: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   197: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   200: invokestatic access$1700 : (Lcom/vilyever/socketclient/SocketClient;)Lcom/vilyever/socketclient/SocketClient$SendThread;
      //   203: ifnull -> 231
      //   206: aload_0
      //   207: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   210: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   213: invokevirtual getSendThread : ()Lcom/vilyever/socketclient/SocketClient$SendThread;
      //   216: invokevirtual interrupt : ()V
      //   219: aload_0
      //   220: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   223: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   226: aconst_null
      //   227: invokevirtual setSendThread : (Lcom/vilyever/socketclient/SocketClient$SendThread;)Lcom/vilyever/socketclient/SocketClient;
      //   230: pop
      //   231: aload_0
      //   232: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   235: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   238: invokestatic access$1800 : (Lcom/vilyever/socketclient/SocketClient;)Lcom/vilyever/socketclient/SocketClient$ReceiveThread;
      //   241: ifnull -> 269
      //   244: aload_0
      //   245: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   248: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   251: invokevirtual getReceiveThread : ()Lcom/vilyever/socketclient/SocketClient$ReceiveThread;
      //   254: invokevirtual interrupt : ()V
      //   257: aload_0
      //   258: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   261: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   264: aconst_null
      //   265: invokevirtual setReceiveThread : (Lcom/vilyever/socketclient/SocketClient$ReceiveThread;)Lcom/vilyever/socketclient/SocketClient;
      //   268: pop
      //   269: aload_0
      //   270: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   273: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   276: iconst_0
      //   277: invokevirtual setDisconnecting : (Z)Lcom/vilyever/socketclient/SocketClient;
      //   280: pop
      //   281: aload_0
      //   282: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   285: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   288: getstatic com/vilyever/socketclient/SocketClient$State.Disconnected : Lcom/vilyever/socketclient/SocketClient$State;
      //   291: invokevirtual setState : (Lcom/vilyever/socketclient/SocketClient$State;)Lcom/vilyever/socketclient/SocketClient;
      //   294: pop
      //   295: aload_0
      //   296: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   299: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   302: aconst_null
      //   303: invokevirtual setSocketInputReader : (Lcom/vilyever/socketclient/helper/SocketInputReader;)Lcom/vilyever/socketclient/SocketClient;
      //   306: pop
      //   307: aload_0
      //   308: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   311: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   314: aconst_null
      //   315: invokevirtual setSocketConfigure : (Lcom/vilyever/socketclient/helper/SocketConfigure;)Lcom/vilyever/socketclient/SocketClient;
      //   318: pop
      //   319: aload_0
      //   320: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   323: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   326: invokestatic access$1900 : (Lcom/vilyever/socketclient/SocketClient;)Landroid/os/CountDownTimer;
      //   329: ifnull -> 345
      //   332: aload_0
      //   333: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   336: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   339: invokestatic access$1900 : (Lcom/vilyever/socketclient/SocketClient;)Landroid/os/CountDownTimer;
      //   342: invokevirtual cancel : ()V
      //   345: aload_0
      //   346: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   349: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   352: invokevirtual getSendingPacket : ()Lcom/vilyever/socketclient/helper/SocketPacket;
      //   355: ifnull -> 390
      //   358: aload_0
      //   359: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   362: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   365: aload_0
      //   366: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   369: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   372: invokevirtual getSendingPacket : ()Lcom/vilyever/socketclient/helper/SocketPacket;
      //   375: invokestatic access$100 : (Lcom/vilyever/socketclient/SocketClient;Lcom/vilyever/socketclient/helper/SocketPacket;)V
      //   378: aload_0
      //   379: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   382: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   385: aconst_null
      //   386: invokevirtual setSendingPacket : (Lcom/vilyever/socketclient/helper/SocketPacket;)Lcom/vilyever/socketclient/SocketClient;
      //   389: pop
      //   390: aload_0
      //   391: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   394: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   397: invokevirtual getSendingPacketQueue : ()Ljava/util/concurrent/LinkedBlockingQueue;
      //   400: invokevirtual poll : ()Ljava/lang/Object;
      //   403: checkcast com/vilyever/socketclient/helper/SocketPacket
      //   406: astore_1
      //   407: aload_1
      //   408: ifnull -> 425
      //   411: aload_0
      //   412: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   415: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   418: aload_1
      //   419: invokestatic access$100 : (Lcom/vilyever/socketclient/SocketClient;Lcom/vilyever/socketclient/helper/SocketPacket;)V
      //   422: goto -> 390
      //   425: aload_0
      //   426: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   429: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   432: invokevirtual getReceivingResponsePacket : ()Lcom/vilyever/socketclient/helper/SocketResponsePacket;
      //   435: ifnull -> 470
      //   438: aload_0
      //   439: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   442: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   445: aload_0
      //   446: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   449: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   452: invokevirtual getReceivingResponsePacket : ()Lcom/vilyever/socketclient/helper/SocketResponsePacket;
      //   455: invokestatic access$500 : (Lcom/vilyever/socketclient/SocketClient;Lcom/vilyever/socketclient/helper/SocketResponsePacket;)V
      //   458: aload_0
      //   459: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   462: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   465: aconst_null
      //   466: invokevirtual setReceivingResponsePacket : (Lcom/vilyever/socketclient/helper/SocketResponsePacket;)Lcom/vilyever/socketclient/SocketClient;
      //   469: pop
      //   470: aload_0
      //   471: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   474: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   477: aconst_null
      //   478: invokevirtual setDisconnectionThread : (Lcom/vilyever/socketclient/SocketClient$DisconnectionThread;)Lcom/vilyever/socketclient/SocketClient;
      //   481: pop
      //   482: aload_0
      //   483: getfield this$0 : Lcom/vilyever/socketclient/SocketClient;
      //   486: getfield self : Lcom/vilyever/socketclient/SocketClient;
      //   489: invokestatic access$1100 : (Lcom/vilyever/socketclient/SocketClient;)V
      //   492: return
      // Exception table:
      //   from	to	target	type
      //   71	103	159	java/io/IOException
      //   71	103	123	finally
      //   103	116	119	java/io/IOException
      //   124	137	140	java/io/IOException
      //   160	173	176	java/io/IOException
    }
  }
  
  private class ReceiveThread extends Thread {
    private ReceiveThread() {}
    
    public void run() {
      super.run();
      if (SocketClient.this.self.getSocketConfigure().getSocketPacketHelper().getReadStrategy() == SocketPacketHelper.ReadStrategy.Manually)
        return; 
      try {
        while (SocketClient.this.self.isConnected() && SocketClient.this.self.getSocketInputReader() != null && !Thread.interrupted()) {
          int i;
          int j;
          int m;
          SocketResponsePacket socketResponsePacket = new SocketResponsePacket();
          this();
          SocketClient.this.self.setReceivingResponsePacket(socketResponsePacket);
          byte[] arrayOfByte1 = SocketClient.this.self.getSocketConfigure().getSocketPacketHelper().getReceiveHeaderData();
          if (arrayOfByte1 == null) {
            i = 0;
          } else {
            i = arrayOfByte1.length;
          } 
          byte[] arrayOfByte2 = SocketClient.this.self.getSocketConfigure().getSocketPacketHelper().getReceiveTrailerData();
          if (arrayOfByte2 == null) {
            j = 0;
          } else {
            j = arrayOfByte2.length;
          } 
          int k = SocketClient.this.self.getSocketConfigure().getSocketPacketHelper().getReceivePacketLengthDataLength();
          SocketClient.this.self.__i__onReceivePacketBegin(socketResponsePacket);
          if (i > 0) {
            arrayOfByte1 = SocketClient.this.self.getSocketInputReader().readToData(arrayOfByte1, true);
            SocketClient.this.self.setLastReceiveMessageTime(System.currentTimeMillis());
            socketResponsePacket.setHeaderData(arrayOfByte1);
            m = i + 0;
          } else {
            m = 0;
          } 
          if (SocketClient.this.self.getSocketConfigure().getSocketPacketHelper().getReadStrategy() == SocketPacketHelper.ReadStrategy.AutoReadByLength) {
            int i1;
            if (k < 0) {
              SocketClient.this.self.__i__onReceivePacketCancel(socketResponsePacket);
              SocketClient.this.self.setReceivingResponsePacket(null);
            } else if (k == 0) {
              SocketClient.this.self.__i__onReceivePacketEnd(socketResponsePacket);
              SocketClient.this.self.setReceivingResponsePacket(null);
            } 
            arrayOfByte2 = SocketClient.this.self.getSocketInputReader().readToLength(k);
            SocketClient.this.self.setLastReceiveMessageTime(System.currentTimeMillis());
            socketResponsePacket.setPacketLengthData(arrayOfByte2);
            m += k;
            int n = SocketClient.this.self.getSocketConfigure().getSocketPacketHelper().getReceivePacketDataLength(arrayOfByte2) - j;
            if (n > 0) {
              int i2 = SocketClient.this.self.getRunningSocket().getReceiveBufferSize();
              i1 = i2;
              if (SocketClient.this.self.getSocketConfigure().getSocketPacketHelper().isReceiveSegmentEnabled())
                i1 = Math.min(i2, SocketClient.this.self.getSocketConfigure().getSocketPacketHelper().getReceiveSegmentLength()); 
              for (i2 = 0; i2 < n; i2 = i3) {
                int i3 = Math.min(i2 + i1, n);
                SocketInputReader socketInputReader = SocketClient.this.self.getSocketInputReader();
                i2 = i3 - i2;
                byte[] arrayOfByte = socketInputReader.readToLength(i2);
                SocketClient.this.self.setLastReceiveMessageTime(System.currentTimeMillis());
                if (socketResponsePacket.getData() == null) {
                  socketResponsePacket.setData(arrayOfByte);
                } else {
                  arrayOfByte1 = new byte[(socketResponsePacket.getData()).length + arrayOfByte.length];
                  System.arraycopy(socketResponsePacket.getData(), 0, arrayOfByte1, 0, (socketResponsePacket.getData()).length);
                  System.arraycopy(arrayOfByte, 0, arrayOfByte1, (socketResponsePacket.getData()).length, arrayOfByte.length);
                  socketResponsePacket.setData(arrayOfByte1);
                } 
                m += i2;
                SocketClient.this.self.__i__onReceivingPacketInProgress(socketResponsePacket, m, i, k, n, j);
              } 
              i1 = m;
            } else {
              i1 = m;
              if (n < 0) {
                SocketClient.this.self.__i__onReceivePacketCancel(socketResponsePacket);
                SocketClient.this.self.setReceivingResponsePacket(null);
                i1 = m;
              } 
            } 
            if (j > 0) {
              arrayOfByte2 = SocketClient.this.self.getSocketInputReader().readToLength(j);
              SocketClient.this.self.setLastReceiveMessageTime(System.currentTimeMillis());
              socketResponsePacket.setTrailerData(arrayOfByte2);
              SocketClient.this.self.__i__onReceivingPacketInProgress(socketResponsePacket, i1 + j, i, k, n, j);
            } 
          } else if (SocketClient.this.self.getSocketConfigure().getSocketPacketHelper().getReadStrategy() == SocketPacketHelper.ReadStrategy.AutoReadToTrailer) {
            if (j > 0) {
              arrayOfByte1 = SocketClient.this.self.getSocketInputReader().readToData(arrayOfByte2, false);
              SocketClient.this.self.setLastReceiveMessageTime(System.currentTimeMillis());
              socketResponsePacket.setData(arrayOfByte1);
              socketResponsePacket.setTrailerData(arrayOfByte2);
              m = arrayOfByte1.length;
            } else {
              SocketClient.this.self.__i__onReceivePacketCancel(socketResponsePacket);
              SocketClient.this.self.setReceivingResponsePacket(null);
            } 
          } 
          socketResponsePacket.setHeartBeat(SocketClient.this.self.getSocketConfigure().getHeartBeatHelper().isReceiveHeartBeatPacket(socketResponsePacket));
          if (SocketClient.this.self.getSocketConfigure().getCharsetName() != null)
            socketResponsePacket.buildStringWithCharsetName(SocketClient.this.self.getSocketConfigure().getCharsetName()); 
          SocketClient.this.self.__i__onReceivePacketEnd(socketResponsePacket);
          SocketClient.this.self.__i__onReceiveResponse(socketResponsePacket);
          SocketClient.this.self.setReceivingResponsePacket(null);
        } 
      } catch (Exception exception) {
        SocketClient.this.self.disconnect();
        if (SocketClient.this.self.getReceivingResponsePacket() != null) {
          SocketClient.this.self.__i__onReceivePacketCancel(SocketClient.this.self.getReceivingResponsePacket());
          SocketClient.this.self.setReceivingResponsePacket(null);
        } 
      } 
    }
  }
  
  private class SendThread extends Thread {
    public void run() {
      super.run();
      try {
        while (SocketClient.this.self.isConnected() && !Thread.interrupted()) {
          SocketPacket socketPacket = SocketClient.this.self.getSendingPacketQueue().take();
          if (socketPacket != null) {
            IllegalArgumentException illegalArgumentException;
            int j;
            int k;
            int m;
            int n;
            SocketClient.this.self.setSendingPacket(socketPacket);
            SocketClient.this.self.setLastSendMessageTime(System.currentTimeMillis());
            if (socketPacket.getData() == null && socketPacket.getMessage() != null)
              if (SocketClient.this.self.getSocketConfigure().getCharsetName() != null) {
                socketPacket.buildDataWithCharsetName(SocketClient.this.self.getSocketConfigure().getCharsetName());
              } else {
                illegalArgumentException = new IllegalArgumentException();
                this("we need string charset to send string type message");
                throw illegalArgumentException;
              }  
            if (illegalArgumentException.getData() == null) {
              SocketClient.this.self.__i__onSendPacketCancel((SocketPacket)illegalArgumentException);
              SocketClient.this.self.setSendingPacket(null);
              continue;
            } 
            byte[] arrayOfByte1 = SocketClient.this.self.getSocketConfigure().getSocketPacketHelper().getSendHeaderData();
            int i = 0;
            if (arrayOfByte1 == null) {
              j = 0;
            } else {
              j = arrayOfByte1.length;
            } 
            byte[] arrayOfByte2 = SocketClient.this.self.getSocketConfigure().getSocketPacketHelper().getSendTrailerData();
            if (arrayOfByte2 == null) {
              k = 0;
            } else {
              k = arrayOfByte2.length;
            } 
            byte[] arrayOfByte3 = SocketClient.this.self.getSocketConfigure().getSocketPacketHelper().getSendPacketLengthData((illegalArgumentException.getData()).length + k);
            if (arrayOfByte3 == null) {
              m = 0;
            } else {
              m = arrayOfByte3.length;
            } 
            illegalArgumentException.setHeaderData(arrayOfByte1);
            illegalArgumentException.setTrailerData(arrayOfByte2);
            illegalArgumentException.setPacketLengthData(arrayOfByte3);
            if (j + m + (illegalArgumentException.getData()).length + k <= 0) {
              SocketClient.this.self.__i__onSendPacketCancel((SocketPacket)illegalArgumentException);
              SocketClient.this.self.setSendingPacket(null);
              continue;
            } 
            SocketClient.this.self.__i__onSendPacketBegin((SocketPacket)illegalArgumentException);
            SocketClient.this.self.__i__onSendingPacketInProgress((SocketPacket)illegalArgumentException, 0, j, m, (illegalArgumentException.getData()).length, k);
            if (j > 0) {
              try {
                SocketClient.this.self.getRunningSocket().getOutputStream().write(arrayOfByte1);
                SocketClient.this.self.getRunningSocket().getOutputStream().flush();
                SocketClient.this.self.setLastSendMessageTime(System.currentTimeMillis());
                n = j + 0;
                SocketClient.this.self.__i__onSendingPacketInProgress((SocketPacket)illegalArgumentException, n, j, m, (illegalArgumentException.getData()).length, k);
              } catch (IOException iOException) {}
            } else {
              n = 0;
            } 
            int i1 = n;
            if (m > 0) {
              SocketClient.this.self.getRunningSocket().getOutputStream().write(arrayOfByte3);
              SocketClient.this.self.getRunningSocket().getOutputStream().flush();
              SocketClient.this.self.setLastSendMessageTime(System.currentTimeMillis());
              i1 = n + m;
              SocketClient.this.self.__i__onSendingPacketInProgress((SocketPacket)iOException, i1, j, m, (iOException.getData()).length, k);
            } 
            int i2 = i1;
            if ((iOException.getData()).length > 0) {
              i2 = SocketClient.this.self.getRunningSocket().getSendBufferSize();
              n = i2;
              if (SocketClient.this.self.getSocketConfigure().getSocketPacketHelper().isSendSegmentEnabled())
                n = Math.min(i2, SocketClient.this.self.getSocketConfigure().getSocketPacketHelper().getSendSegmentLength()); 
              while (true) {
                i2 = i1;
                if (i < (iOException.getData()).length) {
                  i2 = Math.min(i + n, (iOException.getData()).length);
                  OutputStream outputStream = SocketClient.this.self.getRunningSocket().getOutputStream();
                  arrayOfByte3 = iOException.getData();
                  int i3 = i2 - i;
                  outputStream.write(arrayOfByte3, i, i3);
                  SocketClient.this.self.getRunningSocket().getOutputStream().flush();
                  SocketClient.this.self.setLastSendMessageTime(System.currentTimeMillis());
                  i1 += i3;
                  SocketClient.this.self.__i__onSendingPacketInProgress((SocketPacket)iOException, i1, j, m, (iOException.getData()).length, k);
                  i = i2;
                  continue;
                } 
                break;
              } 
            } 
            if (k > 0) {
              SocketClient.this.self.getRunningSocket().getOutputStream().write(arrayOfByte2);
              SocketClient.this.self.getRunningSocket().getOutputStream().flush();
              SocketClient.this.self.setLastSendMessageTime(System.currentTimeMillis());
              SocketClient.this.self.__i__onSendingPacketInProgress((SocketPacket)iOException, i2 + k, j, m, (iOException.getData()).length, k);
            } 
            SocketClient.this.self.__i__onSendPacketEnd((SocketPacket)iOException);
            SocketClient.this.self.setSendingPacket(null);
            SocketClient.this.self.setLastSendMessageTime(-1L);
          } 
        } 
      } catch (InterruptedException interruptedException) {
        if (SocketClient.this.self.getSendingPacket() != null) {
          SocketClient.this.self.__i__onSendPacketCancel(SocketClient.this.self.getSendingPacket());
          SocketClient.this.self.setSendingPacket(null);
        } 
      } 
    }
  }
  
  public enum State {
    Connected, Connecting, Disconnected;
    
    static {
      Connected = new State("Connected", 2);
      $VALUES = new State[] { Disconnected, Connecting, Connected };
    }
  }
  
  private static class UIHandler extends Handler {
    private WeakReference<SocketClient> referenceSocketClient;
    
    public UIHandler(SocketClient param1SocketClient) {
      super(Looper.getMainLooper());
      this.referenceSocketClient = new WeakReference<SocketClient>(param1SocketClient);
    }
    
    public void handleMessage(Message param1Message) {
      super.handleMessage(param1Message);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/vilyever/socketclient/SocketClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */