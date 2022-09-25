package io.reactivex.schedulers;

import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.TimeUnit;

public final class Timed<T> {
  final long time;
  
  final TimeUnit unit;
  
  final T value;
  
  public Timed(T paramT, long paramLong, TimeUnit paramTimeUnit) {
    this.value = paramT;
    this.time = paramLong;
    this.unit = (TimeUnit)ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof Timed;
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (bool) {
      paramObject = paramObject;
      bool2 = bool1;
      if (ObjectHelper.equals(this.value, ((Timed)paramObject).value)) {
        bool2 = bool1;
        if (this.time == ((Timed)paramObject).time) {
          bool2 = bool1;
          if (ObjectHelper.equals(this.unit, ((Timed)paramObject).unit))
            bool2 = true; 
        } 
      } 
    } 
    return bool2;
  }
  
  public int hashCode() {
    byte b;
    T t = this.value;
    if (t != null) {
      b = t.hashCode();
    } else {
      b = 0;
    } 
    long l = this.time;
    return (b * 31 + (int)(l ^ l >>> 31L)) * 31 + this.unit.hashCode();
  }
  
  public long time() {
    return this.time;
  }
  
  public long time(TimeUnit paramTimeUnit) {
    return paramTimeUnit.convert(this.time, this.unit);
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Timed[time=");
    stringBuilder.append(this.time);
    stringBuilder.append(", unit=");
    stringBuilder.append(this.unit);
    stringBuilder.append(", value=");
    stringBuilder.append(this.value);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
  
  public TimeUnit unit() {
    return this.unit;
  }
  
  public T value() {
    return this.value;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/schedulers/Timed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */