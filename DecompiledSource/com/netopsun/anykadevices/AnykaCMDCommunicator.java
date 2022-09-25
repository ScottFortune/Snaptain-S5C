package com.netopsun.anykadevices;

import android.util.Log;
import com.google.gson.Gson;
import com.netopsun.deviceshub.base.CMDCommunicator;
import com.netopsun.deviceshub.base.bean.RemoteVideoFiles;
import com.netopsun.deviceshub.interfaces.Cancelable;
import com.netopsun.deviceshub.interfaces.NothingCancel;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class AnykaCMDCommunicator extends CMDCommunicator {
  private static final byte[] ANYKA_PING = new byte[] { 
      108, 101, 119, 101, 105, 95, 99, 109, 100, 0, 
      1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0 };
  
  private static final int CMD_LENGTH = 46;
  
  private static final byte[] DELETE_REMOTE_VIDEO;
  
  private static final byte[] DOWNLOAD_REMOTE_VIDEO;
  
  private static final byte[] GET_REMOTE_VIDEO_LIST;
  
  private static final byte[] REMOTE_TAKE_PHOTO;
  
  private static final byte[] ROTATE_VIDEO_0_DEGREE_CMD;
  
  private static final byte[] ROTATE_VIDEO_180_DEGREE_CMD = new byte[] { 
      108, 101, 119, 101, 105, 95, 99, 109, 100, 0, 
      34, 0, 0, 0, 3, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0 };
  
  private static final byte[] SET_TIME_CMD;
  
  private static final byte[] START_REMOTE_RECORD;
  
  private static final byte[] STOP_REMOTE_RECORD;
  
  private static final String TAG = "AnykaCMDCommunicator";
  
  private static final CopyOnWriteArrayList<CMDCommunicator.OnExecuteCMDResult> waitReceiveResultList;
  
  final AnykaDevices anykaDevices;
  
  private volatile boolean isConnectByUser = false;
  
  volatile boolean isConnected = false;
  
  private boolean isVideoReversal = false;
  
  private long lastNotifyReconnectCMDTime;
  
  private volatile Disposable receiveTask;
  
  private volatile Socket socket = new Socket();
  
  static {
    ROTATE_VIDEO_0_DEGREE_CMD = new byte[] { 
        108, 101, 119, 101, 105, 95, 99, 109, 100, 0, 
        34, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0 };
    SET_TIME_CMD = new byte[] { 
        108, 101, 119, 101, 105, 95, 99, 109, 100, 0, 
        4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0 };
    GET_REMOTE_VIDEO_LIST = new byte[] { 
        108, 101, 119, 101, 105, 95, 99, 109, 100, 0, 
        8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 
        2, 0, 0, 0, 100, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0 };
    DOWNLOAD_REMOTE_VIDEO = new byte[] { 
        108, 101, 119, 101, 105, 95, 99, 109, 100, 0, 
        18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, -60, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0 };
    DELETE_REMOTE_VIDEO = new byte[] { 
        108, 101, 119, 101, 105, 95, 99, 109, 100, 0, 
        20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 100, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0 };
    REMOTE_TAKE_PHOTO = new byte[] { 
        108, 101, 119, 101, 105, 95, 99, 109, 100, 0, 
        19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0 };
    START_REMOTE_RECORD = new byte[] { 
        108, 101, 119, 101, 105, 95, 99, 109, 100, 0, 
        17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, Byte.MAX_VALUE, 81, 
        1, 0, 44, 1, 0, 0 };
    STOP_REMOTE_RECORD = new byte[] { 
        108, 101, 119, 101, 105, 95, 99, 109, 100, 0, 
        17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, Byte.MAX_VALUE, 81, 
        1, 0, 44, 1, 0, 0 };
    waitReceiveResultList = new CopyOnWriteArrayList<CMDCommunicator.OnExecuteCMDResult>();
  }
  
  protected AnykaCMDCommunicator(AnykaDevices paramAnykaDevices) {
    super(paramAnykaDevices);
    this.anykaDevices = paramAnykaDevices;
  }
  
  private Disposable excuteCMD(final boolean waitRespond, int paramInt, CMDCommunicator.OnExecuteCMDResult paramOnExecuteCMDResult, final byte[] reqBytes, Object paramObject) {
    if (!this.isConnected) {
      if (paramOnExecuteCMDResult != null)
        paramOnExecuteCMDResult.onResult(-1, "cmdCommunicator未连接"); 
      return null;
    } 
    if (paramOnExecuteCMDResult != null) {
      paramOnExecuteCMDResult.cmdTypeObject = paramObject;
      waitReceiveResultList.add(paramOnExecuteCMDResult);
    } 
    final WeakReference<CMDCommunicator.OnExecuteCMDResult> callbackRef = new WeakReference<CMDCommunicator.OnExecuteCMDResult>(paramOnExecuteCMDResult);
    return (Disposable)Observable.create(new ObservableOnSubscribe<Integer>() {
          public void subscribe(ObservableEmitter<Integer> param1ObservableEmitter) throws Exception {
            try {
              AnykaCMDCommunicator.this.socketSend(reqBytes);
              param1ObservableEmitter.onNext(Integer.valueOf(0));
            } catch (Exception exception) {
              Log.e("AnykaCMDCommunicator", "socket send failed: ");
              exception.printStackTrace();
              param1ObservableEmitter.onNext(Integer.valueOf(-1));
              param1ObservableEmitter.onComplete();
              if (!AnykaCMDCommunicator.this.socket.isClosed())
                AnykaCMDCommunicator.this.socket.close(); 
              if (AnykaCMDCommunicator.this.isConnectByUser && AnykaCMDCommunicator.this.autoReconnect && System.currentTimeMillis() - AnykaCMDCommunicator.this.lastNotifyReconnectCMDTime > 3000L) {
                AnykaCMDCommunicator.access$602(AnykaCMDCommunicator.this, System.currentTimeMillis());
                AnykaCMDCommunicator.this.anykaDevices.getConnectHandler().notifyReconnectCMD();
              } 
            } 
          }
        }).timeout(paramInt, TimeUnit.SECONDS).subscribeOn(Schedulers.newThread()).doOnDispose(new Action() {
          public void run() throws Exception {
            CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult = callbackRef.get();
            if (onExecuteCMDResult != null && AnykaCMDCommunicator.waitReceiveResultList.remove(onExecuteCMDResult))
              onExecuteCMDResult.onResult(-507, "用户取消"); 
          }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeWith((Observer)new DisposableObserver<Integer>() {
          boolean sendSucess = false;
          
          public void onComplete() {}
          
          public void onError(Throwable param1Throwable) {
            if (param1Throwable instanceof java.util.concurrent.TimeoutException) {
              boolean bool = this.sendSucess;
              CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult = callbackRef.get();
              if (onExecuteCMDResult != null && AnykaCMDCommunicator.waitReceiveResultList.remove(onExecuteCMDResult))
                if (!this.sendSucess) {
                  onExecuteCMDResult.onResult(-504, "发送超时");
                } else {
                  onExecuteCMDResult.onResult(-505, "接收超时");
                }  
            } 
          }
          
          public void onNext(Integer param1Integer) {
            if (param1Integer.intValue() < 0) {
              CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult = callbackRef.get();
              if (onExecuteCMDResult != null && AnykaCMDCommunicator.waitReceiveResultList.remove(onExecuteCMDResult))
                onExecuteCMDResult.onResult(-1, "发送指令失败"); 
            } else {
              this.sendSucess = true;
              if (!waitRespond) {
                CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult = callbackRef.get();
                if (onExecuteCMDResult != null && AnykaCMDCommunicator.waitReceiveResultList.remove(onExecuteCMDResult))
                  onExecuteCMDResult.onResult(0, "发送指令成功，不接收返回数据"); 
              } 
            } 
          }
        });
  }
  
  private void getDeviceParam() {}
  
  private static byte[] intToByteArray(int paramInt) {
    byte b1 = (byte)(paramInt >> 24 & 0xFF);
    byte b2 = (byte)(paramInt >> 16 & 0xFF);
    byte b3 = (byte)(paramInt >> 8 & 0xFF);
    return new byte[] { (byte)(paramInt & 0xFF), b3, b2, b1 };
  }
  
  private static byte[] longToByteArray(long paramLong) {
    byte b1 = (byte)(int)(paramLong >> 56L & 0xFFL);
    byte b2 = (byte)(int)(paramLong >> 48L & 0xFFL);
    byte b3 = (byte)(int)(paramLong >> 40L & 0xFFL);
    byte b4 = (byte)(int)(paramLong >> 32L & 0xFFL);
    byte b5 = (byte)(int)(paramLong >> 24L & 0xFFL);
    byte b6 = (byte)(int)(paramLong >> 16L & 0xFFL);
    byte b7 = (byte)(int)(paramLong >> 8L & 0xFFL);
    return new byte[] { (byte)(int)(paramLong & 0xFFL), b7, b6, b5, b4, b3, b2, b1 };
  }
  
  public void connect() {
    this.isConnectByUser = true;
    super.connect();
  }
  
  public int connectInternal() {
    if (this.isConnected)
      return 0; 
    if (this.receiveTask != null && !this.receiveTask.isDisposed())
      this.receiveTask.dispose(); 
    try {
      connectSocket(5);
      final InputStream inputStream = this.socket.getInputStream();
      syncDevicesTime();
      this.isConnected = true;
      this.receiveTask = Observable.create(new ObservableOnSubscribe<Object>() {
            public void subscribe(ObservableEmitter<Object> param1ObservableEmitter) throws Exception {
              AnykaCMDCommunicator.this.getRemoteSDCardStatus(false, 5, (CMDCommunicator.OnExecuteCMDResult)null);
              final ArrayList tempWaitReceiveResultList = new ArrayList();
              AnykaDataExtractor anykaDataExtractor = new AnykaDataExtractor();
              anykaDataExtractor.setOnDataCallback(new AnykaDataExtractor.OnDataCallback() {
                    void onRemoteSDReady(boolean param2Boolean) {
                      AnykaCMDCommunicator.this.SDCardState = param2Boolean;
                      tempWaitReceiveResultList.addAll(AnykaCMDCommunicator.waitReceiveResultList);
                      for (param2Boolean = false; param2Boolean < tempWaitReceiveResultList.size(); param2Boolean++) {
                        if (((CMDCommunicator.OnExecuteCMDResult)tempWaitReceiveResultList.get(param2Boolean)).cmdTypeObject.equals(AnykaCMDCommunicator.ANYKA_PING)) {
                          CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult = tempWaitReceiveResultList.get(param2Boolean);
                          StringBuilder stringBuilder = new StringBuilder();
                          stringBuilder.append(AnykaCMDCommunicator.this.SDCardState);
                          stringBuilder.append("");
                          onExecuteCMDResult.onResult(0, stringBuilder.toString());
                          AnykaCMDCommunicator.waitReceiveResultList.remove(tempWaitReceiveResultList.get(param2Boolean));
                        } 
                      } 
                    }
                    
                    public void onRemoteVideoListData(RemoteVideoFiles param2RemoteVideoFiles) {
                      tempWaitReceiveResultList.addAll(AnykaCMDCommunicator.waitReceiveResultList);
                      for (byte b = 0; b < tempWaitReceiveResultList.size(); b++) {
                        if (((CMDCommunicator.OnExecuteCMDResult)tempWaitReceiveResultList.get(b)).cmdTypeObject.equals(AnykaCMDCommunicator.GET_REMOTE_VIDEO_LIST)) {
                          ((CMDCommunicator.OnExecuteCMDResult)tempWaitReceiveResultList.get(b)).onResult(0, (new Gson()).toJson(param2RemoteVideoFiles));
                          AnykaCMDCommunicator.waitReceiveResultList.remove(tempWaitReceiveResultList.get(b));
                        } 
                      } 
                    }
                  });
              byte[] arrayOfByte = new byte[1024];
              while (!param1ObservableEmitter.isDisposed()) {
                try {
                  int i = inputStream.read(arrayOfByte);
                  if (i > 0)
                    anykaDataExtractor.onData(arrayOfByte, i); 
                } catch (Exception exception) {
                  exception.printStackTrace();
                  break;
                } 
              } 
              if (!AnykaCMDCommunicator.this.socket.isClosed())
                AnykaCMDCommunicator.this.socket.close(); 
              param1ObservableEmitter.onComplete();
            }
          }).subscribeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
      return 0;
    } catch (Exception exception) {
      exception.printStackTrace();
      return -1;
    } 
  }
  
  protected void connectSocket(int paramInt) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield socket : Ljava/net/Socket;
    //   6: invokevirtual isClosed : ()Z
    //   9: ifne -> 27
    //   12: aload_0
    //   13: getfield socket : Ljava/net/Socket;
    //   16: invokevirtual isConnected : ()Z
    //   19: istore_2
    //   20: iload_2
    //   21: ifeq -> 27
    //   24: aload_0
    //   25: monitorexit
    //   26: return
    //   27: new java/net/Socket
    //   30: astore_3
    //   31: aload_3
    //   32: invokespecial <init> : ()V
    //   35: aload_0
    //   36: aload_3
    //   37: putfield socket : Ljava/net/Socket;
    //   40: new java/net/InetSocketAddress
    //   43: astore_3
    //   44: aload_3
    //   45: aload_0
    //   46: getfield anykaDevices : Lcom/netopsun/anykadevices/AnykaDevices;
    //   49: invokevirtual getDevicesIP : ()Ljava/lang/String;
    //   52: aload_0
    //   53: getfield anykaDevices : Lcom/netopsun/anykadevices/AnykaDevices;
    //   56: invokevirtual getCmdPort : ()I
    //   59: invokespecial <init> : (Ljava/lang/String;I)V
    //   62: aload_0
    //   63: getfield socket : Ljava/net/Socket;
    //   66: aload_3
    //   67: iload_1
    //   68: sipush #1000
    //   71: imul
    //   72: invokevirtual connect : (Ljava/net/SocketAddress;I)V
    //   75: aload_0
    //   76: monitorexit
    //   77: return
    //   78: astore_3
    //   79: aload_0
    //   80: monitorexit
    //   81: aload_3
    //   82: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	78	finally
    //   27	75	78	finally
  }
  
  public Cancelable deleteRemoteRecordFile(boolean paramBoolean, int paramInt, String paramString, CMDCommunicator.OnExecuteCMDResult paramOnExecuteCMDResult) {
    byte[] arrayOfByte2 = DELETE_REMOTE_VIDEO;
    arrayOfByte2 = Arrays.copyOf(arrayOfByte2, arrayOfByte2.length + 100);
    byte[] arrayOfByte1 = paramString.getBytes();
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, DELETE_REMOTE_VIDEO.length, arrayOfByte1.length);
    return new Disposable2Cancelable(excuteCMD(false, paramInt, paramOnExecuteCMDResult, arrayOfByte2, DELETE_REMOTE_VIDEO));
  }
  
  public void disconnect() {
    this.isConnectByUser = false;
    interruptConnectSocket();
    super.disconnect();
  }
  
  public int disconnectInternal() {
    if (this.receiveTask != null && !this.receiveTask.isDisposed())
      this.receiveTask.dispose(); 
    waitReceiveResultList.clear();
    this.isConnected = false;
    return 0;
  }
  
  public Cancelable downloadRemoteRecordFile(boolean paramBoolean, final int timeOutSecond, final String fileName, final String path, final CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult) {
    return new Disposable2Cancelable((Disposable)Observable.create(new ObservableOnSubscribe<Object>() {
            public void subscribe(ObservableEmitter<Object> param1ObservableEmitter) throws Exception {
              String str2 = path;
              File file2 = new File(str2.substring(0, str2.lastIndexOf(File.separator)));
              if (!file2.exists())
                file2.mkdirs(); 
              String str3 = path;
              String str1 = str3;
              if (!str3.toLowerCase().endsWith(".mp4")) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str3);
                stringBuilder.append(".mp4");
                str1 = stringBuilder.toString();
              } 
              File file1 = new File(str1);
              file1.createNewFile();
              FileOutputStream fileOutputStream = new FileOutputStream(file1);
              Socket socket = new Socket();
              InetSocketAddress inetSocketAddress = new InetSocketAddress(AnykaCMDCommunicator.this.anykaDevices.getDevicesIP(), AnykaCMDCommunicator.this.anykaDevices.getVideoPort());
              try {
                socket.connect(inetSocketAddress, timeOutSecond * 1000);
                byte[] arrayOfByte1 = Arrays.copyOf(AnykaCMDCommunicator.DOWNLOAD_REMOTE_VIDEO, AnykaCMDCommunicator.DOWNLOAD_REMOTE_VIDEO.length + 196);
                byte[] arrayOfByte2 = fileName.getBytes();
                System.arraycopy(arrayOfByte2, 0, arrayOfByte1, AnykaCMDCommunicator.DOWNLOAD_REMOTE_VIDEO.length + 16, arrayOfByte2.length);
                Thread.sleep(500L);
                socket.getOutputStream().write(arrayOfByte1);
                arrayOfByte1 = new byte[1024];
                AnykaDataExtractor anykaDataExtractor = new AnykaDataExtractor();
                this();
                AnykaDataExtractor.OnDataCallback onDataCallback = new AnykaDataExtractor.OnDataCallback() {
                    public void onRemoteVideoDownload(int param2Int1, byte[] param2ArrayOfbyte, int param2Int2, int param2Int3) {
                      emitter.onNext(Integer.valueOf(0));
                      if (param2Int1 == 1 || param2Int1 == 2) {
                        try {
                          fileOutputStream.write(param2ArrayOfbyte, param2Int2, param2Int3);
                        } catch (IOException iOException) {
                          iOException.printStackTrace();
                        } 
                        return;
                      } 
                      if (param2Int1 == 3) {
                        try {
                          fileOutputStream.flush();
                          fileOutputStream.close();
                        } catch (IOException iOException) {
                          iOException.printStackTrace();
                        } 
                        emitter.onComplete();
                      } 
                    }
                    
                    public void onRemoteVideoListData(RemoteVideoFiles param2RemoteVideoFiles) {}
                  };
                super(this, param1ObservableEmitter, fileOutputStream);
                anykaDataExtractor.setOnDataCallback(onDataCallback);
                long l = 0L;
                socket.setSoTimeout(1000);
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                while (true) {
                  long l1 = l;
                  boolean bool = param1ObservableEmitter.isDisposed();
                  if (!bool) {
                    try {
                      anykaDataExtractor.onData(arrayOfByte1, inputStream.read(arrayOfByte1));
                      l = l1;
                      if (System.currentTimeMillis() - l1 > 1000L) {
                        outputStream.write(AnykaCMDCommunicator.ANYKA_PING);
                        l = System.currentTimeMillis();
                      } 
                    } catch (Exception exception) {
                      exception.printStackTrace();
                      l = l1;
                    } 
                    continue;
                  } 
                  socket.close();
                  if (socket.isConnected())
                    socket.close(); 
                  fileOutputStream.close();
                  param1ObservableEmitter.onComplete();
                  return;
                } 
              } catch (IOException iOException) {
                iOException.printStackTrace();
                param1ObservableEmitter.onError(iOException);
              } 
              if (socket.isConnected())
                socket.close(); 
              fileOutputStream.close();
              param1ObservableEmitter.onComplete();
            }
          }).subscribeOn(Schedulers.io()).timeout(timeOutSecond, TimeUnit.SECONDS).subscribeWith((Observer)new DisposableObserver<Object>() {
            public void onComplete() {
              onExecuteCMDResult.onResult(0, "success");
            }
            
            public void onError(Throwable param1Throwable) {
              CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult = onExecuteCMDResult;
              if (onExecuteCMDResult != null)
                if (param1Throwable instanceof java.util.concurrent.TimeoutException) {
                  onExecuteCMDResult.onResult(-504, "超时");
                } else {
                  onExecuteCMDResult.onResult(-1, param1Throwable.getMessage());
                }  
            }
            
            public void onNext(Object param1Object) {}
          }));
  }
  
  public Cancelable getRemoteRecordFileList(boolean paramBoolean, int paramInt1, int paramInt2, String paramString, CMDCommunicator.OnExecuteCMDResult paramOnExecuteCMDResult) {
    byte[] arrayOfByte = (byte[])GET_REMOTE_VIDEO_LIST.clone();
    System.arraycopy(longToByteArray(System.currentTimeMillis() / 1000L), 0, arrayOfByte, arrayOfByte.length - 8, 8);
    return new Disposable2Cancelable(excuteCMD(paramBoolean, paramInt1, paramOnExecuteCMDResult, arrayOfByte, GET_REMOTE_VIDEO_LIST));
  }
  
  public Cancelable getRemoteSDCardStatus(boolean paramBoolean, int paramInt, CMDCommunicator.OnExecuteCMDResult paramOnExecuteCMDResult) {
    byte[] arrayOfByte = ANYKA_PING;
    return new Disposable2Cancelable(excuteCMD(paramBoolean, paramInt, paramOnExecuteCMDResult, arrayOfByte, arrayOfByte));
  }
  
  protected void interruptConnectSocket() {
    if (!this.socket.isClosed())
      try {
        this.socket.close();
      } catch (IOException iOException) {
        iOException.printStackTrace();
      }  
  }
  
  public boolean isConnected() {
    return this.isConnected;
  }
  
  public boolean isVideoReversal() {
    return this.isVideoReversal;
  }
  
  public Cancelable remoteStartRecord(boolean paramBoolean, int paramInt, CMDCommunicator.OnExecuteCMDResult paramOnExecuteCMDResult) {
    byte[] arrayOfByte = (byte[])START_REMOTE_RECORD.clone();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    arrayOfByte[50] = (byte)(byte)(calendar.get(7) - 1);
    return new Disposable2Cancelable(excuteCMD(false, paramInt, paramOnExecuteCMDResult, arrayOfByte, START_REMOTE_RECORD));
  }
  
  public Cancelable remoteStopRecord(boolean paramBoolean, int paramInt, CMDCommunicator.OnExecuteCMDResult paramOnExecuteCMDResult) {
    byte[] arrayOfByte = (byte[])STOP_REMOTE_RECORD.clone();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    arrayOfByte[50] = (byte)(byte)(calendar.get(7) - 1);
    return new Disposable2Cancelable(excuteCMD(false, paramInt, paramOnExecuteCMDResult, arrayOfByte, STOP_REMOTE_RECORD));
  }
  
  public Cancelable remoteTakePhoto(boolean paramBoolean, int paramInt, CMDCommunicator.OnExecuteCMDResult paramOnExecuteCMDResult) {
    byte[] arrayOfByte = REMOTE_TAKE_PHOTO;
    return new Disposable2Cancelable(excuteCMD(false, paramInt, paramOnExecuteCMDResult, arrayOfByte, arrayOfByte));
  }
  
  public Cancelable rotateVideo(boolean paramBoolean1, int paramInt, boolean paramBoolean2, CMDCommunicator.OnExecuteCMDResult paramOnExecuteCMDResult) {
    byte[] arrayOfByte;
    this.isVideoReversal = paramBoolean2;
    if (this.isVideoReversal) {
      arrayOfByte = ROTATE_VIDEO_180_DEGREE_CMD;
    } else {
      arrayOfByte = ROTATE_VIDEO_0_DEGREE_CMD;
    } 
    return new Disposable2Cancelable(excuteCMD(false, paramInt, paramOnExecuteCMDResult, arrayOfByte, (Object)null));
  }
  
  public void setIsAlreadyStartRecord(boolean paramBoolean) {
    ((AnykaVideoCommunicator)this.anykaDevices.getVideoCommunicator()).setRemoteRecording(paramBoolean);
  }
  
  public void setOnRemoteCMDListener(CMDCommunicator.OnRemoteCMDListener paramOnRemoteCMDListener) {
    ((AnykaVideoCommunicator)this.anykaDevices.getVideoCommunicator()).setOnRemoteCMDListener(paramOnRemoteCMDListener);
  }
  
  public Cancelable setVideoQuality(boolean paramBoolean, int paramInt1, int paramInt2, CMDCommunicator.OnExecuteCMDResult paramOnExecuteCMDResult) {
    return ((AnykaVideoCommunicator)this.anykaDevices.getVideoCommunicator()).switchVideoQuality(paramInt2, paramOnExecuteCMDResult);
  }
  
  protected void socketSend(byte[] paramArrayOfbyte) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield socket : Ljava/net/Socket;
    //   6: invokevirtual getOutputStream : ()Ljava/io/OutputStream;
    //   9: aload_1
    //   10: invokevirtual write : ([B)V
    //   13: aload_0
    //   14: getfield socket : Ljava/net/Socket;
    //   17: invokevirtual getOutputStream : ()Ljava/io/OutputStream;
    //   20: invokevirtual flush : ()V
    //   23: ldc2_w 300
    //   26: invokestatic sleep : (J)V
    //   29: goto -> 37
    //   32: astore_1
    //   33: aload_1
    //   34: invokevirtual printStackTrace : ()V
    //   37: aload_0
    //   38: monitorexit
    //   39: return
    //   40: astore_1
    //   41: aload_0
    //   42: monitorexit
    //   43: aload_1
    //   44: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	40	finally
    //   23	29	32	java/lang/InterruptedException
    //   23	29	40	finally
    //   33	37	40	finally
  }
  
  public RemoteVideoFiles string2RemoteVideoFiles(String paramString) {
    try {
      Gson gson = new Gson();
      this();
      return (RemoteVideoFiles)gson.fromJson(paramString, RemoteVideoFiles.class);
    } catch (Exception exception) {
      exception.printStackTrace();
      return null;
    } 
  }
  
  public Cancelable syncDevicesTime() {
    byte[] arrayOfByte1 = SET_TIME_CMD;
    byte[] arrayOfByte2 = new byte[arrayOfByte1.length + 8];
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte1.length);
    System.arraycopy(intToByteArray((int)(System.currentTimeMillis() / 1000L)), 0, arrayOfByte2, SET_TIME_CMD.length, 4);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("syncDevicesTime: ");
    stringBuilder.append(Arrays.toString(arrayOfByte2));
    Log.e("AnykaCMDCommunicator", stringBuilder.toString());
    try {
      socketSend(arrayOfByte2);
    } catch (IOException iOException) {
      iOException.printStackTrace();
    } 
    return (Cancelable)new NothingCancel();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/anykadevices/AnykaCMDCommunicator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */