package com.bumptech.glide.load;

import java.io.IOException;

public final class HttpException extends IOException {
  public static final int UNKNOWN = -1;
  
  private static final long serialVersionUID = 1L;
  
  private final int statusCode;
  
  public HttpException(int paramInt) {
    this(stringBuilder.toString(), paramInt);
  }
  
  public HttpException(String paramString) {
    this(paramString, -1);
  }
  
  public HttpException(String paramString, int paramInt) {
    this(paramString, paramInt, null);
  }
  
  public HttpException(String paramString, int paramInt, Throwable paramThrowable) {
    super(paramString, paramThrowable);
    this.statusCode = paramInt;
  }
  
  public int getStatusCode() {
    return this.statusCode;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/HttpException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */