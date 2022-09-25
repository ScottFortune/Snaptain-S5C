package io.reactivex.exceptions;

public final class UndeliverableException extends IllegalStateException {
  private static final long serialVersionUID = 1644750035281290266L;
  
  public UndeliverableException(Throwable paramThrowable) {
    super(stringBuilder.toString(), paramThrowable);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/exceptions/UndeliverableException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */