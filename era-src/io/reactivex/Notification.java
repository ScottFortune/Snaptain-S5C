package io.reactivex;

import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.util.NotificationLite;

public final class Notification<T> {
  static final Notification<Object> COMPLETE = new Notification(null);
  
  final Object value;
  
  private Notification(Object paramObject) {
    this.value = paramObject;
  }
  
  public static <T> Notification<T> createOnComplete() {
    return (Notification)COMPLETE;
  }
  
  public static <T> Notification<T> createOnError(Throwable paramThrowable) {
    ObjectHelper.requireNonNull(paramThrowable, "error is null");
    return new Notification<T>(NotificationLite.error(paramThrowable));
  }
  
  public static <T> Notification<T> createOnNext(T paramT) {
    ObjectHelper.requireNonNull(paramT, "value is null");
    return new Notification<T>(paramT);
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof Notification) {
      paramObject = paramObject;
      return ObjectHelper.equals(this.value, ((Notification)paramObject).value);
    } 
    return false;
  }
  
  public Throwable getError() {
    Object object = this.value;
    return NotificationLite.isError(object) ? NotificationLite.getError(object) : null;
  }
  
  public T getValue() {
    Object object = this.value;
    return (T)((object != null && !NotificationLite.isError(object)) ? this.value : null);
  }
  
  public int hashCode() {
    boolean bool;
    Object object = this.value;
    if (object != null) {
      bool = object.hashCode();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isOnComplete() {
    boolean bool;
    if (this.value == null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isOnError() {
    return NotificationLite.isError(this.value);
  }
  
  public boolean isOnNext() {
    boolean bool;
    Object object = this.value;
    if (object != null && !NotificationLite.isError(object)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public String toString() {
    Object object = this.value;
    if (object == null)
      return "OnCompleteNotification"; 
    if (NotificationLite.isError(object)) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("OnErrorNotification[");
      stringBuilder1.append(NotificationLite.getError(object));
      stringBuilder1.append("]");
      return stringBuilder1.toString();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("OnNextNotification[");
    stringBuilder.append(this.value);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/Notification.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */