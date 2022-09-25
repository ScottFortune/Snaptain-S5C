package com.sun.jna;

import java.lang.reflect.Field;

public class StructureWriteContext extends ToNativeContext {
  private Field field;
  
  private Structure struct;
  
  StructureWriteContext(Structure paramStructure, Field paramField) {
    this.struct = paramStructure;
    this.field = paramField;
  }
  
  public Field getField() {
    return this.field;
  }
  
  public Structure getStructure() {
    return this.struct;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/StructureWriteContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */