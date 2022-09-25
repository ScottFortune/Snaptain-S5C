package com.sun.jna;

public abstract class IntegerType extends Number implements NativeMapped {
  private static final long serialVersionUID = 1L;
  
  private Number number;
  
  private int size;
  
  private boolean unsigned;
  
  private long value;
  
  public IntegerType(int paramInt) {
    this(paramInt, 0L, false);
  }
  
  public IntegerType(int paramInt, long paramLong) {
    this(paramInt, paramLong, false);
  }
  
  public IntegerType(int paramInt, long paramLong, boolean paramBoolean) {
    this.size = paramInt;
    this.unsigned = paramBoolean;
    setValue(paramLong);
  }
  
  public IntegerType(int paramInt, boolean paramBoolean) {
    this(paramInt, 0L, paramBoolean);
  }
  
  public static final int compare(long paramLong1, long paramLong2) {
    return (paramLong1 == paramLong2) ? 0 : ((paramLong1 < paramLong2) ? -1 : 1);
  }
  
  public static int compare(IntegerType paramIntegerType, long paramLong) {
    return (paramIntegerType == null) ? 1 : compare(paramIntegerType.longValue(), paramLong);
  }
  
  public static <T extends IntegerType> int compare(T paramT1, T paramT2) {
    return (paramT1 == paramT2) ? 0 : ((paramT1 == null) ? 1 : ((paramT2 == null) ? -1 : compare(paramT1.longValue(), paramT2.longValue())));
  }
  
  public double doubleValue() {
    return this.number.doubleValue();
  }
  
  public boolean equals(Object paramObject) {
    boolean bool;
    if (paramObject instanceof IntegerType && this.number.equals(((IntegerType)paramObject).number)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public float floatValue() {
    return this.number.floatValue();
  }
  
  public Object fromNative(Object paramObject, FromNativeContext paramFromNativeContext) {
    long l;
    if (paramObject == null) {
      l = 0L;
    } else {
      l = ((Number)paramObject).longValue();
    } 
    paramObject = Klass.newInstance(getClass());
    paramObject.setValue(l);
    return paramObject;
  }
  
  public int hashCode() {
    return this.number.hashCode();
  }
  
  public int intValue() {
    return (int)this.value;
  }
  
  public long longValue() {
    return this.value;
  }
  
  public Class<?> nativeType() {
    return this.number.getClass();
  }
  
  public void setValue(long paramLong) {
    long l;
    this.value = paramLong;
    int i = this.size;
    if (i != 1) {
      if (i != 2) {
        if (i != 4) {
          if (i == 8) {
            this.number = Long.valueOf(paramLong);
            l = paramLong;
          } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported size: ");
            stringBuilder.append(this.size);
            throw new IllegalArgumentException(stringBuilder.toString());
          } 
        } else {
          if (this.unsigned)
            this.value = 0xFFFFFFFFL & paramLong; 
          i = (int)paramLong;
          l = i;
          this.number = Integer.valueOf(i);
        } 
      } else {
        if (this.unsigned)
          this.value = 0xFFFFL & paramLong; 
        short s = (short)(int)paramLong;
        l = s;
        this.number = Short.valueOf(s);
      } 
    } else {
      if (this.unsigned)
        this.value = 0xFFL & paramLong; 
      byte b = (byte)(int)paramLong;
      l = b;
      this.number = Byte.valueOf(b);
    } 
    i = this.size;
    if (i < 8) {
      long l1 = 0xFFFFFFFFFFFFFFFFL ^ (1L << i * 8) - 1L;
      if ((paramLong < 0L && l != paramLong) || (paramLong >= 0L && (l1 & paramLong) != 0L)) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Argument value 0x");
        stringBuilder.append(Long.toHexString(paramLong));
        stringBuilder.append(" exceeds native capacity (");
        stringBuilder.append(this.size);
        stringBuilder.append(" bytes) mask=0x");
        stringBuilder.append(Long.toHexString(l1));
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
    } 
  }
  
  public Object toNative() {
    return this.number;
  }
  
  public String toString() {
    return this.number.toString();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/IntegerType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */