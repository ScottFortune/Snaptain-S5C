package com.bumptech.glide.disklrucache;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

class StrictLineReader implements Closeable {
  private static final byte CR = 13;
  
  private static final byte LF = 10;
  
  private byte[] buf;
  
  private final Charset charset;
  
  private int end;
  
  private final InputStream in;
  
  private int pos;
  
  public StrictLineReader(InputStream paramInputStream, int paramInt, Charset paramCharset) {
    if (paramInputStream != null && paramCharset != null) {
      if (paramInt >= 0) {
        if (paramCharset.equals(Util.US_ASCII)) {
          this.in = paramInputStream;
          this.charset = paramCharset;
          this.buf = new byte[paramInt];
          return;
        } 
        throw new IllegalArgumentException("Unsupported encoding");
      } 
      throw new IllegalArgumentException("capacity <= 0");
    } 
    throw new NullPointerException();
  }
  
  public StrictLineReader(InputStream paramInputStream, Charset paramCharset) {
    this(paramInputStream, 8192, paramCharset);
  }
  
  private void fillBuf() throws IOException {
    InputStream inputStream = this.in;
    byte[] arrayOfByte = this.buf;
    int i = inputStream.read(arrayOfByte, 0, arrayOfByte.length);
    if (i != -1) {
      this.pos = 0;
      this.end = i;
      return;
    } 
    throw new EOFException();
  }
  
  public void close() throws IOException {
    synchronized (this.in) {
      if (this.buf != null) {
        this.buf = null;
        this.in.close();
      } 
      return;
    } 
  }
  
  public boolean hasUnterminatedLine() {
    boolean bool;
    if (this.end == -1) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public String readLine() throws IOException {
    synchronized (this.in) {
      if (this.buf != null) {
        if (this.pos >= this.end)
          fillBuf(); 
        int i;
        for (i = this.pos; i != this.end; i++) {
          if (this.buf[i] == 10) {
            if (i != this.pos) {
              byte[] arrayOfByte = this.buf;
              int k = i - 1;
              if (arrayOfByte[k] == 13) {
                String str1 = new String();
                this(this.buf, this.pos, k - this.pos, this.charset.name());
                this.pos = i + 1;
                return str1;
              } 
            } 
            int j = i;
            String str = new String();
            this(this.buf, this.pos, j - this.pos, this.charset.name());
            this.pos = i + 1;
            return str;
          } 
        } 
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream() {
            public String toString() {
              int i;
              if (this.count > 0 && this.buf[this.count - 1] == 13) {
                i = this.count - 1;
              } else {
                i = this.count;
              } 
              try {
                return new String(this.buf, 0, i, StrictLineReader.this.charset.name());
              } catch (UnsupportedEncodingException unsupportedEncodingException) {
                throw new AssertionError(unsupportedEncodingException);
              } 
            }
          };
        super(this, this.end - this.pos + 80);
        while (true) {
          byteArrayOutputStream.write(this.buf, this.pos, this.end - this.pos);
          this.end = -1;
          fillBuf();
          for (i = this.pos; i != this.end; i++) {
            if (this.buf[i] == 10) {
              if (i != this.pos)
                byteArrayOutputStream.write(this.buf, this.pos, i - this.pos); 
              this.pos = i + 1;
              return byteArrayOutputStream.toString();
            } 
          } 
        } 
      } 
      IOException iOException = new IOException();
      this("LineReader is closed");
      throw iOException;
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/disklrucache/StrictLineReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */