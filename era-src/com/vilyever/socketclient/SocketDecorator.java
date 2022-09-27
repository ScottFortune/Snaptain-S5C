package com.vilyever.socketclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketDecorator extends Socket {
  private final Communicator communicator;
  
  private final PipedInputStream inputStream = new PipedInputStream();
  
  private volatile boolean isConnect = false;
  
  private final OutputStream outputStream;
  
  private final PipedOutputStream receiveDataOutputPiped = new PipedOutputStream();
  
  private SocketDecorator(final Communicator communicator) {
    this.communicator = communicator;
    communicator.setReceiveCallback(new ReceiveCallback() {
          public void onReceive(byte[] param1ArrayOfbyte) {
            try {
              SocketDecorator.this.receiveDataOutputPiped.write(param1ArrayOfbyte);
              SocketDecorator.this.receiveDataOutputPiped.flush();
            } catch (IOException iOException) {
              iOException.printStackTrace();
            } 
          }
        });
    this.outputStream = new OutputStream() {
        public void write(int param1Int) throws IOException {
          communicator.send(new byte[] { (byte)param1Int });
        }
        
        public void write(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
          byte[] arrayOfByte = new byte[param1Int2];
          System.arraycopy(param1ArrayOfbyte, param1Int1, arrayOfByte, 0, param1Int2);
          communicator.send(arrayOfByte);
        }
      };
  }
  
  public void close() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield communicator : Lcom/vilyever/socketclient/SocketDecorator$Communicator;
    //   6: invokeinterface shutdown : ()V
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	14	finally
  }
  
  public void connect(SocketAddress paramSocketAddress, int paramInt) throws IOException {
    this.communicator.connect(new ConnectCallback() {
          public void onConnect() {
            SocketDecorator.access$102(SocketDecorator.this, true);
            try {
              SocketDecorator.this.inputStream.connect(SocketDecorator.this.receiveDataOutputPiped);
            } catch (IOException iOException) {
              iOException.printStackTrace();
            } 
          }
          
          public void onDisConnect() {
            SocketDecorator.access$102(SocketDecorator.this, false);
            try {
              SocketDecorator.this.receiveDataOutputPiped.close();
            } catch (IOException iOException) {
              iOException.printStackTrace();
            } 
          }
        });
  }
  
  public InputStream getInputStream() throws IOException {
    return this.inputStream;
  }
  
  public OutputStream getOutputStream() throws IOException {
    return this.outputStream;
  }
  
  public boolean isClosed() {
    return this.isConnect ^ true;
  }
  
  public static class Action {
    private static WeakReference<SocketDecorator.Communicator> communicatorWeakReference;
    
    public static Socket createSocket() {
      WeakReference<SocketDecorator.Communicator> weakReference = communicatorWeakReference;
      if (weakReference != null) {
        SocketDecorator.Communicator communicator = weakReference.get();
      } else {
        weakReference = null;
      } 
      return (weakReference == null) ? new Socket() : new SocketDecorator((SocketDecorator.Communicator)weakReference);
    }
    
    public static void setCommunicator(SocketDecorator.Communicator param1Communicator) {
      if (param1Communicator == null) {
        communicatorWeakReference = null;
      } else {
        communicatorWeakReference = new WeakReference<SocketDecorator.Communicator>(param1Communicator);
      } 
    }
  }
  
  public static interface Communicator {
    void connect(SocketDecorator.ConnectCallback param1ConnectCallback);
    
    void send(byte[] param1ArrayOfbyte);
    
    void setReceiveCallback(SocketDecorator.ReceiveCallback param1ReceiveCallback);
    
    void shutdown();
  }
  
  public static interface ConnectCallback {
    void onConnect();
    
    void onDisConnect();
  }
  
  public static interface ReceiveCallback {
    void onReceive(byte[] param1ArrayOfbyte);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/vilyever/socketclient/SocketDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */