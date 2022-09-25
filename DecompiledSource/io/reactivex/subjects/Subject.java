package io.reactivex.subjects;

import io.reactivex.Observable;
import io.reactivex.Observer;

public abstract class Subject<T> extends Observable<T> implements Observer<T> {
  public abstract Throwable getThrowable();
  
  public abstract boolean hasComplete();
  
  public abstract boolean hasObservers();
  
  public abstract boolean hasThrowable();
  
  public final Subject<T> toSerialized() {
    return (this instanceof SerializedSubject) ? this : new SerializedSubject<T>(this);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/subjects/Subject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */