package io.reactivex.exceptions;

public final class OnErrorNotImplementedException extends RuntimeException {
  private static final long serialVersionUID = -6298857009889503852L;
  
  public OnErrorNotImplementedException(String paramString, Throwable paramThrowable) {
    super(paramString, paramThrowable);
  }
  
  public OnErrorNotImplementedException(Throwable paramThrowable) {
    this(stringBuilder.toString(), paramThrowable);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/exceptions/OnErrorNotImplementedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */