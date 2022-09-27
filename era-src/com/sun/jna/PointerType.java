package com.sun.jna;

public abstract class PointerType implements NativeMapped {
  private Pointer pointer = Pointer.NULL;
  
  protected PointerType() {}
  
  protected PointerType(Pointer paramPointer) {}
  
  public boolean equals(Object paramObject) {
    boolean bool = true;
    if (paramObject == this)
      return true; 
    if (paramObject instanceof PointerType) {
      Pointer pointer = ((PointerType)paramObject).getPointer();
      paramObject = this.pointer;
      if (paramObject == null) {
        if (pointer != null)
          bool = false; 
        return bool;
      } 
      return paramObject.equals(pointer);
    } 
    return false;
  }
  
  public Object fromNative(Object paramObject, FromNativeContext paramFromNativeContext) {
    if (paramObject == null)
      return null; 
    PointerType pointerType = (PointerType)Klass.newInstance(getClass());
    pointerType.pointer = (Pointer)paramObject;
    return pointerType;
  }
  
  public Pointer getPointer() {
    return this.pointer;
  }
  
  public int hashCode() {
    boolean bool;
    Pointer pointer = this.pointer;
    if (pointer != null) {
      bool = pointer.hashCode();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public Class<?> nativeType() {
    return Pointer.class;
  }
  
  public void setPointer(Pointer paramPointer) {
    this.pointer = paramPointer;
  }
  
  public Object toNative() {
    return getPointer();
  }
  
  public String toString() {
    String str;
    if (this.pointer == null) {
      str = "NULL";
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.pointer.toString());
      stringBuilder.append(" (");
      stringBuilder.append(super.toString());
      stringBuilder.append(")");
      str = stringBuilder.toString();
    } 
    return str;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/PointerType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */