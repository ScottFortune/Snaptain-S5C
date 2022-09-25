package com.vilyever.socketclient.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

public class SocketInputReader extends Reader {
  private InputStream inputStream;
  
  final SocketInputReader self = this;
  
  public SocketInputReader(InputStream paramInputStream) {
    super(paramInputStream);
    this.inputStream = paramInputStream;
  }
  
  public static void __i__checkOffsetAndCount(int paramInt1, int paramInt2, int paramInt3) {
    if ((paramInt2 | paramInt3) >= 0 && paramInt2 <= paramInt1 && paramInt1 - paramInt2 >= paramInt3)
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("arrayLength=");
    stringBuilder.append(paramInt1);
    stringBuilder.append("; offset=");
    stringBuilder.append(paramInt2);
    stringBuilder.append("; count=");
    stringBuilder.append(paramInt3);
    throw new ArrayIndexOutOfBoundsException(stringBuilder.toString());
  }
  
  private boolean __i__isOpen() {
    boolean bool;
    if (this.inputStream != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void close() throws IOException {
    synchronized (this.lock) {
      if (this.inputStream != null) {
        this.inputStream.close();
        this.inputStream = null;
      } 
      return;
    } 
  }
  
  public int read(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws IOException {
    throw new IOException("read() is not support for SocketInputReader, try readBytes().");
  }
  
  public byte[] readToData(byte[] paramArrayOfbyte, boolean paramBoolean) throws IOException {
    if (paramArrayOfbyte == null || paramArrayOfbyte.length <= 0)
      return null; 
    synchronized (this.lock) {
      boolean bool = __i__isOpen();
      if (bool)
        try {
          ArrayList<Byte> arrayList = new ArrayList();
          this();
          boolean bool1 = false;
          int i = 0;
          while (true) {
            int k = this.inputStream.read();
            if (-1 != k) {
              arrayList.add(Byte.valueOf((byte)k));
              if (k == (paramArrayOfbyte[i] & 0xFF)) {
                k = i + 1;
              } else {
                k = 0;
              } 
              i = k;
              if (k == paramArrayOfbyte.length)
                break; 
              continue;
            } 
            break;
          } 
          int j = arrayList.size();
          if (j == 0)
            return null; 
          i = arrayList.size();
          if (paramBoolean) {
            j = 0;
          } else {
            j = paramArrayOfbyte.length;
          } 
          i -= j;
          paramArrayOfbyte = new byte[i];
          Iterator<Byte> iterator = arrayList.iterator();
          for (j = bool1; j < i; j++)
            paramArrayOfbyte[j] = ((Byte)iterator.next()).byteValue(); 
          return paramArrayOfbyte;
        } catch (IOException iOException1) {
          return null;
        }  
      IOException iOException = new IOException();
      this("InputStreamReader is closed");
      throw iOException;
    } 
  }
  
  public byte[] readToLength(int paramInt) throws IOException {
    if (paramInt <= 0)
      return null; 
    synchronized (this.lock) {
      boolean bool = __i__isOpen();
      if (bool)
        try {
          int j;
          byte[] arrayOfByte = new byte[paramInt];
          int i = 0;
          while (true) {
            int k = this.inputStream.read(arrayOfByte, i, paramInt - i);
            j = i + k;
            if (k != -1) {
              i = j;
              if (j >= paramInt)
                break; 
              continue;
            } 
            break;
          } 
          if (j != paramInt)
            return null; 
          return arrayOfByte;
        } catch (IOException iOException1) {
          return null;
        }  
      IOException iOException = new IOException();
      this("InputStreamReader is closed");
      throw iOException;
    } 
  }
  
  public boolean ready() throws IOException {
    synchronized (this.lock) {
      InputStream inputStream = this.inputStream;
      if (inputStream != null) {
        boolean bool = false;
        try {
          int i = this.inputStream.available();
          if (i > 0)
            bool = true; 
          return bool;
        } catch (IOException iOException1) {
          return false;
        } 
      } 
      IOException iOException = new IOException();
      this("InputStreamReader is closed");
      throw iOException;
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/vilyever/socketclient/helper/SocketInputReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */