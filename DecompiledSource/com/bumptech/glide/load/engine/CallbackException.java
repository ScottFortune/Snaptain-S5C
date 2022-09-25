package com.bumptech.glide.load.engine;

final class CallbackException extends RuntimeException {
  private static final long serialVersionUID = -7530898992688511851L;
  
  CallbackException(Throwable paramThrowable) {
    super("Unexpected exception thrown by non-Glide code", paramThrowable);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/CallbackException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */